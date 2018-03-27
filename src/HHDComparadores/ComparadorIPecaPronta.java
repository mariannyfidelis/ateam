package HHDComparadores;

import HHDBinPackingTree.BPTPedaco;
import HHDInterfaces.IPecaPronta;
import HHDInterfaces.IPedido;
import java.util.Comparator;

public class ComparadorIPecaPronta implements Comparator{

    @Override
    public int compare(Object arg1, Object arg2){
	
        //IPecaPronta peca0 = (IPecaPronta)arg0;
	//IPecaPronta peca1 = (IPecaPronta)arg1;

        BPTPedaco a1 = (BPTPedaco)arg1;
        BPTPedaco a2 = (BPTPedaco)arg2;
        
        IPedido peca1 = a1.getPedidoAtendido();
        IPedido peca2 = a2.getPedidoAtendido();
	
        return compare(peca1, peca2);
    }
	
    //@Override
    public int compare(IPecaPronta peca0, IPecaPronta peca1){
	
	float area1 = peca0.retorneDimensao().retorneArea();
	
	float area2 = peca1.retorneDimensao().retorneArea();
	
        if((area1 - area2) < 0)
    	      return -1;
	
        if((area1 - area2) > 0)
	      return 1;
	
        return 0;
    }
    
    public int compare(IPedido peca1, IPedido peca2){
	
	float area1 = peca1.retorneDimensao().retorneArea();
	
	float area2 = peca2.retorneDimensao().retorneArea();
	
        if((area1 - area2) < 0)
    	      return -1;
	
        if((area1 - area2) > 0)
	      return 1;
	
        return 0;
    }
}
