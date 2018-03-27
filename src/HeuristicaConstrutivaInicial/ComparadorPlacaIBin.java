package HeuristicaConstrutivaInicial;

import HHDInterfaces.IBin;
import java.util.Comparator;

class ComparadorPlacaIBin implements Comparator<IBin> {

    
    @Override
    public int compare(IBin o1, IBin o2) {
        
        double fav1 = o1.getFav();
        double fav2 = o2.getFav();
        
        if((fav1 - fav2) < 0){
            
            return -1;
        }
        else if((fav1 - fav2) > 0){
            
            return 1;
        }
        else if((fav1 - fav2 == 0)){
            
            double sobra1 = o1.getSobra();
            double sobra2 = o2.getSobra();
            
            if((sobra1 - sobra2) < 0){
                
                return 1; //Inverteu quero que os primeiros tenha a maior sobra
            }
            else if((sobra1 - sobra2) > 0){
            
                return -1;
            }
            else return 0;
        } 
      else return 0;
    }
    
}
