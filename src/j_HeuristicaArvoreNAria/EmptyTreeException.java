package j_HeuristicaArvoreNAria;


@SuppressWarnings("serial")
public class EmptyTreeException extends Exception {

	public EmptyTreeException(){ }
	
	public void imprime_erro(){
		
		System.out.println("Erro manipulação na árvore --> vazia");
	}
	
}
