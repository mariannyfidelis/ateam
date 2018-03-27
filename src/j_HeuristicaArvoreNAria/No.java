package j_HeuristicaArvoreNAria;

import Utilidades.Chapa;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;

public class No implements Position,Serializable{

    private No no_pai;
    private Item item;
    private TipoCorte tipo_corte;
    private LinkedList<No> filhos;
    private RetanguloMinimo retMinimo;
    private RetanguloMaximo retMaximo;
    private RetanguloSlack retSlack	;
    private float area;
    private float maxima_area_livre;

    public No(){

        setItem(new Item(0, 0, 0,false));
    }
	
    public No(boolean vert){

        if(vert == false){

            setTipo_corte(new TipoCorte(vert));
            setItem(null);
            filhos = new LinkedList<No>(); 	        
        }

        else if(vert == true){

            setTipo_corte(new TipoCorte(vert));
            setItem(null);
            filhos = new LinkedList<No>();
        }	
    }	

    public No(int id, float larg, float alt){

        setItem(new Item(id, larg, alt,false));
        setTipo_corte(new TipoCorte());
        filhos = new LinkedList<No>();	
    }

    public No parent(No elemento) {

        return elemento.getPai();
    }

    public Iterator<No> children(/*No pai*/) {

        Iterator<No> iterator = getFilhos().iterator();

        return iterator;
    }

    public LinkedList<No> children1(){

        return filhos;
    }

    public boolean isInternal(No elemento) {

        if(elemento.isExternal(elemento)){

                return false;
        } 
        return true;
    }

    public boolean isExternal(No elemento) {

        if(elemento.getItem() != null){

                return true;
        }
        else return false;
    }

    public boolean isRoot(No elemento) {

        if(elemento.getPai() == null){

                return true;
        }
        else 
                return false;
    }

    public No getPai(){

        return no_pai;
    }

    public void setPai(No pai){

        no_pai = pai;
    }

    public LinkedList<No> getFilhos(){

        return filhos;
    }

    public void setFilhos(No filho){

        filhos.add(filho);
    }

    public Item getItem() {

        return item;
    }

    public void setItem(Item item) {

        this.item = item;
    }

    public TipoCorte getTipo_corte() {

        return tipo_corte;
    }

    public void setTipo_corte(TipoCorte tipo_corte) {

        this.tipo_corte = tipo_corte;
    }

    public float getArea() {

        return area;
    }

    public void setArea(float area) {

        this.area = area;
    }	
	
    public RetanguloMinimo getRetanguloMinimo(){

        return retMinimo;
    }

    public void setRetanguloMinimo(RetanguloMinimo retMinimo){

        this.retMinimo = retMinimo;
    }

    public RetanguloMaximo getRetanguloMaximo(){

        return retMaximo;
    }	

    public void setRetanguloMaximo(RetanguloMaximo retMaximo){

        this.retMaximo = retMaximo;
    }

    public RetanguloSlack getRetanguloSlack(){

        return retSlack;
    }

    public void setRetanguloSlack(RetanguloSlack retSlack){

        this.retSlack = retSlack;
    }

    public void calcula_retangulo_minimo(No elemento, boolean rotacao){
            No no = null;
            TipoCorte tipo;
            Iterator<No> iterator;

            RetanguloMinimo retmi = new RetanguloMinimo(elemento);

            if(elemento.getItem() == null){
                    tipo = elemento.getTipo_corte();

                    if( tipo.getTipoCorte().equals("vertical")){

                        System.out.println("\nVou calcular o retângulo Mínimo de um nó vertical\n");

                        iterator = elemento.children(/*elemento*/); 
                            float maior_altura = 0;
                            float largura,maior_largura = 0;

                            while(iterator.hasNext()){
                                    no = iterator.next();

                               largura = no.getRetanguloMinimo().getW_p();

                               if(largura > maior_largura){
                                       maior_largura = largura;
                               }

                               maior_altura = maior_altura + no.getRetanguloMinimo().getH_p();//Aqui representa o somatório da altura;		
                            }

                            retmi.setH_p(maior_altura);
                            retmi.setW_p(maior_largura);

                            elemento.setRetanguloMinimo(retmi);

                            System.out.println("Ret. Mínimo --->  Largura: "+elemento.getRetanguloMinimo().getW_p()+"  Altura: "+elemento.getRetanguloMinimo().getH_p()+"\n");
                    }

                    if( tipo.getTipoCorte().equals("horizontal")){

                            System.out.println("\nVou calcular o retângulo Mínimo de um nó horizontal\n");
                            iterator = elemento.children(/*elemento*/); 

                            float maior_largura = 0;
                            float altura,maior_altura = 0;

                            while(iterator.hasNext()){
                                    no = iterator.next();

                                    altura = no.getRetanguloMinimo().getH_p();

                                    if(altura > maior_altura){
                                               maior_altura = altura;
                                    }

                                    maior_largura = maior_largura + no.getRetanguloMinimo().getW_p();
                            }

                            retmi.setW_p(maior_largura);
                            retmi.setH_p(maior_altura);

                            elemento.setRetanguloMinimo(retmi);

                            System.out.println("Ret. Mínimo --->  Largura: "+elemento.getRetanguloMinimo().getW_p()+"  "
                                                                        + " Altura: "+elemento.getRetanguloMinimo().getH_p()+"\n");
                    }
            }
            else{
                    Item item_ = elemento.getItem();
                    System.out.println("\nVou calcular o retângulo Mínimo de um nó item");

                            retmi.setH_p(item_.getAltura());
                            retmi.setW_p(item_.getLargura());

                            elemento.setRetanguloMinimo(retmi);

                            if(rotacao == true){

                                    retmi.setH_p(item_.getLargura());
                                    retmi.setW_p(item_.getAltura());

                                    elemento.setRetanguloMinimo(retmi);
                            }

                            System.out.println("Ret. Mínimo --->  Largura: "+elemento.getRetanguloMinimo().getW_p()+"  Altura: "+elemento.getRetanguloMinimo().getH_p()+"\n");
            }
    }
	
    public void calcula_retangulo_maximo(No elemento, boolean rotacao){

            No pai;
            RetanguloMaximo retMax = new RetanguloMaximo(elemento);

            pai = elemento.getPai();

                    if(pai == null){
                        System.out.println("\nVou calcular o retângulo Máximo de um nó raiz");

                            retMax.setW_P((float)Chapa.getLargura());
                            retMax.setH_P((float)Chapa.getAltura());

                            elemento.setRetanguloMaximo(retMax);

                            System.out.println("Ret. Máximo --->  Largura: "+elemento.getRetanguloMaximo().getW_P()+"  Altura: "+elemento.getRetanguloMaximo().getH_P()+"\n");
                    }
                    else{

                        if(pai.getTipo_corte().getTipoCort() == No_orientacao.horizontal){

                            System.out.println("\nVou calcular o retângulo Máximo de um nó NÃO RAIZ E DE PAI Horizontal\n");

                                    retMax.setW_P(elemento.getRetanguloMinimo().getW_p()+ pai.getRetanguloSlack().getW_s_p());
                                    retMax.setH_P(pai.getRetanguloMaximo().getH_P());

                                    elemento.setRetanguloMaximo(retMax);

                                    System.out.println("Ret. Máximo --->  Largura: "+elemento.getRetanguloMaximo().getW_P()+"  Altura: "+elemento.getRetanguloMaximo().getH_P()+"\n");
                            }
                            if(pai.getTipo_corte().getTipoCort() == No_orientacao.vertical){

                                    System.out.println("\nVou calcular o retângulo Máximo de um nó NÃO RAIZ E DE PAI Vertical\n");

                                    retMax.setW_P(pai.getRetanguloMaximo().getW_P());
                                    retMax.setH_P(elemento.getRetanguloMinimo().getH_p() + pai.getRetanguloSlack().getH_s_p());

                                    elemento.setRetanguloMaximo(retMax);

                                    System.out.println("Ret. Máximo --->  Largura: "+elemento.getRetanguloMaximo().getW_P()+"  Altura: "+elemento.getRetanguloMaximo().getH_P()+"\n");
                            }
                    }//fim-else
    }

    public void calcula_slack(No elemento){

        RetanguloSlack retSlack2 = new RetanguloSlack();

        System.out.println("\nVou calcular o retângulo Slack de um nó\n");

        retSlack2.setW_s_p(elemento.getRetanguloMaximo().getW_P() - elemento.getRetanguloMinimo().getW_p());
        retSlack2.setH_s_p(elemento.getRetanguloMaximo().getH_P() - elemento.getRetanguloMinimo().getH_p());

        elemento.setRetanguloSlack(retSlack2);

        System.out.println("Ret. Slack --->  Largura: "+elemento.getRetanguloSlack().getW_s_p()+"  Altura: "+elemento.getRetanguloSlack().getH_s_p()+"\n");
    }

    public static float min(No elemento, float hi){

        float min = 0, aux_min;
        Iterator<No> iterator = elemento.children();

        while(iterator.hasNext()){
            No no = iterator.next();


            aux_min = Math.abs(no.getRetanguloMinimo().getH_p() - hi);

            if(min == 0) min = aux_min;

            if(aux_min <= min){

                min = aux_min;
            }
        }

        return min;
    }
	
    public static float min2(No elemento, float wi){

        float min = 0, aux_min;
        Iterator<No> iterator = elemento.children();
        No.imprimeNo(elemento);

        while(iterator.hasNext()){

            No no = iterator.next();
            No.imprimeNo(no);
            aux_min = Math.abs(no.getRetanguloMinimo().getW_p() - wi);

            if(min == 0) min = aux_min;

            if(aux_min <= min){

                min = aux_min;
            }
        }

        return min;
    }
	
    public float calcula_area_padrao(No elemento){

        if(elemento.getItem() != null){

            Item item = elemento.getItem();
            float area = (item.getAltura() * item.getLargura());

            elemento.setArea(area);

            return area;
        }
        else{

            Iterator<No> iterator = elemento.children(/*elemento*/);
            float somatorio_area = 0;
            No no;

            while(iterator.hasNext()){
                    no = iterator.next();

                    somatorio_area = somatorio_area + no.getArea();
            }

            elemento.setArea(somatorio_area);

            return somatorio_area;
        }
    }

    public float maior_area_livre(No no){

        float maxima_area_livre = no.getRetanguloMaximo().getAreaRetMaximo() - no.calcula_area_padrao(no);

        //no.setMaxima_area_livre(maxima_area_livre);

        return maxima_area_livre;
    }

    public float fitness(No elemento){

        float fitness_insercao;

        if(elemento.getTipo_corte() == null){	

                fitness_insercao = (float) 1/((elemento.retSlack.getW_s_p() + 1) * (elemento.retSlack.getH_s_p() + 1) *
                                            Math.abs(elemento.getItem().getAltura() - elemento.retMinimo.getH_p()) + 1);

                return fitness_insercao;
        }

        return 0;
    }
	
    public float fitness(float w_,float h_, float n_){

        float fitness = 0;

        fitness =  1 / (( w_+1)*(h_+1)*(n_+1));

        return fitness;
    }

    public float getMaxima_area_livre() {

        return maxima_area_livre;
    }

    public void setMaxima_area_livre(float maxima_area_livre) {

        this.maxima_area_livre = maxima_area_livre;

        System.out.println("Máxima área livre: "+ this.maxima_area_livre);
    }
	
    public static void imprimeNo(No elemento){

        if(elemento.getItem() != null){
                System.out.println("MaximaAreaLivre --> " +elemento.getMaxima_area_livre());
                System.out.println("NO ITEM --> \tLargura: "+elemento.getItem().getLargura()+" Altura: "+elemento.getItem().getAltura());
        }
        else{
                System.out.println("MaximaAreaLivre --> " +elemento.getMaxima_area_livre());
                System.out.println("NO TIPO CORTE --> "+elemento.getTipo_corte().getTipoCorte());
        }

    }
	
    public No copia_objeto(No objeto2){

        No objeto1 = new No(false);

        objeto1.no_pai = objeto2.getPai();
        objeto1.item = objeto2.getItem();
        objeto1.tipo_corte = objeto2.getTipo_corte();
        objeto1.filhos = objeto2.getFilhos();
        objeto1.retMinimo = objeto2.getRetanguloMinimo();
        objeto1.retMaximo = objeto2.getRetanguloMaximo();
        objeto1.retSlack = objeto2.getRetanguloSlack()	;
        objeto1.area = objeto2.getArea();
        objeto1.maxima_area_livre = objeto2.getMaxima_area_livre();

        return objeto1;
    }
}