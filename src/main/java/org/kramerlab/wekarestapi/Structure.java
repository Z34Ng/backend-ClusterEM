
package org.kramerlab.wekarestapi;

public class Structure {
    public Object[] instancia;
    
    public Structure(Object[] vals, String n_cluster){
        this.instancia=vals;
        vals[0]=n_cluster;
    }
}
