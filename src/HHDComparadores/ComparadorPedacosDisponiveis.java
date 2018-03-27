package HHDComparadores;

import HHDInternal.PedacoDisponivel;
import HHDInternal.Ponto;
import java.util.Comparator;


public class ComparadorPedacosDisponiveis implements Comparator{


    @Override
    public int compare(Object arg0, Object arg1){
	
        if((PedacoDisponivel.class.isAssignableFrom(arg0.getClass())) &&
           (PedacoDisponivel.class.isAssignableFrom(arg1.getClass())))
           return compare((PedacoDisponivel)arg0, (PedacoDisponivel)arg1);
        else
           throw new ClassCastException();
    }
	
    public int compare(PedacoDisponivel arg0, PedacoDisponivel arg1){
	
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