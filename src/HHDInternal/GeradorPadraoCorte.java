package HHDInternal;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;


public class GeradorPadraoCorte {

    int NPP, NP, NCorteP, NCP;
    double media;
    int aleatorioCorte;
    
    public LinkedList<Corte> listCortes = new LinkedList<Corte>();
    public ArrayList<Pedaco> listPedacos = new ArrayList<Pedaco>();
    public ArrayList<Pedaco> listPedacosFinal = new ArrayList<Pedaco>();
    public ArrayList<ArrayList<Pedaco>> solucao = new ArrayList<ArrayList<Pedaco>>();
    
    public LinkedList<Corte> getCortes(){
    
        return listCortes;
    }
    
    public ArrayList<Pedaco> geraMaisCortes(ArrayList<Pedaco> array, int numCortes, boolean tipoCorte){
    
        aleatorioCorte = numCortes;
        Pedaco p,p1 = null,p2 = null;
        SecureRandom random = new SecureRandom();
        
        int j = 0;
        //for(int j = 0; j < (aleatorioCorte); j++){ //Tirei o menos 1
        boolean b;
        
        while(j < (aleatorioCorte)){
            
            Pedaco pedacoEscolhido = array.get(random.nextInt(array.size()));
                
            int C = (int) pedacoEscolhido.retorneDimensao().retorneAltura();
            int L = (int) pedacoEscolhido.retorneDimensao().retorneBase();
                
            System.out.println("L ->  "+L+"\tC ->  "+C);

            float X = pedacoEscolhido.getPontoInferiorEsquerdo().getPositionX();
            float Y = pedacoEscolhido.getPontoInferiorEsquerdo().getPositionY();
            System.out.println("X ->  "+X+"\tY ->  "+Y);

            boolean isVertical = tipoCorte;
            
            int posicaoCorte = 0, tamanhoCorte = 0, corte = 0;
            b = false;
            
            if(C == 1 && isVertical == false){
                isVertical = true;
            }
            if(L == 1 && isVertical == true){
            
                isVertical = false;                
            }
            if(C == 1 && L == 1){
                
                continue;
            }
                
            //isVertical = random.nextBoolean();
            System.out.println("\nIsVertical -> "+isVertical);

            if(isVertical){                    

                while(b == false){

                    System.out.println("Repetindo while ...");

                    posicaoCorte = (random.nextInt(L)+1);
                    tamanhoCorte = C;

                    if(posicaoCorte < L){

                        System.out.println("Condição satisfeita ...");
                        b = true;
                    }
                 }
                System.out.println("PosicaoCorte -> "+posicaoCorte);
                System.out.println("TamanhoCorte -> "+tamanhoCorte);

                p1 = new Pedaco(new Dimensao2D((L-(L-posicaoCorte)),C), new Position(X,Y));

                p2 = new Pedaco(new Dimensao2D((L-posicaoCorte),C), new Position(X+posicaoCorte,Y));

            }else{

                 while(b == false){

                     posicaoCorte = (random.nextInt(C)+1);
                     tamanhoCorte = L;

                     if(posicaoCorte < C){

                        b = true;
                    }
                }

                System.out.println("PosicaoCorte -> "+posicaoCorte);
                System.out.println("TamanhoCorte -> "+tamanhoCorte);

                p1 = new Pedaco(new Dimensao2D(L,(C-(C-posicaoCorte))), new Position(X,Y));

                p2 = new Pedaco(new Dimensao2D(L,(C-posicaoCorte)), new Position(X,Y+posicaoCorte));                        
            }


            array.remove(pedacoEscolhido);
            array.add(p1);
            array.add(p2);

            Ponto ponto = new Ponto(p1.getPontoInferiorEsquerdo().getPositionX(), p1.getPontoInferiorEsquerdo().getPositionY());
            Corte cortes; 
            boolean r = listCortes.add(new Corte((float) posicaoCorte, ponto , isVertical, tamanhoCorte, ++corte));

            System.out.println("\nCortou --> "+r);

            j++;
        } //fim do for para os Cortes
        
        return array;
    
    }
    public ArrayList<Pedaco> geracaoPedacos(int numCortes, int numPlacas, int LP, int CP){
    
        NPP = numPlacas;
        NCorteP = numCortes;
        Pedaco p,p1 = null,p2 = null;
        SecureRandom random = new SecureRandom();
        SecureRandom randomCorte = new SecureRandom();
        
        for(int i = 0; i < numPlacas; i++){
        
            p = new Pedaco(new Dimensao2D(LP,CP));
            listPedacos.add(p);
            
            if(i != (numPlacas - 1)){
                
                float sd = NCorteP/NPP;
                media = Math.ceil(sd);
                
                System.out.println("Divisao -> "+sd);
                System.out.println("Media   -> "+media);
                
                
                aleatorioCorte = random.nextInt((int)media)+1;//Possivelmente deve somar a 1 == +1
                System.out.println("Aleatorio corte  -> "+aleatorioCorte);
                
                NCorteP = NCorteP - aleatorioCorte;
                
                System.out.println("Cortes restantes -> "+NCorteP);
                
            }else{
                
                aleatorioCorte = NCorteP; 
                System.out.println("Aleatorio corte  -> "+aleatorioCorte);
            }
            boolean b = false;
            int j = 0;
            //for(int j = 0; j < (aleatorioCorte); j++){ //Tirei o menos 1
            while(j < (aleatorioCorte)){
                boolean isVertical = random.nextBoolean();
                
                Pedaco pedacoEscolhido = listPedacos.get(random.nextInt(listPedacos.size()));

                int C = (int) pedacoEscolhido.retorneDimensao().retorneAltura();
                int L = (int) pedacoEscolhido.retorneDimensao().retorneBase();

                System.out.println("L ->  "+L+"\tC ->  "+C);

                float X = pedacoEscolhido.getPontoInferiorEsquerdo().getPositionX();
                float Y = pedacoEscolhido.getPontoInferiorEsquerdo().getPositionY();
                System.out.println("X ->  "+X+"\tY ->  "+Y);

                int posicaoCorte = 0, tamanhoCorte = 0, corte = 0;
                
                b = false;
                
                if(C == 1 && isVertical == false){
                
                    isVertical = true;
                }
                if(L == 1 && isVertical == true){
                    
                    isVertical = false;                
                }
                if(C == 1 && L== 1){
                
                    continue;
                }
                
                System.out.println("\nIsVertical -> "+isVertical);
                    
                if(isVertical){                    

                    System.out.println("b "+b);
                    while(b == false){

                        posicaoCorte = (random.nextInt(L)+1);
                        tamanhoCorte = C;

                        if(posicaoCorte < L){

                            b = true;
                        }
                    }
                    System.out.println("PosicaoCorte -> "+posicaoCorte);
                    System.out.println("TamanhoCorte -> "+tamanhoCorte);

                    p1 = new Pedaco(new Dimensao2D((L-(L-posicaoCorte)),C), new Position(X,Y));

                    p2 = new Pedaco(new Dimensao2D((L-posicaoCorte),C), new Position(X+posicaoCorte,Y));

                }else{

                    System.out.println("b "+b);
                    while(b == false){

                        posicaoCorte = (random.nextInt(C)+1);
                        tamanhoCorte = L;

                        if(posicaoCorte < C){

                            b = true;
                        }
                    }                        
                    System.out.println("PosicaoCorte -> "+posicaoCorte);
                    System.out.println("TamanhoCorte -> "+tamanhoCorte);

                    p1 = new Pedaco(new Dimensao2D(L,(C-(C-posicaoCorte))), new Position(X,Y));

                    p2 = new Pedaco(new Dimensao2D(L,(C-posicaoCorte)), new Position(X,Y+posicaoCorte));                        
                }
                
                listPedacos.remove(pedacoEscolhido);
                listPedacos.add(p1);
                listPedacos.add(p2);
            
                Ponto ponto = new Ponto(p1.getPontoInferiorEsquerdo().getPositionX(), p1.getPontoInferiorEsquerdo().getPositionY());
                Corte cortes = new Corte((float) posicaoCorte, ponto , isVertical, tamanhoCorte, ++corte);
                boolean r = listCortes.add(cortes);
                
                System.out.println("\nCortou --> "+r);
                j++;
            } //fim do for para os Cortes
            
            solucao.add(listPedacos);
            listPedacosFinal.addAll(listPedacos);
            listPedacos.removeAll(listPedacos);
            
            NPP = NPP - 1;  
         
        }//fim do for para as Placas
        
        return listPedacosFinal;
        //return solucao;
    }
             
             
    public static void main(String[] args) {
        
        // TODO code application logic here
        int cont = 1;
        ArrayList<Pedaco> result;
        ArrayList<ArrayList<Pedaco>> solution;
        GeradorPadraoCorte g = new GeradorPadraoCorte();
        
        result = g.geracaoPedacos(10, 2, 15, 10);
        
        Iterator<Pedaco> iter = result.iterator();

        System.out.println("Lista de Pedaços");
        int zero = 0;

        while(iter.hasNext()){

            Pedaco p = iter.next();

            System.out.println("\nPedaco -> "+cont);
            System.out.println("Dimensões: ( "+p.retorneDimensao().retorneBase()+" x "+p.retorneDimensao().retorneAltura()+" )");
            System.out.println("PInfEsq ( "+p.getPontoInferiorEsquerdo().getPositionX()+" , "+p.getPontoInferiorEsquerdo().getPositionY()+" )");
            cont++;

            if((p.retorneDimensao().retorneBase() == 0) ||(p.retorneDimensao().retorneAltura() == 0)){

                zero++;
            }
        }

        System.out.println("Zeros -> "+zero);
        System.out.println("\n\nLista de Cortes");
        Iterator<Corte> it = g.getCortes().iterator();
        cont = 1;
        boolean b = iter.hasNext();
        if(b == false) System.out.println("dsfdsfdsf");
        while(iter.hasNext()){

            Corte c = it.next();

            System.out.println("\nCorte -> "+cont);
            System.out.println("TamCorte -> "+c.getTamanho()+ "Posição corte -> "+c.getPosicaoCorte()+ "Ponto Corte -> ("+
                                                    c.getPontoChapaCortada().getX()+","+c.getPontoChapaCortada().getY()+")");

            cont++;
        }
        

       
        /*System.out.println("Vc deseja cortar mais: ");
        boolean bool = true;
        ArrayList<ArrayList<Pedaco>> solucao;
        while(bool){
                
            String recebe = new Scanner(System.in).nextLine();
            
            if(recebe.equalsIgnoreCase("Yes")|| recebe.equalsIgnoreCase("Y")){
        
                System.out.println("Numero de cortes desejado: ");
                int     numC = new Scanner(System.in).nextInt();
                
                System.out.println("True ou false: ");
                boolean bl   = new Scanner(System.in).nextBoolean();
                result = g.geraMaisCortes(result, numC, bl);
            }
            else{
            
                bool = false;
            }
        }*/
        
    }
}