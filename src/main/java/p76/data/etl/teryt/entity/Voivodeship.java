package p76.data.etl.teryt.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Data
@Getter
@Setter
@ToString
public class Voivodeship {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "terytSeq")
    @SequenceGenerator(name = "terytSeq", sequenceName = "terytSeq", initialValue = 5, allocationSize = 100)
    protected long id;

    private String name;
}
