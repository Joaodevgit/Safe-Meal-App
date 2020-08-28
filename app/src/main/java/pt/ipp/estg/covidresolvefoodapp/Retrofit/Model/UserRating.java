package pt.ipp.estg.covidresolvefoodapp.Retrofit.Model;

public class UserRating {

    private String aggregate_rating;
    private String rating_text;
    private String rating_color;
    private int votes;

    public UserRating(String aggregate_rating, String rating_text, String rating_color, int votes) {
        this.aggregate_rating = aggregate_rating;
        this.rating_text = rating_text;
        this.rating_color = rating_color;
        this.votes = votes;
    }

    public String getAggregate_rating() {
        return aggregate_rating;
    }

    public String getRating_text() {
        return rating_text;
    }

    public String getRating_color() {
        return rating_color;
    }

    public int getVotes() {
        return votes;
    }

    @Override
    public String toString() {
        return "UserRating{" +
                "aggregate_rating='" + aggregate_rating + '\'' +
                ", rating_text='" + rating_text + '\'' +
                ", rating_color='" + rating_color + '\'' +
                ", votes=" + votes +
                '}';
    }
}
