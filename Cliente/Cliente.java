import com.google.gson.Gson;

import java.net.*;
import java.io.*;

public class Cliente {
    public static final String HOST_PADRAO = "localhost";
    public static final int PORTA_PADRAO = 3000;

    public static void main(String[] args) {
        if (args.length > 3) {
            System.err.println("Uso esperado: java Cliente [[HOST] [PORTA] [ZIPCODE]]\n");
            return;
        }

        Socket conexao = null;
        String zipcode = "";
        try {
            String host = Cliente.HOST_PADRAO;
            int porta = Cliente.PORTA_PADRAO;

            if (args.length > 0) {
                host = args[0];
                zipcode = args[2];
            }

            if (args.length == 3)
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
            //System.out.println("Conectando ao servidor...");
            servidor = new Parceiro(conexao, receptor, transmissor);
            //System.out.println("Conectado com sucesso!");
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

        try {
            servidor.receba(new PedidoDeOperacao(zipcode));

            Comunicado comunicado = null;
            do
            {
                comunicado = (Comunicado)servidor.espie();
            }
            while (!(comunicado instanceof Resultado));
            Resultado resultado = (Resultado)servidor.envie();
            //System.out.println ("Tamanho da Lista de Serviços Retornada: " + resultado.getListaDeServicos().size());

            Gson gson = new Gson();
            String json = gson.toJson(resultado.getListaDeServicos());
            System.out.println(json);

        } catch (Exception erro) {
            System.err.println("Erro de comunicacao com o servidor;");
            System.err.println("Tente novamente!");
            System.err.println("Caso o erro persista, termine o programa");
            System.err.println("e volte a tentar mais tarde!\n");
        }

        try {
            servidor.receba(new PedidoParaSair());
        } catch (Exception erro) {}

        System.exit(0);
    }
}
