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
import model.Tarefa;

/**
 *
 * @author gabri
 */
public class TarefaJpaController implements Serializable {

    public TarefaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tarefa tarefa) {
        if (tarefa.getViagemList() == null) {
            tarefa.setViagemList(new ArrayList<Viagem>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Viagem> attachedViagemList = new ArrayList<Viagem>();
            for (Viagem viagemListViagemToAttach : tarefa.getViagemList()) {
                viagemListViagemToAttach = em.getReference(viagemListViagemToAttach.getClass(), viagemListViagemToAttach.getIdViagem());
                attachedViagemList.add(viagemListViagemToAttach);
            }
            tarefa.setViagemList(attachedViagemList);
            em.persist(tarefa);
            for (Viagem viagemListViagem : tarefa.getViagemList()) {
                Tarefa oldTarefaIdTarefaOfViagemListViagem = viagemListViagem.getTarefaIdTarefa();
                viagemListViagem.setTarefaIdTarefa(tarefa);
                viagemListViagem = em.merge(viagemListViagem);
                if (oldTarefaIdTarefaOfViagemListViagem != null) {
                    oldTarefaIdTarefaOfViagemListViagem.getViagemList().remove(viagemListViagem);
                    oldTarefaIdTarefaOfViagemListViagem = em.merge(oldTarefaIdTarefaOfViagemListViagem);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tarefa tarefa) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tarefa persistentTarefa = em.find(Tarefa.class, tarefa.getIdTarefa());
            List<Viagem> viagemListOld = persistentTarefa.getViagemList();
            List<Viagem> viagemListNew = tarefa.getViagemList();
            List<String> illegalOrphanMessages = null;
            for (Viagem viagemListOldViagem : viagemListOld) {
                if (!viagemListNew.contains(viagemListOldViagem)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Viagem " + viagemListOldViagem + " since its tarefaIdTarefa field is not nullable.");
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
            tarefa.setViagemList(viagemListNew);
            tarefa = em.merge(tarefa);
            for (Viagem viagemListNewViagem : viagemListNew) {
                if (!viagemListOld.contains(viagemListNewViagem)) {
                    Tarefa oldTarefaIdTarefaOfViagemListNewViagem = viagemListNewViagem.getTarefaIdTarefa();
                    viagemListNewViagem.setTarefaIdTarefa(tarefa);
                    viagemListNewViagem = em.merge(viagemListNewViagem);
                    if (oldTarefaIdTarefaOfViagemListNewViagem != null && !oldTarefaIdTarefaOfViagemListNewViagem.equals(tarefa)) {
                        oldTarefaIdTarefaOfViagemListNewViagem.getViagemList().remove(viagemListNewViagem);
                        oldTarefaIdTarefaOfViagemListNewViagem = em.merge(oldTarefaIdTarefaOfViagemListNewViagem);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tarefa.getIdTarefa();
                if (findTarefa(id) == null) {
                    throw new NonexistentEntityException("The tarefa with id " + id + " no longer exists.");
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
            Tarefa tarefa;
            try {
                tarefa = em.getReference(Tarefa.class, id);
                tarefa.getIdTarefa();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tarefa with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Viagem> viagemListOrphanCheck = tarefa.getViagemList();
            for (Viagem viagemListOrphanCheckViagem : viagemListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tarefa (" + tarefa + ") cannot be destroyed since the Viagem " + viagemListOrphanCheckViagem + " in its viagemList field has a non-nullable tarefaIdTarefa field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tarefa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tarefa> findTarefaEntities() {
        return findTarefaEntities(true, -1, -1);
    }

    public List<Tarefa> findTarefaEntities(int maxResults, int firstResult) {
        return findTarefaEntities(false, maxResults, firstResult);
    }

    private List<Tarefa> findTarefaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tarefa.class));
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

    public Tarefa findTarefa(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tarefa.class, id);
        } finally {
            em.close();
        }
    }

    public int getTarefaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tarefa> rt = cq.from(Tarefa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
