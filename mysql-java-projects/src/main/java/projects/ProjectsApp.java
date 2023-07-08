package projects;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import projects.entity.Project;
import projects.exception.DbException;
import projects.service.ProjectService;

public class ProjectsApp {
	/*
	 * Private Class Fields
	 */
	private Scanner scanner = new Scanner(System.in);
	private ProjectService projectService = new ProjectService();
	private Project currProject;
	
	// @formatter:off
	private List<String> operations = List.of(
			"1) Add a project",
			"2) List projects",
			"3) Select a project"
	);
	// @formatter:on
	

	/**
	 * Main Method Entry point for Java application
	 * 
	 * @param args Unused
	 */
	public static void main(String[] args) {
		new ProjectsApp().processUserSelection();

	}

	/**
	 * Method will display the menu selections, gets a selection from the user, and
	 * then acts on the selection.
	 */
	private void processUserSelection() {
		boolean done = false;

		while (!done) {
			try {
				int selection = getUserSelection();

				switch (selection) {
				case -1:
					done = exitMenu();
					break;
					
				case 1:
					createProject();
					break;
					
				case 2:
					listProjects();
					break;
					
				case 3:
					selectProject();
					break;

				default:
					System.out.println("\n" + selection + " is not a valid selection. Try again");
				}

			} catch (Exception e) {
				System.out.println("\nError: " + e + " Try again");
			}
		}
	}
	
	private void selectProject() {
		listProjects();
		
		Integer projectId = getIntInput("Enter a project ID to select a project");
		
		currProject = null;
		
		currProject = projectService.fetchProjectById(projectId);
		
		if(Objects.isNull(currProject)) {
			System.out.println("\nInvalid project ID selected");
		}
	}

	private void listProjects() {
		List<Project> projects = projectService.fetchAllProjects();
		
		System.out.println("\nProjects:");
		
		projects.forEach(project -> System.out.println("   " + project.getProjectId() + ":   " + project.getProjectName()));
	}

	/**
	 * Method will gather the project details from the user
	 * 
	 */
	private void createProject() {
		String projectName =  getStringInput("Enter the project name");
		BigDecimal estimatedHours = getDecimalInput("Enter the estimated hours");
		BigDecimal actualHours = getDecimalInput("Enter the acutal hours");
		Integer difficulty = getIntInput("Enter the project difficluty (1-5)");
		String notes = getStringInput("Enter the project notes");
		
		Project project = new Project();
		
		project.setProjectName(projectName);
		project.setEstimatedHours(estimatedHours);
		project.setActualHours(actualHours);
		project.setDifficulty(difficulty);
		project.setNotes(notes);
		
		Project dbProject = projectService.addProject(project);
		System.out.println("You have successfully created project: " + dbProject);
	}

	/**
	 * Method will accept user input and converts it to a BigDecimal
	 * 
	 * @param prompt The prompt to display on the console
	 * @return a BigDecimal value if successful
	 * @throws DbException is thrown if user input is not a valid decimal number 
	 */
	private BigDecimal getDecimalInput(String prompt) {
		String input = getStringInput(prompt);

		if (Objects.isNull(input)) {
			return null;
		}

		try {
			return new BigDecimal(input).setScale(2);
		} catch (NumberFormatException e) {
			throw new DbException(input + " is not a valid decimal number.");
		}
	}

	/**
	 * Method will end the Menu application
	 * 
	 * @return {@code true}
	 */
	private boolean exitMenu() {
		System.out.println("\nExit application. Goodbye!");
		return true;
	}

	/**
	 * Method will display the menu selections, gets a selection from the user, and
	 * then acts on the selection.
	 * 
	 * @return Returns -1 if input is null, otherwise returns input
	 */
	private int getUserSelection() {
		printOperations();

		Integer input = getIntInput("Enter a menu selection");

		return Objects.isNull(input) ? -1 : input;
	}
	

	/**
	 * Method will accept user input and converts it to an Integer
	 * 
	 * @param prompt The prompt to display on the console
	 * @return an Integer value if successful
	 * @throws DbException is thrown if user input is not a valid integer
	 */
	private Integer getIntInput(String prompt) {
		String input = getStringInput(prompt);

		if (Objects.isNull(input)) {
			return null;
		}

		try {
			return Integer.valueOf(input);
		} catch (NumberFormatException e) {
			throw new DbException(input + " is not a valid number.");
		}
	}
	

	/**
	 * Method will accept user input and returns it back
	 * 
	 * @param prompt The prompt to display on the console
	 * @return the user's input or {@code null}
	 */
	private String getStringInput(String prompt) {
		System.out.print(prompt + ": ");
		String input = scanner.nextLine();

		return input.isBlank() ? null : input.trim();
	}	

	/**
	 * Method will print each available selection on a separate line in the console
	 * 
	 */
	private void printOperations() {
		System.out.println("\nThese are the available selections. Press the Enter key to quit:");

		operations.forEach(line -> System.out.println("  " + line));
		
		if(Objects.isNull(currProject)) {
			System.out.println("\nYou are not working with a project");
		}else {
			System.out.println("\nYou are working with project: " + currProject);
		}
	}

}
