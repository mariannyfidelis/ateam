package Util;

public class Pedidos {

	private int id;
	private float d_altura, d_largura, d_area;
	@SuppressWarnings("unused")
	private int quantidade;
	
	
	public Pedidos(int id, float largura,float altura){
		this.setId(id);
		d_largura = largura;
		d_altura = altura;
		d_area = altura * largura;
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
	
}
