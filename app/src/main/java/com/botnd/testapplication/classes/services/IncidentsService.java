package com.botnd.testapplication.classes.services;

import android.content.Context;
import android.os.Environment;

import com.botnd.testapplication.classes.models.Incident;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class IncidentsService
{
    private final Context context;
    private static final String FILE_NAME = "incidents.json";

    public IncidentsService(Context context)
    {
        this.context = context;
    }


    public List<Incident> readFile()
    {
        String filePath = Environment.getExternalStorageDirectory() + File.separator + FILE_NAME;
        String json = "";
        try
        {
            File file = new File(filePath);

            FileInputStream stream = new FileInputStream(file);


            FileChannel channel = stream.getChannel();

            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());

            json = Charset.defaultCharset().decode(buffer).toString();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return handleFileData(json);
    }

    private List<Incident> handleFileData(String json)
    {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        List<Incident> incidents = new ArrayList<>();

        try
        {
            incidents = mapper.readValue(json, new TypeReference<List<Incident>>(){});
        }
        catch (JsonProcessingException e)
        {
            e.printStackTrace();
        }


        return incidents;
    }
}
