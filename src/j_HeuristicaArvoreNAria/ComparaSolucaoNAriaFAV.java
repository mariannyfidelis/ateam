package j_HeuristicaArvoreNAria;

import java.util.Comparator;

public class ComparaSolucaoNAriaFAV implements Comparator<SolucaoNAria>{

    @Override
    public int compare(SolucaoNAria sol_1, SolucaoNAria sol_2) {
        
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
        }
        else
            return 0;
    }
    
//    @Override
//    public int compare(SolucaoNAria sNA1, SolucaoNAria sNA2) {
//        
//        int numB1 = sNA1.retornaSolucao().size();
//        int numB2 = sNA2.retornaSolucao().size();
//        
//        if((numB1 - numB2) < 0){
//            
//            return -1;
//        }
//        else if((numB1 - numB2) > 0){
//            
//            return 1;
//        }
//        else if((numB1 - numB2 == 0)){
//            
//            double fav1 = sNA1.getFAV();
//            double fav2 = sNA2.getFAV();
//            
//            if((fav1 - fav2) < 0){
//                
//                return -1;
//            }
//            else if((fav1 - fav2) > 0){
//            
//                return 1;
//            }
//            else{
//                
//                double fav21 = sNA1.getFAV2();
//                double fav22 = sNA2.getFAV2();
//
//                if((fav21 - fav22) < 0){
//
//                    return -1;
//                }
//                else if((fav21 - fav22) > 0){
//
//                    return 1;
//                }
//                else return 0;
//            }
//        }
//        else return 0;
//    }

}
