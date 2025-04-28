import java.util.ArrayList;
import java.util.List;

public class Actor implements Comparable {
    private String nume;
    private List<Performance> performances;
    private String biografie;
    private List<Rating> ratings;
    private double avgrating;

    public Actor(String nume, List<Performance> performances, String biografie, List<Rating> ratings) {
        this.nume = nume;
        this.performances = performances;
        this.biografie = biografie;
        this.ratings = new ArrayList<>();
    }

    // getteri si setteri pentru fiecare camp
    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public List<Performance> getPerformances() {
        return performances;
    }

    public void setPerformances(List<Performance> performances) {
        this.performances = performances;
    }

    public String getBiografie() {
        return biografie;
    }

    public void setBiografie(String biografie) {
        this.biografie = biografie;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

    // Metodă pentru adaugarea unui rating
    public void addRating(Rating rating) {
        this.ratings.add(rating);
    }

    // metoda pentru stergera unui rating
    public void removeRating(Rating rating) {
        this.ratings.remove(rating);
    }

    public void setAvgRating() {
        double sum = 0;
        for (Rating rating : ratings) {
            sum += rating.getRating();
        }
        if (ratings.size() == 0) {
            this.avgrating = 0;
        } else {
            this.avgrating = sum / ratings.size();
        }
    }

    public double getAvgRating() {
        return avgrating;
    }

    //method compareto
    @Override
    public int compareTo(Object o) {
        return this.nume.compareTo(o.toString());
    }
}
