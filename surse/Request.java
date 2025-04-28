import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;

public class Request implements Subject {
    // fields
    private RequestTypes requestType;
    private LocalDateTime date;
    private String title;
    private String description;
    private String username;
    private String usernameToResolve;
    private List<Observer> observers;

    // constructor
    public Request(RequestTypes requestType, LocalDateTime date, String username, String usernameToResolve, String description, String title) {
        this.requestType = requestType;
        this.date = date;
        this.description = description;
        this.username = username;
        this.usernameToResolve = usernameToResolve;
        this.title = title;

        this.observers = new ArrayList<>();  // Inițializați lista de observatori
    }
    // getters
    public RequestTypes getRequestType() {
        return requestType;
    }
    public LocalDateTime getDate() {
        return date;
    }
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public String getUsername() {
        return username;
    }
    public String getUsernameToResolve() {
        return usernameToResolve;
    }
    // setters
    public void setRequestType(RequestTypes requestType) {
        this.requestType = requestType;
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setUsernameToResolve(String usernameToResolve) {
        this.usernameToResolve = usernameToResolve;
    }
    // methods
    public String toString(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return "Request{" +
                "requestType=" + requestType +
                ", date=" + date.format(formatter) +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", username='" + username + '\'' +
                ", usernameToResolve='" + usernameToResolve + '\'' +
                '}';
    }

    // Observer pattern
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
