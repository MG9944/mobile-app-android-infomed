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
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import umg.student.project.ActivityAdapter.Patient_Adapter;
import umg.student.project.ModelClasses.Patient;
import umg.student.project.R;
import umg.student.project.Utils.MyDividerItemDecoration;
import umg.student.project.Utils.RecyclerTouchListener;
import umg.student.project.Utils.RequestService;

public class PatientActivity extends AppCompatActivity{
    private Patient_Adapter pAdapter;
    private final List<Patient> patientList = new ArrayList<>();
    private CoordinatorLayout coordinatorLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private TextView noPatientView;
    private String orderByField;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);
        initWidgets();
        getPatient(getApplicationContext());
        initSearchWidget();
        createPatientOnClick();
        swipeRefreshLayout();
    }

    private void initWidgets()
    {
        swipeRefreshLayout = findViewById(R.id.patient_swpiper_layout);
        coordinatorLayout = findViewById(R.id.patient_coordinator_layout);
        recyclerView = findViewById(R.id.patient_recycler_view);
        noPatientView = findViewById(R.id.empty_patient_view);
    }

    private void swipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            new Handler().postDelayed(() -> {
                patientList.clear();
                getPatient(getApplicationContext());
                swipeRefreshLayout.setRefreshing(false);
            }, 3000);
        });
    }

    public void getPatient(Context context) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String URL = "";

        URL = RequestService.GET_PATIENTS;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null, (Response.Listener<JSONArray>) response -> {
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
        pAdapter = new Patient_Adapter(this, patientList);
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
            }

            @Override
            public void onLongClick(View view, int position) {
                showActionsDialog(position);
            }
        }));
    }

    private void initSearchWidget(){
        SearchView searchView = (SearchView) findViewById(R.id.patientListSearchView);

        // below line is to call set on query text listener method.
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

    private void createPatientOnClick()
    {
        Button createNewPatient = findViewById(R.id.idpatient_button_add_patient);
        createNewPatient.setOnClickListener(view -> showPatientEditDialog(false, null, 0));
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
                orderByField = "pesel";
                pAdapter.notifyDataSetChanged();
                return true;
            case R.id.idmenu_patient_name:
                patientList.clear();
                orderByField = "lastname";
                pAdapter.notifyDataSetChanged();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void createPatient(String pesel, String lastName, String firstName, String address, String phoneNumber) {
        if (patientList == null) {
            pAdapter.notifyDataSetChanged();
            toggleEmptyPatient();
        }
        RequestService.createPatient(PatientActivity.this, pesel, lastName, firstName, address, phoneNumber);
    }

    private void updatePatient(String pesel, String name, String firstname, String address, String phoneNumber, Integer patientId) {
        RequestService.editPatient(PatientActivity.this, pesel, name, firstname, address, phoneNumber, patientId);
        pAdapter.notifyItemChanged(patientId);
        toggleEmptyPatient();
    }

    private void showActionsDialog(final int position) {
        CharSequence[] colors = new CharSequence[]{"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose option");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int selected_option) {
                if (selected_option == 0) {
                    showPatientEditDialog(true, patientList.get(position), position);
                }
            }
        });
        builder.show();
    }

    public void showPatientEditDialog(final boolean shouldUpdate, final Patient patient, final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.dialog_patient, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(PatientActivity.this);
        alertDialogBuilderUserInput.setView(view);

        final EditText inputPesel = view.findViewById(R.id.iddialog_patient_pesel);
        final EditText inputName = view.findViewById(R.id.iddialog_patient_lastname);
        final EditText inputFirstname = view.findViewById(R.id.iddialog_patient_firstname);
        final EditText inputAddress = view.findViewById(R.id.iddialog_patient_address);
        final EditText inputPhoneNumber = view.findViewById(R.id.iddialog_patient_phonenumber);

        TextView dialogTitle = view.findViewById(R.id.iddialog_patient_title);
        dialogTitle.setText(!shouldUpdate ? "Add new patient" : "Edit patient");

        if (shouldUpdate && patient != null) {
            inputPesel.setText(patient.getPesel());
            inputName.setText(patient.getNamePatient());
            inputFirstname.setText(patient.getFirstnamePatient());
            inputAddress.setText(patient.getAddress());
            inputPhoneNumber.setText(patient.getPhoneNumber());
        }
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(shouldUpdate ? "Update" : "Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int id) {
                                dialogInterface.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Show toast message when no text is entered
                if (TextUtils.isEmpty(inputPesel.getText().toString()) ||
                        TextUtils.isEmpty(inputName.getText().toString()) ||
                        TextUtils.isEmpty(inputFirstname.getText().toString())) {

                    Toast.makeText(PatientActivity.this,
                            "Fill in the patient's fields!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }

                if (shouldUpdate && patient != null) {
                    updatePatient(inputPesel.getText().toString(),
                            inputName.getText().toString(),
                            inputFirstname.getText().toString(),
                            inputAddress.getText().toString(),
                            inputPhoneNumber.getText().toString(),
                            patientList.get(position).getId());
                } else {
                    createPatient(inputPesel.getText().toString(),
                            inputName.getText().toString(),
                            inputFirstname.getText().toString(),
                            inputAddress.getText().toString(),
                            inputPhoneNumber.getText().toString());
                }
            }
        });
    }

    private void toggleEmptyPatient() {
        if (patientList.size() > 0) {
            noPatientView.setVisibility(View.GONE);
        } else {
            noPatientView.setVisibility(View.VISIBLE);
        }
    }


}

