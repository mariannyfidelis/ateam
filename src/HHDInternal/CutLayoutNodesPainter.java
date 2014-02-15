package HHDInternal;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ListIterator;
import HHDInterfaces.ICorte;
import HHDInterfaces.IDimensao2d;
import HHDInterfaces.IGroup;
import HHDInterfaces.IPeca;
import HHDInterfaces.ISobra;



public class CutLayoutNodesPainter {

	Graphics g;
	float fator;
	IDimensao2d tamChapa;
	
	/**
	 * @param g2
	 * @param fator2
	 * @param tamChapa2
	 */
	public CutLayoutNodesPainter(Graphics g2, float fator2, IDimensao2d tamChapa2) 
	{
		g = g2;
		fator = fator2;
		tamChapa = tamChapa2;
	}

	/**
	 * @param g2
	 * @param fator2
	 * @param dimensao
	 * @param b
	 */

	public void paintIPeca(IPeca peca) 
	{
		int posx, posy;

		IDimensao2d dimensao = peca.getDimensoes();

		posx = (int)peca.getPontoInfEsq().getX();
		posy = (int)(tamChapa.retorneAltura() - (peca.getPontoInfEsq().getY() +
		       dimensao.retorneAltura()));

		g.setColor(DefinicaoCores.corPeca);
		g.fillRect((int)(posx * fator), (int)(posy * fator), 
					  (int)(dimensao.retorneBase() * fator), (int)(dimensao.retorneAltura() * fator));
		g.setColor(Color.BLACK);
		g.drawRect((int)(posx * fator), (int)(posy * fator), 
				  (int)(dimensao.retorneBase() * fator), (int)(dimensao.retorneAltura() * fator));
		
		if(peca.isSelected())
			paintSelected(g, (int)(posx * fator), (int)(posy * fator), 
				  (int)(dimensao.retorneBase() * fator),  (int)(dimensao.retorneAltura() * fator), Color.blue);
		
	}
	
	public void paintIGroup(IGroup grupo)
	{
		ListIterator iteradorCortes = grupo.getListaCortes().listIterator();
		ListIterator iteradorPecas = grupo.getListaPecas().listIterator();

		Graphics2D g2 = (Graphics2D) g;
		IDimensao2d dimensao = grupo.getDimensoes();
		int posx, posy;

		posx = (int)grupo.getPontoInfEsq().getX();
		posy = (int)(tamChapa.retorneAltura() - (grupo.getPontoInfEsq().getY() +
		       dimensao.retorneAltura()));

		g2.setPaint( DefinicaoCores.corChapa);
		g2.fill3DRect((int)(posx * fator), (int)(posy * fator), 
				  (int)(dimensao.retorneBase() * fator), (int)(dimensao.retorneAltura() * fator), true);
		
		while(iteradorPecas.hasNext())
		{
			IPeca peca = (IPeca) iteradorPecas.next();
			peca.acceptPainter(this);
			//painter.paint((IPeca) iteradorPecas.next(), g, fator, tamChapa);
		}
		
		while(iteradorCortes.hasNext())
			this.paint((ICorte) iteradorCortes.next());
		
		g2.setColor( DefinicaoCores.corMarcacaoAgrupamento);
		g2.setComposite( AlphaComposite.getInstance( AlphaComposite.SRC_OVER, DefinicaoCores.transparenciaAgrupamento ) );
		g2.fill3DRect((int)(posx * fator), (int)(posy * fator), 
				  (int)(dimensao.retorneBase() * fator), (int)(dimensao.retorneAltura() * fator), true);
		g2.setComposite( AlphaComposite.getInstance( AlphaComposite.SRC_OVER, (float) 1 ) );
		
		if( grupo.isSelected())
			this.paintSelected(g, (int)(posx * fator), (int)(posy * fator), 
					(int)(dimensao.retorneBase() * fator), (int)(dimensao.retorneAltura() * fator), DefinicaoCores.corSelecao);
	}

	
	
	private void paintSelected(Graphics g, int posx, int posy, int base, int altura, Color cor) 
	{
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor ( cor );

		g2.setComposite( AlphaComposite.getInstance( AlphaComposite.SRC_OVER, DefinicaoCores.transparenciaSelecao) );
		g.fillRect( posx, posy, base, altura );
		g2.setComposite( AlphaComposite.getInstance( AlphaComposite.SRC_OVER, (float) 1 ) );
	}

	public void paint(ICorte corte) 
	{
		float posx, posy; 
		int largura, altura;
		
		float maxy = tamChapa.retorneAltura();

		if(corte.eVertical())
		{
			posx = corte.getPontoChapaCortada().getX() + corte.getPosicaoCorte();
			posy = corte.getPontoChapaCortada().getY() + corte.getTamanho();
			
			altura = (int) (corte.getTamanho() * fator);
			largura = 2;
		}
		else
		{
			posx = corte.getPontoChapaCortada().getX();
			posy = corte.getPontoChapaCortada().getY() + corte.getPosicaoCorte();
			
			altura = 2;
			largura = (int) (corte.getTamanho() * fator);
		}
		
		posx = posx * fator;
		posy = (maxy - posy) * fator;
		
		g.setColor( DefinicaoCores.corCorte);
		g.fillRect((int) posx, (int) posy, largura, altura);
		
	}

	public void paint(ISobra sobra) 
	{
		int posx, posy;
		
		posx = (int)sobra.getPontoInferiorEsquerdo().getX();
		posy = (int)(tamChapa.retorneAltura() - (sobra.getPontoInferiorEsquerdo().getY() + sobra.retorneAltura()));
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor ( Color.RED );
		g2.setComposite( AlphaComposite.getInstance( AlphaComposite.SRC_OVER, (float) 0.3 ) );
		g.fillRect( (int)(posx * fator),(int)( posy * fator), (int)(sobra.retorneBase() * fator), (int)(sobra.retorneAltura() * fator));
		g2.setComposite( AlphaComposite.getInstance( AlphaComposite.SRC_OVER, (float) 1 ) );

	}

	/**
	 * @param pedaco
	 */
	public void paintISobra(ISobra sobra) 
	{
		int posx, posy;
		
		posx = (int)sobra.getPontoInferiorEsquerdo().getX();
		posy = (int)(tamChapa.retorneAltura() -
				       (sobra.getPontoInferiorEsquerdo().getY() +
				       		sobra.retorneAltura()));
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor ( DefinicaoCores.corChapa);
		//g2.setComposite( AlphaComposite.getInstance( AlphaComposite.SRC_OVER, (float) 0.3 ) );
		g.fillRect( (int)(posx * fator),(int)( posy * fator), (int)(sobra.retorneBase() * fator), (int)(sobra.retorneAltura() * fator));
		//g2.setComposite( AlphaComposite.getInstance( AlphaComposite.SRC_OVER, (float) 1 ) );

		
//		IDimensao2d tamChapa = pedaco.getDimens�es();
//
//		
//		Graphics2D g2 = (Graphics2D) g;
//		g2.setPaint( DefinicaoCores.corChapa);
//		g2.fillRect(0,0,(int)(tamChapa.retorneBase() * fator), (int)(tamChapa.retorneAltura() * fator));
	}

}
