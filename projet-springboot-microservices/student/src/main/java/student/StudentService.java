package student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import studentGroup.StudentGroup;
import studentGroup.StudentGroupRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@ComponentScan(basePackages = "studentGroup")
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentGroupRepository studentGroupRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository, StudentGroupRepository studentGroupRepository) {
        this.studentRepository = studentRepository;
        this.studentGroupRepository = studentGroupRepository;
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

        if (groupId != null && !Objects.equals(groupId, student.getStudentGroupId())) {
            student.setStudentGroupId(groupId);
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
}
