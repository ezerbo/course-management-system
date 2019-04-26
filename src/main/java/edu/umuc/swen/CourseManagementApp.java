package edu.umuc.swen;

import java.util.Date;
import java.util.Scanner;

import edu.umuc.swen.domain.Address;
import edu.umuc.swen.domain.Course;
import edu.umuc.swen.domain.HybridCourse;
import edu.umuc.swen.domain.OnlineCourse;
import edu.umuc.swen.domain.Student;
import edu.umuc.swen.domain.Term;
import edu.umuc.swen.error.CourseOutOfRangeException;
import edu.umuc.swen.error.InvalidStudentOperationException;
import edu.umuc.swen.service.CourseManager;

/**
 * Main class used to test the Course Management System
 * 
 * @author ezerbo
 *
 */
public class CourseManagementApp {
	
	private final static String PATH_TO_FILES = "./term-data/"; // Where files are created and loaded from. 

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int choice = 0;
		do {
			System.out.print("Enter scenario number (1 through 7) or 0 to quit : ");
			choice = scanner.nextInt();
			switch (choice) {
			case 0:
				System.out.println("Goodbye!");
				System.exit(0);
				break;
			case 1:
				scenario1();
				break;
			case 2: 
				scenario2();
				break;
			case 3:
				scenario3();
				break;
			case 4:
				scenario4();
				break;
			case 5:
				scenario5();
				break;
			case 6:
				scenario6();
				break;
			case 7:
				scenario7();
				break;
			default:
				System.out.println("Please select a scenario between 1 and 7");
				break;
			}
		} while(choice != 0);
		scanner.close();
	}

	/**
	 * Create an instance of manager/organizer class and try loading a specific term data
	 * (there is no course data in the file).
	 */
	public static void scenario1() {
		try {
			new CourseManager(PATH_TO_FILES.concat("term-with-no-course.txt"));
		} catch (CourseOutOfRangeException e) {
			System.out.println(String.format("An error has occurred: %s", e.getMessage()));
		}
	}

	/**
	 * 1. Create an instance of manager/organizer class and load a specific term data
	 *    (there should be two courses with 2 students each that is loaded).
	 * 
	 * 2. Print the data that was loaded to the console.
	 */
	public static void scenario2() {
		CourseManager courseManager = new CourseManager(PATH_TO_FILES.concat("term-with-2-courses-and-2-students.txt"));
		Term term = courseManager.getLoadedTerm();
		term.getCourses().stream()
			.forEach(course -> {
				System.out.println("ID : " + course.getId());
				System.out.println("Name : " + course.getName());
				System.out.println("Term Code : " + course.getTermCode());
				System.out.println("Start Date : " + course.getStartDate());
				System.out.println("End Date : " + course.getEndDate());
				System.out.println("Meeting Days : " + course.getMeetingDays());
				System.out.println("Meeting Times : " + course.getMeetingTimes());
				if(course instanceof HybridCourse) {
					HybridCourse hybridCourse = (HybridCourse)course;
					System.out.println("URL : " + hybridCourse.getUrl());
					System.out.println("Location : " + hybridCourse.getClassroomLocation().format());
				} else {
					OnlineCourse onlineCourse = (OnlineCourse)course;
					System.out.println("URL : " + onlineCourse.getUrl());
				}
				System.out.println("Students: ");
				course.getStudents().stream()
					.forEach(student -> {
						System.out.println("\tID : " + student.getId());
						System.out.println("\tFirst Name : " + student.getFirstName());
						System.out.println("\tLast Name : " + student.getLastName());
						System.out.println("\tEmail Address : " + student.getEmailAddress());
						System.out.println("\tOverall GPA : " + student.getOverallGpa());
						System.out.println("\tMailing Address : " + student.getMailingAddress().format());
						System.out.println("\t.....................................");
					});
				System.out.println("-----------------------------------------------------");
			});
	}

	/**
	 * 1. Create an instance of manager/organizer class and load a specific term data.
	 * 
	 * 2. Add new course.
	 * 
	 * 3. Load students into the new course from a file.
	 * 
	 * 4. Print all data to console for the new course added.
	 * 
	 */
	public static void scenario3() {
		CourseManager courseManager = new CourseManager(PATH_TO_FILES.concat("term-with-1-course.txt")); //Term has one course and 2 students
		//Course start 10 days from today ends 100 days from today
		Course course = new OnlineCourse(2, "https://swen-646.umuc.edu", "SWEN646", new Date(System.currentTimeMillis() + 864000000L),
				new Date(System.currentTimeMillis() + 8640000000L), "TH F", "FL2019", "10:00PM - 12:00AM");
		courseManager.addCourse(course);
		courseManager.loadStudentsIntoCourse(PATH_TO_FILES.concat("students.txt"), 2);
		OnlineCourse newCourse = (OnlineCourse)courseManager.getCourse(2);
		System.out.println("ID : " + newCourse.getId());
		System.out.println("Name : " + newCourse.getName());
		System.out.println("Term Code : " + newCourse.getTermCode());
		System.out.println("Start Date : " + newCourse.getStartDate());
		System.out.println("End Date : " + newCourse.getEndDate());
		System.out.println("Meeting Days : " + newCourse.getMeetingDays());
		System.out.println("Meeting Times : " + newCourse.getMeetingTimes());
		System.out.println("URL: " + newCourse.getUrl());
		newCourse.getStudents().stream()
			.forEach(student -> {
				System.out.println("\tID : " + student.getId());
				System.out.println("\tFirst Name : " + student.getFirstName());
				System.out.println("\tLast Name : " + student.getLastName());
				System.out.println("\tEmail Address : " + student.getEmailAddress());
				System.out.println("\tOverall GPA : " + student.getOverallGpa());
				System.out.println("\tMailing Address : " + student.getMailingAddress().format());
				System.out.println("\t.....................................");
			});
	}

	/**
	 * Try loading more than 6 courses.
	 */
	public static void scenario4() {
		CourseManager courseManager = new CourseManager(PATH_TO_FILES.concat("term-with-1-course.txt")); // Term already has one course and 2 students
		// Add 5 courses
		for(int i = 2; i <= 6; i++) {
			Course course = new OnlineCourse(i, "https://swen-646.umuc.edu", "SWEN646", new Date(System.currentTimeMillis() + 864000000L),
					new Date(System.currentTimeMillis() + 8640000000L), "TH F", "FL2019", "10:00PM - 12:00AM");
			courseManager.addCourse(course);
		}
		
		//Now, we try adding the 7th course
		Course course = new OnlineCourse(7, "https://swen-646.umuc.edu", "SWEN646", new Date(System.currentTimeMillis() + 864000000L),
				new Date(System.currentTimeMillis() + 8640000000L), "TH F", "FL2019", "10:00PM - 12:00AM");
		try {
			courseManager.addCourse(course);
		} catch (CourseOutOfRangeException e) {
			System.out.println(String.format("An error occurred : %s", e.getMessage()));
		}
	}

	/**
	 * 1. Create an instance of manager/organizer class and load a specific term data
	 * 
	 * 2. Try adding/loading more than allowed number of students into a class
	 */
	public static void scenario5() {
		CourseManager courseManager = new CourseManager(PATH_TO_FILES.concat("term-with-1-course.txt")); // Term already has one course and 2 students
		//COURSE ID IS 1
		//Add 18 students
		Address mailingAddress = new Address("221-B", "One Piece Street", "North Blue", "Grand Line", "22000");
		for(int i = 3; i <= 20; i++) {
			Student student = new Student(i, "Luffy", "Muguiwara", 4.0, "luffy.muguiwara@onepiece.com", mailingAddress);
			courseManager.addStudentToCourse(student, 1);
		}
		
		//Now we try adding the 21st student
		Student student = new Student(21, "Luffy", "Muguiwara", 4.0, "luffy.muguiwara@onepiece.com", mailingAddress);
		try {
			courseManager.addStudentToCourse(student, 1);
		} catch (InvalidStudentOperationException e) {
			System.out.println(String.format("An error occurred: %s", e.getMessage()));
		}
	}

	/**
	 * 1. Create an instance of manager/organizer class and load a specific term data 
	 *    (there should be one of each type of course with 2 students each that is loaded).
	 * 
	 * 2. Generate and print to console term schedule.
	 */
	public static void scenario6() {
		CourseManager courseManager = new CourseManager(PATH_TO_FILES.concat("term-with-3-courses-and-2-students.txt"));
		System.out.println(courseManager.generateCourseSchedule());
	}

	/**
	 * 1. Create an instance of manager/organizer class and load a specific term data (at least 2 courses)
	 * 
	 * 2. Remove a course
	 * 
	 * 3. Save term data to file
	 * 
	 * 4. Add student to course
	 * 
	 * 5. Remove different student from course
	 * 
	 * 6. Modify student’s GPA for the course
	 * 
	 * 7. Save term data to a different file
	 */
	public static void scenario7() {
		//Two courses in the file being used. IDs: 1,2
		CourseManager courseManager = new CourseManager(PATH_TO_FILES.concat("term-with-2-courses-scenario7.txt"));
		courseManager.removeCourse(2);
		courseManager.saveTermToFile(PATH_TO_FILES.concat("scenario7-file-1.txt"));
		
		Address mailingAddress = new Address("221-B", "One Piece Street", "North Blue", "Grand Line", "22000");
		Student student = new Student(3, "Luffy", "Muguiwara", 3.0, "luffy.muguiwara@onepiece.com", mailingAddress);
		courseManager.addStudentToCourse(student, 1);
		
		courseManager.removeStudentFromCourse(2, 1);
		
		courseManager.changeStudentGpa(3, 1, 4.0); //The new student has id=3 and GPA=3.0
		
		courseManager.saveTermToFile(PATH_TO_FILES.concat("scenario7-file-2.txt"));
	}
}
