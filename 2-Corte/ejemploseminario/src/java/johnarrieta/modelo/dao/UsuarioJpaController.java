/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package johnarrieta.modelo.dao;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import johnarrieta.modelo.entidades.Credito;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import johnarrieta.modelo.dao.exceptions.IllegalOrphanException;
import johnarrieta.modelo.dao.exceptions.NonexistentEntityException;
import johnarrieta.modelo.dao.exceptions.RollbackFailureException;
import johnarrieta.modelo.entidades.Usuario;

/**
 *
 * @author Estudiante
 */
public class UsuarioJpaController implements Serializable {

    public UsuarioJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) throws RollbackFailureException, Exception {
        if (usuario.getCreditoList() == null) {
            usuario.setCreditoList(new ArrayList<Credito>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Credito> attachedCreditoList = new ArrayList<Credito>();
            for (Credito creditoListCreditoToAttach : usuario.getCreditoList()) {
                creditoListCreditoToAttach = em.getReference(creditoListCreditoToAttach.getClass(), creditoListCreditoToAttach.getId());
                attachedCreditoList.add(creditoListCreditoToAttach);
            }
            usuario.setCreditoList(attachedCreditoList);
            em.persist(usuario);
            for (Credito creditoListCredito : usuario.getCreditoList()) {
                Usuario oldUsuarioIdOfCreditoListCredito = creditoListCredito.getUsuarioId();
                creditoListCredito.setUsuarioId(usuario);
                creditoListCredito = em.merge(creditoListCredito);
                if (oldUsuarioIdOfCreditoListCredito != null) {
                    oldUsuarioIdOfCreditoListCredito.getCreditoList().remove(creditoListCredito);
                    oldUsuarioIdOfCreditoListCredito = em.merge(oldUsuarioIdOfCreditoListCredito);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuario usuario) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getId());
            List<Credito> creditoListOld = persistentUsuario.getCreditoList();
            List<Credito> creditoListNew = usuario.getCreditoList();
            List<String> illegalOrphanMessages = null;
            for (Credito creditoListOldCredito : creditoListOld) {
                if (!creditoListNew.contains(creditoListOldCredito)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Credito " + creditoListOldCredito + " since its usuarioId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Credito> attachedCreditoListNew = new ArrayList<Credito>();
            for (Credito creditoListNewCreditoToAttach : creditoListNew) {
                creditoListNewCreditoToAttach = em.getReference(creditoListNewCreditoToAttach.getClass(), creditoListNewCreditoToAttach.getId());
                attachedCreditoListNew.add(creditoListNewCreditoToAttach);
            }
            creditoListNew = attachedCreditoListNew;
            usuario.setCreditoList(creditoListNew);
            usuario = em.merge(usuario);
            for (Credito creditoListNewCredito : creditoListNew) {
                if (!creditoListOld.contains(creditoListNewCredito)) {
                    Usuario oldUsuarioIdOfCreditoListNewCredito = creditoListNewCredito.getUsuarioId();
                    creditoListNewCredito.setUsuarioId(usuario);
                    creditoListNewCredito = em.merge(creditoListNewCredito);
                    if (oldUsuarioIdOfCreditoListNewCredito != null && !oldUsuarioIdOfCreditoListNewCredito.equals(usuario)) {
                        oldUsuarioIdOfCreditoListNewCredito.getCreditoList().remove(creditoListNewCredito);
                        oldUsuarioIdOfCreditoListNewCredito = em.merge(oldUsuarioIdOfCreditoListNewCredito);
                    }
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = usuario.getId();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Credito> creditoListOrphanCheck = usuario.getCreditoList();
            for (Credito creditoListOrphanCheckCredito : creditoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Credito " + creditoListOrphanCheckCredito + " in its creditoList field has a non-nullable usuarioId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(usuario);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Usuario findUsuario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
