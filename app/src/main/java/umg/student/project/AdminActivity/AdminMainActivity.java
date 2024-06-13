package umg.student.project.AdminActivity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import umg.student.project.DoctorActivity.MainActivity;
import umg.student.project.DoctorActivity.PatientCardActivity;
import umg.student.project.DoctorActivity.SettingsActivity;
import umg.student.project.R;
import umg.student.project.Utils.SessionManager;
import umg.student.project.WelcomeActivity;

public class AdminMainActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView imageView;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        setActionBar();
        initActivityCardAndOnClick();
        menuAction();
    }

    private void setActionBar()
    {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

    private void initActivityCardAndOnClick()
    {
        imageView = findViewById(R.id.image_menu);
        CardView AppointmentCard, DoctorCard, PatientListCard, IllnessCard, MedicamenteCard;

        AppointmentCard= findViewById(R.id.idAppointement);
        DoctorCard = findViewById(R.id.idDoctor);
        PatientListCard = findViewById(R.id.idPatient);
        IllnessCard = findViewById(R.id.idIllness);
        MedicamenteCard = findViewById(R.id.idMedicamente);


        AppointmentCard.setOnClickListener(this);
        DoctorCard.setOnClickListener(this);
        PatientListCard.setOnClickListener(this);
        IllnessCard.setOnClickListener(this);
        MedicamenteCard.setOnClickListener(this);
    }

    private void menuAction()
    {
        Context wrapper = new ContextThemeWrapper(this, R.style.style_PopupMenu);
        imageView.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(wrapper, imageView);
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.nav_settings:
                        Intent settingsActivity = new Intent(AdminMainActivity.this, SettingsActivity.class);
                        startActivity(settingsActivity);
                        return true;
                    case R.id.nav_logout:
                        sessionManager = new SessionManager(getApplicationContext());
                        Intent GoToWelcome = new Intent(AdminMainActivity.this, WelcomeActivity.class);
                        GoToWelcome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(GoToWelcome);
                        sessionManager.setLogin(false);
                        finish();
                        return true;
                }
                return false;
            });
            popupMenu.inflate(R.menu.menu);
            popupMenu.show();
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminMainActivity.this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage("Would you like to leave the app?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        sessionManager = new SessionManager(getApplicationContext());
                        Intent GoToWelcome = new Intent(AdminMainActivity.this, WelcomeActivity.class);
                        GoToWelcome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(GoToWelcome);
                        sessionManager.setLogin(false);
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.idPatientCardAdmin:
                i = new Intent(this, PatientCardActivity.class);
                startActivity(i); break;
            case R.id.idDoctor:
                i = new Intent(this, AdminDoctorActivity.class);
                startActivity(i); break;
            case R.id.idPatient :
                i = new Intent(this, AdminPatientActivity.class);
                startActivity(i); break;
            case R.id.idAppointement:
                i = new Intent(this, AdminAppointmentActivity.class);
                startActivity(i); break;
            case R.id.idMedicamente :
                i = new Intent(this, AdminMedicamenteActivity.class);
                startActivity(i); break;
            case R.id.idIllness :
                i = new Intent(this, AdminIllnessActivity.class);
                startActivity(i); break;
            default: break;
        }
    }

}
