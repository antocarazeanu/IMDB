import java.util.List;
public class Movie extends Production {
    private String duration;
    private int year;

    // Constructor pentru clasa Movie
    public Movie(String title, List<String> directors, List<String> actors, List<Genre> genres,
                 List<Rating> ratings, String description, double rating, String duration, int year) {
        super(title, directors, actors, genres, ratings, description, rating);
        this.duration = duration;
        this.year = year;
    }

    // Getteri pentru atributele specifice ale clasei Movie
    public String getDuration() {
        return duration;
    }

    public int getYear() {
        return year;
    }

    // setters
    public void setDuration(String duration) {
        this.duration = duration;
    }
    public void setYear(int year) {
        this.year = year;
    }
}
