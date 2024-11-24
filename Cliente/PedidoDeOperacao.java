import java.util.ArrayList;

public class PedidoDeOperacao extends Comunicado {
    private String zipcode;

    public PedidoDeOperacao(String zipcode) throws Exception {
        if (zipcode == null) throw new Exception("Zipcode nulo");

        this.zipcode = zipcode;
    }

    public String getZipcode() throws Exception {
        return this.zipcode;
    }

    public String toString() {
        return ("Pedido para o CEP: " + this.zipcode);
    }
}
