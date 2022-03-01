package ba.unsa.etf.rpr.database_organization;

import ba.unsa.etf.rpr.classes.PoliticalParty;
import ba.unsa.etf.rpr.classes.VoteStatus;
import ba.unsa.etf.rpr.classes.Voter;
import exceptions.NonexistantVoterException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.*;

public class VotingDAO {
    private static VotingDAO instance = null;
    private Connection conn;
    private PreparedStatement addVoterQuery;
    private PreparedStatement getPartiesQuery, getPartyQuery;
    private PreparedStatement getVotersQuery;
    private PreparedStatement getWaitlistQuery;
    private PreparedStatement findVoterByID;
    private PreparedStatement invalidateBallotQuery;
    private PreparedStatement castVoteVoter, castVoteVotee;

    public static VotingDAO getInstance(){
        if(instance == null) instance = new VotingDAO();
        return instance;
    }

    private VotingDAO(){
        String url = "jdbc:sqlite:database.db";
        try{
            conn = DriverManager.getConnection(url);
        } catch (SQLException e){
            e.printStackTrace();
        }
        try{
            addVoterQuery = conn.prepareStatement("INSERT INTO voters VALUES(?,?,?,?)");
        } catch (SQLException e){
            regenerateBase();
            try{
                addVoterQuery = conn.prepareStatement("INSERT INTO voters VALUES(?,?,?,?)");
            } catch (SQLException i){
                i.printStackTrace();
            }
        }
        try {
            getPartiesQuery = conn.prepareStatement("SELECT * FROM parties");
            getPartyQuery = conn.prepareStatement("SELECT votes FROM parties WHERE name = ?");
            getVotersQuery = conn.prepareStatement("SELECT * FROM voters");
            getWaitlistQuery = conn.prepareStatement("SELECT * FROM waitlist");
            findVoterByID = conn.prepareStatement("SELECT * FROM voters WHERE soc_number = ?");
            invalidateBallotQuery = conn.prepareStatement("UPDATE voters SET vote_status = -1 WHERE soc_number = ?");
            castVoteVoter = conn.prepareStatement("UPDATE voters SET vote_status = 1 WHERE soc_number = ?");
            castVoteVotee = conn.prepareStatement("UPDATE parties SET votes = ? WHERE name = ?");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void regenerateBase() {
        Scanner enter = null;
        try {
            enter = new Scanner(new FileInputStream("database.db.sql"));
            String sqlQuery = "";
            while (enter.hasNext()){
                sqlQuery += enter.nextLine();
                if (sqlQuery.length() > 1 && sqlQuery.charAt(sqlQuery.length() - 1) == ';') {
                    try {
                        Statement stmt = conn.createStatement();
                        stmt.execute(sqlQuery);
                        sqlQuery = "";
                    } catch (SQLException e) {
                        e.printStackTrace();
                        enter.close();
                    }
                }
            }
            enter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void defaultBase(){
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate("DELETE FROM voters");
            stmt.executeUpdate("DELETE FROM parties");
            stmt.executeUpdate("DELETE FROM waitlist");
            regenerateBase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeInstance() {
        try {
            instance.conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NullPointerException n){
            instance = null;
        }
        instance = null; }

    public ArrayDeque<PoliticalParty> parties(){
        ArrayDeque<PoliticalParty> result = new ArrayDeque<>();
        ResultSet rs = null;
        try {
            rs = getPartiesQuery.executeQuery();
            while(rs.next()){
                PoliticalParty politicalParty = getPartyFromResultSet(rs);
                result.add(politicalParty);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return result;
    }

    public List<PoliticalParty> waitlist(){
        ArrayList<PoliticalParty> result = new ArrayList<>();
        ResultSet rs = null;
        try {
            rs = getWaitlistQuery.executeQuery();
            while(rs.next()){
                PoliticalParty politicalParty = getPartyFromResultSet(rs);
                result.add(politicalParty);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return result;
    }

    private PoliticalParty getPartyFromResultSet(ResultSet rs) throws SQLException{
        return new PoliticalParty(rs.getString(2), rs.getString(3), rs.getInt(4));
    }

    public SortedSet<Voter> voters (){
        SortedSet<Voter> result = new TreeSet<>();
        ResultSet rs = null;
        try {
            rs = getVotersQuery.executeQuery();
            while(rs.next()){
                Voter voter = getVoterFromResultSet(rs);
                result.add(voter);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return result;
    }

    private Voter getVoterFromResultSet(ResultSet rs) throws SQLException {
        Voter voter = new Voter(rs.getString(2), rs.getString(3));
        switch(rs.getInt(4)){
            case 0 -> voter.setVoteStatus(VoteStatus.NOT_VOTED);
            case 1 -> voter.setVoteStatus(VoteStatus.VOTED);
            default -> voter.setVoteStatus(VoteStatus.NOT_VALID);
        }
        return voter;
    }

    public Connection getConn(){ return conn;}

    public int checkVoter(String string) throws NonexistantVoterException {
        int res = 0;
        ResultSet rs = null;
        try {
            findVoterByID.setString(1,string);
            rs = findVoterByID.executeQuery();
            if(!rs.next()) throw new NonexistantVoterException();
            else res = rs.getInt(4);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public void addVoter(Voter voter) {
        int size = voters().size();
        try {
            addVoterQuery.setInt(1, size+1);
            addVoterQuery.setString(2, voter.getName());
            addVoterQuery.setString(3, voter.getID());
            switch(voter.getVoteStatus()){
                case VOTED -> addVoterQuery.setInt(4,1);
                default -> addVoterQuery.setInt(4, 0);
            }
            addVoterQuery.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void castVote(PoliticalParty party, Voter voter) {
        int votes=0;
        ResultSet rs = null;
        try {
            getPartyQuery.setString(1, party.getName());
            rs = getPartyQuery.executeQuery();
            while(rs.next()){
                votes = rs.getInt(4);
            }
            castVoteVotee.setInt(1, votes+1);
            castVoteVotee.setString(2, party.getName());
            castVoteVotee.executeUpdate();
            castVoteVoter.setString(1, voter.getID());
            castVoteVoter.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void invalidateBallot(Voter voter) {
        try {
            invalidateBallotQuery.setString(1, voter.getID());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
