package model;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import model.Travel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-10-09T13:43:12")
@StaticMetamodel(Car.class)
public class Car_ { 

    public static volatile ListAttribute<Car, Travel> travelList;
    public static volatile SingularAttribute<Car, String> plate;
    public static volatile SingularAttribute<Car, String> model;
    public static volatile SingularAttribute<Car, String> brand;
    public static volatile SingularAttribute<Car, String> situation;

}