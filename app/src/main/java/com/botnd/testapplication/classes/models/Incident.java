package com.botnd.testapplication.classes.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Incident implements Serializable
{

    @JsonAlias("STATUS")
    public String status;

    @JsonAlias("TICKETID")
    public int tickedID;

    @JsonAlias("REPORTEDBY")
    public String reportedBy;

    @JsonAlias("CLASSIDMAIN")
    public String classIdMain;

    @JsonAlias("CRITIC_LEVEL")
    public byte criticLevel;

    @JsonAlias("ISKNOWNERRORDATE")
    public String isKnownErrorDate;

    public String getIsKnownErrorDateFormatted()
    {
        return parseDate(isKnownErrorDate);
    }


    private String parseDate(String date)
    {
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        SimpleDateFormat output = new SimpleDateFormat("dd.MM.yyyy HH:mm");

        try
        {
            return output.format(input.parse(date));
        }
        catch (ParseException e)
        {
            return "";
        }
    }

    @JsonAlias("TARGETFINISH")
    public String targetFinish;

    public String getTargetFinishFormatted()
    {
        return parseDate(targetFinish);
    }

    @JsonAlias("DESCRIPTION")
    public String description;

    @JsonAlias("EXTSYSNAME")
    public String extSysName;

    @JsonAlias("NORM")
    public float norm;

    @JsonAlias("LNORM")
    public int lnorm;
}
