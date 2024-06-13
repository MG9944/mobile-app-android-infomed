package umg.student.project.DoctorActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import umg.student.project.LoginActivity;
import umg.student.project.ModelClasses.MedicalCenter;
import umg.student.project.ModelClasses.Medicamente;
import umg.student.project.ModelClasses.Specialisation;
import umg.student.project.R;
import umg.student.project.Utils.RequestService;

public class DoctorPersonalDataActivity extends AppCompatActivity{
    private final List<Specialisation> specialisationsName = new ArrayList<>();
    private final List<MedicalCenter> medicalCenterName = new ArrayList<>();
    private Integer selectedSpecialisation;
    private Integer selectedMedicalCenter;
    EditText register_firstName;
    EditText register_lastName;
    Spinner register_specialisation;
    Spinner register_medicalCenter;
    EditText register_phoneNumber;
    Button register_button_register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_personal_data);
        initWidgets();
        getSpecialisations(getApplicationContext());
        getMedicalCenter(getApplicationContext());
        registerOnClick();

    }

    private void initWidgets() {
        register_firstName = findViewById(R.id.idregister_edittext_firstname);
        register_lastName = findViewById(R.id.idregister_edittext_lastname);
        register_specialisation = findViewById(R.id.idregister_spinner_specialisation);
        register_medicalCenter = findViewById(R.id.idregister_spinner_medicalCenter);
        register_phoneNumber = findViewById(R.id.idregister_edittext_phoneNumber);
        register_button_register = findViewById(R.id.idregister_button_register);
    }

    private void registerOnClick() {
        register_button_register.setOnClickListener(view -> {
            try {
                checkCredentials();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }



    private void checkCredentials() throws Exception {
        String regEx = "^[a-zA-Z]";
        String firstName = register_firstName.getText().toString().trim();
        String lastName = register_lastName.getText().toString().trim();
        String phoneNumber = register_phoneNumber.getText().toString().trim();
        Bundle bundle = getIntent().getExtras();
        String email = bundle.getString("email");
        String password = bundle.getString("password");

        if(Pattern.compile(regEx).matcher(firstName).matches() || firstName.isEmpty()){
            Toast.makeText(DoctorPersonalDataActivity.this, "Your firstname cannot contain a number!", Toast.LENGTH_SHORT).show();
        }

        if (Pattern.compile(regEx).matcher(lastName).matches() || lastName.isEmpty()){
            Toast.makeText(DoctorPersonalDataActivity.this, "Your lastname cannot contain a number!", Toast.LENGTH_SHORT).show();
        }
        RequestService.registerActivity(DoctorPersonalDataActivity.this, firstName, lastName, email, password, selectedMedicalCenter, selectedSpecialisation, phoneNumber);
        Intent moveToLogin = new Intent(DoctorPersonalDataActivity.this, LoginActivity.class);
        startActivity(moveToLogin);
        finish();
    }

    private void initSpecialisationSpinner(List<Specialisation> specialisations) {
        ArrayAdapter<Specialisation> specialisationAdapter = new ArrayAdapter<>(DoctorPersonalDataActivity.this,
                android.R.layout.simple_spinner_dropdown_item, specialisations);
        register_specialisation.setAdapter(specialisationAdapter);
        Specialisation specialisation = (Specialisation) register_specialisation.getSelectedItem();
        selectedSpecialisation = specialisation.getIdSpecialisation();
    }

    private void initMedicalCenterSpinner(List<MedicalCenter> medicalCenters) {
        ArrayAdapter<MedicalCenter> medicalCenterAdapter = new ArrayAdapter<>(DoctorPersonalDataActivity.this,
                android.R.layout.simple_spinner_item, medicalCenters);
        medicalCenterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        register_medicalCenter.setAdapter(medicalCenterAdapter);
        MedicalCenter medicalCenter = (MedicalCenter) register_medicalCenter.getSelectedItem();
        selectedMedicalCenter = medicalCenter.getIdMedicalCenter();
    }

    public void getSpecialisations(Context context) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, RequestService.GET_SPECIALISATIONS, null, (Response.Listener<JSONArray>) response -> {
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject specialisationObject = response.getJSONObject(i);
                    Specialisation specialisation = new Specialisation();
                    specialisation.setIdSpecialisation(specialisationObject.getInt("id"));
                    specialisation.setSpecialisationName(specialisationObject.getString("name").toString());
                    specialisationsName.add(specialisation);
                    initSpecialisationSpinner(specialisationsName);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, error -> {
            Log.d("Tag", "Response error" + error.getMessage());
        });
        requestQueue.add(jsonArrayRequest);
    }

    public void getMedicalCenter(Context context) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, RequestService.GET_MEDICAL_CENTER, null, (Response.Listener<JSONArray>) response -> {
            for (int i = 0; i < response.length(); i++) {
                try {
                    Log.d("tag", String.valueOf(response));
                    JSONObject medicalCenterObject = response.getJSONObject(i);
                    MedicalCenter medicalCenter = new MedicalCenter();
                    medicalCenter.setIdMedicalCenter(medicalCenterObject.getInt("id"));
                    medicalCenter.setName(medicalCenterObject.getString("name").toString());
                    medicalCenter.setAddress(medicalCenterObject.getString("address").toString());
                    medicalCenter.setNip(medicalCenterObject.getString("nip").toString());
                    medicalCenterName.add(medicalCenter);
                    initMedicalCenterSpinner(medicalCenterName);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, error -> {
            Log.d("Tag", "Response error" + error.getMessage());
        });
        requestQueue.add(jsonArrayRequest);
    }


}