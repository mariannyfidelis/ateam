package HHDBinPackingTree;

import HHDInterfaces.IDimensao2d;
import HHDInterfaces.IPecaPronta;
import HHDInternal.Ponto;

public class IBPTNodeITestePecaWrapper implements IPecaPronta { 

    private IBPTNode noEmpacotado;
    boolean cortado;

    public IBPTNodeITestePecaWrapper(IBPTNode pedaco){

            noEmpacotado = pedaco;
    }

    @Override
    public void retireUmaPeca(){

        cortado = true;
    }

    @Override
    public int getPecasDisponiveis(){

        if(cortado)
                return 0;

        return 1;
    }

    @Override
    public IDimensao2d retorneDimensao(){

        return noEmpacotado.getDimensoes();
    }

    @Override
    public void setPosicao(Ponto pontoInferiorEsquerdo){

        noEmpacotado.changePositionTo(pontoInferiorEsquerdo);
    }

    public IBPTNode getNoEmpacotado(){

        return noEmpacotado;
    }

    @Override
    public Ponto getPontoInfEsq() {

        return noEmpacotado.getPontoInfEsq();
    }

}
