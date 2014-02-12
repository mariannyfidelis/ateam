package HeuristicaConstrutivaInicial;

import Heuristicas.Individuo;
import java.util.List;
import java.util.Iterator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Collections;
import java.io.BufferedReader;
import java.io.InputStreamReader;


public class HeuristicaGRASP {
   
       
    //Heuristicas.
    public static LinkedList<Solucao> Grasp2d(ArrayList<Item> L,ArrayList<Item> l,int w, int h,double alpha,int maxit,
                                                          List<IPedido> listaPedidosNaoAtendidos) throws PecaInvalidaException, IOException{
	
        LinkedList<Solucao> ListaSolucao = new LinkedList<Solucao>();
        
        LinkedList<Corte> list_cortes = new LinkedList<Corte>();
        LinkedList<Peca>  list_pecas  = new LinkedList<Peca>();
        LinkedList<PedacoDisponivel> list_sobras = new LinkedList<PedacoDisponivel>();
        
        LinkedList<Corte> cortes_auxFH = new LinkedList();
        LinkedList<Corte> cortes_auxFV = new LinkedList();
        
        LinkedList<Peca> pecas_auxFH = new LinkedList();
        LinkedList<Peca> pecas_auxFV = new LinkedList();
        
        LinkedList<PedacoDisponivel> sobras_auxFH = new LinkedList();
        LinkedList<PedacoDisponivel> sobras_auxFV = new LinkedList();
        
        //LinkedList<IPedido> listPedidosNaoAtendidos = new LinkedList();
        //LinkedList<IPedido> listPedidosAtendidos = new LinkedList();
        
	LinkedList<Heuristicas.Solucao> VetorSolucao = new LinkedList<Heuristicas.Solucao>();
        
        
        List<Item> c = new ArrayList<Item>();
        List<Item> lc = new ArrayList<Item>();
        List<Item> lrc = new ArrayList<Item>();
        List<Objeto> Solucao = new ArrayList<Objeto>();
        List<Objeto> SolucaoFinal = new ArrayList<Objeto>();
        
        List<Item> fhi = new ArrayList<Item>();
        List<Item> fvi = new ArrayList<Item>();
        List<Item> bh = new ArrayList<Item>();
        List<Item> bv = new ArrayList<Item>();
        
        StringResultado sh = new StringResultado();
        StringResultado sv = new StringResultado();
        StringResultado S = new StringResultado();
        StringResultado Sm = new StringResultado();
        
        List<StringResultado> SolucaoC = new ArrayList<StringResultado>();
        List<String> SolucaoCorrente = new ArrayList<String>();
        List<String> MelhorSolucao = new ArrayList<String>();
        List<Double> FAVCorrente = new ArrayList<Double>();
        List<Double> FAVMelhor = new ArrayList<Double>();
        
        Double FAV = 0.0;
        Double FAVM = 0.0;
        
        Double FAV2 = 0.0;
	Double MenorAp =0.0;
        
        Item p = new Item();

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in)); //Verificar a utilidade e importância no código
        
        c = (List<Item>) l.clone();

        int rfh;
        int rfw;
        int x = 0;
        int j = 0;
        int j1 =10000;
        int cont = 0;
        boolean r = false;

        // for (int it = 0; it < 50; it++){
        
        long ini = System.currentTimeMillis();
        
        Item item_temp;
        List<Item> listem ;
        Individuo individuo_temp = new Individuo();
        
        //Condição de Parada é o nº de soluções
        while (x < maxit){
        
            Solucao solucao_atual = new Solucao();
            
            int corte = 0;
            
            System.out.println("\n"+(x+1)+"ª iteração\n");
            j = 0;
            Solucao.clear();
            
            list_pecas.clear();
            list_cortes.clear();                
            list_sobras.clear();
            
            while(!c.isEmpty()){// && (!listaPedidosNaoAtendidos.isEmpty())) {//Enquanto existir item no conjunto C de pedidos 
            
                Objeto placa = new Objeto(w, h);
                
                PedacoDisponivel pedaco_disponivel = new PedacoDisponivel((new Ponto(0, 0)), new Dimensao2D(w, h), (x+1));
                
                //Teste adicionando -- As listas auxiliares para faixas horizontais e verticais
                //sobras_auxFH.add(pedaco_disponivel);
                //sobras_auxFV.add(pedaco_disponivel);
                
                //Teste de controle de sobras temporário
                list_sobras.add(pedaco_disponivel);
                //list_sobras.add(pedaco_disponivel);
                
                List<Faixas> faixaTemp = new ArrayList<Faixas>();
                rfh = h;
                rfw = w;
                
                Faixas fh = new Faixas();
                Faixas fv = new Faixas();
                
                //Criando uma estrutura de Faixa auxiliar para armazenar informações extras
                Faixas fh_aux = new Faixas();
                Faixas fv_aux = new Faixas();

                Ponto pIfE = new Ponto(0, 0);
                Ponto pSD = new Ponto(rfw, rfh);
                    
                lc.addAll(c); //Adiciona a lista de candidatos todos os elementos de C

                while (!lc.isEmpty()){ //Enquanto existir item na lista de candidatos faça
                    
                    System.out.println("\n\nA nova faixa vai ser criada a partir dos seguintes pontos: ");
                    System.out.println("InfE ("+pIfE.getX()+","+pIfE.getY()+")\tSupDir ("+pSD.getX()+","+pSD.getY()+")");
                    
                    //PedacoDisponivel pedaco_disponivel = new PedacoDisponivel(pIfE, pSD, (x+1));
                    //sobras_auxFH.add(pedaco_disponivel);
                    //sobras_auxFV.add(pedaco_disponivel);
                    
                    cont++;
                    
                    Funcoes.ImprimeItens("Lista LC ", lc);
                    
                    lrc = Funcoes.CriaLrc(lc, alpha); //Cria uma lista restrita de candidatos
                    
                    Funcoes.ImprimeItens("\n\nLista LRC ", lrc);
                    
                    p = lrc.get(Funcoes.Aleatorio(lrc.size())); //Seleciona um pedido aleatório na LRC
                    
                    System.out.println("\nItem escolhido: "+"Id - "+p.getId()+" W - "+p.getW()+" H - "+p.getH()+" D - "+p.getD()+" O - "+p.getO());
                    
                    if (c.size() > 0 && c.get(c.size()-1).getV() > rfw*rfh){
                        System.out.println("\nItem não pode ser alocado será removido da Lista de Candidatos !");
                        //Seria interessante remover o item ou comentar esse código aki !
                        lc.remove(p);
                        break;
                    }
                    
                    if (!Funcoes.PodeAlocar(p, rfw, rfh)){ //Se o não Item pode ser alocado em rf
                        
                        System.out.println("\nItem não pode ser alocado e foi removido da Lista de Candidatos !");
                        lc.remove(p);
                        continue; // Volta ao Inicio do Laço
                    }
                    
                    if(!Funcoes.RotacaoItem(p, rfw, rfh)){ // Verifica se o Item pode ou deve ser rotacionado em 90º
                     
                        System.out.println("Não pode rotacionar o item !");
                        
                        fh = Funcoes.CriaFh(p, rfw, rfh, 0, pIfE, pSD); //Cria a Faixas horizontal
                        fv = Funcoes.CriaFv(p, rfw, rfh, 0, pIfE, pSD); //Cria a Faixas vertical
                        
                        fh_aux = Funcoes.CriaFh(p, rfw, rfh, 0, pIfE, pSD); //Cria a Faixas horizontal
                        fv_aux = Funcoes.CriaFv(p, rfw, rfh, 0, pIfE, pSD); //Cria a Faixas vertical
                        
                        r = false;
                                                
                    }else{
                        
                        System.out.println("Pode rotacionar o item !");
                        fh = Funcoes.CriaFh(p, rfw, rfh, 1, pIfE, pSD); //Cria a Faixas horizontal
                        fv = Funcoes.CriaFv(p, rfw, rfh, 1, pIfE, pSD);// Cria a Faixas vertical
                        
                        fh_aux = Funcoes.CriaFh(p, rfw, rfh, 1, pIfE, pSD); //Cria a Faixas horizontal
                        fv_aux = Funcoes.CriaFv(p, rfw, rfh, 1, pIfE, pSD);// Cria a Faixas vertical
                        
                        r = true;
                    }
                        
                    System.out.println("\nFh --> InfEsq("+fh.getPontoInferiorEsquerdo().getX()+","+fh.getPontoInferiorEsquerdo().getY()+")"
                                      + " SupDireito ("+fh.getPontoSuperiorDireito().getX()+","+fh.getPontoSuperiorDireito().getY() +")");
                    System.out.println("Fv --> InfEsq("+fv.getPontoInferiorEsquerdo().getX()+","+fv.getPontoInferiorEsquerdo().getY()+")"
                                      + " SupDireito ("+fv.getPontoSuperiorDireito().getX()+","+fv.getPontoSuperiorDireito().getY() +")");
                    
                    System.out.println("\nFh (Rw,Rh) --> InfEsq("+fh.getPontInfERw().getX()+","+fh.getPontInfERw().getY()+")"
                                        + " SupDireito ("+fh.getPontSupDRh().getX()+","+fh.getPontSupDRh().getY() +")");
                    
                    System.out.println("Fv (Rw, Rh) --> InfEsq("+fv.getPontInfERw().getX()+","+fv.getPontInfERw().getY()+")"
                                        + " SupDireito ("+fv.getPontSupDRh().getX()+","+fv.getPontSupDRh().getY() +")");
                    
                    System.out.println("\n\nFh (W x H) ("+fh.getW()+" x "+fh.getH()+") (RFW x RFH) ("+fh.getRw()+" x "+fh.getRh()+") Or. "+fh.getO());
                    System.out.println("Fv (W x H) ("+fv.getW()+" x "+fv.getH()+") (RFW x RFH) ("+fv.getRw()+" x "+fv.getRh()+") Or. "+fv.getO());
                    
                    Ponto pCorteH, pCorteV;
                    
                    if(r == true){
                        //System.out.println("O item vai ser rotacionado...");
                        //Aqui deve ser criado o corte relacionado as faixas
                        pCorteH = new Ponto(fh.getPontoInferiorEsquerdo().getX(),fh.getPontoInferiorEsquerdo().getY() + p.getW());
                        pCorteV = new Ponto(fv.getPontoInferiorEsquerdo().getX() + p.getH(),fv.getPontoInferiorEsquerdo().getY());
                    }
                    else{
                        //System.out.println("O item não vai ser rotacionado...");
                        //Aqui deve ser criado o corte relacionado as faixas
                        pCorteH = new Ponto(fh.getPontoInferiorEsquerdo().getX(),fh.getPontoInferiorEsquerdo().getY() + p.getH());
                        pCorteV = new Ponto(fv.getPontoInferiorEsquerdo().getX() + p.getW(),fv.getPontoInferiorEsquerdo().getY());
                    }
                    
                    System.out.println("\n///////////////Criado um Ponto de Corte das Faixas \\\\\\\\\\\\\\\\\\");
                    System.out.println("Faixa Horizontal P("+pCorteH.getX()+","+pCorteH.getY()+") ||||  "
                            + "Faixa Vertical -- P("+pCorteV.getX()+","+pCorteV.getY()+")");
                    
                    corte = corte + 1;
                    
                    Corte corteFax_H = new Corte(fh.getPontoSuperiorDireito().getY(), pCorteH, false, 
                                        fh.getPontoSuperiorDireito().getX() - fh.getPontoInferiorEsquerdo().getX(), list_cortes.size()+1);//corte);
                
                    Corte corteFax_V = new Corte(fv.getPontoSuperiorDireito().getX(), pCorteV, true, 
                                        fv.getPontoSuperiorDireito().getY() - fv.getPontoInferiorEsquerdo().getY(), list_cortes.size()+1);//corte);
                    corte = corte + 1;

                    System.out.println("\n Alocando um item");
                    
                    Funcoes.AlocarItemFh(p, fh, fhi,sh,"H",fh_aux, cortes_auxFH,pecas_auxFH,sobras_auxFH, pSD,list_sobras); //Aloca o Item p na Faixas horizontal
                    Funcoes.AlocarItemFv(p, fv, fvi,sv,"V",fv_aux, cortes_auxFV,pecas_auxFV,sobras_auxFV, pSD,list_sobras); //Aloca o Item p na Faixas vertical
                    
                    //Colocar informação de itens alocados aqui para controle @@@@@@@
                    
                    //Aqui comeca a conversao
                    System.out.println("\nFaixa Atualizada\n");
                    
                    System.out.println("Fh (W x H) ("+fh.getW()+" x "+fh.getH()+") (RFW x RFH) ("+fh.getRw()+" x "+fh.getRh()+") Or. "+fh.getO());
                    System.out.println("Fv (W x H) ("+fv.getW()+" x "+fv.getH()+") (RFW x RFH) ("+fv.getRw()+" x "+fv.getRh()+") Or. "+fv.getO());

                    bh = Funcoes.CriaBh(lc, bh,fh.getRw(), fh.getRh(), p);
                    bv = Funcoes.CriaBv(lc, bv,fv.getRw(), fv.getRh(), p);

                    Funcoes.ImprimeItens("\nLista de BH ", bh);
                    Funcoes.ImprimeItens("\nLista de BV ", bv);

                    while (!bh.isEmpty()){
                    
                        //funcoes.ImprimeItens("***BH****",bh);
                        bh = Funcoes.MelhoreFh(fh, bh, bh.get(0), fhi,sh,fh_aux, cortes_auxFH, pecas_auxFH, sobras_auxFH,pSD, list_sobras);
                    }

                    while (!bv.isEmpty()){
                    
                        //funcoes.ImprimeItens("***BV***",bv);
                        bv = Funcoes.MelhoreFv(fv, bv, bv.get(0), fvi,sv, fv_aux, cortes_auxFV, pecas_auxFV, sobras_auxFV,pSD,list_sobras);
                    } 

                    System.out.println("\nAtualização das Faixas\nApós criação de BV e BH \n");
                    
                    System.out.println("Fh (W x H) ("+fh.getW()+" x "+fh.getH()+") (RFW x RFH) ("+fh.getRw()+" x "+fh.getRh()+") Or. "+fh.getO());
                    System.out.println("Fv (W x H) ("+fv.getW()+" x "+fv.getH()+") (RFW x RFH) ("+fv.getRw()+" x "+fv.getRh()+") Or. "+fv.getO());

                    double Pih = fh.getPi()/(fh.getH()*fh.getW());
                    double Piv = fv.getPi()/(fv.getH()*fv.getW());

                    System.out.println("Pih -> "+Pih);
                    System.out.println("Piv -> "+Piv);
                    
                    List<Item> itemTemp = new ArrayList<Item>();
                    
                    if (Pih < Piv){
                        
                        fh.setO(0);
                        
                        itemTemp.addAll(fhi);
                        fh.setItem(itemTemp);
                        
                        //Aqui seleciona a "Lista de Itens" da "Faixa Horizontal"
                        listem = new LinkedList<Item>(fhi);
                        //listem = fhi;
                        Iterator<Item> it_item = listem.iterator();
                        item_temp = new Item();
                        individuo_temp = new Individuo();
                        individuo_temp = placa.getIndividuo();
                        
                        while(it_item.hasNext()){
                            item_temp = it_item.next();
                            individuo_temp.adicionaItemLista(item_temp.getId(), item_temp.getV());
                        }
                        
                        individuo_temp.setFitness();
                        individuo_temp.setCapacidadeRespeitada(true);
                        /*Aqui deve-se atribuir as informações da faixa auxilia para faixa */
                        //fh.setPontInfERw(fh_aux.getPontInfERw()); //Acho que não é mais necessário
                        //fh.setPontSupDRh(fh_aux.getPontSupDRh()); //Acho que não é mais necessário
                        
                        faixaTemp.add(fh);
                        
                        list_cortes.add(corteFax_H);
                        
                        pIfE = new Ponto(fh.getPontoInferiorEsquerdo().getX(), fh.getPontoSuperiorDireito().getY());
                        pSD  = new Ponto(fh.getPontoSuperiorDireito().getX(), h);
                        
                        PedacoDisponivel pedaco_disponivel_proxima_faixa = new PedacoDisponivel(pIfE,pSD,list_sobras.size()+1);
                        sobras_auxFH.add(pedaco_disponivel_proxima_faixa);
                        /*if(!list_sobras.isEmpty()){
                            list_sobras.removeFirst();
                            list_sobras.add(new PedacoDisponivel(pIfE, pSD, list_sobras.size()+1));
                        }*/// Creio que não será mais necessário pois será passado para os auxiliares !!!
                        
                        //É importante limpar e atribuir novamente a lista de sobras
                        list_sobras.clear(); list_sobras.removeAll(list_sobras);
                        
                        //Aqui será adicionado o resultado das listas auxiliares
                        list_cortes.addAll(cortes_auxFH);
                        list_pecas.addAll(pecas_auxFH);
                        list_sobras.addAll(sobras_auxFH);
        
                        //Imprimir informações  sobre cada lista e o que será passado a Lista Principal
                        /*System.out.println("\n################ LISTAS COM INFORMAÇÕES ####################");
                        Peca pc;      Corte ct;     PedacoDisponivel sb;
                                                
                        System.out.println("Lista de Peças");
                        
                        Iterator<Peca> it = list_pecas.iterator();
                        
                        while(it.hasNext()){
                        
                            pc = it.next();                        
                                                        
                            System.out.println("Peça Id - " +pc.getPedidoAtendido().id()+"\tPontInfE( "+pc.getPontoInfEsq().getX()
                                                            +","+pc.getPontoInfEsq().getY()+")" + "\tPontSupDir("+pc.getPontoSupDir().getX()
                                                            +","+pc.getPontoSupDir().getY()+")");
                        }
                        
                        System.out.println("\nLista de Corte");
                        
                        Iterator<Corte> itc = list_cortes.iterator();
                        
                        while(itc.hasNext()){
                        
                            ct = itc.next();
                            
                            System.out.println("Corte Id - "+ ct.getId()+"\tPonto de corte - ("+ct.getPontoChapaCortada().getX()+" , "
                                                                        +ct.getPontoChapaCortada().getY()+") "
                                    + " Tamanho - "+ct.getTamanho());
                        }                        
                        
                        System.out.println("\nLista de Sobra");
                        
                        Iterator<PedacoDisponivel> itpd = list_sobras.iterator();
                        
                        while(itpd.hasNext()){
                        
                            sb = itpd.next();                        
                                                        
                            System.out.println("Sobra Id - "+sb.getId()+"\tPontInfE( "+sb.getPontoInferiorEsquerdo().getX()
                                                            +","+sb.getPontoInferiorEsquerdo().getY()+")" 
                                                            +"\tPontSupDir("+sb.getPontoSuperiorDireito().getX()
                                                            +","+sb.getPontoSuperiorDireito().getY()+")");
                        }
                        
                        */
                        
                        System.out.println("\n\n");
                        
                        //Limpa as coleções auxiliares para a próxima iteração
                        cortes_auxFH.clear();
                        pecas_auxFH.clear();
                        sobras_auxFH.clear();
                        
                        cortes_auxFV.clear();
                        pecas_auxFV.clear();
                        sobras_auxFV.clear();
                        
                        cortes_auxFH.removeAll(cortes_auxFH);
                        pecas_auxFH.removeAll(pecas_auxFH);
                        sobras_auxFH.removeAll(sobras_auxFH);
                        
                        cortes_auxFV.removeAll(cortes_auxFV);
                        pecas_auxFV.removeAll(pecas_auxFV);
                        sobras_auxFV.removeAll(sobras_auxFV);
                                                
                        S.setS(S.getS()+sh.getS());
                        
                        if (r){
                            rfh = rfh - p.getW();
                        }
                        else{
                            rfh = rfh - p.getH();
                        }
                        
                        System.out.println("Rfh -> "+ rfh+"\n");
                        
                        c = Funcoes.AtualizaC2(fhi, c, placa, lc, rfw, rfh,listaPedidosNaoAtendidos);

                        //c = Funcoes.AtualizaC(fhi, c, placa);
                        // System.out.println("Orientação Faixas: "+fh.getO());
                    }else{
                    
                        
                        fv.setO(1);
                        itemTemp.addAll(fvi);
                        
                        S.setS(S.getS()+sv.getS());
                        fv.setItem(itemTemp);
                        
                        //Aqui seleciona a "Lista de Itens" da "Faixa Vertical"
                        listem = new LinkedList<Item>(fvi);
                        //listem = fvi;
                        Iterator<Item> it_item = listem.iterator();
                        item_temp = new Item();
                        
                        individuo_temp = new Individuo();
                        individuo_temp =  placa.getIndividuo();
                        
                        while(it_item.hasNext()){
                            item_temp = it_item.next();
                            individuo_temp.adicionaItemLista(item_temp.getId(), item_temp.getV());
                        }
                        
                        individuo_temp.setFitness();
                        individuo_temp.setCapacidadeRespeitada(true);
                        /*Aqui deve-se atribuir as informações da faixa auxilia para faixa */
                        //fv.setPontInfERw(fv_aux.getPontInfERw()); //Acho que não é mais necessário
                        //fv.setPontSupDRh(fv_aux.getPontSupDRh()); //Acho que não é mais necessário
                        
                        faixaTemp.add(fv);
                        list_cortes.add(corteFax_V);
                        
                        pIfE = new Ponto(fv.getPontoSuperiorDireito().getX(), fv.getPontoInferiorEsquerdo().getY());
                        pSD  = new Ponto(w, fv.getPontoSuperiorDireito().getY());
                        
                        PedacoDisponivel pedaco_disponivel_proxima_faixa = new PedacoDisponivel(pIfE,pSD,list_sobras.size()+1);
                        sobras_auxFV.add(pedaco_disponivel_proxima_faixa);
                        /*if(!list_sobras.isEmpty()){

                            list_sobras.removeFirst();
                            list_sobras.add(new PedacoDisponivel(pIfE, pSD, list_sobras.size()+1));
                          }*/// Creio que não será mais necessário pois será passado para os auxiliares !!!
                        //É importante limpar e atribuir novamente a lista de sobras
                        
                        list_sobras.clear(); list_sobras.removeAll(list_sobras);
                        
                        //Aqui será adicionado o resultado das listas auxiliares
                        list_cortes.addAll(cortes_auxFV);
                        list_pecas.addAll(pecas_auxFV);
                        list_sobras.addAll(sobras_auxFV);
                        
                        //Imprimir informações  sobre cada lista e o que será passado a Lista Principal
                        /*System.out.println("\n################ LISTAS COM INFORMAÇÕES ####################");
                        Peca pc;      Corte ct;     PedacoDisponivel sb;
                                                
                        System.out.println("Lista de Peças");
                        
                        Iterator<Peca> it = list_pecas.iterator();
                        
                        while(it.hasNext()){
                        
                            pc = it.next();                        
                                                        
                            System.out.println("Peça Id - " +pc.getPedidoAtendido().id()+"\tPontInfE( "+pc.getPontoInfEsq().getX()
                                                            +","+pc.getPontoInfEsq().getY()+")" + "\tPontSupDir("+pc.getPontoSupDir().getX()
                                                            +","+pc.getPontoSupDir().getY()+")");
                        }
                        
                        System.out.println("\nLista de Corte");
                        
                        Iterator<Corte> itc = list_cortes.iterator();
                        
                        while(itc.hasNext()){
                        
                            ct = itc.next();
                            
                            System.out.println("Corte Id - "+ ct.getId()+"\tPonto de corte - ("+ct.getPontoChapaCortada().getX()+" , "
                                                                        +ct.getPontoChapaCortada().getY()+") "
                                    + " Tamanho - "+ct.getTamanho());
                        }                        
                        
                        System.out.println("\nLista de Sobra");
                        
                        Iterator<PedacoDisponivel> itpd = list_sobras.iterator();
                        
                        while(itpd.hasNext()){
                        
                            sb = itpd.next();                        
                                                        
                            System.out.println("Sobra Id - "+sb.getId()+"\tPontInfE( "+sb.getPontoInferiorEsquerdo().getX()
                                                            +","+sb.getPontoInferiorEsquerdo().getY()+")" 
                                                            +"\tPontSupDir("+sb.getPontoSuperiorDireito().getX()
                                                            +","+sb.getPontoSuperiorDireito().getY()+")");
                        }
                        
                        */
                        System.out.println("\n\n");
                        
                        
                        //Limpa as coleções auxiliares para a próxima iteração
                        cortes_auxFV.clear();
                        pecas_auxFV.clear();
                        sobras_auxFV.clear();
                        
                        cortes_auxFH.clear();
                        pecas_auxFH.clear();
                        sobras_auxFH.clear();
                        
                        cortes_auxFV.removeAll(cortes_auxFV);
                        pecas_auxFV.removeAll(pecas_auxFV);
                        sobras_auxFV.removeAll(sobras_auxFV);
                        
                        cortes_auxFH.removeAll(cortes_auxFH);
                        pecas_auxFH.removeAll(pecas_auxFH);
                        sobras_auxFH.removeAll(sobras_auxFH);
                        
                        if(r){
                            rfw = rfw - p.getH();
                        }
                        else{
                            rfw = rfw - p.getW();
                        }
                        
                        System.out.println("Rfw -> "+ rfw+"\n");
                        
                        c = Funcoes.AtualizaC2(fvi, c, placa,lc, rfw,rfh, listaPedidosNaoAtendidos);
                        //c = Funcoes.AtualizaC(fvi, c, placa);
                        //System.out.println("Orientação Faixas: "+fv.getO());

                    }

                    fhi.clear();
                    fvi.clear();
                    sh.setS("");
                    sv.setS("");
                    lc.clear();
                    lc.addAll(c);

                    /*Aqui deve-se atribuir e setar os pontos InfEsquerdo  e SupDireito das faixas*/
                    
                    /*fh.setPontoInferiorEsquerdo(null);
                    fh.setPontoSuperiorDireito(null);
                    fh.setPontInfERw(null);
                    fh.setPontSupDRh(null);
                    
                    fv.setPontoInferiorEsquerdo(null);
                    fv.setPontoSuperiorDireito(null);
                    fv.setPontInfERw(null);
                    fv.setPontSupDRh(null);*/
                }
                
                placa.setF(faixaTemp); //Adiciona as listas de placas !!!
                
                //Aqui será apresentado as informações finais
                System.out.println("\n################ LISTAS COM INFORMAÇÕES ####################");
                        
                System.out.println("Lista de Peças");
                
                Peca pc; Corte ct; PedacoDisponivel sbpd;
                int cont_ct = 1, cont_sbpd = 1;
                
                Iterator<Peca>  itpeca = list_pecas.iterator();
                
                while(itpeca.hasNext()){
                    
                   pc = itpeca.next();
                   placa.adicionePeca(pc);
                   System.out.println("Peça Id - " +pc.getPedidoAtendido().id()+"\tPontInfE( "+pc.getPontoInfEsq().getX()
                                                            +","+pc.getPontoInfEsq().getY()+")" + "\tPontSupDir("+pc.getPontoSupDir().getX()
                                                            +","+pc.getPontoSupDir().getY()+")");
                }
                
                System.out.println("\nLista de Corte");
                 
                Iterator<Corte> itc = list_cortes.iterator();
                       
                while(itc.hasNext()){                
                
                   ct = itc.next();
                  
                   if(cont_ct != ct.getId()){
 
                      ct.setId(cont_ct);
                   }
                                   
                   cont_ct = cont_ct + 1;
                  
                   placa.adicioneCorte(ct);
                   System.out.println("Corte Id - "+ ct.getId()+"\tPonto de corte - ("+ct.getPontoChapaCortada().getX()+" , "
                                                                        +ct.getPontoChapaCortada().getY()+") "
                                      + " Tamanho - "+ct.getTamanho());
                }
                
                System.out.println("\nLista de Sobras");
                
                Iterator<PedacoDisponivel> itpd = list_sobras.iterator();
                       
                while(itpd.hasNext()){
                
                   sbpd = itpd.next();
                  
                   if(cont_sbpd != sbpd.getId()){
 
                      sbpd.setId(cont_sbpd);
                   }
                                   
                   cont_sbpd = cont_sbpd + 1;
                   
                   placa.adicionarSobra(sbpd);
                   
                   System.out.println("Sobra Id - "+sbpd.getId()+"\tPontInfE( "+sbpd.getPontoInferiorEsquerdo().getX()
                                                            +","+sbpd.getPontoInferiorEsquerdo().getY()+")" 
                                                            +"\tPontSupDir("+sbpd.getPontoSuperiorDireito().getX()
                                                            +","+sbpd.getPontoSuperiorDireito().getY()+")");
                
                }
                          
                
                //Limpando as informações das listas para a próxima iteração !                
                
                list_pecas.clear();
                list_cortes.clear();                
                list_sobras.clear();                
                
                FAV = FAV + placa.getFav();
                S.setFav((placa.getFav()/(w*h))*100);
                S.setI(x);
                SolucaoCorrente.add(S.getS());
                FAVCorrente.add(S.getFav());

                Solucao.add(placa);
                
                //Aqui eu gravo a placa de menor aproveitamento
                Double sobra = 100-(placa.getFav()/(w*h)*100);

                if (sobra > MenorAp){
                    MenorAp = sobra;                	                	
                }
                        
                placa.setFav(0.0);
                S.setS("");
                
                j++;
            }
            
            if (j < j1){ j1 = j;}

            FAV = FAV/(j*w*h);
            FAV2 = ((FAV*100)+MenorAp)/j;
            
            VetorSolucao.add(Funcoes.ConvertSolucao(Solucao,Funcoes.listIPedido(), Solucao.get(0).retorneDimensao()));
            
            Solucao SolucaoTemp = new Solucao(Solucao,FAV,FAV2);
            
            solucao_atual.setObjetos(Solucao);
            solucao_atual.setFAV(FAV);
            solucao_atual.setFAV2(FAV2);
            //solucao_atual.setLinkedIPedidos(new LinkedList(l));
            solucao_atual.setLinkedIPedidos(Funcoes.CriaListaPedidos(l));
            solucao_atual.setTamanhoChapa(new Dimensao2D(w,h));
            
	    //ListaSolucao.add(SolucaoTemp);
            ListaSolucao.add(solucao_atual);
            
            if (FAV2 > FAVM){
            
                MelhorSolucao.clear();
                MelhorSolucao.addAll(SolucaoCorrente);
                FAVMelhor.clear();
                FAVMelhor.addAll(FAVCorrente);
                FAVM = FAV2;
                SolucaoFinal.clear();
                SolucaoFinal.addAll(Solucao);
            }
            
            c = (List<Item>) l.clone();
            x++;
            FAV = 0.0;
            FAV2 = 0.0;
            MenorAp = 0.0;
	    SolucaoCorrente.clear();
            FAVCorrente.clear();

            //System.out.println(x);
    }
        
    // }
    System.out.printf("Tempo Total: %d \n ",(System.currentTimeMillis()-ini)/1000);
    
    //System.out.println("Melhor Solucao: "+FAVM*100+"% Iteracao: "+MelhorSolucao.size()+" Contador: "+cont/maxit+" Padrao: "+Sm.getS());
    System.out.println("Melhor Solucao: "+FAVM+"% Iteracao: "+MelhorSolucao.size()+" Contador: "+cont/maxit+" Padrao: "+Sm.getS());
    
    System.out.println("Padrao: "+j1+" "+MelhorSolucao.size()+" "+FAVMelhor.size());

    for (int i = 0; i < MelhorSolucao.size(); i++){
        System.out.println("Padrao de Corte "+(i+1)+":"+MelhorSolucao.get(i));
        System.out.println("Aproveitamento: "+FAVMelhor.get(i));
    }
    
    Collections.sort(ListaSolucao);
    
    for(int i=0;i < ListaSolucao.size();i++){
            
        System.out.println("Solucao "+i+" FAV: "+ListaSolucao.get(i).getFAV()+" FAV2: "+ListaSolucao.get(i).getFAV2());
    }

    return ListaSolucao;
    //return VetorSolucao;
   }

}