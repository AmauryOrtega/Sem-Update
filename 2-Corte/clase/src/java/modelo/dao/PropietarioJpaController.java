/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.dao;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelo.dao.entidades.Telefono;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import modelo.dao.entidades.Propietario;
import modelo.dao.exceptions.IllegalOrphanException;
import modelo.dao.exceptions.NonexistentEntityException;
import modelo.dao.exceptions.PreexistingEntityException;
import modelo.dao.exceptions.RollbackFailureException;

/**
 *
 * @author aroc
 */
public class PropietarioJpaController implements Serializable {

    public PropietarioJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Propietario propietario) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (propietario.getTelefonoCollection() == null) {
            propietario.setTelefonoCollection(new ArrayList<Telefono>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Telefono> attachedTelefonoCollection = new ArrayList<Telefono>();
            for (Telefono telefonoCollectionTelefonoToAttach : propietario.getTelefonoCollection()) {
                telefonoCollectionTelefonoToAttach = em.getReference(telefonoCollectionTelefonoToAttach.getClass(), telefonoCollectionTelefonoToAttach.getId());
                attachedTelefonoCollection.add(telefonoCollectionTelefonoToAttach);
            }
            propietario.setTelefonoCollection(attachedTelefonoCollection);
            em.persist(propietario);
            for (Telefono telefonoCollectionTelefono : propietario.getTelefonoCollection()) {
                Propietario oldPropietarioIdOfTelefonoCollectionTelefono = telefonoCollectionTelefono.getPropietarioId();
                telefonoCollectionTelefono.setPropietarioId(propietario);
                telefonoCollectionTelefono = em.merge(telefonoCollectionTelefono);
                if (oldPropietarioIdOfTelefonoCollectionTelefono != null) {
                    oldPropietarioIdOfTelefonoCollectionTelefono.getTelefonoCollection().remove(telefonoCollectionTelefono);
                    oldPropietarioIdOfTelefonoCollectionTelefono = em.merge(oldPropietarioIdOfTelefonoCollectionTelefono);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findPropietario(propietario.getCedula()) != null) {
                throw new PreexistingEntityException("Propietario " + propietario + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Propietario propietario) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Propietario persistentPropietario = em.find(Propietario.class, propietario.getCedula());
            Collection<Telefono> telefonoCollectionOld = persistentPropietario.getTelefonoCollection();
            Collection<Telefono> telefonoCollectionNew = propietario.getTelefonoCollection();
            List<String> illegalOrphanMessages = null;
            for (Telefono telefonoCollectionOldTelefono : telefonoCollectionOld) {
                if (!telefonoCollectionNew.contains(telefonoCollectionOldTelefono)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Telefono " + telefonoCollectionOldTelefono + " since its propietarioId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Telefono> attachedTelefonoCollectionNew = new ArrayList<Telefono>();
            for (Telefono telefonoCollectionNewTelefonoToAttach : telefonoCollectionNew) {
                telefonoCollectionNewTelefonoToAttach = em.getReference(telefonoCollectionNewTelefonoToAttach.getClass(), telefonoCollectionNewTelefonoToAttach.getId());
                attachedTelefonoCollectionNew.add(telefonoCollectionNewTelefonoToAttach);
            }
            telefonoCollectionNew = attachedTelefonoCollectionNew;
            propietario.setTelefonoCollection(telefonoCollectionNew);
            propietario = em.merge(propietario);
            for (Telefono telefonoCollectionNewTelefono : telefonoCollectionNew) {
                if (!telefonoCollectionOld.contains(telefonoCollectionNewTelefono)) {
                    Propietario oldPropietarioIdOfTelefonoCollectionNewTelefono = telefonoCollectionNewTelefono.getPropietarioId();
                    telefonoCollectionNewTelefono.setPropietarioId(propietario);
                    telefonoCollectionNewTelefono = em.merge(telefonoCollectionNewTelefono);
                    if (oldPropietarioIdOfTelefonoCollectionNewTelefono != null && !oldPropietarioIdOfTelefonoCollectionNewTelefono.equals(propietario)) {
                        oldPropietarioIdOfTelefonoCollectionNewTelefono.getTelefonoCollection().remove(telefonoCollectionNewTelefono);
                        oldPropietarioIdOfTelefonoCollectionNewTelefono = em.merge(oldPropietarioIdOfTelefonoCollectionNewTelefono);
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
                String id = propietario.getCedula();
                if (findPropietario(id) == null) {
                    throw new NonexistentEntityException("The propietario with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Propietario propietario;
            try {
                propietario = em.getReference(Propietario.class, id);
                propietario.getCedula();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The propietario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Telefono> telefonoCollectionOrphanCheck = propietario.getTelefonoCollection();
            for (Telefono telefonoCollectionOrphanCheckTelefono : telefonoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Propietario (" + propietario + ") cannot be destroyed since the Telefono " + telefonoCollectionOrphanCheckTelefono + " in its telefonoCollection field has a non-nullable propietarioId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(propietario);
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

    public List<Propietario> findPropietarioEntities() {
        return findPropietarioEntities(true, -1, -1);
    }

    public List<Propietario> findPropietarioEntities(int maxResults, int firstResult) {
        return findPropietarioEntities(false, maxResults, firstResult);
    }

    private List<Propietario> findPropietarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Propietario.class));
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

    public Propietario findPropietario(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Propietario.class, id);
        } finally {
            em.close();
        }
    }

    public int getPropietarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Propietario> rt = cq.from(Propietario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
