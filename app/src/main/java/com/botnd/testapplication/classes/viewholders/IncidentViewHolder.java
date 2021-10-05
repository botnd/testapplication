package com.botnd.testapplication.classes.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.botnd.testapplication.R;
import com.botnd.testapplication.classes.models.Incident;

public class IncidentViewHolder extends RecyclerView.ViewHolder
{
    private Incident incident;

    public IncidentViewHolder(@NonNull View itemView)
    {
        super(itemView);
    }


    public void setIncident(Incident incident)
    {
        this.incident = incident;

        TextView extsy = this.itemView.findViewById(R.id.extsyname);
        extsy.setText(incident.extSysName);


        TextView description = this.itemView.findViewById(R.id.description);
        description.setText(incident.description);


        TextView isKnownErrorDate = this.itemView.findViewById(R.id.date);
        isKnownErrorDate.setText(incident.getIsKnownErrorDateFormatted());


        TextView targetFinish = this.itemView.findViewById(R.id.targetfinish);
        targetFinish.setText(incident.getTargetFinishFormatted());


        TextView status = this.itemView.findViewById(R.id.status);
        status.setText(incident.status);
    }

    public Incident getIncident()
    {
        return incident;
    }
}
