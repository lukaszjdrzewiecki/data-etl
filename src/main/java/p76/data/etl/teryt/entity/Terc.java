package p76.data.etl.teryt.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 * Terc - identifiers and names of units of territorial division,
 */
@Data
@Getter
@Setter
@ToString
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Terc extends Teryt {

    //WOJ;POW;GMI;RODZ;NAZWA;NAZWA_DOD;STAN_NA
    private String rodz;
    private String nazwa;
    private String nazwaDod;

    private String unitType;

    public String mapUnitType() {
        switch (rodz) {
            case "1" : return "gmina miejska";
            case "2" : return "gmina wiejska";
            case "3" : return "gmina miejsko-wiejska";
            case "4" : return "miasto w gminie miejsko-wiejskiej";
            case "5" : return "obszar wiejski w gminie miejsko-wiejskiej";
            case "8" : return "dzielnica w m.st. Warszawa";
            case "9" : return "delegatury miast: Kraków, Łódź, Poznań i Wrocław";
            default : return "unknown";
        }
    }

}
