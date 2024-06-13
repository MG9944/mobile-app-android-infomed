package umg.student.project.DoctorActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import umg.student.project.R;
import umg.student.project.Utils.RequestService;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText login_Email;
    Button forgot_button_continue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        initWidgets();
        continueForgotOnClick();
    }

    private void initWidgets()
    {
        login_Email = findViewById(R.id.idforgot_edittext_email);
        forgot_button_continue = findViewById(R.id.idforgot_button_continue);
    }

    private void continueForgotOnClick()
    {
        forgot_button_continue.setOnClickListener(v -> checkExistEmail());
    }


    private void checkExistEmail()
    {
        String email = login_Email.getText().toString().trim();

        if(!email.contains("@")){
            Toast.makeText(ForgotPasswordActivity.this, "Your email is not valid!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            RequestService.checkEmailExist(ForgotPasswordActivity.this, email);
            Intent GoToVerficationCode = new Intent(ForgotPasswordActivity.this, VerficationGeneratedCodeActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("email", email);
            GoToVerficationCode.putExtras(bundle);
            startActivity(GoToVerficationCode);
            finish();
        }
    }
}