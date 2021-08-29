package org.kramerlab.wekarestapi.data;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;

import org.kramerlab.wekarestapi.Evaluation;
import org.kramerlab.wekarestapi.InfoCluster;
import org.kramerlab.wekarestapi.Structure;

import weka.core.Instances;
import weka.clusterers.*;
import weka.core.Utils;
import weka.experiment.InstanceQuery;
import weka.gui.explorer.ClustererAssignmentsPlotInstances;
import weka.gui.sql.ResultSetHelper;
import weka.gui.visualize.JComponentWriter;
import weka.gui.visualize.PNGWriter;
import weka.gui.visualize.Plot2D;

@SuppressWarnings("rawtypes")
public class DatasetService {             
    private static Object[][] resultData; //store values of query
    private static String[] nameColums; //store columns name     

    public static Object[][] getDataFromQuery(){
        return resultData;
    }
    
    public static String[] getColumnsFromQuery(){
        return nameColums;
    }       
    
    /**
    * @param query to execute in the database
    * @return the instances of the query result 
    */
    public static Instances getInstancesFromQuery(String query){
        Instances instances = null;        
        try {
            Dao sqlDao = new Dao();
            InstanceQuery instQ = sqlDao.setConnectionDB();            
            instQ.setCustomPropsFile(new File("src/main/resources/DatabaseUtils.props"));                                   
            
            instQ.execute(query); 
            ResultSet rs = instQ.getResultSet();
            ResultSetHelper rsh = new ResultSetHelper(rs);
            resultData = rsh.getCells(); //get values of query
            nameColums = rsh.getColumnNames();//get columns name                                                            
            
            instances = instQ.retrieveInstances(query);            
        }catch (Exception ex) {
            Logger.getLogger(DatasetService.class.getName()).log(Level.SEVERE, null, ex);
        }        
        return instances;
    }           
    
    /**
    * @param inst the instanes
    * @param clusXinst the cluster number to which an instance is associated
    * @return array of instances with its associated cluster
    */
    public static Structure[] getArrayStructure(Instances inst, double[] clusXinst){                   
        Object[] aux = new Object[inst.numAttributes()];                
        Structure[] obj = new Structure[inst.numInstances()];   
               
        for(int i=0; i<inst.numInstances(); i++){
            //System.arraycopy(resultData[i], 0, aux, 0, inst.numAttributes());
            obj[i]=new Structure(Arrays.copyOf(resultData[i],resultData[i].length+1),Utils.doubleToString(clusXinst[i]+1.0,0));
        }
        return obj;
    }
    
    /**
    * @param inst instancias
    * @param clusXinst the cluster number to which an instance is associated
    * @param nCluster total number of clusters
    * @return information array for each cluster
    */
    public static InfoCluster[] getInfoCluster(Instances inst, double[] clusXinst, int nCluster){
        InfoCluster[] info = new InfoCluster[nCluster];
        int[] stats = clusterStats(nCluster, clusXinst);        
        
        for(int i=0; i<nCluster; i++){
            double aux = Double.valueOf(stats[i])/Double.valueOf(inst.numInstances())*100;
            info[i]=new InfoCluster(stats[i],(Math.round(aux*100.0)/100.0),i+1);
        } 
        
        InfoCluster aux;
        for (int i = 0; i < info.length-1; i++) {
            for (int j = 0; j < info.length - i - 1; j++) {
                if (info[j + 1].getPercentage() > info[j].getPercentage()) {
                    aux = info[j + 1];
                    info[j + 1] = info[j];
                    info[j] = aux;
                }
            }
        }
        
        return info;
    }    
    
    /**
    * @param nCluster total number of clusters
    * @param clusXinst the cluster number to which an instance is associated
    * @return total number of instances in each cluster
    */
    private static int[] clusterStats(int nCluster, double[] clusXinst){                                 
        int[] total = new int[nCluster];
        for(int i=0; i<nCluster; i++)
            total[i]=getAssignt(clusXinst,i);               
        
        return total;
    }
    /**
    * @param clusXinst the cluster number to which an instance is associated
    * @param idCluster given cluster
    * @return total number instances in a given cluster
    */
    private static int getAssignt(double[] clusXinst, int idCluster){
        int total=0;
        for(int i=0; i<clusXinst.length; i++)
            if(clusXinst[i]==idCluster)
                total++;
        
        return total;
    }
    
    /**
    * @param nCluster total number of clusters
    * @param clusXinst the cluster number to which an instance is associated
    * @param inst the instances 
    * @param base64 the graph converted to base64
    * @return 
    */
    public static Evaluation buildEvalution (int nCluster, double[] clusXinst, Instances inst, String base64){        
        
        Structure[] valInstances = getArrayStructure(inst,clusXinst);                         
        InfoCluster[] clusterStats = getInfoCluster(inst, clusXinst, nCluster);
        String[] attributes = getColumnsFromQuery();
        
        return new Evaluation(valInstances,attributes,clusterStats,base64);
    }
    
    /**
    * @param clust the cluster
    * @param inst the instances
    * @param eval the evaluated cluster
    * @return object for generating plottable cluster assignments
    */
    private static ClustererAssignmentsPlotInstances setPlotInstances
        (Clusterer clust, Instances inst, ClusterEvaluation eval){
        
        ClustererAssignmentsPlotInstances capi = new ClustererAssignmentsPlotInstances(); 
        capi.setInstances(inst);
        capi.setClusterer(clust);
        capi.setClusterEvaluation(eval);
        capi.setUp();                                
        
        return capi;
    }
    /**
     * 
     * @param capi for generating plottable cluster assignments
     * @param f graph file 
     */
    private static void generateGraphic(ClustererAssignmentsPlotInstances capi, File f){        
        Plot2D p2 = new Plot2D();
        
        try {  
            p2.setMasterPlot(capi.getPlotData("myplot"));            
            p2.setSize(1200, 800);                 
            p2.setXindex(1);
            p2.setYindex(4);
            p2.setCindex(nameColums.length+1);     
            
            JComponentWriter.toOutput(new PNGWriter(), p2 , f);
        } catch (Exception ex) {
            Logger.getLogger(DatasetService.class.getName()).log(Level.SEVERE, null, ex);
        }                                                     
    }
    /**
     * 
     * @param f graph file 
     * @return the graph converted to base64
     * @throws IOException when there is no file
     */
    public static String GraphictoBase64(File f) throws IOException{          
        
        byte[] fileContent = FileUtils.readFileToByteArray(f);
        String encodedString = Base64.getEncoder().encodeToString(fileContent);
        
        return encodedString;
    }
    /**
     * 
     * @param clust the cluster
     * @param inst the instances
     * @param eval the evaluated cluster
     * @return the graph
     */
    public static File buildGraphic (Clusterer clust, Instances inst, ClusterEvaluation eval){
        String filePath = "src/main/resources/graph.png";
        File img = new File(filePath);
        
        ClustererAssignmentsPlotInstances capi = setPlotInstances(clust,inst,eval);              
        generateGraphic(capi,img);
        
        return img;
    }
    
}
