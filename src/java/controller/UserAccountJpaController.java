/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import controller.exceptions.NonexistentEntityException;
import controller.exceptions.PreexistingEntityException;
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
import model.UserAccount;

/**
 *
 * @author gabri
 */
public class UserAccountJpaController implements Serializable {

    public UserAccountJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(UserAccount userAccount) throws PreexistingEntityException, Exception {
        if (userAccount.getViagemList() == null) {
            userAccount.setViagemList(new ArrayList<Viagem>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Viagem> attachedViagemList = new ArrayList<Viagem>();
            for (Viagem viagemListViagemToAttach : userAccount.getViagemList()) {
                viagemListViagemToAttach = em.getReference(viagemListViagemToAttach.getClass(), viagemListViagemToAttach.getIdViagem());
                attachedViagemList.add(viagemListViagemToAttach);
            }
            userAccount.setViagemList(attachedViagemList);
            em.persist(userAccount);
            for (Viagem viagemListViagem : userAccount.getViagemList()) {
                viagemListViagem.getUserAccountList().add(userAccount);
                viagemListViagem = em.merge(viagemListViagem);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUserAccount(userAccount.getUserLogin()) != null) {
                throw new PreexistingEntityException("UserAccount " + userAccount + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(UserAccount userAccount) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UserAccount persistentUserAccount = em.find(UserAccount.class, userAccount.getUserLogin());
            List<Viagem> viagemListOld = persistentUserAccount.getViagemList();
            List<Viagem> viagemListNew = userAccount.getViagemList();
            List<Viagem> attachedViagemListNew = new ArrayList<Viagem>();
            for (Viagem viagemListNewViagemToAttach : viagemListNew) {
                viagemListNewViagemToAttach = em.getReference(viagemListNewViagemToAttach.getClass(), viagemListNewViagemToAttach.getIdViagem());
                attachedViagemListNew.add(viagemListNewViagemToAttach);
            }
            viagemListNew = attachedViagemListNew;
            userAccount.setViagemList(viagemListNew);
            userAccount = em.merge(userAccount);
            for (Viagem viagemListOldViagem : viagemListOld) {
                if (!viagemListNew.contains(viagemListOldViagem)) {
                    viagemListOldViagem.getUserAccountList().remove(userAccount);
                    viagemListOldViagem = em.merge(viagemListOldViagem);
                }
            }
            for (Viagem viagemListNewViagem : viagemListNew) {
                if (!viagemListOld.contains(viagemListNewViagem)) {
                    viagemListNewViagem.getUserAccountList().add(userAccount);
                    viagemListNewViagem = em.merge(viagemListNewViagem);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = userAccount.getUserLogin();
                if (findUserAccount(id) == null) {
                    throw new NonexistentEntityException("The userAccount with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UserAccount userAccount;
            try {
                userAccount = em.getReference(UserAccount.class, id);
                userAccount.getUserLogin();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The userAccount with id " + id + " no longer exists.", enfe);
            }
            List<Viagem> viagemList = userAccount.getViagemList();
            for (Viagem viagemListViagem : viagemList) {
                viagemListViagem.getUserAccountList().remove(userAccount);
                viagemListViagem = em.merge(viagemListViagem);
            }
            em.remove(userAccount);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<UserAccount> findUserAccountEntities() {
        return findUserAccountEntities(true, -1, -1);
    }

    public List<UserAccount> findUserAccountEntities(int maxResults, int firstResult) {
        return findUserAccountEntities(false, maxResults, firstResult);
    }

    private List<UserAccount> findUserAccountEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(UserAccount.class));
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

    public UserAccount findUserAccount(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(UserAccount.class, id);
        } finally {
            em.close();
        }
    }

    public int getUserAccountCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<UserAccount> rt = cq.from(UserAccount.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
