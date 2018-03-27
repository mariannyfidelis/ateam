package HHDInternal;

import HHDInterfaces.IDimensao2d;
import HHDInterfaces.IPedido;
import java.io.Serializable;

public class Pedidos implements IPedido, Serializable{

    int id;
    int quantidade;
    IDimensao2d dimensao;
    String cod;
    int atendidos;
    
     
    public Pedidos(int id, float largura,float altura){
	
        this.setId(id);
        //this.id = id;
        this.quantidade = 1;
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
        
        return quantidade;
    }

    @Override
    public void atendaUmPedido() {
        
        this.quantidade = this.quantidade - 1;
        
        if(atendidos < quantidade) 
             atendidos++;
    }

    @Override
    public void devolvaPedido() {
        
        this.quantidade = this.quantidade + 1;
        
        if(atendidos > 0)
             atendidos--;
    }

    @Override
    public int getPedidosAtendidos() {
        
        return atendidos;
    }

}