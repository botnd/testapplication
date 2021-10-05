package com.botnd.testapplication;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.botnd.testapplication.classes.models.Incident;

public class IncidentActivity extends AppCompatActivity
{
    public static final String INCIDENT_EXTRA = "com.botnd.testapplication.incident_extra";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);


        Incident incident = (Incident) getIntent().getSerializableExtra(INCIDENT_EXTRA);

        setContentView(R.layout.incident_activity);


        TextView description = findViewById(R.id.description);
        description.setText(incident.description);


        TextView reportedBy = findViewById(R.id.reportedBy);
        reportedBy.setText(incident.reportedBy);


        TextView critLevel = findViewById(R.id.critLevel);
        critLevel.setText(String.valueOf(incident.criticLevel));


        TextView date = findViewById(R.id.date);
        date.setText(incident.getIsKnownErrorDateFormatted());


        TextView target = findViewById(R.id.target_finish);
        target.setText(incident.getTargetFinishFormatted());


        TextView system = findViewById(R.id.system);
        system.setText(incident.extSysName);


        TextView status = findViewById(R.id.status);
        status.setText(incident.status);


        TextView norm = findViewById(R.id.norm);
        norm.setText(String.valueOf(incident.norm));


        TextView lnorm = findViewById(R.id.lnorm);
        lnorm.setText(String.valueOf(incident.lnorm));
    }
}
