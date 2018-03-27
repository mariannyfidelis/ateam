/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package HHDInterfaces;

import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author marianny
 */
public class TestantoILista extends LinkedList<IPedido> implements IListaPedidos{

    LinkedList<IPedido> listaPedidos = null;
    
    public void TestantoILista(LinkedList<IPedido> listaPedidos) {
      
        listaPedidos = new LinkedList<IPedido>(listaPedidos);
    }
    
    @Override
    public IPedido next() {
        
        IPedido ipedido = null;
        Iterator<IPedido> it = listaPedidos.iterator();
         
        if(it.hasNext()){
           ipedido = it.next();
        }
        
        return ipedido;
    }

    @Override
    public boolean hasNext() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
}
