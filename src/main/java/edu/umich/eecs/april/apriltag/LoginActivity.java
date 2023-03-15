package edu.umich.eecs.april.apriltag;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        EditText userName = (EditText) findViewById(R.id.inputUserName);
        Button nextBtn = (Button) findViewById(R.id.nextButton);

        nextBtn.setOnClickListener(view -> {
            if (!userName.getText().toString().isEmpty()) {
                Model.setUserName(userName.getText().toString());
                Intent intent = new Intent(this, ApriltagDetectorActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Please insert your name", Toast.LENGTH_LONG).show();
            }
        });
    }
}
