package Common.responses;

public class AverageOfTransferredStudentsResponse extends Response {
    private final double average;

    public AverageOfTransferredStudentsResponse(double average, String error) {
        super("AverageOfTransferredStudents", error);
        this.average = average;
    }

    public double getAverage() {
        return average;
    }

    @Override
    public String toString() {
        if (getError() != null) {
            return "Ошибка: " + getError();
        }
        return String.format("Среднее количество переведенных студентов: %.2f", average);
    }
} 