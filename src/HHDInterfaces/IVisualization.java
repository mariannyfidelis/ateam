package HHDInterfaces;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

public interface IVisualization{

	public void paint(Graphics g, Dimension d, int indice);
	public void paint(IPedaco pedaco, Graphics g, Dimension d);
	public IPedaco getPedaco(Point pontoClique, Dimension dimensaoGrafico, int indice);
	public boolean selecione(Point ponto1,Point ponto2, Dimension dimensaoGrafico, int indice);
	public boolean eUmPedacoSelecionado (Point pontoCliquearea, Dimension dimensaoGrafico, int indice);
	public boolean mova(IPedaco pedaco, Point pontoCliquearea, Dimension dimensaoGrafico, int indiceBin);
}
