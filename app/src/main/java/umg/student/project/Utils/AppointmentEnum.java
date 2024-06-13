package umg.student.project.Utils;

public enum AppointmentEnum {
    ADDED(0),
    TAKEN(1),
    CANCELED(2);

    private final int code;

    AppointmentEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
