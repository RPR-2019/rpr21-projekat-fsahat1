package ba.unsa.etf.rpr.Classes;

public class Voter extends ElectionPartaker{
    private String ID;
    private VoteStatus voteStatus;

    public Voter(String name, String ID, VoteStatus voteStatus) {
        super(name);
        this.ID = ID;
        this.voteStatus = voteStatus;
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
}
