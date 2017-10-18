package view;

import javax.faces.bean.ManagedBean;
import controller.CarJpaController;
import controller.SectorJpaController;
import controller.UserAccountJpaController;
import controller.exceptions.IllegalOrphanException;
import controller.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import model.Car;
import model.Sector;
import model.Travel;
import model.UserAccount;

@ManagedBean(name = "CarManagedBean")
@SessionScoped
public class CarManagedBean {

    //lists
    private ArrayList<Car> listOfCars = new ArrayList<>();
    private ArrayList<Sector> listOfSectors = new ArrayList<>();

    //actual
    private Sector actualSector = new Sector();
    private Car actualCar = new Car();

    //coontrollers
    private CarJpaController controlCar = new CarJpaController(EmProvider.getInstance().getEntityManagerFactory());
    private SectorJpaController controlSector = new SectorJpaController(EmProvider.getInstance().getEntityManagerFactory());
    private UserAccountJpaController controlUserAccount = new UserAccountJpaController(EmProvider.getInstance().getEntityManagerFactory());

    //aux
    private String filterSituation;

    public CarManagedBean() {
    }

    //gotos
    public String gotoAddCars() {
        actualCar = new Car();
        return "/public/manageCar/addCars.xhtml?faces-redirect=true";
    }

    public String gotoListCars() {
        loadCars();
        return "/public/manageCar/ManageCars.xhtml?faces-redirect=true";
    }

    public String gotoEditCars() {
        UserAccount myself = controlUserAccount.findUserAccount(ManageSessions.getUserId());

        if (myself.getIsAdministrator() || myself.getSectoridSector().getName().equals(actualCar.getSectoridSector().getName())) {
            return "/public/manageCar/EditCars.xhtml?faces-redirect=true";
        } else {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Você não tem Permissão para editar este carro", "!"));
            return "#";
        }
    }

    //loads
    public void loadCars() {
        listOfCars = new ArrayList(controlCar.findCarEntities());
    }

    public void loadSectors() {
        listOfSectors = new ArrayList(controlSector.findSectorEntities());
    }

    //filter
    public void filterBySectors() {
        listOfCars = new ArrayList<>(actualSector.getCarList());
    }

    public void filterBySituation() {
        listOfCars.clear();
        EntityManager em = EmProvider.getInstance().getEntityManagerFactory().createEntityManager();
        List<Car> cars = em.createQuery("SELECT c FROM Car c WHERE c.situation = :situation", Car.class)
                .setParameter("situation", filterSituation )
                .getResultList();
        listOfCars = new ArrayList<>(cars);
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
        UserAccount myself = controlUserAccount.findUserAccount(ManageSessions.getUserId());
        try {
            if (myself.getIsAdministrator() || myself.getSectoridSector().getName().equals(actualCar.getSectoridSector().getName())) {
                controlCar.destroy(actualCar.getPlate());
            } else {
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Você não tem Permissão para remover este carro", "!"));
            }
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

    public ArrayList<Sector> getListOfSectors() {
        return listOfSectors;
    }

    public void setListOfSectors(ArrayList<Sector> listOfSectors) {
        this.listOfSectors = listOfSectors;
    }

    public Sector getActualSector() {
        return actualSector;
    }

    public void setActualSector(Sector actualSector) {
        this.actualSector = actualSector;
    }

    public String getFilterSituation() {
        return filterSituation;
    }

    public void setFilterSituation(String filterSituation) {
        this.filterSituation = filterSituation;
    }

}
