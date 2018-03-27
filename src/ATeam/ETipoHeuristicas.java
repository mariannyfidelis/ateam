package ATeam;

public enum ETipoHeuristicas {
    
    //Tipo Heuristicas para o GRASP
    GRASP_2D_CONSTRUTIVO, Grasp2d_Melhoria, Grasp2dTree_Melhoria,
    
    //Tipo Heuristicas para o HHDHeuristic
    HHDHeuristic, HHDHeuristic_Melhoria, HHDHeuristic_Melhoria_Tree,
    
    //Tipo Heurísticas Simula Cliente
    SimulaGenetico,    
    
    //Tipo Heuristicas para Árvore Nária
    FirstFit, BestFit, Critical, Justification;
}
