package org.kramerlab.wekarestapi.cluster;

import org.kramerlab.wekarestapi.RequestBody;

import org.kramerlab.wekarestapi.annotations.GroupedApiResponsesOk;
import org.kramerlab.wekarestapi.factories.ClusterFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.HashMap;

//@Api(description = "The WEKA Clusterer API")
@Path("/cluster")
public class Cluster {
    
    private final ClusterService delegate;    
    
    public Cluster(@Context ServletConfig servletContext) {
        ClusterService delegate = null;

        if (servletContext != null) {
            String implClass = servletContext.getInitParameter("Cluster.implementation");
            if (implClass != null && !"".equals(implClass.trim())) {
                try {
                    delegate = (ClusterService) Class.forName(implClass).newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        
        if (delegate == null) {
            delegate = ClusterFactory.getCluster();
        }
        this.delegate = delegate;
    }

    @Context ServletContext servletContext;
    @POST
    @Path("/EM")
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ "application/json" })
    @GroupedApiResponsesOk
    public Response clusterEMPost(RequestBody rB) throws Exception {
            HashMap<String, Object> params = new HashMap<>();
            params.put("queryBD", rB.getQueryBD());
            params.put("numFolds", rB.getNumFolds());
            params.put("numKMeansRuns", rB.getNumKMeansRuns());
            params.put("maximumNumberOfClusters", rB.getMaximumNumberOfClusters());
            params.put("numClusters", rB.getNumClusters());
            params.put("maxIterations", rB.getMaxIterations());

            return delegate.clustererPost("EM", params);
    }             
}
