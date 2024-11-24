import java.io.Serializable;

public class Servico implements Serializable {

    private int id;
    private String nome;
    private String endereco;
    private String descricao;
    private double preco;
    private final String zipcode;
    private int numero;

    public Servico (
            int id,
            String nome,
            String endereco,
            String descricao,
            double preco,
            String zipcode,
            int numero
    )
    {
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.descricao = descricao;
        this.preco = preco;
        this.zipcode = zipcode;
        this.numero = numero;
    }

    public int getId() {
        return this.id;
    }

    public String getNome() {
        return this.nome;
    }

    public String getEndereco() {
        return this.endereco;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public double getPreco() {
        return this.preco;
    }

    public int getNumero() {
        return this.numero;
    }

    public String getZipcode () {
        return this.zipcode;
    }
}
