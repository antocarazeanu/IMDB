import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        // initializare IMDB
        IMDB imdb = IMDB.getInstance();
        imdb.run();
        //imdb.printAccounts();

        // incepere functionalitate aplicatie
        System.out.println("App has started!");
        System.out.println("Type the use mode of the app:");

        Scanner citiremod = new Scanner(System.in);
        String modfolosire = "";
        boolean validInput = false;

        while (!validInput) {
            System.out.println("How do you want to use the app? (Terminal / Swing):");
            modfolosire = citiremod.nextLine();

            if (modfolosire.equals("Terminal") || modfolosire.equals("Swing")) {
                validInput = true;
            } else {
                System.out.println("Error: Invalid input.");
            }
        }

        // creare lista de actiuni
        List<String> actions = new ArrayList<>();

        // folosire terminal
        if (modfolosire.equals("Terminal")) {
            User currentuser= imdb.authenticateUser();

            boolean loggedIn = true;

            while (loggedIn) {
                actions.add("View production details");
                actions.add("View actors details");
                actions.add("View notifications");
                actions.add("Search for actors/movies/series");
                actions.add("Add/Delete actors/movies/series to/from favorites");
                if (currentuser.getAccountType() == AccountType.Admin) {
                    actions.add("Add/Delete user");
                }
                // actions.add("Add/Delete user");
                if (currentuser.getAccountType() == AccountType.Admin || currentuser.getAccountType() == AccountType.Contributor) {
                    actions.add("Add/Delete actors/movies/series from system");
                    actions.add("Update production/actor details");
                    actions.add("Solve a request");
                }
                // actions.add("Add/Delete actors/movies/series from system");
                // actions.add("Update production/actor details");
                if (currentuser.getAccountType() == AccountType.Regular || currentuser.getAccountType() == AccountType.Contributor) {
                    actions.add("Create/Delete a request");
                }
                //actions.add("Create/Delete a request");
                // actions.add("Solve a request");
                if (currentuser.getAccountType() == AccountType.Regular) {
                    actions.add("Add/Remove a rating to/from a production");
                    actions.add("Add/Remove a rating to/from an actor");
                }
                // actions.add("Add/Remove a rating to/from a production");
                actions.add("Logout");

                System.out.println("Choose action: ");
                for (int i = 0; i < actions.size(); i++) {
                    System.out.println((i + 1) + ") " + actions.get(i));
                }

                Scanner actiune = new Scanner(System.in);
                int actionIndex = actiune.nextInt();

                if (actionIndex >= 1 && actionIndex <= actions.size()) {
                    String selectedAction = actions.get(actionIndex - 1);
                    System.out.println("You selected: " + selectedAction);
                    // Perform the corresponding action based on selectedAction
                    switch (selectedAction) {
                        case "View production details":
                            System.out.println("Do you want to sort the production by genre or by rating? (genre/rating/no)");
                            Scanner raspuns = new Scanner(System.in);
                            String raspunsfin = raspuns.nextLine();
                            if (raspunsfin.equals("genre")) {
                                System.out.println("What genre do you want to sort by? (Action, Adventure, Biography, Comedy, Crime, Cooking, Drama, Fantasy, Horror, Mystery, Romance, SF, Thriller, War, Western");
                                Scanner raspuns1 = new Scanner(System.in);
                                String raspunsfin1 = raspuns1.nextLine();
                                Genre genre = Genre.valueOf(raspunsfin1);
                                System.out.println(genre);
                                imdb.sortbyGenre(genre);
                                System.out.println("Movies available:");
                                System.out.println();
                                imdb.printMovie();
                                System.out.println("Series available:");
                                System.out.println();
                                imdb.printSeries();
                            } else if (raspunsfin.equals("rating")) {
                                imdb.sortbyRatings();
                                System.out.println("Movies available:");
                                System.out.println();
                                imdb.printMovie();
                                System.out.println("Series available:");
                                System.out.println();
                                imdb.printSeries();
                            } else if (raspunsfin.equals("no")) {
                                System.out.println("Movies available:");
                                System.out.println();
                                imdb.printMovie();
                                System.out.println("Series available:");
                                System.out.println();
                                imdb.printSeries();
                            }else {
                                throw new IllegalArgumentException("Invalid search type.");
                            }
                            actions.removeAll(actions);
                            break;
                        case "View actors details":
                            System.out.println("Do you want to sort the actors by name or by rating? (name/no)");
                            Scanner raspuns2 = new Scanner(System.in);
                            String raspunsfin2 = raspuns2.nextLine();
                            if (raspunsfin2.equals("name")) {
                                imdb.sortbyName();
                                System.out.println("Actori disponibili:");
                                System.out.println("/n");
                                imdb.printActors();
                            } else if (raspunsfin2.equals("no")) {
                                System.out.println("Actori disponibili:");
                                System.out.println("/n");
                                imdb.printActors();
                            } else {
                                throw new IllegalArgumentException("Invalid search type.");
                            }
                            actions.removeAll(actions);
                            break;
                        case "View notifications":
                            imdb.viewNotifications(currentuser.getUsername());
                            actions.removeAll(actions);
                            break;
                        case "Search for actors/movies/series":
                            Scanner tip = new Scanner(System.in);
                            Scanner nume = new Scanner(System.in);
                            System.out.println("What do you want to search? (actor/movie/series)");
                            String tipcautat = tip.nextLine();
                            if (tipcautat.equals("actor")) {
                                System.out.println("Insert the name of the actor:");
                                String numecautat = nume.nextLine();
                                Actor actorcautat = imdb.searchActor(numecautat);
                                if(actorcautat == null) {
                                    throw new IllegalArgumentException("The actor is not in the system");
                                }
                            } else if (tipcautat.equals("movie")) {
                                System.out.println("Insert the name of the movie:");
                                String numecautat = nume.nextLine();
                                Production filmcautat = imdb.searchMovie(numecautat);
                                if(filmcautat == null) {
                                    throw new IllegalArgumentException("The movie is not in the system");
                                }
                            } else if (tipcautat.equals("series")) {
                                System.out.println("Insert the name of the series:");
                                String numecautat = nume.nextLine();
                                Series serialcautat = imdb.searchSeries(numecautat);
                                if(serialcautat == null) {
                                    throw new IllegalArgumentException("The series is not in the system");
                                }
                            } else {
                                throw new IllegalArgumentException("Invalid search type.");
                            }
                            actions.removeAll(actions);
                            break;
                    case "Add/Delete actors/movies/series to/from favorites":
                        System.out.println("Would you like to add or to delete to/from favorites? (add/delete)");
                        Scanner raspuns_add_del = new Scanner(System.in);
                        String raspuns_add_del_fin = raspuns_add_del.nextLine();
                        currentuser.printFavorites();
                        if(raspuns_add_del_fin.equals("add")){
                            System.out.println("What would you like to add? (actor/movie/series)");
                            Scanner raspuns_obj_add = new Scanner(System.in);
                            String raspuns_obj_add_fin = raspuns_obj_add.nextLine();
                            if (raspuns_obj_add_fin.equals("actor")) {
                                System.out.println("Insert the name of the actor:");
                                Scanner nume_actor = new Scanner(System.in);
                                String nume_actor_fin = nume_actor.nextLine();
                                Actor actor = imdb.searchActor(nume_actor_fin);
                                if (actor != null) {
                                    boolean exista = currentuser.searchFavorites(nume_actor_fin);
                                    if (exista == true) {
                                        throw new IllegalArgumentException("The actor is already in favorites");
                                    }
                                    else {
                                        currentuser.addFavorite(actor);
                                        currentuser.printFavorites();
                                    }
                                }
                                else {
                                    throw new IllegalArgumentException("The actor is not in the system");
                                }

                            } else if (raspuns_obj_add_fin.equals("movie")) {
                                System.out.println("Insert the name of the movie:");
                                Scanner nume_movie = new Scanner(System.in);
                                String nume_movie_fin = nume_movie.nextLine();
                                Movie movie = imdb.searchMovie(nume_movie_fin);
                                if(movie != null) {
                                    boolean exista = currentuser.searchFavorites(nume_movie_fin);
                                    if (exista == true) {
                                        throw new IllegalArgumentException("The movie is already in favorites");
                                    }
                                    else {
                                        currentuser.addFavorite(movie);
                                        currentuser.printFavorites();
                                    }
                                }
                                else {
                                    throw new IllegalArgumentException("The movie is not in the system");
                                }
                            } else if (raspuns_obj_add_fin.equals("series")) {
                                System.out.println("Insert the name of the series:");
                                Scanner nume_series = new Scanner(System.in);

                                String nume_series_fin = nume_series.nextLine();
                                Series series = imdb.searchSeries(nume_series_fin);
                                if (series != null) {
                                    boolean exista = currentuser.searchFavorites(nume_series_fin);
                                    if (exista == true) {
                                        throw new IllegalArgumentException("The series is already in favorites");
                                    }
                                    else {
                                        currentuser.addFavorite(series);
                                        currentuser.printFavorites();
                                    }
                                }
                                else {
                                    throw new IllegalArgumentException("The series is not in the system");
                                }
                            } else {
                                throw new IllegalArgumentException("Invalid search type.");
                            }
                        }
                        else if(raspuns_add_del_fin.equals("delete")){
                            System.out.println("What would you like to delete? (actor/movie/series)");
                            Scanner raspuns_obj_del = new Scanner(System.in);
                            String raspuns_obj_del_fin = raspuns_obj_del.nextLine();
                            if (raspuns_obj_del_fin.equals("actor")) {
                                System.out.println("Insert the name of the actor:");
                                Scanner nume_actor = new Scanner(System.in);
                                String nume_actor_fin = nume_actor.nextLine();
                                Actor actor = imdb.searchActor(nume_actor_fin);
                                if (actor != null) {
                                    boolean exista = currentuser.searchFavorites(nume_actor_fin);
                                    if (exista == false) {
                                        throw new IllegalArgumentException("The actor is not in favorites");
                                    }
                                    else {
                                        currentuser.deleteFavorite(actor);
                                        currentuser.printFavorites();
                                    }
                                }
                                else {
                                    throw new IllegalArgumentException("The actor is not in the system");
                                }

                            } else if (raspuns_obj_del_fin.equals("movie")) {
                                System.out.println("Insert the name of the movie:");
                                Scanner nume_movie = new Scanner(System.in);
                                String nume_movie_fin = nume_movie.nextLine();
                                Movie movie = imdb.searchMovie(nume_movie_fin);
                                if(movie != null) {
                                    boolean exista = currentuser.searchFavorites(nume_movie_fin);
                                    if (exista == false) {
                                        throw new IllegalArgumentException("The movie is not in favorites");
                                    }
                                    else {
                                        currentuser.deleteFavorite(movie);
                                        currentuser.printFavorites();
                                    }
                                }
                                else {
                                    throw new IllegalArgumentException("The movie is not in the system");
                                }
                            } else if (raspuns_obj_del_fin.equals("series")) {
                                System.out.println("Insert the name of the series:");
                                Scanner nume_series = new Scanner(System.in);

                                String nume_series_fin = nume_series.nextLine();
                                Series series = imdb.searchSeries(nume_series_fin);
                                if (series != null) {
                                    boolean exista = currentuser.searchFavorites(nume_series_fin);
                                    if (exista == false) {
                                        throw new IllegalArgumentException("The series is not in favorites");
                                    }
                                    else {
                                        currentuser.deleteFavorite(series);
                                        currentuser.printFavorites();
                                    }
                                }
                                else {
                                    throw new IllegalArgumentException("The series is not in the system");
                                }
                            } else {
                                throw new IllegalArgumentException("Invalid search type");
                            }
                        }
                        else{
                            System.out.println("Invalid answer.");
                        }
                        actions.removeAll(actions);
                        break;
                    case "Add/Delete user":
                        System.out.println(currentuser.getAccountType());
                        if (currentuser.getAccountType() == AccountType.Regular || currentuser.getAccountType() == AccountType.Contributor) {
                            // throw exception
                            throw new IllegalArgumentException("You don't have permission to add or delete users.");
                        } else{
                            System.out.println("1. Add user");
                            System.out.println("2. Delete user");
                            Scanner scanner = new Scanner(System.in);
                            int option = scanner.nextInt();
                            if (option == 1) {
                                imdb.addUser();
                                System.out.println("User added successfully.");
                                imdb.printAccounts();
                            } else if (option == 2) {
                                Scanner scanner1 = new Scanner(System.in);
                                System.out.println("Insert the username of the user you want to delete");
                                String username = scanner1.nextLine();
                                User user = imdb.searchUser(username);
                                imdb.deleteUser(user);
                                System.out.println("User deleted successfully.");
                                imdb.printAccounts();
                            } else {
                                System.out.println("Invalid option.");
                            }
                        }
                        actions.removeAll(actions);
                        break;
                    case "Add/Delete actors/movies/series from system":
                        if(currentuser.getAccountType() == AccountType.Regular) {
                            throw new IllegalArgumentException("You don't have permission to add or delete actors/movies/series.");
                        } else {
                            System.out.println("1. Add actor");
                            System.out.println("2. Add production");
                            System.out.println("3. Delete actor");
                            System.out.println("4. Delete production");
                            Scanner scanner = new Scanner(System.in);
                            int option = scanner.nextInt();
                            Staff staff = (Staff) currentuser;
                            if (option == 1) {
                                staff.addActorSystem();
                                System.out.println("Actor added successfully.");
                                imdb.printActors();
                                currentuser.setExperienceStrategy(new addSystemStrategy());
                                currentuser.updateExperience(currentuser.getExperience());
                            } else if (option == 2) {
                                staff.addProductionSystem();
                                System.out.println("Production added successfully.");
                                imdb.printMovie();
                                imdb.printSeries();
                                currentuser.setExperienceStrategy(new addSystemStrategy());
                                currentuser.updateExperience(currentuser.getExperience());
                            } else if (option == 3) {
                            } else if (option == 3) {
                                Scanner scanner1 = new Scanner(System.in);
                                System.out.println("Insert the name of the actor you want to delete");
                                String actorname = scanner1.nextLine();
                                Actor actor = imdb.searchActor(actorname);
                                staff.removeActorSystem(actor);
                                System.out.println("Actor deleted successfully.");
                                imdb.printActors();
                            } else if (option == 4) {
                                System.out.println("movie/series:");
                                Scanner scanner1 = new Scanner(System.in);
                                String type = scanner1.nextLine();
                                if (type.equals("movie")) {
                                    System.out.println("Insert the name of the movie you want to delete");
                                    Scanner scanner2 = new Scanner(System.in);
                                    String moviename = scanner2.nextLine();
                                    Movie movie = imdb.searchMovie(moviename);
                                    staff.removeProductionSystem(movie);
                                    System.out.println("Production deleted successfully.");
                                    imdb.printMovie();
                                } else if (type.equals("series")) {
                                    System.out.println("Insert the name of the series you want to delete");
                                    Scanner scanner2 = new Scanner(System.in);
                                    String seriesname = scanner2.nextLine();
                                    Series series = imdb.searchSeries(seriesname);
                                    staff.removeProductionSystem(series);
                                    System.out.println("Production deleted successfully.");
                                    imdb.printSeries();
                                } else {
                                    throw new IllegalArgumentException("Invalid search type.");
                                }
                            } else {
                                System.out.println("Invalid option.");
                            }
                        }
                        actions.removeAll(actions);
                        break;
                    case "Update production/actor details":
                        if (currentuser.getAccountType() == AccountType.Regular) {
                            throw new IllegalArgumentException("You don't have permission to update production/actor details.");
                        } else {
                            System.out.println("1. Update actor");
                            System.out.println("2. Update production");
                            Scanner scanner = new Scanner(System.in);
                            int option = scanner.nextInt();
                            if (option == 1) {
                                Staff staff = (Staff) currentuser;
                                System.out.println("Enter actor name:");
                                Scanner scanner1 = new Scanner(System.in);
                                String actorname = scanner1.nextLine();
                                Actor actor = imdb.searchActor(actorname);
                                if (actor != null) {
                                    staff.updateActor(actor);
                                    System.out.println("Actor updated successfully.");
                                    imdb.searchActor(actorname);
                                } else {
                                    throw new IllegalArgumentException("Actorul nu exista in sistem");
                                }
                            } else if (option == 2) {
                                Staff staff = (Staff) currentuser;
                                System.out.println("movie/series:");
                                Scanner scanner1 = new Scanner(System.in);
                                String type = scanner1.nextLine();
                                if (type.equals("movie")) {
                                    System.out.println("Enter movie name:");
                                    Scanner scanner2 = new Scanner(System.in);
                                    String moviename = scanner2.nextLine();
                                    Movie movie = imdb.searchMovie(moviename);
                                    if (movie != null) {
                                        staff.updateProduction(movie);
                                        System.out.println("Production updated successfully.");
                                        imdb.printMovie();
                                    } else {
                                        throw new IllegalArgumentException("Filmul nu exista in sistem");
                                    }
                                } else if (type.equals("series")) {
                                    System.out.println("Enter series name:");
                                    Scanner scanner2 = new Scanner(System.in);
                                    String seriesname = scanner2.nextLine();
                                    Series series = imdb.searchSeries(seriesname);
                                    if (series != null) {
                                        staff.updateProduction(series);
                                        System.out.println("Production updated successfully.");
                                        imdb.printSeries();
                                    } else {
                                        throw new IllegalArgumentException("Serialul nu exista in sistem");
                                    }
                                } else {
                                    throw new IllegalArgumentException("Invalid search type.");
                                }
                            } else {
                                System.out.println("Invalid option.");
                            }
                        }
                        actions.removeAll(actions);
                        break;
                    case "Create/Delete a request":
                        if (currentuser.getAccountType() == AccountType.Admin) {
                            throw new IllegalArgumentException("You don't have permission to create or delete requests.");
                        } else {
                            System.out.println("1. Create request");
                            System.out.println("2. Delete request");
                            Scanner scanner = new Scanner(System.in);
                            int option = scanner.nextInt();
                            if (option == 1) {
                                if (currentuser.getAccountType() == AccountType.Regular) {
                                    Regular regularuser = (Regular) currentuser;
                                    regularuser.createRequest();
                                } else {
                                    Contributor contributoruser = (Contributor) currentuser;
                                    contributoruser.createRequest();
                                }
                                System.out.println("Request created successfully.");
                                imdb.printRequests();
                            } else if (option == 2) {
                                Scanner scanner1 = new Scanner(System.in);
                                System.out.println("Insert the name of the request you want to delete");
                                String requestnametodelete = scanner1.nextLine();
                                Request request = imdb.searchRequest(requestnametodelete);
                                if (currentuser.getAccountType() == AccountType.Regular) {
                                    Regular regularuser = (Regular) currentuser;
                                    regularuser.removeRequest(request);
                                } else {
                                    Contributor contributoruser = (Contributor) currentuser;
                                    contributoruser.removeRequest(request);
                                }
                                System.out.println("Request deleted successfully.");
                                imdb.printRequests();
                            } else {
                                System.out.println("Invalid option.");
                            }
                        }
                        actions.removeAll(actions);
                        break;
                    case "Solve a request":
                        if (currentuser.getAccountType() == AccountType.Regular) {
                            throw new IllegalArgumentException("You don't have permission to solve requests.");
                        } else {
                            //imdb.createObsereversforRequests();
                            if (currentuser.getAccountType() == AccountType.Contributor) {
                                Staff staffuser = (Staff) currentuser;
                                Request request = imdb.searchRequest(currentuser.getUsername());
                                if (request != null) {
                                    staffuser.resolveRequest(request);
                                    System.out.println("Request resolved successfully.");
                                    imdb.printRequests();
                                    currentuser = imdb.searchUser(request.getUsername());
                                    currentuser.setExperienceStrategy(new RequestStrategy());
                                    currentuser.updateExperience(currentuser.getExperience());
                                }
                            } else {
                                Admin adminuser = (Admin) currentuser;
                                imdb.searchRequests();
                                // admin user selects request to solve
                                System.out.println("Insert the username for the request you want to solve");
                                Scanner scanner1 = new Scanner(System.in);
                                String requestnametosolve = scanner1.nextLine();
                                Request request = imdb.searchRequestbyusername(requestnametosolve);
                                adminuser.resolveRequest(request);
                                currentuser = imdb.searchUser(request.getUsername());
                                currentuser.setExperienceStrategy(new RequestStrategy());
                                currentuser.updateExperience(currentuser.getExperience());
                            }
                        }
                        actions.removeAll(actions);
                        break;
                    case "Add/Remove a rating to/from a production":
                        if (currentuser.getAccountType() == AccountType.Regular) {
                            System.out.println("1. Add rating");
                            System.out.println("2. Remove rating");
                            Scanner scanner = new Scanner(System.in);
                            int option = scanner.nextInt();
                            if (option == 1) {
                                System.out.println("What would you like to rate? (movie/series)");
                                Scanner raspuns_obj_add = new Scanner(System.in);
                                String raspuns_obj_add_fin = raspuns_obj_add.nextLine();
                                if (raspuns_obj_add_fin.equals("movieS")) {
                                    System.out.println("Insert movie name:");
                                    Scanner nume_movie = new Scanner(System.in);
                                    String moviename = nume_movie.nextLine();
                                    Movie movie = imdb.searchMovie(moviename);
                                    if (movie != null) {
                                        imdb.addRating(movie);
                                    } else {
                                        throw new IllegalArgumentException("Movie not found in system");
                                    }
                                } else {
                                    System.out.println("Introduceti numele serialului:");
                                    Scanner nume_series = new Scanner(System.in);
                                    String seriesname = nume_series.nextLine();
                                    Series series = imdb.searchSeries(seriesname);
                                    if (series != null) {
                                        imdb.addRating(series);
                                    } else {
                                        throw new IllegalArgumentException("Serialul nu exista in sistem");
                                    }
                                }
                                System.out.println("Rating added successfully.");
                            } else if (option == 2) {
                                System.out.println("What would you like to rate? (movie/series)");
                                Scanner raspuns_obj_add = new Scanner(System.in);
                                String raspuns_obj_add_fin = raspuns_obj_add.nextLine();
                                if (raspuns_obj_add_fin.equals("movie")) {
                                    System.out.println("Insert movie name:");
                                    Scanner nume_movie = new Scanner(System.in);
                                    String moviename = nume_movie.nextLine();
                                    Movie movie = imdb.searchMovie(moviename);
                                    if (movie != null) {
                                        imdb.removeRating(movie, currentuser.getUsername());
                                    } else {
                                        throw new IllegalArgumentException("Movie not found in system");
                                    }
                                } else {
                                    System.out.println("Introduceti numele serialului:");
                                    Scanner nume_series = new Scanner(System.in);
                                    String seriesname = nume_series.nextLine();
                                    Series series = imdb.searchSeries(seriesname);
                                    if (series != null) {
                                        imdb.removeRating(series, currentuser.getUsername());
                                    } else {
                                        throw new IllegalArgumentException("Serialul nu exista in sistem");
                                    }
                                }
                                System.out.println("Rating removed successfully.");
                            } else {
                                System.out.println("Invalid option.");
                            }
                        } else {
                            throw new IllegalArgumentException("You don't have permission to add or remove ratings.");
                        }
                        actions.removeAll(actions);
                        break;

                    case "Add/Remove a rating to/from an actor":
                        if (currentuser.getAccountType() == AccountType.Regular) {
                            System.out.println("1. Add rating");
                            System.out.println("2. Remove rating");
                            Scanner scanner = new Scanner(System.in);
                            int option = scanner.nextInt();
                            if (option == 1) {
                                System.out.println("Insert actor name:");
                                Scanner nume_actor = new Scanner(System.in);
                                String actorname = nume_actor.nextLine();
                                Actor actor = imdb.searchActor(actorname);
                                if (actor != null) {
                                    imdb.addRatingtoActor(actor);
                                } else {
                                    throw new IllegalArgumentException("Actor not found in system");
                                }
                                System.out.println("Rating added successfully.");

                                System.out.println("Actor rating: " + actor.getRatings());
                            } else if (option == 2) {
                                System.out.println("Insert actor name:");
                                Scanner nume_actor = new Scanner(System.in);
                                String actorname = nume_actor.nextLine();
                                Actor actor = imdb.searchActor(actorname);
                                if (actor != null) {
                                    imdb.removeRatingtoActor(actor, currentuser.getUsername());
                                } else {
                                    throw new IllegalArgumentException("Actor not found in system");
                                }
                                System.out.println("Rating removed successfully.");

                                System.out.println("Actor rating: " + actor.getRatings());
                            } else {
                                System.out.println("Invalid option.");
                            }
                        } else {
                            throw new IllegalArgumentException("You don't have permission to add or remove ratings.");
                        }
                        actions.removeAll(actions);
                        break;
                    case "Logout":
                        IMDB imdb1 = IMDB.getInstance();
                        imdb1.writeRequestsjson("POO-Tema-2023-checker/POO-Tema-2023-checker/src/main/java/org/example/requests.json");
                        imdb1.writeActorsjson("POO-Tema-2023-checker/POO-Tema-2023-checker/src/main/java/org/example/actors.json");
                        imdb1.writeMoviesjson("POO-Tema-2023-checker/POO-Tema-2023-checker/src/main/java/org/example/movies.json");
                        imdb1.writeSeriesjson("POO-Tema-2023-checker/POO-Tema-2023-checker/src/main/java/org/example/series.json");
                        imdb1.writeUsersjson("POO-Tema-2023-checker/POO-Tema-2023-checker/src/main/java/org/example/users.json");

                        System.out.println("Logging out...");
                        System.out.println("Would you like to log in again? (yes/no)");
                        Scanner raspunslogout = new Scanner(System.in);
                        String raspuns1 = raspunslogout.nextLine();
                        if (raspuns1.equals("yes")) {
                            actions.removeAll(actions);
                            currentuser = currentuser.logout();
                        } else if (raspuns1.equals("no")) {
                            System.out.println("Exiting...");
                            loggedIn = false; // setează loggedIn la false pentru a ieși din buclă
                        } else {
                            System.out.println("Invalid answer.");
                        }
                        break;
                    default:
                        System.out.println("Invalid action selected.");
                    }
                } else {
                    actions.removeAll(actions);
                    System.out.println("Invalid action selected.");
                }
            }
        } else if (modfolosire.equals("Swing")) {
            // Implement Swing UI logic here
            SwingUI.createAndShowGUI(); // Apelează metoda din clasa SwingUI
        }
    }
}
