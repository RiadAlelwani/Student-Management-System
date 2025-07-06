package application;

import persistence.SemesterDAO;
import domain.Semester;
import java.sql.Connection;
import java.util.List;

/**
 * Service class for managing Semester entities.
 * Provides methods to add, update, delete, and retrieve semesters from the database.
 */
public class SemesterService {
    private final SemesterDAO dao;

    /**
     * Constructs a SemesterService with a database connection.
     * 
     * @param conn the database connection to be used by the DAO
     */
    public SemesterService(Connection conn) {
        this.dao = new SemesterDAO(conn);
    }

    /**
     * Adds a new semester to the database.
     * 
     * @param s the Semester object to add
     * @throws Exception if there is an error during the add operation
     */
    public void add(Semester s) throws Exception {
        dao.add(s);
    }

    /**
     * Updates an existing semester's information in the database.
     * 
     * @param s the Semester object with updated information
     * @throws Exception if there is an error during the update operation
     */
    public void update(Semester s) throws Exception {
        dao.update(s);
    }

    /**
     * Deletes a semester from the database by its ID.
     * 
     * @param id the ID of the semester to delete
     * @throws Exception if there is an error during the delete operation
     */
    public void delete(int id) throws Exception {
        dao.delete(id);
    }

    /**
     * Retrieves a list of all semesters from the database.
     * 
     * @return a List of Semester objects
     * @throws Exception if there is an error during the retrieval
     */
    public List<Semester> getAll() throws Exception {
        return dao.getAll();
    }

    /**
     * Retrieves a semester by its ID.
     * 
     * @param id the ID of the semester to retrieve
     * @return the Semester object with the specified ID
     * @throws Exception if there is an error during the retrieval
     */
    public Semester getById(int id) throws Exception {
        return dao.getById(id);
    }
}
