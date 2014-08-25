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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Forma de investidura em um cargo público. Neste sentido, provimento é o ato
 * de preencher o cargo ou ofício público por meio de nomeação, promoção,
 * readaptação, reversão, aproveitamento, reintegração e recondução. Então, a
 * Nomeação é a representação desse ato. A nomeação, em função da natureza do
 * cargo a ser provido, será feita:
 * <ol>
 * <li>Em caráter efetivo (permanente), através de concurso público; </li>
 * <li>Em caráter temporário, cargos ou funções de direção, chefia, assistência
 * e assessoramento superior e intermediário;</li>
 * <li>Em caráter vitalício, magistrados, os membros do Ministério Público e os
 * Conselheiros dos Tribunais de Contas.</li>
 * </ol>
 * A Nomeação dá início à investidura(uma pessoa passar a atuar como NOMEADO) no
 * cargo ou função(Vaga) na Estrutura Operacional de uma Entidade. Ela registra
 * essa ocorrência por um AtoLegal, uma Pessoa e a Vaga. A Nomeação é controlada
 * pela data de publicação(VeiculoPublicacao), data que entra em vigor e data
 * com efeito jurídico. A Nomeação tem seu efeito finalizado por uma Exoneração,
 * Demissão ou Revogação(tornar sem efeito). Lembrando que um Ato Legal pode
 * realizar várias nomeaçoes(itens do ato). A Nomeação pode, também, ser
 * utilizada como mecanismo de Redistribuição.
 * <b>A Posse</b> seria a fase na qual o servidor aceitaria formalmente as
 * atribuições do cargo, emprego ou função que exercerá, mediante assinatura do
 * termo de posse. É o momento de estabelecimento do <b>vínculo formal entre a
 * Administração e os nomeados</b>, razão pela qual, a partir de então, <b>SÃO
 * CONSIDERADOS SERVIDORES PÚBLICOS</b>. Isto não ocorre em relação aos
 * contratados, pois já estarão juridicamente vinculados ao Poder Público desde
 * a data de assinatura do contrato respectivo. Se o nomeado não tomar posse no
 * prazo legal(após 22 dias) a portaria de nomeação será revogada para tornar
 * sem efeito o ato inicial da investidura.
 *
 * @author gilberto.andrade
 */
@Entity
@Table(name = "nomeacao", schema = "sapeo", uniqueConstraints = @UniqueConstraint(name = "nomeacao_uk", columnNames = {"nome_pessoa", "vaga_id", "ato_legal_inicio_id"}))
public class Nomeacao implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Nomeacao.class);
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "nomeacao_id_seq")
    @SequenceGenerator(schema = "sapeo", name = "nomeacao_id_seq", sequenceName = "nomeacao_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "nome_pessoa")
    @Basic(optional = false)
    private String nomePessoa;

    @Column(name = "cpf", length = 11)
    private String cpf;

    @Column(name = "dt_entra_em_vigor")
    @Temporal(TemporalType.DATE)
    @Basic(optional = false)
    private Date dtEntraEmVigor;

    @JoinColumn(name = "ato_legal_inicio_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private AtoLegal atoLegalInicio;

    @JoinColumn(name = "ato_legal_termino_id", referencedColumnName = "id")
    @ManyToOne
    private AtoLegal atoLegalTermino;

    @JoinColumn(name = "vaga_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Vaga vaga;
    
    @OneToOne(mappedBy = "nomeacao", cascade = CascadeType.ALL)
    private EtlServidorPublico etlServidorPublico;

    @Column(name = "dt_efeito_juridico")
    @Temporal(TemporalType.DATE)
    private Date dtEfeitoJuridico;

    @OneToMany(mappedBy = "nomeacao")
    private List<Exoneracao> exoneracoes = new ArrayList<Exoneracao>();

    @OneToMany(mappedBy = "nomeacao")
    private List<Redistribuicao> redistribuicoes = new ArrayList<Redistribuicao>();

    @OneToMany(mappedBy = "nomeacao")
    private List<Demissao> demissoes = new ArrayList<Demissao>();
    
    public Nomeacao() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomePessoa() {
        return nomePessoa;
    }

    public void setNomePessoa(String nomePessoa) {
        this.nomePessoa = nomePessoa;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public boolean isEhExclusivamenteComissionado() {
        return etlServidorPublico.getEtlEhExclusivamenteComissionado();
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
        LOGGER.debug("FINALIZA Nomeacao com ato final: {}", atoLegalTermino);
       /**
        * TODO: Verificar a exposição de métodos set na Interface, pois com essa validação
        * qualquer alteração aciona esse mecanismo
        *
        *    if (isStatusFinalizado()) {
        *        throw new IllegalArgumentException("Nomeação já finalizada!");
        *    }
        *          
        */
        Validate.notNull(atoLegalTermino, "AtoLegalTermino Nulo");
        this.atoLegalTermino = atoLegalTermino;
        vaga.setEhProvido(false);
    }

    public AtoLegal getAtoLegalInicio() {
        return atoLegalInicio;
    }

    public void setAtoLegalInicio(AtoLegal atoLegalInicio) {
        this.atoLegalInicio = atoLegalInicio;
    }

    public Vaga getVaga() {
        return vaga;
    }

    public void setVaga(Vaga vaga) {
        this.vaga = vaga;
        vaga.addNomeacao(this);
        LOGGER.debug("Vinculo entre Nomeação{} e Vaga{} estabelecido!", this, vaga);
    }

    public EtlServidorPublico getEtlServidorPublico() {
        return etlServidorPublico;
    }

    public void setEtlServidorPublico(EtlServidorPublico etlServidorPublico) {
        this.etlServidorPublico = etlServidorPublico;
    }
    
    public boolean isStatusFinalizado() {
        try {
            this.atoLegalTermino.getId();
        } catch (NullPointerException ex) {
            LOGGER.debug("Nomeação NÃO FINALIZADA - AtoLegalTermino: {}", atoLegalTermino);
            return false;
        }
        LOGGER.debug("Nomeação FINALIZADA - AtoLegalTermino: {}", atoLegalTermino);
        return true;
    }

    public boolean isStatusExonerado() {
        return (isStatusFinalizado() && !getExoneracoesDisponiveis().isEmpty());
    }

    public boolean isStatusRedistribuido() {
        return (!getRedistribuicoesDisponiveis().isEmpty());
    }

    public boolean isStatusDemitido() {
        return (isStatusFinalizado() && !getDemissoesDisponiveis().isEmpty());
    }

    /**
     * Exonerações não finalizadas
     *
     * @return
     */
    public List<Exoneracao> getExoneracoesDisponiveis() {
        final List<Exoneracao> disponiveis = new ArrayList<Exoneracao>();
        for (Exoneracao v : getExoneracoes()) {
            if (null == v.getAtoLegalTermino()) {
                disponiveis.add(v);
            }
        }
        return Collections.unmodifiableList(disponiveis);
    }

    public List<Exoneracao> getExoneracoes() {
        return Collections.unmodifiableList(exoneracoes);
    }

    public void addExoneracao(Exoneracao line) {
        LOGGER.debug("Tornando essa nomeação {} exonerada {}", this, line);
        Validate.notNull(line, "Não adicionar exoneração Nula");
        //Validate.isFalse(isStatusFinalizado(), "Nomeação já finalizada!");
        if (isStatusFinalizado()) {
            throw new IllegalArgumentException("Nomeação já finalizada!");
        }
        line.setNomeacao(this);
        this.exoneracoes.add(line);
        setAtoLegalTermino(line.getAtoLegalInicio());
        vaga.setEhProvido(false);
    }

    public void removeExoneracao(Exoneracao line) {
        LOGGER.debug("Removendo exoneração {}", line);
        Validate.notNull(line, "Não remover exoneração Nula");
        exoneracoes.remove(line);
        /**
         * Aqui vamos by-passar a regra de negócio pois estamos desfazendo toda a relação.
         * setAtoLegalTermino(null);
         */
        this.atoLegalTermino=null;
        vaga.setEhProvido(true);
    }

    public void revogarExoneracao(Exoneracao line) {
        LOGGER.debug("Desfazendo exoneração {}", line);
        Validate.notNull(line, "Não desfazer exoneração Nula");
        Validate.notNull(line.getAtoLegalTermino(), "Exoneração não finalizada(revogada)!");
        /**
         * Aqui vamos by-passar a regra de negócio pois estamos desfazendo toda a relação.
         * setAtoLegalTermino(null);
         */
        this.atoLegalTermino=null;
        vaga.setEhProvido(true);
    }

    /**
     * Demissões não finalizadas.
     *
     * @return
     */
    public List<Demissao> getDemissoesDisponiveis() {
        final List<Demissao> disponiveis = new ArrayList<Demissao>();
        for (Demissao v : getDemissoes()) {
            if (null == v.getAtoLegalTermino()) {
                disponiveis.add(v);
            }
        }
        return Collections.unmodifiableList(disponiveis);
    }

    public List<Demissao> getDemissoes() {
        return Collections.unmodifiableList(demissoes);
    }

    public void addDemissao(Demissao line) {
        LOGGER.debug("Tornando essa nomeação {} demitida {}", this, line);
        Validate.notNull(line, "Não adicionar demissão Nula");
        if (isStatusFinalizado()) {
            throw new IllegalArgumentException("Nomeação já finalizada!");
        }
        line.setNomeacao(this);
        this.demissoes.add(line);
        setAtoLegalTermino(line.getAtoLegalInicio());
        vaga.setEhProvido(false);
    }

    public void removeDemissao(Demissao line) {
        LOGGER.debug("Removendo demissão {}",  line);        
        Validate.notNull(line, "Não remover demissão Nula");
        demissoes.remove(line);
        /**
         * Aqui vamos by-passar a regra de negócio pois estamos desfazendo toda a relação.
         * setAtoLegalTermino(null);
         */
        this.atoLegalTermino=null;
        vaga.setEhProvido(true);
    }

    public void revogarDemissao(Demissao line) {
        LOGGER.debug("Desfazendo demissão {}",  line);        
        Validate.notNull(line, "Não desfazer demissão Nula");
        Validate.notNull(line.getAtoLegalTermino(), "Demissão não finalizada(revogada)!");
        /**
         * Aqui vamos by-passar a regra de negócio pois estamos desfazendo toda a relação.
         * setAtoLegalTermino(null);
         */
        this.atoLegalTermino=null;
        vaga.setEhProvido(true);
    }

    /**
     * Redistribuições não finalizadas
     *
     * @return
     */
    public List<Redistribuicao> getRedistribuicoesDisponiveis() {
        final List<Redistribuicao> disponiveis = new ArrayList<Redistribuicao>();
        for (Redistribuicao v : getRedistribuicoes()) {
            if (null == v.getAtoLegalTermino()) {
                disponiveis.add(v);
            }
        }
        return Collections.unmodifiableList(disponiveis);
    }

    public List<Redistribuicao> getRedistribuicoes() {
        return Collections.unmodifiableList(redistribuicoes);
    }

    public void addRedistribuicao(Redistribuicao line) {
        LOGGER.debug("Tornando essa nomeação {} redistribuida {}", this, line);
        Validate.notNull(line, "Não adicionar redistribuição Nula");
        //Validate.isFalse(isStatusFinalizado(), "Nomeação já finalizada!");
        if (isStatusFinalizado()) {
            throw new IllegalArgumentException("Nomeação já finalizada!");
        }
        line.setNomeacao(this);
        this.redistribuicoes.add(line);
    }

    public void removeRedistribuicao(Redistribuicao line) {
        LOGGER.debug("Removendo redistribuição {}",  line);        
        Validate.notNull(line, "Não remover redistribuição Nula");
        redistribuicoes.remove(line);
    }

    public Entidade ondeEsta() {
        return getVaga().ondeEsta();
    }

    public Entidade redistribuidoPara() {
        if (isStatusRedistribuido()) {
            return getRedistribuicoesDisponiveis().get(0).getEntidade();
        } else {
            return null;
        }
    }

    @Override
        public boolean equals(Object other) {
        return (other instanceof Nomeacao) && (id != null)
                ? id.equals(((Nomeacao) other).id)
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
        builder.append("Nomeacao [");
        builder.append("id=").append(id);
        builder.append(", vaga=").append(vaga != null ? vaga.getId() : "null");
        builder.append(", etlServidorPublico=").append(etlServidorPublico != null ? etlServidorPublico.getId() : "null");
        builder.append(", nomePessoa=").append(nomePessoa);
        builder.append(", cpf=").append(cpf);                
        builder.append(", dtEfeitoJuridico=").append(dtEfeitoJuridico);
        builder.append(", dtEntraEmVigor=").append(dtEntraEmVigor);
        builder.append(", atoLegalInicio=").append(atoLegalInicio != null ? atoLegalInicio.getId() : "null");
        builder.append(", atoLegalTermino=").append(atoLegalTermino != null ? atoLegalTermino.getId() : "null");
        builder.append(", exoneracoes=").append(exoneracoes != null ? exoneracoes.size() : -1);
        builder.append(", redistribuicoes=").append(redistribuicoes != null ? redistribuicoes.size() : -1);
        builder.append(", demissoes=").append(demissoes != null ? demissoes.size() : -1);
        builder.append("]");
        return builder.toString();
    }

}
