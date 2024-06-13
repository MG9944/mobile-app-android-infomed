package umg.student.project.DoctorActivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import umg.student.project.ActivityAdapter.Appointment_Adapter;
import umg.student.project.AdminActivity.AdminAppointmentActivity;
import umg.student.project.ModelClasses.Appointment;
import umg.student.project.ModelClasses.Patient;
import umg.student.project.R;
import umg.student.project.Utils.MyDividerItemDecoration;
import umg.student.project.Utils.RecyclerTouchListener;
import umg.student.project.Utils.RequestService;

public class AppointmentActivity extends AppCompatActivity {
        private Appointment_Adapter aAdapter;
        private  List<Appointment> appointmentsArrayList  = new ArrayList<>();
        private CoordinatorLayout coordinatorLayout;
        private SwipeRefreshLayout swipeRefreshLayout;
        private RecyclerView recyclerView;
        private TextView noconsultationView;
        private Spinner patientSpinner;
        private Integer selectedPatient;

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_appointment);
            initWidgets();
            getAppointment(getApplicationContext());
            swipeRefreshLayout();
        }

        private void initWidgets()
        {
            swipeRefreshLayout = findViewById(R.id.appointment_swpiper_layout);
            coordinatorLayout = findViewById(R.id.appointment_coordinator_layout);
            recyclerView = findViewById(R.id.appointment_recycler_view);
            noconsultationView = findViewById(R.id.empty_appointment_view);
        }

    private void swipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            new Handler().postDelayed(() -> {
                appointmentsArrayList.clear();
                getAppointment(getApplicationContext());
                swipeRefreshLayout.setRefreshing(false);
            }, 3000);
        });
    }

    public void getAppointment(Context context) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, RequestService.GET_DOCTOR_APPOINTMENT, null, (Response.Listener<JSONArray>) response -> {
            for (int i = 0; i < response.length(); i++) {
                try {
                    Log.d("response", String.valueOf(response));
                    JSONObject appointmentObject = response.getJSONObject(i);
                    Appointment appointment = new Appointment();
                    appointment.setIdConsultation(appointmentObject.getInt("id"));
                    appointment.setDataconsultation(appointmentObject.getString("date").toString());
                    appointment.setNamePatient(appointmentObject.getString("patientFullName").toString());
                    appointment.setNameDoctor(appointmentObject.getString("doctorFullName"));
                    appointment.setDiagnosis(appointmentObject.getString("diagnosis").toString());
                    appointmentsArrayList.add(appointment);
                    Log.d("Tag", String.valueOf(appointmentsArrayList));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            initAppointment();
            initRecycleView();
        }, error -> {
            Log.d("Tag", "Response error" + error.getMessage());
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + RequestService.USER_TOKEN);
                return headers;
            }
        };
        requestQueue.add(jsonArrayRequest);
    }

    public void getPatientInSpinner(Context context) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        List<Patient> patientList  = new ArrayList<>();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, RequestService.GET_PATIENTS, null, (Response.Listener<JSONArray>) response -> {
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject patientObject = response.getJSONObject(i);
                    Patient patient = new Patient();
                    patient.setPesel(patientObject.getString("pesel"). toString());
                    patient.setFirstnamePatient(patientObject.getString("firstname").toString());
                    patient.setNamePatient(patientObject.getString("lastname").toString());
                    patient.setAddress(patientObject.getString("address").toString());
                    patient.setPhoneNumber(patientObject.getString("phoneNumber").toString());
                    patientList.add(patient);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            initPatientSpinner(patientList);
        }, error -> {
            Log.d("Tag", "Response error" + error.getMessage());
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();

                headers.put("Authorization", "Bearer " + RequestService.USER_TOKEN);
                return headers;
            }
        };
        requestQueue.add(jsonArrayRequest);
    }

    private void initAppointment() {
        aAdapter = new Appointment_Adapter(this, appointmentsArrayList);
    }

        private void initRecycleView()
        {
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
            recyclerView.setAdapter(aAdapter);
            toggleEmptyAppointment();
            recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                    recyclerView, new RecyclerTouchListener.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                }

                @Override
                public void onLongClick(View view, int position) {
                    showActionDialog(position);
                }
            }));
        }

    private void continueEditAppointmentOnClick(int position){
        Intent continueInsertAppointment = new Intent(AppointmentActivity.this, EditPatientAppointmentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("id", appointmentsArrayList.get(position).getIdConsultation());
        continueInsertAppointment.putExtras(bundle);
        startActivity(continueInsertAppointment);
    }

    private void showActionDialog(final int position) {
        CharSequence[] colors = new CharSequence[]{"Edit", "Delete", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose option");
        builder.setItems(colors, (dialog, selected_option) -> {
            if (selected_option == 0) {
                showAppointmentEditDialog(true, appointmentsArrayList.get(position), position);
            }

            if (selected_option == 1){
                Intent continueInsertAppointment = new Intent(AppointmentActivity.this, EditPatientAppointmentActivity.class);
                startActivity(continueInsertAppointment);
            }
            else {
                RequestService.cancelAppointment(AppointmentActivity.this,  appointmentsArrayList.get(position).getIdConsultation());
            }
        });
        builder.show();
    }

    @SuppressLint("SetTextI18n")
    public void showAppointmentEditDialog(final boolean shouldUpdate, final Appointment appointment, final int position)
    {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.dialog_appointment, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(AppointmentActivity.this);
        alertDialogBuilderUserInput.setView(view);

        final EditText input_data = view.findViewById(R.id.iddialog_consultation_data);
        final EditText input_diagnostic = view.findViewById(R.id.iddialog_consultation_diagnostic);
        patientSpinner  = view.findViewById(R.id.idappointment_spinner_patient);
        getPatientInSpinner(getApplicationContext());
        TextView dialogTitle = view.findViewById(R.id.iddialog_consultation_title);
        dialogTitle.setText(!shouldUpdate ? "Add new appointment" : "Edit appointment");

        if (shouldUpdate && appointment != null) {
            input_data.setText(appointment.getDataconsultation());
            input_diagnostic.setText(appointment.getDiagnosis());
        }
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(shouldUpdate ? "Update" : "Save", (dialogInterface, i) -> {
                })
                .setNegativeButton("Cancel",
                        (dialogInterface, i) -> {
                        });
        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            if (TextUtils.isEmpty(input_diagnostic.getText().toString())) {
                Toast.makeText(AppointmentActivity.this,
                        "Fill appointment fields", Toast.LENGTH_SHORT).show();
                return;
            }


            if (shouldUpdate && appointment != null) {
                RequestService.editAppointment(AppointmentActivity.this,
                        input_data.getText().toString(),
                        appointmentsArrayList.get(position).getIdConsultation());
            } else {
                RequestService.createAppointment(AppointmentActivity.this,
                        input_data.getText().toString(),
                        selectedPatient,
                        input_diagnostic.getText().toString());
            }
        });
    }

    private void initPatientSpinner(List<Patient> patientList) {
        ArrayAdapter<Patient> patientAdapter = new ArrayAdapter<>(AppointmentActivity.this,
                android.R.layout.simple_spinner_dropdown_item, patientList);
        patientSpinner.setAdapter(patientAdapter);
        Patient patient = (Patient) patientSpinner.getSelectedItem();
        selectedPatient = patient.getId();
    }

        private void toggleEmptyAppointment() {
            if (appointmentsArrayList.size() > 0) {
                noconsultationView.setVisibility(View.GONE);
            } else {
                noconsultationView.setVisibility(View.VISIBLE);
            }
        }
}