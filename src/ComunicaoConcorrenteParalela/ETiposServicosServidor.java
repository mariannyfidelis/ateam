package ComunicaoConcorrenteParalela;

public enum ETiposServicosServidor {

    //Serviços  AGENTE --> SERVIDOR
    Inserir_Solucao, AtualizaSolucao,
    
    //Serviços  SERVIDOR --> AGENTE
    MelhorSolucao, PiorSolucao, Solucao_Aleatoria , Roleta;
}
