package model;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import model.Viagem;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-10-06T11:15:38")
@StaticMetamodel(Tarefa.class)
public class Tarefa_ { 

    public static volatile ListAttribute<Tarefa, Viagem> viagemList;
    public static volatile SingularAttribute<Tarefa, String> hora;
    public static volatile SingularAttribute<Tarefa, String> nome;
    public static volatile SingularAttribute<Tarefa, Integer> idTarefa;
    public static volatile SingularAttribute<Tarefa, String> descricao;

}