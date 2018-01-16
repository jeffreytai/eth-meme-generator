package com.crypto.slack;

import allbegray.slack.SlackClientFactory;
import allbegray.slack.webapi.SlackWebApiClient;

import java.io.File;
import java.util.Properties;

public class SlackClient {

    private SlackWebApiClient webApiClient;
    private String username;

    public SlackClient() {
        try {
            Properties props = new Properties();
            props.load(SlackClient.class.getClassLoader().getResourceAsStream("slack.properties"));

            this.webApiClient = SlackClientFactory.createWebApiClient(props.getProperty("token"));
        } catch (Throwable ex) {
            System.err.println("Error in instantiating Slack client");
            ex.printStackTrace();
        }
    }

    public void uploadFile(File file) {
        webApiClient.uploadFile(file, "", "", "#testing");
    }

    public void shutdown() {
        this.webApiClient.shutdown();
    }
}
