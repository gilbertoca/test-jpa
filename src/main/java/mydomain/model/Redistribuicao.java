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
 * A Redistribuição é o Ato de REDISTRIBUIR simbólicamente uma Vaga para OUTRA
 * ENTIDADE através da Nomeação.
 *
 * @author gilberto.andrade
 */
@Entity
@Table(name = "redistribuicao", schema = "sapeo", uniqueConstraints = @UniqueConstraint(name = "redistribuicao_uk",columnNames = {"nomeacao_id", "entidade_id", "ato_legal_inicio_id"}))
public class Redistribuicao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "redistribuicao_id_seq")
    @SequenceGenerator(schema = "sapeo", name = "redistribuicao_id_seq", sequenceName = "redistribuicao_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "dt_entra_em_vigor")
    @Temporal(TemporalType.DATE)
    @Basic(optional = false)
    private Date dtEntraEmVigor;

    @Column(name = "dt_efeito_juridico")
    @Temporal(TemporalType.DATE)
    private Date dtEfeitoJuridico;

    @JoinColumn(name = "nomeacao_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Nomeacao nomeacao;
    
    @JoinColumn(name = "entidade_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Entidade entidade;

    @JoinColumn(name = "ato_legal_inicio_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private AtoLegal atoLegalInicio;

    @JoinColumn(name = "ato_legal_termino_id", referencedColumnName = "id")
    @ManyToOne
    private AtoLegal atoLegalTermino;

    public Redistribuicao() {
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

    public Entidade getEntidade() {
        return entidade;
    }

    public void setEntidade(Entidade entidade) {
        this.entidade = entidade;
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
        return (other instanceof Redistribuicao) && (id != null)
                ? id.equals(((Redistribuicao) other).id)
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
        builder.append("Redistribuicao [");
        builder.append("id=").append(id);
        builder.append(", nomeacao=").append(nomeacao != null ? nomeacao.getId() : "null");
        builder.append(", entidade=").append(entidade != null ? entidade.getId() : "null");        
        builder.append(", dtEfeitoJuridico=").append(dtEfeitoJuridico);
        builder.append(", dtEntraEmVigor=").append(dtEntraEmVigor);
        builder.append(", atoLegalInicio=").append(atoLegalInicio != null ? atoLegalInicio.getId() : "null");
        builder.append(", atoLegalTermino=").append(atoLegalTermino != null ? atoLegalTermino.getId() : "null");
        builder.append("]");
        return builder.toString();
    }

}
