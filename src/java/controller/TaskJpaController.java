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
import model.Travel;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import model.Task;

/**
 *
 * @author gabri
 */
public class TaskJpaController implements Serializable {

    public TaskJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Task task) {
        if (task.getTravelList() == null) {
            task.setTravelList(new ArrayList<Travel>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Travel> attachedTravelList = new ArrayList<Travel>();
            for (Travel travelListTravelToAttach : task.getTravelList()) {
                travelListTravelToAttach = em.getReference(travelListTravelToAttach.getClass(), travelListTravelToAttach.getIdTravel());
                attachedTravelList.add(travelListTravelToAttach);
            }
            task.setTravelList(attachedTravelList);
            em.persist(task);
            for (Travel travelListTravel : task.getTravelList()) {
                Task oldTaskIdTaskOfTravelListTravel = travelListTravel.getTaskIdTask();
                travelListTravel.setTaskIdTask(task);
                travelListTravel = em.merge(travelListTravel);
                if (oldTaskIdTaskOfTravelListTravel != null) {
                    oldTaskIdTaskOfTravelListTravel.getTravelList().remove(travelListTravel);
                    oldTaskIdTaskOfTravelListTravel = em.merge(oldTaskIdTaskOfTravelListTravel);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Task task) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Task persistentTask = em.find(Task.class, task.getIdTask());
            List<Travel> travelListOld = persistentTask.getTravelList();
            List<Travel> travelListNew = task.getTravelList();
            List<Travel> attachedTravelListNew = new ArrayList<Travel>();
            for (Travel travelListNewTravelToAttach : travelListNew) {
                travelListNewTravelToAttach = em.getReference(travelListNewTravelToAttach.getClass(), travelListNewTravelToAttach.getIdTravel());
                attachedTravelListNew.add(travelListNewTravelToAttach);
            }
            travelListNew = attachedTravelListNew;
            task.setTravelList(travelListNew);
            task = em.merge(task);
            for (Travel travelListOldTravel : travelListOld) {
                if (!travelListNew.contains(travelListOldTravel)) {
                    travelListOldTravel.setTaskIdTask(null);
                    travelListOldTravel = em.merge(travelListOldTravel);
                }
            }
            for (Travel travelListNewTravel : travelListNew) {
                if (!travelListOld.contains(travelListNewTravel)) {
                    Task oldTaskIdTaskOfTravelListNewTravel = travelListNewTravel.getTaskIdTask();
                    travelListNewTravel.setTaskIdTask(task);
                    travelListNewTravel = em.merge(travelListNewTravel);
                    if (oldTaskIdTaskOfTravelListNewTravel != null && !oldTaskIdTaskOfTravelListNewTravel.equals(task)) {
                        oldTaskIdTaskOfTravelListNewTravel.getTravelList().remove(travelListNewTravel);
                        oldTaskIdTaskOfTravelListNewTravel = em.merge(oldTaskIdTaskOfTravelListNewTravel);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = task.getIdTask();
                if (findTask(id) == null) {
                    throw new NonexistentEntityException("The task with id " + id + " no longer exists.");
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
            Task task;
            try {
                task = em.getReference(Task.class, id);
                task.getIdTask();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The task with id " + id + " no longer exists.", enfe);
            }
            List<Travel> travelList = task.getTravelList();
            for (Travel travelListTravel : travelList) {
                travelListTravel.setTaskIdTask(null);
                travelListTravel = em.merge(travelListTravel);
            }
            em.remove(task);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Task> findTaskEntities() {
        return findTaskEntities(true, -1, -1);
    }

    public List<Task> findTaskEntities(int maxResults, int firstResult) {
        return findTaskEntities(false, maxResults, firstResult);
    }

    private List<Task> findTaskEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Task.class));
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

    public Task findTask(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Task.class, id);
        } finally {
            em.close();
        }
    }

    public int getTaskCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Task> rt = cq.from(Task.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
