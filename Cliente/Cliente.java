import com.google.gson.Gson;
import netscape.javascript.JSObject;

import java.net.*;
import java.io.*;

public class Cliente {
    public static final String HOST_PADRAO = "localhost";
    public static final int PORTA_PADRAO = 3000;

    public static void main(String[] args) {
        if (args.length > 2) {
            System.err.println("Uso esperado: java Cliente [HOST [PORTA]]\n");
            return;
        }

        Socket conexao = null;
        try {
            String host = Cliente.HOST_PADRAO;
            int porta = Cliente.PORTA_PADRAO;

            if (args.length > 0)
                host = args[0];

            if (args.length == 2)
                porta = Integer.parseInt(args[1]);

            conexao = new Socket(host, porta);
        } catch (Exception erro) {
            System.err.println("Indique o servidor e a porta corretos!\n");
            return;
        }

        ObjectOutputStream transmissor = null;
        try {
            transmissor = new ObjectOutputStream(conexao.getOutputStream());
        } catch (Exception erro) {
            System.err.println("Indique o servidor e a porta corretos!\n");
            return;
        }

        ObjectInputStream receptor = null;
        try {
            receptor = new ObjectInputStream(conexao.getInputStream());
        } catch (Exception erro) {
            System.err.println("Indique o servidor e a porta corretos!\n");
            return;
        }

        Parceiro servidor = null;
        try {
            System.out.println("Conectando ao servidor...");
            servidor = new Parceiro(conexao, receptor, transmissor);
            System.out.println("Conectado com sucesso!");
        } catch (Exception erro) {
            System.err.println("Indique o servidor e a porta corretos!\n");
            return;
        }

        TratadoraDeComunicadoDeDesligamento tratadoraDeComunicadoDeDesligamento = null;
        try {
            tratadoraDeComunicadoDeDesligamento = new TratadoraDeComunicadoDeDesligamento(servidor);
        } catch (Exception erro) {
        } // sei que servidor foi instanciado

        tratadoraDeComunicadoDeDesligamento.start();

        // Fazer tipo um "if login -> pedir para o Servidor enviar os Servicos disponiveis
        try {
            servidor.receba(new PedidoDeOperacao("13086-200"));

            Comunicado comunicado = null;
            do
            {
                comunicado = (Comunicado)servidor.espie();
            }
            while (!(comunicado instanceof Resultado));
            Resultado resultado = (Resultado)servidor.envie();
            System.out.println ("Tamanho da Lista de Serviços Retornada: " + resultado.getListaDeServicos().size());

        } catch (Exception erro) {
            System.err.println("Erro de comunicacao com o servidor;");
            System.err.println("Tente novamente!");
            System.err.println("Caso o erro persista, termine o programa");
            System.err.println("e volte a tentar mais tarde!\n");
        }


        // Fazer tipo um  "if logout -> pedir para sair"
        try {
            servidor.receba(new PedidoParaSair());
        } catch (Exception erro) {}

        System.out.println("Obrigado por usar este programa!");
        System.exit(0);
    }
}
