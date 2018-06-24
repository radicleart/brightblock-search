package org.brightblock.lightening.conf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.brightblock.temp.CourseRepository;
import org.brightblock.temp.proto.BrightblockProto.Course;
import org.brightblock.temp.proto.BrightblockProto.Student;
import org.brightblock.temp.proto.BrightblockProto.Student.PhoneNumber;
import org.brightblock.temp.proto.BrightblockProto.Student.PhoneType;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@ComponentScan("org.brightblock")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

    @Bean
    ProtobufHttpMessageConverter protobufHttpMessageConverter() {
        return new ProtobufHttpMessageConverter();
    }

    @Bean
    RestTemplate restTemplate(ProtobufHttpMessageConverter hmc) {
        return new RestTemplate(Arrays.asList(hmc));
    }
    
    @Bean
    public CourseRepository createTestCourses() {
        Map<Integer, Course> courses = new HashMap<>();
        Course course1 = Course.newBuilder()
          .setId(1)
          .setCourseName("REST with Spring")
          .addAllStudent(createTestStudents())
          .build();
        Course course2 = Course.newBuilder()
          .setId(2)
          .setCourseName("Learn Spring Security")
          .addAllStudent(new ArrayList<Student>())
          .build();
        courses.put(course1.getId(), course1);
        courses.put(course2.getId(), course2);
        return new CourseRepository(courses);
    }

	private Iterable<? extends Student> createTestStudents() {
		Set<Student> students = new HashSet<Student>();
		PhoneType pt = PhoneType.LANDLINE;
		PhoneNumber ph = PhoneNumber.newBuilder()
				.setNumber("0127345645654")
				.setType(pt)
				.build();
		Student s1 = Student.newBuilder()
				.setEmail("john.flow@gmail.com")
				.setFirstName("john")
				.setLastName("flow")
				.addPhone(ph)
				.build();
		Student s2 = Student.newBuilder()
				.setEmail("pete.fletcher@gmail.com")
				.setFirstName("pete")
				.setLastName("fletcher")
				.build();
		Student s3 = Student.newBuilder()
				.setEmail("janet.barnet@gmail.com")
				.setFirstName("janet")
				.setLastName("barnet")
				.build();
		students.add(s1);
		students.add(s2);
		students.add(s3);
		return students;
	}
}
