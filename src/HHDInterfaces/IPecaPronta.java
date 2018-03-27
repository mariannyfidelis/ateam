package HHDInterfaces;

import HHDInternal.Ponto;


public interface IPecaPronta{

	void retireUmaPeca();

	int getPecasDisponiveis();

        IDimensao2d retorneDimensao();

	public void setPosicao(Ponto pontoInferiorEsquerdo);
	
	public Ponto getPontoInfEsq ();

	
//	public IDimensao2d retorneDimensao();
//	public int id();
//	public String codigo();
//	public int quantidade();
//	public void atendaUmPedido();
//	public void devolvaPedido();
//	public int getPedidosAtendidos();

}
