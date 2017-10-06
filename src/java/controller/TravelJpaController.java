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
import model.Car;
import model.Task;
import model.UserAccount;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import model.Travel;

/**
 *
 * @author gabri
 */
public class TravelJpaController implements Serializable {

    public TravelJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Travel travel) {
        if (travel.getUserAccountList() == null) {
            travel.setUserAccountList(new ArrayList<UserAccount>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Car carPlate = travel.getCarPlate();
            if (carPlate != null) {
                carPlate = em.getReference(carPlate.getClass(), carPlate.getPlate());
                travel.setCarPlate(carPlate);
            }
            Task taskIdTask = travel.getTaskIdTask();
            if (taskIdTask != null) {
                taskIdTask = em.getReference(taskIdTask.getClass(), taskIdTask.getIdTask());
                travel.setTaskIdTask(taskIdTask);
            }
            List<UserAccount> attachedUserAccountList = new ArrayList<UserAccount>();
            for (UserAccount userAccountListUserAccountToAttach : travel.getUserAccountList()) {
                userAccountListUserAccountToAttach = em.getReference(userAccountListUserAccountToAttach.getClass(), userAccountListUserAccountToAttach.getUserLogin());
                attachedUserAccountList.add(userAccountListUserAccountToAttach);
            }
            travel.setUserAccountList(attachedUserAccountList);
            em.persist(travel);
            if (carPlate != null) {
                carPlate.getTravelList().add(travel);
                carPlate = em.merge(carPlate);
            }
            if (taskIdTask != null) {
                taskIdTask.getTravelList().add(travel);
                taskIdTask = em.merge(taskIdTask);
            }
            for (UserAccount userAccountListUserAccount : travel.getUserAccountList()) {
                userAccountListUserAccount.getTravelList().add(travel);
                userAccountListUserAccount = em.merge(userAccountListUserAccount);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Travel travel) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Travel persistentTravel = em.find(Travel.class, travel.getIdTravel());
            Car carPlateOld = persistentTravel.getCarPlate();
            Car carPlateNew = travel.getCarPlate();
            Task taskIdTaskOld = persistentTravel.getTaskIdTask();
            Task taskIdTaskNew = travel.getTaskIdTask();
            List<UserAccount> userAccountListOld = persistentTravel.getUserAccountList();
            List<UserAccount> userAccountListNew = travel.getUserAccountList();
            if (carPlateNew != null) {
                carPlateNew = em.getReference(carPlateNew.getClass(), carPlateNew.getPlate());
                travel.setCarPlate(carPlateNew);
            }
            if (taskIdTaskNew != null) {
                taskIdTaskNew = em.getReference(taskIdTaskNew.getClass(), taskIdTaskNew.getIdTask());
                travel.setTaskIdTask(taskIdTaskNew);
            }
            List<UserAccount> attachedUserAccountListNew = new ArrayList<UserAccount>();
            for (UserAccount userAccountListNewUserAccountToAttach : userAccountListNew) {
                userAccountListNewUserAccountToAttach = em.getReference(userAccountListNewUserAccountToAttach.getClass(), userAccountListNewUserAccountToAttach.getUserLogin());
                attachedUserAccountListNew.add(userAccountListNewUserAccountToAttach);
            }
            userAccountListNew = attachedUserAccountListNew;
            travel.setUserAccountList(userAccountListNew);
            travel = em.merge(travel);
            if (carPlateOld != null && !carPlateOld.equals(carPlateNew)) {
                carPlateOld.getTravelList().remove(travel);
                carPlateOld = em.merge(carPlateOld);
            }
            if (carPlateNew != null && !carPlateNew.equals(carPlateOld)) {
                carPlateNew.getTravelList().add(travel);
                carPlateNew = em.merge(carPlateNew);
            }
            if (taskIdTaskOld != null && !taskIdTaskOld.equals(taskIdTaskNew)) {
                taskIdTaskOld.getTravelList().remove(travel);
                taskIdTaskOld = em.merge(taskIdTaskOld);
            }
            if (taskIdTaskNew != null && !taskIdTaskNew.equals(taskIdTaskOld)) {
                taskIdTaskNew.getTravelList().add(travel);
                taskIdTaskNew = em.merge(taskIdTaskNew);
            }
            for (UserAccount userAccountListOldUserAccount : userAccountListOld) {
                if (!userAccountListNew.contains(userAccountListOldUserAccount)) {
                    userAccountListOldUserAccount.getTravelList().remove(travel);
                    userAccountListOldUserAccount = em.merge(userAccountListOldUserAccount);
                }
            }
            for (UserAccount userAccountListNewUserAccount : userAccountListNew) {
                if (!userAccountListOld.contains(userAccountListNewUserAccount)) {
                    userAccountListNewUserAccount.getTravelList().add(travel);
                    userAccountListNewUserAccount = em.merge(userAccountListNewUserAccount);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = travel.getIdTravel();
                if (findTravel(id) == null) {
                    throw new NonexistentEntityException("The travel with id " + id + " no longer exists.");
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
            Travel travel;
            try {
                travel = em.getReference(Travel.class, id);
                travel.getIdTravel();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The travel with id " + id + " no longer exists.", enfe);
            }
            Car carPlate = travel.getCarPlate();
            if (carPlate != null) {
                carPlate.getTravelList().remove(travel);
                carPlate = em.merge(carPlate);
            }
            Task taskIdTask = travel.getTaskIdTask();
            if (taskIdTask != null) {
                taskIdTask.getTravelList().remove(travel);
                taskIdTask = em.merge(taskIdTask);
            }
            List<UserAccount> userAccountList = travel.getUserAccountList();
            for (UserAccount userAccountListUserAccount : userAccountList) {
                userAccountListUserAccount.getTravelList().remove(travel);
                userAccountListUserAccount = em.merge(userAccountListUserAccount);
            }
            em.remove(travel);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Travel> findTravelEntities() {
        return findTravelEntities(true, -1, -1);
    }

    public List<Travel> findTravelEntities(int maxResults, int firstResult) {
        return findTravelEntities(false, maxResults, firstResult);
    }

    private List<Travel> findTravelEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Travel.class));
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

    public Travel findTravel(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Travel.class, id);
        } finally {
            em.close();
        }
    }

    public int getTravelCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Travel> rt = cq.from(Travel.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
