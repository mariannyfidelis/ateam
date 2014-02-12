package j_HeuristicaArvoreNAria;


public class RetanguloMinimo {

	private float w_p;
	private float h_p;
	private No id;
	
	public RetanguloMinimo(No elemento){
		
		setId(elemento);
	}

	public float getW_p() {
		return w_p;
	}

	public void setW_p(float w_p) {
		this.w_p = w_p;
	}

	public float getH_p() {
		return h_p;
	}

	public void setH_p(float h_p) {
		this.h_p = h_p;
	}

	public No getId() {
		return id;
	}

	public void setId(No id) {
		this.id = id;
	}
	
	public float getAreaRetMinimo(){
		
		
		return w_p*h_p;
	}
	
	//Calcular o Retângulo Mínimo
	
	
}
