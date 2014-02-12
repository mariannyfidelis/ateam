package j_HeuristicaArvoreNAria;

import javax.swing.JOptionPane;


@SuppressWarnings("serial")
public class InvalidPositionException extends Exception {

	String mensagem = "Erro em operação na árvore !!!";
	
	public InvalidPositionException(){
		
		System.out.println("Erro em operação na árvore !!!");
		JOptionPane.showMessageDialog(null, mensagem);
	}
	
	public void imprimeErro(){
		
		System.out.println(mensagem);
	}
	
}
