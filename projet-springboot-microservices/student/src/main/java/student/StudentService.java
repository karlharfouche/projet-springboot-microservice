package student;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getStudents(){
        return studentRepository.findAll();
    }

    public void addStudent(StudentRequest request){
        Student student = Student.builder()
                .firstName(request.firstName)
                .lastName(request.lastName)
                .email(request.email)
                .groupId(request.groupId)
                .build();
        studentRepository.save(student);
    }

    public void deleteStudent(Integer studentId){
        boolean exists = studentRepository.existsById(studentId);
        if(!exists) {
            throw new IllegalStateException("student with id " + studentId + " does not exists");
        }
        studentRepository.deleteById(studentId);
    }
}
