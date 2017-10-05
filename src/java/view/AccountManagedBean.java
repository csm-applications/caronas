package view;

import model.Account;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import controller.AccountJpaController;
import java.util.ArrayList;

@ManagedBean
@RequestScoped

public class AccountManagedBean {

    private ArrayList<Account> listAccounts = new ArrayList<>();

    private Account ActualAccount = new Account();
    private AccountJpaController controlAccount = new AccountJpaController(EmProvider.getInstance().getEntityManagerFactory());

    public AccountManagedBean() {
    }

    public void listAccounts() {
        listAccounts = new ArrayList(controlAccount.findAccountEntities());
    }

    public String saveAccount() {
        try {
            controlAccount.create(ActualAccount);
            listAccounts();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "ManageAccounts.xhtml";
    }

    public Account getActualAccount() {
        return ActualAccount;
    }

    public void setActualAccount(Account actualAccount) {
        this.ActualAccount = actualAccount;
    }

    public ArrayList<Account> getListAccounts() {
        return listAccounts;
    }

    public void setListAccounts(ArrayList<Account> listAccounts) {
        this.listAccounts = listAccounts;
    }

}
