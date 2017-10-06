package model;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import model.Viagem;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-10-06T11:09:00")
@StaticMetamodel(Carro.class)
public class Carro_ { 

    public static volatile SingularAttribute<Carro, String> marca;
    public static volatile SingularAttribute<Carro, String> situacao;
    public static volatile ListAttribute<Carro, Viagem> viagemList;
    public static volatile SingularAttribute<Carro, Integer> idCarro;
    public static volatile SingularAttribute<Carro, String> modelo;
    public static volatile SingularAttribute<Carro, String> placa;

}