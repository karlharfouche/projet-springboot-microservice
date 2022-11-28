package student;

import evaluation.Evaluation;
import evaluation.EvaluationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import studentGroup.StudentGroup;
import studentGroup.StudentGroupRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@ComponentScan(basePackages = "studentGroup")
@ComponentScan(basePackages = "evaluation")
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentGroupRepository studentGroupRepository;
    private final EvaluationRepository evaluationRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository, StudentGroupRepository studentGroupRepository, EvaluationRepository evaluationRepository) {
        this.studentRepository = studentRepository;
        this.studentGroupRepository = studentGroupRepository;
        this.evaluationRepository = evaluationRepository;
    }

    public List<Student> getStudents(){
        return studentRepository.findAll();
    }

    public void addStudent(Student student){
        Optional<Student> studentOptional = studentRepository.
                findStudentByEmail(student.getEmail());

        Optional<StudentGroup> studentGroupOptional = studentGroupRepository.
                findGroupId(student.getGroupId());

        if(studentOptional.isPresent()) {
            throw new IllegalStateException("email taken");
        }

        if(!studentGroupOptional.isPresent()) {
            throw new IllegalStateException("Group id not found");
        }

        studentRepository.save(student);
    }

    public void deleteStudent(Integer studentId){
        boolean exists = studentRepository.existsById(studentId);
        if(!exists) {
            throw new IllegalStateException("student with id " + studentId + " does not exists");
        }
        studentRepository.deleteById(studentId);
    }

    @Transactional
    public void updateStudent(Integer studentId, String firstName, String lastName, String email, String matricule, Integer groupId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalStateException("student with id " + studentId + " does not exists"));

        if (firstName != null && firstName.length() > 0 && !Objects.equals(firstName, student.getFirstName())) {
            student.setFirstName(firstName);
        }

        if (lastName != null && lastName.length() > 0 && !Objects.equals(lastName, student.getLastName())) {
            student.setLastName(lastName);
        }

        if (groupId != null && !Objects.equals(groupId, student.getGroupId())) {
            student.setGroupId(groupId);
        }

        if (matricule != null && !Objects.equals(matricule, student.getMatricule())) {
            student.setMatricule(matricule);
        }

        if (email != null && email.length() > 0 && !Objects.equals(email, student.getEmail())) {
            Optional<Student> studentOptional = studentRepository.findStudentByEmail(email);
            if (studentOptional.isPresent()) {
                throw new IllegalStateException("email taken");
            }
            student.setEmail(email);
        }
    }

    public List<Student> getStudentsByGroupId(Integer groupId){
        return studentRepository.findStudentsByGroupId(groupId);
    }

    @Transactional
    public Double calculateGrade(Integer studentId){
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalStateException("student with id " + studentId + " does not exists"));

            List<Evaluation> evaluations = evaluationRepository.findByStudentId(studentId);
            List<Double> percentages = new ArrayList<Double>() {{
                add(0.1);
                add(0.15);
                add(0.1);
                add(0.1);
                add(0.2);
                add(0.1);
                add(0.1);
                add(0.15);
            }};
            double grade = 0.0;
            for (int i = 0; i < 8; i++) {
                double tmpGrade = 0;
                for (Evaluation evaluation : evaluations) {
                    tmpGrade += evaluation.getCriterias().get(i);
                }
                tmpGrade /= evaluations.size();
                grade += tmpGrade * percentages.get(i);
            }
            if(grade != student.getGrade())  student.setGrade(grade * 5);
        return student.getGrade();
    }
}
