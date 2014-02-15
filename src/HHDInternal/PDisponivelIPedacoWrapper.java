package HHDInternal;

import HHDInterfaces.IDimensao2d;
import HHDInterfaces.IPedaco;


public class PDisponivelIPedacoWrapper implements IPedaco{
    
    private PedacoDisponivel pedacoEmpacotado;
    private int indiceBinPedaco;

    PDisponivelIPedacoWrapper(PedacoDisponivel pedaco, int indiceBin){

        pedacoEmpacotado = pedaco;
        indiceBinPedaco = indiceBin; 
    }

    @Override
    public int retorneIndiceBin(){

        return indiceBinPedaco; 
    }

    @Override
    public IDimensao2d getDimensao(){

        return new AreaRetangular(pedacoEmpacotado.retorneBase(), pedacoEmpacotado.retorneAltura());
    }

    @Override
    public Ponto getPontoInferiorEsquerdo(){

        return pedacoEmpacotado.getPontoInferiorEsquerdo();
    }

    @Override
    public boolean podeRotacionar(){

        return false;
    }

    @Override
    public boolean temPeca(){

        return false;
    }

    /* (non-Javadoc)
     * @see cuttingInterface.IPeda�o#isSelected()
     */
    @Override
    public boolean isSelected() {

        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see cuttingInterface.IPeda�o#removaDoBin()
     */
    @Override
    public void removaDoBin() {
        // TODO Auto-generated method stub
    }

    /* (non-Javadoc)
     * @see cuttingInterface.IPeda�o#est�Removido()
     */
    @Override
    public boolean estaRemovido() {

        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see cuttingInterface.IPeda�o#getID()
     */
    @Override
    public int getID() {

        // TODO Auto-generated method stub
        return 0;
    }
}
