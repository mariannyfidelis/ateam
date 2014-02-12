package HeuristicaConstrutivaInicial;


public class Pedidos implements IPedido{

    int id;
    int quant;
    IDimensao2d dimensao;
    String cod;
    
     
    public Pedidos(int id, float largura,float altura){
	
        this.id = id;
        this.quant = 1;
        this.dimensao = new Dimensao2D(largura, altura);
    }
    
     public Pedidos(){}
    
    @Override
    public IDimensao2d retorneDimensao() {
        
        return this.dimensao;
    }

    @Override
    public int id() {
        
        return id;
    }
    
    public void setId(int id){
    
        this.id = id;    
    }

    @Override
    public String codigo() {
        
        return cod;
    }

    @Override
    public int quantidade() {
        
        return quant;
    }

    @Override
    public void atendaUmPedido() {
        
        this.quant = this.quant - 1;
    }

    @Override
    public void devolvaPedido() {
        
        this.quant = this.quant + 1;
    }

    @Override
    public int getPedidosAtendidos() {
        
        return 0;
    }
    
}