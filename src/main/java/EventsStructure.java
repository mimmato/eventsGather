import com.google.gson.annotations.SerializedName;

public class EventsStructure_beforeUI {
    @SerializedName("name")
    private String name;

    @SerializedName("start_date")
    private String date;

    @SerializedName("finished")
    private boolean finished;

    @SerializedName("m_description")
    private String description;

    @SerializedName("slug")
    private String slug;

    public String getSlug() {
        return slug;
    }

    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public boolean isFinished() {
        return finished;
    }
}
