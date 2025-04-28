import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

// Clasa Factory pentru crearea utilizatorilor
public class UserFactory {
    // Metoda de fabrică care creează obiecte User în funcție de tipul specificat
    public static <T extends Comparable<T>> User<T> createUser(AccountType accountType, User.Information information,
                                                               String username, int experience, List<String> notifications,
                                                               SortedSet<T> favorites, List<Request> assignedRequests, SortedSet<T> contributions) {
        switch (accountType) {
            case Regular:
                // Specifică explicit tipul pentru Regular ca fiind Rating
                return (User<T>) new Regular(information, accountType, username, experience, notifications, (SortedSet<Rating>) favorites, (ArrayList<Request>) assignedRequests);
            case Contributor:
                return new Contributor<>(information, accountType, username, experience, notifications, favorites, assignedRequests, contributions);
            case Admin:
                return new Admin<>(information, accountType, username, experience, notifications, favorites, assignedRequests, contributions);
            default:
                throw new IllegalArgumentException("Tip de cont nevalid: " + accountType);
        }
    }
}

