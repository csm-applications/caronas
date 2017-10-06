/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author gabri
 */
@Entity
@Table(name = "viagem")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Viagem.findAll", query = "SELECT v FROM Viagem v")
    , @NamedQuery(name = "Viagem.findByIdViagem", query = "SELECT v FROM Viagem v WHERE v.idViagem = :idViagem")
    , @NamedQuery(name = "Viagem.findByDestino", query = "SELECT v FROM Viagem v WHERE v.destino = :destino")
    , @NamedQuery(name = "Viagem.findByDescricao", query = "SELECT v FROM Viagem v WHERE v.descricao = :descricao")
    , @NamedQuery(name = "Viagem.findByHoraInicio", query = "SELECT v FROM Viagem v WHERE v.horaInicio = :horaInicio")
    , @NamedQuery(name = "Viagem.findByHoraTermino", query = "SELECT v FROM Viagem v WHERE v.horaTermino = :horaTermino")})
public class Viagem implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_viagem")
    private Integer idViagem;
    @Basic(optional = false)
    @Column(name = "destino")
    private String destino;
    @Column(name = "descricao")
    private String descricao;
    @Basic(optional = false)
    @Column(name = "hora_inicio")
    @Temporal(TemporalType.DATE)
    private Date horaInicio;
    @Basic(optional = false)
    @Column(name = "hora_termino")
    @Temporal(TemporalType.DATE)
    private Date horaTermino;
    @ManyToMany(mappedBy = "viagemList")
    private List<UserAccount> userAccountList;
    @JoinColumn(name = "carro_id_carro", referencedColumnName = "id_carro")
    @ManyToOne(optional = false)
    private Carro carroIdCarro;
    @JoinColumn(name = "tarefa_id_tarefa", referencedColumnName = "id_tarefa")
    @ManyToOne(optional = false)
    private Tarefa tarefaIdTarefa;

    public Viagem() {
    }

    public Viagem(Integer idViagem) {
        this.idViagem = idViagem;
    }

    public Viagem(Integer idViagem, String destino, Date horaInicio, Date horaTermino) {
        this.idViagem = idViagem;
        this.destino = destino;
        this.horaInicio = horaInicio;
        this.horaTermino = horaTermino;
    }

    public Integer getIdViagem() {
        return idViagem;
    }

    public void setIdViagem(Integer idViagem) {
        this.idViagem = idViagem;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(Date horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Date getHoraTermino() {
        return horaTermino;
    }

    public void setHoraTermino(Date horaTermino) {
        this.horaTermino = horaTermino;
    }

    @XmlTransient
    public List<UserAccount> getUserAccountList() {
        return userAccountList;
    }

    public void setUserAccountList(List<UserAccount> userAccountList) {
        this.userAccountList = userAccountList;
    }

    public Carro getCarroIdCarro() {
        return carroIdCarro;
    }

    public void setCarroIdCarro(Carro carroIdCarro) {
        this.carroIdCarro = carroIdCarro;
    }

    public Tarefa getTarefaIdTarefa() {
        return tarefaIdTarefa;
    }

    public void setTarefaIdTarefa(Tarefa tarefaIdTarefa) {
        this.tarefaIdTarefa = tarefaIdTarefa;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idViagem != null ? idViagem.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Viagem)) {
            return false;
        }
        Viagem other = (Viagem) object;
        if ((this.idViagem == null && other.idViagem != null) || (this.idViagem != null && !this.idViagem.equals(other.idViagem))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Viagem[ idViagem=" + idViagem + " ]";
    }
    
}
