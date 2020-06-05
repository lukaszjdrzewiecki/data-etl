package p76.data.etl.teryt.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 * identifiers and names of localities,
 * SIMC - miejscowosci
 */
@Data
@Getter
@Setter
@ToString
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Simc extends Teryt {

    //WOJ;POW;GMI;RODZ_GMI;RM;MZ;NAZWA;SYM;SYMPOD;STAN_NA
    private String rodzGmi;
    private String rm;
    private String mz;
    private String nazwa;
    private String sym;
    private String sympod;
}
