package HeuristicaConstrutivaInicial;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;


public class Peca{

    private PedacoDisponivel pedacoCortado;
    private IPedido pedidoAtendido;
    private boolean permiteRotacao;
    private boolean selected;

    public Peca(PedacoDisponivel pCortado, IPedido pedido, boolean podeRotacionar)
	                                                                   throws PecaInvalidaException{
	
	if(pCortado.cabePeca(pedido.retorneDimensao(), podeRotacionar)){
		pedacoCortado = pCortado;
		pedidoAtendido = pedido;
	}
	else
        	throw new PecaInvalidaException();
	
            selected = false;
    }

    public IDimensao2d retornDimensoes(){
        return pedidoAtendido.retorneDimensao();
    }
     
    public IDimensao2d retorneDimensoes(){
    
        return new Dimensao2D(retornaAltura(), retornaBase());
    }
    
    public float retornaBase(){
    
        return pedacoCortado.retorneBase();
    }
    
    public float retornaAltura(){
    
        return pedacoCortado.retorneAltura();
    }
	
    public void paint(Graphics g, float fator, IDimensao2d tamanhoChapa){

        int posx, posy;

        posx = (int)pedacoCortado.getPontoInferiorEsquerdo().getX();
        posy = (int)(tamanhoChapa.retorneAltura() -
                               (pedacoCortado.getPontoInferiorEsquerdo().getY() +
                               pedidoAtendido.retorneDimensao().retorneAltura()));
        g.setColor(Color.YELLOW);
        g.fillRect((int)(posx * fator), 
                             (int)(posy * fator), 
                                  (int)(pedacoCortado.retorneBase() * fator), 
                                  (int)(pedacoCortado.retorneAltura() * fator));
        g.setColor(Color.BLACK);
        g.drawRect((int)(posx * fator), 
                     (int)(posy * fator), 
                          (int)(pedacoCortado.retorneBase() * fator), 
                          (int)(pedacoCortado.retorneAltura() * fator));

        if(isSelected())
                paintSelected(g,
                                (int)(posx * fator), 
                     (int)(posy * fator), 
                          (int)(pedacoCortado.retorneBase() * fator), 
                          (int)(pedacoCortado.retorneAltura() * fator));
    }

    private void paintSelected(Graphics g, int posx, int posy, int base, int altura){

        Graphics2D g2 = (Graphics2D) g;
        g2.setColor ( Color.BLUE );
        g2.setComposite( AlphaComposite.getInstance( AlphaComposite.SRC_OVER, (float) 0.3 ) );
        g.fillRect( posx, posy, base, altura );
        g2.setComposite( AlphaComposite.getInstance( AlphaComposite.SRC_OVER, (float) 1 ) );
    }

    public IPedido getPedidoAtendido(){
        return pedidoAtendido;
    }

    public Ponto getPontoInfEsq(){

        return pedacoCortado.getPontoInferiorEsquerdo();
    }

    public Ponto getPontoSupDir(){

        return pedacoCortado.getPontoSuperiorDireito();
    }

    public boolean permiteRotacao() {
        return permiteRotacao;
    }
    
    public void setSelected(){
        selected = true;
    }

    public void movaPara(PedacoDisponivel areaDestino){
        pedacoCortado = areaDestino;
    }

    public PedacoDisponivel remova(){

        PedacoDisponivel sobra = pedacoCortado;
        pedacoCortado = null;
        return sobra;
    }

    public boolean isSelected() {
            return selected;
    }


    public void setSelected(boolean b){
            selected = b;
    }

   /* public IPecaIPedacoWrapper getIPecaIPedacoWrapper() {

            return null;
    }*/
}
