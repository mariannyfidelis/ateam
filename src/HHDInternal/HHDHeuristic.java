package HHDInternal;

import HHDComparadores.*;
import HHDInterfaces.IBin;
import java.util.Iterator;
import java.io.IOException;
import HHDInterfaces.ISobra;
import java.util.LinkedList;
import java.util.Collections;
import HHDInterfaces.IPedido;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import HHDInterfaces.IPecaPronta;
import HHDInterfaces.IDimensao2d;
import HHDInterfaces.IListaPedidos;
import HHDInterfaces.IGenericSolution;
import HHDInterfaces.ISolutionProvider;
import HHDInterfaces.ISolutionInterface;

public class HHDHeuristic implements ISolutionProvider{

    private int estrategia; 
    private boolean permiteRotacao;

    public static final int MAX_MENOR_PEDACO = 0;
    public static final int MAX_MAIOR_AREA = 1;
    public static final int MAX_MENOR_DIMENSAO = 2;

    /*CRIADO SÓ PARA REALIZAÇÃO DE TESTES*/
    ISolutionInterface resultado;
    
    public HHDHeuristic(){
    
    	//permiteRotacao = false;
        permiteRotacao = true;
    	estrategia = MAX_MENOR_PEDACO;
    }
    
    //public ISolutionInterface criaSolucao(IListaPedidos pedido, IDimensao2d tamanhoChapa){
    public SolucaoHeuristica criaSolucao(LinkedList<Pedidos> pedido, IDimensao2d tamanhoChapa){
        
	/*ISolutionInterface solucao;
	solucao = obtenhaSolucao(tamanhoChapa, pedido);*/
        
        SolucaoHeuristica solucao;
        solucao = obtenhaSolucao(tamanhoChapa, pedido);
					
	return solucao;
    }
    
    @Override
    public ISolutionInterface criaSolucao(IListaPedidos pedido, IDimensao2d tamanhoChapa) {
        
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    //private ISolutionInterface obtenhaSolucao(IDimensao2d tamanhoChapa, IListaPedidos pedido){
    private SolucaoHeuristica obtenhaSolucao(IDimensao2d tamanhoChapa, LinkedList<Pedidos> pedido){
	
	 LinkedList<Pedidos> listaPedidos = pedido;

	 /*while(pedido.hasNext())
		listaPedidos.add(pedido.next());*/
		
	 Collections.sort(listaPedidos, new ComparadorAreas());
	 Collections.reverse(listaPedidos);		
	
         Iterator<Pedidos> iterator = listaPedidos.iterator();
         
         System.out.println("\n\nPedidos Ordenados por Área\n");
         
         while(iterator.hasNext()){
                
              Pedidos pedidos = iterator.next();
                    
              System.out.println("Id -- "+pedidos.id()+"\t "+pedidos.retorneDimensao().retorneBase()
                                                +"  x  "+pedidos.retorneDimensao().retorneAltura());
         }
         
         System.out.println("");
	 SolucaoHeuristica resultadoHeuristica = new SolucaoHeuristica(pedido, tamanhoChapa);
		
         int contador = 0;
	 
         while(!listaPedidos.isEmpty()){
             
             System.out.println("\nContador de Bins --> "+(++contador));
		
             resultadoHeuristica.adicionarPlanoDeCorte(corteChapa(new PedacoDisponivel(new Ponto(0,0), tamanhoChapa, 1), listaPedidos));
             
             System.out.println("\nA Bin  "+contador+" foi completada !");
         }
         
	 IGenericSolution solucaoConvertida = SolutionConversorFactory.getInstance().newConversor().converteParaSolucao(resultadoHeuristica);
		
         //Será comentado para realização de testes
	 /*ISolutionInterface*/ resultado = (ISolutionInterface) new Solucao(solucaoConvertida);
	 /*return resultado;*/
         
         return resultadoHeuristica;
    }

    private Bin corteChapa(PedacoDisponivel pedaco, LinkedList<Pedidos> listaPedidos){ 
	
        //System.out.println("Pedaço Disponível ---> "+pedaco.retorneBase()+" x "+pedaco.retorneAltura());
        
        System.out.println("\n############## Informação Pedaço Disponível ################### \n");
        System.out.println("Altura --> "+pedaco.retorneAltura()); 
        System.out.println("Base --> "+pedaco.retorneBase());
        System.out.println("Ponto Inferior Esquerdo --> ( "+pedaco.getPontoInferiorEsquerdo().getX()+", "
                                                           +pedaco.getPontoInferiorEsquerdo().getY()+")");
        
        System.out.println("Ponto Superior Direito --> ( "+pedaco.getPontoSuperiorDireito().getX()+", "
                                                          +pedaco.getPontoSuperiorDireito().getY()+")");
 			
	 Bin planoDeCorte = new Bin(); //Cria novo plano de corte

	 ListIterator iteradorPedidos = listaPedidos.listIterator();
         
	 IPedido pedidoAtual = maiorPedidoQueCabe(pedaco, iteradorPedidos); //Sempre pegará o maior pedido que cabe na menor sobra ou não
         
         System.out.println("Informações do Maior Pedido que cabe");
         
         if(pedidoAtual == null) System.out.println("Return igual a NULL será adicionado as sobras !");
         
	 if(pedidoAtual != null){
             
             System.out.println("Pedido selecionado ---> (W x H) --> ("+pedidoAtual.retorneDimensao().retorneBase()+" x "
                                                          +pedidoAtual.retorneDimensao().retorneAltura()+" )");
                
             System.out.println("\nQuantidade Antes --> "+ pedidoAtual.quantidade());	
		
             pedidoAtual.atendaUmPedido();
		
             System.out.println("Quantidade Depois --> "+ (pedidoAtual.quantidade() - pedidoAtual.getPedidosAtendidos()));	
		
             if(pedidoAtual.quantidade() == pedidoAtual.getPedidosAtendidos()){
		
                  iteradorPedidos.remove();
             }
                
            LinkedList listaDeCortes = new LinkedList(), 
	               listaDePedacosRestantes = new LinkedList();
			
	    Peca pecaCortada = this.efetueMelhorCorte(pedidoAtual, pedaco, listaDeCortes, listaDePedacosRestantes);
                
            System.out.println("\nLista de Cortes --> "+listaDeCortes.toArray().length);
            System.out.println("Lista de Pedaços Restantes --> "+ listaDePedacosRestantes.toArray().length);
                
            planoDeCorte.listaCortes.addAll(listaDeCortes);
            planoDeCorte.adicionePeca(pecaCortada);
 			
            if(listaDePedacosRestantes.size() == 2){
		
                System.out.println("Ficaram 2 - dois pedaços restante !");
                PedacoDisponivel menorPedaco = (PedacoDisponivel) 
			                Collections.min(listaDePedacosRestantes, 
			  		              new ComparadorISobra());
	 	PedacoDisponivel maiorPedaco = (PedacoDisponivel) 
			                Collections.max(listaDePedacosRestantes, 
				                	  new ComparadorISobra());
	 			
	 	planoDeCorte.integrar(this.corteChapa(menorPedaco, listaPedidos));
	 	planoDeCorte.integrar(this.corteChapa(maiorPedaco, listaPedidos));
            }
 		
            else if(listaDePedacosRestantes.size() == 1){
                    
                System.out.println("Ficou 1 - um pedaço restante !");
                        
                PedacoDisponivel pedD = (PedacoDisponivel) listaDePedacosRestantes.getFirst();
                Ponto infE = pedD.getPontoInferiorEsquerdo();
                Ponto supD = pedD.getPontoSuperiorDireito();
                        
                Corte corte = (Corte) listaDeCortes.getFirst();
                Ponto pntChapa= corte.getPontoChapaCortada();
                        
                System.out.println("\n### Informação do Corte ###\n");
                System.out.println("Ponto de Corte --> ( "+pntChapa.getX()+", "+pntChapa.getY()+" )");
                System.out.println("Corte vertical -> "+ corte.eVertical());
                        
                planoDeCorte.integrar(this.corteChapa((PedacoDisponivel) listaDePedacosRestantes.getFirst(), listaPedidos));
            }
	}
	else
		planoDeCorte.adicionarSobra(pedaco);
		
	return planoDeCorte;
   }

   private Peca efetueMelhorCorte(IPedido pedido, PedacoDisponivel pedaco, LinkedList listaDeCortes, LinkedList listaDePedacosRestantes){   
            
       //IMplementar corte vertical incrementando as outras estratégias !!!
       boolean primeiroCorteVertical = corteVerticalEMelhor(pedido, pedaco);

        Corte corte1 = new Corte();
        Corte corte2 = new Corte();

        float pos1, pos2, inicio1, inicio2, tamanho1, tamanho2;

        if(primeiroCorteVertical) {
            
            System.out.println("\nCorte vertical é melhor");
            
            pos1 = pedido.retorneDimensao().retorneBase();
            tamanho1 = pedaco.retorneAltura();
            pos2 = pedido.retorneDimensao().retorneAltura();
            tamanho2 = pedido.retorneDimensao().retorneBase();
        }
        else {
            
            System.out.println("\nCorte horizontal é melhor");
            
            pos1 = pedido.retorneDimensao().retorneAltura();
            tamanho1 = pedaco.retorneBase();
            pos2 = pedido.retorneDimensao().retorneBase();
            tamanho2 = pedido.retorneDimensao().retorneAltura();
        }

        //registraCorte(float pos, Ponto pontoChapa, boolean corteVertical,float tamanho, int idCorte)

        //Verificar como adaptar para cada um tipo de corte e adaptar Ponto Inferior Esquerdo 
        corte1.registraCorte(pos1, pedaco.getPontoInferiorEsquerdo(), primeiroCorteVertical, tamanho1, pedaco.getId());
        corte2.registraCorte(pos2, pedaco.getPontoInferiorEsquerdo(), !primeiroCorteVertical, tamanho2, pedaco.getId());

        PedacoDisponivel AreaRestante1, AreaRestante2;
        AreaRestante1 = pedaco.corte(corte1);
        AreaRestante2 = pedaco.corte(corte2);

        if(AreaRestante1 != null){
            
            System.out.println("Vou adicionar a sobra 1 !!!");
            System.out.println("\nSobra 1 gerada com PInfE ("+AreaRestante1.getPontoInferiorEsquerdo().getX()+","+
                                  AreaRestante1.getPontoInferiorEsquerdo().getY()+" ) e PSupDir("+AreaRestante1.getPontoSuperiorDireito().getX()+","+
                                  AreaRestante1.getPontoSuperiorDireito().getY()+")");
            
            listaDePedacosRestantes.add(AreaRestante1);
            listaDeCortes.add(corte1);
        }
        if(AreaRestante2 != null){
            
            System.out.println("Vou adicionar a sobra 2 !!!");
            System.out.println("\nSobra 2 gerada com PInfE ("+AreaRestante2.getPontoInferiorEsquerdo().getX()+","+
                                  AreaRestante2.getPontoInferiorEsquerdo().getY()+") e PSupDir("+AreaRestante2.getPontoSuperiorDireito().getX()+","+
                                  AreaRestante2.getPontoSuperiorDireito().getY()+")");
            
            listaDePedacosRestantes.add(AreaRestante2);
            listaDeCortes.add(corte2);
        }

        Peca pecaCortada = null;

        try {
            pecaCortada = new Peca(pedaco, pedido, permiteRotacao);
        }
        catch (PecaInvalidaException e){
        }

       return pecaCortada;
    }
	
    /* ESTA FUNÇÃO ABAIXO FUNCIONA APENAS PARA ESTRATEGIA 1 */
    private boolean corteVerticalEMelhor(IPedido pedido, PedacoDisponivel pedaco){

        AreaRetangular areaCorteVertical1 = new AreaRetangular(pedido.retorneDimensao().retorneBase(),
                                 pedaco.retorneAltura() - pedido.retorneDimensao().retorneAltura());

        AreaRetangular areaCorteVertical2 = new AreaRetangular(pedaco.retorneBase() - pedido.retorneDimensao().retorneBase(),
                                 pedaco.retorneAltura());

        AreaRetangular menorVertical;

        if(areaCorteVertical1.retorneArea() < areaCorteVertical2.retorneArea())
                menorVertical = areaCorteVertical1;
        else
                menorVertical = areaCorteVertical2;

        AreaRetangular areaCorteHorizontal1 = new AreaRetangular(pedaco.retorneBase(),
                 pedaco.retorneAltura() - pedido.retorneDimensao().retorneAltura());
        AreaRetangular areaCorteHorizontal2 = new AreaRetangular(pedaco.retorneBase() - pedido.retorneDimensao().retorneBase(),
                        pedido.retorneDimensao().retorneAltura());

        AreaRetangular menorHorizontal;

        if(areaCorteHorizontal1.retorneArea() < areaCorteHorizontal2.retorneArea())
                menorHorizontal = areaCorteHorizontal1;
        else
                menorHorizontal = areaCorteHorizontal2;

        if(menorVertical.retorneArea() > menorHorizontal.retorneArea())
                return true;

        return false;
    }


    private IPedido maiorPedidoQueCabe(PedacoDisponivel pedaco, ListIterator iteradorPedidos){

        int cabe_return = -1;
        IPedido maior, maior2, maior3;

        while(iteradorPedidos.hasNext()){

            maior = (IPedido) iteradorPedidos.next();
            cabe_return = pedaco.cabePeca(maior.retorneDimensao(), permiteRotacao);

            if(cabe_return == 0 || cabe_return == 1){

                if(cabe_return == 0){

                    System.out.println("Retorna o pedido sem rotação !");
                    return maior;
                }
                else{

                    System.out.println("Retorna o pedido rotacionado !");

                    return new Pedidos(maior.id(), maior.retorneDimensao().retorneAltura(),maior.retorneDimensao().retorneBase());


                    //return maior2;
                }
            }

        }

        return null;
    }

    /* Abaixo, a implementacao da "solucaoParcial" para o problema do corte
     * 
     * @param listaSobras possui objetos que atendem à interface ISobra, e para os quais serao gerados planos de corte
     * @param tamanhoChapa informa as dimensões da chapa, caso acabem os elementos da lista de sobras e ainda existam
     *        elementos a serem cortados 
     */
    @Override
    public void solucaoParcial(LinkedList listaPecas, LinkedList listaSobras, IDimensao2d tamanhoChapa){

        Collections.sort(listaSobras, new ComparadorISobra());

        Collections.sort(listaPecas, new ComparadorIPecaPronta());
        Collections.reverse(listaPecas);

        ListIterator iteradorSobras = listaSobras.listIterator();

        LinkedList listaPlanosDeCorte = new LinkedList();

        PlanoDeCorte plano;

        while(iteradorSobras.hasNext()){
            
            ISobra sobraAtual = (ISobra) iteradorSobras.next();
            plano =  this.corteChapa(sobraAtual, listaPecas);
            listaPlanosDeCorte.add(plano);
        }

        PlanoDeCorte binCompleto;
        LinkedList listaBinsCompletos = new LinkedList();

        while(!listaPecas.isEmpty()){
            
            binCompleto = corteChapa((ISobra)(new PedacoDisponivel(new Ponto(0,0),tamanhoChapa,1)),listaPecas);
            listaBinsCompletos.add(binCompleto);
        }

        /*SolutionConversorFactory.getInstance().newConversor().converteSubSolucao(listaPlanosDeCorte, 
                        listaBinsCompletos, tamanhoChapa);
         * 
         */
    }

    private PlanoDeCorte corteChapa(ISobra sobraAtual, LinkedList listaPecas){
        
        ListIterator iteradorPecas = listaPecas.listIterator();
        
        IPecaPronta pecaAtual = maiorPecaQueCabe(sobraAtual, iteradorPecas);
        
        PlanoDeCorte planoDeCorte = new PlanoDeCorte(sobraAtual);  

        if(pecaAtual != null){
            
            pecaAtual.retireUmaPeca(); //Este metodo define que uma das pecas representada por "pecaAtual" esta cortada

                if(pecaAtual.getPecasDisponiveis() == 0)
                        iteradorPecas.remove();

                LinkedList listaDeCortes = new LinkedList(), listaDePedacosRestantes = new LinkedList();

                this.efetueCorte(pecaAtual, sobraAtual, listaDeCortes, listaDePedacosRestantes);
                planoDeCorte.adicioneCortes(listaDeCortes);
                planoDeCorte.adicionePeca(pecaAtual);

                if(listaDePedacosRestantes.size() == 2)
                {
                        ISobra menorPedaco = (ISobra)Collections.min(listaDePedacosRestantes, new ComparadorISobra());
                        ISobra maiorPedaco = (ISobra)Collections.max(listaDePedacosRestantes, new ComparadorISobra());

                        planoDeCorte.integrar(this.corteChapa(menorPedaco, listaPecas));
                        planoDeCorte.integrar(this.corteChapa(maiorPedaco, listaPecas));
                }
                else if(listaDePedacosRestantes.size() == 1)
                        planoDeCorte.integrar(this.corteChapa((ISobra) listaDePedacosRestantes.getFirst(), listaPecas));
        }
        else
                planoDeCorte.adicionarSobra(sobraAtual);

        return planoDeCorte;
    }


    private void efetueCorte(IPecaPronta pedido, ISobra pedaco, LinkedList listaDeCortes, LinkedList listaDePedacosRestantes) {
            boolean primeiroCorteVertical = corteVerticalEMelhor(pedido, pedaco);

            float pos1, pos2, inicio1, inicio2, tamanho1, tamanho2;

            if(primeiroCorteVertical) {
                    pos1 = pedido.retorneDimensao().retorneBase();
                    tamanho1 = pedaco.retorneAltura();
                    pos2 = pedido.retorneDimensao().retorneAltura();
                    tamanho2 = pedido.retorneDimensao().retorneBase();

            }
            else {
                    pos1 = pedido.retorneDimensao().retorneAltura();
                    tamanho1 = pedaco.retorneBase();
                    pos2 = pedido.retorneDimensao().retorneBase();
                    tamanho2 = pedido.retorneDimensao().retorneAltura();
            }

            Corte corte1 = new Corte(pos1, pedaco.getPontoInferiorEsquerdo(), primeiroCorteVertical,
                            tamanho1, pedaco.getId());
            Corte corte2 = new Corte(pos2, pedaco.getPontoInferiorEsquerdo(), !primeiroCorteVertical, 
                            tamanho2, pedaco.getId());

            PedacoDisponivel pedacoCortado, AreaRestante1, AreaRestante2;
            pedacoCortado = new PedacoDisponivel(pedaco.getPontoInferiorEsquerdo(), pedaco.getPontoSuperiorDireito(), pedaco.getId());		
            AreaRestante1 = pedacoCortado.corte(corte1);   
            AreaRestante2 = pedacoCortado.corte(corte2);

            if(AreaRestante1 != null)
            {
                    listaDePedacosRestantes.add(AreaRestante1);
                    listaDeCortes.add(corte1);
            }
            if(AreaRestante2 != null)
            {
                    listaDePedacosRestantes.add(AreaRestante2);
                    listaDeCortes.add(corte2);
            }

            pedido.setPosicao(pedaco.getPontoInferiorEsquerdo());
    }

	
    //Este código e uma duplicata do código para as outras interfaces. ELIMINAR UMA DAS DUAS.
    private boolean corteVerticalEMelhor(IPecaPronta pedido, ISobra pedaco){

            AreaRetangular areaCorteVertical1 = new AreaRetangular(pedido.retorneDimensao().retorneBase(),
                            pedaco.retorneAltura() - pedido.retorneDimensao().retorneAltura());

            AreaRetangular areaCorteVertical2 = new AreaRetangular(pedaco.retorneBase() - 
                            pedido.retorneDimensao().retorneBase(), pedaco.retorneAltura());

            AreaRetangular menorVertical;

            if(areaCorteVertical1.retorneArea() < areaCorteVertical2.retorneArea())
                    menorVertical = areaCorteVertical1;
            else
                    menorVertical = areaCorteVertical2;

            AreaRetangular areaCorteHorizontal1 = new AreaRetangular(pedaco.retorneBase(),
                            pedaco.retorneAltura() - pedido.retorneDimensao().retorneAltura());
            AreaRetangular areaCorteHorizontal2 = new AreaRetangular(pedaco.retorneBase() - 
                            pedido.retorneDimensao().retorneBase(), pedido.retorneDimensao().retorneAltura());

            AreaRetangular menorHorizontal;

            if(areaCorteHorizontal1.retorneArea() < areaCorteHorizontal2.retorneArea()) 
                    menorHorizontal = areaCorteHorizontal1;
            else
                    menorHorizontal = areaCorteHorizontal2;

            if(menorVertical.retorneArea() > menorHorizontal.retorneArea())
                    return true;

            return false;
    }

    private IPecaPronta maiorPecaQueCabe(ISobra pedaco, ListIterator iteradorPedidos){

        int cabe_return = -1, id;

        IPecaPronta maior;

        while(iteradorPedidos.hasNext()){

            maior = (IPecaPronta)iteradorPedidos.next();
            cabe_return = pedaco.cabePeca(maior.retorneDimensao(), permiteRotacao);	

            if(cabe_return == 0 || cabe_return == 1){

                if((cabe_return == 0) || (cabe_return == 1) ){

                    return maior;
                }
            }
        }

        return null;	
    }
        
        
    public static void main(String args[]){

        LinkedList<Pedidos> list_pedidos = new LinkedList<Pedidos>();

        try {

            SolucaoHeuristica solucao;

            HHDHeuristic heuristca1 = new HHDHeuristic();

            list_pedidos = Funcoes.lerArquivo();
            Iterator<Pedidos> iterator = list_pedidos.iterator();

            System.out.println("  Lista de Pedidos  \n");

            while(iterator.hasNext()){

                Pedidos pedido = iterator.next(); 

                System.out.println("Id -- "+pedido.id()+"\t "+pedido.retorneDimensao().retorneBase()
                                            +"  x  "+pedido.retorneDimensao().retorneAltura());
            }

            solucao = heuristca1.criaSolucao(list_pedidos, Funcoes.getChapa());

            System.out.println("Executei para todos os pedidos\n");

            System.out.println("Quantidade de bins encontradas --> "+ solucao.getQtd());

            int c = 0;
            LinkedList<Peca> pecas = new LinkedList<Peca>();

            while(c < solucao.getQtd()){

                Bin bin_atual = solucao.retornePlanoDeCorte(c);

                pecas = bin_atual.getListaPecas();

                System.out.println("\n### Bin "+(++c)+" ###");
                System.out.println("Lista Peças --> "+ bin_atual.getListaPecas().size());
                System.out.println("Lista de Sobras --> "+bin_atual.getListaSobras().size());
                System.out.println("Lista de Cortes --> "+bin_atual.getListaCortes().size());

                Iterator<Peca> iterato = pecas.iterator();

                while(iterato.hasNext()){

                    Peca peca_atual = iterato.next();
                    IPedido pedidoAtendido = peca_atual.getPedidoAtendido();

                    System.out.println("\n -- Peça -- ");
                    System.out.println("Peça ID --> "+pedidoAtendido.id());
                    System.out.println("Ponto InfEQ --> ("+peca_atual.getPontoInfEsq().getX()+", "
                                                          +peca_atual.getPontoInfEsq().getY() +")");
                    System.out.println("Ponto SupDi --> ("+peca_atual.getPontoSupDir().getX()+", "
                                                      +peca_atual.getPontoSupDir().getY() +")\n\n");
                }                
            }                                
        }            
        catch (IOException ex) {

            Logger.getLogger(HHDHeuristic.class.getName()).log(Level.SEVERE, null, ex);

        }

    }

     //Esse algoritmo de desmonte pode estabeler comunicação entre o HHDHeuristic e o GRASP, uma vez que ele
     //pode desmontar uma bin completa e chamar o GRASP para execução, ao término une as duas soluções !!!
    public LinkedList<IPedido> algoritmo_desmonte1(IBin bin_escolhida){

        //Desmonta uma Bin inteira e devolve uma lista de Pedidos Não Atendidos

        int idP;
        IBin bin_atual;
        Peca peca_atual;
        IPedido pedido;

        LinkedList<IPedido> l = new LinkedList<IPedido>();

        Iterator<Peca> iter_peca = bin_escolhida.getListaPecas().iterator();

        while(iter_peca.hasNext()){

            peca_atual = iter_peca.next();

            pedido = peca_atual.getPedidoAtendido();

            //Devolve o pedido
            pedido.devolvaPedido();

            //Remove a peça da lista de peça
            bin_escolhida.getListaPecas().remove(peca_atual);

            //Adiciona o pedido a uma lista de pedidos Não Atendidos
            l.add(pedido);
        }

        //Realiza limpeza da bin

        bin_escolhida.getListaPecas().clear();
        bin_escolhida.getListaCortes().clear();
        bin_escolhida.getListaSobras().clear();


        return l;

    }
     
}

/*    //Ao devolver cria uma sobra no lugar da Peça !!!
            PedacoDisponivel sobras = new PedacoDisponivel(peca_atual.getPontoInfEsq(), peca_atual.getPontoSupDir(),
                                                                                        bin_escolhida.getListaSobras().size());
            
            bin_escolhida.getListaSobras().add(sobras);
        */