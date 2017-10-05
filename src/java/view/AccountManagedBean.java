package view;

import model.Account;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import controller.AccountJpaController;

@ManagedBean
@RequestScoped

public class AccountManagedBean {

    private Account ActualAccount = new Account();
    private AccountJpaController controlAccount = new AccountJpaController(EmProvider.getInstance().getEntityManagerFactory());
    public AccountManagedBean() {
    }

    public void saveAccount() {
        try {
            controlAccount.create(ActualAccount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Account getActualAccount() {
        return ActualAccount;
    }

    public void setActualAccount(Account actualAccount) {
        this.ActualAccount = actualAccount;
    }
}
