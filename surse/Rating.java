import java.util.List;
import java.util.ArrayList;

public class Rating implements Comparable<Rating>, Subject {
    // fields
    String username;
    int rating;
    String feedback;
    private List<Observer> observers;

    // constructor
    public Rating(String username, int rating, String feedback, AccountType userType) {
        // Verifică tipul utilizatorului înainte de a permite evaluarea
        if (userType == AccountType.Regular) {
            this.username = username;
            this.rating = rating;
            this.feedback = feedback;
        } else {
            // exceptie
            throw new IllegalArgumentException("Only Regular users can rate movies and shows.");
        }

        this.observers = new ArrayList<>();  // Inițializați lista de observatori
    }


    // getters
    public String getUsername() {
        return username;
    }
    public int getRating() {
        return rating;
    }
    public String getFeedback() {
        return feedback;
    }
    // setters
    public void setUsername(String username) {
        this.username = username;
    }
    public void setRating(int rating) {
        this.rating = rating;
    }
    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    // toString
    @Override
    public String toString() {
        return "Rating{" +
                "username='" + username + '\'' +
                ", rating=" + rating +
                ", feedback='" + feedback + '\'' +
                '}';
    }

    // methods
    @Override
    public int compareTo(Rating o) {
        return this.username.compareTo(o.username);
    }

    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }
    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }
    @Override
    public void notifyObservers(String notification) {
        for (Observer observer : observers) {
            observer.update(notification);
        }
    }
}
