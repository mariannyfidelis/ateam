package HHDBinPackingTree;

import HHDInternal.Ponto;
import HHDInterfaces.ISobra;
import HHDInterfaces.IPedido;
import HHDInterfaces.IDimensao2d;
import HHDInterfaces.IPecaPronta;
import HHDInternal.AreaRetangular;
import HHDInternal.CutLayoutNodesPainter;
import HHDInterfaces.IPecaIPedacoWrapper;
import java.io.Serializable;

//Separá-la em dois tipos de "nós":
    
    //Um deles, implementara IPeca, o outro implementara ISobra...
    //isto permitirá que apenas metodos necessarios sejam mantidos em cada objeto
    //uma terceira classe especial sera criada para representar os "cortes"
    //inferidos a partir da estrutura da arvore.
    //Isso deve ser feito porque ha uma grande quantidade de informacoes armazenadas em uma Única classe.

public class BPTPedaco implements IBPTNode, ICuttable, IPecaPronta,Serializable{

	private Ponto position;
	private IDimensao2d tamanho;
	
        private BinPackTree Arvore;
	private BinPackTreeForest floresta;

	private float posicaoCorte;
	private boolean cortado;
	private boolean corteVertical;
	
	private IBPTNode Father;
	private IBPTNode LeftSon;
	private IBPTNode RigthSon;
	
	private short Kind;
	
	private IPedido pedidoAtendido;
	private boolean selected;
	private boolean rotacao;
	private boolean estaRemovido;
	
        private float aproveitamento;
        private float desperdicio;
        private float somatorioSobras;
        
        //Aqui poderia incluir o FAV e FAV2

        
	public BPTPedaco(IDimensao2d dimensoes, BinPackTree arvore){
	
		position = new Ponto(0,0);
		tamanho = dimensoes;
		Kind = IBPTNode.PEDACO;
		cortado = false;
		pedidoAtendido = null;
		LeftSon = null;
		RigthSon = null;
		Arvore = arvore;
		
		if(arvore != null)
			floresta = Arvore.getFloresta();		
	}
        
        public boolean atendeAlgumPedido(){
	
            return pedidoAtendido != null;
	}
	
        @Override
	public void setFloresta(BinPackTreeForest floresta){
	
            this.floresta = floresta;
            if(this.LeftSon != null)
            {
                this.LeftSon.setFloresta(floresta);
                this.RigthSon.setFloresta(floresta);
            }
        }
	
        public BPTPedaco(IDimensao2d dimensoes, BinPackTree arvore, Ponto posicao){

            position = posicao;
            tamanho = dimensoes;
            Kind = IBPTNode.PEDACO;
            cortado = false;
            pedidoAtendido = null;
            LeftSon = null;
            RigthSon = null;
            Arvore = arvore;

            if(arvore != null)
                floresta = arvore.getFloresta();

            estaRemovido = false;
        }

        @Override
        public float getWidth(){

            return tamanho.retorneBase();
        }
        @Override
        public IBPTNode getFather(){

            return Father;
        }

        @Override	
        public void setFather(IBPTNode father){

            Father = father;
        }

        @Override
        public float getHeight(){

            return tamanho.retorneAltura();
        }
        
        public float getArea(){
        
            return (getHeight() * getWidth());
        }
    
        @Override
        public short getKind(){

            return Kind;
        }
        @Override
        public IBPTNode getLeftSon(){

            return LeftSon;
        }
        
        public float getAproveitamento() {
                
            return aproveitamento;
        }

        public void setAproveitamento(float aproveitamento) {

            this.aproveitamento = aproveitamento;
        }

        public float getDesperdicio() {

            return desperdicio;
        }

        public void setDesperdicio(float desperdicio) {

            this.desperdicio = desperdicio;
        }

        public float getSomatorioSobras() {

            return somatorioSobras;
        }

        public void setSomatorioSobras(float somatorioSobras) {

            this.somatorioSobras = somatorioSobras;
        }
        
        @Override
        
        public Ponto getPosition(){

            return this.getPontoInferiorEsquerdo();
        }
        @Override
        public void changePositionTo(Ponto position){

            this.position = new Ponto(position.getX(), position.getY());
            if(this.getLeftSon() != null)
            {
                if(this.corteVertical){
                    this.getLeftSon().changePositionTo(position);
                    this.getRigthSon().changePositionTo(new Ponto(this.position.getX() + this.posicaoCorte, this.position.getY()));
                }
                else{
                    this.getLeftSon().changePositionTo(new Ponto(this.position.getX(), this.position.getY() + this.posicaoCorte));
                    this.getRigthSon().changePositionTo(position);
                }

            }
        }

        @Override
        public IBPTNode getRigthSon(){

            return RigthSon;
        }

        @Override        
        public void Paint() {}

        @Override
        public boolean corte(float pCorte, boolean vertical){

                if(!Disponivel())
                        return false;

                if(pCorte < 0)
                        return false;

                BPTPedaco filhoEsquerdo = null, filhoDireito = null;

                if(vertical){
                    
                        if(pCorte > this.tamanho.retorneBase())
                                return false;

                        //System.out.println("Ponto corte tree -> "+ pCorte+"\n");
                        filhoEsquerdo = new BPTPedaco(new AreaRetangular(this.getWidth() - pCorte, this.getHeight()), this.Arvore,
                                                                        new Ponto(this.position.getX() + pCorte, this.position.getY()));

                        
                        filhoDireito = new BPTPedaco(new AreaRetangular(pCorte, this.getHeight()), this.Arvore,
                                                                                 new Ponto(this.position.getX(), this.position.getY()));
                }
                else
                {
                        if(pCorte > this.tamanho.retorneAltura())
                                return false;

                        /*filhoEsquerdo = new BPTPedaco(new AreaRetangular(this.tamanho.retorneBase(),this.tamanho.retorneAltura() - pCorte),
                                                                            this.Arvore,
                                                                            new Ponto(this.position.getX(), this.position.getY() + pCorte));*/
                        
                        //teste
                        filhoEsquerdo = new BPTPedaco(new AreaRetangular(this.getWidth(),this.getHeight() - pCorte), this.Arvore,
                                                                            new Ponto(this.position.getX(), this.position.getY() + pCorte));

                        
                        /*filhoDireito = new BPTPedaco(new AreaRetangular(this.tamanho.retorneBase(), pCorte), this.Arvore,
                                                                          new Ponto(this.position.getX(), this.position.getY()));*/
                        //teste
                        filhoDireito = new BPTPedaco(new AreaRetangular(this.getWidth(), pCorte), this.Arvore,
                                                                          new Ponto(this.position.getX(), this.position.getY()));
                        

                }

                this.cortado = true;
                this.corteVertical = vertical;
                this.posicaoCorte = pCorte;

                filhoEsquerdo.Father = filhoDireito.Father = this;

                this.LeftSon = filhoEsquerdo;
                this.RigthSon = filhoDireito;

                return true;
        }


        private boolean Disponivel(){

            return (!cortado) && (pedidoAtendido == null);
        }

        public boolean destaquePeca(IPedido pedidoAtendido){

            if(Disponivel()){

                this.pedidoAtendido = pedidoAtendido;
                return true;
            }

            return false;
        }

        @Override
        public boolean pertenceArvore(BinPackTree tree){

            return tree == Arvore;
        }

        @Override        
        public boolean isCuttable(){

            return Disponivel();
        }

        @Override
        public boolean temPeca(){

            return pedidoAtendido != null;
        }

        @Override
        public boolean isSelected(){

            return this.selected;
        }

        public boolean setCortado(boolean b){
        
            this.cortado = b;
            
            return this.cortado;
        }
        @Override
        public void setSelected(boolean b){

            this.selected = b;
        }

        @Override
        public IDimensao2d getDimensoes(){

                return this.tamanho;
        }

        @Override
        public void setLeftson(IBPTNode pedacoDisponivel){

            this.LeftSon = pedacoDisponivel;
            if(pedacoDisponivel != null)
            {
                pedacoDisponivel.setFather(this);
                pedacoDisponivel.setArvore(this.Arvore);
            }
        }

        @Override
        public void setRightSon(IBPTNode pedacoDisponivel){

            this.RigthSon = pedacoDisponivel;
            if(pedacoDisponivel != null)
            {
                    pedacoDisponivel.setFather(this);
                    pedacoDisponivel.setArvore(this.Arvore);
            }
        }

        public IPedido getPedidoAtendido(){

            return this.pedidoAtendido;
        }

        // TODO Descobrir em relacao a rotacao URGENTE (possivelmente o c�digo devera ser modificado)
        @Override	
        public boolean permiteRotacao(){

            return false;
        }

        @Override        
        public ISobra remova(){

            estaRemovido = true;
            Ponto posicaoAntiga = this.position;
            this.changePositionTo(new Ponto(0,0));
            
            //Mudei dia 3 de junho as 20:21
            BPTPedaco bpt = new BPTPedaco(this.tamanho, Arvore, posicaoAntiga);
            bpt.setFather(this.Father);
            
            this.Father = null;
            
            return bpt;
        }

        @Override
        public IPecaIPedacoWrapper getIPecaIPedacoWrapper(){

            return new PecaArvoreIPedacoWrapper(this, retorneIndiceBin());
        }

        @Override
        //Ha metodos com mesmo significado aqui...
        public Ponto getPontoInfEsq(){

            return new Ponto(position.getX(), position.getY());
        }

        @Override
        public Ponto getPontoSupDir(){

            return new Ponto(this.getPosition().getX() + this.tamanho.retorneBase(), this.getPosition().getY() + this.tamanho.retorneAltura());
        }

        @Override
        public Ponto getPontoInferiorEsquerdo(){

            return this.getPontoInfEsq();
        }

        @Override
        public Ponto getPontoSuperiorDireito(){

            return this.getPontoSupDir();
        }

        @Override
        public int cabePeca(IDimensao2d maior, boolean permiteRotacao){
            
            int cabe_return = -1;
            boolean b_alt, b_larg;
            
            b_alt  = maior.retorneAltura() <= this.getDimensoes().retorneAltura();
            b_larg = maior.retorneBase() <= this.getDimensoes().retorneBase();	
            
            if(b_alt && b_larg){
            
                return 0;
            }
            
            if(permiteRotacao == true){
            
                b_alt  = maior.retorneBase() <= this.getDimensoes().retorneAltura();
                b_larg = maior.retorneAltura() <= this.getDimensoes().retorneBase();	
                
                if(b_alt && b_larg){
            
                    return 1;
                }
            
            }
            
            return  -1;

    //		boolean cabeSemRotacao = (maior.retorneAltura() <= 
    //    retorneAltura()) &&
    //      (maior.retorneBase() <= 
    //retorneBase());
    //
    //boolean cabeComRotacao = (maior.retorneAltura() <= 
    //      retorneBase()) &&
    //      (maior.retorneBase() <= 
    //   retorneAltura());
    //
    //return cabeSemRotacao || (permiteRotacao && cabeComRotacao);

        }

        @Override
        public float retorneAltura(){

                return getDimensoes().retorneAltura();
        }

        @Override
        public float retorneBase() 
        {
                return getDimensoes().retorneBase();
        }

        //TOMAR CUIDADO COM ESTE M�TODO.
        //OUTRA COISA: DEVO PRESTAR MAIS ATENC�O AO USO DE ID, QUE SIMPLESMENTE
        //N�O ESTA ACONTECENDO
        @Override
        public int getId(){

            return 0;
        }

        @Override        
        public float getCutPosition(){

            return posicaoCorte;
        }

        @Override
        public float getCutTamanho(){

            if(this.corteVertical)
                    return this.tamanho.retorneAltura();

            return this.tamanho.retorneBase();
        }

        @Override
        public boolean corteVertical(){

            return this.corteVertical;
        }

        @Override
        public IPecaPronta getIPecaIPedidoWrapper(){

            return new IBPTNodeITestePecaWrapper(this);
        }

        @Override
        public BinPackTree getArvore() {
            
            return this.Arvore;
        }

        @Override
        public int getNivel(){

            IBPTNode noAtual = this;
            int nivel = 1;

            while(noAtual.getFather() != null){

                nivel++;
                noAtual = noAtual.getFather();
            }

            return nivel;
        }

      @Override
      public int retorneIndiceBin() {
        
          if(Arvore != null)
              return Arvore.getFloresta().retornaIndicePlanoDeCorte(Arvore);

          return -1;
      }

      @Override
      public void acceptPainter(CutLayoutNodesPainter painter){

            if(this.isCuttable()){
                
                painter.paintISobra(this);
                return;
            }
            if(this.temPeca())
                    painter.paintIPeca(this);
            else{
                
                float tamanhoCorte;
                
                if(corteVertical)
                    tamanhoCorte=this.getDimensoes().retorneAltura();
                
                else
                    tamanhoCorte=this.getDimensoes().retorneBase();


                    getLeftSon().acceptPainter(painter);
                    getRigthSon().acceptPainter(painter);
                    painter.paint(new BinPackCorte(posicaoCorte, tamanhoCorte, corteVertical, position));
            }
        }


      @Override
      public void setArvore(BinPackTree arvore) {

            Arvore = arvore;
            if(this.getLeftSon() != null)
            {
                    this.getLeftSon().setArvore(arvore);
                    this.getRigthSon().setArvore(arvore);
            }

            this.estaRemovido = false;
      }

      @Override
      public boolean estaRemovidoDaArvore(){

          return estaRemovido;
      }

      @Override
      public void removeCortes(){

        this.setLeftson(null);
        this.setRightSon(null);
        this.cortado = false;
        this.posicaoCorte = 0;
      }

      @Override
      public int getID(){

        if(this.temPeca())
                return this.pedidoAtendido.id();

        return -1;
      }

      @Override
      public BinPackTreeForest getFloresta(){

        return this.floresta;
      }
      
      @Override
      public boolean cabePeca(boolean permiteRotacao, IDimensao2d maior) {
      
          throw new UnsupportedOperationException("Not supported yet.");
      }
      
      public static void CalculaDesperdicioAproveitamento(BPTPedaco no_atual){
      
          if((no_atual.isCuttable()) && (no_atual.temPeca() == false)){ // É uma sobra
                    
                IDimensao2d dimensPec = no_atual.getDimensoes();
                //no_atual.somatorioSobras = (dimensPec.retorneBase() * dimensPec.retorneAltura());
                no_atual.setSomatorioSobras((dimensPec.retorneBase() * dimensPec.retorneAltura()));
                
                no_atual.setDesperdicio((no_atual.getSomatorioSobras()/ no_atual.getArea())*100);
                
                float aproveitamento_ = (100 - (no_atual.getDesperdicio()));
                no_atual.setAproveitamento(aproveitamento_);
          }
          else if((no_atual.isCuttable() == false) && (no_atual.temPeca() == true)){ //É uma Peça        
                        
              no_atual.somatorioSobras = 0;
              no_atual.setSomatorioSobras(0);
              no_atual.setDesperdicio((no_atual.getSomatorioSobras()/ no_atual.getArea())*100);
              
              float aproveitamento_ = (100 - (no_atual.getDesperdicio()));
              no_atual.setAproveitamento(aproveitamento_);
          }
          else if((no_atual.temPeca() == false) && (no_atual.isCuttable() == false)){  //É um nó intermediário
          
              //no_atual.somatorioSobras = ((BPTPedaco)no_atual.getLeftSon()).getSomatorioSobras();
              //no_atual.somatorioSobras = ((BPTPedaco)no_atual.getRigthSon()).getSomatorioSobras(); 
              
              no_atual.setSomatorioSobras(((BPTPedaco)no_atual.getLeftSon()).getSomatorioSobras() 
                                                    + ((BPTPedaco)no_atual.getRigthSon()).getSomatorioSobras()); 
              
              no_atual.setDesperdicio((no_atual.getSomatorioSobras()/ no_atual.getArea())*100);
              
              float aproveitamento_ = (100 - (no_atual.getDesperdicio()));
              no_atual.setAproveitamento(aproveitamento_);
          }
          
          //float desperdicio_ = (no_atual.getArea() - no_atual.getSomatorioSobras());
          //no_atual.setDesperdicio(desperdicio_);
          
          
      }

    @Override
    public void retireUmaPeca() {
        
        if(temPeca() == true){
        
            getPedidoAtendido().atendaUmPedido();
        }
    }

    public boolean isPecaPronta(){
        
        if(temPeca() == true) 
            return true;
        
        return false;
    }
    
    @Override
    public int getPecasDisponiveis() {
        
        return getPedidoAtendido().quantidade();
    }

    @Override
    public IDimensao2d retorneDimensao() {
        
        return getDimensoes();
    }

    @Override
    public void setPosicao(Ponto pontoInferiorEsquerdo) {
        
        changePositionTo(pontoInferiorEsquerdo);
    }
}

