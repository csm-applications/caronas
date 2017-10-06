/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author gabri
 */
@Entity
@Table(name = "user_account")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserAccount.findAll", query = "SELECT u FROM UserAccount u")
    , @NamedQuery(name = "UserAccount.findByUserLogin", query = "SELECT u FROM UserAccount u WHERE u.userLogin = :userLogin")
    , @NamedQuery(name = "UserAccount.findByPassword", query = "SELECT u FROM UserAccount u WHERE u.password = :password")
    , @NamedQuery(name = "UserAccount.findByUserName", query = "SELECT u FROM UserAccount u WHERE u.userName = :userName")
    , @NamedQuery(name = "UserAccount.findByPhone", query = "SELECT u FROM UserAccount u WHERE u.phone = :phone")
    , @NamedQuery(name = "UserAccount.findBySector", query = "SELECT u FROM UserAccount u WHERE u.sector = :sector")
    , @NamedQuery(name = "UserAccount.findByEmail", query = "SELECT u FROM UserAccount u WHERE u.email = :email")})
public class UserAccount implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "userLogin")
    private String userLogin;
    @Basic(optional = false)
    @Column(name = "password")
    private String password;
    @Basic(optional = false)
    @Column(name = "userName")
    private String userName;
    @Basic(optional = false)
    @Column(name = "phone")
    private String phone;
    @Basic(optional = false)
    @Column(name = "sector")
    private String sector;
    @Basic(optional = false)
    @Column(name = "email")
    private String email;
    @JoinTable(name = "user_account_has_viagem", joinColumns = {
        @JoinColumn(name = "user_account_userLogin", referencedColumnName = "userLogin")}, inverseJoinColumns = {
        @JoinColumn(name = "viagem_id_viagem", referencedColumnName = "id_viagem")})
    @ManyToMany
    private List<Viagem> viagemList;

    public UserAccount() {
    }

    public UserAccount(String userLogin) {
        this.userLogin = userLogin;
    }

    public UserAccount(String userLogin, String password, String userName, String phone, String sector, String email) {
        this.userLogin = userLogin;
        this.password = password;
        this.userName = userName;
        this.phone = phone;
        this.sector = sector;
        this.email = email;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
        hash += (userLogin != null ? userLogin.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserAccount)) {
            return false;
        }
        UserAccount other = (UserAccount) object;
        if ((this.userLogin == null && other.userLogin != null) || (this.userLogin != null && !this.userLogin.equals(other.userLogin))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.UserAccount[ userLogin=" + userLogin + " ]";
    }
    
}
