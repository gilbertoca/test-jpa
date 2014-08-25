package mydomain.model;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.persistence.PostLoad;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author gilberto.andrade
 */
@Entity
@Table(name = "estrutura_operacional", schema = "sapeo", uniqueConstraints = @UniqueConstraint(name = "estrutura_operacional_uk", columnNames = {"entidade_id", "denominacao_cargo_id", "ato_legal_inicio_id"}))
public class EstruturaOperacional implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(EstruturaOperacional.class);

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "estrutura_operacional_id_seq")
    @SequenceGenerator(schema = "sapeo", name = "estrutura_operacional_id_seq", sequenceName = "estrutura_operacional_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "quantitativo_especifico")
    private int quantitativoEspecifico;

    @Column(name = "denominacao_especificacao")
    private String denominacaoEspecificacao;

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

    @OneToMany(orphanRemoval = true, mappedBy = "estruturaOperacional", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Vaga> vagas = new ArrayList<Vaga>();

    @JoinColumn(name = "entidade_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Entidade entidade;

    @JoinColumn(name = "denominacao_cargo_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private DenominacaoCargo denominacaoCargo;
    /**
     * I think an easy way is to save the previous value in a transient variable
     * that JPA will not persist. So just introduce a variable previousSurname
     * and save the actual value before you overwrite it in the setter. If you
     * want to save multiple properties it would be easy if your class MyClass
     * is Serializable. ref.:
     * http://stackoverflow.com/questions/19313629/getting-object-field-previous-value-hibernate-jpa
     */
    @Transient
    private transient int savedStateQuantitativoEspecifico;

    /**
     * Ordenação por denominacaoCargo.ordemImportancia
     */
    public static final Comparator<EstruturaOperacional> ORDEM_IMPORTANCIA_COMPARATOR = new Comparator<EstruturaOperacional>() {

        @Override
        public int compare(EstruturaOperacional o1, EstruturaOperacional o2) {
            return o1.denominacaoCargo.getOrdemImportancia().compareTo(o2.denominacaoCargo.getOrdemImportancia());
        }

    };    
    public EstruturaOperacional() {
    }

    /**
     * I think an easy way is to save the previous value in a transient variable
     * that JPA will not persist. So just introduce a variable previousSurname
     * and save the actual value before you overwrite it in the setter. If you
     * want to save multiple properties it would be easy if your class MyClass
     * is Serializable. ref.:
     * http://stackoverflow.com/questions/19313629/getting-object-field-previous-value-hibernate-jpa
     */
    @PostLoad
    private void safeState() {
        this.savedStateQuantitativoEspecifico = quantitativoEspecifico;
        LOGGER.debug("safeState - @PostLoad: " + quantitativoEspecifico);
    }

    private int diferencaQuantitativoEspecifico() {
        int oldValue = getSavedStateQuantitativoEspecifico();
        int newValue = getQuantitativoEspecifico();
        LOGGER.debug("diferencaQuantitativoEspecifico newValue: {} - oldValue: {}", newValue, oldValue);
        return (newValue - oldValue);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getQuantitativoEspecifico() {
        return quantitativoEspecifico;
    }

    /**
     * Em conjunto com
     * {@link #getSavedStateQuantitativoEspecifico() getSavedStateQuantitativoEspecifico}
     * estabelece o comportamento da {@link #createVagas(int) criação} e/ou
     * {@link #rebuildVagas(int) reconstrução} de vagas. Usando o mecanismo
     *
     * @PostLoad do JPA, armazenamos o valor anterior de
     * {@link #quantitativoEspecifico} no atributo transiente
     * {@link #savedStateQuantitativoEspecifico}. Deste modo, temos conhecimento
     * quando for a criação(savedStateQuantitativoEspecifico == 0) de uma
     * instância da classe EstruturaOperacional e assim acionamos a
     * {@link #createVagas(int) criação} de vagas, caso contrário acionamos
     * {@link #rebuildVagas(int) reconstrução} de vagas. Requisito importante,
     * as seguintes dependência: -DenominacaoCargo -AtoLegalInicio
     *
     * @param qtdEspecifico
     */
    public void setQuantitativoEspecifico(int qtdEspecifico) {
        LOGGER.debug("setQuantitativoEspecifico - quantitativoEspecifico: {}", qtdEspecifico);
        Validate.notNull(this.getDenominacaoCargo(), "Denominação Cargo Nula");
        Validate.notNull(this.getAtoLegalInicio(), "Ato Legal Inicial Nulo");
        if (qtdEspecifico < 1 || qtdEspecifico > getDenominacaoCargo().getDenominacaoCargoDetalhe().getQuantitativoAmplo()) {
            throw new IllegalArgumentException("QuantitativoEspecifico não está no intervalo [1..QuantitativoAmplo]: " + qtdEspecifico);
        }
        this.quantitativoEspecifico = qtdEspecifico;
        //controle de vagas
        if (getSavedStateQuantitativoEspecifico() == 0) {
            LOGGER.debug("setQuantitativoEspecifico - buildVagas(); ");
            createVagas(qtdEspecifico);
        } else {
            int diferenca = diferencaQuantitativoEspecifico();
            LOGGER.debug("setQuantitativoEspecifico - rebuildVagas(diferencaQuantitativoEspecifico()): {} ", diferenca);
            rebuildVagas(diferenca);
        }
    }

    public String getDenominacaoEspecificacao() {
        return denominacaoEspecificacao;
    }

    public void setDenominacaoEspecificacao(String denominacaoEspecificacao) {
        this.denominacaoEspecificacao = denominacaoEspecificacao;
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

    public BigDecimal getValorDenominacaoCargo() {
        BigDecimal valorDenominacaoCargo = BigDecimal.ZERO;
        BigDecimal valorRemunecacao = BigDecimal.ZERO;
        BigDecimal valorGratificacao = BigDecimal.ZERO;

        if (this.denominacaoCargo == null) {
            return BigDecimal.ZERO;
        }
        valorRemunecacao = getDenominacaoCargo().getDenominacaoCargoDetalhe().getRemuneracao();
        valorGratificacao = getDenominacaoCargo().getDenominacaoCargoDetalhe().getGratificacao();

        LOGGER.debug("valorRemunecacao {} + valorGratificacao {}", valorRemunecacao, valorGratificacao);
        valorDenominacaoCargo = valorRemunecacao.add(valorGratificacao);
        return valorDenominacaoCargo;
    }

    protected int getSavedStateQuantitativoEspecifico() {
        LOGGER.debug("getSavedStateQuantitativoEspecifico - savedStateQuantitativoEspecifico: {}", savedStateQuantitativoEspecifico);
        return savedStateQuantitativoEspecifico;
    }
    /**
     * Custo potencial
     * @return BigDecimal O custo total
     */
    public BigDecimal getCusto() {
        return getValorDenominacaoCargo().multiply(new BigDecimal(getQuantitativoEspecifico()));
    }
    /**
     * Custo potencial com vaga providas
     * @return BigDecimal O custo total
     */
    public BigDecimal getCustoVagasProvidas() {
        return getValorDenominacaoCargo().multiply(new BigDecimal(getVagasProvidas().size()));
    }

    /**
     * Todas as vagas
     *
     * @return List vagas ocupadas e não ocupadas
     */
    public List<Vaga> getVagas() {
        return Collections.unmodifiableList(vagas);
    }

    public int countVagas() {
        if (this.getVagas().isEmpty()) {
            return 0;
        }
        return getVagas().size();
    }

    /**
     * Um list iterator de vagas Permite subclasses editar a lista
     *
     * @return ListIterator
     */
    protected ListIterator getVagasList() {
        return this.vagas.listIterator();
    }

    public void addVaga(Vaga line) {
        vagas.add(line);
    }

    public void removeVaga(Vaga line) {
        vagas.remove(line);
    }

    /**
     * Vagas preenchidas(providas) e NÃO FINALIZADAS.
     *
     * @return List vagas providas ou uma lista vazia imutável quando getVagas()
     * for null.
     */
    public final List<Vaga> getVagasProvidas() {
        if (this.vagas.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        List<Vaga> vagaProvidas = new ArrayList<Vaga>();

        for (Vaga v : getVagas()) {
            if (v.getEhProvido() == true && v.isStatusFinalizado() == false) {
                vagaProvidas.add(v);
            }
        }
        return Collections.unmodifiableList(vagaProvidas);
    }

    public int countVagasProvidas() {
        if (this.getVagasProvidas().isEmpty()) {
            return 0;
        }
        return getVagasProvidas().size();
    }
    /**
     * Vagas preenchidas(providas), NÃO FINALIZADAS e REDISTRIBUIDAS.
     * Um PORÉM: uma vaga pode ter mais de uma nomeação. Neste caso, assumimos que a vaga é redistribuída quando houver
     * alguma nomeção redistribuida. 
     * TODO: rever o cálculo do custo potencial total para providos e redistribuidos
     * 
     * @return List vagas providas ou uma lista vazia imutável quando getVagas()
     * for null.
     */
    public final List<Vaga> getVagasProvidasERedistribuidas() {
        if (this.vagas.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        List<Vaga> VagasProvidasERedistribuidas = new ArrayList<Vaga>();
        //Atenção: vaga->*nomeaco->redistribuicao
        for (Vaga v : getVagasProvidas()) {
            if (v.countNomeacoesDisponiveisERedistribuidas() > 0) {
                VagasProvidasERedistribuidas.add(v);
            }
        }
        return Collections.unmodifiableList(VagasProvidasERedistribuidas);
    }

    public int countVagasProvidasERedistribuidas() {
        if (this.getVagasProvidasERedistribuidas().isEmpty()) {
            return 0;
        }
        return getVagasProvidasERedistribuidas().size();
    }

    /**
     * Vagas preenchidas(providas) e FINALIZADAS.
     *
     * @return List vagas providas ou uma lista vazia imutável quando getVagas()
     * for null.
     */
    public final List<Vaga> getVagasProvidasFinalizadas() {
        if (this.vagas.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        List<Vaga> vagaProvidasFinalizadas = new ArrayList<Vaga>();

        for (Vaga v : getVagas()) {
            if (v.getEhProvido() == true && v.isStatusFinalizado() == true) {
                vagaProvidasFinalizadas.add(v);
            }
        }
        return Collections.unmodifiableList(vagaProvidasFinalizadas);
    }

    public int countVagasProvidasFinalizadas() {
        if (this.getVagasProvidasFinalizadas().isEmpty()) {
            return 0;
        }
        return getVagasProvidasFinalizadas().size();
    }

    /**
     * Vagas disponíveis(providas ou não) e não finalizadas
     *
     * @return List vagas disponíveis
     */
    public final List<Vaga> getVagasDisponiveis() {
        if (this.vagas.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        List<Vaga> vagasDiponiveis = new ArrayList<Vaga>();

        for (Vaga v : getVagas()) {
            if (v.isStatusFinalizado() == false) {
                vagasDiponiveis.add(v);
            }
        }
        Collections.sort(vagasDiponiveis, Vaga.NOME_COMPARATOR);
        return Collections.unmodifiableList(vagasDiponiveis);
    }

    /**
     * Total de Vagas disponíveis(providas ou não) não finalizadas.
     *
     * @return
     */
    public int countVagasDisponiveis() {
        if (this.getVagasDisponiveis().isEmpty()) {
            return 0;
        }
        return getVagasDisponiveis().size();
    }

    /**
     * Vagas disponíveis(providas ou não) não finalizadas sem histórico de
     * nomeações.
     *
     * @return List vagas (vazias) sem histórico de nomeações
     */
    public final List<Vaga> getVagasDisponiveisSemHistNomeacao() {
        if (this.vagas.isEmpty()) {
            return Collections.EMPTY_LIST;
        }

        List<Vaga> vagasDisponiveisSemHistNomeacao = new ArrayList<Vaga>();

        for (Vaga v : getVagasDisponiveis()) {
            LOGGER.debug("getVagasDisponiveisSemHistNomeacao() {}", v);
            if (v.getNomeacoes().isEmpty()) {
                vagasDisponiveisSemHistNomeacao.add(v);
            }
        }
        return Collections.unmodifiableList(vagasDisponiveisSemHistNomeacao);
    }

    /**
     * Vagas finalizadas.
     *
     * @return
     */
    public final List<Vaga> getVagasFinalizadas() {
        if (this.vagas.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        List<Vaga> vagasFinalizadas = new ArrayList<Vaga>();

        for (Vaga v : getVagas()) {
            if (v.isStatusFinalizado()) {
                vagasFinalizadas.add(v);
            }
        }
        return Collections.unmodifiableList(vagasFinalizadas);
    }

    /**
     * Criar vaga(s) conforme o {@link qtdEspecifico quantitativo específico}.
     * Para identificar(atributo nome) uma Vaga usaremos o minemônico:
     * entidade.SIGLA denominacaoCargo.NOME denominacaoCargo.SIMBOLONIVEL
     * estruturaOperacional.QUANTITATIVOESPECIFICO SEQUENCIA NUMERICA
     *
     * @param qtdEspecifico
     */
    protected void createVagas(int qtdEspecifico) {
        Validate.isTrue(qtdEspecifico > 0, "Deveria ser maior ou igual a 1, mas foi: {}", qtdEspecifico);
        LOGGER.debug("Criar vaga(s) conforme o qtd: {}", qtdEspecifico);
        Vaga v;
        for (int i = 0; i < qtdEspecifico; i++) {
            v = new Vaga();
            v.setNome(getNomeVaga(i));
            v.setEstruturaOperacional(this);
            v.setAtoLegalInicio(getAtoLegalInicio());
            v.setDtEntraEmVigor(getDtEntraEmVigor());
            v.setDtEfeitoJuridico(getDtEfeitoJuridico());
            LOGGER.debug("Vaga : {}", v);
            addVaga(v);
        }
    }

    /**
     * Nome amigável para a estrutura-operacional.
     *
     * @return entidade.SIGLA-denominacaoCargo.NOME
     */
    public String getNome() {
        StringBuilder nome = new StringBuilder();
        nome.append(getDenominacaoCargo().getNome());
        nome.append("-");
        nome.append(getDenominacaoCargo().getSimboloNivel());
        return nome.toString();
    }

    /**
     * o minemônico: entidade.SIGLA denominacaoCargo.NOME
     * denominacaoCargo.SIMBOLONIVEL estruturaOperacional.QUANTITATIVOESPECIFICO
     * SEQUENCIA NUMERICA, utilizado na criação da Vaga.
     *
     * @param sequence
     * @return String o minemônico
     */
    private String getNomeVaga(int sequence) {
        LOGGER.debug("Montando o minemônico com: {}", this);
        StringBuilder nome = new StringBuilder();
        nome.append(getEntidade().getSigla());
        nome.append("-");
        nome.append(getDenominacaoCargo().getNome());
        nome.append("-");
        nome.append(getDenominacaoCargo().getSimboloNivel());
        nome.append("-");
        nome.append(getQuantitativoEspecifico());
        nome.append("-");
        nome.append(sequence);
        return nome.toString();
    }

    /**
     * Vagas existentes podem ser removidas desde que não tenham
     * vaga_nomeacao_hist;
     *
     * @param qtdEspecifico
     */
    protected void destroyVagas(int qtdEspecifico) {
        Validate.isTrue(qtdEspecifico > 0, "Deveria ser maior ou igual a 1, mas foi: {}", qtdEspecifico);
        LOGGER.debug("Destruir vaga(s) conforme o qtd: {}", qtdEspecifico);
        Iterator<Vaga> iter = vagas.listIterator();
        while (iter.hasNext()) {
            Vaga v = iter.next();
            if (v.getNomeacoes().isEmpty() && qtdEspecifico > 0) {
                LOGGER.debug("Vaga destruida: " + v);
                iter.remove();
                qtdEspecifico--;
            }
        }
    }

    /**
     * Reconstrução de vagas baseado no
     * {@link #diferencaQuantitativoEspecifico() diferencaQuantitativoEspecifico()}.
     *
     * @param qtd
     */
    protected void rebuildVagas(int qtd) {
        Validate.isTrue(diferencaQuantitativoEspecifico() == qtd, "Deveria ser igual a diferencaQuantitativoEspecifico(), mas foi: %d", qtd);
        LOGGER.debug("reconstruir vagas - diferença: {}", qtd);
        if (qtd > 0) {
            createVagas(qtd);
        } else if (qtd < 0) {
            int absQtd = Math.abs(qtd);
            int qtdVagasDisponiveisSemHistNomeacao = getVagasDisponiveisSemHistNomeacao().size();
            LOGGER.debug("qtd de vaga(s) após modulo - Math.abs(qtd) : {}", absQtd);
            LOGGER.debug("qtd de vaga(s) disponíveis sem hist. de nomeações : {}", qtdVagasDisponiveisSemHistNomeacao);

            if (absQtd >= qtdVagasDisponiveisSemHistNomeacao) {
                throw new IllegalArgumentException("Deveria ser menor ou igual ao quantitativo de vagas disponíveis sem histórico de nomeções(" + qtdVagasDisponiveisSemHistNomeacao + "), mas foi: " + absQtd);
            }
            destroyVagas(absQtd);
        }
    }

    /**
     * Finaliza todas as vagas não finalizadas. TODO:Verificar as consequências
     * em Nomeação(e dependentes).
     *
     * @param atoLegalTermino
     */
    protected void finalizarVagas(AtoLegal atoLegalTermino) {
        // Standard iterator sufficient for altering elements
        Iterator<Vaga> iterator = vagas.iterator();

        while (iterator.hasNext()) {
            Vaga v = iterator.next();
            LOGGER.debug("finalizando vaga: {}", v);
            v.setAtoLegalTermino(atoLegalTermino);
        }
    }

    public Entidade getEntidade() {
        return entidade;
    }

    public void setEntidade(Entidade entidade) {
        this.entidade = entidade;
    }

    public DenominacaoCargo getDenominacaoCargo() {
        return denominacaoCargo;
    }

    public void setDenominacaoCargo(DenominacaoCargo denominacaoCargo) {
        this.denominacaoCargo = denominacaoCargo;
    }

    public AtoLegal getAtoLegalInicio() {
        return atoLegalInicio;
    }

    public void setAtoLegalInicio(AtoLegal atoLegalInicio) {
        this.atoLegalInicio = atoLegalInicio;
    }

    public AtoLegal getAtoLegalTermino() {
        return atoLegalTermino;
    }

    /**
     * Finaliza a EstruturaOperacional.
     *
     * @param atoLegalTermino
     */
    public void setAtoLegalTermino(AtoLegal atoLegalTermino) {
        LOGGER.debug("FINALIZA Estrutura-Operacional com ato final: {}", atoLegalTermino);
        Validate.notNull(atoLegalTermino, "AtoLegalTermino Nulo");
        finalizarVagas(atoLegalTermino);
        this.atoLegalTermino = atoLegalTermino;
    }

    public boolean isStatusFinalizado() {
        try {
            this.atoLegalTermino.getId();
        } catch (NullPointerException ex) {
            LOGGER.debug("Estrutura-Operacional NÃO FINALIZADA - AtoLegalTermino: {}", atoLegalTermino);
            return false;
        }
        LOGGER.debug("Estrutura-Operacional FINALIZADA - AtoLegalTermino: {}", atoLegalTermino);
        return true;
    }

    public String getOrdemImportancia() {
        return this.denominacaoCargo.getOrdemImportancia();
    }

    @Override
    public boolean equals(Object other) {
        return (other instanceof EstruturaOperacional) && (id != null)
                ? id.equals(((EstruturaOperacional) other).id)
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
        builder.append("EstruturaOperacional [");
        builder.append("id=").append(id);
        builder.append(", quantitativoEspecifico=").append(quantitativoEspecifico);
        builder.append(", savedStateQuantitativoEspecifico=").append(savedStateQuantitativoEspecifico);
        builder.append(", dtEfeitoJuridico=").append(dtEfeitoJuridico);
        builder.append(", dtEntraEmVigor=").append(dtEntraEmVigor);
        builder.append(", atoLegalInicio=").append(atoLegalInicio != null ? atoLegalInicio.getId() : "null");
        builder.append(", atoLegalTermino=").append(atoLegalTermino != null ? atoLegalTermino.getId() : "null");
        builder.append(", denominacaoCargo=").append(denominacaoCargo != null ? denominacaoCargo.getId() : "null");
        builder.append(", denominacaoEspecificacao=").append(denominacaoEspecificacao);
        builder.append(", entidade=").append(entidade != null ? entidade.getSigla() : "null");
        builder.append(", vagas=").append(vagas != null ? vagas.size() : -1);
        builder.append("]");
        return builder.toString();
    }

}
