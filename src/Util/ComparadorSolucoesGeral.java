/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import HeuristicaConstrutivaInicial.Solucao;
import java.util.Comparator;

/**
 *
 * @author marianny
 */
public class ComparadorSolucoesGeral implements Comparator<HeuristicaConstrutivaInicial.Solucao> {

    @Override
    public int compare(Solucao sol1, Solucao sol2) {
        
        double fitness1 = sol1.getFAV2();
        double fitness2 = sol2.getFAV2();
        
        if((fitness1 - fitness2) < 0){
			return -1;
		}
		else if((fitness1 - fitness2) > 0){
			return 1;
		}
		else if((fitness1 - fitness2) == 0){
		
		    return 0;
	    }
		else
			return 0;
    }
    
}
