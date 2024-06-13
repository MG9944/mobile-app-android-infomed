package umg.student.project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import umg.student.project.AdminActivity.AdminMainActivity;
import umg.student.project.DoctorActivity.ForgotPasswordActivity;
import umg.student.project.DoctorActivity.MainActivity;
import umg.student.project.Utils.RequestService;
import umg.student.project.Utils.SessionManager;


public class LoginActivity extends AppCompatActivity {

    EditText login_Email;
    EditText login_Password;
    Button login_button_login;
    TextView login_TextView_forgot_password;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        try {
            initWidgets();
            forgotPasswordOnClick();
            loginOnClick();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void initWidgets() throws Exception {
        login_Email = findViewById(R.id.idlogin_edittext_email);
        login_Password = findViewById(R.id.idlogin_edittext_password);
        login_button_login = findViewById(R.id.idlogin_button_login);
        login_TextView_forgot_password = findViewById(R.id.idlogin_textview_forgot_password);
        sessionManager = new SessionManager(getApplicationContext());
    }

    private void forgotPasswordOnClick()
    {
        login_TextView_forgot_password.setOnClickListener(v -> {
            Intent forgotPasswordIntent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(forgotPasswordIntent);
            finish();
        });
    }

    private void loginOnClick()
    {
        login_button_login.setOnClickListener(view -> {
            try {
                checkAuthorization();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void checkAuthorization() throws Exception {
        String email = login_Email.getText().toString();
        String password = login_Password.getText().toString();
        checkCredentials(email, password);

        RequestService.loginActivity(this, email, password);
            Toast.makeText(LoginActivity.this, "Logged as Admin!", Toast.LENGTH_SHORT).show();
            Intent GoToMain = new Intent(LoginActivity.this, AdminMainActivity.class);
        getUserInfo(getApplicationContext());
            startActivity(GoToMain);
            sessionManager.setLogin(true);
            finish();
    }


    private void checkCredentials(String email, String password){
        if(!email.contains("@")){
            Toast.makeText(LoginActivity.this, "Your email is not valid!", Toast.LENGTH_SHORT).show();
        }

        if(password.isEmpty() || password.length()<8){
            Toast.makeText(LoginActivity.this, "Your password in not valid!", Toast.LENGTH_SHORT).show();
        }
    }

    public static void getUserInfo(Context context) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, RequestService.GET_USER_INFO, null, (Response.Listener<JSONArray>) response -> {
            try {
                for (int i = 0; i < response.length(); i++) {
                    JSONObject userObject = response.getJSONObject(i);
                    String firstName = userObject.getString("firstname");
                    String lastName = userObject.getString("lastname");
                    String email = userObject.getString("email");
                    JSONArray roles = userObject.getJSONArray("roles");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
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
}
