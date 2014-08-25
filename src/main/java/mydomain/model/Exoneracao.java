package mydomain.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

/**
 * A Exoneração é o Ato de ENCERRAR uma Nomeação. Seu efeito pode ser ex tunc,
 * estendendo-se ao passado de forma absoluta, desde a gênese da norma ou ex
 * nunc ou pro futuro.
 *
 * @author gilberto.andrade
 */
@Entity
@Table(name = "exoneracao", schema = "sapeo", uniqueConstraints = @UniqueConstraint(name = "exoneracao_uk",columnNames = {"nomeacao_id", "ato_legal_inicio_id"}))
public class Exoneracao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "exoneracao_id_seq")
    @SequenceGenerator(schema = "sapeo", name = "exoneracao_id_seq", sequenceName = "exoneracao_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "nomeacao_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Nomeacao nomeacao;

    @Column(name = "eh_a_pedido")
    @Basic(optional = false)
    private boolean ehAPedido;

    @Column(name = "dt_entra_em_vigor")
    @Temporal(TemporalType.DATE)
    @Basic(optional = false)
    private Date dtEntraEmVigor;

    @Column(name = "dt_efeito_juridico")
    @Temporal(TemporalType.DATE)
    private Date dtEfeitoJuridico;

    @JoinColumn(name = "ato_legal_inicio_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private AtoLegal atoLegalInicio;

    @JoinColumn(name = "ato_legal_termino_id", referencedColumnName = "id")
    @ManyToOne
    private AtoLegal atoLegalTermino;

    public Exoneracao() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDtEntraEmVigor() {
        return dtEntraEmVigor;
    }

    public void setDtEntraEmVigor(Date dtEntraEmVigor) {
        this.dtEntraEmVigor = dtEntraEmVigor;
    }

    public Nomeacao getNomeacao() {
        return nomeacao;
    }

    public void setNomeacao(Nomeacao nomeacao) {
        this.nomeacao = nomeacao;
    }

    public boolean getEhAPedido() {
        return ehAPedido;
    }

    public void setEhAPedido(boolean ehAPedido) {
        this.ehAPedido = ehAPedido;
    }

    public AtoLegal getAtoLegalTermino() {
        return atoLegalTermino;
    }

    public void setAtoLegalTermino(AtoLegal atoLegalTermino) {
        this.atoLegalTermino = atoLegalTermino;
    }

    public AtoLegal getAtoLegalInicio() {
        return atoLegalInicio;
    }

    public void setAtoLegalInicio(AtoLegal atoLegalInicio) {
        this.atoLegalInicio = atoLegalInicio;
    }

    public Date getDtEfeitoJuridico() {
        return dtEfeitoJuridico;
    }

    public void setDtEfeitoJuridico(Date dtEfeitoJuridico) {
        this.dtEfeitoJuridico = dtEfeitoJuridico;
    }

    public boolean isStatusFinalizado() {
        try {
            this.atoLegalTermino.getId();
        } catch (NullPointerException ex) {
            return false;
        }
        return true;
    }

    @Override
    public boolean equals(Object other) {
        return (other instanceof Exoneracao) && (id != null)
                ? id.equals(((Exoneracao) other).id)
                : (other == this);
    }

    @Override
    public int hashCode() {
        return (id != null)
                ? (this.getClass().hashCode() + id.hashCode())
                : super.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Exoneracao [");
        builder.append("id=").append(id);
        builder.append(", nomeacao=").append(nomeacao != null ? nomeacao.getId() : "null");
        builder.append(", dtEfeitoJuridico=").append(dtEfeitoJuridico);
        builder.append(", dtEntraEmVigor=").append(dtEntraEmVigor);
        builder.append(", atoLegalInicio=").append(atoLegalInicio != null ? atoLegalInicio.getId() : "null");
        builder.append(", atoLegalTermino=").append(atoLegalTermino != null ? atoLegalTermino.getId() : "null");
        builder.append("]");
        return builder.toString();
    }

}
