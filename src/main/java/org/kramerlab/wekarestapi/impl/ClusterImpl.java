package org.kramerlab.wekarestapi.impl;

import com.google.gson.Gson;
import java.io.File;
import java.util.HashMap;

import weka.clusterers.*;
import weka.core.Instances;
import weka.filters.unsupervised.attribute.StringToNominal;

import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.kramerlab.wekarestapi.WekaOptionHelper;
import org.kramerlab.wekarestapi.cluster.ClusterService;
import org.kramerlab.wekarestapi.data.DatasetService;
import org.kramerlab.wekarestapi.Evaluation;

@SuppressWarnings("rawtypes")
public class ClusterImpl extends ClusterService {
    @Override
    @Produces("text/plain")
    public Response clustererPost( String clustererName, HashMap params) throws Exception {    
        String query = params.get("queryBD").toString();                                        
        Instances instances = DatasetService.getInstancesFromQuery(query);              
        
        String[] options;
        try {
            options = WekaOptionHelper.getClustererOptions(clustererName, params);
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
        
        AbstractClusterer clusterer;
        clusterer = getClusterer(clustererName);
        clusterer.setOptions(options);

        StringToNominal s2n = new StringToNominal();
        s2n.setAttributeRange("first-last");
        s2n.setInputFormat(instances);
        
        Instances newData = new Instances(StringToNominal.useFilter(instances, s2n));        
        clusterer.buildClusterer(newData);

        // evaluate clusterer
        ClusterEvaluation evalCluster = new ClusterEvaluation();
        evalCluster.setClusterer(clusterer);
        evalCluster.evaluateClusterer(newData); 
        
        int n_Cluster = clusterer.numberOfClusters();
        double[] instanceStats = evalCluster.getClusterAssignments(); 
        File img = DatasetService.buildGraphic(clusterer,instances,evalCluster);
        String imgb64 = DatasetService.GraphictoBase64(img);              
                
        Evaluation eval = DatasetService.buildEvalution(n_Cluster, instanceStats, instances,imgb64);    
        
        return Response.ok(new Gson().toJson(eval)).build();
    }

    AbstractClusterer getClusterer(String clustererName){
        AbstractClusterer clusterer = null;
        try {
            switch (clustererName) {
                case "EM":
                    clusterer = new EM();
                    break;
                case "HierarchicalClusterer":
                    clusterer = new HierarchicalClusterer();
                    break;
                case "SimpleKMeans":
                    clusterer = new SimpleKMeans();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return clusterer;
    }

}
