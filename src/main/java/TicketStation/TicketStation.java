package TicketStation;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;
import java.net.http.HttpRequest.BodyPublishers;
import java.util.zip.GZIPInputStream;
import java.io.ByteArrayInputStream;

public class TicketStation {

    public static void main(String[] args) {
        // Create HttpClient
        HttpClient client = HttpClient.newHttpClient();

        // The URL for the POST request
        URI uri = URI.create("https://ticketstation.bg/system/tc:gt");

        // Prepare the POST request payload (same as --data-raw in cURL)
        String jsonBody = "l=1&tc=top-events&take=16&count=1&whitelabel_id=3";

        // Build the HttpRequest with headers and body
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:136.0) Gecko/20100101 Firefox/136.0")
                .header("Accept", "*/*")
                .header("Accept-Language", "en-US,en;q=0.5")
                .header("Accept-Encoding", "gzip, deflate, br, zstd")
                .header("X-CSRF-Token", "YIO6ru1S53ZAVTlXmQJZ1ee5lSIUQKLtEB81xe80")
                .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .header("X-Requested-With", "XMLHttpRequest")
                .header("Origin", "https://ticketstation.bg")
//                .header("Connection", "keep-alive")
                .header("Referer", "https://ticketstation.bg/bg/top-events")
                .header("Cookie", "urbo_main_session=eyJpdiI6IkRBOWpMc25FUVdodzFNcERSZ1NmckE9PSIsInZhbHVlIjoiNlFWS2FuSzR0VGd0ckZ1dFYyYUgrZUc2NHpoaXA5MWIrbGorbTBISlYzYjNwSmVzYm1ydUlRa3A5Q1BDcUxINCIsIm1hYyI6IjgyZWJjYTk1YTg2OTk5YmE0OTJjMWUzMWY1NTk3ZTY4OGI2YTJkM2E1NjZjMmQ0MzA3NDI0MWFjM2ZjMzJhNTYifQ%3D%3D")
                .header("Sec-Fetch-Dest", "empty")
                .header("Sec-Fetch-Mode", "cors")
                .header("Sec-Fetch-Site", "same-origin")
                .header("TE", "trailers")
                .POST(BodyPublishers.ofString(jsonBody))
                .build();

        try {
            // Send the request and get the response
            HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());

            // Check the response status
            System.out.println("Response Status Code: " + response.statusCode());

            // Check for encoding (gzip) and handle it
            byte[] responseBody = response.body();
            if ("gzip".equalsIgnoreCase(response.headers().firstValue("Content-Encoding").orElse(""))) {
                // Decompress GZIP response
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(responseBody);
                GZIPInputStream gzipInputStream = new GZIPInputStream(byteArrayInputStream);
                responseBody = gzipInputStream.readAllBytes();
            }

            String responseBodyStr = new String(responseBody);
            System.out.println("Response Body: " + responseBodyStr);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
