package view;

import controller.TravelJpaController;
import controller.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import model.Travel;
import view.EmProvider;

@ManagedBean
@SessionScoped

public class TravelManagedBean {
    
    private ArrayList<Travel> listOfTravels = new ArrayList<>();
    private Travel actualTravel = new Travel();
    private TravelJpaController controlTravel = new TravelJpaController(EmProvider.getInstance().getEntityManagerFactory());

    public TravelManagedBean() {
    }
    
    public String gotoAddTravel(){
        actualTravel = new Travel();
        return "/public/manageTravel/addTravel.xhtml";
    }
    
    public String gotoListTravels(){
       loadTravels();
       return "/public/manageTravel/ManageTravel.xhtml";
    }
    
    public void loadTravels(){
        listOfTravels = new ArrayList(controlTravel.findTravelEntities());
    }

    public String saveTravels() {
        try {
            controlTravel.create(actualTravel);
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
}
