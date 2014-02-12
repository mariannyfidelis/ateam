package ComunicaoConcorrenteParalela;

import java.io.IOException;
import java.nio.channels.SelectionKey;

public interface ProtocoloComunicacao {

    void gerenciaConexao(SelectionKey selectionKey) throws IOException;
    void gerenciaLeitura(SelectionKey selectionKey) throws IOException;
    void gerenciaEscrita(SelectionKey selectionKey) throws IOException;
}
