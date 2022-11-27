package studentGroup;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class StudentGroup {

    @Id
    @SequenceGenerator(
            name = "studentGroup_id_sequence",
            sequenceName = "studentGroup_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "studentGroup_id_sequence"
    )
    private Integer groupId;
    private String fypTitle;


    public String getFypTitle() {
        return fypTitle;
    }

    public void setFypTitle(String fypTitle) {
        this.fypTitle = fypTitle;
    }

}
