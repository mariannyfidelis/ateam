package Util;

import java.util.Comparator;

public class ComparadorAreaItem implements Comparator<Integer>{


	private Pedidos[] vetor_info;
	
	
	public Pedidos[] getVetorInfo(){
		
		return vetor_info;
	}
	
	public void setVetorInfo(Pedidos[] vetor_inf){
		
		vetor_info = vetor_inf;		
	}
	
	public int compare(Integer d1, Integer d2) {
		
		double areaIt1, areaIt2;
		
		
		areaIt1 = getVetorInfo()[d1].retornaArea();
		areaIt2 = getVetorInfo()[d2].retornaArea();
		
		if((areaIt1 - areaIt2) < 0){
			return -1;
		}
		else if((areaIt1 - areaIt2) > 0){
			return 1;
		}
		else if((areaIt1 - areaIt2) == 0){
		
		return 0;
	    }
		else{
			
			return 0;
		}
	}
	
}
