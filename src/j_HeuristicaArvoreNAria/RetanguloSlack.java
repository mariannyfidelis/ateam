package j_HeuristicaArvoreNAria;

import java.io.Serializable;


public class RetanguloSlack implements Serializable{

	private float ws_p;
	private float hs_p;
	private No idM;
	
	public RetanguloSlack(){
		
	}
	
	public float getW_s_p() {
		return ws_p;
	}
	
	public void setW_s_p(float ws_p) {
		this.ws_p = ws_p;
	}
	
	public float getH_s_p() {
		return hs_p;
	}
	
	public void setH_s_p(float hs_p) {
		this.hs_p = hs_p;
	}
	
	public No getIdM() {
		return idM;
	}
	
	public void setIdM(No idM) {
		this.idM = idM;
	}
	
}
