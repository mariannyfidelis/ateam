/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilidades;

import java.util.Comparator;
import HHDInternal.SolucaoHeuristica;


/**
 *
 * @author marianny
 */
public class ComparadorSolucoesHeuristicas implements Comparator<SolucaoHeuristica> {

    @Override
    public int compare(SolucaoHeuristica sol_1, SolucaoHeuristica sol_2) {
     
        double fitness1 = sol_1.getFAV2();
        double fitness2 = sol_2.getFAV2();
        
        if((fitness1 - fitness2) < 0){
            return -1;
        }
        else if((fitness1 - fitness2) > 0){
            return 1;
        }
        else if((fitness1 - fitness2) == 0){
            return 0;
        }else
            return 0;
    }
}
 /*   @Override
    public int compare(SolucaoHeuristica sol_1, SolucaoHeuristica sol_2) {
        
        int tam1 = sol_1.getQtd();
        int tam2 = sol_1.getQtd();
        
        if((tam1 - tam2) < 0){
            return 1;
        }
        else if((tam1 - tam2) > 0){
            return -1;
        }
        else if((tam1 - tam2) == 0){
            
            int qtCort1 = sol_1.getQtdLinhasCorte();
            int qtCort2 = sol_2.getQtdLinhasCorte();
            
            if((qtCort1 - qtCort2) < 0){
                return 1;
            }
            else if((qtCort1 - qtCort2) > 0){
                return -1;
            }
            else if((qtCort1 - qtCort2) == 0){
            
                double fitness1 = sol_1.getFAV2();
                double fitness2 = sol_2.getFAV2();
        
                if((fitness1 - fitness2) < 0){
                    return -1;
                }
                else if((fitness1 - fitness2) > 0){
                    return 1;
                }
                else if((fitness1 - fitness2) == 0){
                    return 0;
                }else
                     return 0;
            }else
                 return 0;
        }else
             return 0;
       
    }
    
} */
