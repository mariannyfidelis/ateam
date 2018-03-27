package j_HeuristicaArvoreNAria;

import HHDInternal.Dimensao2D;
import HHDInterfaces.IDimensao2d;
import HHDInterfaces.IPedido;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;


public class Pedidos implements IPedido, Serializable{

	private int id;
	private float d_altura, d_largura, d_area;
        
	private int quantidade;
	
	public Pedidos(Pedidos p){
        
            id = p.getId();
            d_largura = p.retornaLargura();
            d_altura = p.retornaAltura();
            d_area = p.retornaLargura() * p.retornaAltura();
            quantidade = 1;
        }
	
        public Pedidos(int id, float largura,float altura){
		
            this.id = id;
            d_largura = largura;
            d_altura = altura;
            d_area = altura * largura;
            quantidade = 1;
	}
	
	public int getId() {
		
            return id;
	}

	public void setId(int id) {
	
            this.id = id;
	}
	
	
	public float retornaArea(){
		
            d_area = d_altura*d_largura;;
	
            return d_area;
	}
	
	public float retornaLargura(){
	
            return d_largura;
	}
	
	public float retornaAltura(){
	
            return d_altura;
	}
	
	public void imprimePedido(Pedidos pedido){
		
            System.out.println("Altura :" + pedido.retornaAltura());
            System.out.println("Largura :"+ pedido.retornaLargura());
            System.out.println("Area :" + pedido.retornaArea());
    }
	public void imprimePedido(){
		
            System.out.println("Altura :" + retornaAltura());
            System.out.println("Largura :"+ retornaLargura());
            System.out.println("Area :" + retornaArea());
    }

	/*public int id();
	public String codigo();
	public int quantidade();
	public void atendaUmPedido();
	public void devolvaPedido();
	public int getPedidosAtendidos();
	
	//Recebe pedidos
	//Recebe dimensões
	*/
	
	//public static Pedidos(){}
	
	
	public static LinkedList<Pedidos> lista_itens_no_dominados(LinkedList<Pedidos> lista_pedidos, int[] nDom){
		
		//Pedidos[] pedidos = new Pedidos[lista_pedidos.size()];
		LinkedList<Pedidos> U = new LinkedList<Pedidos>();
		Iterator<Pedidos> iterador_pedidos = lista_pedidos.iterator();
		
		Pedidos pedido;
		int id_pedido;
		
		while(iterador_pedidos.hasNext()){
			
			pedido = iterador_pedidos.next();
			id_pedido = pedido.getId();
			
			if(nDom[id_pedido - 1] == 0){ //Lembrar de diminuir uma posição pois eh vetor !!!!
				U.add(pedido);
			}	
		}
		
		return U;	
	}

    @Override
    public IDimensao2d retorneDimensao() {
        
        return new Dimensao2D(retornaLargura(),retornaAltura());
    }

    @Override
    public int id() {
        
        return id;
    }

    @Override
    public String codigo() {
     
        return Integer.toString(this.id);
    }

    @Override
    public int quantidade() {
        
        return this.quantidade;
    }

    @Override
    public void atendaUmPedido() {
        
        this.quantidade = this.quantidade - 1;
    }

    @Override
    public void devolvaPedido() {
         
        this.quantidade = this.quantidade + 1;
    }

    @Override
    public int getPedidosAtendidos() {
        
        //Por enquanto
        
        return 0;
    }
}
