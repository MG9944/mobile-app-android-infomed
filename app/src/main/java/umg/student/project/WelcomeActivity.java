package umg.student.project;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
public class WelcomeActivity extends AppCompatActivity {

    Button signIn;
    Button signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initButtons();
        setActionBar();
        signInOnClick();
        signUpOnClick();
    }

    private void initButtons()
    {
        signIn = findViewById(R.id.id_button_login);
        signUp = findViewById(R.id.id_button_register);
    }

    private void setActionBar()
    {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

    private void signInOnClick()
    {
        signIn.setOnClickListener(view -> {
            Intent signIn = new Intent(WelcomeActivity.this, LoginActivity.class);
            startActivity(signIn);
            finish();
        });
    }

    private void signUpOnClick()
    {
        signUp.setOnClickListener(view -> {
            Intent signUp = new Intent(WelcomeActivity.this, RegisterActivity.class);
            startActivity(signUp);
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(WelcomeActivity.this);

        builder.setMessage("Would you like to leave the app?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> {
                    Intent a = new Intent(Intent.ACTION_MAIN);
                    a.addCategory(Intent.CATEGORY_HOME);
                    a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(a);
                })
                .setNegativeButton("No", (dialog, id) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();
    }
}