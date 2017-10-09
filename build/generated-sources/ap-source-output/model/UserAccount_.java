package model;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import model.Travel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-10-09T13:43:12")
@StaticMetamodel(UserAccount.class)
public class UserAccount_ { 

    public static volatile SingularAttribute<UserAccount, String> userLogin;
    public static volatile SingularAttribute<UserAccount, String> password;
    public static volatile SingularAttribute<UserAccount, String> phone;
    public static volatile ListAttribute<UserAccount, Travel> travelList;
    public static volatile SingularAttribute<UserAccount, String> userName;
    public static volatile SingularAttribute<UserAccount, String> sector;
    public static volatile SingularAttribute<UserAccount, String> email;

}