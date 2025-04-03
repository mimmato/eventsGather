package BiletBG;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ResponseWrapper {
    @SerializedName("current_page")
    private int currentPage;

    @SerializedName("data")
    List<EventsStructure> data;

    @SerializedName("links")
    private Links links;

    public String getNextPageUrl() {
        return (links != null) ? links.next : null;
    }
}

class Links {
    @SerializedName("next")
    String next;
}
