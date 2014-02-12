package HeuristicaConstrutivaInicial;



public interface IPeca {
   
        boolean permiteRotacao();

        boolean isSelected();

        void setSelected(boolean b);

        ISobra remova();

        //IPecaIPedacoWrapper getIPecaIPedacoWrapper();

        Ponto getPontoInfEsq();

        Ponto getPontoSupDir();

        IDimensao2d getDimensoes();

        //IPecaPronta getIPecaIPedidoWrapper();

        //void acceptPainter(CutLayoutNodesPainter painter);
}