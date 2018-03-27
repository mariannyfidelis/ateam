package ATeam;

import java.io.ObjectInputStream;
import SimulaGenetico.ETiposServicosAgentes;
import ComunicaoConcorrenteParalela.ServicoAgente;
import ComunicaoConcorrenteParalela.ETiposServicosServidor;
import ComunicaoConcorrenteParalela.ObjetoComunicacaoMelhorado;

public interface AgenteClienteFinal{
 
     
    public boolean isExecutando();
    public void open(String endereco, int portacomunicacao) throws Exception;
    public void close();
    public void start();
    public void stop() throws Exception;
    public void send(ObjetoComunicacaoMelhorado obcom);
    public ObjetoComunicacaoMelhorado receive(ObjectInputStream inputObj);
    public String getName();
    public void setName(String name);
    public int getPortaComunicacao();
    public void setPortaComunicacao(int porta);
    public ServicoAgente getServicoAgente();
    public void setServicoAgente(ServicoAgente servico);
    public ETiposServicosAgentes getTipoServico();
    public void setTipoServico(ETiposServicosAgentes etipoServico);
    public ETiposServicosServidor getTipoServicoServidor();
    public void setTipoServicoServidor(ETiposServicosServidor etipoServicoServidor);
    
}
