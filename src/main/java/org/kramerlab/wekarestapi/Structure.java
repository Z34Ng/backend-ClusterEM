
package org.kramerlab.wekarestapi;

public class Structure {
    public Object[] instancia;
    
    public Structure(Object[] vals, String n_cluster){
        instancia = new Object[vals.length];
        instancia[0]=n_cluster;
        System.arraycopy(vals, 0, instancia, 1, vals.length-1); 
    }
}
