package com.botnd.testapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.botnd.testapplication.classes.models.Incident;
import com.botnd.testapplication.classes.services.IncidentsService;
import com.botnd.testapplication.classes.viewholders.IncidentViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity
{

    private static final int REQUEST_READ_PERMISSION = 1;

    private RecyclerView recycler;


    private List<Incident> incidents = new ArrayList<>();

    private SearchView.SearchAutoComplete searchAutoComplete;

    private String searchQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.MANAGE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.MANAGE_EXTERNAL_STORAGE}, REQUEST_READ_PERMISSION);
        }
        else
        {
            openFile();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            openFile();
        }
        else
        {
            Toast.makeText(this, getString(R.string.error_read_permission), Toast.LENGTH_LONG).show();
        }
    }

    @SuppressLint("RestrictedApi")
    private void openFile()
    {
        IncidentsService service = new IncidentsService(this);

        incidents = service.readFile();

        createAdapter(incidents);




        ArrayAdapter<Incident> adapter = new ArrayAdapter<Incident>(
            this,
            R.layout.incident_suggestion,
            R.id.text,
            new ArrayList<>(incidents)
        ) {

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
            {
                ViewGroup view = (ViewGroup) super.getView(position, convertView, parent);
                TextView textView = view.findViewById(R.id.text);

                textView.setText(getItem(position).description);

                if (searchQuery != null && searchQuery.length() > 0)
                {

                    String text = (String) textView.getText();

                    final int start = text.toLowerCase().indexOf(searchQuery.toLowerCase());
                    final int end = start + searchQuery.length();

                    if (start != -1)
                    {
                        Spannable spannable = new SpannableString(text);
                        spannable.setSpan(new BackgroundColorSpan(Color.YELLOW), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                        textView.setText(spannable);
                    }

                }


                return view;
            }


            @NonNull
            @Override
            public Filter getFilter()
            {
                return new Filter()
                {
                    @Override
                    protected FilterResults performFiltering(CharSequence charSequence)
                    {
                        FilterResults res = new FilterResults();

                        if (charSequence != null)
                        {
                            searchQuery = (String) charSequence;


                            List<Incident> filtered = incidents
                                .stream()
                                .filter(incident -> incident.description.toLowerCase().contains(searchQuery.toLowerCase()))
//                                .map(incident -> incident.description)
                                .collect(Collectors.toList());

                            res.values = filtered;
                            res.count = filtered.size();
                        }

                        return res;
                    }

                    @Override
                    protected void publishResults(CharSequence charSequence, FilterResults filterResults)
                    {
                        clear();

                        if (filterResults != null && filterResults.count > 0)
                        {
                            addAll((List<Incident>)filterResults.values);
                        }

                        notifyDataSetChanged();
                    }
                };
            }
        };

        searchAutoComplete.setAdapter(adapter);
        searchAutoComplete.setThreshold(0);

        searchAutoComplete.setOnItemClickListener((parent, view, position, id) ->
        {
            onIncidentClick(adapter.getItem(position));
        });
    }

    private void createAdapter(List<Incident> incidentList)
    {
        RecyclerView.Adapter<IncidentViewHolder> adapter = new RecyclerView.Adapter<IncidentViewHolder>()
        {
            @NonNull
            @Override
            public IncidentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view = getLayoutInflater().inflate(R.layout.incident_list_item, parent, false);


                return new IncidentViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull IncidentViewHolder holder, int position)
            {
                Incident incident = incidentList.get(position);
                holder.setIncident(incident);

                holder.itemView.setOnClickListener(view1 ->
                {
                    onIncidentClick(incident);
                });
            }

            @Override
            public int getItemCount()
            {
                return incidentList.size();
            }
        };

        recycler.setAdapter(adapter);
    }

    private void onIncidentClick(Incident incident)
    {
        Intent intent = new Intent(this, IncidentActivity.class);
        intent.putExtra(IncidentActivity.INCIDENT_EXTRA, incident);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem searchMenuItem = menu.findItem(R.id.search);

        SearchView searchView = (SearchView) searchMenuItem.getActionView();
        searchAutoComplete = searchView.findViewById(androidx.appcompat.R.id.search_src_text);

        SearchManager searchManager =
            (SearchManager) getSystemService(this.SEARCH_SERVICE);
        searchView.setSearchableInfo(
            searchManager.getSearchableInfo(getComponentName()));

        return true;
    }
}