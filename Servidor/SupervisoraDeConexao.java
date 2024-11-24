import java.io.*;
import java.net.*;
import java.util.*;

public class SupervisoraDeConexao extends Thread
{
    private Parceiro usuario;
    private Socket conexao;
    private final ArrayList<Parceiro> usuarios;
    private final ArrayList<Servico> servicos = new ArrayList<>();

    // ISSO DAQUI É PARA NÃO TER QUE FAZER A BUSCA NO BANCO (NÃO SEI SE TEREMOS TEMPO DE IMPLEMENTAR)
    // ----------------------------------------------------------------------------------------------
    private final ArrayList<Servico> servicosCadastrados = new ArrayList<Servico>();

    Servico servico1 = new Servico(
            4,
            "Tá limpo, lava-rápido",
            "Av. Taltaltal de Tau",
            "Atuando desde não sei quando, oferecendo serviços de qualidade",
            15.00,
            "13086-754",
            115
    );

    Servico servico2 = new Servico(
            5,
            "Lava-rápido Celsão",
            "Av. Teuteuteu de Teu",
            "",
            25.00,
            "13086-200",
            231
    );

    Servico servico3 = new Servico(
            6,
            "Calota Limpa, Lavadora",
            "Av. Não sei o que, Não sei que Lá",
            " Oferecemos nossos servi�os de lavagem para qualquer um con o dia a dia corrido",
            17.50,
            "13086-400",
            9
    );
    // ----------------------------------------------------------------------------------------------

    public SupervisoraDeConexao
    (Socket conexao, ArrayList<Parceiro> usuarios)
    throws Exception
    {
        if (conexao==null)
            throw new Exception ("Conexao ausente");

        if (usuarios==null)
            throw new Exception ("Usuarios ausentes");

        this.conexao  = conexao;
        this.usuarios = usuarios;


        // PARA NÃO PRECISAR BATER NO BANCO PROCURANDO PELOS SERVIÇOS (NÃO SEI SE DARÁ PARA IMPLEMENTAR ISSO ATÉ A DATA LIMITE)
        this.servicosCadastrados.add(this.servico1);
        this.servicosCadastrados.add(this.servico2);
        this.servicosCadastrados.add(this.servico3);
    }

    public void run ()
    {

        ObjectOutputStream transmissor;
        try {
            transmissor = new ObjectOutputStream(this.conexao.getOutputStream());
        } catch (Exception erro) {
            return;
        }

        ObjectInputStream receptor = null;
        try {
            receptor = new ObjectInputStream(this.conexao.getInputStream());
        } catch (Exception err0) {
            try {
                transmissor.close();
            } catch (Exception falha) {} // so tentando fechar antes de acabar a thread
            
            return;
        }

        try {
            this.usuario = new Parceiro (
                    this.conexao,
                    receptor,
                    transmissor
            );
        } catch (Exception erro) {} // sei que passei os parametros corretos

        try {
            synchronized (this.usuarios) {
                this.usuarios.add(this.usuario);
            }

            // A partir daqui vamos modificar algumas coisas:
            for(;;)
            {
                Comunicado comunicado = this.usuario.envie();

                if (comunicado == null) return;

                // MODIFICAR
                // -----------------------------------------
                else if (comunicado instanceof PedidoDeOperacao)
                {
					PedidoDeOperacao pedidoDeOperacao = (PedidoDeOperacao)comunicado;

                    String zipcode = pedidoDeOperacao.getZipcode();

                    for (Servico servico : this.servicosCadastrados) {
                        if (zipcode.equals(servico.getZipcode())) {
                            servicos.add(servico);
                        }
                    }
                    this.usuario.receba(new Resultado(servicos));
                }
                // -----------------------------------------

                // OK
                else if (comunicado instanceof PedidoParaSair)
                {
                    synchronized (this.usuarios)
                    {
                        this.usuarios.remove(this.usuario);
                    }
                    this.usuario.adeus();
                }
            }
        }
        catch (Exception erro)
        {
            try
            {
                transmissor.close ();
                receptor.close ();
            }
            catch (Exception falha)
            {} // so tentando fechar antes de acabar a thread

            return;
        }
    }
}
