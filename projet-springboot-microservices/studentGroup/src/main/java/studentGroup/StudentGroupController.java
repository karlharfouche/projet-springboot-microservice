package studentGroup;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/group")
public class StudentGroupController {

    private final StudentGroupService groupService;

    @Autowired
    public StudentGroupController(StudentGroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping
    public List<StudentGroup> getStudentGroups(){
        return groupService.getStudentGroups();
    }

    @PostMapping
    public void addStudentGroup(@RequestBody StudentGroup group){
        groupService.addStudentGroup(group);
    }

    @DeleteMapping(path="{groupId}")
    public void DeleteStudentGroup(@PathVariable("groupId") Integer id){
        groupService.deleteStudentGroup(id);
    }

    @PutMapping(path="{groupId}")
    public void updateStudentGroup(
            @PathVariable("groupId") Integer groupId,
            @RequestBody StudentGroup group) {
        groupService.updateStudentGroup(groupId, group.getFypTitle());

    }
}
