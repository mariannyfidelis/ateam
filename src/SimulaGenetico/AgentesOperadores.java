package SimulaGenetico;

import Util.Funcoes;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import algoritmosAgCombinacao.Filhos;
import Heuristicas.Individuo;
import Util.ComparadorIndividuos;

public class AgentesOperadores {

    public static Filhos operadorCrossoverPonto1_v(Individuo indv1, Individuo indv2){	

        int ponto;
        int aux = 0;

        Filhos filhos = new Filhos();
        Random random = new Random();

        Individuo maior_individuo,menor_individuo;
        Individuo filho1 = new Individuo();
        Individuo filho2 = new Individuo();
        List<Integer> list1 = new ArrayList<Integer>();
        List<Integer> list2 = new ArrayList<Integer>();

        ComparadorIndividuos compara = new ComparadorIndividuos();
        aux = compara.compare(aux, indv1, indv2);

        if(aux == -1){
            ponto = random.nextInt(indv2.getSize());
            maior_individuo = indv2;
            menor_individuo = indv1;                
        }else if(aux == 1){
            ponto = random.nextInt(indv1.getSize());
            maior_individuo = indv1;
            menor_individuo = indv2;

        }else {
            ponto = random.nextInt(indv1.getSize());
            maior_individuo = indv1;    
            menor_individuo = indv2;
        }
        System.out.println("O valor de 'aux' retornado é:  "+ aux);    
        System.out.println("O ponto foi no índice:"+ ponto);
        System.out.println("INDIVIDUO 1 --> "+indv1.getListaItens());
        System.out.println("INDIVIDUO 2 --> "+indv2.getListaItens());
        System.out.println("MAIOR INDIVIDUO --> "+maior_individuo.getListaItens());
        System.out.println("MENOR INDIVIDUO --> "+menor_individuo.getListaItens());

        if(ponto > (menor_individuo.getListaItens().size() - 1)){

            System.out.println("\n\nPonto maior que o tamanho do menor indivíduo\n");
            //Cria Filho1
            list1.addAll(maior_individuo.getListaItens().subList(0, ponto));
            //list2.addAll(menor_individuo.getListaItens().subList(ponto, maior_individuo.getSize()));

            System.out.println("LIST 1 --> "+list1);
            System.out.println("LIST 2 --> "+list2);
            System.out.println("Vou realizar a primeira combinação do Filho 1");

            filho1.setListaItens(list1); 
            filho1.setListaItens2(list2);

            //Calcular Somatório de itens
            filho1.setSomatorioItens2(filho1.calculaSomatorioItens());

            //Calcular o fitness
            filho1.setFitness();

            //Verifica se a capacida está sendo respeitada
            filho1.setCapacidadeRespeitada(filho1.calculaCapacidadePlaca());

            filhos.setFilho1(filho1);

            System.out.println("FILHO 1--> "+filho1.getListaItens());

            System.out.println("Vou realizar....");
            list1.removeAll(list1);
            list2.removeAll(list2);               

            //Cria Filho2
            list1.addAll(menor_individuo.getListaItens().subList(0, menor_individuo.getSize()));
            list2.addAll(maior_individuo.getListaItens().subList(ponto, maior_individuo.getSize()));

            System.out.println("LIST 1 --> "+list1);
            System.out.println("LIST 2 --> "+list2);
            System.out.println("Vou realizar a primeira combinação do Filho 2");

            filho2.setListaItens(list1); 
            filho2.setListaItens2(list2); 

            //Calcular Somatório de itens
            filho2.setSomatorioItens2(filho2.calculaSomatorioItens());

            //Calcular o fitness
            filho2.setFitness();

            //Verifica se a capacida está sendo respeitada
            filho2.setCapacidadeRespeitada(filho2.calculaCapacidadePlaca());

            filhos.setFilho2(filho2);

            System.out.println("FILHO 2--> "+filho2.getListaItens());

        }
        else if(ponto <= (menor_individuo.getListaItens().size()-1)){

            System.out.println("\n\nPonto menor ou igual ao tamanho do menor individuo\n");
            //Cria Filho1
            list1.addAll(maior_individuo.getListaItens().subList(0, ponto+1));
            list2.addAll(menor_individuo.getListaItens().subList(ponto+1, menor_individuo.getSize()));

            System.out.println("LIST 1 --> "+list1);
            System.out.println("LIST 2 --> "+list2);
            System.out.println("Vou realizar a primeira combinação do Filho 1");

            filho1.setListaItens(list1); 
            filho1.setListaItens2(list2);

            //Calcular Somatório de itens
            filho1.setSomatorioItens2(filho1.calculaSomatorioItens());

            //Calcular o fitness
            filho1.setFitness();

            //Verifica se a capacida está sendo respeitada
            filho1.setCapacidadeRespeitada(filho1.calculaCapacidadePlaca());

            filhos.setFilho1(filho1);

            System.out.println("FILHO 1--> "+filho1.getListaItens());

            //Cria Filho2

            System.out.println("Vou realizar....");
            list1.removeAll(list1);
            list2.removeAll(list2);

            list1.addAll(menor_individuo.getListaItens().subList(0, ponto+1));
            list2.addAll(maior_individuo.getListaItens().subList(ponto+1, maior_individuo.getSize()));

            System.out.println("LIST 1 --> "+list1);
            System.out.println("LIST 2 --> "+list2);
            System.out.println("Vou realizar a primeira combinação do Filho 2");

            filho2.setListaItens(list1); 
            filho2.setListaItens2(list2); 

            //Calcular Somatório de itens
            filho2.setSomatorioItens2(filho2.calculaSomatorioItens());

            //Calcular o fitness
            filho2.setFitness();

            //Verifica se a capacida está sendo respeitada
            filho2.setCapacidadeRespeitada(filho2.calculaCapacidadePlaca());

            filhos.setFilho2(filho2);

            System.out.println("FILHO 2--> "+filho2.getListaItens());
        }	

        return new Filhos(filhos);
    }

    public static Filhos operadorCrossoverPonto1(Individuo indv1, Individuo indv2){

        int ponto;
        int aux = 0;

        Filhos filhos = new Filhos();
        Random random = new Random();
        Individuo filho1 = new Individuo();
        Individuo filho2 = new Individuo();
        List<Integer> list1 = new ArrayList<Integer>();
        List<Integer> list2 = new ArrayList<Integer>();

        ComparadorIndividuos compara = new ComparadorIndividuos();
        aux = compara.compare(aux, indv1, indv2);

        if(aux == -1){

                ponto = random.nextInt(indv1.getSize());
        }else if(aux == 1){

                ponto = random.nextInt(indv2.getSize());
        }else 
                ponto = random.nextInt(indv1.getSize());

        System.out.println("INDIVIDUO 1 --> "+indv1.getListaItens());
        System.out.println("INDIVIDUO 2 --> "+indv2.getListaItens());

        System.out.println("O ponto foi no índice:"+ ponto);

        list1.addAll(indv1.getListaItens().subList(0, ponto+1));//Talvez tirar o "+1" !!!
        list2.addAll(indv2.getListaItens().subList(ponto + 1, indv2.getSize()));

        System.out.println("LIST 1 --> "+list1);
        System.out.println("LIST 2 --> "+list2);
        System.out.println("Vou realizar a primeira combinação do Filho 1");

        filho1.setListaItens(list1); 
        filho1.setListaItens2(list2);

        //Calcular Somatório de itens
        filho1.setSomatorioItens2(filho1.calculaSomatorioItens());

        //Calcular o fitness
        filho1.setFitness();

        //Verifica se a capacida está sendo respeitada
        filho1.setCapacidadeRespeitada(filho1.calculaCapacidadePlaca());

        filhos.setFilho1(filho1);

        System.out.println("FILHO 1--> "+filho1.getListaItens());

        System.out.println("Vou realizar....");
        list1.removeAll(list1);
        list2.removeAll(list2);

        list1.addAll(indv2.getListaItens().subList(0, ponto + 1));//Talvez tirar o "+1" !!!
        list2.addAll(indv1.getListaItens().subList(ponto + 1, indv1.getSize()));

        System.out.println("LIST 1 --> "+list1);
        System.out.println("LIST 2 --> "+list2);
        System.out.println("Vou realizar a primeira combinação do Filho 2");

        filho2.setListaItens(list1); 
        filho2.setListaItens2(list2); 

        //Calcular Somatório de itens
        filho2.setSomatorioItens2(filho2.calculaSomatorioItens());

        //Calcular o fitness
        filho2.setFitness();

        //Verifica se a capacida está sendo respeitada
        filho2.setCapacidadeRespeitada(filho2.calculaCapacidadePlaca());

        filhos.setFilho2(filho2);

        System.out.println("FILHO 2--> "+filho2.getListaItens());

        //return filhos;
        return new Filhos(filhos);
    }

    public static Filhos operadorCrossoverPonto2(Individuo indv1, Individuo indv2){

        int aux = 0;
        int ponto1, ponto2, pnt1, pnt2;

        Filhos filhos = new Filhos();
        Random random = new Random();
        Individuo filho1 = new Individuo();
        Individuo filho2 = new Individuo();

        ComparadorIndividuos compara = new ComparadorIndividuos();
        aux = compara.compare(aux, indv1, indv2);

        if(aux == -1){

                ponto1 = random.nextInt(indv1.getSize());
                ponto2 = random.nextInt(indv1.getSize());

                while((ponto2 == ponto1) && (indv1.getSize() > 1 && indv2.getSize() > 1)){
                        ponto2 = random.nextInt(indv1.getSize());
                }

        }
        else if(aux == 1){

                ponto1 = random.nextInt(indv2.getSize());
                ponto2 = random.nextInt(indv2.getSize());

                while((ponto2 == ponto1) && (indv1.getSize() > 1 && indv2.getSize() > 1)){
                        ponto2 = random.nextInt(indv2.getSize());
                }

        }else {

                ponto1 = random.nextInt(indv1.getSize());
                ponto2 = random.nextInt(indv1.getSize());

                while(ponto2 == ponto1){
                        ponto2 = random.nextInt(indv1.getSize());
                }
        }

        if(ponto1 > ponto2){
                pnt1 = ponto2;
                pnt2 = ponto1;
        }else{
                pnt1 = ponto1;
                pnt2 = ponto2;
        }

        System.out.println(" O ponto 1 foi no índice:"+ pnt1);
        System.out.println(" O ponto 2 foi no índice:"+ pnt2);

        List<Integer> obj1 = new ArrayList<Integer>();
        List<Integer> list1 = new ArrayList<Integer>();
        List<Integer> list2 = new ArrayList<Integer>();

        System.out.println("Vou realizar a primeira combinação do Filho 1");

        System.out.println("INDIVIDUO 1 --> "+indv1.getListaItens());
        System.out.println("INDIVIDUO 2 --> "+indv2.getListaItens());

        obj1.addAll(indv1.getListaItens().subList(0, pnt1));
        System.out.println("OBJ1 -->"+obj1);

        list1.addAll(indv2.getListaItens().subList(pnt1, pnt2 + 1));
        System.out.println("LIST1 -->"+list1);

        list2.addAll(indv1.getListaItens().subList(pnt2 + 1, indv1.getSize()));
        System.out.println("LIST2 -->"+list2);

        filho1.setListaItens(obj1);  
        filho1.setListaItens2(list1);
        filho1.setListaItens2(list2);

        //Calcular Somatório de itens
        filho1.setSomatorioItens2(filho1.calculaSomatorioItens());

        //Calcular o fitness
        filho1.setFitness();

        //Verifica se a capacida está sendo respeitada
        filho1.setCapacidadeRespeitada(filho1.calculaCapacidadePlaca());

        System.out.println("FILHO 1 -->"+filho1.getListaItens());

        filhos.setFilho1(filho1);

        System.out.println("FILHO 1 >>>> "+filhos.getFilho1().getListaItens());


        System.out.println("Vou realizar a primeira combinação do Filho 2");

        System.out.println("INDIVIDUO 1 --> "+indv1.getListaItens());
        System.out.println("INDIVIDUO 2 --> "+indv2.getListaItens());

        obj1.removeAll(obj1);
        list1.removeAll(list1);
        list2.removeAll(list2);

        obj1.addAll(indv2.getListaItens().subList(0, pnt1));
        System.out.println("OBJ1 -->"+obj1);

        list1.addAll(indv1.getListaItens().subList(pnt1,  pnt2 + 1));
        System.out.println("LIST1 -->"+list1);

        list2.addAll(indv2.getListaItens().subList(pnt2 + 1, indv2.getSize()));
        System.out.println("LIST2 -->"+list2);

        filho2.setListaItens(obj1); 
        filho2.setListaItens2(list1); 
        filho2.setListaItens2(list2);

        //Calcular Somatório de itens
        filho2.setSomatorioItens2(filho2.calculaSomatorioItens());

        //Calcular o fitness
        filho2.setFitness();

        //Verifica se a capacida está sendo respeitada
        filho2.setCapacidadeRespeitada(filho2.calculaCapacidadePlaca());

        System.out.println("FILHO 2 -->"+filho2.getListaItens());

        filhos.setFilho2(filho2);

        System.out.println("FILHO 2 >>>> "+filhos.getFilho2().getListaItens());

        return new Filhos(filhos);
    }

    /*Aqui serão implementados os 2 tipos de mutação:
                    * Troca de dois itens (operadorMutacao1)
                    * Realocação de um item (operadorMutacao2)	 
    */
    public void operadorMutacao(Individuo indv1, Individuo indv2){

        //OBSERVAÇÃO -> Não está implementado ainda e creio que nçao será mais necessário !!!;
        double area_it1, area_it2;
        int id1, id2, aux1, aux2;
        Random r1 = new Random();
        Random r2 = new Random();

        //Cria de forma Aleatória

        id1 = r1.nextInt(indv1.getSize());
        id2 = r2.nextInt(indv2.getSize());

        aux1 = indv1.getListaItens().get(id1);
        aux2 = indv2.getListaItens().get(id2);

        area_it1 = Funcoes.getVetor_info()[aux1].retornaArea();
        area_it2 = Funcoes.getVetor_info()[aux2].retornaArea();

        indv1.removeItemLista(id1, area_it1);
        indv2.removeItemLista(id2, area_it2);

        indv1.setListaItens(aux2);
        indv1.setSomatorioItens(Funcoes.getVetor_info()[aux2].retornaArea());

        indv2.setListaItens(aux1);
        indv1.setSomatorioItens(Funcoes.getVetor_info()[aux1].retornaArea());

        // Deve-se verificar a restricao de capacidade dos Individuos  !!!!!!!!!!!

        // Deve-se verificar a restricao de Restricao de Insercao na Memória !!!!!!!!!!!!!!!!				
    }		
}