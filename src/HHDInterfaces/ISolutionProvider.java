package HHDInterfaces;

import java.util.LinkedList;


public interface ISolutionProvider {

	public ISolutionInterface criaSolucao(IListaPedidos pedido, IDimensao2d tamanhoChapa);

	public void solucaoParcial(LinkedList listaPecas, LinkedList listaSobras, IDimensao2d tamanhoChapa);
}
