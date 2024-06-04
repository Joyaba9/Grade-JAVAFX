package bryan.grade;


public class Validation {

    /**
     * Validates the name input.
     *
     * @param name The name to be validated. It can include alphanumeric characters and spaces.
     * @return true if the name is valid, false otherwise.
     */
    public static boolean validateName(String name) {
        return name.matches("[a-zA-Z0-9 ]+");
    }

    /**
     * Validates the category input.
     *
     * @param category The category to be validated. It should only contain letters without any spaces.
     * @return true if the category is valid, false otherwise.
     */
    public static boolean validateCategory(String category) {
        return category.matches("[a-zA-Z]+");
    }

    /**
     * Validates the score input.
     *
     * @param score The score to be validated. It should be a non-empty string of digits.
     * @return true if the score is valid, false otherwise.
     */
    public static boolean validateScore(String score) {
        return score.matches("\\d+");
    }
}