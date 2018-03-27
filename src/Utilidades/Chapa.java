package Utilidades;

import HHDInterfaces.IDimensao2d;
import HHDInternal.Dimensao2D;
import java.io.Serializable;

public class Chapa implements IDimensao2d, Serializable{

    private static int id;
    private static float largura;
    private static float altura;
    private static float area;
    private IDimensao2d dimensao;
    
    public Chapa(float larg, float alt){	

        largura = larg;
        altura = alt;
        area = largura*altura;
        dimensao = new Dimensao2D(altura, largura);
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
    
    @Override
    public float retorneBase(){

        return this.dimensao.retorneBase();      
    }
	
    @Override
    public float retorneAltura(){

        return this.dimensao.retorneAltura();
    }
	
    @Override
    public float retorneArea (){

        return this.dimensao.retorneArea();        
    }

    @Override
    public int compareAreas(IDimensao2d dimensao){

      return 0;
    }
    
}
