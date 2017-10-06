package model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import model.Carro;
import model.Tarefa;
import model.UserAccount;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-10-06T11:09:00")
@StaticMetamodel(Viagem.class)
public class Viagem_ { 

    public static volatile SingularAttribute<Viagem, Carro> carroIdCarro;
    public static volatile SingularAttribute<Viagem, Date> horaTermino;
    public static volatile SingularAttribute<Viagem, Tarefa> tarefaIdTarefa;
    public static volatile SingularAttribute<Viagem, String> destino;
    public static volatile SingularAttribute<Viagem, Integer> idViagem;
    public static volatile ListAttribute<Viagem, UserAccount> userAccountList;
    public static volatile SingularAttribute<Viagem, Date> horaInicio;
    public static volatile SingularAttribute<Viagem, String> descricao;

}