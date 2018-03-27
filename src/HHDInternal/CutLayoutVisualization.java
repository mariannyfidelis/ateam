package HHDInternal;

import java.awt.Point;
import java.awt.Graphics;
import java.util.Iterator;
import java.awt.Dimension;
import HHDInterfaces.IBin;
import HHDInterfaces.IPeca;
import java.awt.Graphics2D;
import HHDInterfaces.ICorte;
import HHDInterfaces.ISobra;
import HHDInterfaces.IPedaco;
import java.util.ListIterator;
import HHDInterfaces.IDimensao2d;
import HHDInterfaces.IVisualization;
import HHDInterfaces.IGenericSolution;
import HHDInterfaces.IPecaIPedacoWrapper;


public class CutLayoutVisualization implements IVisualization{

	private IGenericSolution solucao;
	
	//implementacao OK
	public CutLayoutVisualization(IGenericSolution givenSolution){
		solucao = givenSolution; 
	}
	
	//implementacao OK
	public void paint(Graphics g, Dimension d, int indice){
            
            float fator = calculeFator(d, solucao.getTamanhoChapa());
		
		IBin planoCorte = (IBin) solucao.retornePlanoDeCorte(indice);
		
		ListIterator iteradorCortes = planoCorte.getListaCortes().listIterator();
		ListIterator iteradorPecas = planoCorte.getListaPecas().listIterator();
		
		IDimensao2d tamChapa = solucao.getTamanhoChapa();

		
		Graphics2D g2 = (Graphics2D) g;
		g2.setPaint( DefinicaoCores.corChapa);
		g2.fill3DRect(0,0,(int)(tamChapa.retorneBase() * fator), (int)(tamChapa.retorneAltura() * fator), true);
		
		Iterator iteradorSobras = planoCorte.getListaSobrasMarcadas().iterator();
		CutLayoutNodesPainter painter = new CutLayoutNodesPainter(g, fator, tamChapa);
		
		while(iteradorPecas.hasNext())
		{
			IPeca peca = (IPeca) iteradorPecas.next();
			peca.acceptPainter(painter);
			//painter.paint((IPeca) iteradorPecas.next(), g, fator, tamChapa);
		}
		
		while(iteradorCortes.hasNext())
			painter.paint((ICorte) iteradorCortes.next());
		
		while(iteradorSobras.hasNext())
			painter.paint((ISobra) iteradorSobras.next());
	}

	//implementacao OK (nao mudou)
	private float calculeFator(Dimension d, IDimensao2d tamChapa)
	{
		float a = (d.height - 1) / tamChapa.retorneAltura();
		float b = (d.width - 1) / tamChapa.retorneBase();
		
		if ( a < b )
			return a;
		return b;		
}

	//Verificar a possibilidade de delegar "getPedaco" � solucao, para que o desempenho
	//seja melhorado. Afinal, da forma como esta escrito, a lista sera construida (percorrendo a arvore)
	//e s� depois teremos a busca pela peca. Ao inves disso, podemos simplesmente percorrer a arvore
	//em busca da Peca. E essa adicao nao retira a generalidade do c�digo.
	
	//outra opcao e manter em cache a lista de pecas. Assim, a arvore estara efetivamente mantendo
	//duas estruturas de dados. No entanto, transferir getPedaco para solucao parece mesmo uma melhor
	//solucao
	//Esta decidido: TODO DELEGAR getPedaco ao planoDeCorte apropriado
	public IPedaco getPedaco(Point pontoClique, Dimension dimensaoGrafico, int indice) 
	{
		IBin planoDeCorte = solucao.retornePlanoDeCorte(indice);
		float fator = this.calculeFator(dimensaoGrafico, solucao.getTamanhoChapa());
		Ponto pontoCliqueChapa = new Ponto(pontoClique.x / fator, solucao.getTamanhoChapa().retorneAltura() - pontoClique.y / fator);
		
		IPeca pecaEncontrada = procurePeca(pontoCliqueChapa, planoDeCorte.getListaPecas().listIterator());
		
		if(pecaEncontrada != null)
			return (IPedaco) pecaEncontrada.getIPecaIPedacoWrapper();
		
		return null; // a peca nao foi encontrada
		
		//substituir por:
//		ITesteBin planoDeCorte = solucao.retornePlanoDeCorte(indice);
//		float fator = this.calculeFator(dimensaoGrafico);
//		Ponto pontoCliqueChapa = new Ponto(pontoClique.x / fator, solucao.getTamanhoChapa().retorneAltura()- pontoClique.y / fator);
//		return planoDeCorte.getPedaco(pontoCliqueChapa, dimensaoGrafico) 
	}
	
	//perdendo tempo aqui, ja que a busca em uma arvore binaria e BEM mais rapida
	private IPeca procurePeca(Ponto pontoCliqueChapa, ListIterator iterator) 
	{
		IPeca atual;
		while(iterator.hasNext())
		{
			atual = (IPeca) iterator.next();
			Ponto pIE = atual.getPontoInfEsq();
			Ponto pSD = atual.getPontoSupDir();
			
			if(pontoCliqueChapa.estaNaArea(pIE, pSD))
				return atual;
		}
		return null;
	}


	//por enquanto, manter esta funcao sem implementacao
	public boolean selecione(Point ponto1, Point ponto2, Dimension dimensaoGrafico, int indice) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see transicao.modifiedCuttingInterface.IVisualization#mova(transicao.modifiedCuttingInterface.IPedaco, java.awt.Point, java.awt.Dimension, int)
	 */
	public boolean mova(IPedaco pedaco, Point pontoCliquearea, Dimension dimensaoGrafico, int indiceBin) 
	{
//		if(!pedaco.temPeca())
//		{
//			System.err.println("Retornou false.\n");
//			return false;
//		}

		float fator = calculeFator(dimensaoGrafico, solucao.getTamanhoChapa());
		Ponto pontoCliqueChapa = new Ponto(pontoCliquearea.x / fator, solucao.getTamanhoChapa().retorneAltura() - pontoCliquearea.y / fator);
		IBin planoDeCorte = solucao.retornePlanoDeCorte(indiceBin);
		
		ISobra areaDestino = procureSobra(pontoCliqueChapa, planoDeCorte.getListaSobras().listIterator());
		
		return solucao.mova(pedaco, areaDestino, indiceBin);
	}

	/**
	 * @param pontoCliqueChapa
	 * @param iterator
	 * @return
	 */
	private ISobra procureSobra(Ponto pontoCliqueChapa, Iterator iterator) 
	{
		while (iterator.hasNext())
		{
			ISobra pedaco = (ISobra)iterator.next(); 
			Ponto pIE = pedaco.getPontoInferiorEsquerdo();
			Ponto pSD = pedaco.getPontoSuperiorDireito();
			
			if(pontoCliqueChapa.estaNaArea(pIE, pSD))
				return pedaco;
		}
		return null;
	}

	public boolean eUmPedacoSelecionado(Point pontoClique, Dimension dimensaoGrafico, int indice) 
	{
		IBin planoDeCorte = solucao.retornePlanoDeCorte(indice);
		float fator = calculeFator(dimensaoGrafico, solucao.getTamanhoChapa());
		Ponto pontoCliqueChapa = new Ponto(pontoClique.x / fator, solucao.getTamanhoChapa().retorneAltura() - pontoClique.y / fator);		
		ISobra pedaco = procureSobra(pontoCliqueChapa, planoDeCorte.getListaSobrasMarcadas().iterator());
		if(pedaco == null)
			return false;
		
		return true;
	}

	/* (non-Javadoc)
	 * @see cuttingInterface.IVisualization#paint(cuttingInterface.IPedaco, java.awt.Graphics, java.awt.Dimension)
	 */
	public void paint(IPedaco pedaco, Graphics g, Dimension d) 
	{
		float fator = calculeFator(d, pedaco.getDimensao());
		
		CutLayoutNodesPainter painter = new CutLayoutNodesPainter(g, fator, pedaco.getDimensao());
		((IPecaIPedacoWrapper) pedaco).getPeca().acceptPainter(painter);
	}

	
//	public boolean eUmPedacoSelecionado(Point pontoCliquearea, Dimension dimensaoGrafico, int indice) 
//	{
//		
//		return false;
//	}
}
