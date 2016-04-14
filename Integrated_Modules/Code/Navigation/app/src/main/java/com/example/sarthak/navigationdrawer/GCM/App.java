package com.example.sarthak.navigationdrawer.GCM;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.fasterxml.jackson.databind.ObjectMapper;

public class App
{

    public App(){

    }

    public void sendNotification( String regId, String message)
    {
        System.out.println( "Sending POST to GCM" );

        String apiKey = "AIzaSyBFd63CGXAKcBVWqEyyjMMnVr_PiyKBpjU";
        Content content = createContent(regId,message);

        POST2GCM.post(apiKey, content);
    }

    public static Content createContent(String regId,String message){

        Content c = new Content();


        c.addRegId("APA91bGFWeh1zsRZrl343XVQbelR21gbT-Ze7PCeam3kU28oQzYWFEkoZ_ASsRqoABSx8z7KG8TWmqvDinTzKJrkp4HtoLk0BVrKXe6vGAv81r90uGYTPZpz3bYdZBbexVcSSPAyFq9aIJEXaqD0lNnmEliv5uogbw");
        c.createData("", message);

        return c;
    }
}
