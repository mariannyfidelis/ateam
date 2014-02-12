package j_HeuristicaArvoreNAria;

import java.io.IOException;
import java.util.LinkedList;


public class PrincipalTeste {

	public static void main(String[] args) throws IOException {
		
		LinkedList<Pedidos> pedidos;	
		pedidos = Funcoes.lerArquivo();
		
		Funcoes.imprimeListaPedidos(pedidos);
		
		Solucao c = new Solucao();
		Metodos_heuristicos m = new Metodos_heuristicos();
		
		//(Rotaciona, FirstFit, Pedidos, Chapa)
		
		c = m.FFIH_BFIH(true,false, pedidos, Funcoes.chapa); 
		c.imprime_solucao();
	}
}