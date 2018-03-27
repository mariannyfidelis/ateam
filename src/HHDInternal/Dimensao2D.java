package HHDInternal;

import HHDInterfaces.IDimensao2d;
import java.io.Serializable;


public class Dimensao2D implements IDimensao2d, Serializable{

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
        
        float area1 = (this.retorneBase() * this.retorneAltura());
        float area2 = (dimensao.retorneBase() * dimensao.retorneAltura());
        
        if(area1 == area2){
       
            if((this.retorneBase() == dimensao.retorneBase()) && (this.retorneAltura() == dimensao.retorneAltura())){
            
                return 1;
            }
            if((this.retorneBase() == dimensao.retorneAltura()) && (this.retorneAltura() == dimensao.retorneBase())){
            
                return 1;                
            }
        }
        
        return 0;
    }
    
}
