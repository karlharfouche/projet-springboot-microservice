package evaluator;

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
public class Evaluator {

    @Id
    @SequenceGenerator(
            name = "evaluator_id_sequence",
            sequenceName = "evaluator_id_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "evaluator_id_sequence"
    )
    private Integer evaluatorId;
    private String firstName;
    private String lastName;
    private String email;
}
