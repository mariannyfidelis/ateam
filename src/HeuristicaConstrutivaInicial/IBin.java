package HeuristicaConstrutivaInicial;

import java.util.Collection;
import java.util.LinkedList;


public interface IBin {
    
	public abstract LinkedList getListaCortes();

	public abstract LinkedList getListaPecas();

	public abstract LinkedList getListaSobras();

	public abstract LinkedList getListaSelecionadas();

	public abstract void setId(int i);

	public abstract int getId();

	public abstract boolean selecione(IPedaco pedaco);

	public abstract boolean selecioneApenas(IPedaco pedaco);

	public abstract boolean remova(IPedaco pedaco);
	
	public abstract void inicializeListaSobrasMarcadas();

	public abstract Collection getListaSobrasMarcadas();

	public abstract void aglutineSobras();

	public abstract void inicializaListaSelecionadas();

	public abstract void marqueSobra(ISobra pedacoAtual);
	
	public abstract LinkedList getPecasRetiradas();

	public abstract void reiniciaListaPecasRetiradas();

	public abstract IPeca agrupePecas(LinkedList listaPecas);

	public abstract boolean desagrupe(IGroup noGrupo);

	public abstract void retireListaRemovidas(IPedaco pedaco);
}
