package umg.student.project.ModelClasses;

public class Admins {
    public static final String TABLE_NAME_Admins = "Admins";
    public static final String COLUMN_ADMIN_IdAdmin = "IdAdmin";
    public static final String COLUMN_ADMIN_Email = "Username";
    public static final String COLUMN_ADMIN_Password = "Password";

    public static final String CREATE_TABLE_Admins =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_Admins + "("
                    + COLUMN_ADMIN_IdAdmin + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_ADMIN_Email + " TEXT, "
                    + COLUMN_ADMIN_Password + " TEXT"
                    + ")";
}
