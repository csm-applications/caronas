package view;

import javax.faces.bean.ManagedBean;
import controller.CarJpaController;
import controller.exceptions.IllegalOrphanException;
import controller.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import javax.faces.bean.SessionScoped;
import model.Car;

@ManagedBean(name="CarManagedBean")
@SessionScoped
public class CarManagedBean {

    private ArrayList<Car> listOfCars = new ArrayList<>();
    private Car actualCar = new Car();
    private CarJpaController controlCar = new CarJpaController(EmProvider.getInstance().getEntityManagerFactory());

    public CarManagedBean() {
    }
    
    //gotos
    public String gotoAddCars(){
        actualCar = new Car();
        return "/public/manageCar/addCars.xhtml?faces-redirect=true";
    }
    
    public String gotoListCars(){
        loadCars();
        return "/public/manageCar/ManageCars.xhtml?faces-redirect=true";
    }
    
    public String gotoEditCars(){
        return "/public/manageCar/EditCars.xhtml?faces-redirect=true";
    }

    public void loadCars() {
        listOfCars = new ArrayList(controlCar.findCarEntities());
    }

    public String saveCars() {
        try {
            controlCar.create(actualCar);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gotoListCars();
    }
  
    public String editCar() {
        try {
            controlCar.edit(actualCar);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return gotoListCars();
    }

    public void destroyCars() throws NonexistentEntityException {
        try {
            controlCar.destroy(actualCar.getPlate());
        } catch (IllegalOrphanException ex) {
            ex.printStackTrace();
        }
        gotoListCars();
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
}
