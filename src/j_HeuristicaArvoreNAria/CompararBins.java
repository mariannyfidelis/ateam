package j_HeuristicaArvoreNAria;

import java.util.Comparator;

public class CompararBins implements Comparator<Bin> {

    @Override
    public int compare(Bin bin1, Bin bin2) {
        
        float fav1, fav2;
        No raiz1 = bin1.root();
        No raiz2 = bin2.root();
        
        fav1 = raiz1.getMaxima_area_livre();
        fav2 = raiz1.getMaxima_area_livre();
        
        if((fav1 - fav2) < 0){
            return -1;
        }
        else if((fav1 - fav2) > 0){
            return 1;
        }
        
        else return 0;
    }

    
}
