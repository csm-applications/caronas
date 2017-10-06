/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author gabri
 */
@Entity
@Table(name = "carro")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Carro.findAll", query = "SELECT c FROM Carro c")
    , @NamedQuery(name = "Carro.findByIdCarro", query = "SELECT c FROM Carro c WHERE c.idCarro = :idCarro")
    , @NamedQuery(name = "Carro.findByMarca", query = "SELECT c FROM Carro c WHERE c.marca = :marca")
    , @NamedQuery(name = "Carro.findByModelo", query = "SELECT c FROM Carro c WHERE c.modelo = :modelo")
    , @NamedQuery(name = "Carro.findByPlaca", query = "SELECT c FROM Carro c WHERE c.placa = :placa")
    , @NamedQuery(name = "Carro.findBySituacao", query = "SELECT c FROM Carro c WHERE c.situacao = :situacao")})
public class Carro implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_carro")
    private Integer idCarro;
    @Basic(optional = false)
    @Column(name = "marca")
    private String marca;
    @Basic(optional = false)
    @Column(name = "modelo")
    private String modelo;
    @Basic(optional = false)
    @Column(name = "placa")
    private String placa;
    @Basic(optional = false)
    @Column(name = "situacao")
    private String situacao;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "carroIdCarro")
    private List<Viagem> viagemList;

    public Carro() {
    }

    public Carro(Integer idCarro) {
        this.idCarro = idCarro;
    }

    public Carro(Integer idCarro, String marca, String modelo, String placa, String situacao) {
        this.idCarro = idCarro;
        this.marca = marca;
        this.modelo = modelo;
        this.placa = placa;
        this.situacao = situacao;
    }

    public Integer getIdCarro() {
        return idCarro;
    }

    public void setIdCarro(Integer idCarro) {
        this.idCarro = idCarro;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    @XmlTransient
    public List<Viagem> getViagemList() {
        return viagemList;
    }

    public void setViagemList(List<Viagem> viagemList) {
        this.viagemList = viagemList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCarro != null ? idCarro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Carro)) {
            return false;
        }
        Carro other = (Carro) object;
        if ((this.idCarro == null && other.idCarro != null) || (this.idCarro != null && !this.idCarro.equals(other.idCarro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Carro[ idCarro=" + idCarro + " ]";
    }
    
}
