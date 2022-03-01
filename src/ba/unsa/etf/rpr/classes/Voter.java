package ba.unsa.etf.rpr.classes;

public class Voter extends ElectionPartaker implements Comparable<Voter>{
    private String ID;
    private VoteStatus voteStatus;

    public Voter(String name, String ID, VoteStatus voteStatus) {
        super(name);
        this.ID = ID;
        this.voteStatus = voteStatus;
    }

    public Voter(String name, String ID) {
        super(name);
        this.ID = ID;
        this.voteStatus = VoteStatus.NOT_VOTED;
    }



    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public VoteStatus getVoteStatus() {
        return voteStatus;
    }

    public void setVoteStatus(VoteStatus voteStatus) {
        this.voteStatus = voteStatus;
    }

    @Override
    public int compareTo(Voter o) {
        return this.ID.compareTo(o.ID);
    }

    @Override
    public String toString() {
        return getName() + " " + voteStatus.toString() + "\n";
    }
}
