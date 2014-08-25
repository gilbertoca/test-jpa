package mydomain.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author gilberto.andrade
 */
@Entity
@Table(name = "denominacao_cargo", schema = "sapeo", uniqueConstraints = @UniqueConstraint(name = "denominacao_cargo_uk",columnNames = {"nome", "simbolo_nivel", "ato_legal_inicio_id"}))
public class DenominacaoCargo implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(DenominacaoCargo.class);
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "denominacao_cargo_id_seq")
    @SequenceGenerator(schema = "sapeo", name = "denominacao_cargo_id_seq", sequenceName = "denominacao_cargo_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Basic(optional = false)
    @Column(name = "agrupamento")
    @Enumerated(EnumType.STRING)
    private AgrupamentoEnum agrupamento;

    @Basic(optional = false)
    @Column(name = "nome")
    private String nome;

    @Column(name = "simbolo_nivel")
    @Basic(optional = false)
    private String simboloNivel;

    @Column(name = "ordem_importancia")
    @Basic(optional = false)
    private String ordemImportancia;

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

    @OneToMany(mappedBy = "denominacaoCargo")
    private List<EstruturaOperacional> estruturasOperacionais = new ArrayList<EstruturaOperacional>();
    
    @OneToMany(mappedBy = "denominacaoCargo", cascade = CascadeType.ALL)
    private List<DenominacaoCargoDetalhe> denominacoesCargoDetalhes = new ArrayList<DenominacaoCargoDetalhe>();

    public DenominacaoCargo() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AgrupamentoEnum getAgrupamento() {
        return agrupamento;
    }

    public void setAgrupamento(AgrupamentoEnum agrupamento) {
        this.agrupamento = agrupamento;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSimboloNivel() {
        return simboloNivel;
    }

    public void setSimboloNivel(String simboloNivel) {
        this.simboloNivel = simboloNivel;
    }

    public String getOrdemImportancia() {
        return ordemImportancia;
    }

    public void setOrdemImportancia(String ordemImportancia) {
        this.ordemImportancia = ordemImportancia;
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

    public AtoLegal getAtoLegalTermino() {
        return atoLegalTermino;
    }

    public void setAtoLegalTermino(AtoLegal atoLegalTermino) {
        LOGGER.debug("FINALIZA Denominação de Cargo com ato final: {}", atoLegalTermino);
        Validate.notNull(atoLegalTermino, "AtoLegalTermino Nulo");
        finalizarDenominacaoCargoDetalhe(atoLegalTermino);
        this.atoLegalTermino = atoLegalTermino;
    }

    public AtoLegal getAtoLegalInicio() {
        return atoLegalInicio;
    }

    public void setAtoLegalInicio(AtoLegal atoLegalInicio) {
        this.atoLegalInicio = atoLegalInicio;
    }

    /**
     * Todas as EstruturasOperacionais.
     *
     * @return List estruturasOperacionais ocupadas e não ocupadas
     */
    public List<EstruturaOperacional> getEstruturasOperacionais() {
        return Collections.unmodifiableList(this.estruturasOperacionais);
    }

    public int countEstruturasOperacionais() {
        if (this.getEstruturasOperacionais().isEmpty()) {
            return 0;
        }
        return getEstruturasOperacionais().size();
    }

    /**
     * EstruturasOperacionais não finalizadas
     *
     * @return List estruturasOperacionais disponíveis
     */
    public final List<EstruturaOperacional> getEstruturasOperacionaisDisponiveis() {
        if (this.estruturasOperacionais.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        List<EstruturaOperacional> estruturasOperacionaisDiponiveis = new ArrayList<EstruturaOperacional>(2);

        for (EstruturaOperacional e : getEstruturasOperacionais()) {
            if (e.isStatusFinalizado()==false) {
                estruturasOperacionaisDiponiveis.add(e);
            }
        }
        return Collections.unmodifiableList(estruturasOperacionaisDiponiveis);
    }

    
    /**
     * Finaliza a denominação de cargo detalhe corrente(não finalizada).
     *
     * @param atoLegalTermino
     */
    protected void finalizarDenominacaoCargoDetalhe(AtoLegal atoLegalTermino) {
        DenominacaoCargoDetalhe n = getDenominacaoCargoDetalhe();
        LOGGER.debug("finalizando DenominacaoCargoDetalhe: {}", n);
        n.setAtoLegalTermino(atoLegalTermino);
    }

    public boolean isStatusFinalizado() {
        try {
            this.atoLegalTermino.getId();
        } catch (NullPointerException ex) {
            return false;
        }
        return true;
    }

    /**
     * Retorna o último DenominacaoCargoDetalhe inserido. List mantém a ordem de inserção
     *
     * @return DenominacaoCargoDetalhe or Null
     */
    public DenominacaoCargoDetalhe getDenominacaoCargoDetalhe() {
        if (this.denominacoesCargoDetalhes == null || this.denominacoesCargoDetalhes.isEmpty()) {
            return null;
        }
        
        return this.denominacoesCargoDetalhes.get(this.denominacoesCargoDetalhes.size() - 1);
    }

    /**
     * Denominação Cargo Detalhe não finalizada. Observação, apesar de ser uma
     * lista(que mantém histórico), haverá apenas um não finalizado(@see
     * getDenominacaoCargoDetalhe()).
     *
     * @return
     */
    public List<DenominacaoCargoDetalhe> getDenominacoesCargoDetalhesDisponiveis() {
        final List<DenominacaoCargoDetalhe> disponiveis = new ArrayList<DenominacaoCargoDetalhe>();
        for (DenominacaoCargoDetalhe v : getDenominacoesCargoDetalhes()) {
            if (null == v.getAtoLegalTermino()) {
                disponiveis.add(v);
            }
        }
        return Collections.unmodifiableList(disponiveis);
    }

    public final List<DenominacaoCargoDetalhe> getDenominacoesCargoDetalhesFinalizadas() {
        if (this.denominacoesCargoDetalhes.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        List<DenominacaoCargoDetalhe> denominacoesCargoDetalhesFinalizadas = new ArrayList<DenominacaoCargoDetalhe>(2);

        for (DenominacaoCargoDetalhe n : getDenominacoesCargoDetalhes()) {
            if (n.isStatusFinalizado()) {
                denominacoesCargoDetalhesFinalizadas.add(n);
            }
        }
        return Collections.unmodifiableList(denominacoesCargoDetalhesFinalizadas);
    }

    public List<DenominacaoCargoDetalhe> getDenominacoesCargoDetalhes() {
        return Collections.unmodifiableList(this.denominacoesCargoDetalhes);
    }

    public void addDenominacaoCargoDetalhe(DenominacaoCargoDetalhe line) {
        LOGGER.debug("addDenominacaoCargoDetalhe {} para Denominação de cargo {}", line, this);
        Validate.notNull(line, "Não adicionar DenominacaoCargoDetalhe Nula");
        //Validate.isFalse(isStatusFinalizado(), "Denominação Cargo já finalizada!");
        if (isStatusFinalizado()) {
            throw new IllegalArgumentException("Denominação Cargo já finalizada!");
        }
        //Criação de um Objeto DenominacaoCargo?
        if (!getDenominacoesCargoDetalhes().isEmpty()) {
            LOGGER.debug("addDenominacaoCargoDetalhe (DenominacaoCargoDetalhe Antigo) {} (DenominacaoCargoDetalhe Novo) {}", getDenominacaoCargoDetalhe(), line);
            finalizarDenominacaoCargoDetalhe(line.getAtoLegalInicio());
        }
        line.setDenominacaoCargo(this);
        this.denominacoesCargoDetalhes.add(line);
    }

    public void removeDenominacaoCargoDetalhe(DenominacaoCargoDetalhe line) {
        Validate.notNull(line, "Não adicionar DenominacaoCargoDetalhe Nula");
        denominacoesCargoDetalhes.remove(line);
        setAtoLegalTermino(null);
    }

    public void revogarDenominacaoCargoDetalhe(DenominacaoCargoDetalhe line) {
        Validate.notNull(line, "Não adicionar DenominacaoCargoDetalhe Nula");
        Validate.notNull(line.getAtoLegalTermino(), "DenominacaoCargoDetalhe não finalizada(revogada)!");
        setAtoLegalTermino(null);
    }

    @Override
    public boolean equals(Object other) {
        return (other instanceof DenominacaoCargo) && (id != null)
                ? id.equals(((DenominacaoCargo) other).id)
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
        builder.append("DenominacaoCargo [");
        builder.append("id=").append(id);
        builder.append(", nome=").append(nome);
        builder.append(", simboloNivel=").append(simboloNivel);
        builder.append(", ordemImportancia=").append(ordemImportancia);
        builder.append(", denominacoesCargoDetalhes=").append(denominacoesCargoDetalhes != null ? denominacoesCargoDetalhes.size() : -1);
        builder.append(". agrupamento=").append(agrupamento);
        builder.append(", atoLegalInicio=").append(atoLegalInicio != null ? atoLegalInicio.getId() : "null");
        builder.append(", atoLegalTermino=").append(atoLegalTermino != null ? atoLegalTermino.getId() : "null");
        builder.append(", dtEfeitoJuridico=").append(dtEfeitoJuridico);
        builder.append(", dtEntraEmVigor=").append(dtEntraEmVigor);
        builder.append("]");
        return builder.toString();
    }
}
