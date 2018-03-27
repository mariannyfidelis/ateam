package j_HeuristicaArvoreNAria;

import HHDInterfaces.IPedido;
import java.util.Comparator;

public class ComparadorAreas implements Comparator<Pedidos>{

	
	public int compare(Pedidos p1, Pedidos p2){
		
		float area1 = p1.retornaArea();
		float area2 = p2.retornaArea();
		
		if((area1 - area2) < 0){
			return -1;
		}
		else if((area1 - area2) > 0){
			return 1;
		}
		else if((area1 - area2) == 0){

			float larg1 = p1.retornaLargura();
			float larg2 = p2.retornaLargura();
			
			
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
	
	public int comp_altura(Pedidos p1, Pedidos p2){
		
		float alt1 = p1.retornaAltura();
		float alt2 = p2.retornaAltura();
		
		if((alt1 - alt2) < 0){
				return -1;
		}
		else if((alt1 - alt2) > 0){
				return 1;
		}
		else{
			return 0;
		}
	}

    
    public int compare(IPedido o1, IPedido o2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
