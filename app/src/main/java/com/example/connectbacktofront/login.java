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

public class login extends AppCompatActivity {

    private static final String TAG = login.class.getSimpleName();
    private static final String LOGIN_URL = "http://192.168.140.223:8080/students/login"; // Your server's login URL

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // Assuming you have a login layout

        // Initialize views
        emailEditText = findViewById(R.id.email); // Replace with your actual ID
        passwordEditText = findViewById(R.id.pass); // Replace with your actual ID
        loginButton = findViewById(R.id.summit); // Replace with your actual ID

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performLogin();
            }
        });
    }

    private void performLogin() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Basic validation
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email is required");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password is required");
            return;
        }

        sendLoginRequest(email, password);
    }

    private void sendLoginRequest(String email, String password) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        handleSuccessfulLogin(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handleLoginError(error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email); // Use correct parameter name
                params.put("password", password); // Use correct parameter name
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/x-www-form-urlencoded"); // Correct content type
                return headers;
            }
        };

        requestQueue.add(stringRequest);
    }

    private void handleSuccessfulLogin(String response) {
        Log.d(TAG, "Login Response: " + response);
        Toast.makeText(this, "Login Response: " + response, Toast.LENGTH_SHORT).show();

        // Add logic here to handle successful login, like:
        // 1. Parse the response (if it's JSON).
        // 2. Start the next activity.
        // 3. Save login state, etc.
    }

    private void handleLoginError(VolleyError error) {
        NetworkResponse networkResponse = error.networkResponse;
        String errorMessage = "Login Error";

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