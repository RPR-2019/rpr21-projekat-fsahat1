package ba.unsa.etf.rpr.classes;

import java.util.Objects;

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
        return getName() + " - " + partyLeader + ": " + votes + " votes\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PoliticalParty)) return false;
        PoliticalParty that = (PoliticalParty) o;
        return getName().equals(that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(partyLeader, votes);
    }
}
