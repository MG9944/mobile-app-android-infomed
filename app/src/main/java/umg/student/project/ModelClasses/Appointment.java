package umg.student.project.ModelClasses;

public class Appointment {
    public static final String TABLE_NAME_Consultation = "consultation";

    public static final String COLUMN_CONSULTATION_IdConsultation = "IdConsultation";
    public static final String COLUMN_CONSULTATION_IdPatient = "IdPatient";
    public static final String COLUMN_CONSULTATION_DataConsultation = "DataConsultation";
    public static final String COLUMN_CONSULTATION_Diagnosis = "Diagnosis";
    public static final String COLUMN_CONSULTATION_Status = "Status";
    public static final String COLUMN_CONSULTATION_Temperature = "Temperature";
    public static final String COLUMN_CONSULTATION_BloodPressure = "BloodPressure";
    public static final String COLUMN_CONSULTATION_SugarLevel = "SugarLevel";
    public static final String COLUMN_CONSULTATION_Description = "Description";

    private int IdConsultation, IdPatient, status, bloodPressure, sugarLevel;
    private String dataconsultation, diagnosis, description;
    private float temperature;


    private String PatientName, FirstNamePatient, DoctorName, FirstNameDoctor;

    // SQL Create table String:
    public static final String CREATE_TABLE_Consultation =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_Consultation + "("
                    + COLUMN_CONSULTATION_IdConsultation + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_CONSULTATION_IdPatient + " INTEGER,"
                    + COLUMN_CONSULTATION_DataConsultation + " TEXT,"
                    + COLUMN_CONSULTATION_Diagnosis + " TEXT,"
                    + COLUMN_CONSULTATION_Status + " INTEGER,"
                    + COLUMN_CONSULTATION_Temperature + " REAL,"
                    + COLUMN_CONSULTATION_BloodPressure + " INTEGER,"
                    + COLUMN_CONSULTATION_SugarLevel + " INTEGER,"
                    + COLUMN_CONSULTATION_Description + " TEXT,"
                    + "FOREIGN KEY(IdPatient) REFERENCES patient(IdPatient) ON DELETE CASCADE "
                    + ")";

    // Class Methods
    public Appointment() {
    }

    public Appointment(int IdConsultation, int IdPatient, String dataconsultation, String diagnosis) {
        this.IdConsultation = IdConsultation;
        this.IdPatient = IdPatient;
        this.dataconsultation = dataconsultation;
        this.diagnosis = diagnosis;
    }

    public int getIdConsultation() {
        return IdConsultation;
    }

    public void setIdConsultation(int idConsultation) {
        IdConsultation = idConsultation;
    }

    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }

    public int getIdPatient() {
        return IdPatient;
    }

    public void setIdPatient(int idPatient) {
        IdPatient = idPatient;
    }


    public String getDataconsultation() {
        return dataconsultation;
    }

    public void setDataconsultation(String dataconsultation) {
        this.dataconsultation = dataconsultation;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }


    public int getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(int bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public int getSugarLevel() {
        return sugarLevel;
    }

    public void setSugarLevel(int sugarLevel) {
        this.sugarLevel = sugarLevel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }



    // Attributes belonging to other classes (patients, doctors)

    public String getNamePatient() {
        return PatientName;
    }

    public void setNamePatient(String patientName) {
        PatientName = patientName;
    }

    public String getFirstNamePatient() {
        return FirstNamePatient;
    }

    public void setFirstNamePatient(String firstNamePatient) {
        FirstNamePatient = firstNamePatient;
    }

    public String getNameDoctor() {
        return DoctorName;
    }

    public void setNameDoctor(String doctorName) {
        DoctorName = doctorName;
    }

    public String getFirstNameDoctor() {
        return FirstNameDoctor;
    }

    public void setFirstNameDoctor(String firstNameDoctor) {
        FirstNameDoctor = firstNameDoctor;
    }

}
