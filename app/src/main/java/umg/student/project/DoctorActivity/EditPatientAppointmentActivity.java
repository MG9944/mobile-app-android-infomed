package umg.student.project.DoctorActivity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import umg.student.project.R;
import umg.student.project.Utils.RequestService;

public class EditPatientAppointmentActivity extends AppCompatActivity {

    EditText patientTemperature;
    EditText patientBloodPressure;
    EditText patientSugarLevel;
    EditText patientDescription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_patient_appointment);
        initWidgets();
        saveAppointmentOnClick();
    }

    public void initWidgets(){
        patientTemperature = findViewById(R.id.iddialog_consultation_insert_temperature);
        patientBloodPressure = findViewById(R.id.iddialog_consultation_insert_bloodpressure);
        patientSugarLevel = findViewById(R.id.iddialog_consultation_sugarlevel_insert);
        patientDescription = findViewById(R.id.iddialog_consultation_insert_description);
    }

    private void insertAppointmentData(Float temperature, Integer bloodPressure, Integer sugarLevel, String description, int appointmentId)
    {
        RequestService.continueInsertDataAppointment(EditPatientAppointmentActivity.this,
                temperature,
                bloodPressure,
                sugarLevel,
                description,
                appointmentId);
    }

    private void saveAppointmentOnClick()
    {
        Button saveAppointment = findViewById(R.id.appointment_button_save);
        Bundle bundle = getIntent().getExtras();
        int position = bundle.getInt("id");
        saveAppointment.setOnClickListener(view -> {
            Log.d("Get id", String.valueOf(position));
            insertAppointmentData(Float.valueOf(patientTemperature.getText().toString()),
                    Integer.valueOf(patientBloodPressure.getText().toString()),
                    Integer.valueOf(patientSugarLevel.getText().toString()),
                    patientDescription.getText().toString(),
                    position);
            showActionDialog(position);
    });
    }

    private void showActionDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Would you like to generate patient prescription?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> {
                    RequestService.generatePatientPrescription(getApplicationContext(),position);
                    Intent goToAppointment = new Intent(this, AppointmentActivity.class);
                    startActivity(goToAppointment);
                    finish();
                })
                .setNegativeButton("No", (dialog, id) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();
    }


}