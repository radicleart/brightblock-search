package org.brightblock.temp.web;

import org.brightblock.temp.CourseRepository;
import org.brightblock.temp.proto.BrightblockProto.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LNDSyncController {

	@Autowired private CourseRepository courseRepo;

	@RequestMapping("/courses/{id}")
    Course customer(@PathVariable Integer id) {
        return courseRepo.getCourse(id);
    }
}
