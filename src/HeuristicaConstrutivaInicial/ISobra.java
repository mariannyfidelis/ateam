package HeuristicaConstrutivaInicial;


public interface ISobra {

    public abstract Ponto getPontoInferiorEsquerdo();

    public abstract Ponto getPontoSuperiorDireito();

    public abstract boolean cabePeca(IDimensao2d maior, boolean permiteRotacao);

    public abstract float retorneAltura();

    public abstract float retorneBase();

    public int retorneIndiceBin();
	
    public abstract int getId();
}
