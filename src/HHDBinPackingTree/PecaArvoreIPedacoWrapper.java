package HHDBinPackingTree;

import HHDInterfaces.IDimensao2d;
import HHDInterfaces.IPeca;
import HHDInterfaces.IPecaIPedacoWrapper;
import HHDInternal.Ponto;


public class PecaArvoreIPedacoWrapper implements IPecaIPedacoWrapper{

    IBPTNode pedaco;
    int indicePlanoDeCorte;


    public PecaArvoreIPedacoWrapper(IBPTNode pedaco, int id) 
    {
            this.pedaco = pedaco;
            indicePlanoDeCorte = id;
    }

    @Override
    public IPeca getPeca(){

         return pedaco;
    }

    @Override
    public int retorneIndiceBin(){

        return indicePlanoDeCorte;
    }

    @Override
    public IDimensao2d getDimensao(){

        return pedaco.getDimensoes();
    }


    @Override
    public Ponto getPontoInferiorEsquerdo(){

        return pedaco.getPosition();
    }

    @Override
    public boolean podeRotacionar(){

        return pedaco.permiteRotacao();
    }

    @Override
    public boolean temPeca(){

        return pedaco.temPeca();
    }

    @Override
    public boolean isSelected(){

        return pedaco.isSelected();
    }

    @Override
    public void removaDoBin() 
    {
        pedaco.remova();
    }

    @Override
    public void setSelected(boolean b){

        pedaco.setSelected(b);
    }

    @Override
    public boolean estaRemovido(){

        return pedaco.estaRemovidoDaArvore();
    }

    @Override
    public int getID(){

        return pedaco.getID();
    }

    public void setPeca(IBPTNode pecaAgrupadora){
        
        this.pedaco = pecaAgrupadora;
    }

}
