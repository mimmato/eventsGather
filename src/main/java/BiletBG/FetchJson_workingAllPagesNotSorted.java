package BiletBG;

import com.google.gson.Gson;
import org.jsoup.Jsoup;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class FetchJson_workingAllPagesNotSorted {
    private static final String BASE_URL = "https://panel.bilet.bg/api/v1/events";
    private static final HttpClient client = HttpClient.newHttpClient();

    public static void main(String[] args) {
        List<EventsStructure> allEvents = new ArrayList<>();
        String nextPageUrl = BASE_URL + "?page=1";

        try {
            while (nextPageUrl != null) {
                // Fetch data from the API
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(nextPageUrl))
                        .header("Accept", "application/json")
                        .header("Accept-Language", "en")
                        .GET()
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    String jsonResponse = response.body();
                    Gson gson = new Gson();
                    ResponseWrapper responseWrapper = gson.fromJson(jsonResponse, ResponseWrapper.class);

                    if (responseWrapper.data != null) {
                        for (EventsStructure event : responseWrapper.data) {
                            if (!event.isFinished()) { // Filter out finished events
                                allEvents.add(event);
                                System.out.println("Event: " + event.getName());
                                System.out.println("Date: " + event.getDate());
                                System.out.println("link: " + "https://bilet.bg/bg/events/" + event.getSlug());
                                System.out.println("Page source: " + nextPageUrl);


                                // Clean description (remove HTML tags)
                                if (event.getDescription() != null) {
                                    String cleanDescription = Jsoup.parse(event.getDescription()).text();
                                    System.out.println("Description: " + cleanDescription);
                                } else {
                                    System.out.println("Description: N/A");
                                }
                            }
                            System.out.println("--------------------");
                        }
                    }
                    nextPageUrl = responseWrapper.getNextPageUrl();
                } else {
                    System.out.println("Failed to fetch data. HTTP Status: " + response.statusCode());
                    break;
                }
            }

            System.out.println("Total Events Found: " + allEvents.size());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
