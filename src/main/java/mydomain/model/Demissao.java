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
 *
 * @author gilberto.andrade
 */
@Entity
@Table(name = "demissao", schema = "sapeo", uniqueConstraints = @UniqueConstraint(name = "demissao_uk",columnNames = {"nomeacao_id", "ato_legal_inicio_id"}))
public class Demissao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "demissao_id_seq")
    @SequenceGenerator(schema = "sapeo", name = "demissao_id_seq", sequenceName = "demissao_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "nomeacao_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Nomeacao nomeacao;

    @Column(name = "nu_processo")
    @Basic(optional = false)
    private String nuProcesso;
    
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

    public Demissao() {
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

    public String getNuProcesso() {
        return nuProcesso;
    }

    public void setNuProcesso(String nuProcesso) {
        this.nuProcesso = nuProcesso;
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
        return (other instanceof Demissao) && (id != null)
                ? id.equals(((Demissao) other).id)
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
        builder.append("Demissao [");
        builder.append("id=").append(id);
        builder.append(", nuProcesso=").append(nuProcesso);
        builder.append(", nomeacao=").append(nomeacao != null ? nomeacao.getId() : "null");
        builder.append(", dtEfeitoJuridico=").append(dtEfeitoJuridico);
        builder.append(", dtEntraEmVigor=").append(dtEntraEmVigor);
        builder.append(", atoLegalInicio=").append(atoLegalInicio != null ? atoLegalInicio.getId() : "null");
        builder.append(", atoLegalTermino=").append(atoLegalTermino != null ? atoLegalTermino.getId() : "null");
        builder.append("]");
        return builder.toString();
    }

}
