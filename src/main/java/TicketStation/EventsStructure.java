package TicketStation;

public class EventsStructure {

    private String slug;
    private String title;
    private String city_slug;
    private double max_cost;
    private double min_cost;

    // Getters and Setters
    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCity_slug() {
        return city_slug;
    }

    public void setCity_slug(String city_slug) {
        this.city_slug = city_slug;
    }

    public double getMax_cost() {
        return max_cost;
    }

    public void setMax_cost(double max_cost) {
        this.max_cost = max_cost;
    }

    public double getMin_cost() {
        return min_cost;
    }

    public void setMin_cost(double min_cost) {
        this.min_cost = min_cost;
    }

    @Override
    public String toString() {
        return "EventStructure{" +
                "slug='" + slug + '\'' +
                ", title='" + title + '\'' +
                ", city_slug='" + city_slug + '\'' +
                ", max_cost=" + max_cost +
                ", min_cost=" + min_cost +
                '}';
    }
}
