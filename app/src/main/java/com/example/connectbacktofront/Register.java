package com.example.connectbacktofront;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    private static final String TAG = Register.class.getSimpleName();
    private static final String STUDENT_ADD_URL = "http://192.168.140.223:8080/students/add"; // Your server's add URL

    private EditText nameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText genderEditText;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize views
        nameEditText = findViewById(R.id.name);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.pass);
        genderEditText = findViewById(R.id.gender);
        submitButton = findViewById(R.id.summit);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addStudent();
            }
        });
    }

    private void addStudent() {
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String gender = genderEditText.getText().toString().trim();

        // Input validation
        if (TextUtils.isEmpty(name)) {
            nameEditText.setError("Name is required");
            return;
        }
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email is required");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password is required");
            return;
        }
        if (TextUtils.isEmpty(gender)) {
            genderEditText.setError("Gender is required");
            return;
        }

        sendStudentData(name, email, password, gender);
    }

    private void sendStudentData(final String name, final String email, final String password, final String gender) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                STUDENT_ADD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        handleSuccessfulResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handleErrorResponse(error);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);
                params.put("gender", gender);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }
        };

        requestQueue.add(stringRequest);
    }

    private void handleSuccessfulResponse(String response) {
        Toast.makeText(this, "Student added successfully!", Toast.LENGTH_SHORT).show();
        // Clear the input fields after successful submission
        nameEditText.setText("");
        emailEditText.setText("");
        passwordEditText.setText("");
        genderEditText.setText("");

        if (response == null || response.isEmpty()) {
            Log.w(TAG, "Successful Response: but the server sent an empty or null response");
        } else {
            Log.d(TAG, "Successful Response: " + response);
        }
        if(response==null){
            System.out.println("null response");
        }else if(response.isEmpty()){
            System.out.println("Empty response");
        }else{
            System.out.println(response);
        }

    }

    private void handleErrorResponse(VolleyError error) {
        NetworkResponse networkResponse = error.networkResponse;
        String errorMessage = "Error adding student";

        if (networkResponse != null && networkResponse.data != null) {
            String errorResponse = new String(networkResponse.data);
            Log.e(TAG, "Error Response: " + errorResponse);
            errorMessage += ": " + errorResponse;
        } else {
            Log.e(TAG, "Error: " + error.getMessage(), error);
            errorMessage += ": " + error.getMessage();
        }

        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
    }
}
