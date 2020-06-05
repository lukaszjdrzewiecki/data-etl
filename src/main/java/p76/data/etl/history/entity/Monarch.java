package p76.data.etl.history.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Getter
@Setter
@ToString
@Entity
public class Monarch {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String reignStart;
    private String reignEnd;
    private String name;
    private String dateOfBirth;
    private String dateOfDeath;
    private String dynasty;
    private String country;
}
