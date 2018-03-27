package HHDInterfaces;
 
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Collection;
import java.util.LinkedList;
import HHD_Exception.InvalidVisualizationIdentifierException;

public interface ISolutionInterface extends Cloneable{

	public IVisualization getVisualization();
	public IVisualization getVisualization(String visIdentifier) throws InvalidVisualizationIdentifierException;
	public abstract void paint(Graphics g, Dimension d, int indice);

        public int getQtd();
	
        public Collection getVisualizationList ();
	public IDimensao2d getTamanhoChapa();
	
        public float getFator(Dimension tamanhoGrafico);
	
        public boolean remova(IPedaco pedaco);
	public boolean remova(Collection listaPedacos);
	
        public void marqueSobrasMaiores(IDimensao2d d, int indice);
	public void desmarqueSobras(int indice);

	public boolean mova(IPedaco pedaco, ISobra areaDestino, int indiceBin);
	public boolean selecione(IPedaco pedaco);
	public boolean selecioneApenas(IPedaco pedaco);
	public Collection getSelecionadas ( int indice );
	public void descelecioneTodas ( int indice );
	
	public void solucioneNaoAtendidas(String algoritmoDeCorte);
	public void solucioneNaoAtendidas();
	
	public boolean agrupePecas(LinkedList listaPecas, int indice);
	public boolean ePecaAgrupadora(IPedaco pedaco);
	public boolean desagrupePecas(IPedaco pedaco);
}