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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
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
import java.util.concurrent.CountDownLatch;

import umg.student.project.ActivityAdapter.Medicamente_Adapter;
import umg.student.project.ModelClasses.Medicamente;
import umg.student.project.R;
import umg.student.project.Utils.MyDividerItemDecoration;
import umg.student.project.Utils.RecyclerTouchListener;
import umg.student.project.Utils.RequestService;

public class AdminMedicamenteActivity extends AppCompatActivity {
    private Medicamente_Adapter mAdapter;
    private List<Medicamente> medicamentList = new ArrayList<>();
    private CoordinatorLayout coordinatorLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private TextView noMedicamenteView;

    /* ~~~ onCreate ~~~ */
    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_medicamente);
        initWidgets();
        getMedicamente(getApplicationContext());
        initSearchWidget();
        createMedicamenteOnClick();
        swipeRefreshLayout();
    }


    private void initWidgets()
    {
        swipeRefreshLayout = findViewById(R.id.medicamente_swpiper_layout);
        coordinatorLayout = findViewById(R.id.medicamente_coordinator_layout);
        recyclerView = findViewById(R.id.medicamente_recycler_view);
        noMedicamenteView = findViewById(R.id.empty_medicamente_view);
    }

    private void swipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            new Handler().postDelayed(() -> {
                medicamentList.clear();
                getMedicamente(getApplicationContext());
                swipeRefreshLayout.setRefreshing(false);
            }, 3000);
        });
    }
    public void getMedicamente(Context context) {
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
                    refreshAllContent(1000);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            initMedicamente();
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

    public void initMedicamente()
    {
        mAdapter = new Medicamente_Adapter(getApplicationContext(), medicamentList);
    }

    public void initRecyclerView()
    {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(mAdapter);
        toggleEmptyMedicamente();
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
        SearchView searchView = (SearchView) findViewById(R.id.adminMedicamenteListSearchView);

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
        ArrayList<Medicamente> filteredlist = new ArrayList<Medicamente>();
        for (Medicamente item : medicamentList) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item);
            }

            if (item.getCategory().toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            mAdapter.filterList(filteredlist);
        }
    }

    private void createMedicamenteOnClick()
    {
        Button createNewMedicamente = findViewById(R.id.idmedicamente_button_add_medicamente);
        createNewMedicamente.setOnClickListener(view -> showMedicamenteEditDialog(false, null, 0));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.medicine_menu, menu);
        return true;
    }
    @SuppressLint({"NonConstantResourceId", "NotifyDataSetChanged"})
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.idmenu_medicine_name:
                medicamentList.clear();;
                mAdapter.notifyDataSetChanged();
                return true;
            case R.id.idmenu_medicine_category:
                medicamentList.clear();
                mAdapter.notifyDataSetChanged();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void createMedicament(String name, String category) {
        RequestService.createMedicamente(AdminMedicamenteActivity.this, name, category);
        mAdapter.notifyDataSetChanged();
        toggleEmptyMedicamente();
    }

    private void updateMedicament(String name, String category, Integer medicamenteId) {

        RequestService.editMedicamente(AdminMedicamenteActivity.this, name, category, medicamenteId );
        mAdapter.notifyItemChanged(medicamenteId);
        toggleEmptyMedicamente();

    }


    private void showActionsDialog(final int position) {
        CharSequence[] colors = new CharSequence[]{"Edit"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose option");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int selected_option) {
                if (selected_option == 0) {
                    showMedicamenteEditDialog(true, medicamentList.get(position), position);
                }
            }
        });
        builder.show();
    }

    public void showMedicamenteEditDialog(final boolean shouldUpdate, final Medicamente medicament, final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.dialog_medicamente, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(AdminMedicamenteActivity.this);
        alertDialogBuilderUserInput.setView(view);

        final EditText inputName = view.findViewById(R.id.iddialog_medicament_name);
        final EditText inputCategory = view.findViewById(R.id.iddialog_medicament_category);
        TextView dialogTitle = view.findViewById(R.id.iddialog_medicamente_title);
        dialogTitle.setText(!shouldUpdate ? "Add new medicamente" : "Edit medicamente");

        if (shouldUpdate && medicament != null) {
            inputName.setText(medicament.getName());
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
                if (TextUtils.isEmpty(inputName.getText().toString()) || TextUtils.isEmpty(inputCategory.getText().toString())) {
                    Toast.makeText(AdminMedicamenteActivity.this,
                            "Fill in the fields of the medicine!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }

                if (shouldUpdate && medicament != null) {
                    updateMedicament(inputName.getText().toString(), inputCategory.getText().toString(),
                            medicamentList.get(position).getId());
                } else {
                   createMedicament(inputName.getText().toString(), inputCategory.getText().toString());
                }
            }
        });
    }

    private void toggleEmptyMedicamente() {
        if (medicamentList.size() > 0) {
            noMedicamenteView.setVisibility(View.GONE);
        } else {
            noMedicamenteView.setVisibility(View.VISIBLE);
        }
    }

    public void refreshAllContent(final long timetoupdate) {
        new CountDownTimer(timetoupdate, 100) {
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {
                Log.i("SCROLLS ", "UPDATE CONTENT HERE ");
                medicamentList.clear();
                getMedicamente(getApplicationContext());
            }
        }.start();
    }

}