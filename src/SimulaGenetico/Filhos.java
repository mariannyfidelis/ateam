package SimulaGenetico;

import Heuristicas.Individuo;

public class Filhos {

	private Individuo filho1;
	private Individuo filho2;
	
	public Filhos(){        }
	
        public Filhos(Filhos filhos){
        
            this.filho1 = filhos.getFilho1();
            this.filho2 = filhos.getFilho2();
        }
        
        public Filhos(Individuo ind1, Individuo ind2){
        
            this.filho1 = ind1;
            this.filho2 = ind2;
        }
        
	public Individuo getFilho1() {
	    return filho1;
	}
	
	public Individuo getFilho2() {
            return filho2;
	}
	
        public void setFilhos(Individuo ind1, Individuo ind2){
        
            this.filho1 = ind1;
            this.filho2 = ind2;
        }
        
        public void setFilho1(Individuo filho1) {
            this.filho1 = filho1;
	}
	
        public void setFilho2(Individuo filho2) {
            this.filho2 = filho2;
	}	
        
        public void setFilhos(Filhos filhos){
        
            this.filho1 = filhos.getFilho1();
            this.filho2 = filhos.getFilho2();
        }
        
}