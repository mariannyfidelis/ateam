package HHDInterfaces;

import java.util.LinkedList;


public interface ISolutionConversor {
	
    public abstract IGenericSolution converteParaSolucao(ISolution hSolution/*, boolean isGrasp*/);

    //aqui era void, e tbm não tinha florest no método
    public abstract IGenericSolution converteSubSolucao(IGenericSolution florest, LinkedList listaPlanosDeCorte, 
                                                                                     LinkedList listaBinsCompletos,
                                                                                     IDimensao2d tamanhoChapa/*, boolean isGrasp*/);
}
