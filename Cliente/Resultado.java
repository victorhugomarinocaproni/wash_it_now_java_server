import java.util.ArrayList;

public class Resultado extends Comunicado
{
    private final ArrayList<Servico> listaDeServicos;

    public Resultado (ArrayList<Servico> listaDeServicos) throws Exception {
        this.listaDeServicos = listaDeServicos;
    }

    public ArrayList<Servico> getListaDeServicos ()
    {
        return this.listaDeServicos;
    }

    public String toString ()
    {
        return ("Lista de Servicos com: "+ this.listaDeServicos.size() + " itens");
    }

}
