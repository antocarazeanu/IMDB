import java.util.List;
import java.util.Map;
public class Series extends Production {
    /*
    Clasa extinde Production s, i cont, ine anul lansării, numărul de sezoane s, i un dict, ionar
ce are drept cheie numele sezonului s, i drept valoare o listă ce cont, ine obiecte de tip Episode -
private Map<String, List<Episode> >.
Observat, ii
• Dacă nu exista suficiente detalii pentru completarea tuturor câmpurilor (din Series
s, i Movie), acestea vor primi valoarea null s, i nu vor fi afis,ate la cerere.
     */
    private int releaseYear;
    private int numberOfSeasons;
    private Map<String, List<Episode> > seasons;
public Series(String title, List<String> directors, List<String> actors, List<Genre> genres,
                  List<Rating> ratings, String description, double rating, int releaseYear,
                  int numberOfSeasons, Map<String, List<Episode> > seasons) {
        super(title, directors, actors, genres, ratings, description, rating);
        this.releaseYear = releaseYear;
        this.numberOfSeasons = numberOfSeasons;
        this.seasons = seasons;
    }
    // getters
    public int getReleaseYear() {
        return releaseYear;
    }
    public int getNumberOfSeasons() {
        return numberOfSeasons;
    }
    public Map<String, List<Episode> > getSeasons() {
        return seasons;
    }

    //setters
    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }
    public void setNumberOfSeasons(int numberOfSeasons) {
        this.numberOfSeasons = numberOfSeasons;
    }
    public void setSeasons(Map<String, List<Episode> > seasons) {
        this.seasons = seasons;
    }
}
