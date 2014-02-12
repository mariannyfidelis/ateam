package j_HeuristicaArvoreNAria;

import java.util.Iterator;

public interface Tree{

    public int size();

    public boolean isEmpty();

    //public <TipoCorte,Item> replaceElement(Position v, E e) throws InvalidPositionException;

	public No root() throws EmptyTreeException;

	public No no_pai(No v) throws InvalidPositionException;
   
	public Iterable <No> filhos(No v) throws InvalidPositionException;
    
	public boolean isInternal(No v) throws InvalidPositionException;
    
	public boolean isExternal(No v) throws InvalidPositionException;
    
	public boolean isRoot(No v) throws InvalidPositionException;
    
	public Iterator<No> iterador();
    
	public Iterable<No> positions();
	
}