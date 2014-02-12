package Util;

import java.util.Comparator;
import Heuristicas.Individuo;

public class ComparadorIndividuos implements Comparator<Individuo> {


    @Override
    public int compare(Individuo ind1, Individuo ind2) {

        double fitness1 = ind1.getFitness();
        double fitness2 = ind2.getFitness();

        if((fitness1 - fitness2) < 0){
                return -1;
        }
        else if((fitness1 - fitness2) > 0){
                return 1;
        }
        else if((fitness1 - fitness2) == 0){

        return 0;
    }
        else{

           return 0;
        }

    }
    public int compare(int aux, Individuo ind1, Individuo ind2) {

        int size1 = ind1.getSize();
        int size2 = ind2.getSize();

        if((size1 - size2) < 0){
                return -1;
        }
        else if((size1 - size2) > 0){
                return 1;
        }
        else if((size1 - size2) == 0){

            return 0;
        }
        else{

            return 0;
        }
    }
    
}