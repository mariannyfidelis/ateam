package j_HeuristicaArvoreNAria;

import java.util.Comparator;


public class ComparadorFAV implements Comparator<Bin>{

    @Override
    public int compare(Bin b1, Bin b2) {
        
        double fav1 = b1.getFav();
        double fav2 = b2.getFav();
        
        if((fav1 - fav2) < 0){
            return -1;
        }
        else if((fav1 - fav2) > 0){
            return 1;
        }
        
        else return 0;
    }
    
}