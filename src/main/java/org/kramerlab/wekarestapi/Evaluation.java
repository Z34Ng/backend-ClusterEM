package org.kramerlab.wekarestapi;

public class Evaluation {
    Structure[] valsInstances;
    String[] attributes;
    InfoCluster[] infoCluster;
    String graph;
    Object[][] AttSummaryPanel;

    public Evaluation(Structure[] valsInstances, String[] attributes, InfoCluster[] infClus, String graph, Object[][] AttSummaryPanel) {
        this.valsInstances = valsInstances;
        this.attributes = attributes;        
        this.infoCluster = infClus;
        this.AttSummaryPanel = AttSummaryPanel;
        this.graph=graph;        
    }

    public Structure[] getValsInstances() {
        return valsInstances;
    }

    public String[] getAttributes() {
        return attributes;
    }

    public InfoCluster[] getInfClus() {
        return infoCluster;
    }

    public void setValsInstances(Structure[] valsInstances) {
        this.valsInstances = valsInstances;
    }

    public void setAttributes(String[] attributes) {
        this.attributes = attributes;
    }

    public void setInfClus(InfoCluster[] infClus) {
        this.infoCluster = infClus;
    }
    
    
}
