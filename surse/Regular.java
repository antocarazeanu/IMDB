import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.SortedSet;

public class Regular extends User<Rating> implements RequestsManager {

    private ArrayList<Request> requests;

    public Regular(Information information, AccountType accountType, String username, int experience,
                   List<String> notifications, SortedSet<Rating> favorites, ArrayList<Request> requests) {
        super(information, accountType, username, experience, notifications, favorites);
        this.requests = requests;
    }

    // getters and setters
    public ArrayList<Request> getRequests() {
        return requests;
    }

    public void setRequests(ArrayList<Request> requests) {
        this.requests = requests;
    }

    // methods
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
    public void removeRequest(Request r) {
        IMDB imdb = IMDB.getInstance();
        imdb.requests.remove(r);
    }



    private boolean hasReviewedProduction(Production production) {
        // Verifica daca utilizatorul a evaluat deja aceasta productie
        return production.getRatings().stream().anyMatch(rating -> rating.getUsername().equals(this.getUsername()));
    }

    @Override
    public void update(String notification) {
        super.update(notification);
        System.out.println("Regular " + getUsername() + " received notification: " + notification);
    }
}
