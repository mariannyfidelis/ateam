package HHDInternal;

import HHDInterfaces.IPecaPronta;
import HHDInterfaces.ISobra;
import HHDComparadores.*;
import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;

public class PlanoDeCorte implements Serializable{

	private ISobra sobraCortada;
	private LinkedList listaDeCortes;
	private LinkedList listaDePecas;
	private LinkedList listaDeSobras;
	
	public PlanoDeCorte(ISobra sobra) 
	{
		this.sobraCortada = sobra;
		listaDeCortes = new LinkedList();
		listaDePecas = new LinkedList();
		listaDeSobras = new LinkedList();
	}
	
	public ISobra getSobraCortada()
	{
		return sobraCortada;
	}

	public void adicioneCortes(LinkedList listaCortesAAdicionar) 
	{
		listaDeCortes.addAll(listaCortesAAdicionar);
		if(listaDeCortes.size() != 0)
			Collections.sort(listaDeCortes, (Corte)listaDeCortes.getFirst());
	}


	public void adicionePeca(IPecaPronta pecaAtual) 
	{
		listaDePecas.add(pecaAtual);
	}

	public void integrar(PlanoDeCorte binParcial) 
	{
		if(binParcial == null)
			return;
		
		if(!binParcial.listaDePecas.isEmpty())
		{
			listaDePecas.addAll(binParcial.listaDePecas);
		}
		if(!binParcial.listaDeCortes.isEmpty())
		{
			listaDeCortes.addAll(binParcial.listaDeCortes);
			Collections.sort(listaDeCortes, (Corte)listaDeCortes.getFirst());
		}
		if(!binParcial.listaDeSobras.isEmpty())
		{
			listaDeSobras.addAll(binParcial.listaDeSobras);
			Collections.sort(listaDeSobras, new ComparadorISobra());
		}
	}

	public void adicionarSobra(ISobra sobra) 
	{
		this.listaDeSobras.add(sobra);
	}

	/**
	 * @return
	 */
	public LinkedList getListaCortes() {
		return listaDeCortes;
	}

	/**
	 * @return
	 */
	public LinkedList getListaPecas() {
		return listaDePecas;
	}

}
