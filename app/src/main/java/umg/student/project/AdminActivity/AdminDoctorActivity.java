package umg.student.project.AdminActivity;

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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
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

import umg.student.project.ActivityAdapter.Doctor_Adapter;
import umg.student.project.ModelClasses.Doctor;
import umg.student.project.R;
import umg.student.project.Utils.MyDividerItemDecoration;
import umg.student.project.Utils.RecyclerTouchListener;
import umg.student.project.Utils.RequestService;

public class AdminDoctorActivity extends AppCompatActivity {
    private Doctor_Adapter dAdapter;
    private final List<Doctor> doctorList = new ArrayList<>();
    private CoordinatorLayout coordinatorLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private TextView noDoctorView;


    /* ~~~ onCreate ~~~ */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_doctor);
        initWidgets();
        getDoctors(getApplicationContext());
        createDoctorOnClick();
        swipeRefreshLayout();
    }


    private void initWidgets()
    {
        swipeRefreshLayout = findViewById(R.id.doctor_swpiper_layout);
        coordinatorLayout = findViewById(R.id.doctor_admin_coordinator_layout);
        recyclerView = findViewById(R.id.doctor_recycler_view);
        noDoctorView = findViewById(R.id.empty_doctor_view);
    }

    private void swipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            new Handler().postDelayed(() -> {
                doctorList.clear();
                getDoctors(getApplicationContext());
                swipeRefreshLayout.setRefreshing(false);
            }, 3000);
        });
    }

    public void getDoctors(Context context) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, RequestService.GET_DOCTORS, null, (Response.Listener<JSONArray>) response -> {
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject doctorsObject = response.getJSONObject(i);
                    Doctor doctor = new Doctor();
                    doctor.setId(doctorsObject.getInt("id"));
                    doctor.setNameDoctor(doctorsObject.getString("lastname"));
                    doctor.setFirstname(doctorsObject.getString("firstname"));
                    doctor.setSpecialisation(doctorsObject.getString("specialisation"));
                    doctor.setMedicalCenter(doctorsObject.getString("medicalCenter"));
                    doctor.setIsActive(doctorsObject.getInt("isActive"));
                    doctor.setRoles(doctorsObject.getJSONArray("roles"));

                    doctorList.add(doctor);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            initDoctor();
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

    private void initDoctor()
    {
        dAdapter = new Doctor_Adapter(this, doctorList);
    }

    private void initRecyclerView()
    {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(dAdapter);
        toggleEmptyDoctor();
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
    }


    private void createDoctorOnClick()
    {
        Button createNewDoctor = findViewById(R.id.iddoctor_button_add_doctor);
        createNewDoctor.setOnClickListener(view -> showDoctorEditDialog(false, null, 0));
    }

    private void initSearchWidget()
    {
        SearchView searchView = findViewById(R.id.medicamenteListSearchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                filterList(query);
                return true;
            }
        });
    }

    private void filterList(String text){
        List<Doctor> filtredList = new ArrayList<>();
        for (Doctor doctor : filtredList)
        {
            if (doctor.getNameDoctor().toLowerCase().contains(text.toLowerCase())) {
                filtredList.add(doctor);
            }
            if (doctor.getFirstname().toLowerCase().contains(text.toLowerCase())) {
                filtredList.add(doctor);
            }
        }
        if(filtredList.isEmpty()){
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        }
        else
        {
            dAdapter.setFiltredList(filtredList);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.doctor_menu, menu);
        return true;
    }



    @SuppressLint("NotifyDataSetChanged")
    private void createDoctor(String email, String name, String firstname, Integer specialization, String phoneNumber) {
        RequestService.createDoctor(AdminDoctorActivity.this, firstname, name, email, phoneNumber, specialization);
    }


    private void updateDoctor(String name, String firstname, Integer specialization, String email, String password, int position) {

    }


    private void changeStatusDoctor(Integer doctorId) {
        if (doctorId != null){
            RequestService.changeStatusDoctor(AdminDoctorActivity.this, doctorId);
        }
    }

    private void showActionsDialog(final int position) {
        CharSequence[] colors = new CharSequence[]{"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose option");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int selected_option) {
                if (selected_option == 0) {
                    showDoctorEditDialog(true, doctorList.get(position), position);
                } else {
                    changeStatusDoctor(doctorList.get(position).getId());
                }
            }
        });
        builder.show();
    }

    private void showDoctorEditDialog(final boolean shouldUpdate, final Doctor doctor, final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.dialog_doctor, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(AdminDoctorActivity.this);
        alertDialogBuilderUserInput.setView(view);

        final EditText inputName = view.findViewById(R.id.iddialog_doctor_name);
        final EditText inputFirstname = view.findViewById(R.id.iddialog_doctor_firstname);
        Integer inputSpecialization = 1;
        final EditText inputEmail = view.findViewById(R.id.iddialog_doctor_email);
        final  EditText inputPhoneNumber = view.findViewById(R.id.iddialog_doctor_phoneNumber);

        TextView dialogTitle = view.findViewById(R.id.iddialog_doctor_title);
        dialogTitle.setText(!shouldUpdate ? "Add new doctor" : "Edit doctor");

        if (shouldUpdate && doctor != null) {
            inputName.setText(doctor.getNameDoctor());
            inputFirstname.setText(doctor.getFirstname());
            inputSpecialization = 1;
        }
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(shouldUpdate ? "Update" : "Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {

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

        Integer finalInputSpecialization = inputSpecialization;
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(inputName.getText().toString()) || TextUtils.isEmpty(inputFirstname.getText().toString())) {
                    Toast.makeText(AdminDoctorActivity.this,
                            "Fill in the doctor's fields!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }

                if (shouldUpdate && doctor != null) {

                } else {
                    createDoctor(inputEmail.getText().toString(),
                            inputFirstname.getText().toString(),
                            inputName.getText().toString(),
                            finalInputSpecialization,
                            inputPhoneNumber.getText().toString());

                }
            }
        });
    }

    private void toggleEmptyDoctor() {
        if (doctorList.size() > 0) {
            noDoctorView.setVisibility(View.GONE);
        } else {
            noDoctorView.setVisibility(View.VISIBLE);
        }
    }
}