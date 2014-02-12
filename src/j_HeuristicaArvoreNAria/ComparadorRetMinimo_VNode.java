package j_HeuristicaArvoreNAria;

import java.util.Comparator;

public class ComparadorRetMinimo_VNode implements Comparator<No> {

	
	public int compare(No filho1, No filho2) {
		
		float largura_retMinimo1 = filho1.getRetanguloMinimo().getW_p();
		float largura_retMinimo2 = filho2.getRetanguloMinimo().getW_p();
		
		
		if((largura_retMinimo1 - largura_retMinimo2) < 0){
			return -1;
		}
		else if((largura_retMinimo1 - largura_retMinimo2) > 0){
			return 1;
		}
		else if(largura_retMinimo1 - largura_retMinimo2 == 0){
			
			float altura_retMinimo1 = filho1.getRetanguloMinimo().getH_p();
			float altura_retMinimo2 = filho2.getRetanguloMinimo().getH_p();
			
			if((altura_retMinimo1 - altura_retMinimo2) < 0){
				return -1;
			}
			else if((altura_retMinimo1 - altura_retMinimo2) > 0){
				return 1;
			}
			
			else return 0;
		}		
	
		else return 0;
	}
}
