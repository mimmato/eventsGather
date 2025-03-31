import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class PriceFetcher_beforeUI {

    private static final HttpClient client = HttpClient.newHttpClient();

    public static void fetchEventDetails(EventsStructure event) {
        try {
            String eventPageUrl = "https://bilet.bg/bg/cart/" + event.getSlug();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(eventPageUrl))
                    .header("Accept", "text/html")
                    .header("Accept-Language", "en")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Document doc = Jsoup.parse(response.body());

                // === Extract Event Location ===
                Element locationElement = doc.selectFirst("p:has(.fa-map-marker)");
                if (locationElement != null) {
                    String location = locationElement.text().replace("📍", "").trim();
                    System.out.println("Location: " + location);
                } else {
                    System.out.println("Location not found for " + event.getName());
                }

                // === Extract Ticket Prices ===
                Elements ticketBoxes = doc.select(".select-ticket-box");
                if (ticketBoxes.isEmpty()) {
                    System.out.println("No ticket prices found for " + event.getName());
                } else {
                    for (Element ticketBox : ticketBoxes) {
                        if (ticketBox.hasClass("disabled")) continue; // Skip disabled tickets

                        for (Element priceElement : ticketBox.select(".price-info-pc .price-block .ng-star-inserted")) {
                            System.out.println("Price: " + priceElement.text() + " BGN");
                        }
                    }
                }
            } else {
                System.out.println("Failed to fetch event page for " + event.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
