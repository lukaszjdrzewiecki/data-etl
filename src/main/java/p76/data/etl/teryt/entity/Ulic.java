package p76.data.etl.teryt.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 * Street - ULIC
 */
@Data
@Getter
@Setter
@ToString
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Ulic extends Teryt {

    ////WOJ;POW;GMI;RODZ_GMI;SYM;SYM_UL;CECHA;NAZWA_1;NAZWA_2;STAN_NA
    private String rodzGmi;
    private String sym;
    private String symUl;
    private String cecha;
    private String nazwa1;
    private String nazwa2;
}
