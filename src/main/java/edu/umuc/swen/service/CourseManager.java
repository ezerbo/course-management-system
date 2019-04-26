package edu.umuc.swen.service;

import edu.umuc.swen.domain.Course;
import edu.umuc.swen.domain.Student;
import edu.umuc.swen.domain.Term;

/**
 * Manager class that allows external components to interact with CMS (Course Management System)
 * 
 * @author ezerbo
 *
 */
public class CourseManager {

	private Term loadedTerm;
	
	/**
	 * Creates an instance of CourseManager and loads a term
	 * 
	 * @param termFileName File to load term data from
	 */
	public CourseManager(String termFileName) {
		this.loadedTerm = new Term(termFileName);
	}
	
	/**
	 * Saves the loaded term's data to a file
	 * 
	 * @param fileName File to save term data to
	 */
	public void saveTermToFile(String fileName) {
		loadedTerm.saveToFile(fileName);
	}
	
	/**
	 * Adds a course to the loaded term
	 * 
	 * @param course Course to be added
	 */
	public void addCourse(Course course) {
		loadedTerm.addCourse(course);
	}
	
	/**
	 * Removes a course from the loaded term
	 * 
	 * @param courseId Identifier of the course
	 */
	public void removeCourse(int courseId) {
		loadedTerm.removeCourse(courseId);
	}
	
	/**
	 * Adds a student to a course
	 * 
	 * @param student Student to be added
	 * @param courseId Identifier of the course to add the student to
	 */
	public void addStudentToCourse(Student student, int courseId) {
		loadedTerm.addStudentToCourse(student, courseId);
	}
	
	/**
	 * Removes a student from a course
	 * 
	 * @param studentId Identifier of the student
	 * @param courseId Identifier of the course
	 */
	public void removeStudentFromCourse(int studentId, int courseId) {
		loadedTerm.removeStudentFromCourse(studentId, courseId);
	}
	
	/**
	 * Calculates the average GPA of course whose identifier is passed as parameter
	 * 
	 * @param courseId Identifier of the course
	 * @return averageGpa
	 */
	public double calculateCourseAverageGpa(int courseId) {
		return loadedTerm.calculateCourseAverageGpa(courseId);
	}
	
	/**
	 * Generates schedule for the course whose identifier is passed as parameter
	 * 
	 * @param courseId Identifier of the course
	 * @return courseSchedule
	 */
	public String generateCourseSchedule(int courseId) {
		return loadedTerm.generateCourseSchedule(courseId);
	}
	
	/**
	 * Generates schedule for the loaded term's courses
	 * 
	 * @return courseSchedule
	 */
	public String generateCourseSchedule() {
		return loadedTerm.generateCourseSchedule();
	}
	
	/**
	 * Saves the loaded term's course schedules to a file
	 * 
	 * @param fileName File to save course schedules to
	 */
	public void saveCourseScheduleToFile(String fileName) {
		loadedTerm.saveCourseScheduleToFile(fileName);
	}
	
	/**
	 * Loads a course into the current term
	 * 
	 * @param fileName File to load courses from
	 */
	public void loadCourse(String fileName) {
		loadedTerm.loadCourse(fileName);;
	}
	
	/**
	 * Updates a student's GPA
	 * 
	 * @param studentId Identifier of the student
	 * @param courseId Identifier of the course
	 * @param gpa New GPA
	 */
	public void changeStudentGpa(Integer studentId, int courseId, Double gpa) {
		loadedTerm.changeStudentGpa(studentId, courseId, gpa);
	}
	
	/**
	 * Finds a course using its ID
	 * 
	 * @param courseId Course identifier
	 * @return
	 */
	public Course getCourse(int courseId) {
		return loadedTerm.getCourse(courseId);
	}
	
	/**
	 * Loads students from a file into a course
	 * 
	 * @param fileName File to load students from
	 * @param courseId Identifier of course to load students into
	 */
	public void loadStudentsIntoCourse(String fileName, int courseId) {
		loadedTerm.loadStudentsIntoCourse(fileName, courseId);
	}
	
	/**
	 * Returns the term that's currently loaded
	 * 
	 * @return loadedTerm
	 */
	public Term getLoadedTerm() {
		return loadedTerm;
	}
		
}