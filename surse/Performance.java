public class Performance {
    private String title;
    private String type;

    public Performance(String title, String type) {
        this.title = title;
        this.type = type;
    }

    // getteri si setteri pentru fiecare camp
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
