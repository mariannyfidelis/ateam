package HeuristicaConstrutivaInicial;

public interface IPedido{

	public IDimensao2d retorneDimensao();
	public int id();
	public String codigo();
	public int quantidade();
	public void atendaUmPedido();
	public void devolvaPedido();
	public int getPedidosAtendidos();
}
