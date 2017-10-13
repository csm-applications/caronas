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
import model.Travel;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import model.Task;
import model.UserAccount;

/**
 *
 * @author USER
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
        if (userAccount.getTravelList() == null) {
            userAccount.setTravelList(new ArrayList<Travel>());
        }
        if (userAccount.getTaskList() == null) {
            userAccount.setTaskList(new ArrayList<Task>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Travel> attachedTravelList = new ArrayList<Travel>();
            for (Travel travelListTravelToAttach : userAccount.getTravelList()) {
                travelListTravelToAttach = em.getReference(travelListTravelToAttach.getClass(), travelListTravelToAttach.getIdTravel());
                attachedTravelList.add(travelListTravelToAttach);
            }
            userAccount.setTravelList(attachedTravelList);
            List<Task> attachedTaskList = new ArrayList<Task>();
            for (Task taskListTaskToAttach : userAccount.getTaskList()) {
                taskListTaskToAttach = em.getReference(taskListTaskToAttach.getClass(), taskListTaskToAttach.getIdTask());
                attachedTaskList.add(taskListTaskToAttach);
            }
            userAccount.setTaskList(attachedTaskList);
            em.persist(userAccount);
            for (Travel travelListTravel : userAccount.getTravelList()) {
                travelListTravel.getUserAccountList().add(userAccount);
                travelListTravel = em.merge(travelListTravel);
            }
            for (Task taskListTask : userAccount.getTaskList()) {
                UserAccount oldUseraccountuserLoginOfTaskListTask = taskListTask.getUseraccountuserLogin();
                taskListTask.setUseraccountuserLogin(userAccount);
                taskListTask = em.merge(taskListTask);
                if (oldUseraccountuserLoginOfTaskListTask != null) {
                    oldUseraccountuserLoginOfTaskListTask.getTaskList().remove(taskListTask);
                    oldUseraccountuserLoginOfTaskListTask = em.merge(oldUseraccountuserLoginOfTaskListTask);
                }
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
            List<Travel> travelListOld = persistentUserAccount.getTravelList();
            List<Travel> travelListNew = userAccount.getTravelList();
            List<Task> taskListOld = persistentUserAccount.getTaskList();
            List<Task> taskListNew = userAccount.getTaskList();
            List<Travel> attachedTravelListNew = new ArrayList<Travel>();
            for (Travel travelListNewTravelToAttach : travelListNew) {
                travelListNewTravelToAttach = em.getReference(travelListNewTravelToAttach.getClass(), travelListNewTravelToAttach.getIdTravel());
                attachedTravelListNew.add(travelListNewTravelToAttach);
            }
            travelListNew = attachedTravelListNew;
            userAccount.setTravelList(travelListNew);
            List<Task> attachedTaskListNew = new ArrayList<Task>();
            for (Task taskListNewTaskToAttach : taskListNew) {
                taskListNewTaskToAttach = em.getReference(taskListNewTaskToAttach.getClass(), taskListNewTaskToAttach.getIdTask());
                attachedTaskListNew.add(taskListNewTaskToAttach);
            }
            taskListNew = attachedTaskListNew;
            userAccount.setTaskList(taskListNew);
            userAccount = em.merge(userAccount);
            for (Travel travelListOldTravel : travelListOld) {
                if (!travelListNew.contains(travelListOldTravel)) {
                    travelListOldTravel.getUserAccountList().remove(userAccount);
                    travelListOldTravel = em.merge(travelListOldTravel);
                }
            }
            for (Travel travelListNewTravel : travelListNew) {
                if (!travelListOld.contains(travelListNewTravel)) {
                    travelListNewTravel.getUserAccountList().add(userAccount);
                    travelListNewTravel = em.merge(travelListNewTravel);
                }
            }
            for (Task taskListOldTask : taskListOld) {
                if (!taskListNew.contains(taskListOldTask)) {
                    taskListOldTask.setUseraccountuserLogin(null);
                    taskListOldTask = em.merge(taskListOldTask);
                }
            }
            for (Task taskListNewTask : taskListNew) {
                if (!taskListOld.contains(taskListNewTask)) {
                    UserAccount oldUseraccountuserLoginOfTaskListNewTask = taskListNewTask.getUseraccountuserLogin();
                    taskListNewTask.setUseraccountuserLogin(userAccount);
                    taskListNewTask = em.merge(taskListNewTask);
                    if (oldUseraccountuserLoginOfTaskListNewTask != null && !oldUseraccountuserLoginOfTaskListNewTask.equals(userAccount)) {
                        oldUseraccountuserLoginOfTaskListNewTask.getTaskList().remove(taskListNewTask);
                        oldUseraccountuserLoginOfTaskListNewTask = em.merge(oldUseraccountuserLoginOfTaskListNewTask);
                    }
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
            List<Travel> travelList = userAccount.getTravelList();
            for (Travel travelListTravel : travelList) {
                travelListTravel.getUserAccountList().remove(userAccount);
                travelListTravel = em.merge(travelListTravel);
            }
            List<Task> taskList = userAccount.getTaskList();
            for (Task taskListTask : taskList) {
                taskListTask.setUseraccountuserLogin(null);
                taskListTask = em.merge(taskListTask);
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
