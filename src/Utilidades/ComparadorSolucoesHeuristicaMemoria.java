package Utilidades;

import HHDInternal.SolucaoHeuristica;
import java.util.Comparator;

public class ComparadorSolucoesHeuristicaMemoria implements Comparator<SolucaoHeuristica> {

    @Override
    public int compare(SolucaoHeuristica sol_1, SolucaoHeuristica sol_2) {
        
        int numChapas1 = sol_1.getQtd();
        int numChapas2 = sol_2.getQtd();
        
        if((numChapas1 - numChapas2) < 0){
        
            return -1;
        }
        else if((numChapas1 - numChapas2) > 0){
            
            return 1;
        }
        else if((numChapas1 - numChapas2) == 0){
            
            int numLinhasCorte_Chapa1 = sol_1.getQtdLinhasCorte();
            int numLinhasCorte_Chapa2 = sol_2.getQtdLinhasCorte();       
                        
            if((numLinhasCorte_Chapa1 - numLinhasCorte_Chapa2) < 0){
            
                return -1;
            }
            else if((numLinhasCorte_Chapa1 - numLinhasCorte_Chapa2) > 0){
                
                return 1;
            }
            else if((numLinhasCorte_Chapa1 - numLinhasCorte_Chapa2) == 0){
                
                double qtdSobra_Chapa1 = sol_1.getSomatorioSobras();
                double qtdSobra_Chapa2 = sol_2.getSomatorioSobras();
        
                if((qtdSobra_Chapa1 - qtdSobra_Chapa2) < 0){
                
                    return -1;
                }
                else if((qtdSobra_Chapa1 - qtdSobra_Chapa2) > 0){
                    
                    return 1;
		}
                else if((qtdSobra_Chapa1 - qtdSobra_Chapa2) == 0){
                    
                    double mediaSobra1 = sol_1.getMediaSobras();
                    double mediaSobra2 = sol_2.getMediaSobras();
                    
                    if((mediaSobra1 - mediaSobra2) < 0){
                        
                        return -1;
                    }
                    else if((mediaSobra1 - mediaSobra2) > 0){
                        
                        return 1;
                    }
                    else if((mediaSobra1 - mediaSobra2) == 0){
                        
                        return 0;
                    }
                    //else
                      //  return 0;
                }
                else
                    return 0;
		                
                return 0;
	    }
            else
                return 0;
        }
        else
            return 0;    
    }
}