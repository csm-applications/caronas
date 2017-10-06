package view;

import model.UserAccount;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import controller.UserAccountJpaController;
import controller.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "UserAccountManagedBean")
@SessionScoped
public class UserAccountManagedBean {

    private ArrayList<UserAccount> listOfUserAccounts = new ArrayList<>();
    private UserAccount ActualUserAccount = new UserAccount();
    private UserAccountJpaController controlUserAccount = new UserAccountJpaController(EmProvider.getInstance().getEntityManagerFactory());

    public UserAccountManagedBean() {
    }

    //gotos
    public String gotoAddAccounts() {
        ActualUserAccount = new UserAccount();
        return "/public/manageAccounts/addAccounts.xhtml?faces-redirect=true";
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
