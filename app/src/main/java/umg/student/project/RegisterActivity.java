package umg.student.project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import umg.student.project.DoctorActivity.DoctorPersonalDataActivity;

public class RegisterActivity extends AppCompatActivity {

        EditText register_Email;;
        EditText register_Password;
        EditText register_Password_confirm;
        Button register_button_continue;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_register);
            initWidgets();
            registerContinueOnClick();
        }

        private void initWidgets()
        {
            register_Email = findViewById(R.id.idregister_edittext_email);
            register_Password = findViewById(R.id.idregister_edittext_password);
            register_Password_confirm = findViewById(R.id.idregister_edittext_confirm_password);
            register_button_continue = findViewById(R.id.idregister_button_continue);
        }


        private void registerContinueOnClick()
        {
            register_button_continue.setOnClickListener(view -> {
                checkCredentials();
            });
        }

        private void checkCredentials() {
            String email = register_Email.getText().toString().trim();
            String password = register_Password.getText().toString().trim();
            String confirmPassword = register_Password_confirm.getText().toString().trim();

            if (!email.contains("@")) {
                Toast.makeText(RegisterActivity.this, "Your email is not valid!", Toast.LENGTH_SHORT).show();
            }


            if (password.isEmpty() || password.length() < 8) {
                Toast.makeText(RegisterActivity.this, "Password must be 8 characters!", Toast.LENGTH_SHORT).show();
            }

            if (confirmPassword.isEmpty() || !confirmPassword.equals(password)) {
                Toast.makeText(RegisterActivity.this, "Password not match!", Toast.LENGTH_SHORT).show();
            }


                Intent moveToInsertPersonalData = new Intent(RegisterActivity.this, DoctorPersonalDataActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("email", email);
                bundle.putString("password", password);
                moveToInsertPersonalData.putExtras(bundle);
                startActivity(moveToInsertPersonalData);

        }

         public void onBackPressed() {
            Intent setIntent = new Intent(RegisterActivity.this, WelcomeActivity.class);
            setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(setIntent);
        }
}