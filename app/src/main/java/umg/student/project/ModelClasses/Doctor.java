package umg.student.project.ModelClasses;

import org.json.JSONArray;

import java.util.ArrayList;

public class Doctor {
    // Class variables/properties
    public static final String TABLE_NAME_Doctor = "doctor";
    public static final String COLUMN_Doctor_Id = "idDoctor";
    public static final String COLUMN_Doctor_Name = "Lastname";
    public static final String COLUMN_Doctor_Firstname = "Firstname";
    public static final String COLUMN_Doctor_Specialisation = "Specialisation";
    public static final String COLUMN_Doctor_Email = "Email";
    public static final String COLUMN_Doctor_Password = "Password";

    private int id;
    private String specialisation;
    private String medicalCenter;
    private int isActive;
    private String name, firstname;
    private JSONArray roles;

    // SQL Create Table String:
    public static final String CREATE_TABLE_Doctor =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_Doctor + "("
                    + COLUMN_Doctor_Id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_Doctor_Name + " TEXT,"
                    + COLUMN_Doctor_Firstname + " TEXT,"
                    + COLUMN_Doctor_Specialisation + " INTEGER,"
                    + COLUMN_Doctor_Email + " TEXT,"
                    + COLUMN_Doctor_Password + " TEXT,"
                    + "FOREIGN KEY(Specialisation) REFERENCES specialisation(IdSpecialisation) ON DELETE CASCADE "
                    + ")";


    // Class methods
    public Doctor() {
    }

    public Doctor(int id, String name, String firstname, String specialisation, String medicalCenter, JSONArray roles, Integer isActive) {
        this.id = id;
        this.name = name;
        this.firstname = firstname;
        this.specialisation = specialisation;
        this.medicalCenter= medicalCenter;
        this.isActive = isActive;
        this.roles = roles;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameDoctor() {
        return name;
    }

    public void setNameDoctor(String name) {
        this.name = name;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSpecialisation() {
        return specialisation;
    }

    public void setSpecialisation(String specialisation) {
        this.specialisation = specialisation;
    }


    public String getMedicalCenter() {
        return medicalCenter;
    }

    public void setMedicalCenter(String medicalCenter) {
        this.medicalCenter = medicalCenter;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public JSONArray getRoles() {
        return roles;
    }

    public void setRoles(JSONArray roles) {
        this.roles = roles;
    }
}

