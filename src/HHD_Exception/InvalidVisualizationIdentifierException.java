package HHD_Exception;


public class InvalidVisualizationIdentifierException extends Exception {
	
	String errorDescription;
	
	public InvalidVisualizationIdentifierException(String string) {
		
		errorDescription = string;
	}
	public InvalidVisualizationIdentifierException(){
	
		errorDescription = "Erro no identificador de visualização";
	}

}
