package HHDInternal;

import HHDInterfaces.IDimensao2d;
import java.io.Serializable;


public class AreaRetangular implements IDimensao2d, Serializable{
	
    private float base;
    private float altura;

    public AreaRetangular(float base, float altura) {

        super();
        this.base = base;
        this.altura = altura;
    }
	
    @Override
    public float retorneArea(){

       return base * altura;
    }

    @Override
    public float retorneBase(){

       return base;
    }

    @Override
    public float retorneAltura(){

        return altura;
    }

    @Override
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