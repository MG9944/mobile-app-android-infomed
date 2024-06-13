package umg.student.project.DoctorActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import umg.student.project.R;
import umg.student.project.Utils.RequestService;
import umg.student.project.WelcomeActivity;

public class ChangePassword extends AppCompatActivity {

    EditText old_password;
    EditText new_password;
    EditText confirm_new_password;
    Button button_change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        initWidgets();
        changePasswordOnClick();
    }

    private void initWidgets()
    {
        old_password = findViewById(R.id.idforgot_edittext_old_password);
        new_password = findViewById(R.id.idforgot_edittext_new_password);
        confirm_new_password = findViewById(R.id.idforgot_edittext_confirm_new_password);
        button_change = findViewById(R.id.idforgot_button_change);
    }

    private void changePasswordOnClick()
    {
        button_change.setOnClickListener(v -> {
            try {
                changePassword();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }



    private void changePassword() throws Exception {
        String oldPassword = old_password.getText().toString().trim();
        String newPassword = new_password.getText().toString().trim();
        String confirmNewPassword = confirm_new_password.getText().toString().trim();

        if(newPassword.isEmpty() || newPassword.length()<8 || confirmNewPassword.isEmpty() || !confirmNewPassword.equals(newPassword)){
            Toast.makeText(ChangePassword.this, "Password not match!", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            RequestService.changeDoctorPassword(ChangePassword.this, oldPassword, newPassword, confirmNewPassword);
            Intent GoToWelcome = new Intent(ChangePassword.this, MainActivity.class);
            startActivity(GoToWelcome);
            finish();
        }

    }
}