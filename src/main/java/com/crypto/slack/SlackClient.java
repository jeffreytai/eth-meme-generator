package com.crypto.slack;

import allbegray.slack.SlackClientFactory;
import allbegray.slack.exception.SlackException;
import allbegray.slack.webapi.SlackWebApiClient;

import java.io.File;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class SlackClient {

    private final Integer RETRY_COUNT = 3;
    private final String SLACK_CHANNEL = "#general";

    private SlackWebApiClient webApiClient;
    private String username;

    /**
     * Instantiate the web API client with the designated properties
     */
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

    /**
     * Uploads a file to the specified Slack channel. If an exception occurs, attempt a total of RETRY_COUNT times.
     * @param file
     */
    public void uploadFile(File file) {
        try {
            for (int count = 0; count < this.RETRY_COUNT; count++) {
                try {
                    webApiClient.uploadFile(file, "", "",  this.SLACK_CHANNEL);
                    break;
                } catch (SlackException ex) {
                    TimeUnit.SECONDS.sleep(5);
                }
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Clean up resources used for Slack API
     */
    public void shutdown() {
        this.webApiClient.shutdown();
    }
}
