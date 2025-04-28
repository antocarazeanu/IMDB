import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;

public class IMDB {
    // Variabila statică unică pentru instanța Singleton
    private static IMDB instance;

    // Atribuții ale clasei IMDB
    public List<User<?>> users;
    public List<Actor> actors;
    public List<Request> requests;
    public List<Movie> movies;
    public List<Series> series;

    // Constructor privat pentru a preveni instanțierea directă
    private IMDB() {
        users = new ArrayList<>();
        actors = new ArrayList<>();
        requests = new ArrayList<>();
        movies = new ArrayList<>();
        series = new ArrayList<>();
    }

    // Metoda statică pentru a obține instanța Singleton
    public static synchronized IMDB getInstance() {
        if (instance == null) {
            instance = new IMDB();
        }
        return instance;
    }
    public void run() {
        // Încărcați toate datele parsate din fișierele JSON
        loadAccountsFromJson("D:\\IntelliJ IDEA Community Edition 2023.2.2\\TemaPOO2023\\POO-Tema-2023-checker\\POO-Tema-2023-checker\\src\\main\\resources\\input\\accounts.json");
        loadActorsFromJson("D:\\IntelliJ IDEA Community Edition 2023.2.2\\TemaPOO2023\\POO-Tema-2023-checker\\POO-Tema-2023-checker\\src\\test\\resources\\testResources\\actors.json");
        loadProductionFromJson("D:\\IntelliJ IDEA Community Edition 2023.2.2\\TemaPOO2023\\POO-Tema-2023-checker\\POO-Tema-2023-checker\\src\\test\\resources\\testResources\\production.json");
        loadRequestsFromJson("D:\\IntelliJ IDEA Community Edition 2023.2.2\\TemaPOO2023\\POO-Tema-2023-checker\\POO-Tema-2023-checker\\src\\test\\resources\\testResources\\requests.json");
    }
    private void loadAccountsFromJson(String fileName) {
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader(fileName)) {
            // Parse the JSON file
            Object obj = jsonParser.parse(reader);
            JSONArray accountsList = (JSONArray) obj;
            // Iterate over production array
            accountsList.forEach(account -> parseAccountsObject((JSONObject) account));
        } catch (IOException e) {
            System.out.println("Error: file could not open " + fileName);
            e.printStackTrace();
        } catch (ParseException e) {
            System.out.println("Error: Data from " + fileName + " are not valid");
            e.printStackTrace();
        }
    }
    private void parseAccountsObject(JSONObject account) {
        String username = (String) account.get("username");

        // Parse "experience" as Long and then convert to int
        Object experienceObj = account.get("experience");
        int experience = 0;  // default value
        if (experienceObj instanceof Long) {
            experience = ((Long) experienceObj).intValue();
        } else if (experienceObj instanceof String) {
            experience = Integer.parseInt((String) experienceObj);
        }

        // Parsare și construire obiect Information
        JSONObject informationJson = (JSONObject) account.get("information");
        JSONObject credentialsJson = (JSONObject) informationJson.get("credentials");
        String email = (String) credentialsJson.get("email");
        String password = (String) credentialsJson.get("password");
        Credentials credentials = new Credentials(email, password);
        int age = ((Long) informationJson.get("age")).intValue();
        String name = (String) informationJson.get("name");
        String country = (String) informationJson.get("country");
        String gender = (String) informationJson.get("gender");
        LocalDateTime birthDate = LocalDate.parse((String) informationJson.get("birthDate"), DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay();

        // creare obiect Information
        InformationBuilder builder = new InformationBuilder();
        builder.setName(name);
        builder.setAge(age);
        builder.setBirthDate(birthDate);
        builder.setCountry(country);
        builder.setCredentials(credentials);
        builder.setGender(gender);
        User.Information information = builder.build();

        String userType = (String) account.get("userType");

        AccountType accountType = AccountType.valueOf(userType);
        // System.out.println(email + " " + accountType);

        if (accountType == AccountType.Regular) {
            // parsare list de notificari si verificare daca este null
            List<String> notifications = new ArrayList<>();
            JSONArray notificationsJson = (JSONArray) account.get("notifications");
            if (notificationsJson != null) {
                for (Object notificationObj : notificationsJson) {
                    notifications.add((String) notificationObj);
                }
            }

            User currentUser = UserFactory.createUser(accountType, information, username, experience, notifications, null, null, null);

            JSONArray favoriteActorsJson = (JSONArray) account.get("favoriteActors");
            if (favoriteActorsJson != null) {
                for (Object favoriteActorObj : favoriteActorsJson) {
                    currentUser.addFavorite((String) favoriteActorObj);
                }
            }
            JSONArray favoriteProductionsJson = (JSONArray) account.get("favoriteProductions");
            if (favoriteProductionsJson != null) {
                for (Object favoriteProductionObj : favoriteProductionsJson) {
                    currentUser.addFavorite((String) favoriteProductionObj);
                }
            }

            this.users.add(currentUser);
        }
        else {
            // parsare list de notificari si verificare daca este null
            List<String> notifications = new ArrayList<>();
            JSONArray notificationsJson = (JSONArray) account.get("notifications");
            if (notificationsJson != null) {
                for (Object notificationObj : notificationsJson) {
                    notifications.add((String) notificationObj);
                }
            }

            User currentUser = UserFactory.createUser(accountType, information, username, experience, notifications, null, null, null);
            //parsare lista actori favoriti care sunt in fisierul json salvati ca favoriteActors si sunt Stringuri
            // producțiile sunt in fisierul json salvate ca favoriteProductions si sunt obiecte de tip String
            // in user sunt de tip SortedSet<T> unde T poate fi Actor sau Production
            JSONArray favoriteActorsJson = (JSONArray) account.get("favoriteActors");
            if (favoriteActorsJson != null) {
                for (Object favoriteActorObj : favoriteActorsJson) {
                    currentUser.addFavorite((String) favoriteActorObj);
                }
            }
            JSONArray favoriteProductionsJson = (JSONArray) account.get("favoriteProductions");
            if (favoriteProductionsJson != null) {
                for (Object favoriteProductionObj : favoriteProductionsJson) {
                    currentUser.addFavorite((String) favoriteProductionObj);
                }
            }

            // check if the user is a staff member by looking for actorcontributions or productioncontributions
            JSONArray actorContributionsJson = (JSONArray) account.get("actorsContribution");
            JSONArray productionContributionsJson = (JSONArray) account.get("productionsContribution");
            if (actorContributionsJson != null) {
                for (Object actorContributionObj : actorContributionsJson) {
                    //adaugare current user in staff class
                    Staff staffmember = (Staff) currentUser;
                    staffmember.addContribution((String) actorContributionObj);
                }
            }
            if (productionContributionsJson != null) {
                for (Object productionContributionObj : productionContributionsJson) {
                    //adaugare current user in staff class
                    Staff staffmember = (Staff) currentUser;
                    staffmember.addContribution((String) productionContributionObj);
                }
            }

            this.users.add(currentUser);
        }
    }

    public void viewNotifications(String username){
        for (User<?> user :this.users) {
            if(user.getUsername().equals(username)){
                System.out.println("Notifications: " + user.getNotifications());
            }
        }
    }
    public void printAccounts() {
        for (User<?> user : this.users) {
            System.out.println("Username: " + user.getUsername());
            System.out.println("Experience: " + user.getExperience());
            System.out.println("Email: " + user.getInformation().getCredentials().getEmail());
            System.out.println("Password: " + user.getInformation().getCredentials().getPassword());
            System.out.println("Age: " + user.getInformation().getAge());
            System.out.println("Name: " + user.getInformation().getName());
            System.out.println("Country: " + user.getInformation().getCountry());
            System.out.println("Gender: " + user.getInformation().getGender());
            System.out.println("Birth Date: " + user.getInformation().getBirthDate());
            System.out.println("User Type: " + user.getAccountType());

            System.out.println("Notifications: " + String.join(", ", user.getNotifications()));
            System.out.println("Favorite Actors/Productions: " + user.getFavorites());

            // Check if the user is an instance of Staff
            if (user instanceof Staff) {
                Staff staffMember = (Staff) user;
                System.out.println("Contributions: " + staffMember.getContributions());
            }

            System.out.println();
        }
    }



    private void loadActorsFromJson(String fileName) {
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader(fileName)) {
            // Parse the JSON file
            Object obj = jsonParser.parse(reader);
            JSONArray actorList = (JSONArray) obj;

            // Iterate over actor array
            actorList.forEach(act -> parseActorObject((JSONObject) act));

        } catch (IOException e) {
            System.out.println("Error: File could not open" + fileName);
            e.printStackTrace();
        } catch (ParseException e) {
            System.out.println("Error: Data from " + fileName + " are not valid");
            e.printStackTrace();
        }
    }


    private void parseActorObject(JSONObject actor) {
        // Get actor name
        String name = (String) actor.get("name");

        // Get actor biography
        String biography = (String) actor.get("biography");

        // Get actor performances
        JSONArray performancesJson = (JSONArray) actor.get("performances");
        List<Performance> performances = new ArrayList<>();
        for (Object performanceObj : performancesJson) {
            JSONObject performanceJson = (JSONObject) performanceObj;
            String title = (String) performanceJson.get("title");
            String type = (String) performanceJson.get("type");
            performances.add(new Performance(title, type));
        }

        // Create Actor object
        Actor actorObj = new Actor(name, performances, biography, null);

        // Add actor to list
        this.actors.add(actorObj);
    }

    public Actor searchActor(String actorName) {
        for (Actor actor : this.actors) {
            if (actor.getNume().equals(actorName)) {
                System.out.println("Name: " + actor.getNume());
                System.out.println("Biography: " + actor.getBiografie());
                System.out.println("Performance:");
                for (Performance performance : actor.getPerformances()) {
                    System.out.println("    Title: " + performance.getTitle());
                    System.out.println("    Type: " + performance.getType());
                }
                System.out.println();
                return actor;
            }
        }
        return null;
    }

    // implement a sort method for actors by name
    public void sortbyName() {
        this.actors.sort((actor1, actor2) -> actor1.getNume().compareTo(actor2.getNume()));
    }
    public void printActors() {
        for (Actor actor : this.actors) {
            System.out.println("Name: " + actor.getNume());
            System.out.println("Biography: " + actor.getBiografie());
            System.out.println("Performance:");
            for (Performance performance : actor.getPerformances()) {
                System.out.println("    Title: " + performance.getTitle());
                System.out.println("    Type: " + performance.getType());
            }
            System.out.println();
        }
    }

    public void loadProductionFromJson(String fileName) {
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader(fileName)) {
            // Parse the JSON file
            Object obj = jsonParser.parse(reader);
            JSONArray productionList = (JSONArray) obj;
            // Iterate over production array
            productionList.forEach(prod -> parseProductionObject((JSONObject) prod));
        } catch (IOException e) {
            System.out.println("Error: File could not open " + fileName);
            e.printStackTrace();
        } catch (ParseException e) {
            System.out.println("Error: Data from file " + fileName + " are not valid");
            e.printStackTrace();
        }
    }
    private void parseProductionObject(JSONObject production) {
        String title = (String) production.get("title");
        String type = (String) production.get("type");
        List<String> directors = (List<String>) production.get("directors");
        List<String> actors = (List<String>) production.get("actors");
        JSONArray genresJson = (JSONArray) production.get("genres");
        List<Genre> genres = new ArrayList<>();
        for (Object genreObj : genresJson) {
            genres.add(Genre.valueOf((String) genreObj));
        }
        JSONArray ratings = (JSONArray) production.get("ratings");
        // get username, rating, comment for every rating there is for this production
        List<Rating> ratingsList = new ArrayList<>();
        for (Object ratingObj : ratings) {
            JSONObject ratingJson = (JSONObject) ratingObj;
            String username = (String) ratingJson.get("username");
            Long ratingValue = (Long) ratingJson.get("rating");
            int rating = ratingValue != null ? ratingValue.intValue() : 0;
            String comment = (String) ratingJson.get("comment");

            Rating ratingfin = new Rating(username, rating, comment, AccountType.Regular);
            ratingsList.add(ratingfin);
        }

        String description = (String) production.get("plot");
        Double ratingValue = (Double) production.get("averageRating");
        double rating = ratingValue != null ? ratingValue.doubleValue() : 0.0;

        if (type.equals("Movie")) {
            String durationStr = (String) production.get("duration");
            String duration = durationStr != null ? durationStr : "";
            Long yearLong = (Long) production.get("releaseYear");
            int year = yearLong != null ? yearLong.intValue() : 0;
            Movie movie = new Movie(title, directors, actors, genres, ratingsList, description, rating, duration, year);
            movies.add(movie);
        } else if (type.equals("Series")) {
            Long releaseYearLong = (Long) production.get("releaseYear");
            int releaseYear = releaseYearLong != null ? releaseYearLong.intValue() : 0;
            Long numberOfSeasonsLong = (Long) production.get("numSeasons");
            int numberOfSeasons = numberOfSeasonsLong != null ? numberOfSeasonsLong.intValue() : 0;
            Map<String, List<Episode>> seasons = (Map<String, List<Episode>>) production.get("seasons");
            Series serie = new Series(title, directors, actors, genres, ratingsList, description, rating, releaseYear, numberOfSeasons, seasons);
            series.add(serie);
        }
    }

    public Movie searchMovie(String moviename) {
        for (Movie production : this.movies) {
            if (production.getTitle().equals(moviename)) {
                System.out.println("Title: " + production.getTitle());
                System.out.println("Directors: " + String.join(", ", production.getDirectors()));
                System.out.println("Actors: " + String.join(", ", production.getActors()));
                System.out.println("Genre: " + production.getGenres());
                System.out.println("Ratings: " + production.getRatings());
                System.out.println("Plot: " + production.getDescription());
                System.out.println("Rating: " + production.getRating());
                System.out.println();
                return production;
            }
        }
        return null;
    }

    public Series searchSeries(String seriesname) {
        for (Series production : this.series) {
            if (production.getTitle().equals(seriesname)) {
                System.out.println("Title: " + production.getTitle());
                System.out.println("Directors: " + String.join(", ", production.getDirectors()));
                System.out.println("Actors: " + String.join(", ", production.getActors()));
                System.out.println("Genre: " + production.getGenres());
                System.out.println("Ratings: " + production.getRatings());
                System.out.println("Plot: " + production.getDescription());
                System.out.println("Rating: " + production.getRating());
                System.out.println();
                return production;
            }
        }
        return null;
    }
    // method to sort all productions by genre
    public void sortbyGenre(Genre genre) {
        // put movies that have the genre first and the ones wihtout the genre after
        this.movies.sort((movie1, movie2) -> movie1.getGenres().contains(genre) ? -1 : 1);
        this.series.sort((series1, series2) -> series1.getGenres().contains(genre) ? -1 : 1);
    }
    public void sortbyRatings() {
        this.movies.sort((movie1, movie2) -> movie1.getRating() > movie2.getRating() ? 1 : -1);
        this.series.sort((series1, series2) -> series1.getRating() > series2.getRating() ? 1 : -1);
    }

    public void printMovie() {
        for (Production production : this.movies) {
            System.out.println("Title: " + production.getTitle());
            System.out.println("Directors: " + String.join(", ", production.getDirectors()));
            System.out.println("Actors: " + String.join(", ", production.getActors()));
            System.out.println("Genre: " + production.getGenres());

            // ratings
            System.out.println("Ratings: ");
            List<Rating> ratings = production.getRatings();
            for (Rating rating : ratings) {
                System.out.println(rating.getUsername() + ": " + rating.getRating() + ", " + rating.getFeedback());
            }

            System.out.println("Plot: " + production.getDescription());
            System.out.println("Rating: " + production.getRating());
            System.out.println("Duration: " + ((Movie) production).getDuration());
            System.out.println("Release Year: " + ((Movie) production).getYear());
            System.out.println();
        }
    }

    public void printSeries() {
        for (Production production : this.series) {
            System.out.println("Title: " + production.getTitle());
            System.out.println("Directors: " + String.join(", ", production.getDirectors()));
            System.out.println("Actors: " + String.join(", ", production.getActors()));
            System.out.println("Genre: " + production.getGenres());

            // ratings
            System.out.println("Ratings: ");
            List<Rating> ratings = production.getRatings();
            for (Rating rating : ratings) {
                System.out.println(rating.getUsername() + ": " + rating.getRating() + ", " + rating.getFeedback());
            }

            System.out.println("Plot: " + production.getDescription());
            System.out.println("Rating: " + production.getRating());
            System.out.println("Release Year: " + ((Series) production).getReleaseYear());
            System.out.println("Number of seasons: " + ((Series) production).getNumberOfSeasons());
            System.out.println("Seasons: " + ((Series) production).getSeasons());
            System.out.println();
        }
    }



    private void loadRequestsFromJson(String fileName) {
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader(fileName)) {
            // Parse the JSON file
            Object obj = jsonParser.parse(reader);
            JSONArray requestList = (JSONArray) obj;

            // Iterate over request array
            requestList.forEach(req -> parseRequestObject((JSONObject) req));

        } catch (IOException e) {
            System.out.println("Error: File could not open " + fileName);
            e.printStackTrace();
        } catch (ParseException e) {
            System.out.println("Error: Data from file " + fileName + " are not valid");
            e.printStackTrace();
        }
    }

    private void parseRequestObject(JSONObject request) {
        // Get request type
        String type = (String) request.get("type");
        RequestTypes requestType = RequestTypes.valueOf(type);

        // Get request date
        String createdDate = (String) request.get("createdDate");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime date = LocalDateTime.parse(createdDate, formatter);

        // Get request username
        String username = (String) request.get("username");

        // Get request to
        String to = (String) request.get("to");

        // Get request description
        String description = (String) request.get("description");

        if (requestType == RequestTypes.MOVIE_ISSUE || requestType == RequestTypes.ACTOR_ISSUE) {
            // Get request title
            String title = (String) request.get("title");

            Request requestObj = new Request(requestType, date, username, to, description, title);

            // Add request to list
            this.requests.add(requestObj);
        } else {
            Request requestObj = new Request(requestType, date, username, to, description, null);

            // Add request to list
            this.requests.add(requestObj);
        }
    }

    public void printRequests() {
        for (Request request : this.requests) {
            System.out.println("Request type: " + request.getRequestType());
            System.out.println("Date: " + request.getDate());
            System.out.println("Username: " + request.getUsername());
            System.out.println("To: " + request.getUsernameToResolve());
            System.out.println("Description: " + request.getDescription());
            System.out.println();
        }
    }

    //getters and setters
    public List<User<?>> getUsers() {
        return users;
    }
    public void setUsers(List<User<?>> users) {
        this.users = users;
    }
    public List<Actor> getActors() {
        return actors;
    }
    public void setActors(List<Actor> actors) {
        this.actors = actors;
    }
    public List<Request> getRequests() {
        return requests;
    }
    public void setRequests(List<Request> requests) {
        this.requests = requests;
    }
    public List<Movie> getMovies() {
        return movies;
    }
    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }
    public List<Series> getSeries() { return series; }
    public void setSeries(List<Series> series) { this.series = series; }
    public User<?> authenticateUser() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Please insert your credentials.");
            System.out.print("Email: ");
            String email = scanner.nextLine();

            System.out.print("Password: ");
            String password = scanner.nextLine();

            // Caută utilizatorul în lista dată pe baza credențialelor
            for (User<?> user : users) {
                // Obține obiectul de credențiale
                Credentials credentials = user.getInformation().getCredentials();

                // Verifică dacă credențialele sunt nenule și dacă adresa de email și parola sunt nenule
                if (credentials != null &&
                        credentials.getEmail() != null &&
                        credentials.getPassword() != null &&
                        credentials.getEmail().equals(email) &&
                        credentials.getPassword().equals(password)) {
                    System.out.println("Authentification successful. Welcome, " + user.getInformation().getName() + "!");
                    System.out.println("Username: " + user.getUsername());
                    if(user.getExperience() == 0) {
                        System.out.println("Experience: " + '-');
                    }
                    else {
                        System.out.println("Experience: " + user.getExperience());
                    }
                    return user;
                }
            }

            // Afișează mesaj de eroare și solicită reintroducerea credențialelor
            System.out.println("Incorrect credentials. Please try again.");
        }
    }

    public User<?> authenticateUserswing(String email, String password) {
        // Caută utilizatorul în lista dată pe baza credențialelor
        for (User<?> user : users) {
            // Obține obiectul de credențiale
            Credentials credentials = user.getInformation().getCredentials();

            // Verifică dacă credențialele sunt nenule și dacă adresa de email și parola sunt nenule
            if (credentials != null &&
                    credentials.getEmail() != null &&
                    credentials.getPassword() != null &&
                    credentials.getEmail().equals(email) &&
                    credentials.getPassword().equals(password)) {
                        return user;
            }
        }
        return null;
    }

    public User<?> searchUser(String username) {
        for (User user : this.users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public void addUser() {
        System.out.println("Add user details:");
        System.out.print("username: ");
        Scanner scanner = new Scanner(System.in);
        String username = scanner.nextLine();

        System.out.println("experience: ");
        int experience = scanner.nextInt();

        System.out.println("credentials: ");
        System.out.println("email: ");
        String email = scanner.nextLine();
        System.out.println("password: ");
        String password = scanner.nextLine();
        scanner.nextLine(); // Consuma newline-ul ramas in buffer
        Credentials credentials = new Credentials(email, password);

        System.out.println("name: ");
        String name = scanner.nextLine();

        System.out.println("country: ");
        String country = scanner.nextLine();
        System.out.println("age: ");
        int age = scanner.nextInt();
        System.out.println("gender: ");
        String gender = scanner.nextLine();
        System.out.println("birthDate: ");
        // birthdate is type
        LocalDateTime birthDate = LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay();

        InformationBuilder builder = new InformationBuilder();
        builder.setName(name);
        builder.setAge(age);
        builder.setBirthDate(birthDate);
        builder.setCountry(country);
        builder.setCredentials(credentials);
        User.Information information = builder.build();

        System.out.println("userType: ");
        String userType = scanner.nextLine();
        AccountType accountType = AccountType.valueOf(userType);

        if (accountType == AccountType.Regular) {
            // citire notificari de la tastatura
            List<String> notifications = new ArrayList<>();
            System.out.println("notifications: ");
            // read notifications till user presses enter button
            while (scanner.hasNextLine()) {
                String notification = scanner.nextLine();
                if (notification.equals("")) {
                    break;
                }
                notifications.add(notification);
            }

            User currentUser = UserFactory.createUser(accountType, information, username, experience, notifications, null, null, null);

            this.users.add(currentUser);
        } else {
            User currentUser = UserFactory.createUser(accountType, information, username, experience, null, null, null, null);
            this.users.add(currentUser);
        }
    }

    public void deleteUser(User<?> user) {
        this.users.remove(user);
    }

    public Request searchRequest(String username) {
        for (Request request : this.requests) {
            if (request.getUsernameToResolve().equals(username)) {
                System.out.println("Request found");
                System.out.println("Request type: " + request.getRequestType());
                System.out.println("Date: " + request.getDate());
                System.out.println("Username: " + request.getUsername());
                System.out.println("Title: " + request.getTitle());
                System.out.println("To: " + request.getUsernameToResolve());
                System.out.println("Description: " + request.getDescription());
                System.out.println();
                return request;
            }
        }
        return null;
    }

    public Request searchRequestbyusername(String username) {
        for (Request request : this.requests) {
            if (request.getUsername().equals(username)) {
                System.out.println("Request found");
                System.out.println("Request type: " + request.getRequestType());
                System.out.println("Date: " + request.getDate());
                System.out.println("Username: " + request.getUsername());
                System.out.println("Title: " + request.getTitle());
                System.out.println("To: " + request.getUsernameToResolve());
                System.out.println("Description: " + request.getDescription());
                System.out.println();
                // add notification to user
                if (searchUser(request.getUsernameToResolve()) != null) {
                    request.registerObserver(searchUser(request.getUsernameToResolve()));
                    String notificationMessage = "User " + request.getUsername() + " has a new request for you";
                    request.notifyObservers(notificationMessage);
                    request.removeObserver(searchUser(request.getUsernameToResolve()));
                }
                return request;
            }
        }
        return null;
    }
    public void searchRequests() {
        RequestsHolder requests = new RequestsHolder();
        int ok = 0;
        for (Request request : this.requests) {
            if (request.getUsernameToResolve().equals("ADMIN")) {
                if (ok == 0) {
                    createAdminObservers();
                    ok = 1;
                }
                System.out.println("Request found");
                System.out.println("Request type: " + request.getRequestType());
                System.out.println("Date: " + request.getDate());
                System.out.println("Username: " + request.getUsername());
                System.out.println("To: " + request.getUsernameToResolve());
                System.out.println("Description: " + request.getDescription());
                System.out.println();
                requests.addRequest(request);
                // add notification to user
                String notificationMessage = "User " + request.getUsername() + " has a new request for you";
//                OpenAI openAI = new OpenAI();
//                String notificationMessageAI = openAI.createNotification(notificationMessage);
//                System.out.println("aici : " + notificationMessageAI);
                request.notifyObservers(notificationMessage);
            }
        }
        removeAdminObservers();
    }

    public void addRating(Production production) {
        // add rating
        System.out.println("Add rating details:");
        System.out.print("username: ");
        Scanner scanner = new Scanner(System.in);
        String username = scanner.nextLine();
        System.out.print("rating: ");
        int rating = scanner.nextInt();
        scanner.nextLine(); // Consuma newline-ul ramas in buffer
        System.out.print("comment: ");
        String comment = scanner.nextLine();
        Rating ratingfin = new Rating(username, rating, comment, AccountType.Regular);
        production.addRating(ratingfin);

        // update experience
        User<?> userxp = searchUser(username);
        if (userxp != null) {
            userxp.setExperienceStrategy(new RatingExperienceStrategy());
            userxp.updateExperience(userxp.getExperience());
        }

        // create observers
        for(Rating rating1 : production.getRatings()) {
            if (searchUser(rating1.getUsername()) != null) {
                ratingfin.registerObserver(searchUser(rating1.getUsername()));
            }
        }
        for (User user : this.users) {
            if (user.getAccountType() == AccountType.Admin || user.getAccountType() == AccountType.Contributor) {
                Staff admin = (Staff) user;
                for (Object contribution : admin.getContributions()) {
                    if (contribution.equals(production.getTitle())) {
                        ratingfin.registerObserver(admin);
                    }
                }
            }
        }

        // notify users that a new rating has been added
        String notificationMessage = "User " + username + " added a new rating for " + production.getTitle();
        ratingfin.notifyObservers(notificationMessage);

        // remove observers
        for(Rating rating1 : production.getRatings()) {
            if (searchUser(rating1.getUsername()) != null) {
                rating1.removeObserver(searchUser(rating1.getUsername()));
            }
        }
    }

    public void removeRating(Production production, String username) {
        int gasit = 0;
        for (Rating rating : production.getRatings()) {
            if (rating.getUsername().equals(username)) {
                System.out.println("Rating found");
                gasit = 1;
                production.removeRating(rating);
                break;
            }
        }
        if (gasit == 0) {
            throw new IllegalArgumentException("You have no rating to remove");
        }
    }

    public void createAdminObservers() {
        for (User user : this.users) {
            if (user.getAccountType() == AccountType.Admin) {
                for (Request request : this.requests) {
                    if (request.getUsernameToResolve().equals("ADMIN")){
                        request.registerObserver(user);
                        request.registerObserver(user);
                    }
                }
            }
        }
    }
    public void removeAdminObservers() {
        for (User user : this.users) {
            if (user.getAccountType() == AccountType.Admin) {
                for (Request request : this.requests) {
                    if (request.getUsernameToResolve().equals("ADMIN")){
                        request.removeObserver(user);
                    }
                }
            }
        }
    }
    public void createObsereversforRequests() {
        for (Request request : this.requests) {
            User user = searchUser(request.getUsername());
            request.registerObserver(user);
//            if (request.getUsernameToResolve().equals("ADMIN")) {
//                for (User user1 : this.users) {
//                    if (user1.getAccountType() == AccountType.Admin) {
//                        request.registerObserver(user1);
//                    }
//                }
//            }
        }
    }

    public void addRatingtoActor(Actor actor) {
                // add rating
                System.out.println("Add rating details:");
                System.out.print("username: ");
                Scanner scanner = new Scanner(System.in);
                String username = scanner.nextLine();
                System.out.print("rating: ");
                int rating = scanner.nextInt();
                scanner.nextLine(); // Consuma newline-ul ramas in buffer
                System.out.print("comment: ");
                String comment = scanner.nextLine();
                Rating ratingfin = new Rating(username, rating, comment, AccountType.Regular);
                actor.addRating(ratingfin);

                // update experience
                User<?> userxp = searchUser(username);
                if (userxp != null) {
                    userxp.setExperienceStrategy(new RatingExperienceStrategy());
                    userxp.updateExperience(userxp.getExperience());
                }

                // create observers
                for(Rating rating1 : actor.getRatings()) {
                    if (searchUser(rating1.getUsername()) != null) {
                        ratingfin.registerObserver(searchUser(rating1.getUsername()));
                    }
                }
                for (User user : this.users) {
                    if (user.getAccountType() == AccountType.Admin || user.getAccountType() == AccountType.Contributor) {
                        Staff admin = (Staff) user;
                        for (Object contribution : admin.getContributions()) {
                            if (contribution.equals(actor.getNume())) {
                                ratingfin.registerObserver(admin);
                            }
                        }
                    }
                }

                // notify users that a new rating has been added
                String notificationMessage = "User " + username + " added a new rating for " + actor.getNume();
                ratingfin.notifyObservers(notificationMessage);

                // remove observers
                for(Rating rating1 : actor.getRatings()) {
                    if (searchUser(rating1.getUsername()) != null) {
                        rating1.removeObserver(searchUser(rating1.getUsername()));
                    }
                }
    }

    public void removeRatingtoActor(Actor actor, String username) {
        int gasit = 0;
        for (Rating rating : actor.getRatings()) {
            if (rating.getUsername().equals(username)) {
                System.out.println("Rating found");
                gasit = 1;
                actor.removeRating(rating);
                break;
            }
        }
        if (gasit == 0) {
            throw new IllegalArgumentException("You have no rating to remove");
        }
    }

    // function to write rqeuests to json file
    public void writeRequestsjson(String filename) {
        // takes the requests from imdn.requests and writes them to a json file
        JSONArray requests = new JSONArray();
        for (Request request : this.requests) {
            JSONObject requestDetails = new JSONObject();
            requestDetails.put("type", request.getRequestType());
            requestDetails.put("createdDate", request.getDate());
            requestDetails.put("username", request.getUsername());
            requestDetails.put("to", request.getUsernameToResolve());
            requestDetails.put("description", request.getDescription());
            requestDetails.put("title", request.getTitle());
            requests.add(requestDetails);
        }
        try (FileWriter file = new FileWriter(filename)) {
            file.write(requests.toJSONString().replaceAll("\\{", "{\n").replaceAll(",", ",\n").replaceAll("}", "\n}").replaceAll("\\[", "[\n").replaceAll("]", "\n]"));            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeActorsjson(String filename){
        JSONArray actors = new JSONArray();
        for (Actor actor : this.actors) {
            JSONObject actorDetails = new JSONObject();
            actorDetails.put("name", actor.getNume());
            actorDetails.put("biography", actor.getBiografie());
            JSONArray performances = new JSONArray();
            for (Performance performance : actor.getPerformances()) {
                JSONObject performanceDetails = new JSONObject();
                performanceDetails.put("title", performance.getTitle());
                performanceDetails.put("type", performance.getType());
                performances.add(performanceDetails);
            }
            actorDetails.put("performances", performances);
            actors.add(actorDetails);
        }
        // write in json file
        try (FileWriter file = new FileWriter(filename)) {
            // add newlines in the json file after every '{' and ',' and before every '}' and after it
            file.write(actors.toJSONString().replaceAll("\\{", "{\n").replaceAll(",", ",\n").replaceAll("}", "\n}").replaceAll("\\[", "[\n").replaceAll("]", "\n]"));
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeMoviesjson(String filename) {
        JSONArray movies = new JSONArray();
        for (Movie movie : this.movies) {
            JSONObject movieDetails = new JSONObject();
            movieDetails.put("title", movie.getTitle());
            movieDetails.put("directors", movie.getDirectors());
            movieDetails.put("actors", movie.getActors());
            movieDetails.put("genres", movie.getGenres());
            JSONArray ratings = new JSONArray();
            for (Rating rating : movie.getRatings()) {
                JSONObject ratingDetails = new JSONObject();
                ratingDetails.put("username", rating.getUsername());
                ratingDetails.put("rating", rating.getRating());
                ratingDetails.put("comment", rating.getFeedback());
                ratings.add(ratingDetails);
            }
            movieDetails.put("ratings", ratings);
            movieDetails.put("plot", movie.getDescription());
            movieDetails.put("averageRating", movie.getRating());
            movieDetails.put("duration", movie.getDuration());
            movieDetails.put("releaseYear", movie.getYear());
            movies.add(movieDetails);
        }
        // write in json file
        try (FileWriter file = new FileWriter(filename)) {
            file.write(movies.toJSONString().replaceAll("\\{", "{\n").replaceAll(",", ",\n").replaceAll("}", "\n}").replaceAll("\\[", "[\n").replaceAll("]", "\n]"));
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeSeriesjson(String filename) {
        JSONArray series = new JSONArray();
        for (Series serie : this.series) {
            JSONObject serieDetails = new JSONObject();
            serieDetails.put("title", serie.getTitle());
            serieDetails.put("directors", serie.getDirectors());
            serieDetails.put("actors", serie.getActors());
            serieDetails.put("genres", serie.getGenres());
            JSONArray ratings = new JSONArray();
            for (Rating rating : serie.getRatings()) {
                JSONObject ratingDetails = new JSONObject();
                ratingDetails.put("username", rating.getUsername());
                ratingDetails.put("rating", rating.getRating());
                ratingDetails.put("comment", rating.getFeedback());
                ratings.add(ratingDetails);
            }
            serieDetails.put("ratings", ratings);
            serieDetails.put("plot", serie.getDescription());
            serieDetails.put("averageRating", serie.getRating());
            serieDetails.put("releaseYear", serie.getReleaseYear());
            serieDetails.put("numSeasons", serie.getNumberOfSeasons());
            serieDetails.put("seasons", serie.getSeasons());
            series.add(serieDetails);
        }
        // write in json file
        try (FileWriter file = new FileWriter(filename)) {
            file.write(series.toJSONString().replaceAll("\\{", "{\n").replaceAll(",", ",\n").replaceAll("}", "\n}").replaceAll("\\[", "[\n").replaceAll("]", "\n]"));
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeUsersjson(String filename) {
        IMDB imdb = IMDB.getInstance();
        JSONArray users = new JSONArray();
        for (User user : this.users) {
            JSONObject userDetails = new JSONObject();
            userDetails.put("username", user.getUsername());
            userDetails.put("experience", user.getExperience());
            userDetails.put("accountType", user.getAccountType());
            userDetails.put("notifications", user.getNotifications());
            userDetails.put("favorites", user.getFavorites());
            if (user.getAccountType() == AccountType.Contributor || user.getAccountType() == AccountType.Admin) {
                Staff staffMember = (Staff) user;
                userDetails.put("contributions", staffMember.getContributions());
            }
            JSONObject information = new JSONObject();
            information.put("name", user.getInformation().getName());
            information.put("age", user.getInformation().getAge());
            information.put("birthDate", user.getInformation().getBirthDate());
            information.put("country", user.getInformation().getCountry());
            JSONObject credentials = new JSONObject();
            credentials.put("email", user.getInformation().getCredentials().getEmail());
            credentials.put("password", user.getInformation().getCredentials().getPassword());
            information.put("credentials", credentials);
            userDetails.put("information", information);
            users.add(userDetails);
        }
        // write in json file
        try (FileWriter file = new FileWriter(filename)) {
            // after every user add a newline
            file.write(users.toJSONString().replaceAll("\\{", "{\n").replaceAll(",", ",\n").replaceAll("}", "\n}").replaceAll("\\[", "[\n").replaceAll("]", "\n]"));
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
