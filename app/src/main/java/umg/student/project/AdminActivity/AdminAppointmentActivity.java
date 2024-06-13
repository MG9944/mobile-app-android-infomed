package umg.student.project.AdminActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.SimpleTimeZone;

import umg.student.project.ActivityAdapter.Appointment_Adapter;
import umg.student.project.DoctorActivity.EditPatientAppointmentActivity;
import umg.student.project.ModelClasses.Appointment;
import umg.student.project.ModelClasses.Patient;
import umg.student.project.R;
import umg.student.project.Utils.MyDividerItemDecoration;
import umg.student.project.Utils.RecyclerTouchListener;
import umg.student.project.Utils.RequestService;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AdminAppointmentActivity extends AppCompatActivity{
        private Appointment_Adapter aAdapter;
        private List<Appointment> appointmentsArrayList  = new ArrayList<>();
        private List<Patient> patientList  = new ArrayList<>();
        private CoordinatorLayout coordinatorLayout;
        private SwipeRefreshLayout swipeRefreshLayout;
        private RecyclerView recyclerView;
        private TextView noConsultationView;
        private Integer selectedPatient;
        private Spinner patientSpinner;
        final Calendar myCalendar = Calendar.getInstance();
    String hours;
    String dates;

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_appointment);
            initWidgets();
            getAppointment(getApplicationContext());
            createAppointmentOnClick();
            swipeRefreshLayout();
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
        
        private void initWidgets()
        {
            swipeRefreshLayout = findViewById(R.id.appointment_swpiper_layout);
            coordinatorLayout = findViewById(R.id.appointment_coordinator_layout);
            recyclerView = findViewById(R.id.appointment_recycler_view);
            noConsultationView = findViewById(R.id.empty_appointment_view);}

        public void getAppointment(Context context) {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, RequestService.GET_DOCTOR_APPOINTMENT, null, response -> {
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
                        Log.e("AppointmentList", String.valueOf(appointmentsArrayList));
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
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, RequestService.GET_PATIENTS, null, (Response.Listener<JSONArray>) response -> {
            for (int i = 0; i < response.length(); i++) {
                try {
                    Log.e("res", String.valueOf(response));
                    JSONObject patientObject = response.getJSONObject(i);
                    Patient patient = new Patient();
                    patient.setPesel(patientObject.getString("pesel"). toString());
                    patient.setFirstnamePatient(patientObject.getString("firstname").toString());
                    patient.setNamePatient(patientObject.getString("lastname").toString());
                    patient.setAddress(patientObject.getString("address").toString());
                    patient.setPhoneNumber(patientObject.getString("phoneNumber").toString());
                    patientList.add(patient);
                    initPatientSpinner(patientList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
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

            private void createAppointmentOnClick()
            {
                Button createNewAppointment = findViewById(R.id.idappointment_button_add_appointment);
                createNewAppointment.setOnClickListener(view -> showAppointmentEditDialog(false, null, 1));
        }

        private void continueEditAppointmentOnClick(int position){
            Intent continueInsertAppointment = new Intent(AdminAppointmentActivity.this, EditPatientAppointmentActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("id", appointmentsArrayList.get(position).getIdConsultation());
            continueInsertAppointment.putExtras(bundle);
            startActivity(continueInsertAppointment);
        }



        private void showActionDialog(final int position) {
            CharSequence[] colors = new CharSequence[]{"Edit", "Continue", "Cancel"};
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Choose option");
            builder.setItems(colors, (dialog, selected_option) -> {
                if (selected_option == 0) {
                    showAppointmentEditDialog(true, appointmentsArrayList.get(position), position);
                }
                if (selected_option == 1){
                    continueEditAppointmentOnClick(position);
                }
                else {
                    RequestService.cancelAppointment(AdminAppointmentActivity.this,  appointmentsArrayList.get(position).getIdConsultation());
                }
            });
            builder.show();
        }

        @SuppressLint("SetTextI18n")
        public void showAppointmentEditDialog(final boolean shouldUpdate, final Appointment appointment, final int position)
        {
            LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
            View view = layoutInflaterAndroid.inflate(R.layout.dialog_appointment, null);
            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(AdminAppointmentActivity.this);
            alertDialogBuilderUserInput.setView(view);

            final EditText input_date = view.findViewById(R.id.iddialog_consultation_data);
            final EditText input_hour = view.findViewById(R.id.iddialog_consultation_time);
            final EditText input_diagnostic = view.findViewById(R.id.iddialog_consultation_diagnostic);
            patientSpinner  = view.findViewById(R.id.idappointment_spinner_patient);
            getPatientInSpinner(getApplicationContext());
            TextView dialogTitle = view.findViewById(R.id.iddialog_consultation_title);
            dialogTitle.setText(!shouldUpdate ? "Add new appointment" : "Edit appointment");

            DatePickerDialog.OnDateSetListener date = (view1, year, month, day) -> {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                input_date.setText(year + "-" + (month+1) + "-" + day);
                dates = input_date.getText().toString();
            };

            input_date.setOnClickListener(view12 ->
                    new DatePickerDialog(AdminAppointmentActivity.this, date, myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show());

            TimePickerDialog.OnTimeSetListener time = (timePicker, hour, minute) -> {
                myCalendar.set(Calendar.HOUR_OF_DAY, hour);
                myCalendar.set(Calendar.MINUTE, minute);
                input_hour.setText(hour + ":" +minute );
                hours = input_hour.getText().toString();
            };

            input_hour.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new TimePickerDialog(AdminAppointmentActivity.this,time,myCalendar.get(Calendar.HOUR_OF_DAY),myCalendar.get(Calendar.MINUTE), true).show();
                }
            });

            String appointmentDate = dates + " " + hours;

            if (shouldUpdate && appointment != null) {
                input_date.setText(appointmentDate);
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
                    Toast.makeText(AdminAppointmentActivity.this,
                            "Fill appointment fields", Toast.LENGTH_SHORT).show();
                    return;

                }

                if (shouldUpdate && appointment != null) {
                    RequestService.editAppointment(AdminAppointmentActivity.this,
                            appointmentDate,
                            appointmentsArrayList.get(position).getIdConsultation());
                } else {
                    Log.e("date",appointmentDate);
                    RequestService.createAppointment(AdminAppointmentActivity.this,
                            appointmentDate,
                            selectedPatient,
                            input_diagnostic.getText().toString());
                }
            });
        }

        private void initPatientSpinner(List<Patient> patientList) {
            Log.e("patientlist", String.valueOf(patientList));
            ArrayAdapter<Patient> patientAdapter = new ArrayAdapter<>(AdminAppointmentActivity.this,
                    android.R.layout.simple_spinner_dropdown_item, patientList);
            Log.e("adapter", String.valueOf(patientAdapter));
            patientSpinner.setAdapter(patientAdapter);
            Patient patient = (Patient) patientSpinner.getSelectedItem();
            selectedPatient = patient.getId();
        }

        private void toggleEmptyAppointment() {
            if (appointmentsArrayList.size() > 0) {
                noConsultationView.setVisibility(View.GONE);
            } else {
                noConsultationView.setVisibility(View.VISIBLE);
            }
        }

}