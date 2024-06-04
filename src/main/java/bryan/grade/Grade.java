package bryan.grade;

import com.google.gson.annotations.SerializedName;

/**
 * Represents a grade with a name, category, and score.
 * This class is used for handling grade objects, including serialization and deserialization with Gson.
 */
public class Grade {

    @SerializedName("name")
    private String Name;

    @SerializedName("category")
    private String Category;

    @SerializedName("score")
    private int Grade;

    /**
     * Constructs a new Grade with specified name, category, and score.
     *
     * @param name     The name of the grade.
     * @param category The category of the grade.
     * @param grade    The score of the grade.
     */
    public Grade(String name, String category, int grade) {
        Name = name;
        Category = category;
        Grade = grade;
    }

    /**
     * Returns the name of the grade.
     *
     * @return The name of the grade.
     */
    public String getName() {
        return Name;
    }

    /**
     * Sets the name of the grade.
     *
     * @param name The new name of the grade.
     */
    public void setName(String name) {
        Name = name;
    }

    /**
     * Returns the category of the grade.
     *
     * @return The category of the grade.
     */
    public String getCategory() {
        return Category;
    }

    /**
     * Sets the category of the grade.
     *
     * @param category The new category of the grade.
     */
    public void setCategory(String category) {
        Category = category;
    }

    /**
     * Returns the score of the grade.
     *
     * @return The score of the grade.
     */
    public int getGrade() {
        return Grade;
    }

    /**
     * Sets the score of the grade.
     *
     * @param grade The new score of the grade.
     */
    public void setGrade(int grade) {
        Grade = grade;
    }
}