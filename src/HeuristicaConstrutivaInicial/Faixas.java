package HeuristicaConstrutivaInicial;

import HHDInternal.Ponto;
import java.io.Serializable;
import java.util.List;

public class Faixas implements Serializable{

    private float W;
    private float H;
    private double Pi;
    private float Rw;
    private float Rh;
    private int O;
    private List<Item> item;
    private Ponto pontoInferiorEsquerdo;
    private Ponto pontoSuperiorDireito;
    private Ponto pontInfERw;
    private Ponto pontSupDRh;
    //poderia ter um rendimento por faixa = (AreaFaixa - Somatorio(AreaDosItens))
    
    public Faixas() {}

    public Faixas(int w, int h) {
        
        W = w;
        H = h;
        Pi = w * h;
        Rw = w;
        Rh = h;
        O = 0;
        pontoInferiorEsquerdo =  new Ponto(0, 0);
        pontoSuperiorDireito = new Ponto(w, h);
        
        pontInfERw = new Ponto(0, 0);
        pontSupDRh = new Ponto(w, h);

    }

    public float getW() {

        return W;
    }

    public void setW(float W) {
        
        this.W = W;
    }

    public float getH() {
        
        return H;
    }

    public void setH(float H) {
        
        this.H = H;
    }

    public double getPi() {
        
        return Pi;
    }

    public void setPi(double Pi) {
        
        this.Pi = Pi;
    }

    public float getRw() {
    
        return Rw;
    }

    public void setRw(float Rw) {
        
        this.Rw = Rw;
    }

    public float getRh() {
        
        return Rh;
    }

    public void setRh(float Rh) {
        
        this.Rh = Rh;
    }

    public int getO() {
        
        return O;
    }

    public void setO(int O) {
        
        this.O = O;
    }

    public List<Item> getItem() {
        
        return item;
    }

    public void setItem(List<Item> item) {
        
        this.item = item;
    }
    
    public Ponto getPontoInferiorEsquerdo() {
        
        return pontoInferiorEsquerdo;
    }

    public void setPontoInferiorEsquerdo(Ponto pontoInferiorEsquerdo) {
        
        this.pontoInferiorEsquerdo = pontoInferiorEsquerdo;
    }
    
    public void setPontoInferiorEsquerdo(Ponto pontoInferiorEsquerdo, int i) {
        
        pontoInferiorEsquerdo = new Ponto(pontoInferiorEsquerdo.getX(),pontoInferiorEsquerdo.getY());
    }

    public Ponto getPontoSuperiorDireito() {
        
        return pontoSuperiorDireito;
    }

    public void setPontoSuperiorDireito(Ponto pontoSuperiorDireito) {
        
        this.pontoSuperiorDireito = pontoSuperiorDireito;
    }
    
    public void setPontoSuperiorDireito(Ponto pontoSuperiorDireito, int i) {
        
        pontoSuperiorDireito = new Ponto(pontoSuperiorDireito.getX(), pontoSuperiorDireito.getY());
    }

    public Ponto getPontInfERw() {
        
        return pontInfERw;
    }

    public void setPontInfERw(Ponto pontInfERw) {
        
        this.pontInfERw = pontInfERw;
    }

    public void setPontInfERw(Ponto pontInfERw, int i) {
        
        pontInfERw = new Ponto(pontInfERw.getX(), pontInfERw.getY());
    }
    
    public Ponto getPontSupDRh() {
        
        return pontSupDRh;
    }

    public void setPontSupDRh(Ponto pontSupDRh) {
        
        this.pontSupDRh = pontSupDRh;
    }

    public void setPontSupDRh(Ponto pontSupDRh, int i) {
        
        this.pontSupDRh = new Ponto(pontSupDRh.getX(), pontSupDRh.getY());
    }
}