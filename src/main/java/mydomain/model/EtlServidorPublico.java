package mydomain.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Representa uma vinculação com um contrato de integração
 * {@link  br.gov.to.secad.etl.model.VwErgonServidorPublico}. Melhor dizendo,
 * vincula o contrato com uma {@link Nomeacao}.
 *
 * @author gilberto.andrade
 */
@Entity
@Table(name = "etl_servidor_publico", schema = "sapeo")
public class EtlServidorPublico implements Serializable {

    @Id
    @Column(name = "nomeacao_id")
    private Long id;
    @MapsId
    @OneToOne(optional = false)
    @JoinColumn(name = "nomeacao_id", nullable = false, updatable = false)
    private Nomeacao nomeacao;

    @Column(name = "etl_cpf", length = 11)
    private String etlCpf;
    
    @Basic(optional = false)
    @Column(name = "etl_nome_servidor", nullable = false, length = 150)
    private String etlNomeServidor;
    
    @Basic(optional = false)
    @Column(name = "etl_eh_exclusivamente_comissionado", nullable = false)
    private boolean etlEhExclusivamenteComissionado;
    
    @Basic(optional = false)
    @Column(name = "etl_num_funcional", nullable = false)
    private long etlNumFuncional;
    
    @Basic(optional = false)
    @Column(name = "etl_num_vinculo", nullable = false)
    private long etlNumVinculo;
    
    @Column(name = "etl_cargo", length = 150)
    private String etlCargo;
    
    @Column(name = "etl_cargo_referencia", length = 50)
    private String etlCargoReferencia;
    
    @Column(name = "etl_dt_posse")
    @Temporal(TemporalType.DATE)
    private Date etlDtPosse;
    
    @Column(name = "etl_dt_exercicio")
    @Temporal(TemporalType.DATE)
    private Date etlDtExercicio;
    
    @Column(name = "etl_dt_vacancia")
    @Temporal(TemporalType.DATE)
    private Date etlDtVacancia;
    
    @Basic(optional = false)
    @Column(name = "etl_dt_busca", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date etlDtBusca;
    
    @Column(name = "etl_cargo_remuneracao", precision = 12, scale = 2)
    private BigDecimal etlCargoRemuneracao;
    
    @Column(name = "etl_comissao", length = 150)
    private String etlComissao;

    @Column(name = "etl_comissao_referencia", length = 50)
    private String etlComissaoReferencia;
    
    @Column(name = "etl_folha_mes_ano")
    @Temporal(TemporalType.DATE)
    private Date etlFolhaMesAno;
    
    @Column(name = "etl_folha_numero")
    private Integer etlFolhaNumero;

    @Column(name = "etl_orgao_sigla", length = 20)
    private String etlOrgaoSigla;

    protected EtlServidorPublico() {
    }

    public EtlServidorPublico(String etlCpf, String etlNomeServidor, boolean etlEhExclusivamenteComissionado, long etlNumFuncional, long etlNumVinculo, String etlCargo, String etlCargoReferencia, Date etlDtPosse, Date etlDtExercicio, Date etlDtVacancia, Date etlDtBusca, BigDecimal etlCargoRemuneracao, String etlComissao, String etlComissaoReferencia, Date etlFolhaMesAno, Integer etlFolhaNumero, String etlOrgaoSigla) {
        this.etlCpf = etlCpf;
        this.etlNomeServidor = etlNomeServidor;
        this.etlEhExclusivamenteComissionado = etlEhExclusivamenteComissionado;
        this.etlNumFuncional = etlNumFuncional;
        this.etlNumVinculo = etlNumVinculo;
        this.etlCargo = etlCargo;
        this.etlCargoReferencia = etlCargoReferencia;
        this.etlDtPosse = etlDtPosse;
        this.etlDtExercicio = etlDtExercicio;
        this.etlDtVacancia = etlDtVacancia;
        this.etlDtBusca = etlDtBusca;
        this.etlCargoRemuneracao = etlCargoRemuneracao;
        this.etlComissao = etlComissao;
        this.etlComissaoReferencia = etlComissaoReferencia;
        this.etlFolhaMesAno = etlFolhaMesAno;
        this.etlFolhaNumero = etlFolhaNumero;
        this.etlOrgaoSigla = etlOrgaoSigla;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEtlCpf() {
        return etlCpf;
    }

    public void setEtlCpf(String etlCpf) {
        this.etlCpf = etlCpf;
    }

    public String getEtlNomeServidor() {
        return etlNomeServidor;
    }

    public void setEtlNomeServidor(String etlNomeServidor) {
        this.etlNomeServidor = etlNomeServidor;
    }

    public boolean getEtlEhExclusivamenteComissionado() {
        return etlEhExclusivamenteComissionado;
    }

    public void setEtlEhExclusivamenteComissionado(boolean etlEhExclusivamenteComissionado) {
        this.etlEhExclusivamenteComissionado = etlEhExclusivamenteComissionado;
    }

    public long getEtlNumFuncional() {
        return etlNumFuncional;
    }

    public void setEtlNumFuncional(long etlNumFuncional) {
        this.etlNumFuncional = etlNumFuncional;
    }

    public long getEtlNumVinculo() {
        return etlNumVinculo;
    }

    public void setEtlNumVinculo(long etlNumVinculo) {
        this.etlNumVinculo = etlNumVinculo;
    }

    public String getEtlCargo() {
        return etlCargo;
    }

    public void setEtlCargo(String etlCargo) {
        this.etlCargo = etlCargo;
    }

    public String getEtlCargoReferencia() {
        return etlCargoReferencia;
    }

    public void setEtlCargoReferencia(String etlCargoReferencia) {
        this.etlCargoReferencia = etlCargoReferencia;
    }

    public Date getEtlDtPosse() {
        return etlDtPosse;
    }

    public void setEtlDtPosse(Date etlDtPosse) {
        this.etlDtPosse = etlDtPosse;
    }

    public Date getEtlDtExercicio() {
        return etlDtExercicio;
    }

    public void setEtlDtExercicio(Date etlDtExercicio) {
        this.etlDtExercicio = etlDtExercicio;
    }

    public Date getEtlDtVacancia() {
        return etlDtVacancia;
    }

    public void setEtlDtVacancia(Date etlDtVacancia) {
        this.etlDtVacancia = etlDtVacancia;
    }

    public Date getEtlDtBusca() {
        return etlDtBusca;
    }

    public void setEtlDtBusca(Date etlDtBusca) {
        this.etlDtBusca = etlDtBusca;
    }

    public BigDecimal getEtlCargoRemuneracao() {
        return etlCargoRemuneracao;
    }

    public void setEtlCargoRemuneracao(BigDecimal etlCargoRemuneracao) {
        this.etlCargoRemuneracao = etlCargoRemuneracao;
    }

    public String getEtlComissao() {
        return etlComissao;
    }

    public void setEtlComissao(String etlComissao) {
        this.etlComissao = etlComissao;
    }

    public String getEtlComissaoReferencia() {
        return etlComissaoReferencia;
    }

    public void setEtlComissaoReferencia(String etlComissaoReferencia) {
        this.etlComissaoReferencia = etlComissaoReferencia;
    }

    public Date getEtlFolhaMesAno() {
        return etlFolhaMesAno;
    }

    public void setEtlFolhaMesAno(Date etlFolhaMesAno) {
        this.etlFolhaMesAno = etlFolhaMesAno;
    }

    public Integer getEtlFolhaNumero() {
        return etlFolhaNumero;
    }

    public void setEtlFolhaNumero(Integer etlFolhaNumero) {
        this.etlFolhaNumero = etlFolhaNumero;
    }

    public String getEtlOrgaoSigla() {
        return etlOrgaoSigla;
    }

    public void setEtlOrgaoSigla(String etlOrgaoSigla) {
        this.etlOrgaoSigla = etlOrgaoSigla;
    }

    public Nomeacao getNomeacao() {
        return nomeacao;
    }

    public void setNomeacao(Nomeacao nomeacao) {
        this.nomeacao = nomeacao;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EtlServidorPublico)) {
            return false;
        }
        EtlServidorPublico other = (EtlServidorPublico) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("EtlServidorPublico [");
        builder.append("etlCargo=").append(etlCargo);
        builder.append(", etlCargoReferencia=").append(etlCargoReferencia);
        builder.append(", etlCargoRemuneracao=").append(etlCargoRemuneracao);
        builder.append(", etlComissao=").append(etlComissao);
        builder.append(", etlComissaoReferencia=").append(etlComissaoReferencia);
        builder.append(", etlCpf=").append(etlCpf);
        builder.append(", etlDtBusca=").append(etlDtBusca);
        builder.append(", etlDtExercicio=").append(etlDtExercicio);
        builder.append(", etlDtPosse=").append(etlDtPosse);
        builder.append(", etlDtVacancia=").append(etlDtVacancia);
        builder.append(", etlEhExclusivamenteComissionado=").append(etlEhExclusivamenteComissionado);
        builder.append(", etlFolhaMesAno=").append(etlFolhaMesAno);
        builder.append(", etlFolhaNumero=").append(etlFolhaNumero);
        builder.append(", etlNomeServidor=").append(etlNomeServidor);
        builder.append(", etlNumFuncional=").append(etlNumFuncional);
        builder.append(", etlNumVinculo=").append(etlNumVinculo);
        builder.append(", etlOrgaoSigla=").append(etlOrgaoSigla);
        builder.append(", id=").append(id);
        builder.append(", nomeacao=").append(nomeacao);
        builder.append("]");
        return builder.toString();
    }

}
