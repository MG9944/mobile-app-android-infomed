package umg.student.project.DoctorActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import umg.student.project.R;

public class SettingsActivity extends AppCompatActivity {
    TextView changeUserPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        textViewOnClick();
    }

    private void textViewOnClick()
    {
        changeUserPassword = findViewById(R.id.changePassword);
        changeUserPassword.setOnClickListener(view -> {
            Intent changePassword = new Intent(SettingsActivity.this, ChangePassword.class);
            startActivity(changePassword);
            finish();
        });
    }
}