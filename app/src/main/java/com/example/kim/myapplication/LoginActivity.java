package com.example.kim.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText idet, pwet;
    private Button loginbt,sigupbt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        idet = (EditText)findViewById(R.id.idEditText);
        pwet = (EditText)findViewById(R.id.pwEditText);
        loginbt = (Button)findViewById(R.id.loginButton);
        sigupbt = (Button)findViewById(R.id.sigupButton);
        idet.setPrivateImeOptions("defaultInputmode=english;");

        loginbt.setOnClickListener(this);
        sigupbt.setOnClickListener(this);

        Intent intent = new Intent(this, CalendarActivity.class);
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.loginButton :
                Intent intent = new Intent(this, CalendarActivity.class);
                startActivity(intent);
                break;
            case R.id.sigupButton :
                Toast.makeText(getApplicationContext(), pwet.getText().toString(), Toast.LENGTH_SHORT).show();
                Log.i("TEST", pwet.getText().toString());
                break;
        }
    }
}
