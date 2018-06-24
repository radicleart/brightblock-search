package org.brightblock.lightening.service;

import java.util.Map;

import org.brightblock.temp.proto.BrightblockProto.Course;
import org.springframework.stereotype.Service;

@Service
public class LndRepository {
	Map<Integer, Course> courses;

	public LndRepository () {
	}

}
