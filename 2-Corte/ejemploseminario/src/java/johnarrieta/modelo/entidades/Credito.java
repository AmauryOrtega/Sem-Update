/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package johnarrieta.modelo.entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Estudiante
 */
@Entity
@Table(name = "Creditos", catalog = "ejemplounicartagena", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Credito.findAll", query = "SELECT c FROM Credito c"),
    @NamedQuery(name = "Credito.findById", query = "SELECT c FROM Credito c WHERE c.id = :id"),
    @NamedQuery(name = "Credito.findByFecha", query = "SELECT c FROM Credito c WHERE c.fecha = :fecha"),
    @NamedQuery(name = "Credito.findByValor", query = "SELECT c FROM Credito c WHERE c.valor = :valor"),
    @NamedQuery(name = "Credito.findByCutas", query = "SELECT c FROM Credito c WHERE c.cutas = :cutas"),
    @NamedQuery(name = "Credito.findByInteres", query = "SELECT c FROM Credito c WHERE c.interes = :interes")})
public class Credito implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Basic(optional = false)
    @NotNull
    @Column(name = "valor", nullable = false)
    private float valor;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cutas", nullable = false)
    private int cutas;
    @Basic(optional = false)
    @NotNull
    @Column(name = "interes", nullable = false)
    private float interes;
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Usuario usuarioId;

    public Credito() {
    }

    public Credito(Integer id) {
        this.id = id;
    }

    public Credito(Integer id, float valor, int cutas, float interes) {
        this.id = id;
        this.valor = valor;
        this.cutas = cutas;
        this.interes = interes;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public int getCutas() {
        return cutas;
    }

    public void setCutas(int cutas) {
        this.cutas = cutas;
    }

    public float getInteres() {
        return interes;
    }

    public void setInteres(float interes) {
        this.interes = interes;
    }

    public Usuario getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Usuario usuarioId) {
        this.usuarioId = usuarioId;
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
        if (!(object instanceof Credito)) {
            return false;
        }
        Credito other = (Credito) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "johnarrieta.modelo.entidades.Credito[ id=" + id + " ]";
    }
    
}
