/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aroc.v1.modelo.entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "registros", catalog = "registro", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Registro.listarTodo", query = "SELECT r FROM Registro r")
    , @NamedQuery(name = "Registro.ecnontrarPorId", query = "SELECT r FROM Registro r WHERE r.idUsuario = :idUsuario")
    , @NamedQuery(name = "Registro.findByPuertoPHP", query = "SELECT r FROM Registro r WHERE r.puertoPHP = :puertoPHP")
    , @NamedQuery(name = "Registro.findByPuertoSQL", query = "SELECT r FROM Registro r WHERE r.puertoSQL = :puertoSQL")})
public class Registro implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idUsuario", nullable = false)
    private Integer idUsuario;
    @Basic(optional = false)
    @NotNull
    @Column(name = "puertoPHP", nullable = false)
    private int puertoPHP;
    @Basic(optional = false)
    @NotNull
    @Column(name = "puertoSQL", nullable = false)
    private int puertoSQL;

    public Registro() {
    }

    public Registro(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Registro(Integer idUsuario, int puertoPHP, int puertoSQL) {
        this.idUsuario = idUsuario;
        this.puertoPHP = puertoPHP;
        this.puertoSQL = puertoSQL;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getPuertoPHP() {
        return puertoPHP;
    }

    public void setPuertoPHP(int puertoPHP) {
        this.puertoPHP = puertoPHP;
    }

    public int getPuertoSQL() {
        return puertoSQL;
    }

    public void setPuertoSQL(int puertoSQL) {
        this.puertoSQL = puertoSQL;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idUsuario != null ? idUsuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Registro)) {
            return false;
        }
        Registro other = (Registro) object;
        if ((this.idUsuario == null && other.idUsuario != null) || (this.idUsuario != null && !this.idUsuario.equals(other.idUsuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "aroc.v1.modelo.entidades.Registro[ idUsuario=" + idUsuario + " ]";
    }
    
}
