import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;
import java.util.SortedSet;

public class Contributor<T extends Comparable<T>> extends Staff<T> implements RequestsManager {

    public Contributor(Information information, AccountType accountType, String username, int experience,
                       List<String> notifications, SortedSet<T> favorites, List<Request> assignedRequests,
                       SortedSet<T> contributions) {
        super(information, accountType, username, experience, notifications, favorites, assignedRequests, contributions);
    }

    // getteri si setteri pentru fiecare camp
    @Override
    public String toString() {
        return "Contributor{" +
                "information=" + getInformation() +
                ", accountType=" + getAccountType() +
                ", username='" + getUsername() + '\'' +
                ", experience=" + getExperience() +
                ", notifications=" + getNotifications() +
                ", favorites=" + getFavorites() +
                ", assignedRequests=" + getAssignedRequests() +
                '}';
    }

    // Metodele specifice interfeței RequestsManager
    @Override
    public void removeRequest(Request request) {
        IMDB imdb = IMDB.getInstance();
        imdb.requests.remove(request);
    }

    @Override
    public void createRequest() {
        System.out.println("Add request details:");
        System.out.print("type: ");
        Scanner scanner = new Scanner(System.in);
        RequestTypes type = RequestTypes.valueOf(scanner.nextLine());
        System.out.print("username: ");
        String username = scanner.nextLine();
        System.out.print("to: ");
        String to = scanner.nextLine();
        System.out.print("description: ");
        String description = scanner.nextLine();
        if (type == RequestTypes.ACTOR_ISSUE || type == RequestTypes.MOVIE_ISSUE) {
            System.out.print("title: ");
            String title = scanner.nextLine();
            Request request = new Request(type, LocalDateTime.now(), username, to, description, title);
            IMDB imdb = IMDB.getInstance();
            imdb.requests.add(request);
        } else {
            Request request = new Request(type, LocalDateTime.now(), username, to, description, null);
            IMDB imdb = IMDB.getInstance();
            imdb.requests.add(request);
        }
    }

    @Override
    public void update(String notification) {
        super.update(notification);
        System.out.println("Contributor " + getUsername() + " received notification: " + notification);
    }
}
