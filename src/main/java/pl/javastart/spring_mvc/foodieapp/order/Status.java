package pl.javastart.spring_mvc.foodieapp.order;

public enum Status {
    NEW,
    IN_PROGRESS,
    COMPLETE;

    public static Status nextStatus(Status status) {
        if (status == NEW) {
            return IN_PROGRESS;
        } else if (status == IN_PROGRESS) {
            return COMPLETE;
        } else throw new IllegalArgumentException("Nie ma takiego statusu");
    }
}