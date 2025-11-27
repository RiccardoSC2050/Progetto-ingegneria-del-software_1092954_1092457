package it.unibg.progetto.api.operators;

/**
 * Interface defining data file management operations.
 * Provides methods for basic CRUD operations on data files.
 * 
 * @author Employee Management System
 * @version 1.0
 */
interface DataControl {
	
	/**
	 * Reads data from the associated data file.
	 * Loads employee information or operator data from persistent storage.
	 */
	public void readDataFile();
	
	/**
	 * Creates a new data file for storing information.
	 * Initializes the file structure if it doesn't exist.
	 */
	public void createDataFile();
	
	/**
	 * Deletes the existing data file.
	 * Permanently removes the data file from storage.
	 */
	public void deleteDataFile();
}
