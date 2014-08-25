package mydomain.model;

/**
 * Representa o enumerado para tipo de ato legal.
 *
 * @author gilberto.andrade
 */
public enum AtoLegalTipoEnum {

    APOSTILA("Apostila"),
    CESSAO("Cessão"),
    DECLARATORIO("Declaratório"),
    DECRETO("Decreto"),
    DESIGNACAO("Designação"),
    DISPENSA("Dispensa"),
    EXONERACAO("Exoneração"),
    LEI_COMPLEMENTAR("Lei Complementar"),
    LEI("Lei"),
    MEDIDA_PROVISORIA("Medida Provisória"),
    NOMEACAO("Nomeação"),
    PORTARIA("Portaria"),
    PROMOCAO("Promoção"),
    REDISTRIBUICAO("Redistribuição"),
    REMOCAO("Remoção"),
    RETIFICACAO("Retificação"),
    REVOGACAO("Revogação");

    String label;

    AtoLegalTipoEnum(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return this.label;
    }
}
