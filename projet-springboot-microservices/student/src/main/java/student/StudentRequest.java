package student;

public class StudentRequest {
    String firstName;
    String lastName;
    String email;
    Integer groupId;

    public StudentRequest(String firstName, String lastName, String email, Integer groupId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.groupId = groupId;
    }
}
