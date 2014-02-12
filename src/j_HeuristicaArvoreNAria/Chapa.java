package j_HeuristicaArvoreNAria;


public class Chapa {

	private static float largura;
	private static float altura;
	private static float area;
	

	public Chapa(float larg, float alt){	
		
		largura = larg;
		altura = alt;
		area = largura*altura;	
	}
	
	
	public static float getAltura(){
	
            return altura;
	}
	public void setAltura(int alt){
	
            altura = alt;
	}
	public static float getLargura(){
		
            return largura;
	}
	public void setLargura(int larg){
	
            largura = larg;
	}
	
	public static float getArea(){
	
            area = largura*altura;
	
            return area; 
	}
	
	public void setArea(int are){
	
            area = are;
	}
}
