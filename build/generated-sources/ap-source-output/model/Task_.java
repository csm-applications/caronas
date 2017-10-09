package model;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import model.Travel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-10-09T13:43:12")
@StaticMetamodel(Task.class)
public class Task_ { 

    public static volatile SingularAttribute<Task, Integer> idTask;
    public static volatile SingularAttribute<Task, String> name;
    public static volatile SingularAttribute<Task, String> description;
    public static volatile ListAttribute<Task, Travel> travelList;
    public static volatile SingularAttribute<Task, String> time;

}