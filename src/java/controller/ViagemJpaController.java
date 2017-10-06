/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import controller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.Carro;
import model.Tarefa;
import model.UserAccount;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import model.Viagem;

/**
 *
 * @author gabri
 */
public class ViagemJpaController implements Serializable {

    public ViagemJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Viagem viagem) {
        if (viagem.getUserAccountList() == null) {
            viagem.setUserAccountList(new ArrayList<UserAccount>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Carro carroIdCarro = viagem.getCarroIdCarro();
            if (carroIdCarro != null) {
                carroIdCarro = em.getReference(carroIdCarro.getClass(), carroIdCarro.getIdCarro());
                viagem.setCarroIdCarro(carroIdCarro);
            }
            Tarefa tarefaIdTarefa = viagem.getTarefaIdTarefa();
            if (tarefaIdTarefa != null) {
                tarefaIdTarefa = em.getReference(tarefaIdTarefa.getClass(), tarefaIdTarefa.getIdTarefa());
                viagem.setTarefaIdTarefa(tarefaIdTarefa);
            }
            List<UserAccount> attachedUserAccountList = new ArrayList<UserAccount>();
            for (UserAccount userAccountListUserAccountToAttach : viagem.getUserAccountList()) {
                userAccountListUserAccountToAttach = em.getReference(userAccountListUserAccountToAttach.getClass(), userAccountListUserAccountToAttach.getUserLogin());
                attachedUserAccountList.add(userAccountListUserAccountToAttach);
            }
            viagem.setUserAccountList(attachedUserAccountList);
            em.persist(viagem);
            if (carroIdCarro != null) {
                carroIdCarro.getViagemList().add(viagem);
                carroIdCarro = em.merge(carroIdCarro);
            }
            if (tarefaIdTarefa != null) {
                tarefaIdTarefa.getViagemList().add(viagem);
                tarefaIdTarefa = em.merge(tarefaIdTarefa);
            }
            for (UserAccount userAccountListUserAccount : viagem.getUserAccountList()) {
                userAccountListUserAccount.getViagemList().add(viagem);
                userAccountListUserAccount = em.merge(userAccountListUserAccount);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Viagem viagem) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Viagem persistentViagem = em.find(Viagem.class, viagem.getIdViagem());
            Carro carroIdCarroOld = persistentViagem.getCarroIdCarro();
            Carro carroIdCarroNew = viagem.getCarroIdCarro();
            Tarefa tarefaIdTarefaOld = persistentViagem.getTarefaIdTarefa();
            Tarefa tarefaIdTarefaNew = viagem.getTarefaIdTarefa();
            List<UserAccount> userAccountListOld = persistentViagem.getUserAccountList();
            List<UserAccount> userAccountListNew = viagem.getUserAccountList();
            if (carroIdCarroNew != null) {
                carroIdCarroNew = em.getReference(carroIdCarroNew.getClass(), carroIdCarroNew.getIdCarro());
                viagem.setCarroIdCarro(carroIdCarroNew);
            }
            if (tarefaIdTarefaNew != null) {
                tarefaIdTarefaNew = em.getReference(tarefaIdTarefaNew.getClass(), tarefaIdTarefaNew.getIdTarefa());
                viagem.setTarefaIdTarefa(tarefaIdTarefaNew);
            }
            List<UserAccount> attachedUserAccountListNew = new ArrayList<UserAccount>();
            for (UserAccount userAccountListNewUserAccountToAttach : userAccountListNew) {
                userAccountListNewUserAccountToAttach = em.getReference(userAccountListNewUserAccountToAttach.getClass(), userAccountListNewUserAccountToAttach.getUserLogin());
                attachedUserAccountListNew.add(userAccountListNewUserAccountToAttach);
            }
            userAccountListNew = attachedUserAccountListNew;
            viagem.setUserAccountList(userAccountListNew);
            viagem = em.merge(viagem);
            if (carroIdCarroOld != null && !carroIdCarroOld.equals(carroIdCarroNew)) {
                carroIdCarroOld.getViagemList().remove(viagem);
                carroIdCarroOld = em.merge(carroIdCarroOld);
            }
            if (carroIdCarroNew != null && !carroIdCarroNew.equals(carroIdCarroOld)) {
                carroIdCarroNew.getViagemList().add(viagem);
                carroIdCarroNew = em.merge(carroIdCarroNew);
            }
            if (tarefaIdTarefaOld != null && !tarefaIdTarefaOld.equals(tarefaIdTarefaNew)) {
                tarefaIdTarefaOld.getViagemList().remove(viagem);
                tarefaIdTarefaOld = em.merge(tarefaIdTarefaOld);
            }
            if (tarefaIdTarefaNew != null && !tarefaIdTarefaNew.equals(tarefaIdTarefaOld)) {
                tarefaIdTarefaNew.getViagemList().add(viagem);
                tarefaIdTarefaNew = em.merge(tarefaIdTarefaNew);
            }
            for (UserAccount userAccountListOldUserAccount : userAccountListOld) {
                if (!userAccountListNew.contains(userAccountListOldUserAccount)) {
                    userAccountListOldUserAccount.getViagemList().remove(viagem);
                    userAccountListOldUserAccount = em.merge(userAccountListOldUserAccount);
                }
            }
            for (UserAccount userAccountListNewUserAccount : userAccountListNew) {
                if (!userAccountListOld.contains(userAccountListNewUserAccount)) {
                    userAccountListNewUserAccount.getViagemList().add(viagem);
                    userAccountListNewUserAccount = em.merge(userAccountListNewUserAccount);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = viagem.getIdViagem();
                if (findViagem(id) == null) {
                    throw new NonexistentEntityException("The viagem with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Viagem viagem;
            try {
                viagem = em.getReference(Viagem.class, id);
                viagem.getIdViagem();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The viagem with id " + id + " no longer exists.", enfe);
            }
            Carro carroIdCarro = viagem.getCarroIdCarro();
            if (carroIdCarro != null) {
                carroIdCarro.getViagemList().remove(viagem);
                carroIdCarro = em.merge(carroIdCarro);
            }
            Tarefa tarefaIdTarefa = viagem.getTarefaIdTarefa();
            if (tarefaIdTarefa != null) {
                tarefaIdTarefa.getViagemList().remove(viagem);
                tarefaIdTarefa = em.merge(tarefaIdTarefa);
            }
            List<UserAccount> userAccountList = viagem.getUserAccountList();
            for (UserAccount userAccountListUserAccount : userAccountList) {
                userAccountListUserAccount.getViagemList().remove(viagem);
                userAccountListUserAccount = em.merge(userAccountListUserAccount);
            }
            em.remove(viagem);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Viagem> findViagemEntities() {
        return findViagemEntities(true, -1, -1);
    }

    public List<Viagem> findViagemEntities(int maxResults, int firstResult) {
        return findViagemEntities(false, maxResults, firstResult);
    }

    private List<Viagem> findViagemEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Viagem.class));
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

    public Viagem findViagem(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Viagem.class, id);
        } finally {
            em.close();
        }
    }

    public int getViagemCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Viagem> rt = cq.from(Viagem.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
