## Web service to WEKA Algorithm Cluster EM

The service provides to machine learning algorithm EM from the WEKA library :

## Quick start

The REST service is based on the JAX-RS. 

To run a local environment for exploring the web service, execute the following(in path from project):

```
mvn clean package jetty:run
```

This will run the web service on a local Jetty instance which can be consumed by entering the following data with the post methodd:

```
http://localhost:8081/cluster/EM

{
"queryBD": "SELECT * FROM table",
"numFolds": 10,
"numKMeansRuns": 10,
"maximumNumberOfClusters": -1,
"numClusters": -1,
"maxIterations": 100
}
