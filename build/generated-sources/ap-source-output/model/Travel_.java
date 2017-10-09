package model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import model.Car;
import model.Task;
import model.UserAccount;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-10-09T13:43:12")
@StaticMetamodel(Travel.class)
public class Travel_ { 

    public static volatile SingularAttribute<Travel, Integer> idTravel;
    public static volatile SingularAttribute<Travel, Date> timeEnd;
    public static volatile SingularAttribute<Travel, Car> carPlate;
    public static volatile SingularAttribute<Travel, Date> timeInitial;
    public static volatile SingularAttribute<Travel, Task> taskIdTask;
    public static volatile SingularAttribute<Travel, String> destination;
    public static volatile SingularAttribute<Travel, String> description;
    public static volatile ListAttribute<Travel, UserAccount> userAccountList;

}