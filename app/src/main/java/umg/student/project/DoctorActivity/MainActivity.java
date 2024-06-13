package umg.student.project.DoctorActivity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import umg.student.project.AdminActivity.AdminAppointmentActivity;
import umg.student.project.AdminActivity.AdminIllnessActivity;
import umg.student.project.AdminActivity.AdminMainActivity;
import umg.student.project.AdminActivity.AdminPatientActivity;
import umg.student.project.R;
import umg.student.project.Utils.SessionManager;
import umg.student.project.WelcomeActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    ImageView imageView;
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        imageView = findViewById(R.id.doctor_menu);
        CardView AppointmentCard, PatientCardCard,PatientListCard, IllnessCard, MedicamenteCard;

        AppointmentCard= findViewById(R.id.idAppointement);
        PatientCardCard = findViewById(R.id.idPatientCard);
        PatientListCard = findViewById(R.id.idPatient);
        IllnessCard = findViewById(R.id.idIllness);
        MedicamenteCard = findViewById(R.id.idMedicamente);


        AppointmentCard.setOnClickListener(this);
        PatientCardCard.setOnClickListener(this);
        PatientListCard.setOnClickListener(this);
        IllnessCard.setOnClickListener(this);
        MedicamenteCard.setOnClickListener(this);
    }

    private void menuAction()
    {
        imageView.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(MainActivity.this, imageView);
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.nav_settings:
                        Intent settingsActivity = new Intent(MainActivity.this, SettingsActivity.class);
                        startActivity(settingsActivity);
                        return true;
                    case R.id.nav_logout:
                        sessionManager = new SessionManager(getApplicationContext());
                        Intent GoToWelcome = new Intent(MainActivity.this, WelcomeActivity.class);
                        GoToWelcome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(GoToWelcome);
                        sessionManager.setLogin(false);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage("Would you like to leave the app?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        sessionManager = new SessionManager(getApplicationContext());
                        Intent a = new Intent(Intent.ACTION_MAIN);
                        a.addCategory(Intent.CATEGORY_HOME);
                        a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(a);
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

    //PLAY ACTIVITIES (menu buttons) each time we press:
    //override onClick method
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()) { //we check each time the card has been pressed(click)

            case R.id.idPatientCard:
                i = new Intent(this, PatientCardActivity.class);
                startActivity(i); break;
            case R.id.idPatient :
                i = new Intent(this, PatientActivity.class);
                startActivity(i); break;
            case R.id.idAppointement:
                i = new Intent(this, AppointmentActivity.class);
                startActivity(i); break;
            case R.id.idMedicamente :
                i = new Intent(this, MedicamenteActivity.class);
                startActivity(i); break;
            case R.id.idIllness :
                i = new Intent(this, IllnessActivity.class);
                startActivity(i); break;
            default: break;
        }
    }

}
