package HHDInternal;

import HHDInterfaces.IDimensao2d;
import HHDInterfaces.IPedaco;
import java.io.Serializable;


public class PecaIPedacoWrapper implements IPedaco, Serializable{

    private Peca peca;
    private int indiceBin;

    public PecaIPedacoWrapper(Peca pecaAdaptada, int indiceBin){

        peca = pecaAdaptada;
        this.indiceBin = indiceBin;

    }

    @Override
    public int retorneIndiceBin(){

        return indiceBin;
    }

    @Override
    public IDimensao2d getDimensao() {
        return peca.retorneDimensoes();
    }

    @Override
    public Ponto getPontoInferiorEsquerdo() {
        return peca.getPontoInfEsq();
    }

    @Override
    public boolean podeRotacionar() {
        return peca.permiteRotacao();
    }

    public Peca getPeca(){
        return peca;
    }

    @Override
    public boolean temPeca(){
        return true;
    }

    @Override
    public boolean isSelected() {

        return peca.isSelected();
    }

    @Override
    public void removaDoBin(){
        indiceBin = -1;
    }

    public void setSelected(boolean b){
        peca.setSelected(b);
    }

    @Override
    public boolean estaRemovido() {

            return false;
    }

    @Override
    public int getID(){
        return 0;
    }
}