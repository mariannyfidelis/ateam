package HHDInternal;

import HHDInterfaces.IPedido;
import HHDInterfaces.IListaPedidos;
import java.util.Iterator;
import java.util.LinkedList;

public class ListPedidos implements IListaPedidos{

      LinkedList<IPedido> list_pedidos;

      
      public ListPedidos(){
      
            list_pedidos = new LinkedList<IPedido>();
      }

      
      public Iterator retorneIteratorPedidos(LinkedList<IPedido> list_pedidos){
      
         Iterator<IPedido> iterator = list_pedidos.iterator();
         
         return iterator;
      }      
     
      public boolean hasNext(){
	
	 if(list_pedidos.iterator().hasNext()){
	      
	       return true;
	 }
	     
	 return false;
      } 
      
  
      public IPedido next(){
      
          if(list_pedidos.iterator().hasNext()){
            
               return list_pedidos.iterator().next();//erator().next();
          }
          
          return null;
      }
	
	
      public void reset(){}

}