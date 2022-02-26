package ba.unsa.etf.rpr.Classes;

public class PoliticalParty extends ElectionPartaker{
    private String partyLeader;

    public PoliticalParty(String name, String partyLeader) {
        super(name);
        this.partyLeader = partyLeader;
    }

    public String getPartyLeader() {
        return partyLeader;
    }

    public void setPartyLeader(String partyLeader) {
        this.partyLeader = partyLeader;
    }

    @Override
    public String toString() {
        return getName() + " - " + partyLeader;
    }
}
