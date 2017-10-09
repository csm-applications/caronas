package view;

import controller.CarJpaController;
import controller.TravelJpaController;
import controller.exceptions.NonexistentEntityException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import model.Car;
import model.Travel;
import org.primefaces.event.SelectEvent;
import view.EmProvider;
import view.CarManagedBean;

@ManagedBean
@SessionScoped

public class TravelManagedBean {

    //Actual
    private Travel actualTravel = new Travel();
    private Car actualCar = new Car();
    //List
    private ArrayList<Travel> listOfTravels = new ArrayList<>();
    private ArrayList<Car> listOfCars = new ArrayList<>();
    //Controller
    private TravelJpaController controlTravel = new TravelJpaController(EmProvider.getInstance().getEntityManagerFactory());
    private CarJpaController controlCar = new CarJpaController(EmProvider.getInstance().getEntityManagerFactory());
    //String
    private String dateInitial;
    private String dateEnd;

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

    public void loadTravels() {
        listOfTravels = new ArrayList(controlTravel.findTravelEntities());
    }

    public void loadCars() {
        listOfCars = new ArrayList(controlCar.findCarEntities());
    }

    public String saveTravels() {
        String a = "cheguei";
        System.out.println(a);
        try {
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
            Date toInsertInitial = formato.parse(dateInitial);
            Date toInsertEnd = formato.parse(dateEnd);
            actualTravel.setTimeInitial(toInsertInitial);
            actualTravel.setTimeEnd(toInsertEnd);
            controlTravel.create(actualTravel);
            System.out.println(toInsertInitial);
            System.out.println(toInsertEnd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gotoListTravels();
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
        controlTravel.destroy(actualTravel.getIdTravel());
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

    public String getDateInitial() {
        return dateInitial;
    }

    public void setDateInitial(String dateInitial) {
        this.dateInitial = dateInitial;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

}
