package HHDBinPackingTree;

import java.util.Comparator;

public class ComparadorBinPackTree implements Comparator<BinPackTree>{
    
    @Override
    public int compare(BinPackTree bpt1, BinPackTree bpt2) {
        
        IBPTNode raiz1 = bpt1.getRaiz();
        IBPTNode raiz2 = bpt2.getRaiz();
        
        float aproveitamento1 = ((BPTPedaco)raiz1).getAproveitamento();
        float aproveitamento2 = ((BPTPedaco)raiz2).getAproveitamento();
        
        if(aproveitamento1 > aproveitamento2){
            return 1;
        }
        else if(aproveitamento1 < aproveitamento2){
            return -1;
        }
        else
            return 0;
    }
}