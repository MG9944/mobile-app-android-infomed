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
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
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
import java.util.Objects;

import umg.student.project.ActivityAdapter.Illness_Adapter;
import umg.student.project.ModelClasses.Illness;
import umg.student.project.ModelClasses.Medicamente;
import umg.student.project.ModelClasses.Patient;
import umg.student.project.R;
import umg.student.project.Utils.MyDividerItemDecoration;
import umg.student.project.Utils.RecyclerTouchListener;
import umg.student.project.Utils.RequestService;

public class AdminIllnessActivity extends AppCompatActivity {
    private Illness_Adapter illnessAdapter;
    private final List<Illness> illnessList = new ArrayList<>();
    final List<Medicamente> medicamentList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private CoordinatorLayout coordinatorLayout;
    private RecyclerView recyclerView;
    private TextView noIllnessView;
    private Spinner medicamenteSpinner;
    private Integer selectedMedicamente;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true); //back button <- from toolbar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_illness);
        initWidgets();
        getIllness(getApplicationContext());
        initSearchWidget();
        createIllnessOnCLick();
        swipeRefreshLayout();
    }

    private void swipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            new Handler().postDelayed(() -> {
                illnessList.clear();
                getIllness(getApplicationContext());
                swipeRefreshLayout.setRefreshing(false);
            }, 3000);
        });
    }

    private void initWidgets()
    {
        swipeRefreshLayout = findViewById(R.id.illness_swpiper_layout);
        coordinatorLayout = findViewById(R.id.illness_coordinator_layout);
        recyclerView = findViewById(R.id.illness_recycler_view);
        noIllnessView = findViewById(R.id.empty_illness_view);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void getIllness(Context context) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, RequestService.GET_ILLNESS, null, (Response.Listener<JSONArray>) response -> {
            Log.d("respinse", String.valueOf(response));
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject illnessObject = response.getJSONObject(i);
                    Illness illness = new Illness();
                    illness.setId(illnessObject.getInt("id"));
                    illness.setIllnessName(illnessObject.getString("name").toString());
                    illness.setCategory(illnessObject.getString("category").toString());
                    illness.setMedicamente(illnessObject.getString("medicamente").toString());

                    illnessList.add(illness);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            initIllness();
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

    public void getMedicamenteInSpinner(Context context) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, RequestService.GET_MEDICAMENTE, null, (Response.Listener<JSONArray>) response -> {
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject medicamenteObject = response.getJSONObject(i);
                    Medicamente medicamente = new Medicamente();
                    medicamente.setId(medicamenteObject.getInt("id"));
                    medicamente.setName(medicamenteObject.getString("name").toString());
                    medicamente.setCategory(medicamenteObject.getString("category").toString());
                    medicamentList.add(medicamente);
                    initMedicamenteSpinner(medicamentList);
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

    private void initIllness() {
        illnessAdapter = new Illness_Adapter(this, illnessList);
        illnessAdapter.notifyDataSetChanged();
    }

    private void initRecycleView()
    {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(illnessAdapter);
        toggleEmptyIllness();

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


    private void createIllnessOnCLick()
    {
        Button createNewIllness = findViewById(R.id.idillness_button_add_illness);
        createNewIllness.setOnClickListener(view -> showIllnessEditDialog(false, null, 1));
    }

    private void initSearchWidget(){
        SearchView searchView = (SearchView) findViewById(R.id.adminIllnessListSearchView);

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
        // creating a new array list to filter our data.
        ArrayList<Illness> filteredlist = new ArrayList<Illness>();

        for (Illness item : illnessList) {
            if (item.getIllnessName().toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            illnessAdapter.filterList(filteredlist);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.illness_menu, menu);
        return true;
    }
    @SuppressLint({"NonConstantResourceId", "NotifyDataSetChanged"})
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.idmenu_illness_sort_name:
                illnessList.clear();
                illnessAdapter.notifyDataSetChanged();
                return true;
            case R.id.idmenu_illness_category:
                illnessList.clear();
                illnessAdapter.notifyDataSetChanged();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void createIllness(String name, String category, Integer medicine)
    {
        if (illnessList == null) {
            illnessAdapter.notifyDataSetChanged();
            toggleEmptyIllness();
        }
        RequestService.createIllness(AdminIllnessActivity.this, name, category, medicine);
    }

    private void updateIllness(String name, String category, Integer medicine, Integer illnessId)
    {
        RequestService.editIllness(AdminIllnessActivity.this, name, category, medicine, illnessId);
        toggleEmptyIllness();
    }


    private void showActionsDialog(final int position) {
        CharSequence[] colors = new CharSequence[]{"Edit", "Delete"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose option");
        builder.setItems(colors, (dialog, selected_option) -> {
            if (selected_option == 0) {
                showIllnessEditDialog(true, illnessList.get(position), position);
            }
        });
        builder.show();
    }


    public void showIllnessEditDialog(final boolean shouldUpdate, final Illness illness, final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.dialog_illness, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(AdminIllnessActivity.this);
        alertDialogBuilderUserInput.setView(view);

        final EditText inputName = view.findViewById(R.id.iddialog_illness_name);
        final EditText inputCategory = view.findViewById(R.id.iddialog_illness_category);
        medicamenteSpinner = view.findViewById(R.id.idillness_spinner_medicamente);
        TextView dialogTitle = view.findViewById(R.id.iddialog_illness_title);
        dialogTitle.setText(!shouldUpdate ? "Add new illness" : "Edit illness");
        getMedicamenteInSpinner(getApplicationContext());

        if (shouldUpdate && illness != null) {
            inputName.setText(illness.getIllnessName());
        }
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(shouldUpdate ? "Update" : "Save", (dialogInterface, i) -> {
                })
                .setNegativeButton("Cancel",
                        (dialogInterface, id) -> dialogInterface.cancel());

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            if (TextUtils.isEmpty(inputName.getText().toString()) ||
                    TextUtils.isEmpty(inputCategory.getText().toString())) {
                Toast.makeText(AdminIllnessActivity.this,
                        "Fill in the fields of the illness!", Toast.LENGTH_SHORT).show();
                return;
            }
            else
            {
                alertDialog.dismiss();
            }

            if (shouldUpdate && illness != null) {
                updateIllness(inputName.getText().toString(),
                                inputCategory.getText().toString(),
                                selectedMedicamente,
                                illnessList.get(position).getId()
                );
            } else {
                createIllness(inputName.getText().toString(),
                                 inputCategory.getText().toString(),
                                selectedMedicamente
                );
            }
        });
    }

    private void initMedicamenteSpinner(List<Medicamente> medicamenteList) {
        ArrayAdapter<Medicamente> medicamenteAdapter = new ArrayAdapter<>(AdminIllnessActivity.this,
                android.R.layout.simple_spinner_dropdown_item, medicamenteList);
        medicamenteSpinner.setAdapter(medicamenteAdapter);
        Medicamente medicamente = (Medicamente) medicamenteSpinner.getSelectedItem();
        selectedMedicamente = medicamente.getId();
    }
    private void toggleEmptyIllness() {
        if (illnessList.size() > 0) {
            noIllnessView.setVisibility(View.GONE);
        } else {
            noIllnessView.setVisibility(View.VISIBLE);
        }
    }
}