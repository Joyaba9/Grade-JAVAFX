package bryan.grade;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HelloController {

    @FXML
    private TextField nameField;
    @FXML
    private TextField categoryField;
    @FXML
    private TextField scoreField;
    @FXML
    private Label welcomeText;
    @FXML
    private ListView<String> listViewShows;

    /**
     * Adds a new grade to the database based on user input.
     * Validates the input before insertion. If any field is invalid, shows an alert.
     */
    @FXML
    protected void addGrade() {
        String name = nameField.getText();
        String category = categoryField.getText();
        String score = scoreField.getText();

        if (!Validation.validateName(name) || !Validation.validateCategory(category) || !Validation.validateScore(score)) {
            showAlert("Invalid Input", "Please ensure all fields are correctly filled.\n- Name: Letters, digits, spaces\n- Category: Letters only\n- Score: Digits only");
            return;
        }
        // Database operation starts here
        String dbFilePath = ".//Grade.accdb";
        String databaseURL = "jdbc:ucanaccess://" + dbFilePath;

        String insertSql = "INSERT INTO Grades (Name, Category, Score) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(databaseURL);
             PreparedStatement pstmt = conn.prepareStatement(insertSql)) {

            pstmt.setString(1, name);
            pstmt.setString(2, category);
            pstmt.setInt(3, Integer.parseInt(score));


            pstmt.executeUpdate();
            System.out.println("New grade data saved to database successfully.");

            // Clear the fields after successful insertion
            nameField.clear();
            categoryField.clear();
            scoreField.clear();

        } catch (SQLException e) {
            showAlert("Error", "Failed to add grade to database.\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Shows an alert dialog with a specified title and content.
     *
     * @param title   The title of the alert dialog.
     * @param content The content (message) of the alert dialog.
     */
    @FXML
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Displays grades from the database in the ListView.
     * Clears existing items before fetching new data.
     */

    @FXML
    private void onDisplayfromDBclick() {
        // Clear existing items in the ListView
        listViewShows.getItems().clear();

        // Database connection details
        String dbFilePath = ".//Grade.accdb";
        String databaseURL = "jdbc:ucanaccess://" + dbFilePath;

        // SQL query to select all grades
        String query = "SELECT Name, Category, Score FROM Grades";

        try (Connection conn = DriverManager.getConnection(databaseURL);
             Statement stmt = conn.createStatement();

             ResultSet rs = stmt.executeQuery(query)) {

            // Process the result set and add items to the ListView
            while (rs.next()) {
                String name = rs.getString("Name");
                String category = rs.getString("Category");
                int score = rs.getInt("Score");

                // Format how you want each item to be displayed in the ListView
                String listItem = String.format("Name: %s, Category: %s, Score: %d", name, category, score);
                listViewShows.getItems().add(listItem);
            }

            System.out.println("Data displayed in ListView.");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to fetch data from database.");
        }
    }

    /**
     * Exits the application.
     */
    @FXML
    protected void onExitclick() {
        Platform.exit();
    }

    /**
     * Loads grades from a JSON file and inserts them into the database.
     * Clears existing grades before insertion.
     */
    @FXML
    protected void onLoadDBfromJSONclick() {
        Connection conn = null;
        try {
            // Create a Gson instance
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();

            // Ensure the path to Grade.json is correct
            FileReader fr = new FileReader("src/Grade.json");
            Grade[] grades = gson.fromJson(fr, Grade[].class);

            // Prepare database connection details
            String dbFilePath = ".//Grade.accdb"; // Updated to reflect the correct database file
            String databaseURL = "jdbc:ucanaccess://" + dbFilePath;

            // Initialize database connection
            conn = DriverManager.getConnection(databaseURL);

            // Clear existing data from the Grades table
            String deleteSql = "DELETE FROM Grades";
            try (Statement deleteStmt = conn.createStatement()) {
                deleteStmt.execute(deleteSql);
                System.out.println("Existing grade data cleared successfully.");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            // SQL statement to insert new grade data
            String insertSql = "INSERT INTO Grades (Name, Category, Score) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
                for (Grade grade : grades) { // Iterate over each grade object
                    pstmt.setString(1, grade.getName());
                    pstmt.setString(2, grade.getCategory());
                    pstmt.setInt(3, grade.getGrade());

                    // Execute the insert statement
                    pstmt.executeUpdate();
                    System.out.println("New grade data saved to database successfully.");
                }
            } catch (SQLException e) {
                System.err.println("Database operation failed.");
                e.printStackTrace();
            } finally {
                try {
                    if (conn != null) conn.close();
                } catch (SQLException ex) {
                    System.err.println("Failed to close the database connection.");
                    ex.printStackTrace();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Initializes the controller.
     * This method is called and cretaes Access DATABASE
     */

    public void initialize() {
        System.out.println("initialize called");

        // Creates database file
        String dbFilePath = ".//Grade.accdb";
        File dbFile = new File(dbFilePath);

        // Check if the database file does not exist and create one
        if (!dbFile.exists()) {
            try {
                DatabaseBuilder.create(Database.FileFormat.V2010, dbFile);
                System.out.println("The database file has been created.");
            } catch (IOException ioe) {
                ioe.printStackTrace(System.err);
            }
        }

        // Initialize database connection
        String databaseURL = "jdbc:ucanaccess://" + dbFilePath;
        try (Connection conn = DriverManager.getConnection(databaseURL)) {
            // Create a table
            String sql = "CREATE TABLE Grades (Name nvarchar(255), Category nvarchar(255), Score int)";
            try (Statement createTableStatement = conn.createStatement()) {
                createTableStatement.execute(sql);
                conn.commit();
                System.out.println("Table Grades created successfully.");
            } catch (SQLException sqlException) {
                Logger.getLogger(HelloController.class.getName()).log(Level.SEVERE, null, sqlException);
            }
        } catch (SQLException ex) {
            Logger.getLogger(HelloController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}