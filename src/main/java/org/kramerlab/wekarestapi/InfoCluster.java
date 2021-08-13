
package org.kramerlab.wekarestapi;

public class InfoCluster {
    int id;
    int numInstances;
    double percentage;    
    
    public InfoCluster(int numInstances, double percentage, int id){
        this.numInstances=numInstances;
        this.percentage=percentage;
        this.id=id;
    }
    
    public int getNumInstances() {
        return numInstances;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setNumInstances(int numInstances) {
        this.numInstances = numInstances;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }        
}
