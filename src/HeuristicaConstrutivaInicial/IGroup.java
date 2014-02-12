package HeuristicaConstrutivaInicial;

import java.util.LinkedList;


public interface IGroup extends IPeca {

	LinkedList getListaCortes();

	LinkedList getListaPecas();

	int retorneIndiceBin();
}
