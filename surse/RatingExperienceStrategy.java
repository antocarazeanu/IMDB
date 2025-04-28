public class RatingExperienceStrategy implements ExperienceStrategy {
    @Override
    public int calculateExperience(int experience) {
        return experience + 5;
    }
}
