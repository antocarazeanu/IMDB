public class Episode {
    private String episodeName;
    private int duration; // Durata episodului în minute

    // Constructor pentru clasa Episode
    public Episode(String episodeName, int duration) {
        this.episodeName = episodeName;
        this.duration = duration;
    }

    // Getteri pentru atributele clasei Episode
    public String getEpisodeName() {
        return episodeName;
    }

    public int getDuration() {
        return duration;
    }

    // Setteri pentru atributele clasei Episode
    public void setEpisodeName(String episodeName) {
        this.episodeName = episodeName;
    }
}
