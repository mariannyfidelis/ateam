package HHDInternal;

import HHDInterfaces.IDimensao2d;
import HHDInterfaces.IPedido;
import java.io.Serializable;


public class Pedaco implements Serializable{

	private Position pontoInferiorEsquerdo;
	private IDimensao2d tamanhoPedaco;
	
	public Pedaco(IDimensao2d tamanhoChapa) 
	{
		pontoInferiorEsquerdo = new Position(0,0);
		tamanhoPedaco = tamanhoChapa;
	}
	
	public Pedaco(IDimensao2d tamanhoChapa, Position posicao)
	{
		pontoInferiorEsquerdo = posicao;
		tamanhoPedaco = tamanhoChapa;
	}

	public void setPontoInferiorEsquerdo(Position posicao) 
	{
		pontoInferiorEsquerdo = posicao;
	}

	public Position getPontoInferiorEsquerdo() {
		return pontoInferiorEsquerdo;
	}
	
	public IDimensao2d retorneDimensao() {
		return tamanhoPedaco;
	}

	public Pedaco corta(float posicaoCorte, boolean vertical) 
	{
		Pedaco novoPedaco;
		if(vertical)
		{
			novoPedaco = new Pedaco(new AreaRetangular(tamanhoPedaco.retorneBase() - posicaoCorte, tamanhoPedaco.retorneAltura()));
			novoPedaco.setPontoInferiorEsquerdo(new Position(this.pontoInferiorEsquerdo.getPositionX() + posicaoCorte, 
					                                         this.pontoInferiorEsquerdo.getPositionY()));
			tamanhoPedaco = new AreaRetangular(posicaoCorte, tamanhoPedaco.retorneAltura());
		}
		else
		{
			novoPedaco = new Pedaco(new AreaRetangular(tamanhoPedaco.retorneBase(), tamanhoPedaco.retorneAltura() - posicaoCorte), 
					     new Position(pontoInferiorEsquerdo));
			tamanhoPedaco = new AreaRetangular(tamanhoPedaco.retorneBase(), posicaoCorte);
			pontoInferiorEsquerdo.setPosition(pontoInferiorEsquerdo.getPositionX(),
					                          pontoInferiorEsquerdo.getPositionY() + posicaoCorte);
			
		}
		return novoPedaco;
	}

	// QUANDO ADICIONAR ROTACAO, ESTA ROTINA DEVERA SER ALTERADA
	public boolean cabePeca(IPedido maior, boolean permiteRotacao) 
	{
		return maior.retorneDimensao().retorneAltura() < this.retorneDimensao().retorneAltura() &&
		       maior.retorneDimensao().retorneBase() < this.retorneDimensao().retorneBase();
	}
	
	public Pedaco copia(){
            
            Pedaco copia = new Pedaco(new AreaRetangular(tamanhoPedaco.retorneBase(),
					                           tamanhoPedaco.retorneAltura()));
			
            copia.setPontoInferiorEsquerdo(new Position(pontoInferiorEsquerdo));
		
		return copia;
	}
}
