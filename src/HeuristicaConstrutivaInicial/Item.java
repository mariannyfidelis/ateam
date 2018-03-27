package HeuristicaConstrutivaInicial;

import HHDInternal.Dimensao2D;
import HHDInterfaces.IDimensao2d;
import HHDInterfaces.IPedido;
import java.io.Serializable;


public class Item  implements IPedido, Serializable{
    
    private int Id;
    private float W;
    private float H;
    private int D;
    private float V;
    private int O;

    public Item(int id, float w, float h, int d){
        
        Id = id;
        W = w;
        H = h;
        D = d;
        V = w*h;
        O = 0;
    }

    public Item() {
    }

    public int getId() {
        
        return Id;
    }

    public void setId(int Id) {
        
        this.Id = Id;
    }

    public float getH() {
        
        return H;
    }

    public void setH(float H) {
        
        this.H = H;
    }

    public float getW() {
        
        return W;
    }

    public void setW(float W) {
        
        this.W = W;
    }

    public int getD() {
        
        return D;
    }

    public void setD(int D) {
        
        this.D = D;
    }

    public float getV() {
        
        return V;
    }

    public void setV(float V) {
        
        this.V = V;
    }

    public int getO() {
        
        return O;
    }

    public void setO(int O) {
        
        this.O = O;
    }

    @Override
    public IDimensao2d retorneDimensao() {
        
        return new Dimensao2D(getW(),getH());
    }

    @Override
    public int id() {
        
        return getId();
    }

    @Override
    public String codigo() {
        
        String cod = Integer.toString(getId());
                
       return cod;
    }

    @Override
    public int quantidade() {
        return getD();
    }

    @Override
    public void atendaUmPedido() {
        this.D = this.D - 1;
    }

    @Override
    public void devolvaPedido() {
        this.D = this.D + 1;
    }

    @Override
    public int getPedidosAtendidos() {
        
        return 0;
    }

}
