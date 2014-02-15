package HHDInternal;

import HHDInterfaces.IDimensao2d;


public class AreaRetangular implements IDimensao2d{
	
    private float base;
    private float altura;

    public AreaRetangular(float base, float altura) {

        super();
        this.base = base;
        this.altura = altura;
    }
	
    public float retorneArea(){

       return base * altura;
    }

    public float retorneBase(){

       return base;
    }

    public float retorneAltura(){

        return altura;
    }

    public int compareAreas(IDimensao2d dimensao){

         float diferenca;

         diferenca = retorneArea() - dimensao.retorneArea();

         if(diferenca < 0)
              return - 1;
         if (diferenca == 0)
              return 0;

         return 1;
    }
}