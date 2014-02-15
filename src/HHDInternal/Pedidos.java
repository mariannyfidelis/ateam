package HHDInternal;

import HHDInterfaces.IDimensao2d;
import HHDInterfaces.IPedido;

public class Pedidos implements IPedido{

    int id;
    int quant;
    IDimensao2d dimensao;
    String cod;
    int atendidos;
    
     
    public Pedidos(int id, float largura,float altura){
	
        this.setId(id);
        this.quant = 1;
        this.atendidos = 0;
        this.dimensao = new Dimensao2D(largura, altura);
    }
    
    
    @Override
    public IDimensao2d retorneDimensao() {
        
        return this.dimensao;
    }
    
    
    public IDimensao2d retorneDimensao(int rot) {
        
        return new Dimensao2D(retorneDimensao().retorneBase(), retorneDimensao().retorneAltura());
        //return this.dimensao;
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
        
        if(atendidos < quant) 
             atendidos++;
    }

    @Override
    public void devolvaPedido() {
        
        if(atendidos > 0)
             atendidos--;
    }

    @Override
    public int getPedidosAtendidos() {
        
        return atendidos;
    }

}