package HeuristicaConstrutivaInicial;

import java.util.List;

public class Faixas{

    private int W;
    private int H;
    private double Pi;
    private int Rw;
    private int Rh;
    private int O;
    private List<Item> item;
    private Ponto pontoInferiorEsquerdo;
    private Ponto pontoSuperiorDireito;
    private Ponto pontInfERw;
    private Ponto pontSupDRh;
    //poderia ter um rendimento por faixa = (AreaFaixa - Somatorio(AreaDosItens))
    
    public Faixas() {     }

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

    /**
     * @return the W
     */
    public int getW() {

        return W;
    }

    /**
     * @param W the W to set
     */
    public void setW(int W) {
        
        this.W = W;
    }

    /**
     * @return the H
     */
    public int getH() {
        
        return H;
    }

    /**
     * @param H the H to set
     */
    public void setH(int H) {
        
        this.H = H;
    }

    /**
     * @return the Pi
     */
    public double getPi() {
        
        return Pi;
    }

    /**
     * @param Pi the Pi to set
     */
    public void setPi(double Pi) {
        
        this.Pi = Pi;
    }

    /**
     * @return the Rw
     */
    public int getRw() {
        
        return Rw;
    }

    /**
     * @param Rw the Rw to set
     */
    public void setRw(int Rw) {
        
        this.Rw = Rw;
    }

    /**
     * @return the Rh
     */
    public int getRh() {
        
        return Rh;
    }

    /**
     * @param Rh the Rh to set
     */
    public void setRh(int Rh) {
        
        this.Rh = Rh;
    }

    /**
     * @return the O
     */
    public int getO() {
        
        return O;
    }

    /**
     * @param O the O to set
     */
    public void setO(int O) {
        
        this.O = O;
    }

    /**
     * @return the Item
     */
    public List<Item> getItem() {
        
        return item;
    }

    /**
     * @param Item the Item to set
     */
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