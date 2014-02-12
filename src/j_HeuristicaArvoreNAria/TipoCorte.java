package j_HeuristicaArvoreNAria;


public class TipoCorte {

	No_orientacao corte;
	
	public TipoCorte(){
		
		corte = No_orientacao.nulo;//"vertical";
	}
	public TipoCorte(boolean vert){
		
		if(vert == false){
			
			corte = No_orientacao.horizontal;
		}
		if(vert == true){
			
			corte = No_orientacao.vertical;
		}
	}
	
	public String getTipoCorte(){
		
		if(corte == No_orientacao.vertical){
			
			return "vertical";
		}
		
		else if(corte == No_orientacao.horizontal){
			
			return "horizontal";
		}
		else
			return "nulo";
	}
	
	public No_orientacao getTipoCort(){
		
		return corte;
	}
}
