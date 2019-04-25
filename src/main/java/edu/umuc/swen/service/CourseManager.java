package edu.umuc.swen.service;

import edu.umuc.swen.domain.Course;
import edu.umuc.swen.domain.Student;
import edu.umuc.swen.domain.Term;

/**
 * @author ezerbo
 *
 */
public class CourseManager {

	private Term loadedTerm;
	
	//private String dataDirectoryPath;
	
	public CourseManager(String termFileName) {
		this.loadedTerm = new Term(termFileName);
		//this.dataDirectoryPath = "";
	}
	
	public void saveTermToFile(String fileName) {
		
	}
	
	public void addCourse(Course course) {
		
	}
	
	public void removeCourse(int courseId) {
		
	}
	
	public void addStudentToCourse(Student student, int courseId) {
		
	}
	
	public void removeStudentFromCourse(int studentId, int courseId) {
		
	}
	
	public double calculateCourseAverageGpa(int courseId) {
		return 0;
	}
	
	public String generateCourseSchedule(int courseId) {
		return "";
	}
	
	public String generateCourseSchedule() {
		return "";
	}
	
	public void saveCourseScheduleToFile(String fileName) {
		
	}
	
	public Course loadCourse(String fileName) {
		return null;
	}
	
	public void changeStudentGpa(int studentId, int courseId, double gpa) {
		
	}
	
	public Term getLoadedTerm() {
		return loadedTerm;
	}
	
	
}
