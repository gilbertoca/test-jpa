package mydomain.model;

/**
 * Representa o enumerado para tipo de agrupamento das denominações de cargos.
 *
 * @author gilberto.andrade
 */
public enum AgrupamentoEnum {

    AGENTES_POLITICOS("Agentes Políticos"),
    DIRIGENTES_E_ASSESSORES("Dirigentes e Assessores"),
    LEGISLACAO_FEDERAL("Legislação Federal"),
    SEM_PREVISAO_LEGAL("Sem previsão legal");
    
    String label;

    AgrupamentoEnum(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return this.label;
    }
}
