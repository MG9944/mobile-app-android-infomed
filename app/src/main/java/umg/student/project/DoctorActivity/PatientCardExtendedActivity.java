package umg.student.project.DoctorActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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

import umg.student.project.ActivityAdapter.PatientCardSecond_Adapter;
import umg.student.project.ModelClasses.Appointment;
import umg.student.project.R;
import umg.student.project.Utils.MyDividerItemDecoration;
import umg.student.project.Utils.RecyclerTouchListener;
import umg.student.project.Utils.RequestService;

public class PatientCardExtendedActivity extends AppCompatActivity {
    private PatientCardSecond_Adapter aAdapter;
    private List<Appointment> appointmentsArrayList  = new ArrayList<>();
    private CoordinatorLayout coordinatorLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private TextView noConsultationView;
    private Integer patientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_card_extended);
        initWidgets();
        initRecycleView();
        swipeRefreshLayout();
    }

    private void initWidgets()
    {
        swipeRefreshLayout = findViewById(R.id.patient_card_extended_swpiper_layout);
        coordinatorLayout = findViewById(R.id.patient_card_extend_coordinator_layout);
        recyclerView = findViewById(R.id.patient_card_extended_recycler_view);
        noConsultationView = findViewById(R.id.empty_patient_card_extended_view);
        Bundle bundle = getIntent().getExtras();
        patientId = bundle.getInt("id");
        Log.d("Get id", String.valueOf(patientId));
        getPatientAppointmentInPatientCard(getApplicationContext(), patientId);
    }

    private void swipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            new Handler().postDelayed(() -> {
                appointmentsArrayList.clear();
                getPatientAppointmentInPatientCard(getApplicationContext(),patientId);
                swipeRefreshLayout.setRefreshing(false);
            }, 3000);
        });
    }

    private void getPatientAppointmentInPatientCard(Context context, Integer patientId) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        final String GET_PATIENT_CARD = RequestService.URL + "/api/patients/patient-card/"+patientId+"/appointments";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, GET_PATIENT_CARD, null, (Response.Listener<JSONArray>) response -> {
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject appointmentObject = response.getJSONObject(i);
                    Appointment appointment = new Appointment();
                    appointment.setDataconsultation(appointmentObject.getString("date").toString());
                    appointment.setDiagnosis(appointmentObject.getString("diagnosis").toString());
                    appointment.setTemperature((float) appointmentObject.getDouble("temperature"));
                    appointment.setBloodPressure(appointmentObject.getInt("bloodPressure"));
                    appointment.setSugarLevel(appointmentObject.getInt("sugarLevel"));
                    appointment.setDescription(appointmentObject.getString("description").toString());
                    appointmentsArrayList.add(appointment);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            initPatientAppointments();
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



    private void initPatientAppointments() {
        aAdapter = new PatientCardSecond_Adapter(this, appointmentsArrayList);
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

            }
        }));
    }

    private void toggleEmptyAppointment() {
        if (appointmentsArrayList.size() > 0) {
            noConsultationView.setVisibility(View.GONE);
        } else {
            noConsultationView.setVisibility(View.VISIBLE);
        }
    }
}