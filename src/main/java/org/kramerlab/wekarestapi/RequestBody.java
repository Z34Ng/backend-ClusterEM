
package org.kramerlab.wekarestapi;

public class RequestBody {
    String queryBD; 
    int numFolds;
    int numKMeansRuns;
    int maximumNumberOfClusters;
    int numClusters;
    int maxIterations;

    public void setQueryBD(String queryBD) {
        this.queryBD = queryBD;
    }

    public void setNumFolds(int numFolds) {
        this.numFolds = numFolds;
    }

    public void setNumKMeansRuns(int numKMeansRuns) {
        this.numKMeansRuns = numKMeansRuns;
    }

    public void setMaximumNumberOfClusters(int maximumNumberOfClusters) {
        this.maximumNumberOfClusters = maximumNumberOfClusters;
    }

    public void setNumClusters(int numClusters) {
        this.numClusters = numClusters;
    }

    public void setMaxIterations(int maxIterations) {
        this.maxIterations = maxIterations;
    }

    public String getQueryBD() {
        return queryBD;
    }

    public int getNumFolds() {
        return numFolds;
    }

    public int getNumKMeansRuns() {
        return numKMeansRuns;
    }

    public int getMaximumNumberOfClusters() {
        return maximumNumberOfClusters;
    }

    public int getNumClusters() {
        return numClusters;
    }

    public int getMaxIterations() {
        return maxIterations;
    }       
}
