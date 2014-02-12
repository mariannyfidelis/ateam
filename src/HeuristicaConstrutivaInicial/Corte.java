package HeuristicaConstrutivaInicial;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Comparator;


public class Corte implements Comparator{

    private int id;
    private float posicaoCorte;
    private float tamanho;
    private boolean vertical;
    private Ponto pontoChapaCortada;

    public Corte(float pos, Ponto pontoChapa, boolean corteVertical, float tamanho, int idCorte) {

            posicaoCorte = pos;
            pontoChapaCortada = pontoChapa;
            this.vertical = corteVertical;
            this.tamanho = tamanho;
            id = idCorte;
    }

    public Corte() {
    }

    public void registraCorte(float pos, Ponto pontoChapa, boolean corteVertical, 
                                        float tamanho, int idCorte)
    {
            posicaoCorte = pos;
            pontoChapaCortada = pontoChapa;
            this.vertical = corteVertical;
            this.tamanho = tamanho;
            id = idCorte;
    }

    public float getPosicaoCorte(){

            return posicaoCorte;
    }

    public float getTamanho(){
            return tamanho;
    }

    public boolean eVertical(){

            return vertical;
    }

    public int getId(){

            return id;
    }
    
    public void setId(int id){
        this.id = id;
    }

    public int compare(Object arg0, Object arg1){

            return ((Corte)arg0).getId() - ((Corte)arg1).getId();
    }

    public void paint(Graphics g, float fator, IDimensao2d tamanhoChapa){

            float posx, posy; 
            int largura, altura;

            float maxy = tamanhoChapa.retorneAltura();

            if(vertical){

                    posx = pontoChapaCortada.getX() + posicaoCorte;
                    posy = pontoChapaCortada.getY() + tamanho;

                    altura = (int) (tamanho * fator);
                    largura = 2;
            }
            else{

                    posx = pontoChapaCortada.getX();
                    posy = pontoChapaCortada.getY() + posicaoCorte;

                    altura = 2;
                    largura = (int) (tamanho * fator);
            }

            posx = posx * fator;
            posy = (maxy - posy) * fator;

            g.setColor( Color.RED);
            g.fillRect((int) posx, (int) posy, largura, altura);
    }
	
    public Ponto getPontoChapaCortada(){

            return pontoChapaCortada;
    }
}
