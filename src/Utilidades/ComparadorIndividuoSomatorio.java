package Utilidades;

import Heuristicas.Individuo;
import java.util.Comparator;

public class ComparadorIndividuoSomatorio implements Comparator<Individuo> {
    
    @Override
     public int compare(Individuo ind1, Individuo ind2) {

        double somatorio1 = ind1.getSomatorioItens();
        double somatorio2 = ind2.getSomatorioItens();

        if((somatorio1 - somatorio2) < 0){
            return -1;
        }
        else if((somatorio1 - somatorio2) > 0){
            return 1;
        }
        else if((somatorio1 - somatorio2) == 0){

            return 0;
        }
        else{

            return 0;
        }

    }
}
