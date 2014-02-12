package AgCombinacao;

import ComunicaoConcorrenteParalela.ObjetoComunicacao;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

interface AgentesServicos {

    
    public void criaConexaoServidor(int porta_comunicacao);
    
    public SocketChannel criaConexaoServidor2(int porta_comunicacao);
    //public void execute(ServicoAgente servico_agente, TiposServicosAgentes tipo_servico);
    
    public void escreveSocketChannel(ByteBuffer byteBuffer, SocketChannel socket);
    
    public ByteBuffer lerSocketChannel(SocketChannel socket, SelectionKey selectKey);
    
    public ByteBuffer serializaMensagem(ObjetoComunicacao objeto);
    
    public ObjetoComunicacao  deserializaMensagem(ByteBuffer byteBuffer);
    
    //public ObjetoComunicacao processaInformacao(ByteBuffer byteBuffer,ServicoAgente servico_agente, TiposServicosAgentes tipo_servico)
    //       throws IllegalArgumentException;
     
}
