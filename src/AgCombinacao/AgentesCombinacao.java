package AgCombinacao;

import java.nio.ByteBuffer;
import algoritmosAgCombinacao.*;
import Heuristicas.Solucao;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import ComunicaoConcorrenteParalela.ServicoAgente;
import ComunicaoConcorrenteParalela.ObjetoComunicacao;

public class AgentesCombinacao extends Agentes{

    
    /*########################################################################################
                MÉTODOS QUE ESTABELECEM COMUNICAÇÃO COM O SERVIDOR
    ########################################################################################*/
   
    public AgentesCombinacao(int porta_comunicao, String name, ServicoAgente servico_, ETiposServicosAgentes operacaoAgente){
        
        this.porta_comunicacao = porta_comunicao;
        this.name = name;
        this.tipo_agente = servico_;
        this.operacao_agente = operacaoAgente;
        
        super.criaConexaoServidor(porta_comunicacao);
        //run();
    }
    
    @Override
    public void criaConexaoServidor(int porta_comunicacao){

        super.criaConexaoServidor(porta_comunicacao);
    }
        
    public void execute(ServicoAgente servicoAgente, ETiposServicosAgentes tipo_servico) {
       
       ObjetoComunicacao objeto;
       
       //Pega os dados do InputStream do Socket      
       //Processa informação do socket de entrada e processa informação
       objeto = processaInformacao(lerSocketChannel(socketChannel, null), tipo_servico);
  
       //Escrever o resultado para o socketChannel
       //Após o processamento é realizado novamente a serialização e enviar ao socket de saída
       escreveSocketChannel(serializaMensagem(objeto), socketChannel);
                       
       //Verificar se está implementado nos métodos
       //flush();
       
       //read.close();
       //write.close();
   }
    
   public ObjetoComunicacao processaInformacao(ByteBuffer byteBuffer, ETiposServicosAgentes tipo) throws IllegalArgumentException{

      Solucao solucao_ = new Solucao();
      ObjetoComunicacao objeto = new ObjetoComunicacao();
      objeto = deserializaMensagem(byteBuffer);
      
      switch(tipo){
         
         case TotalmenteAleatorioSolucao:
              solucao_ = TotalmenteAleatorioSolucao(objeto.getSolucao());
              objeto.setSolucao(solucao_);
         break;
             
         case AleatorioPior:
              solucao_ = AleatorioPior(objeto.getSolucao());
              objeto.setSolucao(solucao_);
         break;
             
         case AleatorioMelhor:
              solucao_ = AleatorioMelhor(objeto.getSolucao());
              objeto.setSolucao(solucao_);
         break;
                      
         case Melhor_Dois:
              solucao_ = Melhor_Dois(objeto.getSolucao());
              objeto.setSolucao(solucao_);
         break;
       
         case Melhor_Pior:
              solucao_ = Melhor_Pior(objeto.getSolucao());
              objeto.setSolucao(solucao_);
         break;
                                   
         case MelhorSol_Aleatorio:
              solucao_ = MelhorSol_Aleatorio(objeto.getSolucao());  
              objeto.setSolucao(solucao_);
         break;
                                                 
         case MelhorSol_MelhorIndividuo:
              solucao_ = MelhorSol_MelhorIndividuo(objeto.getSolucao());
              objeto.setSolucao(solucao_);
         break;
         
         case AleatorioMelhorS:
              solucao_ = AleatorioMelhorS(objeto.getSolucao()); 
              objeto.setSolucao(solucao_);
         break;
             
         case MelhorSol_Melhor_DoisIndv:
              solucao_ = MelhorSol_Melhor_DoisIndv(objeto.getSolucao()); 
              objeto.setSolucao(solucao_);
         break;
             
         case MelhorSol_Maior_MenorIndv:
              solucao_ = MelhorSol_Maior_MenorIndv(objeto.getSolucao());
              objeto.setSolucao(solucao_);
         break;
      }
      
      return objeto;
   } 
   
   @Override
   public ByteBuffer lerSocketChannel(SocketChannel socket, SelectionKey selectKey){//SelectionKey selectKey
        
       return (super.lerSocketChannel(socket, selectKey));
   }
    
   //Escrever de um Buffer para um SocketChannel
   @Override
   public void escreveSocketChannel(ByteBuffer byteBuffer, SocketChannel socket){
   
       super.escreveSocketChannel(byteBuffer, socket);
   }
    
    //Métodos para serializar e deserializar mensagens
   @Override
   public ByteBuffer serializaMensagem(ObjetoComunicacao objeto){ //Passa o objeto no caso SOLUÇÃO
    
       return (super.serializaMensagem(objeto));
   }
   
   @Override
   public ObjetoComunicacao  deserializaMensagem(ByteBuffer byteBuffer){
        
       return (super.deserializaMensagem(byteBuffer));
    }         
        
   /*########################################################################################
              MÉTODOS DOS AGENTES EXECUTAREM
   ########################################################################################*/
   
   //CombinacaoAleat1 
   public Solucao TotalmenteAleatorioSolucao(Solucao solution_memoria){

        //Seleciona Solução Aleatória
        //solucao = opSolucao.retornaSolucaoAleatoria(memoria).clone();
        this.solucao.setSolucao(solution_memoria);		
        
        if((solution_memoria == this.solucao)||(solution_memoria.equals(this.solucao))){
            System.out.println("PODE HAVER PROBLEMA !!!");
        }
        
        //Seleciona Indivíduos Aleatórios
        individuo1 = OperacoesSolucoes_Individuos.selecionaAleatorioIndividuo(solution_memoria);
        this.solucao.removeIndividuos(individuo1);
        
        individuo2 = OperacoesSolucoes_Individuos.selecionaAleatorioIndividuo(this.solucao);
                        
        while (individuo1.equals(individuo2)){
            individuo2 = OperacoesSolucoes_Individuos.selecionaAleatorioIndividuo(this.solucao);	
        }
        this.solucao.removeIndividuos(individuo2);

        System.out.println("Tamanho Indv1 --> "+individuo1.getSize());
        System.out.println("Tamanho Indv2 --> "+individuo2.getSize());

        this.filhos.setFilhos(AgentesOperadores.operadorCrossoverPonto1_v(individuo1, individuo2));
        //this.filhos.setFilhos(AgentesOperadores.operadorCrossoverPonto2(individuo1, individuo2));
    
        System.out.println("%%%%%%%%% Só para testar %%%%%%%%%%\n\n");
        System.out.println("FILHO 1 >>>> "+this.filhos.getFilho1().getListaItens());
        System.out.println("FILHO 2 >>>> "+this.filhos.getFilho2().getListaItens());
        
        this.solucao.adicionaIndividuo(this.filhos.getFilho1());
        this.solucao.adicionaIndividuo(this.filhos.getFilho2());
        
        //Verifica aqui mesmo se algum indivíduo criado extrapolou o limite da placa
        
        boolean capac_ind1 = this.filhos.getFilho1().isCapacidadeRespeitada();
        boolean capac_ind2 = this.filhos.getFilho2().isCapacidadeRespeitada();
        
        
        if((capac_ind1 == false) || (capac_ind2 ==  false)){
             this.solucao.setCapacidadeRespeitada(false);
        }
        else{
             this.solucao.setCapacidadeRespeitada(true);
        }
        
        //Atualizar a nova solução com os novos indivíduos gerados
        this.solucao.calculaFitness();
        
        System.out.println("Saindo normalmente !!!");

      
      return this.solucao;
    }
   
   //CombinacaoAleat2
   public Solucao AleatorioPior(Solucao solution_memoria){
		
        this.solucao.setSolucao(solution_memoria);
        
        
        if((solution_memoria == this.solucao)|| (solution_memoria.equals(this.solucao))){
            System.out.println("PODE HAVER PROBLEMA !!!");
        }
        
        //Selecionar Indivíduo aleatório
        individuo1 = OperacoesSolucoes_Individuos.selecionaAleatorioIndividuo(solution_memoria);
        this.solucao.removeIndividuos(individuo1);
        
        //Selecionar Pior Indivíduo
        individuo2 = OperacoesSolucoes_Individuos.selecionaMenorIndividuo(this.solucao);
                
        while (individuo1.equals(individuo2)){

                individuo2 = OperacoesSolucoes_Individuos.selecionaMenorIndividuo(this.solucao);
        }
        this.solucao.removeIndividuos(individuo2);

        System.out.println("Tamanho Indv1 --> "+individuo1.getSize());
        System.out.println("Tamanho Indv2 --> "+individuo2.getSize());

        this.filhos.setFilhos(AgentesOperadores.operadorCrossoverPonto1_v(individuo1, individuo2));
        //this.filhos.setFilhos(AgentesOperadores.operadorCrossoverPonto2(individuo1, individuo2));
        
        System.out.println("%%%%%%%%% Só para testar %%%%%%%%%%\n\n");
        System.out.println("FILHO 1 >>>> "+this.filhos.getFilho1().getListaItens());
        System.out.println("FILHO 2 >>>> "+this.filhos.getFilho2().getListaItens());
        
        this.solucao.adicionaIndividuo(this.filhos.getFilho1());
        this.solucao.adicionaIndividuo(this.filhos.getFilho2());
        
        //Verifica aqui mesmo se algum indivíduo criado extrapolou o limite da placa
        
        boolean capac_ind1 = this.filhos.getFilho1().isCapacidadeRespeitada();
        boolean capac_ind2 = this.filhos.getFilho2().isCapacidadeRespeitada();
        
        
        if((capac_ind1 == false) || (capac_ind2 ==  false)){
             this.solucao.setCapacidadeRespeitada(false);
        }
        else{
             this.solucao.setCapacidadeRespeitada(true);
        }
        
        this.solucao.calculaFitness();
        
        System.out.println("Saindo normalmente !!!");

      return this.solucao;
    }
   
   //CombinacaoAleat3
   public Solucao AleatorioMelhor(Solucao solution_memoria){
		
        this.solucao.setSolucao(solution_memoria);
      
        
        if((solution_memoria == this.solucao) || (solution_memoria.equals(this.solucao))){
            System.out.println("PODE HAVER PROBLEMA !!!");
        }
        
        //Selecionar Indivíduo aleatório
        individuo1 = OperacoesSolucoes_Individuos.selecionaAleatorioIndividuo(solution_memoria);
        this.solucao.removeIndividuos(individuo1);

        //Selecionar Melhor Indivíduo
        individuo2 = OperacoesSolucoes_Individuos.selecionaMaiorIndividuo(this.solucao);
        
        while (individuo1.equals(individuo2)){
           individuo2 = OperacoesSolucoes_Individuos.selecionaMaiorIndividuo(this.solucao);
        }
        this.solucao.removeIndividuos(individuo2);

        System.out.println("Tamanho Indv1 --> "+individuo1.getSize());
        System.out.println("Tamanho Indv2 --> "+individuo2.getSize());

        this.filhos.setFilhos(AgentesOperadores.operadorCrossoverPonto1_v(individuo1, individuo2));
        //this.filhos.setFilhos(AgentesOperadores.operadorCrossoverPonto2(individuo1, individuo2));
        
        System.out.println("%%%%%%%%% Só para testar %%%%%%%%%%\n\n");
        System.out.println("FILHO 1 >>>> "+this.filhos.getFilho1().getListaItens());
        System.out.println("FILHO 2 >>>> "+this.filhos.getFilho2().getListaItens());
        
        this.solucao.adicionaIndividuo(this.filhos.getFilho1());
        this.solucao.adicionaIndividuo(this.filhos.getFilho2());
        
        //Verifica aqui mesmo se algum indivíduo criado extrapolou o limite da placa
        
        boolean capac_ind1 = this.filhos.getFilho1().isCapacidadeRespeitada();
        boolean capac_ind2 = this.filhos.getFilho2().isCapacidadeRespeitada();
        
        
        if((capac_ind1 == false) || (capac_ind2 ==  false)){
             this.solucao.setCapacidadeRespeitada(false);
        }
        else{
             this.solucao.setCapacidadeRespeitada(true);
        }
        
        this.solucao.calculaFitness();
        
        System.out.println("Saindo normalmente !!!");

        if(solution_memoria.equals(solucao)){
             System.out.println("\n\nPODE HAVER PROBLEMAS !!! ");
        }
        
      return this.solucao;	
    }
   //CombinacaoAleat4
   public Solucao Melhor_Dois(Solucao solution_memoria){
	
        this.solucao.setSolucao(solution_memoria);					
        
        
        if((solution_memoria == this.solucao) || (solution_memoria.equals(this.solucao))){
            System.out.println("PODE HAVER PROBLEMA !!!");
        }
        
        //Selecionar Melhor Indivíduo
        individuo1 = OperacoesSolucoes_Individuos.selecionaMaiorIndividuo(solution_memoria);
        this.solucao.removeIndividuos(individuo1);
        
        //Selecionar Melhor Indivíduo
        individuo2 = OperacoesSolucoes_Individuos.selecionaMaiorIndividuo(this.solucao);
                
        while (individuo1.equals(individuo2)){
           individuo2 = OperacoesSolucoes_Individuos.selecionaMaiorIndividuo(this.solucao);
        }
        this.solucao.removeIndividuos(individuo2);

        System.out.println("Tamanho Indv1 --> "+individuo1.getSize());
        System.out.println("Tamanho Indv2 --> "+individuo2.getSize());

        this.filhos.setFilhos(AgentesOperadores.operadorCrossoverPonto1_v(individuo1, individuo2));
        //this.filhos.setFilhos(AgentesOperadores.operadorCrossoverPonto2(individuo1, individuo2));
        
        System.out.println("%%%%%%%%% Só para testar %%%%%%%%%%\n\n");
        System.out.println("FILHO 1 >>>> "+this.filhos.getFilho1().getListaItens());
        System.out.println("FILHO 2 >>>> "+this.filhos.getFilho2().getListaItens());
        
        this.solucao.adicionaIndividuo(this.filhos.getFilho1());
        this.solucao.adicionaIndividuo(this.filhos.getFilho2());
        
        //Verifica aqui mesmo se algum indivíduo criado extrapolou o limite da placa
        
        boolean capac_ind1 = this.filhos.getFilho1().isCapacidadeRespeitada();
        boolean capac_ind2 = this.filhos.getFilho2().isCapacidadeRespeitada();
        
        
        if((capac_ind1 == false) || (capac_ind2 ==  false)){
             this.solucao.setCapacidadeRespeitada(false);
        }
        else{
             this.solucao.setCapacidadeRespeitada(true);
        }
        
        this.solucao.calculaFitness();
        
        System.out.println("Saindo normalmente !!!");

      return this.solucao;	
   }
   
   //CombinacaoAleat5
   public Solucao Melhor_Pior(Solucao solution_memoria){

        this.solucao.setSolucao(solution_memoria);				

        
        if((solution_memoria == this.solucao) || (solution_memoria.equals(this.solucao))){
            System.out.println("PODE HAVER PROBLEMA !!!");
        }
        
        //Selecionar Melhor Indivíduo
        individuo1 = OperacoesSolucoes_Individuos.selecionaMaiorIndividuo(solution_memoria);
        this.solucao.removeIndividuos(individuo1);
        
        //Selecionar Melhor Indivíduo
        individuo2 = OperacoesSolucoes_Individuos.selecionaMenorIndividuo(this.solucao);
                
        while (individuo1.equals(individuo2)){
            individuo2 = OperacoesSolucoes_Individuos.selecionaMenorIndividuo(this.solucao);
        }
        this.solucao.removeIndividuos(individuo2);
        
        System.out.println("Tamanho Indv1 --> "+individuo1.getSize());
        System.out.println("Tamanho Indv2 --> "+individuo2.getSize());

        this.filhos.setFilhos(AgentesOperadores.operadorCrossoverPonto1_v(individuo1, individuo2));
        //this.filhos.setFilhos(AgentesOperadores.operadorCrossoverPonto2(individuo1, individuo2));
        
        System.out.println("%%%%%%%%% Só para testar %%%%%%%%%%\n\n");
        System.out.println("FILHO 1 >>>> "+this.filhos.getFilho1().getListaItens());
        System.out.println("FILHO 2 >>>> "+this.filhos.getFilho2().getListaItens());
        
        this.solucao.adicionaIndividuo(this.filhos.getFilho1());
        this.solucao.adicionaIndividuo(this.filhos.getFilho2());
        
        //Verifica aqui mesmo se algum indivíduo criado extrapolou o limite da placa
        
        boolean capac_ind1 = this.filhos.getFilho1().isCapacidadeRespeitada();
        boolean capac_ind2 = this.filhos.getFilho2().isCapacidadeRespeitada();
        
        
        if((capac_ind1 == false) || (capac_ind2 ==  false)){
             this.solucao.setCapacidadeRespeitada(false);
        }
        else{
             this.solucao.setCapacidadeRespeitada(true);
        }
        
        this.solucao.calculaFitness();
        
        System.out.println("Saindo normalmente !!!");

      return this.solucao;	
    }
   
   //CombinacaoMelhor_S1
   
   public Solucao MelhorSol_Aleatorio(Solucao solution_memoria){

        this.solucao.setSolucao(solution_memoria);

        
        if((solution_memoria == this.solucao) || (solution_memoria.equals(this.solucao))){
            System.out.println("PODE HAVER PROBLEMA !!!");
        }
        
        //Seleciona Indivíduos Aleatórios
        individuo1 = OperacoesSolucoes_Individuos.selecionaAleatorioIndividuo(solution_memoria);
        this.solucao.removeIndividuos(individuo1);
        
        individuo2 = OperacoesSolucoes_Individuos.selecionaAleatorioIndividuo(this.solucao);
        
        while (individuo1.equals(individuo2)){
            individuo2 = OperacoesSolucoes_Individuos.selecionaAleatorioIndividuo(this.solucao);	
        }
        this.solucao.removeIndividuos(individuo2);
        
        System.out.println("Tamanho Indv1 --> "+individuo1.getSize());
        System.out.println("Tamanho Indv2 --> "+individuo2.getSize());

        this.filhos.setFilhos(AgentesOperadores.operadorCrossoverPonto1_v(individuo1, individuo2));
        //this.filhos.setFilhos(AgentesOperadores.operadorCrossoverPonto2(individuo1, individuo2));
        
        System.out.println("%%%%%%%%% Só para testar %%%%%%%%%%\n\n");
        System.out.println("FILHO 1 >>>> "+this.filhos.getFilho1().getListaItens());
        System.out.println("FILHO 2 >>>> "+this.filhos.getFilho2().getListaItens());
        
        this.solucao.adicionaIndividuo(this.filhos.getFilho1());
        this.solucao.adicionaIndividuo(this.filhos.getFilho2());
        
        //Verifica aqui mesmo se algum indivíduo criado extrapolou o limite da placa
        
        boolean capac_ind1 = this.filhos.getFilho1().isCapacidadeRespeitada();
        boolean capac_ind2 = this.filhos.getFilho2().isCapacidadeRespeitada();
        
        
        if((capac_ind1 == false) || (capac_ind2 ==  false)){
             this.solucao.setCapacidadeRespeitada(false);
        }
        else{
             this.solucao.setCapacidadeRespeitada(true);
        }
        
        this.solucao.calculaFitness();
        
        System.out.println("Saindo normalmente !!!");

      return this.solucao;
    }
   //CombinacaoMelhor S_2
   
   public Solucao MelhorSol_MelhorIndividuo(Solucao solution_memoria){
		
        this.solucao.setSolucao(solution_memoria);		

        
        if((solution_memoria == this.solucao) || (solution_memoria.equals(this.solucao))){
            System.out.println("PODE HAVER PROBLEMA !!!");
        }
        
        //Selecionar Indivíduo aleatório
        individuo1 = OperacoesSolucoes_Individuos.selecionaAleatorioIndividuo(solution_memoria);
        this.solucao.removeIndividuos(individuo1);
        
        //Selecionar Pior Indivíduo
        individuo2 = OperacoesSolucoes_Individuos.selecionaMenorIndividuo(this.solucao);
        
        while (individuo1.equals(individuo2)){
            individuo2 = OperacoesSolucoes_Individuos.selecionaMenorIndividuo(this.solucao);
        }
        this.solucao.removeIndividuos(individuo2);

        System.out.println("Tamanho Indv1 --> "+individuo1.getSize());
        System.out.println("Tamanho Indv2 --> "+individuo2.getSize());

        this.filhos.setFilhos(AgentesOperadores.operadorCrossoverPonto1_v(individuo1, individuo2));
        //this.filhos.setFilhos(AgentesOperadores.operadorCrossoverPonto2(individuo1, individuo2));
        
        System.out.println("%%%%%%%%% Só para testar %%%%%%%%%%\n\n");
        System.out.println("FILHO 1 >>>> "+this.filhos.getFilho1().getListaItens());
        System.out.println("FILHO 2 >>>> "+this.filhos.getFilho2().getListaItens());
        
        this.solucao.adicionaIndividuo(this.filhos.getFilho1());
        this.solucao.adicionaIndividuo(this.filhos.getFilho2());
        
        //Verifica aqui mesmo se algum indivíduo criado extrapolou o limite da placa
        
        boolean capac_ind1 = this.filhos.getFilho1().isCapacidadeRespeitada();
        boolean capac_ind2 = this.filhos.getFilho2().isCapacidadeRespeitada();
        
        
        if((capac_ind1 == false) || (capac_ind2 ==  false)){
             this.solucao.setCapacidadeRespeitada(false);
        }
        else{
             this.solucao.setCapacidadeRespeitada(true);
        }
        
        this.solucao.calculaFitness();
        
        System.out.println("Saindo normalmente !!!");

      return this.solucao;
    }
    
   //CombinacaoMelhor S_3
   public Solucao AleatorioMelhorS(Solucao solution_memoria){
	
        this.solucao.setSolucao(solution_memoria);		
        
        
        if((solution_memoria == this.solucao) || (solution_memoria.equals(this.solucao))){
            System.out.println("PODE HAVER PROBLEMA !!!");
        }
        
        //Selecionar Indivíduo aleatório
        individuo1 = OperacoesSolucoes_Individuos.selecionaAleatorioIndividuo(solution_memoria);
        this.solucao.removeIndividuos(individuo1);
        
        //Selecionar Melhor Indivíduo
        individuo2 = OperacoesSolucoes_Individuos.selecionaMaiorIndividuo(this.solucao);
        
        while (individuo1.equals(individuo2)){
             individuo2 = OperacoesSolucoes_Individuos.selecionaMaiorIndividuo(this.solucao);
        }
        this.solucao.removeIndividuos(individuo2);

        System.out.println("Tamanho Indv1 --> "+individuo1.getSize());
        System.out.println("Tamanho Indv2 --> "+individuo2.getSize());

        this.filhos.setFilhos(AgentesOperadores.operadorCrossoverPonto1_v(individuo1, individuo2));
        //this.filhos.setFilhos(AgentesOperadores.operadorCrossoverPonto2(individuo1, individuo2));
        
        System.out.println("%%%%%%%%% Só para testar %%%%%%%%%%\n\n");
        System.out.println("FILHO 1 >>>> "+this.filhos.getFilho1().getListaItens());
        System.out.println("FILHO 2 >>>> "+this.filhos.getFilho2().getListaItens());
        
        this.solucao.adicionaIndividuo(this.filhos.getFilho1());
        this.solucao.adicionaIndividuo(this.filhos.getFilho2());
        
        //Verifica aqui mesmo se algum indivíduo criado extrapolou o limite da placa
        
        boolean capac_ind1 = this.filhos.getFilho1().isCapacidadeRespeitada();
        boolean capac_ind2 = this.filhos.getFilho2().isCapacidadeRespeitada();
        
        
        if((capac_ind1 == false) || (capac_ind2 ==  false)){
             this.solucao.setCapacidadeRespeitada(false);
        }
        else{
             this.solucao.setCapacidadeRespeitada(true);
        }
        
        this.solucao.calculaFitness();
        
        System.out.println("Saindo normalmente !!!");

      return this.solucao;	
    }
   
   //CombinacaoMelhor S_4 
   public Solucao MelhorSol_Melhor_DoisIndv(Solucao solution_memoria){
	
        this.solucao.setSolucao(solution_memoria);			
       
        
        if((solution_memoria == this.solucao) || (solution_memoria.equals(this.solucao))){
            System.out.println("PODE HAVER PROBLEMA !!!");
        }
        
        //Selecionar Melhor Indivíduo
        individuo1 = OperacoesSolucoes_Individuos.selecionaMaiorIndividuo(solution_memoria);
        this.solucao.removeIndividuos(individuo1);
        
        //Selecionar Melhor Indivíduo
        individuo2 = OperacoesSolucoes_Individuos.selecionaMaiorIndividuo(this.solucao);
        
        while (individuo1.equals(individuo2)){
           individuo2 = OperacoesSolucoes_Individuos.selecionaMaiorIndividuo(this.solucao);
        }
        
        this.solucao.removeIndividuos(individuo2);
        System.out.println("Tamanho Indv1 --> "+individuo1.getSize());
        System.out.println("Tamanho Indv2 --> "+individuo2.getSize());

        this.filhos.setFilhos(AgentesOperadores.operadorCrossoverPonto1_v(individuo1, individuo2));
        //this.filhos.setFilhos(AgentesOperadores.operadorCrossoverPonto2(individuo1, individuo2));
        
        System.out.println("%%%%%%%%% Só para testar %%%%%%%%%%\n\n");
        System.out.println("FILHO 1 >>>> "+this.filhos.getFilho1().getListaItens());
        System.out.println("FILHO 2 >>>> "+this.filhos.getFilho2().getListaItens());
        
        this.solucao.adicionaIndividuo(this.filhos.getFilho1());
        this.solucao.adicionaIndividuo(this.filhos.getFilho2());
        
        //Verifica aqui mesmo se algum indivíduo criado extrapolou o limite da placa
        
        boolean capac_ind1 = this.filhos.getFilho1().isCapacidadeRespeitada();
        boolean capac_ind2 = this.filhos.getFilho2().isCapacidadeRespeitada();
        
        
        if((capac_ind1 == false) || (capac_ind2 ==  false)){
             this.solucao.setCapacidadeRespeitada(false);
        }
        else{
             this.solucao.setCapacidadeRespeitada(true);
        }
        
        this.solucao.calculaFitness();
        
        System.out.println("Saindo normalmente !!!");

       return this.solucao;	
    }
   
   //CombinacaoMelhor S_5 
   public Solucao MelhorSol_Maior_MenorIndv(Solucao solution_memoria){
	
        this.solucao.setSolucao(solution_memoria);			
        
        
        if((solution_memoria == this.solucao) || (solution_memoria.equals(this.solucao))){
            System.out.println("PODE HAVER PROBLEMA !!!");
        }
        
        //Selecionar Melhor Indivíduo
        individuo1 = OperacoesSolucoes_Individuos.selecionaMaiorIndividuo(solution_memoria);
        this.solucao.removeIndividuos(individuo1);
        
        //Selecionar Melhor Indivíduo
        individuo2 = OperacoesSolucoes_Individuos.selecionaMenorIndividuo(this.solucao);
        
        while (individuo1.equals(individuo2)){
            individuo2 = OperacoesSolucoes_Individuos.selecionaMenorIndividuo(this.solucao);
        }
        this.solucao.removeIndividuos(individuo2);
        System.out.println("Tamanho Indv1 --> "+individuo1.getSize());
        System.out.println("Tamanho Indv2 --> "+individuo2.getSize());

        this.filhos.setFilhos(AgentesOperadores.operadorCrossoverPonto1_v(individuo1, individuo2));
        //this.filhos.setFilhos(AgentesOperadores.operadorCrossoverPonto2(individuo1, individuo2));
        
        System.out.println("%%%%%%%%% Só para testar %%%%%%%%%%\n\n");
        System.out.println("FILHO 1 >>>> "+this.filhos.getFilho1().getListaItens());
        System.out.println("FILHO 2 >>>> "+this.filhos.getFilho2().getListaItens());
        
        this.solucao.adicionaIndividuo(this.filhos.getFilho1());
        this.solucao.adicionaIndividuo(this.filhos.getFilho2());
        
        //Verifica aqui mesmo se algum indivíduo criado extrapolou o limite da placa
        
        boolean capac_ind1 = this.filhos.getFilho1().isCapacidadeRespeitada();
        boolean capac_ind2 = this.filhos.getFilho2().isCapacidadeRespeitada();
        
        
        if((capac_ind1 == false) || (capac_ind2 ==  false)){
             this.solucao.setCapacidadeRespeitada(false);
        }
        else{
             this.solucao.setCapacidadeRespeitada(true);
        }
        
        this.solucao.calculaFitness();
        
        System.out.println("Saindo normalmente !!!");

       return this.solucao;	
   }
   
   /*########################################################################################
              MÉTODOS DE EXECUÇÃO PRINCIPAL PARA OS AGENTES 
   ########################################################################################*/
   
   
    public void run() {
        String args[] = null;
        main(args);    
    }
    public static void main(String args[]){
    //Chama Conexão com o Servidor
        
               
       AgentesCombinacao agente = new AgentesCombinacao(3000, "AGENTE-FUNÇÃO-COMBINAÇÃO", ServicoAgente.Combinacao, 
                                                       ETiposServicosAgentes.TotalmenteAleatorioSolucao);
            //.criaConexaoServidor(3000);
        
        //Verificar o encerramento da conexão 
    }
}