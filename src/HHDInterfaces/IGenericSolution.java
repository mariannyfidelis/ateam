package HHDInterfaces;

//import HeuristicaConstrutivaInicial.Bin;


public interface IGenericSolution extends IObservable{
    
	public abstract int getQtd();

	public abstract IDimensao2d getTamanhoChapa();

	public abstract IBin retornePlanoDeCorte(int indice);

	public abstract boolean mova(IPedaco pedaco, ISobra areaDestino, int indiceBin);
	
        public abstract void aglutineSobras(int indice);

}