package HHDComparadores;

import HHDInternal.Pedidos;
import java.util.Comparator;

public class ComparadorAreas implements Comparator<Pedidos>{
    
    
    /*public int compare(Object arg0, Object arg1){
	
        if(IPedido.class.isAssignableFrom(arg0.getClass()) &&
           IPedido.class.isAssignableFrom(arg1.getClass()))
           return ((IPedido) arg0).retorneDimensao().compareAreas(((IPedido) arg1).retorneDimensao());
        else{
           //throw new ClassCastException(); 
           
           return -1;
        }
                
    }*/
    
    public int compare(Pedidos p1, Pedidos p2){
		
		float area1 = p1.retorneDimensao().retorneArea();
		float area2 = p2.retorneDimensao().retorneArea();
		
		if((area1 - area2) < 0){
			return -1;
		}
		else if((area1 - area2) > 0){
			return 1;
		}
		else if((area1 - area2) == 0){

			float larg1 = p1.retorneDimensao().retorneBase();
			float larg2 = p2.retorneDimensao().retorneBase();
			
			
			if((larg1 - larg2) < 0){
				return -1;
			}
			else if((larg1 - larg2) > 0){
				return 1;
			}
			else{
				
				return 0;
			}
			
		}
		else return 0;
	}
}
