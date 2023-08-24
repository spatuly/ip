public class Deadline extends Task {
    String dueDate;
    public Deadline(String desc, String dueDate) {
        super(desc);
        this.dueDate = dueDate;
    }

    @Override
    public String toString() {
        return String.format("[D] %s (by: %s)", super.toString(), this.dueDate);
    }
}
