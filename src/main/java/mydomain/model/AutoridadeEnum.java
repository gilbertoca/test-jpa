package mydomain.model;

/**
 * Representa o enumerado para tipo de autoridade que assina um Ato Legal.
 *
 * @author gilberto.andrade
 */
public enum AutoridadeEnum {

    GOVERNADOR("Governador"),
    MINISTRO("Ministro"),
    PREFEITO("Prefeito"),
    PRESIDENTE("Presidente"),
    SECRETARIO("Secretário");

    String label;

    AutoridadeEnum(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return this.label;
    }
}
