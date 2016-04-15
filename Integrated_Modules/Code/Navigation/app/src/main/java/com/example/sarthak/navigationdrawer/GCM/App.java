package com.example.sarthak.navigationdrawer.GCM;

public class App
{

    public App(){

    }

    public void sendNotification(String regId, String message, String title)
    {
        System.out.println( "Sending POST to GCM" );

        String apiKey = "AIzaSyBFd63CGXAKcBVWqEyyjMMnVr_PiyKBpjU";
        Content content = createContent(regId, message, title);

        POST2GCM.post(apiKey, content);
    }

    public static Content createContent(String regId, String message, String title) {

        Content c = new Content();

/*"APA91bGFWeh1zsRZrl343XVQbelR21gbT-Ze7PCeam3kU28oQzYWFEkoZ_ASsRqoABSx8z7KG8TWmqvDinTzKJrkp4HtoLk0BVrKXe6vGAv81r90uGYTPZpz3bYdZBbexVcSSPAyFq9aIJEXaqD0lNnmEliv5uogbw"*/
        c.addRegId(regId);
        c.createData(title, message);

        return c;
    }
}
