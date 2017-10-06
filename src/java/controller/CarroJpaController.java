/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import controller.exceptions.IllegalOrphanException;
import controller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.Viagem;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import model.Carro;

/**
 *
 * @author gabri
 */
public class CarroJpaController implements Serializable {

    public CarroJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Carro carro) {
        if (carro.getViagemList() == null) {
            carro.setViagemList(new ArrayList<Viagem>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Viagem> attachedViagemList = new ArrayList<Viagem>();
            for (Viagem viagemListViagemToAttach : carro.getViagemList()) {
                viagemListViagemToAttach = em.getReference(viagemListViagemToAttach.getClass(), viagemListViagemToAttach.getIdViagem());
                attachedViagemList.add(viagemListViagemToAttach);
            }
            carro.setViagemList(attachedViagemList);
            em.persist(carro);
            for (Viagem viagemListViagem : carro.getViagemList()) {
                Carro oldCarroIdCarroOfViagemListViagem = viagemListViagem.getCarroIdCarro();
                viagemListViagem.setCarroIdCarro(carro);
                viagemListViagem = em.merge(viagemListViagem);
                if (oldCarroIdCarroOfViagemListViagem != null) {
                    oldCarroIdCarroOfViagemListViagem.getViagemList().remove(viagemListViagem);
                    oldCarroIdCarroOfViagemListViagem = em.merge(oldCarroIdCarroOfViagemListViagem);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Carro carro) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Carro persistentCarro = em.find(Carro.class, carro.getIdCarro());
            List<Viagem> viagemListOld = persistentCarro.getViagemList();
            List<Viagem> viagemListNew = carro.getViagemList();
            List<String> illegalOrphanMessages = null;
            for (Viagem viagemListOldViagem : viagemListOld) {
                if (!viagemListNew.contains(viagemListOldViagem)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Viagem " + viagemListOldViagem + " since its carroIdCarro field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Viagem> attachedViagemListNew = new ArrayList<Viagem>();
            for (Viagem viagemListNewViagemToAttach : viagemListNew) {
                viagemListNewViagemToAttach = em.getReference(viagemListNewViagemToAttach.getClass(), viagemListNewViagemToAttach.getIdViagem());
                attachedViagemListNew.add(viagemListNewViagemToAttach);
            }
            viagemListNew = attachedViagemListNew;
            carro.setViagemList(viagemListNew);
            carro = em.merge(carro);
            for (Viagem viagemListNewViagem : viagemListNew) {
                if (!viagemListOld.contains(viagemListNewViagem)) {
                    Carro oldCarroIdCarroOfViagemListNewViagem = viagemListNewViagem.getCarroIdCarro();
                    viagemListNewViagem.setCarroIdCarro(carro);
                    viagemListNewViagem = em.merge(viagemListNewViagem);
                    if (oldCarroIdCarroOfViagemListNewViagem != null && !oldCarroIdCarroOfViagemListNewViagem.equals(carro)) {
                        oldCarroIdCarroOfViagemListNewViagem.getViagemList().remove(viagemListNewViagem);
                        oldCarroIdCarroOfViagemListNewViagem = em.merge(oldCarroIdCarroOfViagemListNewViagem);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = carro.getIdCarro();
                if (findCarro(id) == null) {
                    throw new NonexistentEntityException("The carro with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Carro carro;
            try {
                carro = em.getReference(Carro.class, id);
                carro.getIdCarro();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The carro with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Viagem> viagemListOrphanCheck = carro.getViagemList();
            for (Viagem viagemListOrphanCheckViagem : viagemListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Carro (" + carro + ") cannot be destroyed since the Viagem " + viagemListOrphanCheckViagem + " in its viagemList field has a non-nullable carroIdCarro field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(carro);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Carro> findCarroEntities() {
        return findCarroEntities(true, -1, -1);
    }

    public List<Carro> findCarroEntities(int maxResults, int firstResult) {
        return findCarroEntities(false, maxResults, firstResult);
    }

    private List<Carro> findCarroEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Carro.class));
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

    public Carro findCarro(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Carro.class, id);
        } finally {
            em.close();
        }
    }

    public int getCarroCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Carro> rt = cq.from(Carro.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
