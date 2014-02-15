package HHDInternal;


import HHDInterfaces.IDimensao2d;

public class Chapa implements IDimensao2d{

    private IDimensao2d dimensao;
    //private float base;
    //private float altura;

    public Chapa(float largura,float altura){
    
        dimensao = new Dimensao2D(altura, largura);
       
    }
    
    @Override
    public float retorneBase(){

        return this.dimensao.retorneBase();
        
        //return base;
    }
	
    @Override
    public float retorneAltura(){

        return this.dimensao.retorneAltura();
        //return altura;
    }
	
    @Override
    public float retorneArea (){

        return this.dimensao.retorneArea();
        //return base * altura;	
    }

    @Override
    public int compareAreas(IDimensao2d dimensao){

      return 0;
    }
}
