package uke.exception;

/**
 * Represents a Uke exception when an invalid command is entered.
 */
public class UkeInvalidCommandException extends UkeException {

    private String command;

    /**
     * Constructor for the exception.
     *
     * @param command Invalid command entered.
     */
    public UkeInvalidCommandException(String command) {
        this.command = command;
    }

    /**
     * Returns the string representation of the exception.
     */
    @Override
    public String toString() {
        return String.format("Error: %s is an invalid command!", this.command);
    }

}
