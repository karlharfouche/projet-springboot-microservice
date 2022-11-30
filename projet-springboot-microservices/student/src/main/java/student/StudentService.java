package student;

import evaluation.Evaluation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import studentGroup.StudentGroup;


import javax.transaction.Transactional;
import java.util.*;

@Service

public class StudentService {

    private final StudentRepository studentRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public StudentService(StudentRepository studentRepository, RestTemplate restTemplate) {
        this.studentRepository = studentRepository;
        this.restTemplate = restTemplate;
    }

    public List<Student> getStudents(){
        return studentRepository.findAll();

    }

    public void addStudent(Student student){
        Optional<Student> studentOptional = studentRepository.
                findStudentByEmail(student.getEmail());

        StudentGroup studentGroup = restTemplate.getForObject(
                "http://GROUP/api/v1/group/{groupId}",
                StudentGroup.class, student.getGroupId()
        );

        if(studentGroup==null){
            throw new IllegalStateException("Group with id " + student.getGroupId() + " not found");
        }
        if(studentOptional.isPresent()) {
            throw new IllegalStateException("email taken");
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

        ResponseEntity<Evaluation[]> responseEvaluations = restTemplate.getForEntity(
                "http://EVALUATION/api/v1/evaluation/student/{studentId}",
                Evaluation[].class, student.getStudentId()
        );
        Evaluation[] evaluations = responseEvaluations.getBody();
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
            assert evaluations != null;
            for (Evaluation evaluation : evaluations) {
                System.out.println(evaluation);
                tmpGrade += evaluation.getCriterias().get(i);
            }
            tmpGrade /= evaluations.length;
            grade += tmpGrade * percentages.get(i);
        }
            student.setGrade(grade * 5);
        return student.getGrade();
    }

}
