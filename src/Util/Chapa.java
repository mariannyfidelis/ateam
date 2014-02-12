package Util;


public class Chapa {

	private static int id;
	private static double largura;
	private static double altura;
	private static double area;
	

	public Chapa(double larg, double alt){	
		
		largura = larg;
		altura = alt;
		area = largura*altura;	
	}
	
	
	public static double getAltura(){
		
		return altura;
	}
	public void setAltura(int alt){
		
		altura = alt;
	}
	public static double getLargura(){
		
		return largura;
	}
	public void setLargura(int larg){
		
		largura = larg;
	}
	
	public static double getArea(){
		
		area = getLargura()*getAltura();
		
		return area; 
	}
	
	public void setArea(int are){
		
		area = are;
	}

	public static int getId() {
		return id;
	}

	public static void setId(int id) {
		Chapa.id = id;
	}
}
