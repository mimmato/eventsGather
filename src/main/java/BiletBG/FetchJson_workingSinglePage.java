package BiletBG;

import com.google.gson.Gson;
import org.jsoup.Jsoup;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class FetchJson_workingSinglePage {

    public static void main(String[] args) {
        String url = "https://panel.bilet.bg/api/v1/events";

        try {
            // Create HttpClient
            HttpClient client = HttpClient.newHttpClient();

            // Create HttpRequest
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", "application/json")
                    .header("Accept-Language", "en")
                    .GET()
                    .build();

            // Send request and get response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Check for success (HTTP 200 OK)
            if (response.statusCode() == 200) {
                String jsonResponse = response.body();
                // Parse JSON with Gson
                Gson gson = new Gson();
                ResponseWrapper responseWrapper = gson.fromJson(jsonResponse, ResponseWrapper.class);

                // Print events
                if (responseWrapper.data != null) {
                    System.out.println("Total Events Found on " + url + " URL: " + responseWrapper.data.size());
                    System.out.println("--------------------------------");

                    int count = 0; // Counter for unfinished events
                    for (EventsStructure event : responseWrapper.data) {
                        if (!event.isFinished()) { // Only include unfinished events
                            System.out.println("Event: " + event.getName());
                            System.out.println("Date: " + event.getDate());

                            // Clean description (remove HTML tags)
                            if (event.getDescription() != null) {
                                String cleanDescription = Jsoup.parse(event.getDescription()).text();
                                System.out.println("Description: " + cleanDescription);
                            } else {
                                System.out.println("Description: N/A");
                            }


                            System.out.println("--------------------------------");
                            count++;
                        }
                    }
                } else {
                    System.out.println("No events found.");
                }
            } else {
                System.out.println("Failed to fetch data. HTTP Status: " + response.statusCode());
            }

        } catch (Exception e) {
            e.printStackTrace();



        }
    }
}