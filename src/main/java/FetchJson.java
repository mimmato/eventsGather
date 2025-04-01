import com.google.gson.Gson;
import org.jsoup.Jsoup;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FetchJson {
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

//                                PriceFetcher.fetchPriceFromCartPage(event.getSlug());
                            }
                        }
                    }
                    System.out.println("Page source found: " + nextPageUrl);
                    nextPageUrl = responseWrapper.getNextPageUrl();
                } else {
                    System.out.println("Failed to fetch data. HTTP Status: " + response.statusCode());
                    break;
                }
            }

            System.out.println("Total Events Found: " + allEvents.size());
            System.out.println("--------------------");

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            allEvents.sort(Comparator.comparing(event -> {
                String dateTime = event.getDate(); // Example: "2025-04-11 19:00"
                String dateOnly = dateTime.split(" ")[0]; // Extract "2025-04-11"
                return LocalDate.parse(dateOnly, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }));

            // === PRINT SORTED EVENTS ===
            for (EventsStructure event : allEvents) {
                System.out.println("Event: " + event.getName());
                System.out.println("Date: " + event.getDate());
                System.out.println("Event Info: " + "https://bilet.bg/bg/events/" + event.getSlug());
//                System.out.println("Page source: " + nextPageUrl);
                System.out.println("Buy Ticket: " + "https://bilet.bg/bg/cart/" + event.getSlug());

                // Clean description (remove HTML tags)
                if (event.getDescription() != null) {
                    String cleanDescription = Jsoup.parse(event.getDescription()).text();
                    System.out.println("Description: " + cleanDescription);
                } else {
                    System.out.println("Description: N/A");
                }
                PriceFetcher.fetchEventDetails(event);
                System.out.println("--------------------");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
