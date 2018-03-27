package HHDInternal;

import HHDInterfaces.IBin;

import HHDInterfaces.IPeca;
import HHDInterfaces.ISobra;
import HHDInterfaces.IGroup;
import java.awt.Graphics;
import HHDInterfaces.IPedaco;
import java.util.Iterator;
import java.awt.Dimension;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.ListIterator;
import HHDInterfaces.IDimensao2d;
import HHDInterfaces.ITreeObserver;
import HHDInterfaces.IVisualization;
import HHDInterfaces.IGenericSolution;
import HHDInterfaces.ISolutionProvider;
import HHDInterfaces.ISolutionInterface;
import HHDInterfaces.IPecaIPedacoWrapper;
import HHD_Exception.InvalidVisualizationIdentifierException;
import HeuristicaConstrutivaInicial.Bin;

public class Solucao implements ISolutionInterface{

    private IGenericSolution solucaoGenerica;
    private Hashtable visualizations;
    private LinkedList listaObservers;
    private String defaultVisualization;

    //primeira implementacao OK
    //sem testes
    public Solucao(IGenericSolution givenSolution){

        solucaoGenerica = givenSolution;
        listaObservers = new LinkedList();

        //Criando dicionario de visualizacoes
        //Todas as novas visualizacoes devem ser incluidas aqui

        visualizations = new Hashtable();
        //visualizations.put("arvore Binaria de Corte", new BinaryCuttingTreeVisualization(solucaoGenerica));
        //visualizations.put("Layout de Corte", new CutLayoutVisualization(givenSolution));

        defaultVisualization = "Layout de Corte";
        solucaoGenerica.attach((ITreeObserver) visualizations.get("arvore Binaria de Corte"));
    }
	
	
    /*public Solucao(SolucaoHeuristica solucao) {
            solucaoEncontrada = solucao;
    }*/

    //primeira implementacao OK
    //sem testes	
    @Override
    public int getQtd(){
        
            return solucaoGenerica.getQtd();
    }

    //primeira implementacao OK
    //sem testes
    private float calculeFator(Dimension d){
        
        IDimensao2d tamChapa = solucaoGenerica.getTamanhoChapa();

        float a = (d.height - 1) / tamChapa.retorneAltura();
        float b = (d.width - 1) / tamChapa.retorneBase();

        if ( a < b )
                return a;
        
        return b;	
        
    }

    //primeira implementacao OK
    //sem testes
    @Override
    public IDimensao2d getTamanhoChapa() {
        
        return solucaoGenerica.getTamanhoChapa();
    }

    //primeira implementacao OK
    //sem testes
    @Override
    public float getFator(Dimension tamanhoGrafico) {

        return this.calculeFator(tamanhoGrafico);
    }

    //primeira implementacao OK
    //sem testes
    //comecei a implementar estes metodos nas classes da arvore.
    //Alguns problemas encontrados: pode ser necessario fazer "subclasses de IBPTNode
    //para tratar casos especificos de "peca", "sobra", "lista"...
    @Override
    public boolean remova(IPedaco pedaco){
        
        if(pedaco == null)
                return false;
        boolean resultado = solucaoGenerica.retornePlanoDeCorte(pedaco.retorneIndiceBin()).remova(pedaco); 
        if(resultado)
        {
                solucaoGenerica.aglutineSobras(pedaco.retorneIndiceBin());
                solucaoGenerica.sendUpdateSignal(pedaco.retorneIndiceBin());
        }

        return resultado; 
    }

    //primeira implementacao OK
    //sem testes
    @Override
    public boolean remova(Collection listaPedacos){
        
        Iterator iterador = listaPedacos.iterator();

        while (iterador.hasNext())
                if (!remova((IPedaco) iterador.next()))
                        return false;

        return true;
    }

    //primeira implementacao OK
    //NO ENTANTO, lembrar de:
        // Colocar os membros de "ListaSobraMarcadas" implementando interface ISobra
        // Alias, qualquer refer�ncia a "sobras" (antes IPedacoDisponivel) deve agora
        // implementar a interface ISobra.
          // 10/10/2004 - Aparentemente, isto ja esta pronto.
    @Override
    public void marqueSobrasMaiores(IDimensao2d d, int indice){
        
        int cabe_return = -1;
        
        IBin planoDeCorte = solucaoGenerica.retornePlanoDeCorte(indice);
        ListIterator listaSobras = planoDeCorte.getListaSobras().listIterator();
        planoDeCorte.inicializeListaSobrasMarcadas();

        while(listaSobras.hasNext()){
            
                // ALTERAcaO: Modificar (PedacoDisponivel) para (ISobra) (10/10/2004 OK, FEITO)
                // LEMBRETE: Definir ISOBRA, IPEcA e ICORTE, de forma que sejam suficientes para que
                // esta classe possa trabalhar com qualquer estrutura de dados subjacente.
                ISobra pedacoAtual = (ISobra) listaSobras.next();
                
                cabe_return = pedacoAtual.cabePeca(d, false);

                if(cabe_return == 0)
                    planoDeCorte.marqueSobra(pedacoAtual);
        }

//		solucaoGenerica.sendUpdateSignal(indice);


    }
	
//	public void sendUpdateSignal(int indice)
//	{
//		for(int i = 0; i < listaObservers.size(); i++)
//			((ITreeObserver)listaObservers.get(i)).update(indice);
//	}
//	
//	public void sendUpdateSignal()
//	{
//		for(int i = 0; i < listaObservers.size(); i++)
//			((ITreeObserver)listaObservers.get(i)).update();
//	}

   // primeira implementacao OK
    @Override
	public void desmarqueSobras(int indice) 
	{
		IBin planoDeCorte = solucaoGenerica.retornePlanoDeCorte(indice);
		planoDeCorte.inicializeListaSobrasMarcadas();
		
//		solucaoGenerica.sendUpdateSignal(indice);
	}

	// primeira implementacao OK
	// No entanto, foram adicionados muitos metodos em outras classes que devem ser implementados URGENTEMENTE
	
	//ATENcaO --- ESTE MeTODO DEVE SER PRECEDIDO DE UM "getSobra" na visualizacao, para que possa ser 
	//enviado o ISobra apropriado
//	public boolean mova(IPedaco pedaco, ISobra areaDestino, int indiceBin) 
//	{
//		if(!pedaco.temPeca())
//			return false;
//		
//		ITesteBin planoDeCorte = solucaoGenerica.retornePlanoDeCorte(indiceBin);
//		
//		
//		IPeca pecaTransferida = ((IPecaIPedacoWrapper) pedaco).getPeca();
//		
//		if(areaDestino != null && areaDestino.cabePeca(pecaTransferida.getDimensoes(), pecaTransferida.permiteRotacao()))
//		{
//			ISolutionProvider heuristica = SolutionProviderFactory.getInstance().newSolutionProvider();
//
//			planoDeCorte.remova(pedaco);
//			planoDeCorte.aglutineSobras();
//			LinkedList listaPecas = new LinkedList(), listaSobras = new LinkedList();
//			listaPecas.add(pecaTransferida.getIPecaIPedidoWrapper());
//			listaSobras.add(areaDestino);
//			heuristica.solucaoParcial(listaPecas, listaSobras, solucaoGenerica.getTamanhoChapa());
//				
//			solucaoGenerica.sendUpdateSignal(indiceBin);
//			solucaoGenerica.sendUpdateSignal(areaDestino.retorneindiceBin());
//			
//			return true;
//		}
//		
//		return false;
//	}
	
	// implementacao OK
    @Override
    public boolean selecione(IPedaco pedaco){
        
        if(pedaco == null)
                return false;

        boolean resultado = solucaoGenerica.retornePlanoDeCorte(pedaco.retorneIndiceBin()).selecione(pedaco);

//		if(resultado)
//			solucaoGenerica.sendUpdateSignal(pedaco.retorneindiceBin());

        return resultado;
    }

	// implementacao OK
    @Override
    public boolean selecioneApenas(IPedaco pedaco){
        
        if(pedaco == null)
                return false;

        if(pedaco.estaRemovido() == true)
                return false;

        for (int i = 0; i < this.getQtd(); i++)
                if(i != pedaco.retorneIndiceBin())
                        this.descelecioneTodas(i);

        boolean resultado = solucaoGenerica.retornePlanoDeCorte(pedaco.retorneIndiceBin()).selecioneApenas(pedaco);
//		solucaoGenerica.sendUpdateSignal(pedaco.retorneindiceBin());

        return resultado;
    }


	// implementacao OK
    @Override
    public Collection getSelecionadas(int indice){

            IBin planoDeCorte = solucaoGenerica.retornePlanoDeCorte(indice);
            LinkedList listaSelecionadas = planoDeCorte.getListaSelecionadas();
            ListIterator iterador = listaSelecionadas.listIterator();
            LinkedList listaRetorno = new LinkedList();
            while(iterador.hasNext())
                    listaRetorno.add(((IPeca) iterador.next()).getIPecaIPedacoWrapper());

            return listaRetorno;
    }

	/* (non-Javadoc)
	 * @see cuttingInterface.ISolutionInterface#descelecioneTodas(int)
	 */
    @Override
    public void descelecioneTodas(int indice){

            Iterator iteradorListaSelecionadas = solucaoGenerica.retornePlanoDeCorte(indice).getListaSelecionadas().iterator();

            while(iteradorListaSelecionadas.hasNext())
                ((IPeca)iteradorListaSelecionadas.next()).setSelected(false);

            if(solucaoGenerica.retornePlanoDeCorte(indice).getListaSelecionadas().size() > 0)
                    solucaoGenerica.retornePlanoDeCorte(indice).inicializaListaSelecionadas();

//		solucaoGenerica.sendUpdateSignal(indice);
    }

    /* (non-Javadoc)
     * @see cuttingInterface.ISolutionInterface#eUmPedacoSelecionado(java.awt.Point, java.awt.Dimension, int)
     */
    // PRECISA ARRANJAR UMA FORMA DE PERMITIR QUE O MARCELO CONSIGA CLICAR
    // EM UM PONTO E RECEBER UM "ISobra"
    // e EVIDENTE QUE ESSE TIPO DE FUNcaO DEVE SER IMPLEMENTA EM VISUALIZATION
    public boolean eUmPedacoSelecionado(ISobra pedaco, int indice){

            IBin planoDeCorte = solucaoGenerica.retornePlanoDeCorte(indice);

            if(pedaco == null)
                    return false;

            if(planoDeCorte.getListaSobrasMarcadas().contains(pedaco))
                    return true;

            return false;
    }

    /* (non-Javadoc)
     * @see cuttingInterface.ISolutionInterface#clone()
     */
    @Override
    public Object clone() {
            // TODO Auto-generated method stub
            return this;
    }

    //implementacao OK
    @Override
    public IVisualization getVisualization(String visIdentifier) throws InvalidVisualizationIdentifierException
    {
            Object visualizacao = visualizations.get(visIdentifier);
            if (visualizacao == null)
                    throw new InvalidVisualizationIdentifierException("Identificador de visualizacao invalido/visualizacao nao implementada");

            if(IVisualization.class.isInstance(visualizacao))
                    return (IVisualization) visualizacao;
            else throw new ClassCastException("Objeto no dicionario de visualizacoes nao implementa IVisualization");
    }

    @Override
    public Collection getVisualizationList(){
        
        LinkedList lista = new LinkedList();

        Enumeration keys = visualizations.keys();

        while(keys.hasMoreElements())
                lista.add(keys.nextElement());

        return lista;
    }
	
    @Override
    public void solucioneNaoAtendidas(String algoritmoDeCorte){
        
        solucaoParcial(SolutionProviderFactory.getInstance().newSolutionProvider(algoritmoDeCorte));
        solucaoGenerica.sendUpdateSignal();
    }

    @Override
    public void solucioneNaoAtendidas(){
        
        solucaoParcial(SolutionProviderFactory.getInstance().newSolutionProvider());
        solucaoGenerica.sendUpdateSignal();
    }
	
    private void solucaoParcial(ISolutionProvider provedorDeSolucoes){
     
        LinkedList listaISobras = new LinkedList();
        LinkedList listaPecas = new LinkedList();


        for(int i = 0; i < solucaoGenerica.getQtd(); i++)
        {
                IBin binAtual = solucaoGenerica.retornePlanoDeCorte(i);
                listaISobras.addAll(binAtual.getListaSobras());
                ListIterator iteradorPecas = binAtual.getPecasRetiradas().listIterator();

                while(iteradorPecas.hasNext())
                        listaPecas.add(((IPeca) iteradorPecas.next()).getIPecaIPedidoWrapper());

                binAtual.reiniciaListaPecasRetiradas(); //para desligar as pecas do bin
        }
        if(listaPecas.size() == 0)
        {
                System.err.println("Lista pecas enviadas para execucao parcial e vazia");
                return;
        }

        provedorDeSolucoes.solucaoParcial(listaPecas, listaISobras, solucaoGenerica.getTamanhoChapa());
    }

	/*
	 * Pre-condicao:
	 * @param listaPecas e uma lista de pedacos valida (obtidos atraves de sucessivos "getPedaco()")
	 * 
	 * P�s-condicao:
	 * A estrutura de dados agrupou as pecas no menor pedaco da chapa nos quais estas cabiam 
	 * */
    @Override
    public boolean agrupePecas(LinkedList listaPecas, int indice)
    {
            this.descelecioneTodas(indice);
            boolean resultado = ((solucaoGenerica.retornePlanoDeCorte(indice).agrupePecas(listaPecas)) != null); 
            solucaoGenerica.sendUpdateSignal(indice);
            return resultado;
    }

    /* TODO Pr�ximos passos: 
     * 
     * Separar BPTPedaco em N�Sobra e N�Peca  **facil** 
     * Fazer o seguinte: mover \o metodo "mova" para as visualizacoes, de forma que possamos
     * 						selecionar o "PedacoDisponivel" a partir do ponto de clique)
     * Incluir rotacao e estrategias em HHDHeuristic *menos importante - mas grande impacto*
     * Finalizar projeto *escrever documentacao e monografia - leva tempo. Comecar dia 17*
     * 
     * Implementar persist�ncia de objetos *complicado, mas menos importante* (pode deixar para depois do PFC)
    */

    @Override
    public boolean desagrupePecas(IPedaco pedaco){
        
        if(pedaco == null)
                return false;
        IPeca noEmpacotado = ((IPecaIPedacoWrapper)pedaco).getPeca();

        if(!IGroup.class.isInstance(noEmpacotado))
                return false;

        IGroup noGrupo = (IGroup) noEmpacotado;

        int indiceBin = noGrupo.retorneIndiceBin();

        if(indiceBin == -1)
                return false;

        boolean resultado = solucaoGenerica.retornePlanoDeCorte(noGrupo.retorneIndiceBin()).desagrupe(noGrupo) ;
        solucaoGenerica.sendUpdateSignal(indiceBin);
        return resultado;
    }


    /* (non-Javadoc)
     * @see cuttingInterface.ISolutionInterface#ePecaAgrupadora(cuttingInterface.IPedaco)
     */
    @Override
    public boolean ePecaAgrupadora(IPedaco pedaco)	{

        if(pedaco == null)
                return false;

        IPeca noEmpacotado = ((IPecaIPedacoWrapper)pedaco).getPeca();

        if(IGroup.class.isInstance(noEmpacotado))
                return true;

        return false;
    }

    /* (non-Javadoc)
     * @see cuttingInterface.ISolutionInterface#getVisualization()
     */
    
    @Override
    public IVisualization getVisualization(){
        
        try {
                
            return (IVisualization) getVisualization(this.defaultVisualization);
        } 
        catch (InvalidVisualizationIdentifierException e) {
                // TODO Auto-generated catch block
             e.printStackTrace();
        }
        
        return null;
    } 


    /* (non-Javadoc)
     * @see cuttingInterface.ISolutionInterface#getDesperdicio(int)
     */
    public float getDesperdicio(int indice){

        Bin bin = (Bin) solucaoGenerica.retornePlanoDeCorte(indice);
        
        return bin.getWaste();
    }

    /* (non-Javadoc)
     * @see cuttingInterface.ISolutionInterface#getQuantidadeSobras()
     */
    public int getQuantidadeSobras() {
            // TODO Auto-generated method stub
            return 0;
    }

    /* (non-Javadoc)
     * @see cuttingInterface.ISolutionInterface#getQuantidadePecas()
     */
    
    public int getQuantidadePecas() {
            // TODO Auto-generated method stub
            return 0;
    }

    /* (non-Javadoc)
     * @see cuttingInterface.ISolutionInterface#removaTodosPedacos(int)
     */
    public void removaTodosPedacos(int indice) {
            // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see cuttingInterface.ISolutionInterface#selecioneTodosPedacos(int)
     */
    public void selecioneTodosPedacos(int indice) {
            // TODO Auto-generated method stub
    }

    @Override
    public void paint(Graphics g, Dimension d, int indice) {
        
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean mova(IPedaco pedaco, ISobra areaDestino, int indiceBin) {
        
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
