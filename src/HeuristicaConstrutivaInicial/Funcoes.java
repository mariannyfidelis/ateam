package HeuristicaConstrutivaInicial;

import java.io.*;
import java.security.SecureRandom;
import java.util.*;
import javax.swing.JOptionPane;

public class Funcoes{
        
    static int cntPecaH, cntPecaV, cntCortH, cntCortV, cntSobraH, cntSobraV;
    static LinkedList<IPedido> listPedidos = new LinkedList<IPedido>();
    static LinkedList<j_HeuristicaArvoreNAria.Pedidos> linkPedidos = new LinkedList<j_HeuristicaArvoreNAria.Pedidos>();
    
    public static ArrayList LerArq() throws FileNotFoundException, IOException{

        File arq = new File("/home/marianny/workspace/ProjetoA-Team/src/Util/arquivo1.txt");

        try {
            FileReader filereader = new FileReader(arq);
            BufferedReader bufferedreader = new BufferedReader (filereader);
            String linha ="";
            String result ="";
            ArrayList<Item> itens = new ArrayList();
            
            int x = 0;
            
            while ((linha = bufferedreader.readLine()) != null){
                
               if (x > 0){
                    result = linha;
                    String resultado[]= result.split(" ");
                    Item item = new Item(x,Integer.valueOf(resultado[0].trim()),Integer.valueOf(resultado[1].trim()),
                                                                                Integer.valueOf(resultado[2].trim()));
                    itens.add(item);
                    listPedidos.add(item);
               }
               x++;
            }
            filereader.close();
            bufferedreader.close();

            return itens;


        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, e);
        }
                return null;
    }

    public static void OrdenaList (List<Item> itens){

        boolean houveTroca = true;

        while (houveTroca) {
                houveTroca = false;
                for (int i = (itens.size())-1; i >=1 ; i--){
                        if (itens.get(i).getV() > itens.get(i-1).getV()){
                                Item aux1 = itens.get(i-1);
                                itens.set(i-1, itens.get(i));
                                itens.set(i, aux1);
                                houveTroca = true;
                        }
                }
        }
    }

    public static void ImprimeItens(String titulo,List<Item> itens){
        System.out.println(titulo);
        System.out.println("N.  Id    W    H   D    V    O");
        System.out.println("--------------------------------------");
        try {
                for(int i = 0;i<itens.size();i++){
            System.out.println(i+"   "+itens.get(i).getId()+"    "+itens.get(i).getW()+"   "+itens.get(i).getH()+"   "+itens.get(i).getD()+"   "+itens.get(i).getV()+"   "+itens.get(i).getO());
                }
        }catch(NullPointerException e) {}
    }

    public static Item CriaItem(Item x,Item y){
        
        x.setId(y.getId());
        x.setW(y.getW());
        x.setH(y.getH());
        x.setD(y.getD());
        x.setV(y.getV());

        return x;
    }
    
    public static j_HeuristicaArvoreNAria.Pedidos ConverteItemPedido(Item i){

        j_HeuristicaArvoreNAria.Pedidos p = new j_HeuristicaArvoreNAria.Pedidos(i.getId(), i.getW(), i.getH());
        
        return p;
    }
    
    public static LinkedList<j_HeuristicaArvoreNAria.Pedidos> CriaListaPedidos(ArrayList<Item> a){
    
        Item temp;
        Iterator<Item> it_item = a.iterator();
        LinkedList<j_HeuristicaArvoreNAria.Pedidos> list_Pedidos =  new LinkedList<j_HeuristicaArvoreNAria.Pedidos>();
        
        while(it_item.hasNext()){
            temp = it_item.next();
            list_Pedidos.add(new j_HeuristicaArvoreNAria.Pedidos(temp.getId(), temp.getW(), temp.getH()));
        }
    
        return list_Pedidos;
    }

    public static List<Item> CriaLrc(List<Item> Lc, double alpha){

        List<Item> LRC = new ArrayList<Item>();

        double b = 1+alpha*(Lc.size()-1);
        
        LRC = Lc.subList(0, (int) b);
        //double b = Lc.get(0).getV()*alpha;
        //ImprimeItens("Lc",Lc);
        /*int lim =1;
        for(int i = Lc.size()-1;Lc.get(i).getV() <= b && i > 0;i--){

            lim=i;

        }
        
        LRC = Lc.subList(0, lim);*/
        return LRC;
    }

    public static LinkedList<IPedido> listIPedido(){
        
        return listPedidos;
    }
    public static int Aleatorio(int l){
        
            SecureRandom r = new SecureRandom();
            
            int x =0;
            
            if (l > 0){
            
                x = r.nextInt(l);
            }

        return x;
    }

    public static boolean PodeAlocar(Item p, int w, int h){

            if (p.getV() > w*h){return false;}

            if (p.getW() > w && p.getW() > h){return false;}

            if (p.getH() > h && p.getH() > w){return false;}

            if (p.getW() > w && p.getH() > w){return false;}

            if (p.getH() > h && p.getW() > h){return false;}

            return true;
    }

    public static boolean RotacaoItem(Item i, int w, int h){
        
        if (i.getW() > h || i.getH() > w){
        
            System.out.println("Não pode rotaciona o item -> "+i.getId());
            
            return false;
        }
        
        /*else*/if ( (i.getH() > h && i.getH() <= w && i.getW() <= h) || (i.getW() > w && i.getW() <= h && i.getH() <= w)){
        
            System.out.println("Pode rotacionar o item -> "+i.getId());
            
            return true;
        }

        SecureRandom x = new SecureRandom();
        
        return x.nextBoolean();
       /*if ((h - i.getH())*i.getW() > (w - i.getW())*i.getH()){
            return true;
        }

        return true;*/

    }

    public static Faixas CriaFh(Item p, int w, int h, int o, Ponto pIfE, Ponto pSD){
        
        Faixas f = new Faixas();
        
        if(o == 0 ){
            
            f.setW(w);
            f.setH(p.getH());
            f.setPi(w*p.getH());
            f.setRw(w);
            f.setRh(p.getH());
            f.setO(o);

            f.setPontoInferiorEsquerdo(pIfE);
            f.setPontoSuperiorDireito(new Ponto(pSD.getX(),(p.getH() + pIfE.getY())));
            
            f.setPontInfERw(pIfE);
            f.setPontSupDRh(new Ponto(pSD.getX(), (p.getH() + pIfE.getY())));
            
        }else if (o == 1){
            
            f.setW(w);
            f.setH(p.getW());
            f.setPi(w*p.getW());
            f.setRw(w);
            f.setRh(p.getW());
            f.setO(o);
            
            f.setPontoInferiorEsquerdo(pIfE);
            f.setPontoSuperiorDireito(new Ponto(pSD.getX(), (p.getW() + pIfE.getY())));
            
            f.setPontInfERw(pIfE);
            f.setPontSupDRh(new Ponto(pSD.getX(), (p.getW() + pIfE.getY())));
        }
        
    /* Aqui deve inserir informações sobre a nova faixa criada  para cada orientação*/
        

        return f;
    }

    public static Faixas CriaFv(Item p, int w, int h, int o, Ponto pIfE, Ponto pSD ){//
    
        Faixas f = new Faixas();
        
        if(o == 0 ){
            
            f.setW(p.getW());
            f.setH(h);
            f.setPi(p.getW()*h);
            f.setRw(p.getW());
            f.setRh(h);
            f.setO(o);
            
            f.setPontoInferiorEsquerdo(pIfE);
            f.setPontoSuperiorDireito(new Ponto((p.getW() + pIfE.getX()), pSD.getY()));
            
            f.setPontInfERw(pIfE);
            f.setPontSupDRh(new Ponto((p.getW() + pIfE.getX()), pSD.getY()));

        }else {
            
            f.setW(p.getH());
            f.setH(h);
            f.setPi(p.getH()*h);
            f.setRw(p.getH());
            f.setRh(h);
            f.setO(o);
            
            f.setPontoInferiorEsquerdo(pIfE);
            f.setPontoSuperiorDireito(new Ponto((p.getH() + pIfE.getX()), pSD.getY()));
            
            f.setPontInfERw(pIfE);
            f.setPontSupDRh(new Ponto((p.getH() + pIfE.getX()), pSD.getY()));
        }
        
        /* Aqui deve inserir informações sobre a nova faixa criada  para cada orientação*/

        return f;
    }

    public static void AlocarItemFh(Item p, Faixas f, List<Item> Fhi, StringResultado sh, String t, Faixas fh_aux,
                                            LinkedList<Corte> l_corte, LinkedList<Peca> l_peca, LinkedList<PedacoDisponivel> l_sobra,
                                            Ponto pSD, LinkedList<PedacoDisponivel> list_sobras) throws PecaInvalidaException{
       
       cntPecaH = l_peca.size();
       cntCortH = l_corte.size();
       cntSobraH = l_sobra.size();
              
       for (int i = 0; i < p.getD();i++ ){
            
            if (Funcoes.PodeAlocar(p, f.getRw(), f.getRh())){
                
                if (i == 0){// && t.equals("H")) || i == 0 && t.equals("M") && sh.getA().equals("H")){
                    
                    sh.setS(sh.getS()+t);
                    sh.setA(t);
                }
                if(f.getO() == 0){
                    
                    f.setPi(f.getPi() - p.getV());
                    f.setRw(f.getRw() - p.getW());
                    p.setO(0); //Talvez não seja necessário
                    Fhi.add(p);
                    
                    //Informações extras 
                    //fh_aux.setPontoSuperiorDireito(new Ponto(fh_aux.getPontoSuperiorDireito().getX(), p.getH()));
                    //fh_aux.setPontInfERw(new Ponto(fh_aux.getPontInfERw().getX()+p.getW(), fh_aux.getPontInfERw().getY()));
                    //fh_aux.setPontSupDRh(new Ponto(fh_aux.getPontSupDRh().getX(), p.getH()));                    
                    
                    //A lista de sobra auxiliar recebe a principal e atualiza
                    l_sobra.clear();
                    l_sobra.addAll(list_sobras);
                    
                    cntPecaH = cntPecaH + 1;
                    //Aqui insere informações de Pedaços Disponíveis/Peças /Cortes e Sobras ##############################
                    PedacoDisponivel pedac = new PedacoDisponivel(f.getPontInfERw(), 
                                                    new Ponto(f.getPontInfERw().getX()+p.getW(),(p.getH() + f.getPontInfERw().getY())),cntPecaH);
                    
                    Peca peca_Ser_Cortada = new Peca(pedac, p, false);
                    
                    //Adiciona a Peça a lista de Peças da Faixa Horizontal
                    l_peca.add(peca_Ser_Cortada);
                    
                    System.out.println("\n#################\nCriei uma peça Horizontal - Dados - (W x H) ("
                        + peca_Ser_Cortada.retornaBase() + " x " + peca_Ser_Cortada.retornaAltura()+")");
                    System.out.println("Peça --> PontInfE( "+peca_Ser_Cortada.getPontoInfEsq().getX()+","+peca_Ser_Cortada.getPontoInfEsq().getY()+")"
                            + "\tPontSupDir("+peca_Ser_Cortada.getPontoSupDir().getX()+","+peca_Ser_Cortada.getPontoSupDir().getY()+")");
                    
                    Corte corte1 = new Corte();
		    Corte corte2 = new Corte();
		
                    float pos1, pos2, inicio1, inicio2, tamanho1, tamanho2;
                    
                    if((peca_Ser_Cortada.getPontoSupDir().getY() == f.getPontoSuperiorDireito().getY()) && 
                                                                                  peca_Ser_Cortada.getPontoSupDir().getX() < f.getPontoSuperiorDireito().getX()){
                        cntCortH = cntCortH + 1;
                        corte1.registraCorte(peca_Ser_Cortada.getPontoSupDir().getX(), new Ponto(peca_Ser_Cortada.getPontoSupDir().getX(),
                                                                                                 peca_Ser_Cortada.getPontoInfEsq().getY()), true, 
                                                                                                 peca_Ser_Cortada.retornaAltura(),
                                                                                                 l_corte.size()+1);                      
                        System.out.println("\nCorte realizado no Ponto("+corte1.getPontoChapaCortada().getX()+","
                                           + corte1.getPontoChapaCortada().getY()+")  Tamanho -> "+corte1.getTamanho());
                        
                        //Adicionando a lista de corte
                        l_corte.add(corte1);
                        
                        //Criar uma sobra
                        PedacoDisponivel sobra1 = 
                                new PedacoDisponivel(new Ponto(f.getPontInfERw().getX()+p.getW(), f.getPontInfERw().getY()),
                                                     new Ponto(f.getPontSupDRh().getX(), (p.getH() + f.getPontInfERw().getY())), cntSobraH);
                                                                                                   //Aqui pode ser a altura da faixa !!!
                        System.out.println("\nSobra gerada com PInfE ("+sobra1.getPontoInferiorEsquerdo().getX()+","+
                                  sobra1.getPontoInferiorEsquerdo().getY()+" ) e PSupDir("+sobra1.getPontoSuperiorDireito().getX()+","+
                                  sobra1.getPontoSuperiorDireito().getY()+")");
                        
                        if(!l_sobra.isEmpty()){
                            System.out.println("\nQuantidade de Lsobra "+l_sobra.size());
                            l_sobra.removeLast();
                            l_sobra.add(sobra1);
                        }

                    }else if((peca_Ser_Cortada.getPontoSupDir().getX() == f.getPontoSuperiorDireito().getX()) && 
                                                                        peca_Ser_Cortada.getPontoSupDir().getY() < f.getPontoSuperiorDireito().getY()){
                        cntCortH = cntCortH + 1;
                        corte1.registraCorte(peca_Ser_Cortada.getPontoSupDir().getY(), new Ponto(peca_Ser_Cortada.getPontoInfEsq().getX(),
                                                                                                peca_Ser_Cortada.getPontoSupDir().getY()),//peca_Ser_Cortada.getPontoInfEsq().getY() + p.getH()),
                                                                                        false, peca_Ser_Cortada.retornaBase(), l_corte.size()+1);
                        
                        System.out.println("\nCorte realizado no Ponto("+corte1.getPontoChapaCortada().getX()+","
                                           + corte1.getPontoChapaCortada().getY()+")  Tamanho -> "+corte1.getTamanho());
                        
                        //Adicionando a lista de corte
                        l_corte.add(corte1);
                        
                        //Criar uma sobra
                        PedacoDisponivel sobra1 = 
                                       new PedacoDisponivel(new Ponto(f.getPontInfERw().getX(), f.getPontInfERw().getY()+p.getH()),
                                                             new Ponto(f.getPontSupDRh().getX(), f.getPontSupDRh().getY()), cntSobraH);
                        
                        if(!l_sobra.isEmpty()){
                            System.out.println("\nQuantidade de Lsobra "+l_sobra.size());
                            l_sobra.removeLast();
                            l_sobra.add(sobra1);
                        }
                        
                        System.out.println("\nSobra gerada com PInfE ("+sobra1.getPontoInferiorEsquerdo().getX()+","+
                                  sobra1.getPontoInferiorEsquerdo().getY()+" ) e PSupDir("+sobra1.getPontoSuperiorDireito().getX()+","+
                                  sobra1.getPontoSuperiorDireito().getY()+")");
                    }                    
                    else{ //São necessários 2 cortes e Teremos 2 sobras
                        
                        Ponto pife, pisd;
                        
                        if((peca_Ser_Cortada.getPontoSupDir().getX() == f.getPontoSuperiorDireito().getX()) &&
                                                        peca_Ser_Cortada.getPontoSupDir().getY() == f.getPontoSuperiorDireito().getY()){
                       
                           //Não precisa de corte pois já está no limite das placas
                           
                           pife = new Ponto(f.getPontInfERw().getX(), f.getPontInfERw().getY() + p.getH());
                           pisd = new Ponto(pSD.getX(), pSD.getY());
                           
                           if(!l_sobra.isEmpty()){
                               System.out.println("\nQuantidade de Lsobra "+l_sobra.size());
                               l_sobra.removeLast();
                           }
                           //Não será mais necessário sobra houve um encaixe perfeito na faixa
                           System.out.println("\nNão será mais necessário sobra ! Houve um encaixe perfeito na faixa !");
                           //PedacoDisponivel sobra1 = new PedacoDisponivel(pife, pisd, (l_sobra.size()+1));
                           //l_sobra.add(sobra1);
                           
                           //System.out.println("\nSobra 1 gerada com PInfE ("+sobra1.getPontoInferiorEsquerdo().getX()+","+
                           //           sobra1.getPontoInferiorEsquerdo().getY()+" ) e PSupDir("+sobra1.getPontoSuperiorDireito().getX()+","+
                           //           sobra1.getPontoSuperiorDireito().getY()+")");
                       
                        }
                        else{

                            pife = new Ponto(peca_Ser_Cortada.getPontoSupDir().getX(),peca_Ser_Cortada.getPontoInfEsq().getY());
                            pisd = new Ponto(f.getPontSupDRh().getX(), f.getPontSupDRh().getY());

                            cntCortH = cntCortH + 1;
                            corte1.registraCorte(peca_Ser_Cortada.getPontoSupDir().getX(),pife,true, f.getH(), l_corte.size()+1); //Altura da faixa

                            System.out.println("\nCorte 1 realizado no Ponto("+corte1.getPontoChapaCortada().getX()+","
                                               + corte1.getPontoChapaCortada().getY()+")  Tamanho -> "+corte1.getTamanho());

                            //Adicionando a lista de corte
                            l_corte.add(corte1);

                            //Sobra relacionada ao corte 1 realizado
                            PedacoDisponivel sobra1 = new PedacoDisponivel(pife, pisd,  ++cntSobraH);

                            System.out.println("\nSobra 1 gerada com PInfE ("+sobra1.getPontoInferiorEsquerdo().getX()+","+
                                      sobra1.getPontoInferiorEsquerdo().getY()+" ) e PSupDir("+sobra1.getPontoSuperiorDireito().getX()+","+
                                      sobra1.getPontoSuperiorDireito().getY()+")");

                            if(!l_sobra.isEmpty()){
                                
                                System.out.println("\nQuantidade de Lsobra "+l_sobra.size());
                                l_sobra.removeLast();
                            }

                            pife = new Ponto(peca_Ser_Cortada.getPontoInfEsq().getX(), peca_Ser_Cortada.getPontoSupDir().getY());
                                             //peca_Ser_Cortada.getPontoInfEsq().getY() + peca_Ser_Cortada.retorneDimensoes().retorneAltura());

                            pisd = new Ponto(peca_Ser_Cortada.getPontoSupDir().getX(), f.getPontoSuperiorDireito().getY());

                            cntCortH = cntCortH + 1;

                            corte2.registraCorte(peca_Ser_Cortada.getPontoInfEsq().getY() + peca_Ser_Cortada.retornaAltura(),
                                                 pife, false, peca_Ser_Cortada.retornaBase(), l_corte.size()+1);

                            System.out.println("\nCorte 2 realizado no Ponto("+corte2.getPontoChapaCortada().getX()+","
                                               + corte2.getPontoChapaCortada().getY()+")  Tamanho -> "+corte2.getTamanho());

                            //Adicionando a lista de corte
                            l_corte.add(corte2);

                            PedacoDisponivel sobra2 = new PedacoDisponivel(pife, pisd,  --cntSobraH);

                            l_sobra.add(sobra2);
                            l_sobra.add(sobra1);

                            System.out.println("\nSobra 2 gerada com PInfE ("+sobra2.getPontoInferiorEsquerdo().getX()+","+
                                      sobra2.getPontoInferiorEsquerdo().getY()+" ) e PSupDir("+sobra2.getPontoSuperiorDireito().getX()+","+
                                      sobra2.getPontoSuperiorDireito().getY()+")");
                       }
                    }
		    
                    //Aqui Atualiza o Pedaço Disponível
                    f.setPontInfERw(new Ponto(f.getPontInfERw().getX()+p.getW(), f.getPontInfERw().getY()));
                    f.setPontSupDRh(new Ponto(f.getPontSupDRh().getX(), f.getPontSupDRh().getY()));//(p.getH() + f.getPontInfERw().getY())
                                           
                    sh.setS(sh.getS()+Integer.toString(1)+"P"+Integer.toString(p.getId())) ;
                    
                }else{
                    
                    f.setPi(f.getPi() - p.getV());
                    f.setRw(f.getRw() - p.getH());                    
                    p.setO(1);
                    Fhi.add(p);
                    
                    //Informações extras 
                    //fh_aux.setPontoSuperiorDireito(new Ponto(fh_aux.getPontoSuperiorDireito().getX(),p.getW()));
                    //fh_aux.setPontInfERw(new Ponto(fh_aux.getPontInfERw().getX()+p.getH(), fh_aux.getPontInfERw().getY()));
                    //fh_aux.setPontSupDRh(new Ponto(fh_aux.getPontSupDRh().getX(), p.getW()));                    
                    
                    //A lista de sobra auxiliar recebe a principal e atualiza
                    l_sobra.clear();
                    l_sobra.addAll(list_sobras);
                    
                    cntPecaH = cntPecaH + 1;
                    
                    //Aqui insere informações de Pedaços Disponíveis/Peças /Cortes e Sobras ##############################
                    PedacoDisponivel pedac = new PedacoDisponivel(f.getPontInfERw(), 
                                                    new Ponto(f.getPontInfERw().getX()+p.getH(),(p.getW() + f.getPontInfERw().getY())),cntPecaH);
                    
                    Peca peca_Ser_Cortada = new Peca(pedac, p, true);
                    
                    //Adiciona a Peça a lista de Peças da Faixa Horizontal
                    l_peca.add(peca_Ser_Cortada);
                    
                    System.out.println("\n#################\nCriei uma peça Horizontal- Dados - (W x H) ("
                        + peca_Ser_Cortada.retornaBase() + " x " + peca_Ser_Cortada.retornaAltura()+")");
                    System.out.println("Peça --> PontInfE( "+peca_Ser_Cortada.getPontoInfEsq().getX()+","+peca_Ser_Cortada.getPontoInfEsq().getY()+")"
                            + "\tPontSupDir("+peca_Ser_Cortada.getPontoSupDir().getX()+","+peca_Ser_Cortada.getPontoSupDir().getY()+")");
                    
                    Corte corte1 = new Corte();
		    Corte corte2 = new Corte();
		
                    float pos1, pos2, inicio1, inicio2, tamanho1, tamanho2;
                    
                    if((peca_Ser_Cortada.getPontoSupDir().getY() == f.getPontoSuperiorDireito().getY()) && 
                                                                peca_Ser_Cortada.getPontoSupDir().getX() < f.getPontoSuperiorDireito().getX()){
                        
                        cntCortH = cntCortH + 1;
                        corte1.registraCorte(peca_Ser_Cortada.getPontoSupDir().getX(), new Ponto(peca_Ser_Cortada.getPontoSupDir().getX(),
                                                                                                peca_Ser_Cortada.getPontoInfEsq().getY()), 
                                                                                true, peca_Ser_Cortada.retornaAltura(),
                                                                                l_peca.size()+1);
                        System.out.println("\nCorte realizado no Ponto("+corte1.getPontoChapaCortada().getX()+","
                                           + corte1.getPontoChapaCortada().getY()+")  Tamanho -> "+corte1.getTamanho());
                        
                        //Adicionando a lista de corte
                        l_corte.add(corte1);
                        
                        //Criar uma sobra
                        PedacoDisponivel sobra1 = 
                                  new PedacoDisponivel(new Ponto(f.getPontInfERw().getX()+p.getH(), f.getPontInfERw().getY()),
                                                       new Ponto(f.getPontSupDRh().getX(), (p.getW() + f.getPontInfERw().getY())),  cntSobraH);
                                                                                                //Aqui pode ser a altura da faixa !!!
                        if(!l_sobra.isEmpty()){
                            System.out.println("\nQuantidade de Lsobra "+l_sobra.size());
                            l_sobra.removeLast();
                            l_sobra.add(sobra1);
                        }                                                                       
                        
                        System.out.println("\nSobra gerada com PInfE ("+sobra1.getPontoInferiorEsquerdo().getX()+","+
                                  sobra1.getPontoInferiorEsquerdo().getY()+" ) e PSupDir("+sobra1.getPontoSuperiorDireito().getX()+","+
                                  sobra1.getPontoSuperiorDireito().getY()+")");
                        
                    }else if((peca_Ser_Cortada.getPontoSupDir().getX() == f.getPontoSuperiorDireito().getX()) && 
                                                                peca_Ser_Cortada.getPontoSupDir().getY() < f.getPontoSuperiorDireito().getY()){
  
                        cntCortH = cntCortH + 1;
                        corte1.registraCorte(peca_Ser_Cortada.getPontoSupDir().getY(), new Ponto(peca_Ser_Cortada.getPontoInfEsq().getX(),
                                                                                     peca_Ser_Cortada.getPontoSupDir().getY()),//peca_Ser_Cortada.getPontoInfEsq().getY() + p.getH()),
                                                                                 false, peca_Ser_Cortada.retornaBase(),
                                                                                 l_corte.size()+1);
                        System.out.println("\nCorte realizado no Ponto("+corte1.getPontoChapaCortada().getX()+","
                                           + corte1.getPontoChapaCortada().getY()+")  Tamanho -> "+corte1.getTamanho());
                        
                        //Adicionando a lista de corte
                        l_corte.add(corte1);
                        
                        //Criar uma sobra
                        PedacoDisponivel sobra1 = 
                              new PedacoDisponivel(new Ponto(f.getPontInfERw().getX(), f.getPontInfERw().getY()+p.getW()),
                                                   new Ponto(f.getPontSupDRh().getX(), f.getPontSupDRh().getY()),  cntSobraH);

                        if(!l_sobra.isEmpty()){
                            System.out.println("\nQuantidade de Lsobra "+l_sobra.size());
                            l_sobra.removeLast();
                            l_sobra.add(sobra1);
                        }
                        
                        System.out.println("\nSobra gerada com PInfE ("+sobra1.getPontoInferiorEsquerdo().getX()+","+
                                  sobra1.getPontoInferiorEsquerdo().getY()+" ) e PSupDir("+sobra1.getPontoSuperiorDireito().getX()+","+
                                  sobra1.getPontoSuperiorDireito().getY()+")");
                    } 
                    else{ //São necessários 2 cortes e Teremos 2 sobras
                        
                        Ponto pife, pisd;
                        
                        if((peca_Ser_Cortada.getPontoSupDir().getX() == f.getPontoSuperiorDireito().getX()) &&
                                                       peca_Ser_Cortada.getPontoSupDir().getY() == f.getPontoSuperiorDireito().getY()){
                       
                           //Não precisa de corte pois já está no limite das placas
                           
                           pife = new Ponto(f.getPontInfERw().getX(), f.getPontInfERw().getY() + p.getW());
                           pisd = new Ponto(pSD.getX(), pSD.getY());
                           
                           if(!l_sobra.isEmpty()){
                               System.out.println("\nQuantidade de Lsobra "+l_sobra.size());
                               l_sobra.removeLast();
                           }
                           
                           //Não será mais necessário sobra houve um encaixe perfeito na faixa
                           System.out.println("\nNão será mais necessário sobra ! Houve um encaixe perfeito na faixa !");
                           //PedacoDisponivel sobra1 = new PedacoDisponivel(pife, pisd, (l_sobra.size()+1));
                           //l_sobra.add(sobra1);
                           
                           //System.out.println("\nSobra 1 gerada com PInfE ("+sobra1.getPontoInferiorEsquerdo().getX()+","+
                           //           sobra1.getPontoInferiorEsquerdo().getY()+" ) e PSupDir("+sobra1.getPontoSuperiorDireito().getX()+","+
                           //           sobra1.getPontoSuperiorDireito().getY()+")");
                       
                        }
                        else{
      
                            pife = new Ponto(peca_Ser_Cortada.getPontoSupDir().getX(),peca_Ser_Cortada.getPontoInfEsq().getY());
                            pisd = new Ponto(f.getPontSupDRh().getX(), f.getPontSupDRh().getY());

                            cntCortH = cntCortH + 1;
                            corte1.registraCorte(peca_Ser_Cortada.getPontoSupDir().getX(), pife, true,f.getH(), l_corte.size()+1); //Altura da faixa

                            System.out.println("\nCorte 1 realizado no Ponto("+corte1.getPontoChapaCortada().getX()+","
                                               + corte1.getPontoChapaCortada().getY()+")  Tamanho -> "+corte1.getTamanho());

                            //Adicionando a lista de corte
                            l_corte.add(corte1);

                            //Sobra relacionada ao corte 1 realizado
                            PedacoDisponivel sobra1 = new PedacoDisponivel(pife, pisd,  ++cntSobraH);

                            if(!l_sobra.isEmpty()){
                                
                                System.out.println("\nQuantidade de Lsobra "+l_sobra.size());
                                l_sobra.removeLast();
                            }

                            System.out.println("\nSobra 1 gerada com PInfE ("+sobra1.getPontoInferiorEsquerdo().getX()+","+
                                      sobra1.getPontoInferiorEsquerdo().getY()+" ) e PSupDir("+sobra1.getPontoSuperiorDireito().getX()+","+
                                      sobra1.getPontoSuperiorDireito().getY()+")");

                            pife = new Ponto(peca_Ser_Cortada.getPontoInfEsq().getX(), peca_Ser_Cortada.getPontoSupDir().getY());
                                             //peca_Ser_Cortada.getPontoInfEsq().getY() + peca_Ser_Cortada.retorneDimensoes().retorneAltura());

                            pisd = new Ponto(peca_Ser_Cortada.getPontoSupDir().getX(), f.getPontoSuperiorDireito().getY());

                            cntCortH = cntCortH + 1;
                            corte2.registraCorte(peca_Ser_Cortada.getPontoInfEsq().getY() + peca_Ser_Cortada.retornaAltura(),
                                                                                pife, false, peca_Ser_Cortada.retornaBase(),
                                                                                l_corte.size()+1);

                            System.out.println("\nCorte 2 realizado no Ponto("+corte2.getPontoChapaCortada().getX()+","
                                               + corte2.getPontoChapaCortada().getY()+")  Tamanho -> "+corte2.getTamanho());

                            //Adicionando a lista de corte
                            l_corte.add(corte2);

                            PedacoDisponivel sobra2 = new PedacoDisponivel(pife, pisd, --cntSobraH);

                            l_sobra.add(sobra2);
                            l_sobra.add(sobra1);

                            System.out.println("\nSobra 2 gerada com PInfE ("+sobra2.getPontoInferiorEsquerdo().getX()+","+
                                      sobra2.getPontoInferiorEsquerdo().getY()+" ) e PSupDir("+sobra2.getPontoSuperiorDireito().getX()+","+
                                      sobra2.getPontoSuperiorDireito().getY()+")");
                       }
                    }
                    
                    f.setPontInfERw(new Ponto(f.getPontInfERw().getX()+p.getH(), f.getPontInfERw().getY()));
                    f.setPontSupDRh(new Ponto(f.getPontSupDRh().getX(), f.getPontSupDRh().getY()));                    
                    
                    sh.setS(sh.getS()+Integer.toString(1)+"R"+Integer.toString(p.getId()));
                }
            }
            else{
                break;
            }
            
            System.out.println("\nFh (Rw,Rh) --> InfEsq("+f.getPontInfERw().getX()+","+f.getPontInfERw().getY()+")"
                                        + " SupDireito ("+f.getPontSupDRh().getX()+","+f.getPontSupDRh().getY() +")");
        }
    }

    public static void AlocarItemFv(Item p, Faixas f, List<Item> Fvi, StringResultado sv, String t, Faixas fv_aux,            
                                            LinkedList<Corte> l_corte, LinkedList<Peca> l_peca, LinkedList<PedacoDisponivel> l_sobra,
                                            Ponto pSD,LinkedList<PedacoDisponivel> list_sobras) throws PecaInvalidaException{
        
        cntPecaV = l_peca.size();
        cntCortV = l_corte.size();
        cntSobraV = l_sobra.size();
        
        for (int i = 0;i < p.getD();i++ ){
            
            if(Funcoes.PodeAlocar(p, f.getRw(), f.getRh())){
                if (i == 0 ){
                    
                    sv.setS(sv.getS()+t);
                    sv.setA(t);
                }
                if(f.getO() == 0){
                    
                    f.setPi(f.getPi() - p.getV());
                    f.setRh(f.getRh() - p.getH());
                    p.setO(0); //Talvez não seja mais necessário
                    Fvi.add(p);
                    
                    //Informações extras 
                    //fv_aux.setPontoSuperiorDireito(new Ponto(p.getW(), fv_aux.getPontoSuperiorDireito().getY()));
                    //fv_aux.setPontInfERw(new Ponto(fv_aux.getPontInfERw().getX(), fv_aux.getPontInfERw().getY()+p.getH()));
                    //fv_aux.setPontSupDRh(new Ponto(p.getW(), fv_aux.getPontSupDRh().getY()));
                                       
                    //############################################################################################################################
                    //A lista de sobra auxiliar recebe a principal e atualiza
                    l_sobra.clear();
                    l_sobra.addAll(list_sobras);
                    
                    
                    cntPecaV = cntPecaV + 1;
                    //Aqui insere informações de Pedaços Disponíveis/Peças /Cortes e Sobras ##############################
                    PedacoDisponivel pedac = new PedacoDisponivel(f.getPontInfERw(), 
                                                    new Ponto(f.getPontInfERw().getX()+p.getW(),(p.getH() + f.getPontInfERw().getY())),cntPecaV);
                    
                    Peca peca_Ser_Cortada = new Peca(pedac, p, false);
                    
                    //Adiciona a Peça a lista de Peças da Faixa Vertical
                    l_peca.add(peca_Ser_Cortada);
                    
                    System.out.println("\n#################\nCriei uma peça Vertical- Dados - (W x H) ("
                        + peca_Ser_Cortada.retornaBase() + " x " + peca_Ser_Cortada.retornaAltura()+")");
                    System.out.println("Peça --> PontInfE( "+peca_Ser_Cortada.getPontoInfEsq().getX()+","+peca_Ser_Cortada.getPontoInfEsq().getY()+")"
                            + "\tPontSupDir("+peca_Ser_Cortada.getPontoSupDir().getX()+","+peca_Ser_Cortada.getPontoSupDir().getY()+")");
                    
                    
                    Corte corte1 = new Corte();
		    Corte corte2 = new Corte();
		
                    float pos1, pos2, inicio1, inicio2, tamanho1, tamanho2;
                    
                    Ponto pife, pisd;
      
                    pife = new Ponto(peca_Ser_Cortada.getPontoInfEsq().getX(),peca_Ser_Cortada.getPontoSupDir().getY());
                    pisd = new Ponto(f.getPontSupDRh().getX(), f.getPontSupDRh().getY());
                                                
                    if((peca_Ser_Cortada.getPontoSupDir().getX() == f.getPontoSuperiorDireito().getX()) && 
                                                      (peca_Ser_Cortada.getPontoSupDir().getY() < f.getPontoSuperiorDireito().getY())){
                        
                        cntCortV = cntCortV + 1;
                        corte1.registraCorte(peca_Ser_Cortada.getPontoSupDir().getY(),pife, false, 
                                                                                peca_Ser_Cortada.retornaBase(),
                                                                                l_corte.size()+1);
                       
                       System.out.println("\nCorte realizado no Ponto("+corte1.getPontoChapaCortada().getX()+","
                                           + corte1.getPontoChapaCortada().getY()+")  Tamanho -> "+corte1.getTamanho());
                       
                       //Adicionando a lista de corte
                        l_corte.add(corte1);
                        
                       //Criar uma sobra relacionada ao corte 1
                       PedacoDisponivel sobra1 = new PedacoDisponivel(new Ponto(f.getPontInfERw().getX(), f.getPontInfERw().getY()+p.getH()),
                                                                      new Ponto(f.getPontInfERw().getX()+p.getW(), f.getPontSupDRh().getY()),
                                                                       cntSobraV);
                                                                                     //Aqui pode ser a altura da faixa !!!
                       if(!l_sobra.isEmpty()){
                            System.out.println("\nQuantidade de Lsobra "+l_sobra.size());
                            l_sobra.removeLast();
                            l_sobra.add(sobra1);
                       }
                       
                       System.out.println("\nSobra 1 gerada com PInfE ("+sobra1.getPontoInferiorEsquerdo().getX()+","+
                                  sobra1.getPontoInferiorEsquerdo().getY()+" ) e PSupDir("+sobra1.getPontoSuperiorDireito().getX()+","+
                                  sobra1.getPontoSuperiorDireito().getY()+")");
                       
                   }else if((peca_Ser_Cortada.getPontoSupDir().getY() == f.getPontoSuperiorDireito().getY()) 
                                                              && (peca_Ser_Cortada.getPontoSupDir().getX() < f.getPontoSuperiorDireito().getX())){
                        
                       cntCortV = cntCortV + 1;
                       corte1.registraCorte(peca_Ser_Cortada.getPontoSupDir().getX(), new Ponto(peca_Ser_Cortada.getPontoSupDir().getX(),
                                                                                                peca_Ser_Cortada.getPontoInfEsq().getY()),
                                            true, peca_Ser_Cortada.retornaAltura(), l_corte.size()+1); //Verificar isso aqui !!!
                       
                       System.out.println("\nCorte realizado no Ponto("+corte1.getPontoChapaCortada().getX()+","
                                           + corte1.getPontoChapaCortada().getY()+")  Tamanho -> "+corte1.getTamanho());
                       
                       //Adicionando a lista de corte
                        l_corte.add(corte1);
                        
                       //Ponto(peca_Ser_Cortada.getPontoSupDir().getX(),  peca_Ser_Cortada.getPontoInfEsq().getY()),
                    
                       //Criar uma sobra relacionada ao corte 1
                       PedacoDisponivel sobra1 = 
                                new PedacoDisponivel(new Ponto(f.getPontInfERw().getX() + p.getW(), f.getPontInfERw().getY()),//+p.getH()),
                                                     new Ponto(f.getPontSupDRh().getX(), f.getPontSupDRh().getY()),  cntSobraV);
                       
                       if(!l_sobra.isEmpty()){
                            System.out.println("\nQuantidade de Lsobra "+l_sobra.size());
                            l_sobra.removeLast();
                            l_sobra.add(sobra1);
                       }
                       
                       System.out.println("\nSobra 1 gerada com PInfE ("+sobra1.getPontoInferiorEsquerdo().getX()+","+
                                  sobra1.getPontoInferiorEsquerdo().getY()+" ) e PSupDir("+sobra1.getPontoSuperiorDireito().getX()+","+
                                  sobra1.getPontoSuperiorDireito().getY()+")");
                   }
                   else{ //São necessários 2 cortes e Teremos 2 sobras

                       if((peca_Ser_Cortada.getPontoSupDir().getX() == f.getPontoSuperiorDireito().getX()) &&
                               peca_Ser_Cortada.getPontoSupDir().getY() == f.getPontoSuperiorDireito().getY()){
                       
                           //Não precisa de corte pois já está no limite das placas
                           
                           pife = new Ponto(f.getPontInfERw().getX() + p.getW(), f.getPontInfERw().getY());
                           pisd = new Ponto(pSD.getX(), pSD.getY());//f.getPontSupDRh().getX(), f.getPontSupDRh().getY());
                           
                           if(!l_sobra.isEmpty()){
                               System.out.println("\nQuantidade de Lsobra "+l_sobra.size());
                               l_sobra.removeLast();
                           }
                           //Não será mais necessário sobra houve um encaixe perfeito na faixa
                           System.out.println("\nNão será mais necessário sobra ! Houve um encaixe perfeito na faixa !");
                           
                           //PedacoDisponivel sobra1 = new PedacoDisponivel(pife, pisd, (l_sobra.size()+1));
                           //l_sobra.add(sobra1);
                           
                           //System.out.println("\nSobra 1 gerada com PInfE ("+sobra1.getPontoInferiorEsquerdo().getX()+","+
                           //           sobra1.getPontoInferiorEsquerdo().getY()+" ) e PSupDir("+sobra1.getPontoSuperiorDireito().getX()+","+
                           //           sobra1.getPontoSuperiorDireito().getY()+")");
                       
                       } else{
                           pife = new Ponto(peca_Ser_Cortada.getPontoInfEsq().getX(),peca_Ser_Cortada.getPontoSupDir().getY());
                           pisd = new Ponto(f.getPontSupDRh().getX(), f.getPontSupDRh().getY());

                           cntCortV = cntCortV + 1;
                           corte1.registraCorte(peca_Ser_Cortada.getPontoSupDir().getY(), pife, false,f.getW(), l_corte.size()+1); //Altura da faixa

                           System.out.println("\nCorte 1 realizado no Ponto("+corte1.getPontoChapaCortada().getX()+","
                                               + corte1.getPontoChapaCortada().getY()+")  Tamanho -> "+corte1.getTamanho());

                           //Adicionando a lista de corte
                            l_corte.add(corte1);

                           //Sobra relacionada ao corte 1 realizado
                           PedacoDisponivel sobra1 = new PedacoDisponivel(pife, pisd,  ++cntSobraV);

                           if(!l_sobra.isEmpty()){
                               System.out.println("\nQuantidade de Lsobra "+l_sobra.size());
                               l_sobra.removeLast();
                           }

                           System.out.println("\nSobra 1 gerada com PInfE ("+sobra1.getPontoInferiorEsquerdo().getX()+","+
                                      sobra1.getPontoInferiorEsquerdo().getY()+" ) e PSupDir("+sobra1.getPontoSuperiorDireito().getX()+","+
                                      sobra1.getPontoSuperiorDireito().getY()+")");

                           cntCortV = cntCortV + 1;
                           corte2.registraCorte(peca_Ser_Cortada.getPontoInfEsq().getX() + peca_Ser_Cortada.retornaBase(),
                                                new Ponto(peca_Ser_Cortada.getPontoSupDir().getX(), peca_Ser_Cortada.getPontoInfEsq().getY()),
                                                true, peca_Ser_Cortada.retornaAltura(), l_corte.size()+1);

                           System.out.println("\nCorte 2 realizado no Ponto("+corte2.getPontoChapaCortada().getX()+","
                                               + corte2.getPontoChapaCortada().getY()+")  Tamanho -> "+corte2.getTamanho());

                           //Adicionando a lista de corte
                           l_corte.add(corte2);

                           pife = new Ponto(peca_Ser_Cortada.getPontoSupDir().getX(),peca_Ser_Cortada.getPontoInfEsq().getY());
                           pisd = new Ponto(f.getPontSupDRh().getX(),peca_Ser_Cortada.getPontoSupDir().getY());

                           PedacoDisponivel sobra2 = new PedacoDisponivel(pife, pisd, --cntSobraV);

                           l_sobra.add(sobra2);
                           l_sobra.add(sobra1);

                           System.out.println("\nSobra 2 gerada com PInfE ("+sobra2.getPontoInferiorEsquerdo().getX()+","+
                                      sobra2.getPontoInferiorEsquerdo().getY()+" ) e PSupDir("+sobra2.getPontoSuperiorDireito().getX()+","+
                                      sobra2.getPontoSuperiorDireito().getY()+")");
                       }
                    }
                    //##########################################################################
                                                              
                    f.setPontInfERw(new Ponto(f.getPontInfERw().getX(), f.getPontInfERw().getY() + p.getH()));
                    f.setPontSupDRh(new Ponto(f.getPontSupDRh().getX(), f.getPontSupDRh().getY()));
                    //f.setPontSupDRh(new Ponto((p.getW() + f.getPontInfERw().getX()), f.getPontSupDRh().getY()));
                    
                    sv.setS(sv.getS()+Integer.toString(1)+"P"+Integer.toString(p.getId()));
                }else{
                
                    f.setPi(f.getPi()-p.getV());
                    f.setRh(f.getRh()-p.getW());
                    p.setO(1); //Talvez não seja mais necessário
                    Fvi.add(p);
                    
                    //Informações extras 
                    //fv_aux.setPontoSuperiorDireito(new Ponto(p.getH(), fv_aux.getPontoSuperiorDireito().getY()));
                    //fv_aux.setPontInfERw(new Ponto(fv_aux.getPontInfERw().getX(), fv_aux.getPontInfERw().getY()+p.getW()));
                    //fv_aux.setPontSupDRh(new Ponto(p.getH(), fv_aux.getPontSupDRh().getY()));
                    
                    //A lista de sobra auxiliar recebe a principal e atualiza
                    l_sobra.clear();
                    l_sobra.addAll(list_sobras);
                    
                    //############################################################################################################################
                    cntPecaV = cntPecaV + 1;
                    
                    //Aqui insere informações de Pedaços Disponíveis/Peças /Cortes e Sobras ##############################
                    PedacoDisponivel pedac = new PedacoDisponivel(f.getPontInfERw(), 
                                                    new Ponto(f.getPontInfERw().getX()+p.getH(),(p.getW() + f.getPontInfERw().getY())),cntPecaV);
                    
                    Peca peca_Ser_Cortada = new Peca(pedac, p, true);
                    
                    //Adiciona a Peça a lista de Peças da Faixa Vertical
                    l_peca.add(peca_Ser_Cortada);
                    
                    System.out.println("\n#################\nCriei uma peça Vertical - Dados - (W x H) "
                        + peca_Ser_Cortada.retornaBase() + " x " + peca_Ser_Cortada.retornaAltura());
                    System.out.println("Peça --> PontInfE( "+peca_Ser_Cortada.getPontoInfEsq().getX()+","+peca_Ser_Cortada.getPontoInfEsq().getY()+")"
                            + "\tPontSupDir("+peca_Ser_Cortada.getPontoSupDir().getX()+","+peca_Ser_Cortada.getPontoSupDir().getY()+")");
                    
                    Corte corte1 = new Corte();
		    Corte corte2 = new Corte();
		
                    float pos1, pos2, inicio1, inicio2, tamanho1, tamanho2;
                    Ponto pife, pisd;

                    pife = new Ponto(peca_Ser_Cortada.getPontoInfEsq().getX(),peca_Ser_Cortada.getPontoSupDir().getY());
                    pisd = new Ponto(f.getPontSupDRh().getX(), f.getPontSupDRh().getY());
                    
                    if((peca_Ser_Cortada.getPontoSupDir().getX() == f.getPontoSuperiorDireito().getX())  && 
                                                        (peca_Ser_Cortada.getPontoSupDir().getY() < f.getPontoSuperiorDireito().getY())){
                       
                        cntCortV = cntCortV + 1;
                        corte1.registraCorte(peca_Ser_Cortada.getPontoSupDir().getY(),pife, false, 
                                                                                 peca_Ser_Cortada.retornaBase(),
                                                                                 l_corte.size()+1);
                           
                        System.out.println("\nCorte realizado no Ponto("+corte1.getPontoChapaCortada().getX()+","
                                           + corte1.getPontoChapaCortada().getY()+")  Tamanho -> "+corte1.getTamanho());
                           
                         //Adicionando a lista de corte
                         l_corte.add(corte1);
           
                         //Criar uma sobra relacionada ao corte 1
                         PedacoDisponivel sobra1 = new PedacoDisponivel(
                                                             new Ponto(f.getPontInfERw().getX(), f.getPontInfERw().getY() + p.getW()),
                                                             new Ponto(p.getH() + f.getPontInfERw().getX(), f.getPontSupDRh().getY()),
                                                             cntSobraV);
                         
                         if(!l_sobra.isEmpty()){
                             System.out.println("\nQuantidade de Lsobra "+l_sobra.size());
                             l_sobra.removeLast();
                             l_sobra.add(sobra1);
                         }
                           
                         System.out.println("\nSobra 1 gerada com PInfE ("+sobra1.getPontoInferiorEsquerdo().getX()+","+
                                  sobra1.getPontoInferiorEsquerdo().getY()+" ) e PSupDir("+sobra1.getPontoSuperiorDireito().getX()+","+
                                  sobra1.getPontoSuperiorDireito().getY()+")");
                           
                    }else if((peca_Ser_Cortada.getPontoSupDir().getY() == f.getPontoSuperiorDireito().getY()) 
                                                    && (peca_Ser_Cortada.getPontoSupDir().getX() < f.getPontoSuperiorDireito().getX())){
                         
                        cntCortV = cntCortV + 1;
                        corte1.registraCorte(peca_Ser_Cortada.getPontoSupDir().getX(), 
                                                                              new Ponto(peca_Ser_Cortada.getPontoSupDir().getX(),
                                                                                       peca_Ser_Cortada.getPontoInfEsq().getY()),
                                                                          true, peca_Ser_Cortada.retornaAltura(),l_corte.size()+1); //Verificar isso aqui !!!
           
                        System.out.println("\nCorte realizado no Ponto("+corte1.getPontoChapaCortada().getX()+","
                                           + corte1.getPontoChapaCortada().getY()+")  Tamanho -> "+corte1.getTamanho());
                        
                        //Adicionando a lista de corte
                        l_corte.add(corte1);
                        
                        //Criar uma sobra relacionada ao corte 1
                        PedacoDisponivel sobra1 = new PedacoDisponivel(
                                                       new Ponto(f.getPontInfERw().getX() + p.getH(), f.getPontInfERw().getY()),
                                                       new Ponto(f.getPontSupDRh().getX(), f.getPontSupDRh().getY()),
                                                       cntSobraV);
                        if(!l_sobra.isEmpty()){
                            System.out.println("\nQuantidade de Lsobra "+l_sobra.size());
                            l_sobra.removeLast();
                            l_sobra.add(sobra1);
                        }
                        
                        System.out.println("\nSobra 1 gerada com PInfE ("+sobra1.getPontoInferiorEsquerdo().getX()+","+
                                  sobra1.getPontoInferiorEsquerdo().getY()+" ) e PSupDir("+sobra1.getPontoSuperiorDireito().getX()+","+
                                  sobra1.getPontoSuperiorDireito().getY()+")");
                    }
                    else{ //São necessários 2 cortes
                        
                        if((peca_Ser_Cortada.getPontoSupDir().getX() == f.getPontoSuperiorDireito().getX()) &&
                               peca_Ser_Cortada.getPontoSupDir().getY() == f.getPontoSuperiorDireito().getY()){
                       
                           //Não precisa de corte pois já está no limite das placas
                           
                           pife = new Ponto(f.getPontInfERw().getX() + p.getH(), f.getPontInfERw().getY());
                           pisd = new Ponto(pSD.getX(), pSD.getY());//f.getPontSupDRh().getX(), f.getPontSupDRh().getY());
                           
                           if(!l_sobra.isEmpty()){
                               System.out.println("\nQuantidade de Lsobra "+l_sobra.size());
                               l_sobra.removeLast();
                           }
                           //Não será mais necessário sobra houve um encaixe perfeito na faixa
                           System.out.println("\nNão será mais necessário sobra ! Houve um encaixe perfeito na faixa !");
                           
                           //PedacoDisponivel sobra1 = new PedacoDisponivel(pife, pisd, (l_sobra.size()+1));
                           //l_sobra.add(sobra1);
                           
                           //System.out.println("\nSobra 1 gerada com PInfE ("+sobra1.getPontoInferiorEsquerdo().getX()+","+
                           //           sobra1.getPontoInferiorEsquerdo().getY()+" ) e PSupDir("+sobra1.getPontoSuperiorDireito().getX()+","+
                           //           sobra1.getPontoSuperiorDireito().getY()+")");
                       
                        } else{

                            pife = new Ponto(peca_Ser_Cortada.getPontoInfEsq().getX(),peca_Ser_Cortada.getPontoSupDir().getY());
                            pisd = new Ponto(f.getPontSupDRh().getX(), f.getPontSupDRh().getY());

                            cntCortV = cntCortV + 1;
                            corte1.registraCorte(peca_Ser_Cortada.getPontoSupDir().getY(), pife, false,f.getW(),l_corte.size()+1); //Altura da faixa

                            System.out.println("\nCorte 1 realizado no Ponto("+corte1.getPontoChapaCortada().getX()+","
                                               + corte1.getPontoChapaCortada().getY()+")  Tamanho -> "+corte1.getTamanho());

                            //Adicionando a lista de corte
                            l_corte.add(corte1);

                            //Sobra relacionada ao corte 1 realizado
                            PedacoDisponivel sobra1 = new PedacoDisponivel(pife, pisd, ++cntSobraV);

                            if(!l_sobra.isEmpty()){
                                System.out.println("\nQuantidade de Lsobra "+l_sobra.size());
                                l_sobra.removeLast();
                            }

                            System.out.println("\nSobra 1 gerada com PInfE ("+sobra1.getPontoInferiorEsquerdo().getX()+","+
                                      sobra1.getPontoInferiorEsquerdo().getY()+" ) e PSupDir("+sobra1.getPontoSuperiorDireito().getX()+","+
                                      sobra1.getPontoSuperiorDireito().getY()+")");

                            cntCortV = cntCortV + 1;
                            corte2.registraCorte(peca_Ser_Cortada.getPontoInfEsq().getX() + peca_Ser_Cortada.retornaBase(),
                                    new Ponto(peca_Ser_Cortada.getPontoSupDir().getX(), peca_Ser_Cortada.getPontoInfEsq().getY()),
                                    true, peca_Ser_Cortada.retornaAltura(), l_corte.size()+1);

                            System.out.println("\nCorte 2 realizado no Ponto("+corte2.getPontoChapaCortada().getX()+","
                                               + corte2.getPontoChapaCortada().getY()+")  Tamanho -> "+corte2.getTamanho());

                            //Adicionando a lista de corte
                            l_corte.add(corte2);

                            pife = new Ponto(peca_Ser_Cortada.getPontoSupDir().getX(),peca_Ser_Cortada.getPontoInfEsq().getY());
                            pisd = new Ponto(f.getPontSupDRh().getX(),peca_Ser_Cortada.getPontoSupDir().getY());

                            PedacoDisponivel sobra2 = new PedacoDisponivel(pife, pisd,  --cntSobraH);

                            l_sobra.add(sobra2);
                            l_sobra.add(sobra1);

                            System.out.println("\nSobra 2 gerada com PInfE ("+sobra2.getPontoInferiorEsquerdo().getX()+","+
                                      sobra2.getPontoInferiorEsquerdo().getY()+" ) e PSupDir("+sobra2.getPontoSuperiorDireito().getX()+","+
                                      sobra2.getPontoSuperiorDireito().getY()+")");
                        }
                    }
                    //##########################################################################
                                                                               
                    f.setPontInfERw(new Ponto(f.getPontInfERw().getX(), f.getPontInfERw().getY() + p.getW()));
                    f.setPontSupDRh(new Ponto(f.getPontSupDRh().getX(), f.getPontSupDRh().getY()));
                    //f.setPontSupDRh(new Ponto((p.getH() + f.getPontInfERw().getX()), f.getPontSupDRh().getY()));
                    
                    sv.setS(sv.getS()+Integer.toString(1)+"R"+Integer.toString(p.getId()));
                }

            }else{          
                break;
            }
            
            System.out.println("\nFv (Rw, Rh) --> InfEsq("+f.getPontInfERw().getX()+","+f.getPontInfERw().getY()+")"
                                        + " SupDireito ("+f.getPontSupDRh().getX()+","+f.getPontSupDRh().getY() +")");                    
        }
    }

    public static List<Item> CriaBh(List<Item> Lc, List<Item> Bh, int w, int h,Item p){
        
        Item item = new Item();
        
        //System.out.println("Fhrw x Fhrv "+w+"x"+h);
        Bh.clear();
        
        for(int i = 0;i< Lc.size();i++){
            
            item = Lc.get(i);
        
            //if(Funcoes.PodeAlocar(Item, w, h) ){
            if ((item.getW() <= w && item.getH() <= h) || (item.getW()<= h && item.getH() <= w)){
                Bh.add(item);
            }
        }
        
        Bh.remove(p);
       
        OrdenaBh(Bh);
        
      return Bh;
    }

    public static List<Item> CriaBv(List<Item> Lc, List<Item> Bv, int w, int h,Item p){
        
        Item item = new Item();
        Bv.clear();
        
        for(int i = 0;i<Lc.size();i++){
        
            item = Lc.get(i);
            //if(!Funcoes.PodeAlocar(Item, w, h)){
            if(((item.getW() <= w && item.getH() <= h) || (item.getW()<=h && item.getH() <= w))){
                Bv.add(item);
            }
        }
        
        Bv.remove(p);
        OrdenaBv(Bv);
        
      return Bv;
    }

    public static List<Item> MelhoreFh(Faixas Fh, List<Item> Bh ,Item item, List<Item> Fhi, StringResultado sh, Faixas fh_aux,
                                            LinkedList<Corte> l_corte, LinkedList<Peca> l_peca, LinkedList<PedacoDisponivel> l_sobra,
                                            Ponto pSD, LinkedList<PedacoDisponivel> list_sobra) throws PecaInvalidaException{

        
        if (!Funcoes.PodeAlocar(item, Fh.getRw(),Fh.getRh()) || item.getD() == 0){
            //System.out.println("Nao alocou Id->"+Item.getId()+" WxH->"+Item.getW()+"x"+Item.getH()+" V->"+Item.getV()+"Rfh x Rfw"+r.getW()+"x"+r.getH());
            Bh.remove(item);
            return Bh;
        }
        
        if (Funcoes.RotacaoItem(item, Fh.getRw(),Fh.getH())){
            
            Fh.setO(1);
            //System.out.println("Alocou Rotacionado Id->"+Item.getId()+" WxH->"+Item.getW()+"x"+Item.getH()+" V->"+Item.getV());
        }else{
            
            Fh.setO(0);
            //System.out.println("Alocou Id->"+Item.getId()+" WxH->"+Item.getW()+"x"+Item.getH()+" V->"+Item.getV());
        }
        
        System.out.println("Faixa Horizontal:\nAlocou item:\tId->"+item.getId()+" (W x H)-> ("+item.getW()+" x "+item.getH()+") V->"+item.getV()+ "O -> "+item.getO());    
        Funcoes.AlocarItemFh(item, Fh, Fhi,sh,"M",fh_aux, l_corte, l_peca,l_sobra, pSD,list_sobra);
        Bh.remove(item);
        
        return Bh;
    }

    public static List<Item> MelhoreFv(Faixas Fv,List<Item> Bv ,Item item,List<Item> Fvi, StringResultado sv, Faixas fv_aux,
                                            LinkedList<Corte> l_corte, LinkedList<Peca> l_peca, LinkedList<PedacoDisponivel> l_sobra,
                                            Ponto pSD, LinkedList<PedacoDisponivel> list_sobra) throws PecaInvalidaException{
        
        if (!Funcoes.PodeAlocar(item, Fv.getRw(), Fv.getRh()) || item.getD() == 0){
        //if (!Funcoes.PodeAlocar(item, Fv.getW(), Fv.getH()) || item.getD() == 0){
            Bv.remove(item);
            return Bv;
        }
        if (Funcoes.RotacaoItem(item, Fv.getRw(), Fv.getRh())){
        //if (Funcoes.RotacaoItem(item, Fv.getW(), Fv.getH())){
            Fv.setO(1);
            //System.out.println("Alocou Rotacionado Id->"+Item.getId()+" WxH->"+Item.getW()+"x"+Item.getH()+" V->"+Item.getV());
        }else{
            Fv.setO(0);
            //System.out.println("Alocou Id->"+Item.getId()+" WxH->"+Item.getW()+"x"+Item.getH()+" V->"+Item.getV());
        }
        
        System.out.println("Faixa Vertical:\nAlocou item:\tId->"+item.getId()+" (W x H)-> ("+item.getW()+" x "+item.getH()+") V->"+item.getV()+ "O -> "+item.getO());
        Funcoes.AlocarItemFv(item, Fv, Fvi,sv,"M", fv_aux, l_corte, l_peca, l_sobra, pSD,list_sobra);
        Bv.remove(item);
        
        return Bv;
    }

    public static List<Item> AtualizaC(List<Item> item, List<Item> c, Objeto S){
      
        try{
        int q = 1;
        
        for(int i = 0;i < item.size();i++ ){
        
            Item j = new Item();            
            
            j = Funcoes.CriaItem(j, item.get(i));
            
            //System.out.println("Item: "+j.getId()+"Qtde: "+j.getD());
            
            if (i > 0 && j.getId() == item.get(i-1).getId()){
                q++;
            }else{ q = 1;}
            
            j.setD(j.getD()-q);
            S.setFav(S.getFav()+j.getV());

            for (int x = 0; x < c.size();x++){
                if(j.getId() == c.get(x).getId()){
                   if(j.getD() > 0){
                        //System.out.println("Diminui 1 Unidade "+j.getId()+" IndexOf "+j+"-"+Item.get(i)+" "+c.indexOf(j));
                        c.set(x, j);
                   }else{
                        //System.out.println("Eliminou de C "+j.getId());
                        c.remove(x);
                   }

                   break;
                }
            }
        }
        
      }catch(NullPointerException e){ }

        //System.out.println("S->"+S.getFav());
      
        return c;
    }

    public static List<Item> AtualizaC2(List<Item> item, List<Item> c, Objeto Chapa, List<Item> lc, int w, int h, 
                                                                                            List<IPedido> lPedidosNaoAtendidos){ 
        try{        
            int q = 1;
            
            for(int i = 0;i < item.size();i++ ){
            
                Item j = new Item();
            
                j = Funcoes.CriaItem(j, item.get(i));
                System.out.println("Item: "+j.getId()+"  Qtde: "+j.getD());
                
                if (i > 0 && j.getId() == item.get(i-1).getId()){
                
                    q++;
                }
                else{ q = 1;}
                
                j.setD(j.getD() - q);
                Chapa.setFav(Chapa.getFav()+j.getV());
                
                for (int x = 0; x < c.size();x++){
                    
                    if(j.getId() == c.get(x).getId()){
                    
                        if(j.getD() > 0){
                            //System.out.println("Diminui 1 Unidade "+j.getId()+" IndexOf "+j+"-"+Item.get(i)+" "+c.indexOf(j));
                            c.set(x, j);
                        }else{
                            
                            System.out.println("Eliminou de C "+j.getId());
                            c.remove(x);
                            //lPedidosNaoAtendidos.remove(x);
                            /*Iterator<IPedido> iterator = lPedidosNaoAtendidos.iterator();
                            
                            while(iterator.hasNext()){
                            
                                IPedido pedido = iterator.next();
                                
                                if(pedido.id() == j.getId()){
                                    
                                }
                            
                            
                             * }
                            */
                            System.out.println("Pedidos Não Atendidos");
                            
                            for(int a = 0; a < lPedidosNaoAtendidos.size(); a++)
                                System.out.print(lPedidosNaoAtendidos.get(a).id()+" - ");
                            
                            System.out.println("");
                        }
                        
                        break;
                    }                    
                }                
            }
            
            lc.clear();
            lc.addAll(c);
            
            for (int i = 0; i < lc.size();i++){
                
                if (!PodeAlocar(lc.get(i), w, h)){
                    lc.remove(lc.get(i));
                }
            }   
            /*    for (int i= 0; i < c.size();i++){
                    if (PodeAlocar(c.get(i), w, h)){
                    //lc.add(c.get(i));
                    lc = c.subList(i, c.size());
                    break;
                    }
            }*/
        }catch(NullPointerException e){ }

        System.out.println("\nChapa -> "+Chapa.getFav());
        
        return c;
    }

    public static List<Item> AtualizaC3(List<Item> item, List<Item> c, Objeto S, List<Item> lc, int w, int h){
    
        try{
        
            int q = 1;
        
            for(int i = 0;i < item.size();i++ ){

                Item j = new Item();
                j = Funcoes.CriaItem(j, item.get(i));
                //System.out.println("Item: "+j.getId()+"Qtde: "+j.getD());
                if (i > 0 && j.getId() == item.get(i-1).getId()){
                    q++;
                }
                else{ q=1;}
                j.setD(j.getD()-q);
                S.setFav(S.getFav()+j.getV());


                for (int x = 0; x < c.size();x++){
                    
                    if(j.getId() == c.get(x).getId()){
                        if(j.getD() > 0){
                        //System.out.println("Diminui 1 Unidade "+j.getId()+" IndexOf "+j+"-"+Item.get(i)+" "+c.indexOf(j));
                        c.set(x, j);
                        }
                        else{
                            //System.out.println("Eliminou de C "+j.getId());
                            c.remove(x);
                        }
                        break;
                    }
                }

            }
        if (c.size() < 2){
            lc.clear();
            lc.addAll(c);
        }else if (PodeAlocar(c.get(0), w, h) ){
            lc.addAll(c);
        }else{
            int ini = 0, meio = 0, fim =0;
            fim = c.size();
            meio = fim/2;
            //System.out.println("WXH"+w+"x"+h);
            while(meio != fim){

                if(PodeAlocar(c.get(meio), w, h)){
        //            System.out.println(meio+"-"+fim);
                    fim = meio-1;
                    meio = (ini+fim)/2;
                }else{
        //            System.out.println(meio+"-"+fim);
                    ini=meio+1;
                    meio = (ini+fim)/2;
                }
            }
            lc = c.subList(meio, c.size());}
              }catch(NullPointerException e){ }

                //System.out.println("S->"+S.getFav());
                return c;
            }

    public static void OrdenaBh (List<Item> itens){

        //Pode-se ordenar passando a Collection e Comparar por Altura com um "sort"
         boolean houveTroca = true;

         while (houveTroca) {
             
             houveTroca = false;
             
             for (int i = (itens.size())-1; i >=1 ; i--){
                 
                 if (itens.get(i).getH() > itens.get(i-1).getH()){
                     
                     Item aux1 = itens.get(i-1);
                     itens.set(i-1, itens.get(i));
                     itens.set(i, aux1);
                     houveTroca = true;
                 }
             }
         }
    }

    public static void OrdenaBv (List<Item> itens){

        boolean houveTroca = true;

        while (houveTroca) {
            houveTroca = false;

            for (int i = (itens.size())-1; i >=1 ; i--){
                if (itens.get(i).getW() > itens.get(i-1).getW()){

                    Item aux1 = itens.get(i-1);
                    itens.set(i-1, itens.get(i));
                    itens.set(i, aux1);
                    houveTroca = true;
                }
            }
        }
    }


    public static void ImprimeSolucao(List<Objeto> Solucao){
        
        System.out.println("Numero de Objetos: "+Solucao.size());

        for(int i = 0; i < Solucao.size(); i++){
            
            System.out.println("Objeto "+i);
            
            List<Faixas> f = new ArrayList<Faixas>();
            f = Solucao.get(i).getF();
            
            for(int j = 0; j < f.size(); j++){
            
                System.out.println("Faixa: "+j+" Orientacao: "+f.get(j).getO());
                Funcoes.ImprimeItens("Itens", f.get(j).getItem());
            }
        }
    }

    public static Heuristicas.Solucao ConvertSolucao(List<Objeto> Solucao, LinkedList<IPedido> lPedidos, IDimensao2d tamanho){

        SolucaoHeuristica solucaoHeuristica = new SolucaoHeuristica(lPedidos, tamanho);

        LinkedList<Heuristicas.Individuo> individuous =new LinkedList<Heuristicas.Individuo>();

        for(int i = 0;i < Solucao.size(); i++){
            
            //System.out.println("Objeto "+i);    	
            List<Faixas> f = new ArrayList<Faixas>();
            f = Solucao.get(i).getF();
            
            ArrayList<Integer> IdIndividuos = new ArrayList<Integer>();
            
            for(int j = 0;j < f.size();j++){
            
                //System.out.println("Faixas: "+j+" Orientacao: "+f.get(j).getO());

                List<Item> it = new ArrayList<Item>();
                it = f.get(j).getItem();
                
                for (int k = 0;k < it.size();k++){
                
                    IdIndividuos.add(it.get(k).getId());
                }

            }
            
            Heuristicas.Individuo individuo = new Heuristicas.Individuo();
            individuo.setListaItens(IdIndividuos);
            individuous.add(individuo);

        }
        
        Heuristicas.Solucao solucao = new Heuristicas.Solucao(true,individuous,0);

      return solucao;
    }


    public static void BuscaBinaria(List<Item> Lc,int w,int h){
        ImprimeItens("LC Original", Lc);
        List<Item> lc = new ArrayList<Item>();
        int cont =0;

        for (int i= 0; i < Lc.size();i++){
                if (PodeAlocar(Lc.get(i), w, h)){
                    lc.add(Lc.get(i));
                }
        cont++;
        System.out.println(cont);
        }

        ImprimeItens("LC Estrategia 1", lc);
        System.out.println("Tamanho LC Original "+Lc.size());
        lc.clear();

        cont =0;
        ImprimeItens("LC Estrategia 2", lc);
        lc.clear();
        int ini = 0, meio = 0, fim =0;
        System.out.println("Tamanho LC Original "+Lc.size());
        fim = Lc.size();
        meio = fim/2;
        
        while(meio != fim){
            cont++;
            System.out.println(cont);        
            if(PodeAlocar(Lc.get(meio), w, h)){
                System.out.println(meio+"-"+fim);
                fim = meio-1;
                meio = (ini+fim)/2;            
            }else{
                System.out.println(meio+"-"+fim);
                ini=meio+1;
                meio = (ini+fim)/2;
            }
        }
        lc = Lc.subList(meio, Lc.size());

        ImprimeItens("Lc Estratégia 3", lc);
    }
    
    public static void imprimeSolucoes( LinkedList<Heuristicas.Solucao> VetorSolucao){
        
	for (int i =0; i < VetorSolucao.size();i++){
            
            Heuristicas.Solucao s = new Heuristicas.Solucao();
            s = VetorSolucao.get(i);
            
            System.out.println("Solucao "+i+" Com "+s.getLista().size()+" individuos");
            
            LinkedList<Heuristicas.Individuo> ind = new LinkedList<Heuristicas.Individuo>();
            
            ind = s.getLista();

            for(int j = 0;j < ind.size();j++){
                
                Heuristicas.Individuo it = new Heuristicas.Individuo();
                it = ind.get(j);
                System.out.print("Individou "+j+" Qtde Itens "+it.getSize()+" Itens ");
                
                for (int k = 0;k < it.getSize(); k++){
                     System.out.print(it.getListaItens().get(k).toString()+" ");
                }
                System.out.println();
            }
	}
    }
    
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
