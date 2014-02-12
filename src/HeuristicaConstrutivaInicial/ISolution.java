package HeuristicaConstrutivaInicial;

public interface ISolution {
	
	public int getQtd();

	public IDimensao2d getTamanhoChapa();

	public Objeto retornePlanoDeCorte(int indice);
}