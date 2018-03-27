package HHDInterfaces;

/**
 * Esta interface e um modo de encapsular todos os pedidos de corte
 * em um mesmo objeto de forma que as informacoes sobre estes 
 * pedidos possam ser extraidas
 */
public interface IListaPedidos {
	/**
	 *  Retorna as informacoes a respeito da proxima
	 *  peca na lista de pedidos.
	 */
	public IPedido next();
	/**
	 *  Informa se ha mais elementos na lista de
	 *  pedidos.
	 */
	public boolean hasNext();
	/**
	 *  Reinicia a "iteracao" na lista de pedidos.
	 */
	public void reset();
}
