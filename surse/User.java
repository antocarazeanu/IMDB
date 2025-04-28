import java.security.spec.ECField;
import java.time.LocalDateTime;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.ArrayList;

public abstract class User<T extends Comparable<T>> implements Comparable<User<T>>, Observer {
    // fields
    private Information information;
    private AccountType accountType;
    private String username;
    private int experience;
    private List<String> notifications;
    // SortedSet<T> (Actor or Production)
    private SortedSet<T> favorite;

    private ExperienceStrategy experienceStrategy;

    // Clasa Information internă clasei User
    public static class Information {
        private Credentials credentials;
        private String name;
        private String country;
        private int age;
        private String gender;
        private LocalDateTime birthDate;
        // constructor
        public Information(Credentials credentials, String name, String country, int age, String gender, LocalDateTime date){
            this.credentials = credentials;
            this.name = name;
            this.country = country;
            this.age = age;
            this.gender = gender;
            this.birthDate = date;

        }

        // getters
        public Credentials getCredentials() {
            return this.credentials;
        }
        public String getName() {
            return this.name;
        }
        public String getCountry() {
            return this.country;
        }
        public int getAge() {
            return this.age;
        }
        public String getGender(){return  this.gender;}
        public LocalDateTime getBirthDate(){return this.birthDate;}
        // setters
        public void setCredentials(Credentials credentials) {
            this.credentials = credentials;
        }
        public void setName(String name) {
            this.name = name;
        }
        public void setCountry(String country) {
            this.country = country;
        }
        public void setAge(int age) {
            this.age = age;
        }
    }

    // methods
    public void addFavorite(T item) {
        if (item instanceof Actor) {
            favorite.add(item);
            System.out.println("Actor adăugat cu succes la favorite.");
        } else if (item instanceof Production) {
            favorite.add(item);
            System.out.println("Producție adăugată cu succes la favorite.");
        } else if (item instanceof String) {
            IMDB imdb = IMDB.getInstance();
            if(imdb.searchActor((String) item) != null){
                Actor actor = imdb.searchActor((String) item);
                favorite.add((T) actor);
            }
            else if(imdb.searchMovie((String) item) != null){
                Production production = imdb.searchMovie((String) item);
                favorite.add((T) production);
            }
            else if (imdb.searchSeries((String) item) != null){
                Production production = imdb.searchSeries((String) item);
                favorite.add((T) production);
            }
            else{
                favorite.add(item);
            }

        } else {
            // Handle other types or throw an exception if needed
            throw new IllegalArgumentException("Unsupported type");
        }
    }
    // method to print favorites:
    public void printFavorites() {
        System.out.println("Favorites:");

        for (T item : favorite) {
            if (item instanceof Actor) {
                System.out.println(((Actor) item).getNume());
            } else if (item instanceof Production) {
                System.out.println(((Production) item).getTitle());
            }
            else if (item instanceof String) {
                System.out.println(item);
            }
        }
    }

    public void deleteFavorite(T item){
        if (item instanceof Actor) {
            favorite.remove(item);
        } else if (item instanceof Production) {
            favorite.remove(item);
        } else if (item instanceof String) {
            favorite.remove(item);
        }
        else {
            // Handle other types or throw an exception if needed
            throw new IllegalArgumentException("Unsupported type");
        }
    }
    public boolean searchFavorites(String item){
        for (T i : favorite) {
            if (i instanceof Actor) {
                if(((Actor) i).getNume().equals(item)){
                    System.out.println("Actorul " + item + " se afla in lista de favorite");
                    return true;
                }

            } else if (i instanceof Production) {
                if(((Production) i).getTitle().equals(item)){
                    System.out.println("Productia " + item + " se afla in lista de favorite");
                    return true;
                }

            }
            else if (i instanceof String) {
                if(i.equals(item)){
                    System.out.println(item + " se afla in lista de favorite");
                    return true;
                }
            }
        }
        return false;
    }

    public void setExperienceStrategy(ExperienceStrategy experienceStrategy) {
        this.experienceStrategy = experienceStrategy;
    }
    public void updateExperience(int experience){
        setExperience(experienceStrategy.calculateExperience(experience));
    }

    // constructor
    public User(Information information, AccountType accountType, String username, int experience, List<String> notifications, SortedSet<T> favorite) {
        this.information = information;
        this.accountType = accountType;
        this.username = username;
        this.experience = experience;
        if (notifications == null) {
            this.notifications = new ArrayList<>();
        } else {
            this.notifications = notifications;
        }
        this.favorite = new TreeSet<>();
    }
    // getters
    public Information getInformation() {
        return information;
    }
    public AccountType getAccountType() {
        return this.accountType;
    }
    public String getUsername() {
        return username;
    }
    public int getExperience() {
        return experience;
    }
    public List<String> getNotifications() {
        return notifications;
    }
    public SortedSet<T> getFavorites() {
        return favorite;
    }

    // setters
    public void setInformation(Information information) {
        this.information = information;
    }
    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setExperience(int experience) {
        this.experience = experience;
    }
    public void setNotifications(List<String> notifications) {
        this.notifications = notifications;
    }
    public void setFavorites(SortedSet<T> favorites) {
        this.favorite = favorites;
    }

    public User logout(){
        IMDB imdb = IMDB.getInstance();
        return imdb.authenticateUser();
    }
    @Override
    public int compareTo(User<T> o) {
        // Implementația comparației
        return Integer.compare(this.experience, o.experience);
    }

    @Override
    public void update(String notification) {
        notifications.add(notification);
        //System.out.println("User " + getUsername() + " received notification: " + notification);
    }

}
