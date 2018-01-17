package com.crypto.utils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

import java.io.InputStream;
import java.nio.charset.Charset;

public class ApiUtils {

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }

        return sb.toString();
    }

    public static JSONObject getResponseBody(final String requestUrl) throws IOException {
        InputStream stream = new URL(requestUrl).openStream();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, Charset.forName("UTF-8")));
            String jsonString = readAll(reader);
            JSONObject json = new JSONObject(jsonString);
            return json;
        } finally {
            stream.close();
        }
    }
}
