package view;

import controller.CarJpaController;
import controller.TaskJpaController;
import controller.TravelJpaController;
import controller.exceptions.IllegalOrphanException;
import controller.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import model.Car;
import model.Task;
import model.Travel;

@ManagedBean
@SessionScoped

public class TravelManagedBean {

    //Actual
    private Travel actualTravel = new Travel();
    private Car actualCar = new Car();
    private Task actualTask = new Task();
    //List
    private ArrayList<Travel> listOfTravels = new ArrayList<>();
    private ArrayList<Car> listOfCars = new ArrayList<>();
    private ArrayList<Task> listOfTasks = new ArrayList<>();
    //Controller
    private TravelJpaController controlTravel = new TravelJpaController(EmProvider.getInstance().getEntityManagerFactory());
    private CarJpaController controlCar = new CarJpaController(EmProvider.getInstance().getEntityManagerFactory());
    private TaskJpaController controlTask = new TaskJpaController(EmProvider.getInstance().getEntityManagerFactory());
    //String
    private Date dateInitial;
    private Date dateEnd;

    public TravelManagedBean() {
    }

    public String gotoAddTravel() {
        actualTravel = new Travel();
        loadCars();
        return "/public/manageTravel/addTravel.xhtml?faces-redirect=true";
    }

    public String gotoListTravels() {
        loadTravels();
        return "/public/manageTravel/ManageTravel.xhtml?faces-redirect=true";
    }
    
    public String gotoDetails() {
        loadTasks();
        return "/public/manageTravel/detailsTravel.xhtml?faces-redirect=true";
    }

    public void loadTravels() {
        listOfTravels = new ArrayList(controlTravel.findTravelEntities());
    }

    public void loadCars() {
        listOfCars = new ArrayList(controlCar.findCarEntities());
    }
    
    public void loadTasks(){
        listOfTasks = new ArrayList<>(actualTravel.getTaskList());
    }

    public String saveTravels() {
        try {
            actualTravel.setCarPlate(actualCar);
            actualTravel.setTimeInitial(dateInitial);
            actualTravel.setTimeEnd(dateEnd);
            controlTravel.create(actualTravel);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gotoListTravels();
    }
    
    public void saveTask() {
        try {
            actualTask.setTravelIdTravel(actualTravel);
            controlTask.create(actualTask);
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

    public void destroyTravels() throws NonexistentEntityException {
        try {
            controlTravel.destroy(actualTravel.getIdTravel());
        } catch (IllegalOrphanException ex) {
            Logger.getLogger(TravelManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        gotoListTravels();
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
    
    

  

}
