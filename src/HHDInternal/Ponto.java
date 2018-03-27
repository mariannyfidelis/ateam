package HHDInternal;

import java.io.Serializable;

public class Ponto implements Serializable{
    
    private float x;
    private float y;
	
    public Ponto(float x, float y){
        
	this.x = x;
	this.y = y;
    }
    
    public Ponto(Ponto ponto){
    
        this.x = ponto.getX();    
        this.y = ponto.getY();
    }

    //Secao GETTERS e SETTERS (tentar eliminar)
    public float getX() {
	return x;
    }
    
    public void setX(float x) {
	this.x = x;
    }
    
    public float getY() {
	return y;
    }

    public void setY(float y) {
	this.y = y;
    }

    /* TENTAR DESCOBRIR FUTURAMENTE POSS�VEIS ERROS DE ARREDONDAMENTO */
    public static float calculaAreaPontos(Ponto pIE, Ponto pSD){
        
	return (pSD.getX() - pIE.getX()) * (pSD.getY() - pIE.getY());
    }
	
    public boolean eIgualAoPonto(Ponto pontoComparado)	{
        
	return (x == pontoComparado.x) && (y == pontoComparado.y);
    }

    public boolean estaNaArea(Ponto pontoInfEsq, Ponto pontoSupDir){
        
	if(this.x >= pontoInfEsq.x && this.y >= pontoInfEsq.y &&
		   this.x <= pontoSupDir.x && this.y <= pontoSupDir.y)
			return true;
		
		return false;
	}
}
