package mydomain.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "entidade", schema = "sapeo", uniqueConstraints = @UniqueConstraint(name = "entidade_uk",columnNames = {"nome", "sigla", "ato_legal_inicio_id"}))
public class Entidade implements Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(Entidade.class);
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "entidade_id_seq")
    @SequenceGenerator(schema = "sapeo", name = "entidade_id_seq", sequenceName = "entidade_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Basic(optional = false)
    @Column(name = "nome")
    private String nome;

    @Basic(optional = false)
    @Column(name = "sigla")
    private String sigla;

    @Column(name = "cnpj")
    private String cnpj;

    @Column(name = "eh_administracao_direta")
    @Basic(optional = false)
    private boolean ehAdministracaoDireta;

    @Column(name = "dt_entra_em_vigor")
    @Temporal(TemporalType.DATE)
    @Basic(optional = false)
    private Date dtEntraEmVigor;

    @JoinColumn(name = "ato_legal_inicio_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private AtoLegal atoLegalInicio;

    @Column(name = "dt_efeito_juridico")
    @Temporal(TemporalType.DATE)
    private Date dtEfeitoJuridico;

    @JoinColumn(name = "ato_legal_termino_id", referencedColumnName = "id")
    @ManyToOne
    private AtoLegal atoLegalTermino;

    @OneToMany(mappedBy = "entidade")
    private List<Redistribuicao> redistribuicoes = new ArrayList<Redistribuicao>();
    
    @OneToMany(mappedBy = "entidade", cascade = CascadeType.ALL)
    private List<EstruturaOperacional> estruturasOperacionais = new ArrayList<EstruturaOperacional>();

    public Entidade() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSigla() {
        return (this.sigla != null) ? this.sigla.toUpperCase() : null;
    }

    public void setSigla(String aSigla) {
        this.sigla = (aSigla != null) ? aSigla.toUpperCase() : null;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public boolean getEhAdministracaoDireta() {
        return ehAdministracaoDireta;
    }

    public void setEhAdministracaoDireta(boolean ehAdministracaoDireta) {
        this.ehAdministracaoDireta = ehAdministracaoDireta;
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
        LOGGER.debug("FINALIZA Entidade com ato final: {}",atoLegalTermino) ;
        Validate.notNull(atoLegalTermino, "AtoLegalTermino Nulo");
        finalizarEstruturasOperacionais(atoLegalTermino);        
        this.atoLegalTermino = atoLegalTermino;
    }

    public AtoLegal getAtoLegalInicio() {
        return atoLegalInicio;
    }

    public void setAtoLegalInicio(AtoLegal atoLegalInicio) {
        this.atoLegalInicio = atoLegalInicio;
    }

    /**
     * Todas as Redistribuicoes.
     *
     * @return List redistribuicoes ocupadas e não ocupadas
     */
    public List<Redistribuicao> getRedistribuicoes() {
        return Collections.unmodifiableList(this.redistribuicoes);
    }

    public int countRedistribuicoes() {
        if (this.getRedistribuicoes().isEmpty()) {
            return 0;
        }
        return getRedistribuicoes().size();
    }

    /**
     * Redistribuicoes não finalizadas
     *
     * @return List redistribuicoes disponíveis
     */
    public final List<Redistribuicao> getRedistribuicoesDisponiveis() {
        if (this.redistribuicoes.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        List<Redistribuicao> redistribuicoesDiponiveis = new ArrayList<Redistribuicao>(2);

        for (Redistribuicao e : getRedistribuicoes()) {
            if (e.isStatusFinalizado()==false) {
                redistribuicoesDiponiveis.add(e);
            }
        }
        return Collections.unmodifiableList(redistribuicoesDiponiveis);
    }
    
    /**
     * Finaliza todas as estrutura-operacionais não finalizadas. 
     * TODO:Verificar as consequências em Vagas(e dependentes).
     *
     * @param atoLegalTermino
     */
    protected void finalizarEstruturasOperacionais(AtoLegal atoLegalTermino) {
        // Standard iterator sufficient for altering elements
        Iterator<EstruturaOperacional> iterator = estruturasOperacionais.iterator();

        while (iterator.hasNext()) {
            EstruturaOperacional e = iterator.next();
            LOGGER.debug("finalizando estrutura-operacional: {}", e);            
            e.setAtoLegalTermino(atoLegalTermino);
        }
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
        Collections.sort(estruturasOperacionaisDiponiveis, EstruturaOperacional.ORDEM_IMPORTANCIA_COMPARATOR);
        return Collections.unmodifiableList(estruturasOperacionaisDiponiveis);
    }
    
    public int countEstruturasOperacionaisDisponiveis() {
        if (this.getEstruturasOperacionaisDisponiveis().isEmpty()) {
            return 0;
        }
        return getEstruturasOperacionaisDisponiveis().size();
    }
    
    public final List<EstruturaOperacional> getEstruturasOperacionaisFinalizadas() {
        if (this.estruturasOperacionais.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        List<EstruturaOperacional> estruturasOperacionaisFinalizadas = new ArrayList<EstruturaOperacional>(2);

        for (EstruturaOperacional v : getEstruturasOperacionais()) {
            if (v.isStatusFinalizado()) {
                estruturasOperacionaisFinalizadas.add(v);
            }
        }
        return Collections.unmodifiableList(estruturasOperacionaisFinalizadas);
    }
    
    /**
     * Um list iterator de estruturasOperacionais Permite subclasses editar a
     * lista
     *
     * @return ListIterator
     */
    protected ListIterator getEstruturasOperacionaisList() {
        return this.estruturasOperacionais.listIterator();
    }

    public void addEstruturaOperacional(EstruturaOperacional line) {
        estruturasOperacionais.add(line);
    }

    public void removeEstruturaOperacional(EstruturaOperacional line) {
        estruturasOperacionais.remove(line);
    }

    public boolean isStatusFinalizado() {
        try {
            this.atoLegalTermino.getId();
        } catch (NullPointerException ex) {
            LOGGER.debug("Entidade NÃO FINALIZADA - AtoLegalTermino: {}", atoLegalTermino);
            return false;
        }
        LOGGER.debug("Entidade FINALIZADA - AtoLegalTermino: {}", atoLegalTermino);
        return true;
    }
    /**
     * Custo potencial total por entidade
     * @return BigDecimal O custo total
     */
    public BigDecimal getCusto() {
        BigDecimal total = BigDecimal.ZERO;
        if (this.getEstruturasOperacionaisDisponiveis().isEmpty()) {
            return BigDecimal.ZERO;
        }   
        for (EstruturaOperacional estruturaOperacional : this.getEstruturasOperacionaisDisponiveis()) {
            total = total.add(estruturaOperacional.getCusto());
        }
        return total;
    }
    /**
     * Custo potencial com vagas providas por entidade
     * @return BigDecimal O custo total
     */    
    public BigDecimal getCustoVagasProvidas() {
        BigDecimal total = BigDecimal.ZERO;
        if (this.getEstruturasOperacionaisDisponiveis().isEmpty()) {
            return BigDecimal.ZERO;
        }   
        for (EstruturaOperacional estruturaOperacional : this.getEstruturasOperacionaisDisponiveis()) {
            total = total.add(estruturaOperacional.getCustoVagasProvidas());
        }
        return total;
    }
    
    @Override
    public boolean equals(Object other) {
        return (other instanceof Entidade) && (id != null)
                ? id.equals(((Entidade) other).id)
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
        builder.append("Entidade [");
        builder.append("id=").append(id);
        builder.append(", nome=").append(nome);
        builder.append(", sigla=").append(sigla);
        builder.append(", cnpj=").append(cnpj);
        builder.append(", dtEfeitoJuridico=").append(dtEfeitoJuridico);
        builder.append(", dtEntraEmVigor=").append(dtEntraEmVigor);
        builder.append(", ehAdministracaoDireta=").append(ehAdministracaoDireta);
        builder.append(", atoLegalInicio=").append(atoLegalInicio != null ? atoLegalInicio.getId() : "null");
        builder.append(", atoLegalTermino=").append(atoLegalTermino != null ? atoLegalTermino.getId() : "null");
        builder.append(", estruturasOperacionais=").append(estruturasOperacionais != null ? estruturasOperacionais.size() : -1);
        builder.append("]");
        return builder.toString();
    }

}
