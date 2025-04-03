import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class EpayGo {

    public static void main(String[] args) {
        try {
            // Step 1: Connect to the page and get the HTML document
            String url = "https://epaygo.bg/epaygo/concerts-and-festivals";
            Document doc = Jsoup.connect(url).get();

            System.out.println("Page source found: " + url);
            // Step 2: Find the script tag containing the event data
            Element script = doc.selectFirst("script:containsData(events_page_tt)");

            if (script != null) {
                // Step 3: Extract the JSON array from the script content
                String scriptData = script.data();  // Get the content of the script tag
                String jsonData = scriptData.substring(scriptData.indexOf('['), scriptData.lastIndexOf(']') + 1);

                // Step 4: Parse the JSON data
                JSONArray eventsArray = new JSONArray(jsonData);

                System.out.println("Total Events Found: " + eventsArray.length());
                System.out.println("--------------------");

                // Step 5: Loop through each event and extract relevant details
                for (int i = 0; i < eventsArray.length(); i++) {
                    JSONObject event = eventsArray.getJSONObject(i);

                    String eventName = event.getString("SALE.NAME");
                    String eventDateTime = event.getString("SALE.EVENT_DATE");
                    String eventPlace = event.getString("SALE.EVENT_PLACE");

                    // Output event details
                    System.out.println("Event Name: " + eventName);
                    System.out.println("Event Date and Time: " + eventDateTime);
                    System.out.println("Event Place: " + eventPlace);
                    System.out.println("--------------------");
                }
            } else {
                System.out.println("No event data found in the script.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
