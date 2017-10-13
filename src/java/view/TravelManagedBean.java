package view;

import controller.CarJpaController;
import controller.TaskJpaController;
import controller.TravelJpaController;
import controller.UserAccountJpaController;
import controller.exceptions.IllegalOrphanException;
import controller.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import model.Car;
import model.Task;
import model.Travel;
import model.UserAccount;
import org.primefaces.model.DualListModel;

@ManagedBean
@SessionScoped

public class TravelManagedBean {

    //Actual
    private Travel actualTravel = new Travel();
    private Car actualCar = new Car();
    private Task actualTask = new Task();
    private UserAccount actualUserAccount = new UserAccount();
    //List
    private ArrayList<Travel> listOfTravels = new ArrayList<>();
    private ArrayList<Car> listOfCars = new ArrayList<>();
    private ArrayList<Task> listOfTasks = new ArrayList<>();
    private ArrayList<UserAccount> listOfUserAccounts = new ArrayList<>();
    //Controller
    private TravelJpaController controlTravel = new TravelJpaController(EmProvider.getInstance().getEntityManagerFactory());
    private CarJpaController controlCar = new CarJpaController(EmProvider.getInstance().getEntityManagerFactory());
    private TaskJpaController controlTask = new TaskJpaController(EmProvider.getInstance().getEntityManagerFactory());
    private UserAccountJpaController controlUser = new UserAccountJpaController(EmProvider.getInstance().getEntityManagerFactory());

    //auxiliarys
    private Date dateInitial;
    private Date dateEnd;
    private DualListModel<String> users = new DualListModel<>();

    public TravelManagedBean() {
    }

    public String gotoAddTravel() {
        actualTravel = new Travel();
        loadCars();
        loadUsers();
        return "/public/manageTravel/addTravel.xhtml?faces-redirect=true";
    }

    public String gotoListTravels() {
        loadTravels();
        return "/public/manageTravel/ManageTravel.xhtml?faces-redirect=true";
    }

    public String gotoDetails() {
        loadTasks();
        actualTask = new Task();
        return "/public/manageTravel/detailsTravel.xhtml?faces-redirect=true";
    }

    public void loadTravels() {
        listOfTravels = new ArrayList(controlTravel.findTravelEntities());
    }

    public void loadCars() {
        listOfCars = new ArrayList(controlCar.findCarEntities());
    }

    public void loadUsers() {
        listOfUserAccounts = new ArrayList(controlUser.findUserAccountEntities());
        List<String> source = new ArrayList<>();
        List<String> target = new ArrayList<>();

        for (UserAccount a : listOfUserAccounts) {
            source.add(a.getUserLogin());
        }
        users = new DualListModel<String>(source, target);
    }

    public void loadTasks() {
        actualTravel = controlTravel.findTravel(actualTravel.getIdTravel());
        listOfTasks = new ArrayList<>(actualTravel.getTaskList());
    }

    public String saveTravels() {
        List<UserAccount> usersToAdd = new ArrayList<>();
        for (String a : users.getTarget()) {
            usersToAdd.add(controlUser.findUserAccount(a));
        }
        try {
            actualTravel.setUserAccountList(usersToAdd);
            actualTravel.setCarPlate(actualCar);
            actualTravel.setTimeInitial(dateInitial);
            actualTravel.setTimeEnd(dateEnd);
            controlTravel.create(actualTravel);
            return gotoListTravels();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "#";
    }

    public void submitToThisTrip() {
        List<UserAccount> listToEdit = actualTravel.getUserAccountList();
        try {
            UserAccount toAdd = controlUser.findUserAccount(ManageSessions.getUserName());
            for (UserAccount a : listToEdit) {
                if (a.getUserLogin().equals(ManageSessions.getUserName())) {
                    FacesContext context = FacesContext.getCurrentInstance();
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Você já está nessa viagem", "!"));
                    return;
                }
            }
            listToEdit.add(toAdd);
            controlTravel.edit(actualTravel);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void removeUserFromTravel() {
        List<UserAccount> listToEdit = actualTravel.getUserAccountList();
        try {
            if (isThisMyself(actualUserAccount) || actualUserAccount.getIsAdministrator()) {
                listToEdit.remove(actualUserAccount);
                actualTravel.setUserAccountList(listToEdit);
                controlTravel.edit(actualTravel);
            } else {
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Opa! você não tem permissão pra remover outras pessoas de uma viagem", "!"));
                return;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void saveTask() {
        UserAccount myself = controlUser.findUserAccount(ManageSessions.getUserName());
        try {
            actualTask.setUseraccountuserLogin(myself);
            actualTask.setTravelIdTravel(actualTravel);
            controlTask.create(actualTask);
            actualTask = new Task();
            loadTasks();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String editUserAccount() {
        try {
            controlTravel.edit(actualTravel);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return gotoListTravels();
    }

    public void destroyTravels() {
        try {
            for (Task t : actualTravel.getTaskList()) {
                controlTask.destroy(t.getIdTask());
            }
            controlTravel.destroy(actualTravel.getIdTravel());
            loadTravels();
        } catch (IllegalOrphanException ex) {
            Logger.getLogger(TravelManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(TravelManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void destroyTask() {
        UserAccount myself = controlUser.findUserAccount(ManageSessions.getUserName());
        try {
            if (isThisMyself(actualTask.getUseraccountuserLogin()) || myself.getIsAdministrator()) {
                controlTask.destroy(actualTask.getIdTask());
                loadTasks();
            } else {
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Opa! você não tem permissão para excluir tarefas de outras pessoas", "!"));
                return;
            }
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(TravelManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean isThisMyself(UserAccount toVerify) {
        String userInSession = ManageSessions.getUserName();
        if (userInSession.equals(toVerify.getUserLogin())) {
            return true;
        }
        return false;
    }

    public ArrayList<Travel> getListOfTravels() {
        return listOfTravels;
    }

    public void setListOfTravels(ArrayList<Travel> listOfTravels) {
        this.listOfTravels = listOfTravels;
    }

    public Travel getActualTravel() {
        return actualTravel;
    }

    public void setActualTravel(Travel actualTravel) {
        this.actualTravel = actualTravel;
    }

    public Car getActualCar() {
        return actualCar;
    }

    public void setActualCar(Car actualCar) {
        this.actualCar = actualCar;
    }

    public ArrayList<Car> getListOfCars() {
        return listOfCars;
    }

    public void setListOfCars(ArrayList<Car> listOfCars) {
        this.listOfCars = listOfCars;
    }

    public Date getDateInitial() {
        return dateInitial;
    }

    public void setDateInitial(Date dateInitial) {
        this.dateInitial = dateInitial;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public Task getActualTask() {
        return actualTask;
    }

    public void setActualTask(Task actualTask) {
        this.actualTask = actualTask;
    }

    public ArrayList<Task> getListOfTasks() {
        return listOfTasks;
    }

    public void setListOfTasks(ArrayList<Task> listOfTasks) {
        this.listOfTasks = listOfTasks;
    }

    public UserAccount getActualUserAccount() {
        return actualUserAccount;
    }

    public void setActualUserAccount(UserAccount actualUserAccount) {
        this.actualUserAccount = actualUserAccount;
    }

    public ArrayList<UserAccount> getListOfUserAccounts() {
        return listOfUserAccounts;
    }

    public void setListOfUserAccounts(ArrayList<UserAccount> listOfUserAccounts) {
        this.listOfUserAccounts = listOfUserAccounts;
    }

    public DualListModel<String> getUsers() {
        return users;
    }

    public void setUsers(DualListModel<String> users) {
        this.users = users;
    }

}
