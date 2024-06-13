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

public class ChangeForgotPasswordActivity extends AppCompatActivity {

    EditText forgot_new_password;
    EditText forgot_confirm_new_password;
    Button forgot_button_change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_forgot_password);
        initWidgets();
        changePasswordOnClick();
    }

    private void initWidgets()
    {
        forgot_new_password = findViewById(R.id.idforgot_edittext_new_password);
        forgot_confirm_new_password = findViewById(R.id.idforgot_edittext_confirm_new_password);
        forgot_button_change = findViewById(R.id.idforgot_button_change);
    }

    private void changePasswordOnClick()
    {
        forgot_button_change.setOnClickListener(v -> {
            try {
                changePasswordAndSendEmail();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }



    private void changePasswordAndSendEmail() throws Exception {
        String newPassword = forgot_new_password.getText().toString().trim();
        String confirmNewPassword = forgot_confirm_new_password.getText().toString().trim();
        Bundle bundle = getIntent().getExtras();
        String email = bundle.getString("email");

        if(newPassword.isEmpty() || newPassword.length()<8 || confirmNewPassword.isEmpty() || !confirmNewPassword.equals(newPassword)){
            Toast.makeText(ChangeForgotPasswordActivity.this, "Password not match!", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            RequestService.setNewPassword(ChangeForgotPasswordActivity.this, email, newPassword, confirmNewPassword);
            Intent GoToWelcome = new Intent(ChangeForgotPasswordActivity.this, WelcomeActivity.class);
            startActivity(GoToWelcome);
            finish();
        }

    }
}