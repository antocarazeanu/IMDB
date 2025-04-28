public class addSystemStrategy implements ExperienceStrategy {
    @Override
    public int calculateExperience(int experience) {
        return experience + 2;
    }
}
