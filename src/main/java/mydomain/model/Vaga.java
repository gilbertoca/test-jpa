package mydomain.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
 * Representação concreta de um item(vaga ou cargo?) da Denominação de Cargo
 * definida na Estrutura Operacional de uma Entidade(Estrutura Organizacional).
 * A Vaga(poderia ser cargo, não?) é provida através de uma Nomeação. A Vaga
 * sabe qual Nomeação é corrente e tem seu histórico de nomeções. A inexistência
 * de uma Nomeação significa que a Vaga não está ocupada. Para
 * identificar(atributo nome) uma Vaga usaremos o minemônico: entidade.SIGLA
 * denominacaoCargo.NOME denominacaoCargo.SIMBOLONIVEL
 * estruturaOperacional.QUANTITATIVOESPECIFICO SEQUENCIA NUMERICA
 *
 * @author gilberto.andrade
 */
@Entity
@Table(name = "vaga", schema = "sapeo", uniqueConstraints = @UniqueConstraint(name = "vaga_uk", columnNames = {"estrutura_operacional_id", "ato_legal_inicio_id", "nome"}))
public class Vaga implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Vaga.class);

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vaga_id_seq")
    @SequenceGenerator(schema = "sapeo", name = "vaga_id_seq", sequenceName = "vaga_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "nome")
    @Basic(optional = false)
    private String nome;

    @Column(name = "denominacao_especificacao")
    private String denominacaoEspecificacao;

    @Column(name = "eh_provido")
    @Basic(optional = false)
    private boolean ehProvido;

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

    @JoinColumn(name = "estrutura_operacional_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private EstruturaOperacional estruturaOperacional;

    @OneToMany(mappedBy = "vaga", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Nomeacao> nomeacoes = new ArrayList<Nomeacao>();
    /**
     * Ordenação por nome
     */
    public static final Comparator<Vaga> NOME_COMPARATOR = new Comparator<Vaga>() {

        @Override
        public int compare(Vaga o1, Vaga o2) {
            return o1.nome.compareTo(o2.nome);
        }

    };

    public Vaga() {
    }

    public Vaga(String denominacaoEspecificacao, boolean ehProvido, Date dtEntraEmVigor, AtoLegal atoLegalInicio, Date dtEfeitoJuridico, AtoLegal atoLegalTermino, EstruturaOperacional estruturaOperacional) {
        this.denominacaoEspecificacao = denominacaoEspecificacao;
        this.ehProvido = ehProvido;
        this.dtEntraEmVigor = dtEntraEmVigor;
        this.atoLegalInicio = atoLegalInicio;
        this.dtEfeitoJuridico = dtEfeitoJuridico;
        this.atoLegalTermino = atoLegalTermino;
        this.estruturaOperacional = estruturaOperacional;
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

    public String getDenominacaoEspecificacao() {
        return denominacaoEspecificacao;
    }

    public void setDenominacaoEspecificacao(String denominacaoEspecificacao) {
        this.denominacaoEspecificacao = denominacaoEspecificacao;
    }

    public boolean getEhProvido() {
        return ehProvido;
    }

    public void setEhProvido(boolean ehProvido) {
        this.ehProvido = ehProvido;
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

    public EstruturaOperacional getEstruturaOperacional() {
        return estruturaOperacional;
    }

    public void setEstruturaOperacional(EstruturaOperacional aEstruturaOperacional) {
//        if (this.estruturaOperacional != null) {
//            this.estruturaOperacional.internalRemoveVaga(this);
//        }
        this.estruturaOperacional = aEstruturaOperacional;
//        if (aEstruturaOperacional != null) {
//            aEstruturaOperacional.internalAddVaga(this);
//        }
    }

    public AtoLegal getAtoLegalTermino() {
        return atoLegalTermino;
    }

    /**
     * Finaliza a Vaga e sua(s) nomeação(ões).
     *
     * @param atoLegalTermino
     */
    public void setAtoLegalTermino(AtoLegal atoLegalTermino) {
        LOGGER.debug("FINALIZA Vaga com ato final: {}", atoLegalTermino);
        Validate.notNull(atoLegalTermino, "AtoLegalTermino Nulo");
        finalizarNomeacoes(atoLegalTermino);
        this.atoLegalTermino = atoLegalTermino;
    }

    public AtoLegal getAtoLegalInicio() {
        return atoLegalInicio;
    }

    public void setAtoLegalInicio(AtoLegal atoLegalInicio) {
        this.atoLegalInicio = atoLegalInicio;
    }

    /**
     * Finaliza todas as nomeações não finalizadas. TODO:Verificar as
     * consequências em demissões e exonerações.
     *
     * @param atoLegalTermino
     */
    protected void finalizarNomeacoes(AtoLegal atoLegalTermino) {
        // Standard iterator sufficient for altering elements
        Iterator<Nomeacao> iterator = nomeacoes.iterator();

        while (iterator.hasNext()) {
            Nomeacao n = iterator.next();
            LOGGER.debug("finalizando nomeacao: {}", n);
            n.setAtoLegalTermino(atoLegalTermino);
        }
    }

    /**
     * Todas as nomeações.
     *
     * @return List nomeacoes finalizadas e não finalizadas
     */
    public List<Nomeacao> getNomeacoes() {
        return Collections.unmodifiableList(nomeacoes);
    }

    /**
     * Todas as nomeações
     *
     * @return
     */
    public int countNomeacoes() {
        return getNomeacoes().size();
    }

    /**
     * Nomeações disponíveis(não finalizadas)
     *
     * @return List nomeacaos disponíveis
     */
    public final List<Nomeacao> getNomeacoesDisponiveis() {
        if (this.nomeacoes.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        List<Nomeacao> nomeacoesDisponiveis = new ArrayList<Nomeacao>(2);

        for (Nomeacao n : getNomeacoes()) {
            if (n.isStatusFinalizado() == false) {
                nomeacoesDisponiveis.add(n);
            }
        }
        return Collections.unmodifiableList(nomeacoesDisponiveis);
    }
    /**
     * Número de Nomeações disponíveis(não finalizadas)
     *
     * @return int
     */
    public int countNomeacoesDisponiveis() {
        if (this.getNomeacoesDisponiveis().isEmpty()) {
            return 0;
        }
        return getNomeacoesDisponiveis().size();
    }
    /**
     * Nomeações disponíveis(não finalizadas) e Redistribuidas
     *
     * @return List nomeacaos disponíveis
     */
    public final List<Nomeacao> getNomeacoesDisponiveisERedistribuidas() {
        if (this.nomeacoes.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        List<Nomeacao> nomeacoesDisponiveisERedistribuidas = new ArrayList<Nomeacao>(2);

        for (Nomeacao n : getNomeacoes()) {
            if (n.isStatusRedistribuido()) {
                nomeacoesDisponiveisERedistribuidas.add(n);
            }
        }
        return Collections.unmodifiableList(nomeacoesDisponiveisERedistribuidas);
    }
    /**
     * Número de Nomeações disponíveis(não finalizadas) e Redistribuidas
     *
     * @return int
     */
    public int countNomeacoesDisponiveisERedistribuidas() {
        if (this.getNomeacoesDisponiveisERedistribuidas().isEmpty()) {
            return 0;
        }
        return getNomeacoesDisponiveisERedistribuidas().size();
    }

    /**
     * Nomeações finalizadas
     *
     * @return List nomeacaos finalizadas
     */    
    public final List<Nomeacao> getNomeacoesFinalizadas() {
        if (this.nomeacoes.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        List<Nomeacao> nomeacoesFinalizadas = new ArrayList<Nomeacao>(2);

        for (Nomeacao n : getNomeacoes()) {
            if (n.isStatusFinalizado()) {
                nomeacoesFinalizadas.add(n);
            }
        }
        return Collections.unmodifiableList(nomeacoesFinalizadas);
    }
    
    /**
     * Número de Nomeações finalizadas
     *
     * @return int
     */
    public int countNomeacoesFinalizadas() {
        if (this.getNomeacoesFinalizadas().isEmpty()) {
            return 0;
        }
        return getNomeacoesFinalizadas().size();
    }
    

    /**
     * Um list iterator de nomeacoes Permite subclasses editar a lista
     *
     * @return ListIterator
     */
    protected ListIterator getNomeacoesList() {
        return this.nomeacoes.listIterator();
    }

    /**
     * Adiciona uma nomeação, tornando a vaga corrente provida
     *
     * @param line
     */
    public void addNomeacao(Nomeacao line) {
        if (!nomeacoes.contains(line)) {
            nomeacoes.add(line);
            setEhProvido(true);
            LOGGER.debug("Vinculo entre Vaga{} e Nomeação{} estabelecido! Vaga considerada PROVIDA.", this, line);
        }
    }

    public void removeNomeacao(Nomeacao line) {
        nomeacoes.remove(line);
    }

    public boolean isStatusFinalizado() {
        try {
            this.atoLegalTermino.getId();
        } catch (NullPointerException ex) {
            return false;
        }
        return true;
    }

    public Entidade ondeEsta() {
        return getEstruturaOperacional().getEntidade();
    }

    @Override
    public boolean equals(Object other) {
        return (other instanceof Vaga) && (id != null)
                ? id.equals(((Vaga) other).id)
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
        builder.append("Vaga [");
        builder.append("id=").append(id);
        builder.append(", nome=").append(nome);
        builder.append(", ehProvido=").append(ehProvido);
        builder.append(", estruturaOperacional=").append(estruturaOperacional != null ? estruturaOperacional.getId() : "null");
        builder.append(", denominacaoEspecificacao=").append(denominacaoEspecificacao);
        builder.append(", dtEfeitoJuridico=").append(dtEfeitoJuridico);
        builder.append(", dtEntraEmVigor=").append(dtEntraEmVigor);
        builder.append(", atoLegalInicio=").append(atoLegalInicio != null ? atoLegalInicio.getId() : "null");
        builder.append(", atoLegalTermino=").append(atoLegalTermino != null ? atoLegalTermino.getId() : "null");
        builder.append(", nomeacaoHist=").append(nomeacoes != null ? nomeacoes.size() : -1);
        builder.append("]");
        return builder.toString();
    }

}
