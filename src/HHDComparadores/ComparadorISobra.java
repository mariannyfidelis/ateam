package HHDComparadores;

import HHDInterfaces.ISobra;
import HHDInternal.Ponto;
import java.util.Comparator;

public class ComparadorISobra implements Comparator {

    public int compare(Object arg0, Object arg1){
	
	if((ISobra.class.isAssignableFrom(arg0.getClass())) &&  (ISobra.class.isAssignableFrom(arg1.getClass())))
	   return compare((ISobra)arg0, (ISobra)arg1);
	else
	   throw new ClassCastException("Classe " + arg0.getClass().getName() + " ou " + arg1.getClass().getModifiers() +
		   		                       "nao implementam interface " + ISobra.class.getName());
    }
	
    public int compare(ISobra arg0, ISobra arg1){
	
        float area1 = Ponto.calculaAreaPontos(arg0.getPontoInferiorEsquerdo(),
        arg0.getPontoSuperiorDireito());

        float area2 = Ponto.calculaAreaPontos(arg1.getPontoInferiorEsquerdo(),
        arg1.getPontoSuperiorDireito());

        if((area1 - area2) < 0)
                return -1;

        if((area1 - area2) > 0)
                return 1;

        return 0;
    }
}
