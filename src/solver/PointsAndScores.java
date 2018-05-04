package solver;

public class PointsAndScores {
    private int score;
    private Points point;

    public PointsAndScores(int score, Points point) {
        this.score = score;
        this.point = point;
    }

    public int getScore() {
        return score;
    }

    public Points getPoint() {
        return point;
    }
}
