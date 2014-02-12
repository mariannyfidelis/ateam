package j_HeuristicaArvoreNAria;

import java.util.Iterator;


public interface Position{

    //Métodos Gerais
	
    //public int size(); //retorna o nº de nós da árvore

    //public boolean isEmpty(); //indica se a árvore é vazia

    //public Iterator<No> elements(); //iterador para os elementos da árvore
    
    //public Iterator<No> positions(); //iterador para os nós da árvore

    //Métodos de Atualização
    
    //public Object replaceElement(No p, Object o); //substitui o elemento do nó retornando o
                                                    //elemento original
    //public void swapElements(No p, Object o);   //troca os elementos de dois nós

    //Métodos de Acesso

    //public TipoCorte root(); //retorna nó raiz

    public Position parent(No o);//retorna o pai de um nó;

    public Iterator<No> children(/*No p*/); //retorna um iterador para os filhos

    public boolean isInternal(No p); 

    public boolean isExternal(No p); 

    public boolean isRoot(No p); //testa se é um nó raiz
	
    //Métodos de Expansão

    //Definido de acordo com a aplicação da árvore; 

	    /*
	    --> testar por exemplo se é um H-node ou V-node
	    --> testar se é um Item;
	    --> calcular área de um padrão
	    --> calcular retângulo mínimo
	    --> calcular retângulo máximo
	    */	
}