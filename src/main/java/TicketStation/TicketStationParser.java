package TicketStation;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TicketStationParser {

    public static void main(String[] args) {
        // Sample JSON response (this should be the response body from your HTTP request)
        String responseBody = "{\n" +
                "  \"slug\": \"The-Big-Garden-Party--Family-Edition\",\n" +
                "  \"title\": \"The Big Garden Party: Family Edition\",\n" +
                "  \"city_slug\": \"sofia\",\n" +
                "  \"max_cost\": \"240.00\",\n" +
                "  \"min_cost\": \"66.00\"\n" +
                "}";

        // Parse the JSON response
        EventsStructure event = parseEvent(responseBody);

        // Print the parsed event
        System.out.println(event);
    }

    public static EventsStructure parseEvent(String jsonResponse) {
        // Create an ObjectMapper instance to map the JSON response to an Event object
        ObjectMapper objectMapper = new ObjectMapper();
        EventsStructure event = null;
        try {
            // Convert JSON string to JsonNode
            JsonNode rootNode = objectMapper.readTree(jsonResponse);

            // Create an Event object and set its fields using the JsonNode
            event = new EventsStructure();
            event.setSlug(rootNode.path("slug").asText());
            event.setTitle(rootNode.path("title").asText());
            event.setCity_slug(rootNode.path("city_slug").asText());

            // Handle the price fields (converting them from String to Double)
            event.setMax_cost(Double.parseDouble(rootNode.path("max_cost").asText()));
            event.setMin_cost(Double.parseDouble(rootNode.path("min_cost").asText()));

        } catch (IOException e) {
            e.printStackTrace();
        }return event;
    }
}
