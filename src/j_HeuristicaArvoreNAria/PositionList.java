package j_HeuristicaArvoreNAria;

/*
 * Falta verificar se essa interface será necessária, como a mesma deve ser utilizada e implemen-
 * tada e o que deve ser adicionado;
 * * 
 * */

public interface PositionList<T> extends Iterable<T>{

	public int size();
	public boolean isEmpty();
	
	public Position first();
	public Position last();
	public Position next(Position p) throws InvalidPositionException;
	public Position prev(Position p) throws InvalidPositionException;
	
	public void addFirst(T e);        //Passa como parametro a arvore (no raiz)
	public void addLast(T e);         //Passa a arvore e percorre ate o ultimo elemento
	public void addAfter(Position p, T e) throws InvalidPositionException;
	public void addBeforer(Position p, T e) throws InvalidPositionException;
	
	public T remove(Position p) throws InvalidPositionException;
	public T set(Position p) throws InvalidPositionException;
	public Iterable<Position> positions();
	
	
}
