package j_HeuristicaArvoreNAria;


public class Item {

	private int id;
	private float largura;
	private float altura;
	private boolean rotacao;
	private float area;
	
	public Item(int id, float larg, float alt, boolean rotacao){
		this.id = id;
		this.largura = larg;
		this.altura = alt;
		this.rotacao = rotacao;
	}
	public Item(){}
	
	
	public int getId(){
		
		return id;
	}
	
	public void setId(int new_id){
		id = new_id;
	}
	
	public float getLargura(){
		
		return largura;
	}
	
	public void setLargura(float larg){
		
		largura = larg;
	}
	
	public float getAltura(){
		
		return altura;
	}
	
	public void setAltura(float alt){
		
		altura = alt;
	}
	
	public float getArea(){
		
		area = altura * largura;
		
		return area;
		
	}
	
	public void setArea(float are){
		
		area = are;
	}
	
	public boolean getRotacao() {
		
		return rotacao;
	}
	
	public void setRotacao(boolean rotacao) {
		
		this.rotacao = rotacao;
	}
	
}
