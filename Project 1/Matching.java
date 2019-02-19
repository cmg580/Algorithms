import java.util.ArrayList;

/**
 * A Matching represents a candidate solution to the Stable Marriage problem. A Matching may or may
 * not be stable.
 */
public class Matching {
    /**
     * Number of servers.
     */
    private Integer m;

    /**
     * Number of users.
     */
    private Integer n;

    /**
     * A list containing each server's preference list.
     */
    private ArrayList<ArrayList<Integer>> server_preference;

    /**
     * A list containing each user's preference list.
     */
    private ArrayList<ArrayList<Integer>> user_preference;

    /**
     * Number of slots available in each server.
     */
    private ArrayList<Integer> server_slots;

    /**
     * Matching information representing the index of server a user is matched to, -1 if not
     * matched.
     *
     * <p>An empty matching is represented by a null value for this field.
     */
    private ArrayList<Integer> user_matching;

    public Matching(
            Integer m,
            Integer n,
            ArrayList<ArrayList<Integer>> server_preference,
            ArrayList<ArrayList<Integer>> user_preference,
            ArrayList<Integer> server_slots) {
        this.m = m;
        this.n = n;
        this.server_preference = server_preference;
        this.user_preference = user_preference;
        this.server_slots = server_slots;
        this.user_matching = null;
    }

    public Matching(
            Integer m,
            Integer n,
            ArrayList<ArrayList<Integer>> server_preference,
            ArrayList<ArrayList<Integer>> user_preference,
            ArrayList<Integer> server_slots,
            ArrayList<Integer> user_matching) {
        this.m = m;
        this.n = n;
        this.server_preference = server_preference;
        this.user_preference = user_preference;
        this.server_slots = server_slots;
        this.user_matching = user_matching;
    }

    /**
     * Constructs a solution to the stable marriage problem, given the problem as a Matching. Take a
     * Matching which represents the problem data with no solution, and a user_matching which
     * solves the problem given in data.
     *
     * @param data              The given problem to solve.
     * @param user_matching 	The solution to the problem.
     */
    public Matching(Matching data, ArrayList<Integer> user_matching) {
        this(
                data.m,
                data.n,
                data.server_preference,
                data.user_preference,
                data.server_slots,
                user_matching);
    }

    /**
     * Creates a Matching from data which includes an empty solution.
     *
     * @param data The Matching containing the problem to solve.
     */
    public Matching(Matching data) {
        this(
                data.m,
                data.n,
                data.server_preference,
                data.user_preference,
                data.server_slots,
                new ArrayList<Integer>(0));
    }

    public void setUserMatching(ArrayList<Integer> user_matching) {
        this.user_matching = user_matching;
    }

    public Integer getServerCount() {
        return m;
    }

    public Integer getUserCount() {
        return n;
    }

    public ArrayList<ArrayList<Integer>> getServerPreference() {
        return server_preference;
    }

    public ArrayList<ArrayList<Integer>> getUserPreference() {
        return user_preference;
    }

    public ArrayList<Integer> getServerSlots() {
        return server_slots;
    }

    public ArrayList<Integer> getUserMatching() {
        return user_matching;
    }

    public int totalServerSlots() {
        int slots = 0;
        for (int i = 0; i < m; i++) {
            slots += server_slots.get(i);
        }
        return slots;
    }

    public String getInputSizeString() {
        return String.format("m=%d n=%d\n", m, n);
    }

    public String getSolutionString() {
        if (user_matching == null) {
            return " ";
        }

        StringBuilder s = new StringBuilder();
        for (int i = 0; i < user_matching.size(); i++) {
            String str = String.format("User %d Server %d", i, user_matching.get(i));
            s.append(str);
            if (i != user_matching.size() - 1) {
                s.append("\n");
            }
        }
        return s.toString();
    }

    public String toString() {
        return getInputSizeString() + getSolutionString();
    }
}
