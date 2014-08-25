package mydomain.model;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "denominacao_cargo_detalhe", schema = "sapeo", uniqueConstraints = @UniqueConstraint(name = "denominacao_cargo_detalhe_uk",columnNames = {"denominacao_cargo_id", "ato_legal_inicio_id"}))
public class DenominacaoCargoDetalhe implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "denominacao_cargo_detalhe_id_seq")
    @SequenceGenerator(schema = "sapeo", name = "denominacao_cargo_detalhe_id_seq", sequenceName = "denominacao_cargo_detalhe_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "remuneracao", precision = 12, scale = 2)
    private BigDecimal remuneracao = BigDecimal.ZERO;

    @Column(name = "gratificacao", precision = 12, scale = 2)
    private BigDecimal gratificacao = BigDecimal.ZERO;
    
    @Column(name = "percentual_acrescimo_efetivo")
    private float percentualAcrescimoEfetivo;

    @Column(name = "quantitativo_amplo")
    private int quantitativoAmplo;

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

    @JoinColumn(name = "denominacao_cargo_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private DenominacaoCargo denominacaoCargo;

    @JoinColumn(name = "ato_legal_termino_id", referencedColumnName = "id")
    @ManyToOne
    private AtoLegal atoLegalTermino;

    public DenominacaoCargoDetalhe() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setQuantitativoAmplo(int quantitativoAmplo) {
        this.quantitativoAmplo = quantitativoAmplo;
    }

    public int getQuantitativoAmplo() {
        return this.quantitativoAmplo;
    }

    public BigDecimal getRemuneracao() {
        return (this.remuneracao == null ? BigDecimal.ZERO : this.remuneracao);
    }

    public BigDecimal getGratificacao() {
        return (this.gratificacao == null ? BigDecimal.ZERO : this.gratificacao);
    }

    public void setGratificacao(BigDecimal gratificacao) {
        this.gratificacao = gratificacao;
    }

    public void setRemuneracao(BigDecimal remuneracao) {
        this.remuneracao = remuneracao;
    }

    public Date getDtEntraEmVigor() {
        return this.dtEntraEmVigor;
    }

    public void setDtEntraEmVigor(Date dtEntraEmVigor) {
        this.dtEntraEmVigor = dtEntraEmVigor;
    }

    public Date getDtEfeitoJuridico() {
        return this.dtEfeitoJuridico;
    }

    public void setDtEfeitoJuridico(Date dtEfeitoJuridico) {
        this.dtEfeitoJuridico = dtEfeitoJuridico;
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

    public DenominacaoCargo getDenominacaoCargo() {
        return denominacaoCargo;
    }

    public void setDenominacaoCargo(DenominacaoCargo denominacaoCargo) {
        this.denominacaoCargo = denominacaoCargo;
    }

    public float getPercentualAcrescimoEfetivo() {
        return percentualAcrescimoEfetivo;
    }

    public void setPercentualAcrescimoEfetivo(float percentualAcrescimoEfetivo) {
        this.percentualAcrescimoEfetivo = percentualAcrescimoEfetivo;
    }

    public boolean isStatusFinalizado() {
        try {
            this.atoLegalTermino.getId();
        } catch (NullPointerException ex) {
            return false;
        }
        return true;
    }
  
    public boolean isBefore(Date aDate)
    { 
        return this.dtEntraEmVigor.before(aDate);
    }
    
    public boolean isAfter(Date aDate)
    { 
        return this.dtEntraEmVigor.after(aDate);
    }    
    @Override
    public boolean equals(Object other) {
        return (other instanceof DenominacaoCargoDetalhe) && (id != null)
                ? id.equals(((DenominacaoCargoDetalhe) other).id)
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
        builder.append("DenominacaoCargoDetalhe [");
        builder.append("id=").append(id);
        builder.append(", quantitativoAmplo=").append(quantitativoAmplo);
        builder.append(", remuneracao=").append(remuneracao);
        builder.append(", gratificacao=").append(gratificacao);
        builder.append(", percentualAcrescimoEfetivo=").append(percentualAcrescimoEfetivo);
        builder.append(", atoLegalInicio=").append(atoLegalInicio != null ? atoLegalInicio.getId() : "null");
        builder.append(", atoLegalTermino=").append(atoLegalTermino != null ? atoLegalTermino.getId() : "null");
        builder.append(", denominacaoCargo=").append(denominacaoCargo != null ? denominacaoCargo.getId() : "null");
        builder.append(", dtEfeitoJuridico=").append(dtEfeitoJuridico);
        builder.append(", dtEntraEmVigor=").append(dtEntraEmVigor);
        builder.append("]");
        return builder.toString();
    }

}
