package org.brightblock.temp;

import java.util.Map;

import org.brightblock.temp.proto.BrightblockProto.Course;


public class CourseRepository {
	Map<Integer, Course> courses;

	public CourseRepository (Map<Integer, Course> courses) {
		this.courses = courses;
	}

	public Course getCourse(int id) {
		return courses.get(id);
	}

}
