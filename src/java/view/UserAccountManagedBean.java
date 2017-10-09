package view;

import model.UserAccount;
import javax.faces.bean.ManagedBean;
import controller.UserAccountJpaController;
import controller.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import javax.faces.bean.SessionScoped;
import javax.servlet.http.HttpSession;

@ManagedBean(name = "UserAccountManagedBean")
@SessionScoped
public class UserAccountManagedBean {

    private ArrayList<UserAccount> listOfUserAccounts = new ArrayList<>();
    private UserAccount ActualUserAccount = new UserAccount();
    private UserAccountJpaController controlUserAccount = new UserAccountJpaController(EmProvider.getInstance().getEntityManagerFactory());

    public UserAccountManagedBean() {
    }

    //gotos
    public String gotoIndex(){
        return "index?faces-redirect=true";
    }
            
    public String gotoAddAccounts() {
        ActualUserAccount = new UserAccount();
        return "addAccounts.xhtml?faces-redirect=true";
    }

    public String gotoListUsers() {
        loadUserAccounts();
        return "/public/manageAccounts/ManageUserAccounts.xhtml?faces-redirect=true";
    }

    public String gotoEditUsers() {
        return "/public/manageAccounts/EditAccounts.xhtml?faces-redirect=true";
    }

    public void loadUserAccounts() {
        listOfUserAccounts = new ArrayList(controlUserAccount.findUserAccountEntities());
    }
    
    public boolean isAnyoneLoggedIn(){
        boolean isLogado = false;
        if(ManageSessions.getUserName() != null){
            isLogado = true;
        }
        return isLogado;
    }
    
       public String validateUsernamePassword() {
        if (validateUser(ActualUserAccount)) {
            HttpSession session = ManageSessions.getSession();
            session.setAttribute("username", ActualUserAccount.getUserLogin());
            return "/public/manageTravel/ManageTravel?faces-redirect=true";
        } else {
            
            return "/index?faces-redirect=true";
        }
    }
       
    public String logout(){
        HttpSession session = ManageSessions.getSession();
        session.invalidate();
        return "/index?faces-redirect=true";
    }

    public boolean validateUser(UserAccount comparar) {
        UserAccount validate = controlUserAccount.findUserAccount(ActualUserAccount.getUserLogin());
        if (validate == null) {
            return false;
        }
        if (validate.getUserLogin().equals(comparar.getUserLogin())
                && validate.getPassword().equals(comparar.getPassword())) {
            return true;
        }
        return false;
    }

    public String saveUserAccount() {
        try {
            controlUserAccount.create(ActualUserAccount);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gotoListUsers();
    }

    public String editUserAccount() {
        try {
            controlUserAccount.edit(ActualUserAccount);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return gotoListUsers();
    }

    public void destroyUserAccounts() throws NonexistentEntityException {
        controlUserAccount.destroy(ActualUserAccount.getUserLogin());
        gotoListUsers();
    }

    public UserAccount getActualUserAccount() {
        return ActualUserAccount;
    }

    public void setActualUserAccount(UserAccount actualUserAccount) {
        this.ActualUserAccount = actualUserAccount;
    }

    public ArrayList<UserAccount> getListOfUserAccounts() {
        return listOfUserAccounts;
    }

    public void setListOfUserAccounts(ArrayList<UserAccount> listOfUserAccounts) {
        this.listOfUserAccounts = listOfUserAccounts;
    }
}
