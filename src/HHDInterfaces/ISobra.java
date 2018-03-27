package HHDInterfaces;

import HHDInternal.Ponto;


public interface ISobra {

    public abstract Ponto getPontoInferiorEsquerdo();

    public abstract Ponto getPontoSuperiorDireito();

    public abstract int cabePeca(IDimensao2d maior, boolean permiteRotacao);
    
    public abstract boolean cabePeca(boolean permiteRotacao,IDimensao2d maior);

    public abstract float retorneAltura();

    public abstract float retorneBase();

    public abstract IDimensao2d retorneDimensao();
    
    public int retorneIndiceBin();
	
    public abstract int getId();
}
