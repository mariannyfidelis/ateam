package HHDInternal;

import HHDInterfaces.IDimensao2d;


public class Dimensao2D implements IDimensao2d{

    float altura;
    float largura;
    
    public Dimensao2D(float largura, float altura){
    
        this.altura = altura;
        this.largura = largura;
    
    }
    @Override
    public float retorneBase() {
        return this.largura;
    }

    @Override
    public float retorneAltura() {
        return this.altura;
    }

    @Override
    public float retorneArea() {
        return this.altura * this.largura;
    }

    @Override
    public int compareAreas(IDimensao2d dimensao) {
        return 0;
    }
    
}
