import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.util.SortedSet;
import java.util.TreeSet;
import java.time.LocalDateTime;

public class InformationBuilder {
    private Credentials credentials;
    private String name;
    private String country;
    private int age;
    private String gender;
    private LocalDateTime birthDate;

    public InformationBuilder setCredentials(Credentials credentials) {
        this.credentials = credentials;
        return this;
    }

    public InformationBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public InformationBuilder setCountry(String country) {
        this.country = country;
        return this;
    }

    public InformationBuilder setAge(int age) {
        this.age = age;
        return this;
    }

    public InformationBuilder setGender(String gender) {
        this.gender = gender;
        return this;
    }

    public InformationBuilder setBirthDate(LocalDateTime birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public User.Information build() {
        return new User.Information(credentials, name, country, age, gender, birthDate);
    }
}