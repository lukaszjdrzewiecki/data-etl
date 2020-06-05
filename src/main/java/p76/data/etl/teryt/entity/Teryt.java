package p76.data.etl.teryt.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Data
@Getter
@Setter
@ToString
@MappedSuperclass
public class Teryt {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "terytSeq")
    @SequenceGenerator(name = "terytSeq", sequenceName = "terytSeq", initialValue = 5, allocationSize = 100)
    protected long id;

    protected String woj;
    protected String pow;
    protected String gmi;
    protected String stanNa;
}
