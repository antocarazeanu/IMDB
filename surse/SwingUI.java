import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.net.URL;

public class SwingUI {

    private static JFrame mainFrame;

    public static void createAndShowGUI() {
        JFrame frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // set the login in the centre of the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(400, 200);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        placeComponents(panel);

        frame.getContentPane().add(BorderLayout.CENTER, panel);
        frame.setVisible(true);
    }

    private static void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel userLabel = new JLabel("Email:");
        userLabel.setBounds(10, 20, 80, 25);
        panel.add(userLabel);

        JTextField userText = new JTextField(20);
        userText.setBounds(100, 20, 165, 25);
        panel.add(userText);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(10, 50, 80, 25);
        panel.add(passwordLabel);

        JPasswordField passwordText = new JPasswordField(20);
        passwordText.setBounds(100, 50, 165, 25);
        panel.add(passwordText);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(10, 80, 80, 25);
        panel.add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (authenticateUser(userText.getText(), new String(passwordText.getPassword())) != null) {
                    JOptionPane.showMessageDialog(panel, "Autentificare cu succes! Bun venit!");
                    IMDB imdb = IMDB.getInstance();
                    List<User<?>> users = imdb.getUsers();
                    for (User<?> user : users) {
                        if (user.getInformation().getCredentials().getEmail().equals(userText.getText())) {
                            openMainPage(user.getUsername(), user.getInformation().getName(), user.getExperience(), user);

                            // close the login tab
                            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(panel);
                            frame.dispose();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(panel, "Credențiale incorecte. Vă rugăm să încercați din nou.");
                }
            }
        });
    }

    private static void openMainPage(String username, String name, int experience, User<?> currentuser){
        mainFrame = new JFrame("Pagina Principală");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // set the size of the main page and centre it
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        mainFrame.setSize(screenSize.width, screenSize.height);
        mainFrame.setLocationRelativeTo(null);


        // set the background color to dark gray
        mainFrame.getContentPane().setBackground(Color.darkGray);

        // add logo to the main page
        ImageIcon imageIcon = new ImageIcon("POO-Tema-2023-checker/POO-Tema-2023-checker/src/main/java/org/example/logo.png");
        Image image = imageIcon.getImage();
        Image newimg = image.getScaledInstance(200, 100,  java.awt.Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(newimg);
        JLabel imageLabel = new JLabel(imageIcon);
        mainFrame.getContentPane().add(BorderLayout.NORTH, imageLabel);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainFrame.getContentPane().add(mainPanel);
        mainFrame.setVisible(true);

        // add a scroll bar
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        mainFrame.getContentPane().add(scrollPane);

        JPanel contentPanel = new JPanel();
        // set the background color to dark gray
        contentPanel.setBackground(Color.darkGray);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        mainPanel.add(contentPanel);

        // label with recommendations
        JPanel recommendationsPanel = new JPanel();
        recommendationsPanel.setBorder(BorderFactory.createTitledBorder("Recommendations"));
        contentPanel.add(recommendationsPanel);

        recommendationsPanel.add(new JLabel("Recommendations for " + username + " (" + name + ") with " + experience + " experience:"));

// Use a JTextArea for multiline text
        JTextArea recommendationsTextArea = new JTextArea(10, 30);
        recommendationsTextArea.setEditable(false); // make it read-only
        JScrollPane scrollPanex = new JScrollPane(recommendationsTextArea);
        recommendationsPanel.add(scrollPanex);

// create recommendations
        createRecommendations(username, recommendationsTextArea);

// add 2 buttons to sort the productions, one button for genre sorting and one for rating sorting
        JButton genreButton = new JButton("Sort by genre");
        recommendationsPanel.add(genreButton);

        JButton ratingButton = new JButton("Sort by rating");
        recommendationsPanel.add(ratingButton);

// when the genre button is pressed, the production in the current page are sorted by genre
        genreButton.addActionListener(e -> {
            IMDB imdb = IMDB.getInstance();
            String genretext = JOptionPane.showInputDialog("Enter genre:");

            Genre genre = Genre.valueOf(genretext);
            imdb.sortbyGenre(genre);
            createRecommendations(username, recommendationsTextArea);
        });

// when the rating button is pressed, the production in the current page are sorted by rating
        ratingButton.addActionListener(e -> {
            IMDB imdb = IMDB.getInstance();
            imdb.sortbyRatings();
            createRecommendations(username, recommendationsTextArea);
        });


        // Search bars panels
        JPanel searchPanelmovies = new JPanel();
        searchPanelmovies.setBorder(BorderFactory.createTitledBorder("Search movies"));
        contentPanel.add(searchPanelmovies);

        JPanel searchPanelseries = new JPanel();
        searchPanelseries.setBorder(BorderFactory.createTitledBorder("Search series"));
        contentPanel.add(searchPanelseries);

        JPanel searchPanelactors = new JPanel();
        searchPanelactors.setBorder(BorderFactory.createTitledBorder("Search actors"));
        contentPanel.add(searchPanelactors);

        // add a search bar to every search panel
        JTextField searchFieldmovies = new JTextField(20);
        searchPanelmovies.add(searchFieldmovies);

        JTextField searchFieldseries = new JTextField(20);
        searchPanelseries.add(searchFieldseries);

        JTextField searchFieldactors = new JTextField(20);
        searchPanelactors.add(searchFieldactors);

        // search buttons on the right of every search bar
        JButton searchButtonmovies = new JButton("Search");
        searchPanelmovies.add(searchButtonmovies);

        JButton searchButtonseries = new JButton("Search");
        searchPanelseries.add(searchButtonseries);

        JButton searchButtonactors = new JButton("Search");
        searchPanelactors.add(searchButtonactors);

        // saves the text from the search bar and call the search function
        searchFieldmovies.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String movieName = searchFieldmovies.getText();
                Movie movie = searchMovie(movieName);
                if (movie != null) {
                    JOptionPane.showMessageDialog(searchPanelmovies, "Title: " + movie.getTitle() + "\n" + "Year: " + movie.getYear() + "\n" + "Rating: " + movie.getRating() + "\n" + "Duration: " + movie.getDuration() + "\n" + "Genres: " + movie.getGenres() + "\n" + "Actors: " + movie.getActors() + "\n" + "Director: " + movie.getDirectors() + "\n" + "Description: " + movie.getDescription() + "\n" + "Reviews: " + movie.getRatings());
                } else {
                    JOptionPane.showMessageDialog(searchPanelmovies, "Movie not found.");
                }
            }
        });

        searchFieldseries.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String seriesName = searchFieldseries.getText();
                Series series = searchSeries(seriesName);
                if (series != null) {
                    JOptionPane.showMessageDialog(searchPanelseries, "Title: " + series.getTitle() + "\n" + "\n" + "Rating: " + series.getRating() + "\n" + "Number of seasons:" + series.getNumberOfSeasons() + "\n" + "Genres: " + series.getGenres() + "\n" + "Actors: " + series.getActors() + "\n" + "Director: " + series.getDirectors() + "\n" + "Description: " + series.getDescription() + "\n" + "Reviews: " + series.getRatings());
                } else {
                    JOptionPane.showMessageDialog(searchPanelseries, "Series not found.");
                }
            }
        });

        searchFieldactors.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String actorName = searchFieldactors.getText();
                Actor actor = searchActor(actorName);
                if (actor != null) {
                    JOptionPane.showMessageDialog(searchPanelactors, "Name: " + actor.getNume() + "\n" + "Performances: " + actor.getPerformances() + "\n" + "Biography: " + actor.getBiografie());
                } else {
                    JOptionPane.showMessageDialog(searchPanelactors, "Actor not found.");
                }
            }
        });

        // Actor panel
        JPanel actorPanel = new JPanel();
        actorPanel.setBorder(BorderFactory.createTitledBorder("Actors"));
        contentPanel.add(actorPanel);

        // actor button that opens a new window with the actor's information
        JButton actorButton = new JButton("Actor");
        actorPanel.add(actorButton);

        // when the button is pressed, a new window opens and list every actor in the system with a scroll bar
        actorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame actorFrame = new JFrame("Actor");
                actorFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                actorFrame.setSize(400, 800);

                JPanel actorPanel = new JPanel();
                actorPanel.setLayout(new BoxLayout(actorPanel, BoxLayout.Y_AXIS));
                actorFrame.getContentPane().add(actorPanel);

                IMDB imdb = IMDB.getInstance();
                List<Actor> actors = imdb.getActors();
                // sort the actors by name
                actors.sort((a1, a2) -> a1.getNume().compareTo(a2.getNume()));
                for (Actor actor : actors) {
                    actorPanel.add(new JLabel(actor.getNume() + ":"));

                    List<User<?>> users = imdb.getUsers();
                    User<?> currentUser = null;
                    for (User<?> user : users) {
                        if (user.getUsername().equals(username)) {
                            currentUser = user;
                        }
                    }

                    if (currentuser.getAccountType() == AccountType.Regular) {
                        // add a rating button for every actor in the system that opens a new window with the actor's rating
                        JButton ratingButton = new JButton("Rate " + actor.getNume());
                        actorPanel.add(ratingButton);

                        // when the rating button is pressed, a new window opens and list every rating for the actor
                        ratingButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                // opens a form to fill with the rating
                                // rating in int ( 1 - 10)
                                // feedback is a string
                                JFrame ratingFrame = new JFrame("Rating");
                                ratingFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                ratingFrame.setSize(400, 800);

                                JPanel ratingPanel = new JPanel();
                                ratingPanel.setLayout(new BoxLayout(ratingPanel, BoxLayout.Y_AXIS));
                                ratingFrame.getContentPane().add(ratingPanel);

                                // add form
                                JLabel ratingLabel = new JLabel("Rating:");
                                ratingPanel.add(ratingLabel);
                                JTextField ratingField = new JTextField(20);
                                ratingPanel.add(ratingField);

                                JLabel feedbackLabel = new JLabel("Feedback:");
                                ratingPanel.add(feedbackLabel);
                                JTextField feedbackField = new JTextField(20);
                                ratingPanel.add(feedbackField);

                                // add button
                                JButton rateButton = new JButton("Rate");
                                ratingPanel.add(rateButton);

                                // when the rate button is pressed, the rating is added to the actor
                                // and the actor page is refreshed so that the ratings are updated
                                rateButton.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        int rating = Integer.parseInt(ratingField.getText());
                                        String feedback = feedbackField.getText();

                                        Rating rating1 = new Rating(username, rating, feedback, AccountType.Regular);
                                        actor.addRating(rating1);
                                        JOptionPane.showMessageDialog(ratingPanel, "Rating added.");

                                        // update the system
                                        IMDB imdb = IMDB.getInstance();
                                        List<Actor> actors = imdb.getActors();
                                        for (Actor actor1 : actors) {
                                            if (actor1.getNume().equals(actor.getNume())) {
                                                actor1 = actor;
                                            }
                                        }
                                        imdb.actors = actors;

                                        // refresh the actor page by closing and reopening it
                                        actorFrame.dispose();
                                        // actionate the actorbutton
                                        actorButton.doClick();
                                    }
                                });

                                ratingFrame.setVisible(true);
                            }
                        });
                    }

                    // add scroll
                    JScrollPane scrollPane = new JScrollPane(actorPanel);
                    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                    actorFrame.getContentPane().add(scrollPane);

                    // add image for every actor
                    ImageIcon imageIcon = new ImageIcon("POO-Tema-2023-checker/POO-Tema-2023-checker/src/main/java/org/example/" + actor.getNume() + ".jpg");
                    Image image = imageIcon.getImage();
                    Image newimg = image.getScaledInstance(200, 100,  java.awt.Image.SCALE_SMOOTH);
                    imageIcon = new ImageIcon(newimg);
                    JLabel imageLabel = new JLabel(imageIcon);
                    actorPanel.add(imageLabel);

                    actor.setAvgRating();
                    double avg = actor.getAvgRating();
                    actorPanel.add(new JLabel("Average rating: " + avg));
                }

                actorFrame.setVisible(true);
            }
        });

        // Menu button under actor panel
        JPanel menuPanel = new JPanel();
        menuPanel.setBorder(BorderFactory.createTitledBorder("Menu"));
        contentPanel.add(menuPanel);

        // Add action button that opens a new window with the menu options
        JButton menuButton = new JButton("Menu");
        menuPanel.add(menuButton);

        // when pressed the menu button opens a new window with the menu options
        menuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame menuFrame = new JFrame("Menu");
                menuFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                menuFrame.setSize(400, 800);

                JPanel menuPanel = new JPanel();
                menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
                menuFrame.getContentPane().add(menuPanel);

                IMDB imdb = IMDB.getInstance();
                List<String> actions = new ArrayList<>();
                actions.add("View production details");
                actions.add("View actors details");
                actions.add("View notifications");
                actions.add("Add/Delete actors/movies/series to/from favorites");
                if (currentuser.getAccountType() == AccountType.Admin) {
                    actions.add("Add/Delete user");
                }
                if (currentuser.getAccountType() == AccountType.Admin || currentuser.getAccountType() == AccountType.Contributor) {
                    actions.add("Add/Delete actors/movies/series from system");
                    actions.add("Update production/actor details");
                    actions.add("Solve a request");
                }
                if (currentuser.getAccountType() == AccountType.Regular || currentuser.getAccountType() == AccountType.Contributor) {
                    actions.add("Create/Delete a request");
                }
                if (currentuser.getAccountType() == AccountType.Regular) {
                    actions.add("Add/Remove a rating to/from a production");
                }
                actions.add("Logout");
                for (String action : actions) {
                    JButton button = new JButton(action);
                    button.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            // Perform action based on the button clicked
                            if (action.equals("View production details")) {
                                // Perform action for "View production details" button
                                JFrame productionFrame = new JFrame("Production");
                                productionFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                productionFrame.setSize(400, 800);

                                JPanel productionPanel = new JPanel();
                                productionPanel.setLayout(new BoxLayout(productionPanel, BoxLayout.Y_AXIS));
                                productionFrame.getContentPane().add(productionPanel);

                                // add scroll
                                JScrollPane scrollPane = new JScrollPane(productionPanel);
                                scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                                productionFrame.getContentPane().add(scrollPane);

                                // add productions to the panel
                                IMDB imdb = IMDB.getInstance();
                                List<Movie> movies = imdb.getMovies();
                                List<Series> series = imdb.getSeries();
                                for (Movie movie : movies) {
                                    productionPanel.add(new JLabel("Movie: "));
                                    productionPanel.add(new JLabel("Title: " + movie.getTitle()));
                                    productionPanel.add(new JLabel("Year: " + movie.getYear()));
                                    productionPanel.add(new JLabel("Rating: " + movie.getRating()));
                                    productionPanel.add(new JLabel("Duration: " + movie.getDuration()));
                                    productionPanel.add(new JLabel("Genres: " + movie.getGenres()));
                                    productionPanel.add(new JLabel("Actors: " + movie.getActors()));
                                    productionPanel.add(new JLabel("Director: " + movie.getDirectors()));
                                    productionPanel.add(new JLabel("Description: " + movie.getDescription()));
                                    productionPanel.add(new JLabel("Reviews: " + movie.getRatings()));

                                    // add a link button that opens the movie's trailer in a new tab
                                    JButton trailerButton = new JButton("Trailer");
                                    productionPanel.add(trailerButton);
                                    trailerButton.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            try {
                                                // the link shoud look like https://www.youtube.com/results?search_query=movie.name(without spcaese)+Trailer
                                                String movieName = movie.getTitle();
                                                String movieNameWithoutSpaces = movieName.replace(" ", "+");
                                                String link = "https://www.youtube.com/results?search_query=" + movieNameWithoutSpaces + "+Trailer";
                                                Desktop.getDesktop().browse(new URL(link).toURI());
                                            } catch (Exception ex) {
                                                ex.printStackTrace();
                                            }
                                        }
                                    });

                                    // set visible
                                    productionFrame.setVisible(true);
                                }
                                for (Series serie : series) {
                                    productionPanel.add(new JLabel("Series: "));
                                    productionPanel.add(new JLabel("Title: " + serie.getTitle()));
                                    productionPanel.add(new JLabel("Rating: " + serie.getRating()));
                                    productionPanel.add(new JLabel("Number of seasons: " + serie.getNumberOfSeasons()));
                                    productionPanel.add(new JLabel("Genres: " + serie.getGenres()));
                                    productionPanel.add(new JLabel("Actors: " + serie.getActors()));
                                    productionPanel.add(new JLabel("Director: " + serie.getDirectors()));
                                    productionPanel.add(new JLabel("Description: " + serie.getDescription()));
                                    productionPanel.add(new JLabel("Reviews: " + serie.getRatings()));

                                    // add a link button that opens the movie's trailer in a new tab
                                    JButton trailerButton = new JButton("Trailer");
                                    productionPanel.add(trailerButton);
                                    trailerButton.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            try {
                                                // the link shoud look like https://www.youtube.com/results?search_query=movie.name(without spcaese)+Trailer
                                                String seriesTitle = serie.getTitle();
                                                String seriesNameWithoutSpaces = seriesTitle.replace(" ", "+");
                                                String link = "https://www.youtube.com/results?search_query=" + seriesNameWithoutSpaces + "+Trailer";
                                                Desktop.getDesktop().browse(new URL(link).toURI());
                                            } catch (Exception ex) {
                                                ex.printStackTrace();
                                            }
                                        }
                                    });

                                    // set visible
                                    productionFrame.setVisible(true);
                                }

                                productionFrame.setVisible(true);
                            } else if (action.equals("View actors details")) {
                                // Perform action for "View actors details" button
                                JFrame actorFrame = new JFrame("Actor");
                                actorFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                actorFrame.setSize(400, 800);

                                JPanel actorPanel = new JPanel();
                                actorPanel.setLayout(new BoxLayout(actorPanel, BoxLayout.Y_AXIS));
                                actorFrame.getContentPane().add(actorPanel);

                                // add scroll
                                JScrollPane scrollPane = new JScrollPane(actorPanel);
                                scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                                actorFrame.getContentPane().add(scrollPane);

                                IMDB imdb = IMDB.getInstance();
                                List<Actor> actors = imdb.getActors();
                                for (Actor actor : actors) {
                                    actorPanel.add(new JLabel("Actor:"));
                                    actorPanel.add(new JLabel("Name: " + actor.getNume()));
                                    actorPanel.add(new JLabel("Performances: "));
                                    for (Performance performance : actor.getPerformances()) {
                                        actorPanel.add(new JLabel("Title: " + performance.getTitle()));
                                        actorPanel.add(new JLabel("Type: " + performance.getType()));
                                    }
                                    actorPanel.add(new JLabel("Biography: " + actor.getBiografie()));
                                }

                                actorFrame.setVisible(true);
                            } else if (action.equals("View notifications")) {
                                // Perform action for "View notifications" button
                                // notification are in user.getNotifications()
                                // call the function here and print the result in a new window
                                JFrame notificationFrame = new JFrame("Notifications");
                                notificationFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                notificationFrame.setSize(900, 500);

                                JPanel notificationPanel = new JPanel();
                                notificationPanel.setLayout(new BoxLayout(notificationPanel, BoxLayout.Y_AXIS));
                                notificationFrame.getContentPane().add(notificationPanel);

                                // scroll
                                JScrollPane scrollPane = new JScrollPane(notificationPanel);
                                scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                                notificationFrame.getContentPane().add(scrollPane);

                                // add the notifications to the panel
                                IMDB imdb = IMDB.getInstance();
                                List<User<?>> users = imdb.getUsers();
                                for (User<?> user : users) {
                                    if (user.getUsername().equals(username)) {
                                        List<String> notifications = user.getNotifications();

                                        // add the notifications to the panel
                                        for (String notification : notifications) {
                                            notificationPanel.add(new JLabel(notification));
                                        }
                                    }
                                }

                                notificationFrame.setVisible(true);
                            } else if (action.equals("Add/Delete actors/movies/series to/from favorites")) {
                                // Perform action for "Add/Delete actors/movies/series to/from favorites" button
                                // add a new window where the user can search the object he wants to add to favorites
                                // and a button to add it to favorites
                                JFrame favoritesFrame = new JFrame("Favorites");
                                favoritesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                favoritesFrame.setSize(400, 800);

                                JPanel favoritesPanel = new JPanel();
                                favoritesPanel.setLayout(new BoxLayout(favoritesPanel, BoxLayout.Y_AXIS));
                                favoritesFrame.getContentPane().add(favoritesPanel);

                                // add scroll
                                JScrollPane scrollPane = new JScrollPane(favoritesPanel);
                                scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                                favoritesFrame.getContentPane().add(scrollPane);

                                // add search bar
                                JTextField searchField = new JTextField(20);
                                favoritesPanel.add(searchField);

                                // add search button
                                JButton searchButton = new JButton("Search");
                                favoritesPanel.add(searchButton);

                                // get the text and search the object in the system using imdb.searchMovie/Actor/Series
                                // if the object is found the user can add it to favorites (in the system)
                                // if the item already exists in the favorites list, the user can delete it
                                searchButton.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        String objectName = searchField.getText();
                                        Movie movie = searchMovie(objectName);
                                        Series series = searchSeries(objectName);
                                        Actor actor = searchActor(objectName);
                                        if (movie != null) {
                                            // add the movie to favorites
                                            // if the movie is already in favorites, delete it
                                            // if the movie is not in favorites, add it
                                            IMDB imdb = IMDB.getInstance();
                                            List<User<?>> users = imdb.getUsers();
                                            for (User<?> user : users) {
                                                if (user.getUsername().equals(username)) {
                                                    List<Movie> movies = new ArrayList<>();
                                                    for (Object film : user.getFavorites()) {
                                                        if (film instanceof Movie) {
                                                            movies.add((Movie) film);
                                                        }
                                                    }
                                                    if (movies.contains(movie)) {
                                                        movies.remove(movie);
                                                        JOptionPane.showMessageDialog(favoritesPanel, "Movie removed from favorites.");
                                                    } else {
                                                        movies.add(movie);
                                                        JOptionPane.showMessageDialog(favoritesPanel, "Movie added to favorites.");
                                                    }
                                                }
                                            }
                                        } else if (series != null) {
                                            // add the series to favorites
                                            // if the series is already in favorites, delete it
                                            // if the series is not in favorites, add it
                                            IMDB imdb = IMDB.getInstance();
                                            List<User<?>> users = imdb.getUsers();
                                            for (User<?> user : users) {
                                                if (user.getUsername().equals(username)) {
                                                    List<Series> seriesList = new ArrayList<>();
                                                    for (Object serial: user.getFavorites()) {
                                                        if (serial instanceof Series) {
                                                            seriesList.add((Series) series);
                                                        }
                                                    }
                                                    if (seriesList.contains(series)) {
                                                        seriesList.remove(series);
                                                        JOptionPane.showMessageDialog(favoritesPanel, "Series removed from favorites.");
                                                    } else {
                                                        seriesList.add(series);
                                                        JOptionPane.showMessageDialog(favoritesPanel, "Series added to favorites.");
                                                    }
                                                }
                                            }
                                        } else if (actor != null) {
                                            // add the actor to favorites
                                            // if the actor is already in favorites, delete it
                                            // if the actor is not in favorites, add it
                                            IMDB imdb = IMDB.getInstance();
                                            List<User<?>> users = imdb.getUsers();
                                            for (User<?> user : users) {
                                                if (user.getUsername().equals(username)) {
                                                    List<Actor> actors = new ArrayList<>();
                                                    for (Object act : user.getFavorites()) {
                                                        if (act instanceof Actor) {
                                                            actors.add((Actor) act);
                                                        }
                                                    }
                                                    if (actors.contains(actor)) {
                                                        actors.remove(actor);
                                                        JOptionPane.showMessageDialog(favoritesPanel, "Actor removed from favorites.");
                                                    } else {
                                                        actors.add(actor);
                                                        JOptionPane.showMessageDialog(favoritesPanel, "Actor added to favorites.");
                                                    }
                                                }
                                            }
                                        } else {
                                            JOptionPane.showMessageDialog(favoritesPanel, "Object not found.");
                                        }
                                    }
                                });

                                favoritesFrame.setVisible(true);
                            } else if (action.equals("Add/Delete user")) {
                                // Perform action for "Add/Delete user" button
                                // open a new window with 2 buttons: add user and delete user
                                // when any button is pressed a new window opens with a form to fill
                                // when the form is filled, the user is added/deleted from the system
                                JFrame userFrame = new JFrame("User");
                                userFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                userFrame.setSize(400, 800);

                                JPanel userPanel = new JPanel();
                                userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
                                userFrame.getContentPane().add(userPanel);

                                // add scroll
                                JScrollPane scrollPane = new JScrollPane(userPanel);
                                scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                                userFrame.getContentPane().add(scrollPane);

                                // add buttons
                                JButton addUserButton = new JButton("Add user");
                                userPanel.add(addUserButton);

                                JButton deleteUserButton = new JButton("Delete user");
                                userPanel.add(deleteUserButton);

                                // when the add user button is pressed, a new window opens with a form to fill
                                addUserButton.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        JFrame addUserFrame = new JFrame("Add user");
                                        addUserFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                        addUserFrame.setSize(400, 800);

                                        JPanel addUserPanel = new JPanel();
                                        addUserPanel.setLayout(new BoxLayout(addUserPanel, BoxLayout.Y_AXIS));
                                        addUserFrame.getContentPane().add(addUserPanel);

                                        // add scroll
                                        JScrollPane scrollPane = new JScrollPane(addUserPanel);
                                        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                                        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                                        addUserFrame.getContentPane().add(scrollPane);

                                        // add form
                                        JTextField usernameField = new JTextField(20);
                                        addUserPanel.add(usernameField);

                                        JTextField emailField = new JTextField(20);
                                        addUserPanel.add(emailField);

                                        JTextField passwordField = new JTextField(20);
                                        addUserPanel.add(passwordField);

                                        JTextField nameField = new JTextField(20);
                                        addUserPanel.add(nameField);

                                        JTextField experienceField = new JTextField(20);
                                        addUserPanel.add(experienceField);

                                        // add button
                                        JButton addUserButton = new JButton("Add user");
                                        addUserPanel.add(addUserButton);

                                        // when the add user button is pressed, the user is added to the system
                                        addUserButton.addActionListener(new ActionListener() {
                                            @Override
                                            public void actionPerformed(ActionEvent e) {
                                                String username = usernameField.getText();
                                                String email = emailField.getText();
                                                String password = passwordField.getText();
                                                Credentials credentials = new Credentials(email, password);
                                                String name = nameField.getText();
                                                int experience = Integer.parseInt(experienceField.getText());

                                                InformationBuilder builder = new InformationBuilder();
                                                builder.setName(name);
                                                builder.setCredentials(credentials);
                                                User.Information information = builder.build();

                                                IMDB imdb = IMDB.getInstance();
                                                List<User<?>> users = imdb.getUsers();
                                                for (User user : users) {
                                                    if (user.getUsername().equals(username)) {
                                                        JOptionPane.showMessageDialog(addUserPanel, "Username already exists.");
                                                    } else {
                                                        User currentUser = UserFactory.createUser(AccountType.Regular, information, username, experience, null, null, null, null);
                                                        imdb.users.add(currentUser);
                                                        JOptionPane.showMessageDialog(addUserPanel, "User added.");
                                                    }
                                                }
                                            }
                                        });

                                        addUserFrame.setVisible(true);
                                    }
                                });

                                // when the delete user button is pressed, a new window opens with a form to fill
                                // fill just the username and the system searches for the user and deletes it
                                deleteUserButton.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        JFrame deleteUserFrame = new JFrame("Delete user");
                                        deleteUserFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                        deleteUserFrame.setSize(400, 800);

                                        JPanel deleteUserPanel = new JPanel();
                                        deleteUserPanel.setLayout(new BoxLayout(deleteUserPanel, BoxLayout.Y_AXIS));
                                        deleteUserFrame.getContentPane().add(deleteUserPanel);

                                        // add scroll
                                        JScrollPane scrollPane = new JScrollPane(deleteUserPanel);
                                        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                                        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                                        deleteUserFrame.getContentPane().add(scrollPane);

                                        // add form
                                        JTextField usernameField = new JTextField(20);
                                        deleteUserPanel.add(usernameField);

                                        // add button
                                        JButton deleteUserButton = new JButton("Delete user");
                                        deleteUserPanel.add(deleteUserButton);

                                        // when the delete user button is pressed, the user is deleted from the system
                                        deleteUserButton.addActionListener(new ActionListener() {
                                            @Override
                                            public void actionPerformed(ActionEvent e) {
                                                String username = usernameField.getText();

                                                IMDB imdb = IMDB.getInstance();
                                                List<User<?>> users = imdb.getUsers();
                                                for (User<?> user : users) {
                                                    if (user.getUsername().equals(username)) {
                                                        users.remove(user);
                                                        JOptionPane.showMessageDialog(deleteUserPanel, "User deleted.");
                                                    } else {
                                                        JOptionPane.showMessageDialog(deleteUserPanel, "User not found.");
                                                    }
                                                }
                                            }
                                        });

                                        deleteUserFrame.setVisible(true);
                                    }
                                });

                                userFrame.setVisible(true);
                            } else if (action.equals("Add/Delete actors/movies/series from system")) {
                                // Perform action for "Add/Delete actors/movies/series from system" button
                                JFrame addDeleteFrame = new JFrame("Add/Delete to/from system");
                                addDeleteFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                addDeleteFrame.setSize(400, 800);

                                JPanel addDeletePanel = new JPanel();
                                addDeletePanel.setLayout(new BoxLayout(addDeletePanel, BoxLayout.Y_AXIS));
                                addDeleteFrame.getContentPane().add(addDeletePanel);

                                // add scroll
                                JScrollPane scrollPane = new JScrollPane(addDeletePanel);
                                scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                                addDeleteFrame.getContentPane().add(scrollPane);

                                // add buttons
                                JButton addMovieButton = new JButton("Add movie");
                                addDeletePanel.add(addMovieButton);

                                JButton deleteMovieButton = new JButton("Delete movie");
                                addDeletePanel.add(deleteMovieButton);

                                JButton addSeriesButton = new JButton("Add series");
                                addDeletePanel.add(addSeriesButton);

                                JButton deleteSeriesButton = new JButton("Delete series");
                                addDeletePanel.add(deleteSeriesButton);

                                JButton addActorButton = new JButton("Add actor");
                                addDeletePanel.add(addActorButton);

                                JButton deleteActorButton = new JButton("Delete actor");
                                addDeletePanel.add(deleteActorButton);

                                // when the add movie button is pressed, a new window opens with a form to fill
                                addMovieButton.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        JFrame addMovieFrame = new JFrame("Add movie");
                                        addMovieFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                        addMovieFrame.setSize(400, 800);

                                        JPanel addMoviePanel = new JPanel();
                                        addMoviePanel.setLayout(new BoxLayout(addMoviePanel, BoxLayout.Y_AXIS));
                                        addMovieFrame.getContentPane().add(addMoviePanel);

                                        // add scroll
                                        JScrollPane scrollPane = new JScrollPane(addMoviePanel);
                                        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                                        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                                        addMovieFrame.getContentPane().add(scrollPane);

                                        // add form
                                        JLabel titleLabel = new JLabel("Title:");
                                        addMoviePanel.add(titleLabel);
                                        JTextField titleField = new JTextField(20);
                                        addMoviePanel.add(titleField);

                                        JLabel yearLabel = new JLabel("Year:");
                                        addMoviePanel.add(yearLabel);
                                        JTextField yearField = new JTextField(20);
                                        addMoviePanel.add(yearField);

                                        JLabel ratingLabel = new JLabel("Rating:");
                                        addMoviePanel.add(ratingLabel);
                                        JTextField ratingField = new JTextField(20);
                                        addMoviePanel.add(ratingField);

                                        JLabel durationLabel = new JLabel("Duration:");
                                        addMoviePanel.add(durationLabel);
                                        JTextField durationField = new JTextField(20);
                                        addMoviePanel.add(durationField);

                                        JLabel genresLabel = new JLabel("Genres:");
                                        addMoviePanel.add(genresLabel);
                                        JTextField genresField = new JTextField(20);
                                        addMoviePanel.add(genresField);

                                        JLabel actorsLabel = new JLabel("Actors:");
                                        addMoviePanel.add(actorsLabel);
                                        JTextField actorsField = new JTextField(20);
                                        addMoviePanel.add(actorsField);

                                        JLabel directorsLabel = new JLabel("Directors:");
                                        addMoviePanel.add(directorsLabel);
                                        JTextField directorsField = new JTextField(20);
                                        addMoviePanel.add(directorsField);

                                        JLabel descriptionLabel = new JLabel("Description:");
                                        addMoviePanel.add(descriptionLabel);
                                        JTextField descriptionField = new JTextField(20);
                                        addMoviePanel.add(descriptionField);

                                        // add button
                                        JButton addMovieButton = new JButton("Add movie");
                                        addMoviePanel.add(addMovieButton);

                                        // when the add movie button is pressed, the movie is added to the system
                                        addMovieButton.addActionListener(new ActionListener() {
                                            @Override
                                            public void actionPerformed(ActionEvent e) {
                                                String title = titleField.getText();
                                                int year = Integer.parseInt(yearField.getText());
                                                double rating = Double.parseDouble(ratingField.getText());
                                                String duration = durationField.getText();
                                                List<Genre> genres = new ArrayList<>();
                                                String genresString = genresField.getText();
                                                String[] genresArray = genresString.split(",");
                                                for (String genre : genresArray) {
                                                    genres.add(Genre.valueOf(genre));
                                                }
                                                List<String> actors = new ArrayList<>();
                                                String actorsString = actorsField.getText();
                                                String[] actorsArray = actorsString.split(",");
                                                for (String actor : actorsArray) {
                                                    actors.add(actor);
                                                }
                                                List<String> directors = new ArrayList<>();
                                                String directorsString = directorsField.getText();
                                                String[] directorsArray = directorsString.split(",");
                                                for (String director : directorsArray) {
                                                    directors.add(director);
                                                }
                                                String description = descriptionField.getText();

                                                IMDB imdb = IMDB.getInstance();
                                                List<Movie> movies = imdb.getMovies();
                                                Movie movie = new Movie(title, directors, actors, genres, null, description, rating, duration, year);
                                                imdb.movies.add(movie);
                                                JOptionPane.showMessageDialog(addMoviePanel, "Movie added.");
                                            }
                                        });

                                        // set visible
                                        addMovieFrame.setVisible(true);
                                    }
                                });

                                // set visible
                                addDeleteFrame.setVisible(true);

                                // when the delete movie button is pressed, a search bar appears to search for the movie to delete
                                deleteMovieButton.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        JFrame deleteMovieFrame = new JFrame("Delete movie");
                                        deleteMovieFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                        deleteMovieFrame.setSize(400, 800);

                                        JPanel deleteMoviePanel = new JPanel();
                                        deleteMoviePanel.setLayout(new BoxLayout(deleteMoviePanel, BoxLayout.Y_AXIS));
                                        deleteMovieFrame.getContentPane().add(deleteMoviePanel);

                                        JLabel searchLabel = new JLabel("Search movie to delete:");
                                        deleteMoviePanel.add(searchLabel);

                                        // add scroll
                                        JScrollPane scrollPane = new JScrollPane(deleteMoviePanel);
                                        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                                        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                                        deleteMovieFrame.getContentPane().add(scrollPane);

                                        // add search bar
                                        JTextField searchField = new JTextField(20);
                                        deleteMoviePanel.add(searchField);

                                        // add search button
                                        JButton searchButton = new JButton("Search");
                                        deleteMoviePanel.add(searchButton);

                                        // get the text and search the movie in the system using imdb.searchMovie
                                        // if the movie is found, delete it
                                        searchButton.addActionListener(new ActionListener() {
                                            @Override
                                            public void actionPerformed(ActionEvent e) {
                                                String movieName = searchField.getText();
                                                Movie movie = searchMovie(movieName);
                                                if (movie != null) {
                                                    IMDB imdb = IMDB.getInstance();
                                                    imdb.movies.remove(movie);
                                                    JOptionPane.showMessageDialog(deleteMoviePanel, "Movie deleted.");
                                                } else {
                                                    JOptionPane.showMessageDialog(deleteMoviePanel, "Movie not found.");
                                                }
                                            }
                                        });

                                        deleteMovieFrame.setVisible(true);
                                    }
                                });

                                // set visible
                                addDeleteFrame.setVisible(true);

                                // when the add series button is pressed, a new window opens with a form to fill
                                addSeriesButton.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        JFrame addSeriesFrame = new JFrame("Add series");
                                        addSeriesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                        addSeriesFrame.setSize(400, 800);

                                        JPanel addSeriesPanel = new JPanel();
                                        addSeriesPanel.setLayout(new BoxLayout(addSeriesPanel, BoxLayout.Y_AXIS));
                                        addSeriesFrame.getContentPane().add(addSeriesPanel);

                                        // add scroll
                                        JScrollPane scrollPane = new JScrollPane(addSeriesPanel);
                                        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                                        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                                        addSeriesFrame.getContentPane().add(scrollPane);

                                        // add form
                                        JLabel titleLabel = new JLabel("Title:");
                                        addSeriesPanel.add(titleLabel);
                                        JTextField titleField = new JTextField(20);
                                        addSeriesPanel.add(titleField);

                                        JLabel ratingLabel = new JLabel("Rating:");
                                        addSeriesPanel.add(ratingLabel);
                                        JTextField ratingField = new JTextField(20);
                                        addSeriesPanel.add(ratingField);

                                        JLabel numberOfSeasonsLabel = new JLabel("Number of seasons:");
                                        addSeriesPanel.add(numberOfSeasonsLabel);
                                        JTextField numberOfSeasonsField = new JTextField(20);
                                        addSeriesPanel.add(numberOfSeasonsField);

                                        JLabel genresLabel = new JLabel("Genres:");
                                        addSeriesPanel.add(genresLabel);
                                        JTextField genresField = new JTextField(20);
                                        addSeriesPanel.add(genresField);

                                        JLabel actorsLabel = new JLabel("Actors:");
                                        addSeriesPanel.add(actorsLabel);
                                        JTextField actorsField = new JTextField(20);
                                        addSeriesPanel.add(actorsField);

                                        JLabel directorsLabel = new JLabel("Directors:");
                                        addSeriesPanel.add(directorsLabel);
                                        JTextField directorsField = new JTextField(20);
                                        addSeriesPanel.add(directorsField);

                                        JLabel descriptionLabel = new JLabel("Description:");
                                        addSeriesPanel.add(descriptionLabel);
                                        JTextField descriptionField = new JTextField(20);
                                        addSeriesPanel.add(descriptionField);

                                        JLabel releaseYearLabel = new JLabel("Release year:");
                                        addSeriesPanel.add(releaseYearLabel);
                                        JTextField releaseYearField = new JTextField(20);
                                        addSeriesPanel.add(releaseYearField);


                                        // add button
                                        JButton addSeriesButton = new JButton("Add series");
                                        addSeriesPanel.add(addSeriesButton);

                                        // when the add series button is pressed, the series is added to the system
                                        addSeriesButton.addActionListener(new ActionListener() {
                                            @Override
                                            public void actionPerformed(ActionEvent e) {
                                                String title = titleField.getText();
                                                double rating = Double.parseDouble(ratingField.getText());
                                                int numberOfSeasons = Integer.parseInt(numberOfSeasonsField.getText());
                                                List<Genre> genres = new ArrayList<>();
                                                String genresString = genresField.getText();
                                                String[] genresArray = genresString.split(",");
                                                for (String genre : genresArray) {
                                                    genres.add(Genre.valueOf(genre));
                                                }
                                                List<String> actors = new ArrayList<>();
                                                String actorsString = actorsField.getText();
                                                String[] actorsArray = actorsString.split(",");
                                                for (String actor : actorsArray) {
                                                    actors.add(actor);
                                                }
                                                List<String> directors = new ArrayList<>();
                                                String directorsString = directorsField.getText();
                                                String[] directorsArray = directorsString.split(",");
                                                for (String director : directorsArray) {
                                                    directors.add(director);
                                                }
                                                String description = descriptionField.getText();
                                                int releaseYear = Integer.parseInt(releaseYearField.getText());

                                                IMDB imdb = IMDB.getInstance();
                                                List<Series> series = imdb.getSeries();
                                                Series serie = new Series(title, directors, actors, genres, null, description, rating, releaseYear,numberOfSeasons, null);
                                                imdb.series.add(serie);
                                                JOptionPane.showMessageDialog(addSeriesPanel, "Series added.");
                                            }
                                        });

                                        addSeriesFrame.setVisible(true);
                                    }
                                });

                                // set visible
                                addDeleteFrame.setVisible(true);

                                // when the delete series button is pressed, a search bar appears to search for the series to delete
                                deleteSeriesButton.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        JFrame deleteSeriesFrame = new JFrame("Delete series");
                                        deleteSeriesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                        deleteSeriesFrame.setSize(400, 800);

                                        JPanel deleteSeriesPanel = new JPanel();
                                        deleteSeriesPanel.setLayout(new BoxLayout(deleteSeriesPanel, BoxLayout.Y_AXIS));
                                        deleteSeriesFrame.getContentPane().add(deleteSeriesPanel);

                                        JLabel searchLabel = new JLabel("Search series to delete:");
                                        deleteSeriesPanel.add(searchLabel);

                                        // add scroll
                                        JScrollPane scrollPane = new JScrollPane(deleteSeriesPanel);
                                        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                                        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                                        deleteSeriesFrame.getContentPane().add(scrollPane);

                                        // add search bar
                                        JTextField searchField = new JTextField(20);
                                        deleteSeriesPanel.add(searchField);

                                        // add search button
                                        JButton searchButton = new JButton("Search");
                                        deleteSeriesPanel.add(searchButton);

                                        // get the text and search the series in the system using imdb.searchSeries
                                        // if the series is found, delete it
                                        searchButton.addActionListener(new ActionListener() {
                                            @Override
                                            public void actionPerformed(ActionEvent e) {
                                                String seriesName = searchField.getText();
                                                Series series = searchSeries(seriesName);
                                                if (series != null) {
                                                    IMDB imdb = IMDB.getInstance();
                                                    imdb.series.remove(series);
                                                    JOptionPane.showMessageDialog(deleteSeriesPanel, "Series deleted.");
                                                } else {
                                                    JOptionPane.showMessageDialog(deleteSeriesPanel, "Series not found.");
                                                }
                                            }
                                        });

                                        deleteSeriesFrame.setVisible(true);
                                    }
                                });

                                // set visible
                                addDeleteFrame.setVisible(true);

                                // when the add actor button is pressed, a new window opens with a form to fill
                                addActorButton.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        JFrame addActorFrame = new JFrame("Add actor");
                                        addActorFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                        addActorFrame.setSize(400, 800);

                                        JPanel addActorPanel = new JPanel();
                                        addActorPanel.setLayout(new BoxLayout(addActorPanel, BoxLayout.Y_AXIS));
                                        addActorFrame.getContentPane().add(addActorPanel);

                                        // add scroll
                                        JScrollPane scrollPane = new JScrollPane(addActorPanel);
                                        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                                        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                                        addActorFrame.getContentPane().add(scrollPane);

                                        // add form
                                        JLabel nameLabel = new JLabel("Name:");
                                        addActorPanel.add(nameLabel);
                                        JTextField nameField = new JTextField(20);
                                        addActorPanel.add(nameField);

                                        JLabel performancesLabel = new JLabel("Performances:");
                                        addActorPanel.add(performancesLabel);
                                        JTextField titleField = new JTextField(20);
                                        addActorPanel.add(titleField);
                                        JTextField typeField = new JTextField(20);
                                        addActorPanel.add(typeField);

                                        JLabel biographyLabel = new JLabel("Biography:");
                                        addActorPanel.add(biographyLabel);
                                        JTextField biographyField = new JTextField(20);
                                        addActorPanel.add(biographyField);

                                        // add button
                                        JButton addActorButton = new JButton("Add actor");
                                        addActorPanel.add(addActorButton);

                                        // when the add actor button is pressed, the actor is added to the system
                                        addActorButton.addActionListener(new ActionListener() {
                                            @Override
                                            public void actionPerformed(ActionEvent e) {
                                                String name = nameField.getText();
                                                List<Performance> performances = new ArrayList<>();
                                                List<String> titles = new ArrayList<>();
                                                String titlesString = titleField.getText();
                                                String[] titlesArray = titlesString.split(",");
                                                for (String title : titlesArray) {
                                                    titles.add(title);
                                                }
                                                List<String> types = new ArrayList<>();
                                                String typesString = typeField.getText();
                                                String[] typesArray = typesString.split(",");
                                                for (String type : typesArray) {
                                                    types.add(type);
                                                }
                                                String biography = biographyField.getText();

                                                IMDB imdb = IMDB.getInstance();
                                                List<Actor> actors = imdb.getActors();
                                                Actor actor = new Actor(name, performances, biography, null);
                                                imdb.actors.add(actor);
                                                JOptionPane.showMessageDialog(addActorPanel, "Actor added.");
                                            }
                                        });

                                        addActorFrame.setVisible(true);
                                    }
                                });

                                // set visible
                                addDeleteFrame.setVisible(true);


                                //when the delete actor button is pressed, a search bar appears to search for the actor to delete
                                deleteActorButton.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        JFrame deleteActorFrame = new JFrame("Delete actor");
                                        deleteActorFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                        deleteActorFrame.setSize(400, 800);

                                        JPanel deleteActorPanel = new JPanel();
                                        deleteActorPanel.setLayout(new BoxLayout(deleteActorPanel, BoxLayout.Y_AXIS));
                                        deleteActorFrame.getContentPane().add(deleteActorPanel);

                                        JLabel searchLabel = new JLabel("Search actor to delete:");
                                        deleteActorPanel.add(searchLabel);

                                        // add scroll
                                        JScrollPane scrollPane = new JScrollPane(deleteActorPanel);
                                        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                                        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                                        deleteActorFrame.getContentPane().add(scrollPane);

                                        // add search bar
                                        JTextField searchField = new JTextField(20);
                                        deleteActorPanel.add(searchField);

                                        // add search button
                                        JButton searchButton = new JButton("Search");
                                        deleteActorPanel.add(searchButton);

                                        // get the text and search the actor in the system using imdb.searchActor
                                        // if the actor is found, delete it
                                        searchButton.addActionListener(new ActionListener() {
                                            @Override
                                            public void actionPerformed(ActionEvent e) {
                                                String actorName = searchField.getText();
                                                Actor actor = searchActor(actorName);
                                                if (actor != null) {
                                                    IMDB imdb = IMDB.getInstance();
                                                    imdb.actors.remove(actor);
                                                    JOptionPane.showMessageDialog(deleteActorPanel, "Actor deleted.");
                                                } else {
                                                    JOptionPane.showMessageDialog(deleteActorPanel, "Actor not found.");
                                                }
                                            }
                                        });

                                        deleteActorFrame.setVisible(true);
                                    }
                                });

                                addDeleteFrame.setVisible(true);
                            } else if (action.equals("Update production/actor details")) {
                                // Perform action for "Update production/actor details" button
                                // open a new window with 2 buttons: update production and update actor
                                // when any button is pressed a window opens with all the productions/actors in the system
                                // the user can select the specific production/actor and update it
                                JFrame updateFrame = new JFrame("Update");
                                updateFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                updateFrame.setSize(400, 800);

                                JPanel updatePanel = new JPanel();
                                updatePanel.setLayout(new BoxLayout(updatePanel, BoxLayout.Y_AXIS));
                                updateFrame.getContentPane().add(updatePanel);

                                // add scroll
                                JScrollPane scrollPane = new JScrollPane(updatePanel);
                                scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                                updateFrame.getContentPane().add(scrollPane);

                                // add buttons
                                JButton updateProductionButton = new JButton("Update production");
                                updatePanel.add(updateProductionButton);

                                JButton updateActorButton = new JButton("Update actor");
                                updatePanel.add(updateActorButton);

                                // when the update production button is pressed, a new window opens with all the productions in the system
                                // the user can select the production he wants to update and the window shows him the production's details
                                // the user can update the details and save them
                                updateProductionButton.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        JFrame updateProductionFrame = new JFrame("Update production");
                                        updateProductionFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                        updateProductionFrame.setSize(400, 800);

                                        JPanel updateProductionPanel = new JPanel();
                                        updateProductionPanel.setLayout(new BoxLayout(updateProductionPanel, BoxLayout.Y_AXIS));
                                        updateProductionFrame.getContentPane().add(updateProductionPanel);

                                        // add scroll
                                        JScrollPane scrollPane = new JScrollPane(updateProductionPanel);
                                        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                                        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                                        updateProductionFrame.getContentPane().add(scrollPane);

                                        // add productions to the panel
                                        IMDB imdb = IMDB.getInstance();
                                        List<Movie> movies = imdb.getMovies();
                                        List<Series> series = imdb.getSeries();
                                        for (Movie movie : movies) {
                                            updateProductionPanel.add(new JLabel("Movie: "));
                                            updateProductionPanel.add(new JLabel("Title: " + movie.getTitle()));

                                            // add a update button
                                            JButton updateButton = new JButton("Update");
                                            updateProductionPanel.add(updateButton);

                                            // when the update button is pressed, a new window opens with all details of the movie
                                            // and the user selects what to update
                                            updateButton.addActionListener(new ActionListener() {
                                                @Override
                                                public void actionPerformed(ActionEvent e) {
                                                    JFrame updateMovieFrame = new JFrame("Update movie");
                                                    updateMovieFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                                    updateMovieFrame.setSize(400, 800);

                                                    JPanel updateMoviePanel = new JPanel();
                                                    updateMoviePanel.setLayout(new BoxLayout(updateMoviePanel, BoxLayout.Y_AXIS));
                                                    updateMovieFrame.getContentPane().add(updateMoviePanel);

                                                    // add scroll
                                                    JScrollPane scrollPane = new JScrollPane(updateMoviePanel);
                                                    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                                                    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                                                    updateMovieFrame.getContentPane().add(scrollPane);

                                                    // add form
                                                    JLabel titleLabel = new JLabel("Title:");
                                                    updateMoviePanel.add(titleLabel);
                                                    JTextField titleField = new JTextField(20);
                                                    updateMoviePanel.add(titleField);

                                                    JLabel yearLabel = new JLabel("Year:");
                                                    updateMoviePanel.add(yearLabel);
                                                    JTextField yearField = new JTextField(20);
                                                    updateMoviePanel.add(yearField);

                                                    JLabel ratingLabel = new JLabel("Rating:");
                                                    updateMoviePanel.add(ratingLabel);
                                                    JTextField ratingField = new JTextField(20);
                                                    updateMoviePanel.add(ratingField);

                                                    JLabel durationLabel = new JLabel("Duration:");
                                                    updateMoviePanel.add(durationLabel);
                                                    JTextField durationField = new JTextField(20);
                                                    updateMoviePanel.add(durationField);

                                                    JLabel genresLabel = new JLabel("Genres:");
                                                    updateMoviePanel.add(genresLabel);
                                                    JTextField genresField = new JTextField(20);
                                                    updateMoviePanel.add(genresField);

                                                    JLabel actorsLabel = new JLabel("Actors:");
                                                    updateMoviePanel.add(actorsLabel);
                                                    JTextField actorsField = new JTextField(20);
                                                    updateMoviePanel.add(actorsField);

                                                    JLabel directorsLabel = new JLabel("Directors:");
                                                    updateMoviePanel.add(directorsLabel);
                                                    JTextField directorsField = new JTextField(20);
                                                    updateMoviePanel.add(directorsField);

                                                    JLabel descriptionLabel = new JLabel("Description:");
                                                    updateMoviePanel.add(descriptionLabel);
                                                    JTextField descriptionField = new JTextField(20);
                                                    updateMoviePanel.add(descriptionField);

                                                    // add button
                                                    JButton updateButton = new JButton("Update");
                                                    updateMoviePanel.add(updateButton);

                                                    // when the update button is pressed, the movie is updated
                                                    updateButton.addActionListener(new ActionListener() {
                                                        @Override
                                                        public void actionPerformed(ActionEvent e) {
                                                            String title = titleField.getText();
                                                            int year = Integer.parseInt(yearField.getText());
                                                            double rating = Double.parseDouble(ratingField.getText());
                                                            String duration = durationField.getText();
                                                            List<Genre> genres = new ArrayList<>();
                                                            String genresString = genresField.getText();
                                                            String[] genresArray = genresString.split(",");
                                                            for (String genre : genresArray) {
                                                                genres.add(Genre.valueOf(genre));
                                                            }
                                                            List<String> actors = new ArrayList<>();
                                                            String actorsString = actorsField.getText();
                                                            String[] actorsArray = actorsString.split(",");
                                                            for (String actor : actorsArray) {
                                                                actors.add(actor);
                                                            }
                                                            List<String> directors = new ArrayList<>();
                                                            String directorsString = directorsField.getText();
                                                            String[] directorsArray = directorsString.split(",");
                                                            for (String director : directorsArray) {
                                                                directors.add(director);
                                                            }
                                                            String description = descriptionField.getText();

                                                            movie.setTitle(title);
                                                            movie.setYear(year);
                                                            movie.setRating(rating);
                                                            movie.setDuration(duration);
                                                            movie.setGenres(genres);
                                                            movie.setActors(actors);
                                                            movie.setDirectors(directors);
                                                            movie.setDescription(description);
                                                            movie.setRatings(null);
                                                            JOptionPane.showMessageDialog(updateMoviePanel, "Movie updated.");
                                                        }
                                                    });

                                                    // set visible
                                                    updateMovieFrame.setVisible(true);
                                                }
                                            });

                                            // set visible
                                            updateProductionFrame.setVisible(true);
                                        }

                                        for (Series serie : series) {
                                            updateProductionPanel.add(new JLabel("Series: "));
                                            updateProductionPanel.add(new JLabel("Title: " + serie.getTitle()));

                                            // add a update button
                                            JButton updateButton = new JButton("Update");
                                            updateProductionPanel.add(updateButton);

                                            // when the update button is pressed, a new window opens with all details of the series
                                            // and the user selects what to update
                                            updateButton.addActionListener(new ActionListener() {
                                                @Override
                                                public void actionPerformed(ActionEvent e) {
                                                    JFrame updateSeriesFrame = new JFrame("Update series");
                                                    updateSeriesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                                    updateSeriesFrame.setSize(400, 800);

                                                    JPanel updateSeriesPanel = new JPanel();
                                                    updateSeriesPanel.setLayout(new BoxLayout(updateSeriesPanel, BoxLayout.Y_AXIS));
                                                    updateSeriesFrame.getContentPane().add(updateSeriesPanel);

                                                    // add scroll
                                                    JScrollPane scrollPane = new JScrollPane(updateSeriesPanel);
                                                    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                                                    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                                                    updateSeriesFrame.getContentPane().add(scrollPane);

                                                    // add form
                                                    JLabel titleLabel = new JLabel("Title:");
                                                    updateSeriesPanel.add(titleLabel);
                                                    JTextField titleField = new JTextField(20);
                                                    updateSeriesPanel.add(titleField);

                                                    JLabel ratingLabel = new JLabel("Rating:");
                                                    updateSeriesPanel.add(ratingLabel);
                                                    JTextField ratingField = new JTextField(20);
                                                    updateSeriesPanel.add(ratingField);

                                                    JLabel numberOfSeasonsLabel = new JLabel("Number of seasons:");
                                                    updateSeriesPanel.add(numberOfSeasonsLabel);
                                                    JTextField numberOfSeasonsField = new JTextField(20);
                                                    updateSeriesPanel.add(numberOfSeasonsField);

                                                    JLabel genresLabel = new JLabel("Genres:");
                                                    updateSeriesPanel.add(genresLabel);
                                                    JTextField genresField = new JTextField(20);
                                                    updateSeriesPanel.add(genresField);

                                                    JLabel actorsLabel = new JLabel("Actors:");
                                                    updateSeriesPanel.add(actorsLabel);
                                                    JTextField actorsField = new JTextField(20);
                                                    updateSeriesPanel.add(actorsField);

                                                    JLabel directorsLabel = new JLabel("Directors:");
                                                    updateSeriesPanel.add(directorsLabel);
                                                    JTextField directorsField = new JTextField(20);
                                                    updateSeriesPanel.add(directorsField);

                                                    JLabel descriptionLabel = new JLabel("Description:");
                                                    updateSeriesPanel.add(descriptionLabel);
                                                    JTextField descriptionField = new JTextField(20);
                                                    updateSeriesPanel.add(descriptionField);

                                                    // add button
                                                    JButton updateButton = new JButton("Update");
                                                    updateSeriesPanel.add(updateButton);

                                                    // when the update button is pressed, the series is updated
                                                    updateButton.addActionListener(new ActionListener() {
                                                        @Override
                                                        public void actionPerformed(ActionEvent e) {
                                                            String title = titleField.getText();
                                                            double rating = Double.parseDouble(ratingField.getText());
                                                            int numberOfSeasons = Integer.parseInt(numberOfSeasonsField.getText());
                                                            List<Genre> genres = new ArrayList<>();
                                                            String genresString = genresField.getText();
                                                            String[] genresArray = genresString.split(",");
                                                            for (String genre : genresArray) {
                                                                genres.add(Genre.valueOf(genre));
                                                            }
                                                            List<String> actors = new ArrayList<>();
                                                            String actorsString = actorsField.getText();
                                                            String[] actorsArray = actorsString.split(",");
                                                            for (String actor : actorsArray) {
                                                                actors.add(actor);
                                                            }
                                                            List<String> directors = new ArrayList<>();
                                                            String directorsString = directorsField.getText();
                                                            String[] directorsArray = directorsString.split(",");
                                                            for (String director : directorsArray) {
                                                                directors.add(director);
                                                            }
                                                            String description = descriptionField.getText();

                                                            serie.setTitle(title);
                                                            serie.setRating(rating);
                                                            serie.setNumberOfSeasons(numberOfSeasons);
                                                            serie.setGenres(genres);
                                                            serie.setActors(actors);
                                                            serie.setDirectors(directors);
                                                            serie.setDescription(description);
                                                            serie.setRatings(null);
                                                            JOptionPane.showMessageDialog(updateSeriesPanel, "Series updated.");
                                                        }
                                                    });

                                                    // set visible
                                                    updateSeriesFrame.setVisible(true);
                                                }
                                            });
                                        }
                                    }
                                });

                                // when the update actor button is pressed, a new window opens with all the actors in the system
                                // the user can select the actor he wants to update and the window shows him the actor's details
                                // the user can update the details and save them
                                updateActorButton.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        JFrame updateActorFrame = new JFrame("Update actor");
                                        updateActorFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                        updateActorFrame.setSize(400, 800);

                                        JPanel updateActorPanel = new JPanel();
                                        updateActorPanel.setLayout(new BoxLayout(updateActorPanel, BoxLayout.Y_AXIS));
                                        updateActorFrame.getContentPane().add(updateActorPanel);

                                        // add scroll
                                        JScrollPane scrollPane = new JScrollPane(updateActorPanel);
                                        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                                        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                                        updateActorFrame.getContentPane().add(scrollPane);

                                        // add actors to the panel
                                        IMDB imdb = IMDB.getInstance();
                                        List<Actor> actors = imdb.getActors();
                                        for (Actor actor : actors) {
                                            updateActorPanel.add(new JLabel("Actor:"));
                                            updateActorPanel.add(new JLabel("Name: " + actor.getNume()));

                                            // add a update button
                                            JButton updateButton = new JButton("Update");
                                            updateActorPanel.add(updateButton);

                                            // when the update button is pressed, a new window opens with all details of the actor
                                            // and the user selects what to update
                                            updateButton.addActionListener(new ActionListener() {
                                                @Override
                                                public void actionPerformed(ActionEvent e) {
                                                    JFrame updateActorFrame = new JFrame("Update actor");
                                                    updateActorFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                                    updateActorFrame.setSize(400, 800);

                                                    JPanel updateActorPanel = new JPanel();
                                                    updateActorPanel.setLayout(new BoxLayout(updateActorPanel, BoxLayout.Y_AXIS));
                                                    updateActorFrame.getContentPane().add(updateActorPanel);

                                                    // add scroll
                                                    JScrollPane scrollPane = new JScrollPane(updateActorPanel);
                                                    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                                                    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                                                    updateActorFrame.getContentPane().add(scrollPane);

                                                    // add form
                                                    JLabel nameLabel = new JLabel("Name:");
                                                    updateActorPanel.add(nameLabel);
                                                    JTextField nameField = new JTextField(20);
                                                    updateActorPanel.add(nameField);

                                                    JLabel performancesLabel = new JLabel("Performances:");
                                                    updateActorPanel.add(performancesLabel);
                                                    JTextField titleField = new JTextField(20);
                                                    updateActorPanel.add(titleField);
                                                    JTextField typeField = new JTextField(20);
                                                    updateActorPanel.add(typeField);

                                                    JLabel biographyLabel = new JLabel("Biography:");
                                                    updateActorPanel.add(biographyLabel);
                                                    JTextField biographyField = new JTextField(20);
                                                    updateActorPanel.add(biographyField);

                                                    // add button
                                                    JButton updateButton = new JButton("Update");
                                                    updateActorPanel.add(updateButton);

                                                    // when the update button is pressed, the actor is updated
                                                    updateButton.addActionListener(new ActionListener() {
                                                        @Override
                                                        public void actionPerformed(ActionEvent e) {
                                                            String name = nameField.getText();
                                                            List<Performance> performances = new ArrayList<>();
                                                            List<String> titles = new ArrayList<>();
                                                            String titlesString = titleField.getText();
                                                            String[] titlesArray = titlesString.split(",");
                                                            for (String title : titlesArray) {
                                                                titles.add(title);
                                                            }
                                                            List<String> types = new ArrayList<>();
                                                            String typesString = typeField.getText();
                                                            String[] typesArray = typesString.split(",");
                                                            for (String type : typesArray) {
                                                                types.add(type);
                                                            }
                                                            for (int i = 0; i < titles.size(); i++) {
                                                                performances.add(new Performance(titles.get(i), types.get(i)));
                                                            }
                                                            String biography = biographyField.getText();

                                                            actor.setNume(name);
                                                            actor.setPerformances(performances);
                                                            actor.setBiografie(biography);
                                                            JOptionPane.showMessageDialog(updateActorPanel, "Actor updated.");
                                                        }
                                                    });

                                                    // set visible
                                                    updateActorFrame.setVisible(true);
                                                }
                                            });
                                        }
                                    }
                                });

                                // set visible
                                updateFrame.setVisible(true);
                            } else if (action.equals("Create/Delete a request")) {
                                // Perform action for "Create/Delete a request" button
                                // open a new window with 2 buttons: add user and delete request
                                // when any button is pressed a new window opens with a form to fill
                                // when the form is filled, the request is added/deleted from the system
                                JFrame requestFrame = new JFrame("Request");
                                requestFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                requestFrame.setSize(400, 800);

                                JPanel requestPanel = new JPanel();
                                requestPanel.setLayout(new BoxLayout(requestPanel, BoxLayout.Y_AXIS));
                                requestFrame.getContentPane().add(requestPanel);

                                // add scroll
                                JScrollPane scrollPane = new JScrollPane(requestPanel);
                                scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                                requestFrame.getContentPane().add(scrollPane);

                                // add buttons
                                JButton addRequestButton = new JButton("Add request");
                                requestPanel.add(addRequestButton);

                                JButton deleteRequestButton = new JButton("Delete request");
                                requestPanel.add(deleteRequestButton);

                                // when the add request button is pressed, a new window opens with a form to fill
                                // the user must select the type from a list :     DELETE_ACCOUNT, ACTOR_ISSUE, MOVIE_ISSUE, OTHERS
                                // if the type is actor/movie issue, the user must insert the name of the actor/movie                                // the user must insert the name of the user to address the request to
                                // the user must insert a description
                                addRequestButton.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        JFrame addRequestFrame = new JFrame("Add request");
                                        addRequestFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                        addRequestFrame.setSize(400, 800);

                                        JPanel addRequestPanel = new JPanel();
                                        addRequestPanel.setLayout(new BoxLayout(addRequestPanel, BoxLayout.Y_AXIS));
                                        addRequestFrame.getContentPane().add(addRequestPanel);

                                        // add scroll
                                        JScrollPane scrollPane = new JScrollPane(addRequestPanel);
                                        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                                        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                                        addRequestFrame.getContentPane().add(scrollPane);

                                        // add form
                                        // add a name to the lable so the user knows what to search
                                        JLabel searchLabel = new JLabel("Request type:");
                                        addRequestPanel.add(searchLabel);
                                        JTextField typeField = new JTextField(20);
                                        addRequestPanel.add(typeField);

                                        JLabel nameLabel = new JLabel("Name of the movie or actor:");
                                        addRequestPanel.add(nameLabel);
                                        JTextField nameField = new JTextField(20);
                                        addRequestPanel.add(nameField);

                                        JLabel descriptionLabel = new JLabel("Description:");
                                        addRequestPanel.add(descriptionLabel);
                                        JTextField descriptionField = new JTextField(20);
                                        addRequestPanel.add(descriptionField);

                                        // add button
                                        JButton addRequestButton = new JButton("Add request");
                                        addRequestPanel.add(addRequestButton);

                                        // when the add request button is pressed, the request is added to the system
                                        addRequestButton.addActionListener(new ActionListener() {
                                            @Override
                                            public void actionPerformed(ActionEvent e) {
                                                String type = typeField.getText();
                                                String name = nameField.getText();
                                                String description = descriptionField.getText();

                                                IMDB imdb = IMDB.getInstance();

                                                if (type.equals("DELETE_ACCOUNT")) {
                                                    Request request = new Request(RequestTypes.DELETE_ACCOUNT, LocalDateTime.now(), username, name, description, null);
                                                    imdb.requests.add(request);
                                                } else if (type.equals("ACTOR_ISSUE")) {
                                                    Request request = new Request(RequestTypes.ACTOR_ISSUE, LocalDateTime.now(), username, name, description, null);
                                                    imdb.requests.add(request);
                                                } else if (type.equals("MOVIE_ISSUE")) {
                                                    Request request = new Request(RequestTypes.MOVIE_ISSUE, LocalDateTime.now(), username, name, description, null);
                                                    imdb.requests.add(request);
                                                } else if (type.equals("OTHERS")) {
                                                    Request request = new Request(RequestTypes.OTHERS, LocalDateTime.now(), username, name, description, null);
                                                    imdb.requests.add(request);
                                                }
                                                JOptionPane.showMessageDialog(addRequestPanel, "Request added.");
                                            }
                                        });

                                        addRequestFrame.setVisible(true);
                                    }
                                });

                                // when the delete request button is pressed, a new window opens with a form to
                                // search the username that created the request and delete it
                                deleteRequestButton.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        JFrame deleteRequestFrame = new JFrame("Delete request");
                                        deleteRequestFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                        deleteRequestFrame.setSize(400, 800);

                                        JPanel deleteRequestPanel = new JPanel();
                                        deleteRequestPanel.setLayout(new BoxLayout(deleteRequestPanel, BoxLayout.Y_AXIS));
                                        deleteRequestFrame.getContentPane().add(deleteRequestPanel);

                                        // add scroll
                                        JScrollPane scrollPane = new JScrollPane(deleteRequestPanel);
                                        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                                        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                                        deleteRequestFrame.getContentPane().add(scrollPane);

                                        // add form
                                        JLabel usernameLabel = new JLabel("Username that created the request:");
                                        deleteRequestPanel.add(usernameLabel);
                                        JTextField usernameField = new JTextField(20);
                                        deleteRequestPanel.add(usernameField);

                                        // add button
                                        JButton deleteRequestButton = new JButton("Delete request");
                                        deleteRequestPanel.add(deleteRequestButton);

                                        // when the delete request button is pressed, the request is deleted from the system
                                        deleteRequestButton.addActionListener(new ActionListener() {
                                            @Override
                                            public void actionPerformed(ActionEvent e) {
                                                String username = usernameField.getText();

                                                IMDB imdb = IMDB.getInstance();
                                                List<Request> requests = imdb.getRequests();
                                                for (Request request : requests) {
                                                    if (request.getUsername().equals(username)) {
                                                        requests.remove(request);
                                                        JOptionPane.showMessageDialog(deleteRequestPanel, "Request deleted.");
                                                    } else {
                                                        JOptionPane.showMessageDialog(deleteRequestPanel, "Request not found.");
                                                    }
                                                }
                                            }
                                        });

                                        deleteRequestFrame.setVisible(true);
                                    }
                                });

                                requestFrame.setVisible(true);
                            } else if (action.equals("Solve a request")) {
                                // Perform action for "Solve a request" button
                                // if the user is a contributor, the system searches requests for him and he can resolve them
                                // for every request the user can press a button to solve it and then
                                // the request is deleted from the system and the user gets a notification tab message
                                // if the user is an admin, he can see all the requests for all admins and resolve them
                                // the admin can also delete the request from the system
                                JFrame solveRequestFrame = new JFrame("Solve request");
                                solveRequestFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                solveRequestFrame.setSize(400, 800);

                                JPanel solveRequestPanel = new JPanel();
                                solveRequestPanel.setLayout(new BoxLayout(solveRequestPanel, BoxLayout.Y_AXIS));
                                solveRequestFrame.getContentPane().add(solveRequestPanel);

                                // add scroll
                                JScrollPane scrollPane = new JScrollPane(solveRequestPanel);
                                scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                                solveRequestFrame.getContentPane().add(scrollPane);

                                IMDB imdb = IMDB.getInstance();
                                List<Request> requests = imdb.getRequests();
                                for (Request request : requests) {
                                    if (currentuser.getAccountType() == AccountType.Contributor) {
                                        if (request.getUsername().equals(username)) {
                                            solveRequestPanel.add(new JLabel("Request:"));
                                            solveRequestPanel.add(new JLabel("Type: " + request.getRequestType()));
                                            solveRequestPanel.add(new JLabel("Username: " + request.getUsername()));
                                            solveRequestPanel.add(new JLabel("Name of actor / movie: " + request.getTitle()));
                                            solveRequestPanel.add(new JLabel("To: " + request.getUsernameToResolve()));
                                            solveRequestPanel.add(new JLabel("Description: " + request.getDescription()));

                                            JButton solveButton = new JButton("Solve");
                                            solveRequestPanel.add(solveButton);

                                            solveButton.addActionListener(new ActionListener() {
                                                @Override
                                                public void actionPerformed(ActionEvent e) {
                                                    requests.remove(request);
                                                    JOptionPane.showMessageDialog(solveRequestPanel, "Request solved.");
                                                }
                                            });
                                        }
                                    } else if (currentuser.getAccountType() == AccountType.Admin) {
                                        solveRequestPanel.add(new JLabel("Request:"));
                                        solveRequestPanel.add(new JLabel("Type: " + request.getRequestType()));
                                        solveRequestPanel.add(new JLabel("Username: " + request.getUsername()));
                                        solveRequestPanel.add(new JLabel("Name of the movie / actor: " + request.getTitle()));
                                        solveRequestPanel.add(new JLabel("To: " + request.getUsernameToResolve()));
                                        solveRequestPanel.add(new JLabel("Description: " + request.getDescription()));

                                        JButton solveButton = new JButton("Solve");
                                        solveRequestPanel.add(solveButton);

                                        JButton deleteButton = new JButton("Delete");
                                        solveRequestPanel.add(deleteButton);

                                        solveButton.addActionListener(new ActionListener() {
                                            @Override
                                            public void actionPerformed(ActionEvent e) {
                                                imdb.requests.remove(request);
                                                JOptionPane.showMessageDialog(solveRequestPanel, "Request solved.");
                                            }
                                        });

                                        deleteButton.addActionListener(new ActionListener() {
                                            @Override
                                            public void actionPerformed(ActionEvent e) {
                                                imdb.requests.remove(request);
                                                JOptionPane.showMessageDialog(solveRequestPanel, "Request deleted.");
                                            }
                                        });
                                    }
                                }

                                solveRequestFrame.setVisible(true);


                            } else if (action.equals("Add/Remove a rating to/from a production")) {
                                // Perform action for "Add/Remove a rating to/from a production" button
                                // open a new window with a form to fill
                                // the user must select the type from a list :     MOVIE, SERIES
                                // the user must insert the name of the production
                                // the user must insert the rating and feedack
                                JFrame ratingFrame = new JFrame("Rating");
                                ratingFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                ratingFrame.setSize(400, 800);

                                JPanel ratingPanel = new JPanel();
                                ratingPanel.setLayout(new BoxLayout(ratingPanel, BoxLayout.Y_AXIS));
                                ratingFrame.getContentPane().add(ratingPanel);

                                // add scroll
                                JScrollPane scrollPane = new JScrollPane(ratingPanel);
                                scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                                ratingFrame.getContentPane().add(scrollPane);

                                // seach production
                                // add a name to the lable so the user knows what to search
                                JLabel searchLabel = new JLabel("Search production:");
                                ratingPanel.add(searchLabel);

                                JTextField searchField = new JTextField(20);
                                ratingPanel.add(searchField);

                                // add 2 search buttons one for movies and one for series
                                JButton searchButtonMovie = new JButton("Search movie");
                                ratingPanel.add(searchButtonMovie);

                                JButton searchButtonSeries = new JButton("Search series");
                                ratingPanel.add(searchButtonSeries);

                                // if the movie btton is pressed, search for the movie and add the rating
                                searchButtonMovie.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        String movieName = searchField.getText();
                                        Movie movie = searchMovie(movieName);
                                        if (movie != null) {
                                            // add the rating to the movie
                                            // if the movie already has a rating from the user, delete it
                                            // if the movie doesn't have a rating from the user, add it
                                            IMDB imdb = IMDB.getInstance();
                                            List<Movie> movies = imdb.getMovies();
                                            int gata = 0;
                                            for (Movie mov : movies) {
                                                if (mov.getTitle().equals(movie.getTitle())) {
                                                    List<Rating> ratings = new ArrayList<>();
                                                    for (Rating recenzie : mov.getRatings()) {
                                                        if (recenzie.getUsername().equals(username)) {
                                                            ratings.remove(recenzie);
                                                            JOptionPane.showMessageDialog(ratingPanel, "Rating removed.");
                                                            gata = 1;
                                                        }
                                                    }
                                                    if (gata == 0) {
                                                        // open a new tab with a form to fill
                                                        // the user must insert the rating and feedback
                                                        JFrame ratingFrame = new JFrame("Rating");
                                                        ratingFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                                        ratingFrame.setSize(400, 800);

                                                        JPanel ratingPanel = new JPanel();
                                                        ratingPanel.setLayout(new BoxLayout(ratingPanel, BoxLayout.Y_AXIS));
                                                        ratingFrame.getContentPane().add(ratingPanel);

                                                        // add form
                                                        JLabel ratingLabel = new JLabel("Add rating:");
                                                        ratingPanel.add(ratingLabel);
                                                        JTextField ratingField = new JTextField(20);
                                                        ratingPanel.add(ratingField);

                                                        JLabel feedbackLabel = new JLabel("Add feedback:");
                                                        ratingPanel.add(feedbackLabel);
                                                        JTextField feedbackField = new JTextField(20);
                                                        ratingPanel.add(feedbackField);

                                                        // add button
                                                        JButton addRatingButton = new JButton("Add rating");
                                                        ratingPanel.add(addRatingButton);

                                                        // when the add rating button is pressed, the rating is added to the system
                                                        addRatingButton.addActionListener(new ActionListener() {
                                                            @Override
                                                            public void actionPerformed(ActionEvent e) {
                                                                int ratingint = Integer.parseInt(ratingField.getText());
                                                                String feedback = feedbackField.getText();

                                                                IMDB imdb = IMDB.getInstance();
                                                                List<Movie> movies = imdb.getMovies();
                                                                for (Movie mov : movies) {
                                                                    if (mov.getTitle().equals(movie.getTitle())) {
                                                                        mov.addRating(new Rating(username, ratingint, feedback, AccountType.Regular));
                                                                        JOptionPane.showMessageDialog(ratingPanel, "Rating added.");
                                                                    }
                                                                }
                                                            }
                                                        });

                                                        // set visible
                                                        ratingFrame.setVisible(true);
                                                    }
                                                }
                                            }
                                        } else {
                                            JOptionPane.showMessageDialog(ratingPanel, "Movie not found.");
                                        }
                                    }
                                });

                                // if the series button is pressed, search for the series and add the rating
                                searchButtonSeries.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        String seriesName = searchField.getText();
                                        Series series = searchSeries(seriesName);
                                        if (series != null) {
                                            // add the rating to the series
                                            // if the series already has a rating from the user, delete it
                                            // if the series doesn't have a rating from the user, add it
                                            IMDB imdb = IMDB.getInstance();
                                            List<Series> seriesList = imdb.getSeries();
                                            int gata = 0;
                                            for (Series serie : seriesList) {
                                                if (serie.getTitle().equals(series.getTitle())) {
                                                    List<Rating> ratings = new ArrayList<>();
                                                    for (Rating recenzie : serie.getRatings()) {
                                                        if (recenzie.getUsername().equals(username)) {
                                                            ratings.remove(recenzie);
                                                            JOptionPane.showMessageDialog(ratingPanel, "Rating removed.");
                                                            gata = 1;
                                                        }
                                                    }
                                                    if (gata == 0) {
                                                        // open a new tab with a form to fill
                                                        // the user must insert the rating and feedback
                                                        JFrame ratingFrame = new JFrame("Rating");
                                                        ratingFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                                        ratingFrame.setSize(400, 800);

                                                        JPanel ratingPanel = new JPanel();
                                                        ratingPanel.setLayout(new BoxLayout(ratingPanel, BoxLayout.Y_AXIS));
                                                        ratingFrame.getContentPane().add(ratingPanel);

                                                        // add form
                                                        JLabel ratingLabel = new JLabel("Add rating:");
                                                        ratingPanel.add(ratingLabel);
                                                        JTextField ratingField = new JTextField(20);
                                                        ratingPanel.add(ratingField);

                                                        JLabel feedbackLabel = new JLabel("Add feedback:");
                                                        ratingPanel.add(feedbackLabel);
                                                        JTextField feedbackField = new JTextField(20);
                                                        ratingPanel.add(feedbackField);

                                                        // add button
                                                        JButton addRatingButton = new JButton("Add rating");
                                                        ratingPanel.add(addRatingButton);

                                                        // when the add rating button is pressed, the rating is added to the system
                                                        addRatingButton.addActionListener(new ActionListener() {
                                                            @Override
                                                            public void actionPerformed(ActionEvent e) {
                                                                int ratingint = Integer.parseInt(ratingField.getText());
                                                                String feedback = feedbackField.getText();

                                                                IMDB imdb = IMDB.getInstance();
                                                                List<Series> seriesList = imdb.getSeries();
                                                                for (Series serie : seriesList) {
                                                                    if (serie.getTitle().equals(series.getTitle())) {
                                                                        serie.addRating(new Rating(username, ratingint, feedback, AccountType.Regular));
                                                                        JOptionPane.showMessageDialog(ratingPanel, "Rating added.");
                                                                    }
                                                                }
                                                            }
                                                        });

                                                        // set visible
                                                        ratingFrame.setVisible(true);
                                                    }
                                                }
                                            }
                                        } else {
                                            JOptionPane.showMessageDialog(ratingPanel, "Series not found.");
                                        }
                                    }
                                });

                                ratingFrame.setVisible(true);
                            } else if (action.equals("Logout")) {
                                // Perform action for "Logout" button
                                mainFrame.dispose();
                                menuFrame.dispose();
                                IMDB imdb1 = IMDB.getInstance();
                                imdb1.writeRequestsjson("POO-Tema-2023-checker/POO-Tema-2023-checker/src/main/java/org/example/requests.json");
                                imdb1.writeActorsjson("POO-Tema-2023-checker/POO-Tema-2023-checker/src/main/java/org/example/actors.json");
                                imdb1.writeMoviesjson("POO-Tema-2023-checker/POO-Tema-2023-checker/src/main/java/org/example/movies.json");
                                imdb1.writeSeriesjson("POO-Tema-2023-checker/POO-Tema-2023-checker/src/main/java/org/example/series.json");
                                imdb1.writeUsersjson("POO-Tema-2023-checker/POO-Tema-2023-checker/src/main/java/org/example/users.json");
                            }
                        }
                    });
                    menuPanel.add(button);
                }

                menuFrame.setVisible(true);
            }
        });
    }

    // create a function that saves the text from the search bar and returns the result
    public static Movie searchMovie(String movieName) {
        IMDB imdb = IMDB.getInstance();
        return imdb.searchMovie(movieName);
    }

    // create a function that saves the text from the search bar and returns the result
    public static Series searchSeries(String seriesName) {
        IMDB imdb = IMDB.getInstance();
        return imdb.searchSeries(seriesName);
    }

    // create a function that saves the text from the search bar and returns the result
    public static Actor searchActor(String actorName) {
        IMDB imdb = IMDB.getInstance();
        return imdb.searchActor(actorName);
    }

    // create recommendations
    public static void createRecommendations(String username, JTextArea recommendationsTextArea) {
        IMDB imdb = IMDB.getInstance();
        List<Movie> movies = imdb.getMovies();
        List<Series> series = imdb.getSeries();
        List<User<?>> users = imdb.getUsers();
        StringBuilder recommendationText = new StringBuilder();

        for (User<?> user : users) {
            if (user.getUsername().equals(username)) {
                recommendationText.append("Movie recommendations:\n");
                for (Movie movie : movies) {
                    if (movie.getRating() >= 9 && movie.getYear() >= 2010 && movie.getYear() <= 2020) {
                        recommendationText.append(movie.getTitle()).append("\n");
                    }
                }
                recommendationText.append("Series recommendations:\n");
                for (Series serie : series) {
                    if (serie.getNumberOfSeasons() > 3 && serie.getRating() >= 9) {
                        recommendationText.append(serie.getTitle()).append("\n");
                    }
                }
            }
        }

        recommendationsTextArea.setText(recommendationText.toString());
    }
    public static User<?> authenticateUser(String email, String password) {
        IMDB imdb = IMDB.getInstance();
        return imdb.authenticateUserswing(email, password);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
