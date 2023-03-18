package edu.umich.eecs.april.apriltag.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import edu.umich.eecs.april.apriltag.R;
import edu.umich.eecs.april.apriltag.model.Model;
import edu.umich.eecs.april.apriltag.thread.DataFetchThread;

public class LoginActivity extends AppCompatActivity {
    private DataFetchThread dataFetchThread;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

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

        AlertDialog alertDialog = waitDialog("Fetching Data");

        dataFetchThread = new DataFetchThread(alertDialog);
        Model.setMainContext(this);
        Model.setDataFetch(dataFetchThread);
        dataFetchThread.start();
    }

    private AlertDialog waitDialog(String sTitle) {
        AlertDialog.Builder aBuilder = new AlertDialog.Builder(this);

        aBuilder.setTitle(sTitle);
        aBuilder.setCancelable(false);

        ImageView img = new ImageView(this);
        img.setImageResource(R.drawable.app_logo);
        img.setLayoutParams(new ViewGroup.LayoutParams(200, 200));
        aBuilder.setView(img);

        ProgressBar progressBar = new ProgressBar(this);
        progressBar.setLayoutParams(new ViewGroup.LayoutParams(200, 200));
        progressBar.setIndeterminate(true);
        aBuilder.setView(progressBar);

        AlertDialog alert = aBuilder.create();
        alert.show();

        return alert;
    }
}
