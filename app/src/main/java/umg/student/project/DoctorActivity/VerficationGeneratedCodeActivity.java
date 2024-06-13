package umg.student.project.DoctorActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import umg.student.project.R;
import umg.student.project.Utils.RequestService;

public class VerficationGeneratedCodeActivity extends AppCompatActivity {

    EditText verfication_code;
    Button verfication_button_continue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verfication_generated_code);
        initWidgets();
        continueVerficationCodeOnClick();
    }

    private void initWidgets()
    {
        verfication_code = findViewById(R.id.idverfi_edittext_code);
        verfication_button_continue = findViewById(R.id.idverfi_button_continue);
    }

    private void continueVerficationCodeOnClick()
    {
        verfication_button_continue.setOnClickListener(v -> verificationInsertedCode());
    }

    private void verificationInsertedCode()
    {
        String otp = verfication_code.getText().toString().trim();
        Bundle bundle = getIntent().getExtras();
        String email = bundle.getString("email");
        RequestService.checkCode(getApplicationContext(),email,otp);
            Intent GoToChangePassword = new Intent(VerficationGeneratedCodeActivity.this, ChangeForgotPasswordActivity.class);
            Bundle bundles = new Bundle();
            bundles.putString("email", email);
            GoToChangePassword.putExtras(bundle);
            startActivity(GoToChangePassword);
            finish();
    }
}