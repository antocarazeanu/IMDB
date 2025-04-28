import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.SortedSet;
public class Admin<T extends Comparable<T>> extends Staff<T> {
    // Constructor
    public Admin(Information information, AccountType accountType, String username, int experience,
                 List<String> notifications, SortedSet<T> favorites, List<Request> assignedRequests,
                 SortedSet<T> contributions) {
        super(information, accountType, username, experience, notifications, favorites, assignedRequests, contributions);
    }

    // Metodele specifice interfeței RequestsManager suprascrise
    @Override
    public void addProductionSystem() {
        // Adăugare producție în sistem
        // check if it is a movie or a series
        Scanner scanner = new Scanner(System.in);
        System.out.println("What type of production do you want to add? (movie/series)");
        String type = scanner.nextLine();
        if (type.equals("movie")) {
            addMovie();
        } else if (type.equals("series")) {
            addSeries();
        } else {
            System.out.println("Invalid type of production!");
        }
    }

    public void addMovie() {
        IMDB imdb = IMDB.getInstance();
        System.out.println("Add movie details:");
        System.out.print("title: ");
        Scanner scanner = new Scanner(System.in);
        String title = scanner.nextLine();
        String type = "Movie";
        System.out.println("directors: ");
        List<String> directors = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String director = scanner.nextLine();
            if (director.equals("")) {
                break;
            }
            directors.add(director);
        }
        System.out.println("actors: ");
        List<String> actors = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String actor = scanner.nextLine();
            if (actor.equals("")) {
                break;
            }
            actors.add(actor);
        }
        System.out.println("genres: ");
        List<Genre> genres = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String genre = scanner.nextLine();
            if (genre.equals("")) {
                break;
            }
            genres.add(Genre.valueOf(genre));
        }
        System.out.println("ratings: ");
        List<Rating> ratings = new ArrayList<>();
        while (scanner.hasNextLine()) {
            System.out.print("username: ");
            String username = scanner.nextLine();
            if (username.equals("")) {
                break;
            }
            System.out.print("rating: ");
            int rating = scanner.nextInt();
            scanner.nextLine(); // Consuma newline-ul ramas in buffer
            System.out.print("comment: ");
            String comment = scanner.nextLine();
            ratings.add(new Rating(username, rating, comment, AccountType.Regular));
        }
        System.out.println("description: ");
        String description = scanner.nextLine();
        System.out.println("rating: ");
        double rating = scanner.nextDouble();
        scanner.nextLine(); // Consuma newline-ul ramas in buffer
        System.out.println("duration: ");
        String duration = scanner.nextLine();
        System.out.println("year: ");
        int year = scanner.nextInt();
        scanner.nextLine(); // Consuma newline-ul ramas in buffer
        Movie movie = new Movie(title, directors, actors, genres, ratings, description, rating, duration, year);
        imdb.movies.add(movie);
    }

    public void addSeries() {
        IMDB imdb = IMDB.getInstance();
        System.out.println("Add series details:");
        System.out.print("title: ");
        Scanner scanner = new Scanner(System.in);
        String title = scanner.nextLine();
        String type = "Series";
        System.out.println("directors: ");
        List<String> directors = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String director = scanner.nextLine();
            if (director.equals("")) {
                break;
            }
            directors.add(director);
        }
        System.out.println("actors: ");
        List<String> actors = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String actor = scanner.nextLine();
            if (actor.equals("")) {
                break;
            }
            actors.add(actor);
        }
        System.out.println("genres: ");
        List<Genre> genres = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String genre = scanner.nextLine();
            if (genre.equals("")) {
                break;
            }
            genres.add(Genre.valueOf(genre));
        }
        System.out.println("ratings: ");
        List<Rating> ratings = new ArrayList<>();
        while (scanner.hasNextLine()) {
            System.out.print("username: ");
            String username = scanner.nextLine();
            if (username.equals("")) {
                break;
            }
            System.out.print("rating: ");
            int rating = scanner.nextInt();
            scanner.nextLine(); // Consuma newline-ul ramas in buffer
            System.out.print("comment: ");
            String comment = scanner.nextLine();
            ratings.add(new Rating(username, rating, comment, AccountType.Regular));
        }
        System.out.println("description: ");
        String description = scanner.nextLine();
        System.out.println("rating: ");
        double rating = scanner.nextDouble();
        scanner.nextLine(); // Consuma newline-ul ramas in buffer
        System.out.println("duration: ");
        String duration = scanner.nextLine();
        System.out.println("year: ");
        int year = scanner.nextInt();
        scanner.nextLine(); // Consuma newline-ul ramas in buffer
    }


    @Override
    public void addActorSystem() {
        // Adăugare actor în sistem
        IMDB imdb = IMDB.getInstance();
        System.out.println("Add actor details:");
        System.out.print("name: ");
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        System.out.println("Perfomances: ");
        List<Performance> performances = new ArrayList<>();
        while (scanner.hasNextLine()) {
            System.out.print("Title: ");
            String performance = scanner.nextLine();
            if (performance.equals("")) {
                break;
            }
            System.out.print("Type: ");
            String type = scanner.nextLine();
            performances.add(new Performance(performance, type));
        }
        System.out.println("Biography: ");
        String biography = scanner.nextLine();

        Actor actor = new Actor(name, performances, biography, null);
        imdb.actors.add(actor);
    }

    @Override
    public void removeProductionSystem(Production production) {
        // Ștergere producție din sistem
        IMDB imdb = IMDB.getInstance();
        if (production instanceof Movie) {
            imdb.movies.remove(production);
        } else if (production instanceof Series) {
            imdb.series.remove(production);
        }
    }

    @Override
    public void removeActorSystem(Actor actor) {
        // Ștergere actor din sistem
        IMDB imdb = IMDB.getInstance();
        imdb.actors.remove(actor);
    }

    @Override
    public void updateProduction(Production p) {
        // Actualizare detalii producție
        Scanner scanner = new Scanner(System.in);
        System.out.println("What type of production do you want to update? (movie/series)");
        String type = scanner.nextLine();
        if (type.equals("movie")) {
            updateMovie((Movie) p);
        } else if (type.equals("series")) {
            updateSeries((Series) p);
        } else {
            System.out.println("Invalid type of production!");
        }
    }

    public void updateMovie(Movie p){
        Scanner scanner = new Scanner(System.in);
        System.out.println("What do you want to update? (title/directors/actors/genres/ratings/description/rating/duration/year)");
        String field = scanner.nextLine();
        // field can be : title, directors, actors, genres, ratings, description, rating, duration, year
        if (field.equals("title")) {
            System.out.println("Enter new title: ");
            String newTitle = scanner.nextLine();
            p.setTitle(newTitle);
        } else if (field.equals("directors")) {
            System.out.println("Enter new directors: ");
            List<String> newDirectors = new ArrayList<>();
            while (scanner.hasNextLine()) {
                String director = scanner.nextLine();
                if (director.equals("")) {
                    break;
                }
                newDirectors.add(director);
            }
            p.setDirectors(newDirectors);
        } else if (field.equals("actors")) {
            System.out.println("Enter new actors: ");
            List<String> newActors = new ArrayList<>();
            while (scanner.hasNextLine()) {
                String actor = scanner.nextLine();
                if (actor.equals("")) {
                    break;
                }
                newActors.add(actor);
            }
            p.setActors(newActors);
        } else if (field.equals("genres")) {
            System.out.println("Enter new genres: ");
            List<Genre> newGenres = new ArrayList<>();
            while (scanner.hasNextLine()) {
                String genre = scanner.nextLine();
                if (genre.equals("")) {
                    break;
                }
                newGenres.add(Genre.valueOf(genre));
            }
            p.setGenres(newGenres);
        } else if (field.equals("ratings")) {
            System.out.println("Enter new ratings: ");
            List<Rating> newRatings = new ArrayList<>();
            while (scanner.hasNextLine()) {
                System.out.print("username: ");
                String username = scanner.nextLine();
                if (username.equals("")) {
                    break;
                }
                System.out.print("rating: ");
                int rating = scanner.nextInt();
                scanner.nextLine(); // Consuma newline-ul ramas in buffer
                System.out.print("comment: ");
                String comment = scanner.nextLine();
                newRatings.add(new Rating(username, rating, comment, IMDB.getInstance().searchUser(username).getAccountType()));
            }
            p.setRatings(newRatings);
        } else if (field.equals("description")) {
            System.out.println("Enter new description: ");
            String newDescription = scanner.nextLine();
            p.setDescription(newDescription);
        } else if (field.equals("rating")) {
            System.out.println("Enter new rating: ");
            double newRating = scanner.nextDouble();
            scanner.nextLine(); // Consuma newline-ul ramas in buffer
            p.setRating(newRating);
        } else if (field.equals("duration")) {
            System.out.println("Enter new duration: ");
            String newDuration = scanner.nextLine();
            p.setDuration(newDuration);
        } else if (field.equals("year")) {
            System.out.println("Enter new year: ");
            int newYear = scanner.nextInt();
            scanner.nextLine(); // Consuma newline-ul ramas in buffer
            p.setYear(newYear);
        } else {
            System.out.println("Invalid field!");
        }
    }

    public void updateSeries(Series p) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("What do you want to update? (title/directors/actors/genres/ratings/description/rating/duration/year)");
        String field = scanner.nextLine();
        // field can be : title, directors, actors, genres, ratings, description, rating, duration, year
        if (field.equals("title")) {
            System.out.println("Enter new title: ");
            String newTitle = scanner.nextLine();
            p.setTitle(newTitle);
        } else if (field.equals("directors")) {
            System.out.println("Enter new directors: ");
            List<String> newDirectors = new ArrayList<>();
            while (scanner.hasNextLine()) {
                String director = scanner.nextLine();
                if (director.equals("")) {
                    break;
                }
                newDirectors.add(director);
            }
            p.setDirectors(newDirectors);
        } else if (field.equals("actors")) {
            System.out.println("Enter new actors: ");
            List<String> newActors = new ArrayList<>();
            while (scanner.hasNextLine()) {
                String actor = scanner.nextLine();
                if (actor.equals("")) {
                    break;
                }
                newActors.add(actor);
            }
            p.setActors(newActors);
        } else if (field.equals("genres")) {
            System.out.println("Enter new genres: ");
            List<Genre> newGenres = new ArrayList<>();
            while (scanner.hasNextLine()) {
                String genre = scanner.nextLine();
                if (genre.equals("")) {
                    break;
                }
                newGenres.add(Genre.valueOf(genre));
            }
            p.setGenres(newGenres);
        } else if (field.equals("ratings")) {
            System.out.println("Enter new ratings: ");
            List<Rating> newRatings = new ArrayList<>();
            while (scanner.hasNextLine()) {
                System.out.print("username: ");
                String username = scanner.nextLine();
                if (username.equals("")) {
                    break;
                }
                System.out.print("rating: ");
                int rating = scanner.nextInt();
                scanner.nextLine(); // Consuma newline-ul ramas in buffer
                System.out.print("comment: ");
                String comment = scanner.nextLine();
                newRatings.add(new Rating(username, rating, comment, IMDB.getInstance().searchUser(username).getAccountType()));
            }
            p.setRatings(newRatings);
        } else if (field.equals("description")) {
            System.out.println("Enter new description: ");
            String newDescription = scanner.nextLine();
            p.setDescription(newDescription);
        } else if (field.equals("rating")) {
            System.out.println("Enter new rating: ");
            double newRating = scanner.nextDouble();
            scanner.nextLine(); // Consuma newline-ul ramas in buffer
            p.setRating(newRating);
        } else {
            System.out.println("Invalid field!");
        }
    }

    @Override
    public void updateActor(Actor actor) {
        // Actualizare detalii actor
        Scanner scanner = new Scanner(System.in);
        System.out.println("What do you want to update? (name/biography/performances)");
        String field = scanner.nextLine();
        // field can be : name, biography, performances(list of title and type of production)
        if (field.equals("name")) {
            System.out.println("Enter new name: ");
            String newName = scanner.nextLine();
            actor.setNume(newName);
        } else if (field.equals("biography")) {
            System.out.println("Enter new biography: ");
            String newBiography = scanner.nextLine();
            actor.setBiografie(newBiography);
        } else if (field.equals("performances")) {
            System.out.println("Enter new performances: ");
            List<Performance> newPerformances = new ArrayList<>();
            while (scanner.hasNextLine()) {
                System.out.println("Enter title: ");
                String title = scanner.nextLine();
                if (title.equals("")) {
                    break;
                }
                System.out.println("Enter type of production: ");
                String type = scanner.nextLine();
                newPerformances.add(new Performance(title, type));
            }
            actor.setPerformances(newPerformances);
        } else {
            System.out.println("Invalid field!");
        }
    }

    @Override
    public void resolveRequest(Request request) {
        // Rezolvarea cererilor primite
        IMDB imdb = IMDB.getInstance();
        if (request.getRequestType() == RequestTypes.DELETE_ACCOUNT) {
            User user = imdb.searchUser(request.getUsername());
            imdb.deleteUser(user);

            // notify user
            String notificationMessage = "Request " + request.getTitle() + " has been solved!";
            request.notifyObservers(notificationMessage);
            System.out.println("Account deleted!");
        } else if (request.getRequestType() == RequestTypes.OTHERS || request.getRequestType() == RequestTypes.MOVIE_ISSUE || request.getRequestType() == RequestTypes.ACTOR_ISSUE) {
            System.out.println(request.getDescription());
            User user = imdb.searchUser(request.getUsername());

            // add notification to user
            request.registerObserver(user);
            String notificationMessage = "Request " + request.getTitle() + " has been solved!";
            request.notifyObservers(notificationMessage);
            //System.out.println(user.getNotifications());
            System.out.println("Your request has been resolved!");
            request.removeObserver(user);

            // remove request from list and from system
            RequestsHolder.deleteRequest(request);
            imdb.requests.remove(request);
        } else {
            throw new IllegalArgumentException("Invalid request type!");
        }
    }


    @Override
    public void update(String notification) {
        super.update(notification);
        System.out.println("Admin " + getUsername() + " received notification: " + notification);
    }
}
