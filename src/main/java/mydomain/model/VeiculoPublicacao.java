package mydomain.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
@Table(name = "veiculo_publicacao", schema = "sapeo", uniqueConstraints = @UniqueConstraint(name = "veiculo_publicacao_uk",columnNames = {"nu_publicacao", "tipo"}))
public class VeiculoPublicacao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "veiculo_publicacao_id_seq")
    @SequenceGenerator(schema = "sapeo", name = "veiculo_publicacao_id_seq", sequenceName = "veiculo_publicacao_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "nu_publicacao")
    @Basic(optional = false)
    private String nuPublicacao;

    @Column(name = "tipo")
    @Enumerated(EnumType.STRING)
    private VeiculoPublicacaoTipoEnum tipo;

    @Column(name = "dt_publicacao")
    @Temporal(TemporalType.DATE)
    @Basic(optional = false)
    private Date dtPublicacao;

    @OneToMany(mappedBy = "veiculoPublicacao", cascade = CascadeType.ALL)
    private List<AtoLegal> atosLegais = new ArrayList<AtoLegal>();

    public VeiculoPublicacao() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNuPublicacao() {
        return nuPublicacao;
    }

    public void setNuPublicacao(String nuPublicacao) {
        this.nuPublicacao = nuPublicacao;
    }

    public VeiculoPublicacaoTipoEnum getTipo() {
        return tipo;
    }

    public void setTipo(VeiculoPublicacaoTipoEnum tipo) {
        this.tipo = tipo;
    }

    public Date getDtPublicacao() {
        return dtPublicacao;
    }

    public void setDtPublicacao(Date dtPublicacao) {
        this.dtPublicacao = dtPublicacao;
    }

    public List<AtoLegal> getAtosLegais() {
        return atosLegais;
    }

    public void setAtosLegais(List<AtoLegal> atosLegais) {
        this.atosLegais = atosLegais;
    }

    /**
     * A common problem with bi-directional relationships is the application
     * updates one side of the relationship, but the other side does not get
     * updated, and becomes out of sync.
     *
     * @param atoLegal
     */
    public void addAtoLegal(AtoLegal atoLegal) {
        this.atosLegais.add(atoLegal);
        if (atoLegal.getVeiculoPublicacao() != this) {
            atoLegal.setVeiculoPublicacao(this);
        }
    }

    /**
     * Nome amigável para o veículo de publicação.
     *
     * @return tipo-nuPublicacao
     */
    public String getNome() {
        StringBuilder builder = new StringBuilder();
        builder.append(getTipo());
        builder.append("-");
        builder.append(getNuPublicacao());
        return builder.toString();
    }

    @Override
    public boolean equals(Object other) {
        return (other instanceof VeiculoPublicacao) && (id != null)
                ? id.equals(((VeiculoPublicacao) other).id)
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
        builder.append("VeiculoPublicacao [");
        builder.append("id=").append(id);
        builder.append(", nuPublicacao=").append(nuPublicacao);
        builder.append(", tipo=").append(tipo);
        builder.append(", dtPublicacao=").append(dtPublicacao);
        builder.append(", atosLegais=").append(atosLegais != null ? atosLegais.size() : -1);
        builder.append("]");
        return builder.toString();
    }

}
