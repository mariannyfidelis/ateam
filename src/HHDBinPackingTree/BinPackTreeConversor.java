package HHDBinPackingTree;

import HHDInternal.Peca;
import HHDInternal.Corte;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;
import HHDInterfaces.ISolution;
import HHDInternal.HHDHeuristic;
import HHDInternal.PlanoDeCorte;
import HHDInterfaces.IDimensao2d;
import HHDInterfaces.IPecaPronta;
import HHDInterfaces.IGenericSolution;
import HHDInterfaces.ISolutionConversor;
import HeuristicaConstrutivaInicial.Bin;

//Uma classe conversora responsável por converter uma solução na forma de árvore Binária
public class BinPackTreeConversor implements ISolutionConversor{

    
    @Override
    public IGenericSolution converteParaSolucao(ISolution hSolution){

        LinkedList listaArvores;
        BinPackTreeForest floresta;//Ao final terá uma floresta de árvores
                
        Bin aux_bin = (Bin) hSolution.retornePlanoDeCorte(0);
        
        floresta = new BinPackTreeForest(hSolution.getTamanhoChapa());	

        int contadorBin = 0;

        while(contadorBin < hSolution.getQtd()){ //Percorre a quantidade de bins

            aux_bin = (Bin) hSolution.retornePlanoDeCorte(contadorBin++);//Recebe o plano de corte indicado

//            System.out.println("\nTam Corte - "+aux_bin.getListaCortes().size());
//            System.out.println("Tam Peças - "+aux_bin.getListaPecas().size());
//            System.out.println("Tam Sobras - "+aux_bin.getListaSobras().size());
//            
            //Realiza conversão criando uma árvore bin packing 
            BinPackTree arvore = converteBinArvore(aux_bin, hSolution.getTamanhoChapa());

//            System.out.println("\nTam Corte Árvore - "+arvore.getListaCortes().size());
//            System.out.println("Tam Peças Árvore - "+arvore.getListaPecas().size());
//            System.out.println("Tam Sobras Árvore - "+arvore.getListaSobras().size());
            
            if(arvore != null)
                floresta.adicionaPlanoDeCorte(arvore);
        }
        
        ArrayList<ArrayList<HHDBinPackingTree.IBPTNode>> solucao = HHDHeuristic.VerificaImprimeArvoreDeCorte((BinPackTreeForest) 
                                                                                                                            floresta);
        floresta.calculaFAVFAV2(floresta, solucao);
        
        return (IGenericSolution) floresta;
    }

    private BinPackTree converteBinArvore(Bin aux_bin, IDimensao2d tamanhoChapa){

        LinkedList listaCortesBin = aux_bin.getListaCortes(); //testar se funciona mesmo
        LinkedList listaCortes = new LinkedList();

        for(int i = 0; i < listaCortesBin.size(); i++){
            
            Corte c = (Corte) listaCortesBin.get(i);
            
            if((c.eVertical() == true) && (c.getPosicaoCorte()+c.getPontoChapaCortada().getX() == tamanhoChapa.retorneBase())){
            }
            else if((c.eVertical() == false) && (c.getPosicaoCorte()+c.getPontoChapaCortada().getY() == tamanhoChapa.retorneAltura())){
            
            }
            else{
            
                listaCortes.add(listaCortesBin.get(i));
            }
        }
//        System.out.println("\nNo converte bin árvore Qtd Corte --> "+ listaCortes.size());
        
        BinPackTree arvore = new BinPackTree(tamanhoChapa, aux_bin.getId(), null);

        LinkedList listaFolhas = cortaNos(arvore.getRaiz(), listaCortes);

//        System.out.println("Num folhas --> "+ listaFolhas.size());
        
        DestacaPecas(listaFolhas, aux_bin.getListaPecas());

        /*
           FIX ME O trecho abaixo nao foi incluido pq nao existem 
           essas informacoes no bin original. Resolver o problema.
           arvore.quant := PtBin^.quant;
           arvore.perda := PtBin^.perda;
        */

        return arvore;
    }

    private void DestacaPecas(LinkedList listaFolhas, LinkedList listaPecas){

        if(listaPecas.isEmpty()){
            return;
        }

        LinkedList cListaPecas = (LinkedList) listaPecas.clone();
        ListIterator iteradorFolhas = listaFolhas.listIterator();

        while ( iteradorFolhas.hasNext() ){

            BPTPedaco pedaco = (BPTPedaco) iteradorFolhas.next();
            Peca peca = encontraPeca(pedaco, cListaPecas);

            if(peca != null){
            
                cListaPecas.remove(peca);
                pedaco.destaquePeca(peca.getPedidoAtendido());
            }
        }
    }

    private Peca encontraPeca(BPTPedaco pedaco, LinkedList listaPecas){

        ListIterator iteradorPecas = listaPecas.listIterator();

        while(iteradorPecas.hasNext()){

            Peca peca = (Peca) iteradorPecas.next();
            float base = peca.retorneDimensoes().retorneBase();
            float altura = peca.retorneDimensoes().retorneAltura();

            
            //System.out.println("Pedaço comparado --> (Base x Altura) -> ( "+pedaco.getWidth()+" , "+pedaco.getHeight()+")");
            
            if(peca.getPontoInfEsq().eIgualAoPonto(pedaco.getPosition())){
            
                if((base == pedaco.getWidth()) && (altura == pedaco.getHeight())){
            
//                    System.out.println("\nEncontra peça --> (Base x Altura) -> ( "+base+" , "+altura+")"+
//                                                                                        "  Id -> "+peca.getPedidoAtendido().id());
                    return peca;
                }
                else if((base == pedaco.getHeight()) && (altura == pedaco.getWidth())){
                
//                    System.out.println("\nEncontra peça --> (Base x Altura) -> ( "+base+" , "+altura+")"+
//                                                                                        "  Id -> "+peca.getPedidoAtendido().id());
                    return peca;
                }
                else{}
            }
            //if((base == pedaco.getWidth()) && (altura == pedaco.getHeight())
            //                               && peca.getPontoInfEsq().eIgualAoPonto(pedaco.getPosition())){
            //         return peca;
            //}
        }

        return null;
    }

    private LinkedList cortaNos(IBPTNode pedaco, LinkedList listaCortes){

        LinkedList listaFolhas = new LinkedList();

        if(pedaco != null){

                listaFolhas = EfetuaCorte(pedaco, listaCortes);
//                System.out.println("\nVou cortar filho esquerdo\n");
                listaFolhas.addAll(cortaNos(pedaco.getLeftSon(), listaCortes));
//                System.out.println("\nVou cortar filho direito\n");
                listaFolhas.addAll(cortaNos(pedaco.getRigthSon(), listaCortes));
        }

        return listaFolhas;
    }

    private LinkedList EfetuaCorte(IBPTNode pedaco, LinkedList listaCortes){

        Corte corteEscolhido = null;
        ListIterator iteradorCortes = listaCortes.listIterator(0);

        while(iteradorCortes.hasNext()){

            Corte corteAtual = (Corte) iteradorCortes.next();

            if(cabeCorte(pedaco, corteAtual)){
                
                corteEscolhido = corteAtual;
                
//                System.out.println("\n########\nCorte escolhido Id -> "+corteEscolhido.getId()+" V -> "+corteEscolhido.eVertical()+
//                                   " Posição do corte -> "+corteEscolhido.getPosicaoCorte()+" PosicaoGrasp -> "+corteEscolhido.getPontoChapaCortada());
/*                System.out.println("\nCorte Id - "+ corteAtual.getId()+"\tPonto de corte - ("+corteAtual.getPontoChapaCortada().getX()+" , "
                                                                        +corteAtual.getPontoChapaCortada().getY()+") "
                                                                        + " Tamanho - "+corteAtual.getTamanho()
                                                                        + " Posição Corte - "+corteAtual.getPosicaoCorte()
                                                                        +"\n\tPonto de corte Grasp - ("+
                                                                        corteAtual.getPontoChapaCortadaGrasp().getX()+" , "
                                                                        +corteAtual.getPontoChapaCortadaGrasp().getY()+" )");
//  */       //       System.out.println("\n################\n");
                break;
            }
        }

        LinkedList listaFolhas = new LinkedList();

        if(corteEscolhido != null){
            
//            if(isGrasp == true){
//                ((BPTPedaco)pedaco).corte(corteEscolhido.getPosicaoCorteGrasp(), corteEscolhido.eVertical());
//                listaCortes.remove(corteEscolhido);
//            }
//            else{
                ((BPTPedaco)pedaco).corte(corteEscolhido.getPosicaoCorte(), corteEscolhido.eVertical());
                listaCortes.remove(corteEscolhido);
//            }
        }
        else {
//            System.out.println("\nAdicionando uma folha !\n");    
            listaFolhas.add(pedaco);
        }

        return listaFolhas;
    }

    private boolean cabeCorte(IBPTNode pedaco, Corte corteAtual){
        
        boolean corteCabe = false;
        
//        System.out.println("\nInformação do Pedaço -> L x A -> ("+((BPTPedaco)pedaco).retorneBase()+","+
//                                                                                    ((BPTPedaco)pedaco).retorneAltura()+" )");
        
//        if(isGrasp == true){
//        
//            corteCabe = corteAtual.getPontoChapaCortadaGrasp().eIgualAoPonto(pedaco.getPosition());
//        }
//        else{
            corteCabe = corteAtual.getPontoChapaCortada().eIgualAoPonto(pedaco.getPosition());      
//        }
        
        return corteCabe;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    // 
    //   ALGORITMO E MÉTODOS PARA CONVERSÃO DE SUBSOLUÇÃO.
    //
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    @Override
    public IGenericSolution /*void*/ converteSubSolucao(IGenericSolution florest, LinkedList listaPlanosDeCorte,
                                                                           LinkedList listaBinsCompletos, IDimensao2d tamanhoChapa) {
        
            ListIterator iteradorPlanosDeCorte = listaPlanosDeCorte.listIterator();
            BPTPedaco noCortado = null;

            while(iteradorPlanosDeCorte.hasNext()) {
                
                    try {
                            PlanoDeCorte planoAtual = (PlanoDeCorte) iteradorPlanosDeCorte.next();

                            noCortado = (BPTPedaco) planoAtual.getSobraCortada();
                            
                            LinkedList listaCortes = planoAtual.getListaCortes();

                            if(listaCortes.size() == 0)
                            {
                                    if(noCortado.getArvore().getRaiz() == noCortado)
                                    {
                                            noCortado.getArvore().getFloresta().removaPlanoDeCorte(noCortado.getArvore());
                                            continue;
                                    }

                            }
                            LinkedList listaFolhas = cortaNos(noCortado, planoAtual.getListaCortes());
                            IncluaPecas(listaFolhas, planoAtual.getListaPecas());

                    }
                    catch (ClassCastException ex) {
                        
                        System.err.println("Erro durante conversao: plano de corte recebido nao possui uma sobra da arvore valida.");
                        ex.printStackTrace(System.err);
                        System.exit(0);
                    }

            }
            BinPackTreeForest floresta = (BinPackTreeForest) florest; //era null !!!
            /* Comentei depois testo sem  comente !! OBS: Cuidado interfere no outro Agente ...
                try{

                    if((listaPlanosDeCorte.size() > 0)||(listaBinsCompletos.size() == 0)){
                        floresta = (BinPackTreeForest) noCortado.getFloresta();
                    }
                    else if((listaPlanosDeCorte.size() == 0) || (listaBinsCompletos.size() > 0)){

                        floresta = new BinPackTreeForest(tamanhoChapa);
                    }

                }catch(NullPointerException e){

                    System.out.println("Ponto nulo na subsolução ...");
                    //return null;
                }
            */
            if(listaBinsCompletos.size() == 0){
                return (IGenericSolution) floresta;
            }
            LinkedList ListaBinPackTrees = new LinkedList();
            ListIterator iteradorLista = listaBinsCompletos.listIterator();
            

            while(iteradorLista.hasNext()){
                
                PlanoDeCorte binCompleto = (PlanoDeCorte) iteradorLista.next();
                BinPackTree arvore = new BinPackTree(tamanhoChapa, -1, null);
                
                LinkedList listaCortesBin = binCompleto.getListaCortes();
                LinkedList listaCortes = new LinkedList();

                for(int i = 0; i < listaCortesBin.size(); i++){
                    listaCortes.add(listaCortesBin.get(i));
                }
                
                LinkedList listaFolhas = cortaNos(arvore.getRaiz(), listaCortes);
                IncluaPecas(listaFolhas, binCompleto.getListaPecas());
                
                floresta.adicionaPlanoDeCorte(arvore);
                
            }
            
            ArrayList<ArrayList<HHDBinPackingTree.IBPTNode>> solucao = HHDHeuristic.VerificaImprimeArvoreDeCorte((BinPackTreeForest) 
                                                                                                                               floresta);
            floresta.calculaFAVFAV2(floresta, solucao);
            
            return (IGenericSolution) floresta;//só um return....
    }

    private void IncluaPecas(LinkedList listaFolhas, LinkedList listaPecas) {
        
//        System.out.println("\nTamanho da lista de folhas ....> "+listaFolhas.size());
//        System.out.println("Tamanho da lista de peças  ....> "+listaPecas.size());
        
        if(listaPecas.isEmpty()){
            return;
        }

        LinkedList cListaPecas = (LinkedList) listaPecas.clone();
        ListIterator iteradorFolhas = listaFolhas.listIterator();

        while ( iteradorFolhas.hasNext()){
            
            BPTPedaco pedaco = (BPTPedaco) iteradorFolhas.next();
            IPecaPronta peca = encontreITestePeca(pedaco, cListaPecas);
            
            if(peca != null){
                
                cListaPecas.remove(peca);
                
                if(pedaco.isCuttable()){
                
//                    System.out.println("Vou cortar e atribuir pedido ao P ( "+pedaco.retorneBase()+","+pedaco.retorneAltura()+" )");
                    pedaco.setCortado(pedaco.destaquePeca(((BPTPedaco)((IBPTNodeITestePecaWrapper) peca).getNoEmpacotado()).getPedidoAtendido())); 
                
                }
                IBPTNode pai = pedaco.getFather();
                
                if(pai != null){
                    
                    if(pai.getLeftSon() == pedaco){
//                        System.out.println("setando filho esquerdo ...");
                       
                        pai.setLeftson(((IBPTNodeITestePecaWrapper) peca).getNoEmpacotado());
                        
                        //((IBPTNodeITestePecaWrapper) peca).getNoEmpacotado().setFather(pai);
                    }
                    else{
//                        System.out.println("setando filho direito ...");
                        pai.setRightSon(((IBPTNodeITestePecaWrapper) peca).getNoEmpacotado());
                        //((IBPTNodeITestePecaWrapper) peca).getNoEmpacotado().setFather(pai);
                    }
                }
                else{
                    BinPackTree arvore = pedaco.getArvore();
                    
                    try {
                        arvore.substitua(arvore.getRaiz(), ((IBPTNodeITestePecaWrapper) peca).getNoEmpacotado());
                    } catch (Exception e) {
                        
                        System.err.println(e.getMessage());
                        e.printStackTrace();
                        System.exit(0);
                    }
                }
                
                ((IBPTNodeITestePecaWrapper) peca).getNoEmpacotado().setFather(pedaco.getFather());
                ((IBPTNodeITestePecaWrapper) peca).getNoEmpacotado().setFloresta(pedaco.getFloresta());			
            }
        }
        
    }

    private IPecaPronta encontreITestePeca(BPTPedaco pedaco, LinkedList listaPecas){
        
        ListIterator iteradorPecas = listaPecas.listIterator();

        while(iteradorPecas.hasNext()){
            
            IPecaPronta peca = (IPecaPronta) iteradorPecas.next();
            float base = peca.retorneDimensao().retorneBase();
            float altura = peca.retorneDimensao().retorneAltura();

            if(peca.getPontoInfEsq().eIgualAoPonto(pedaco.getPosition())){
            
                  if((base == pedaco.getWidth()) && (altura == pedaco.getHeight())){
                        
//                      System.out.println("\nEncontra peça --> (Base x Altura) -> ( "+base+" , "+altura+")"+
//                                                                        "  Id -> "+((IBPTNodeITestePecaWrapper)peca).getNoEmpacotado().getID());
                        return peca;                                              
                  }
                  if((base == pedaco.getHeight()) && (altura == pedaco.getWidth())){
                        
//                      System.out.println("\nEncontra peça --> (Base x Altura) -> ( "+base+" , "+altura+")"+
//                                                                         "  Id -> "+((IBPTNodeITestePecaWrapper)peca).getNoEmpacotado().getID());
                        return peca;
                  }
            }
            
        }

        return null;
    }
}