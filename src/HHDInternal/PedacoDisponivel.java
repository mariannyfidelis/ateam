package HHDInternal;

import HHDInterfaces.IDimensao2d;
import HHDInterfaces.IPedido;
import HHDInterfaces.ISobra;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.Serializable;


public class PedacoDisponivel implements ISobra, Serializable{

    private Ponto pontoInferiorEsquerdo;
    private Ponto pontoSuperiorDireito;
    private int id;
	
    public PedacoDisponivel(Ponto pIE, Ponto pSD, int id){

        pontoInferiorEsquerdo = pIE;
        pontoSuperiorDireito = pSD;
        this.id = id;
    }
			
    public PedacoDisponivel(Ponto ponto, IDimensao2d tamanhoChapa, int id){

        pontoInferiorEsquerdo = ponto;
        pontoSuperiorDireito = new Ponto(ponto.getX() + tamanhoChapa.retorneBase(),
                                                        ponto.getY() + tamanhoChapa.retorneAltura());
        this.id = id;
    }

    @Override
    public Ponto getPontoInferiorEsquerdo() {

        return pontoInferiorEsquerdo;
    }
    
    public Ponto setPontoInferiorEsquerdo(Ponto piFE) {

        this.pontoInferiorEsquerdo = piFE;
        
        return this.pontoInferiorEsquerdo;
    }
	
    @Override
    public Ponto getPontoSuperiorDireito() {

        return pontoSuperiorDireito;
    }

    public int cabePeca(IPedido maior, boolean permiteRotacao){

            return cabePeca(maior.retorneDimensao(), permiteRotacao);
    }
	
    @Override
    public int cabePeca(IDimensao2d maior, boolean permiteRotacao){

        // 0 - cabeSemRotacao
        //-1 - Não cabe
        // 1 - Cabe rotacionado
        int permite = 0; //"0" é o mesmo que "false";
        boolean cabeSemRotacao = (maior.retorneAltura() <= retorneAltura()) && (maior.retorneBase() <=  retorneBase());
        
        if(cabeSemRotacao == true){ 
            
            return 0;
        }

        if(permiteRotacao == true){
            
            boolean cabeComRotacao = (maior.retorneAltura() <= retorneBase()) && 
                                                        (maior.retorneBase() <= retorneAltura());

            if(cabeComRotacao == true){  
            
                return 1;
            }
        }
        
            
        return -1;
                
        //return cabeSemRotacao || (permiteRotacao && cabeComRotacao);
    }

    //Aqui será implementado o cabe Peça retornando o booleano original
    public boolean cabePecaG(IPedido maior, boolean permiteRotacao){

        return cabePeca( permiteRotacao, maior.retorneDimensao());
    }
	
    public boolean cabePecaG(boolean permiteRotacao,IDimensao2d maior ){

        System.out.println("\n\nPedido --> (Largura x Altura) -> ("+maior.retorneBase()+" , "+maior.retorneAltura()+")");

        System.out.println("\nPeça Criada --> (Largura x Altura) -> ("+retorneBase()+" , "+retorneAltura()+")");

        boolean cabeSemRotacao = (maior.retorneAltura() <= retorneAltura()) && (maior.retorneBase() <=  retorneBase());

        boolean cabeComRotacao = (maior.retorneAltura() <= retorneBase()) &&   (maior.retorneBase() <= retorneAltura());

        return cabeSemRotacao || (permiteRotacao && cabeComRotacao);
    }
    
    public boolean podeAtenderPedido(IPedido pedido, boolean permiteRotacao){
        
            boolean atendeSemRotacao = 
                        (pedido.retorneDimensao().retorneAltura() == 
            retorneAltura()) &&
            (pedido.retorneDimensao().retorneBase() == 
                            retorneBase());

            boolean atendeComRotacao =
                    (pedido.retorneDimensao().retorneAltura() == 
            retorneBase()) &&
            (pedido.retorneDimensao().retorneBase() == 
            retorneAltura());

            return atendeSemRotacao || (permiteRotacao && atendeComRotacao);
    }

    @Override
    public float retorneAltura(){
        
        return pontoSuperiorDireito.getY() - pontoInferiorEsquerdo.getY();
    }

    @Override
    public float retorneBase(){
        
        return pontoSuperiorDireito.getX() - pontoInferiorEsquerdo.getX();
    }

    public IDimensao2d retorneDimensao(){
    
        return new Dimensao2D(retorneBase(), retorneAltura());
    }
    
    @Override
    public int getId(){
        
        return id;
    }

    public void setId(int id) {
        
        this.id = id;
    }
    
    public PedacoDisponivel corte(Corte corte){
            
        Ponto pIE, pSD;

        float x, y;

        pSD = new Ponto(pontoSuperiorDireito.getX(), pontoSuperiorDireito.getY());

        if(corte.eVertical()){

            x = pontoInferiorEsquerdo.getX() + corte.getPosicaoCorte();
            y = pontoInferiorEsquerdo.getY();
            pontoSuperiorDireito = new Ponto(x, pSD.getY());
        }
        else{
            
            x = pontoInferiorEsquerdo.getX();
            y = pontoInferiorEsquerdo.getY() + corte.getPosicaoCorte();
            pontoSuperiorDireito = new Ponto(pSD.getX(), y);
        }

        pIE = new Ponto(x, y);

        if(((pSD.getX() - pIE.getX()) == 0) ||(pSD.getY() - pIE.getY()) == 0){

            return null;
        }

        return new PedacoDisponivel(pIE, pSD, this.id + 1);
    }

    /**
     * @param g
     * @param fator
     * @param tamChapa
     */
    public void paint(Graphics g, float fator, IDimensao2d tamanhoChapa){
            int posx, posy;

            posx = (int)this.getPontoInferiorEsquerdo().getX();
            posy = (int)(tamanhoChapa.retorneAltura() -
                                   (this.getPontoInferiorEsquerdo().getY() +
                                            this.retorneAltura()));
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor ( Color.RED );
            g2.setComposite( AlphaComposite.getInstance( AlphaComposite.SRC_OVER, (float) 0.3 ) );
            g.fillRect( (int)(posx * fator),(int)( posy * fator), (int)(this.retorneBase() * fator), (int)(this.retorneAltura() * fator));
            g2.setComposite( AlphaComposite.getInstance( AlphaComposite.SRC_OVER, (float) 1 ) );
    }

    @Override
    public int retorneIndiceBin(){

       return 0;
    }

    @Override
    public boolean cabePeca(boolean permiteRotacao, IDimensao2d maior) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
