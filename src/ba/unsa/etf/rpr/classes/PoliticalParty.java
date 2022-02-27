package ba.unsa.etf.rpr.classes;

public class PoliticalParty extends ElectionPartaker{
    private String partyLeader;
    private Integer votes;

    public PoliticalParty(String name, String partyLeader) {
        super(name);
        this.partyLeader = partyLeader;
        votes = 0;
    }

    public PoliticalParty(String name, String partyLeader, Integer votes) {
        super(name);
        this.partyLeader = partyLeader;
        this.votes = votes;
    }

    public String getPartyLeader() {
        return partyLeader;
    }

    public void setPartyLeader(String partyLeader) {
        this.partyLeader = partyLeader;
    }

    public Integer getVotes() {
        return votes;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }

    @Override
    public String toString() {
        return getName() + " - " + partyLeader;
    }
}
