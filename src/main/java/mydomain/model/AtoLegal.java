package mydomain.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
 * No Brasil, a obrigatoriedade da lei surge a partir da sua publicação no
 * Diário Oficial(dtPubicacao), mas a sua vigência(dtEntraEmVigor) não se inicia
 * no dia da publicação, salvo se ela assim o estipular. Não havendo
 * determinação, o art. 1º da Lei de Introdução às Normas do Direito Brasileiro
 * - LINDB (anteriormente chamada de Lei de Introdução ao Código Civil)
 * determina 45 dias. O intervalo entre a data de sua publicação e sua entrada
 * em vigor chama-se vacatio legis. Uma lei deve ser aplicada até que seja
 * revogada ou modificada por outra (no Brasil, este princípio está positivado
 * no art. 2º da LINDB). @see <a href="http://pt.wikipedia.org/wiki/Lei>wikipedia.org/wiki/Lei</a>
 * A revogação gera efeitos - EX NUNC - ou seja, a partir da sua declaração. Não
 * retroage. A anulação gera efeitos EX TUNC (retroage à data de início dos
 * efeitos do ato).
 *
 * @author gilberto.andrade
 */
@Entity
@Table(name = "ato_legal", schema = "sapeo", uniqueConstraints = @UniqueConstraint(name = "ato_legal_uk",columnNames = {"veiculo_publicacao_id", "nu_ato_legal", "tipo"}))
public class AtoLegal implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ato_legal_id_seq")
    @SequenceGenerator(schema = "sapeo", name = "ato_legal_id_seq", sequenceName = "ato_legal_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "tipo")
    @Enumerated(EnumType.STRING)
    private AtoLegalTipoEnum tipo;

    @Column(name = "nu_ato_legal")
    @Basic(optional = false)
    private String nuAtoLegal;

    @Column(name = "dt_ato_legal")
    @Temporal(TemporalType.DATE)
    @Basic(optional = false)
    private Date dtAtoLegal;

    @Column(name = "dt_entra_em_vigor")
    @Temporal(TemporalType.DATE)
    @Basic(optional = false)
    private Date dtEntraEmVigor;

    @Column(name = "dt_efeito_juridico")
    @Temporal(TemporalType.DATE)
    private Date dtEfeitoJuridico;

    @Column(name = "autoridade")
    @Enumerated(EnumType.STRING)
    private AutoridadeEnum autoridade;

    @JoinColumn(name = "veiculo_publicacao_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private VeiculoPublicacao veiculoPublicacao;

    public AtoLegal() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AtoLegalTipoEnum getTipo() {
        return tipo;
    }

    public void setTipo(AtoLegalTipoEnum tipo) {
        this.tipo = tipo;
    }

    public String getNuAtoLegal() {
        return nuAtoLegal;
    }

    public void setNuAtoLegal(String nuAtoLegal) {
        this.nuAtoLegal = nuAtoLegal;
    }

    public Date getDtAtoLegal() {
        return dtAtoLegal;
    }

    public void setDtAtoLegal(Date dtAtoLegal) {
        this.dtAtoLegal = dtAtoLegal;
    }

    public Date getDtEntraEmVigor() {
        return dtEntraEmVigor;
    }

    public void setDtEntraEmVigor(Date dtEntraEmVigor) {
        this.dtEntraEmVigor = dtEntraEmVigor;
    }

    public Date getDtEfeitoJuridico() {
        return dtEfeitoJuridico;
    }

    public void setDtEfeitoJuridico(Date dtEfeitoJuridico) {
        this.dtEfeitoJuridico = dtEfeitoJuridico;
    }

    public AutoridadeEnum getAutoridade() {
        return autoridade;
    }

    public void setAutoridade(AutoridadeEnum autoridade) {
        this.autoridade = autoridade;
    }

    public VeiculoPublicacao getVeiculoPublicacao() {
        return veiculoPublicacao;
    }
    /**
     * Nome amigável para o ato legal.
     *
     * @return tipo-nuAtoLegal
     */
    public String getNome() {
        StringBuilder builder = new StringBuilder();
        builder.append(getTipo().label);
        builder.append("-");
        builder.append(getNuAtoLegal());
        return builder.toString();
    }
    /**
     * Conveniente método para retornar a data de publicação de um Veiculo de
     * Publicação.
     *
     * @return
     */
    public Date getDtPublicacao() {
        return veiculoPublicacao.getDtPublicacao();
    }

    /**
     * A common problem with bi-directional relationships is the application
     * updates one side of the relationship, but the other side does not get
     * updated, and becomes out of sync.
     *
     * @param veiculoPublicacao
     */
    public void setVeiculoPublicacao(VeiculoPublicacao veiculoPublicacao) {
        this.veiculoPublicacao = veiculoPublicacao;
        if (!veiculoPublicacao.getAtosLegais().contains(this)) {
            veiculoPublicacao.getAtosLegais().add(this);
        }
    }

    @Override
    public boolean equals(Object other) {
        return (other instanceof AtoLegal) && (id != null)
                ? id.equals(((AtoLegal) other).id)
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
        builder.append("AtoLegal [");
        builder.append("id=").append(id);        
        builder.append(", autoridade=").append(autoridade);
        builder.append(", dtAtoLegal=").append(dtAtoLegal);
        builder.append(", dtEfeitoJuridico=").append(dtEfeitoJuridico);
        builder.append(", dtEntraEmVigor=").append(dtEntraEmVigor);
        builder.append(", nuAtoLegal=").append(nuAtoLegal);
        builder.append(", tipo=").append(tipo);
        builder.append(", veiculoPublicacao=").append(veiculoPublicacao != null ? veiculoPublicacao.getId() : "null");
        builder.append("]");
        return builder.toString();
    }

}
