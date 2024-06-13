package umg.student.project.ModelClasses;

public class Illness {
    // Class variables/properties
    public static final String TABLE_NAME_Illness = "illness";

    public static final String COLUMN_Illness_Id = "IdIllness";
    public static final String COLUMN_Illness_Name = "Name";
    public static final String COLUMN_Illness_Category = "Category";
    public static final String COLUMN_Illness_IdMedicamente = "Medicamente";

    private int id;
    private String name, category, medicamente;

    // SQL Create Table String:
    public static final String CREATE_TABLE_Illness =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_Illness + "("
                    + COLUMN_Illness_Id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_Illness_Name + " TEXT,"
                    + COLUMN_Illness_Category + " TEXT,"
                    + COLUMN_Illness_IdMedicamente + " INTEGER,"
                    + "FOREIGN KEY(Medicamente) REFERENCES medicamente(IdMedicament) ON DELETE CASCADE "
                    + ")";
    // Class methods
    public Illness() {
    }

    public Illness(int id, String name, String category, String medicamente) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.medicamente = medicamente;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIllnessName() {
        return name;
    }

    public void setIllnessName(String name) {
        this.name = name;
    }

    public String getCategory() {return category;}

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMedicamente() {
        return medicamente;
    }

    public void setMedicamente(String medicamente) {
        this.medicamente = medicamente;
    }
}
