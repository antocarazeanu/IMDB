public class RequestStrategy implements ExperienceStrategy {
    @Override
    public int calculateExperience(int experience) {
        return experience + 10;
    }
}
