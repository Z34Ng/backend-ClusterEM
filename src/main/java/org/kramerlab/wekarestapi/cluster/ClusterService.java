package org.kramerlab.wekarestapi.cluster;

import javax.ws.rs.core.Response;
import java.util.HashMap;


@SuppressWarnings("rawtypes")
public abstract class ClusterService {
    public abstract Response clustererPost( String clusterer, HashMap params) throws Exception;
    
}
