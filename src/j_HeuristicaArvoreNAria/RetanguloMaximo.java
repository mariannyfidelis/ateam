package j_HeuristicaArvoreNAria;

import java.io.Serializable;


public class RetanguloMaximo implements Serializable{

	private float W_P;
	private float H_P;
	private No idM;
	
	public RetanguloMaximo(No elemento){
		
		setIdM(elemento);
	}

	public float getW_P() {
		return W_P;
	}

	public void setW_P(float w_P) {
		W_P = w_P;
	}

	public float getH_P() {
		return H_P;
	}

	public void setH_P(float h_P) {
		H_P = h_P;
	}

	public No getIdM() {
		return idM;
	}

	public void setIdM(No idM) {
		this.idM = idM;
	}
	
	public float getAreaRetMaximo(){
		
		
		return W_P*H_P;
	}
	
	//Calcular o Retângulo Máximo
	
}
