package HHDInterfaces;

import HeuristicaConstrutivaInicial.Bin;


public interface ISolution{
    
    public int getQtd();
    
    public IDimensao2d getTamanhoChapa();
        
    public Bin retornePlanoDeCorte(int indice);
    
    public Bin retornePlanoDeCorte_Ob(int indice);
}