package HeuristicaConstrutivaInicial;


public class Item  implements IPedido{
    
    private int Id;
    private int W;
    private int H;
    private int D;
    private int V;
    private int O;

    public Item(int id, int w, int h, int d){
        
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

    public int getH() {
        
        return H;
    }

    public void setH(int H) {
        
        this.H = H;
    }

    public int getW() {
        
        return W;
    }

    public void setW(int W) {
        
        this.W = W;
    }

    public int getD() {
        
        return D;
    }

    public void setD(int D) {
        
        this.D = D;
    }

    public int getV() {
        
        return V;
    }

    public void setV(int V) {
        
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
        
        return new Dimensao2D(getH(), getW());
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
        this.D = this.D -1;
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
