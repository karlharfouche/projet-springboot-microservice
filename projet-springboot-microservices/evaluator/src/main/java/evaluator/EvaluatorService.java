package evaluator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@ComponentScan(basePackages = "evaluatorGroup")
public class EvaluatorService {

    private final EvaluatorRepository evaluatorRepository;

    @Autowired
    public EvaluatorService(EvaluatorRepository evaluatorRepository) {
        this.evaluatorRepository = evaluatorRepository;
    }

    public List<Evaluator> getEvaluators(){
        return evaluatorRepository.findAll();
    }

    public void addEvaluator(Evaluator evaluator){
        Optional<Evaluator> evaluatorOptional = evaluatorRepository.
                findEvaluatorByEmail(evaluator.getEmail());

        if(evaluatorOptional.isPresent()) {
            throw new IllegalStateException("Evaluator with this email already exists");
        }

        evaluatorRepository.save(evaluator);
    }

    public void deleteEvaluator(Integer evaluatorId){
        boolean exists = evaluatorRepository.existsById(evaluatorId);
        if(!exists) {
            throw new IllegalStateException("Evaluator with id " + evaluatorId + " does not exists");
        }
        evaluatorRepository.deleteById(evaluatorId);
    }

    @Transactional
    public void updateEvaluator(Integer evaluatorId, String firstName, String lastName, String email) {
        Evaluator evaluator = evaluatorRepository.findById(evaluatorId)
                .orElseThrow(() -> new IllegalStateException("evaluator with id " + evaluatorId + " does not exists"));

        if (firstName != null && firstName.length() > 0 && !Objects.equals(firstName, evaluator.getFirstName())) {
            evaluator.setFirstName(firstName);
        }

        if (lastName != null && lastName.length() > 0 && !Objects.equals(lastName, evaluator.getLastName())) {
            evaluator.setLastName(lastName);
        }

        if (email != null && email.length() > 0 && !Objects.equals(email, evaluator.getEmail())) {
            Optional<Evaluator> evaluatorOptional = evaluatorRepository.findEvaluatorByEmail(email);
            if (evaluatorOptional.isPresent()) {
                throw new IllegalStateException("email taken");
            }
            evaluator.setEmail(email);
        }
    }
}
