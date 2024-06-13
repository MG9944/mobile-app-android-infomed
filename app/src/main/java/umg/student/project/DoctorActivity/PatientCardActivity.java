package umg.student.project.DoctorActivity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
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

import umg.student.project.ActivityAdapter.PatientCardFirst_Adapter;
import umg.student.project.ModelClasses.Patient;
import umg.student.project.R;
import umg.student.project.Utils.MyDividerItemDecoration;
import umg.student.project.Utils.RecyclerTouchListener;
import umg.student.project.Utils.RequestService;

public class PatientCardActivity extends AppCompatActivity {
    private PatientCardFirst_Adapter pAdapter;
    private final List<Patient> patientList = new ArrayList<>();
    private CoordinatorLayout coordinatorLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private TextView noPatientView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_card);
        initWidgets();
        getPatientInPatientCard(getApplicationContext());
        initSearchWidget();
        swipeRefreshLayout();
    }

    private void initWidgets()
    {
        swipeRefreshLayout = findViewById(R.id.patient_card_swpiper_layout);
        coordinatorLayout = findViewById(R.id.patient_card_coordinator_layout);
        recyclerView = findViewById(R.id.patient_card_recycler_view);
        noPatientView = findViewById(R.id.empty_patient_card_view);
    }

    private void swipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            new Handler().postDelayed(() -> {
                patientList.clear();
                getPatientInPatientCard(getApplicationContext());
                swipeRefreshLayout.setRefreshing(false);
            }, 3000);
        });
    }

    private void getPatientInPatientCard(Context context) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, RequestService.GET_PATIENTS_IN_PATIENT_CARD, null, (Response.Listener<JSONArray>) response -> {
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject patientObject = response.getJSONObject(i);
                    Patient patient = new Patient();
                    patient.setId(patientObject.getInt("id"));
                    patient.setPesel(patientObject.getString("pesel"). toString());
                    patient.setFirstnamePatient(patientObject.getString("firstname").toString());
                    patient.setNamePatient(patientObject.getString("lastname").toString());
                    patientList.add(patient);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            initPatient();
            initRecyclerView();
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

    private void initPatient()
    {
        pAdapter = new PatientCardFirst_Adapter(this, patientList);
    }

    private void initRecyclerView()
    {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(pAdapter);
        toggleEmptyPatient();
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                Intent goToPatientCardInfo = new Intent(PatientCardActivity.this, PatientCardExtendedActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("id", patientList.get(position).getId());
                goToPatientCardInfo.putExtras(bundle);
                startActivity(goToPatientCardInfo);
            }

            @Override
            public void onLongClick(View view, int position) {
                showActionsDialog(position);
            }
        }));
    }

    private void showActionsDialog(final int position) {
        CharSequence[] colors = new CharSequence[]{"Generate patient card"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose option");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int selected_option) {
                if (selected_option == 0) {
                    RequestService.generatePatientCard(getApplicationContext(),patientList.get(position).getId());
                }
            }
        });
        builder.show();
    }

    private void initSearchWidget(){
        SearchView searchView = (SearchView) findViewById(R.id.patientCardListSearchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }

        });
    }

    private void filter(String text) {
        ArrayList<Patient> filteredlist = new ArrayList<Patient>();
        for (Patient item : patientList) {
            if (item.getPesel().toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item);
            }
            if (item.getNamePatient().toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            pAdapter.filterList(filteredlist);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.patient_menu, menu);
        return true;
    }
    @SuppressLint({"NonConstantResourceId", "NotifyDataSetChanged"})
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.idmenu_patient_pesel:
                patientList.clear();
                pAdapter.notifyDataSetChanged();
                return true;
            case R.id.idmenu_patient_name:
                patientList.clear();
                pAdapter.notifyDataSetChanged();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toggleEmptyPatient() {
        if (patientList.size() > 0) {
            noPatientView.setVisibility(View.GONE);
        } else {
            noPatientView.setVisibility(View.VISIBLE);
        }
    }


}