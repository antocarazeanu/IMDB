import java.util.List;
import java.util.stream.Collectors;
public abstract class Production implements Comparable {
    private String title;
    private List<String> directors;
    private List<String> actors;
    private List<Genre> genres;
    private List<Rating> ratings;
    private String description;
    private double rating;

    // Constructor pentru clasa Production
    public Production(String title, List<String> directors, List<String> actors, List<Genre> genres,
                      List<Rating> ratings, String description, double rating) {
        this.title = title;
        this.directors = directors;
        this.actors = actors;
        this.genres = genres;
        this.ratings = ratings;
        this.description = description;
        this.rating = rating;
    }

    // Getteri pentru atributele clasei Production
    public String getTitle() {
        return title;
    }

    public List<String> getDirectors() {
        return directors;
    }

    public List<String> getActors() {
        return actors;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public String getDescription() {
        return description;
    }

    public double getRating() {
        return rating;
    }

    //setteri
    public void setTitle(String title) {
        this.title = title;
    }
    public void setDirectors(List<String> directors) {
        this.directors = directors;
    }
    public void setActors(List<String> actors) {
        this.actors = actors;
    }
    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }
    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setRating(double rating) {
        this.rating = rating;
    }


    // Metodă pentru adaugarea unui rating
    public void addRating(Rating rating) {
        this.ratings.add(rating);
    }

    // metoda pentru stergera unui rating
    public void removeRating(Rating rating) {
        this.ratings.remove(rating);
    }

    // Metodă necesară sortării filmelor si serialelor în functie de titlu
    @Override
    public int compareTo(Object o) {
        return this.getTitle().compareTo(o.toString());
    }

}