package Utilidades;

import java.util.Comparator;
import Heuristicas.Solucao;

public class ComparadorSolucoes implements Comparator<Solucao> {


    @Override
    public int compare(Solucao soluc1, Solucao soluc2) {


            double fitness1 = soluc1.getFitness();
            double fitness2 = soluc2.getFitness();

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
