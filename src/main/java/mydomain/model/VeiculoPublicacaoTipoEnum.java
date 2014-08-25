package mydomain.model;

/**
 * Representa o enumerado para tipo de veículo de publicação.
 *
 * @author gilberto.andrade
 */
public enum VeiculoPublicacaoTipoEnum {

    DOE("Diário Oficial do Estado"),
    DOM("Diário Oficial do Município"),
    DOU("Diário Oficial da União"),
    JCE("Jornal Circulação Estadual"),
    JCM("Jornal Circulação Municipal");

    String label;

    VeiculoPublicacaoTipoEnum(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return this.label;
    }
}
