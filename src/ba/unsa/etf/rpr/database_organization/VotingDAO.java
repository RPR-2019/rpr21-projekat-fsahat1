package ba.unsa.etf.rpr.database_organization;

import ba.unsa.etf.rpr.classes.PoliticalParty;
import ba.unsa.etf.rpr.classes.VoteStatus;
import ba.unsa.etf.rpr.classes.Voter;


import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

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
    private PreparedStatement registerPartyQuery;
    private PreparedStatement addPartyQuery, deleteRegisteredParty;
    private PreparedStatement resetWaitlist, resetParties, resetVoters;

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
            getPartyQuery = conn.prepareStatement("SELECT * FROM parties WHERE name = ?");
            getVotersQuery = conn.prepareStatement("SELECT * FROM voters");
            getWaitlistQuery = conn.prepareStatement("SELECT * FROM waitlist");
            findVoterByID = conn.prepareStatement("SELECT * FROM voters WHERE soc_number = ?");
            invalidateBallotQuery = conn.prepareStatement("UPDATE voters SET vote_status = -1 WHERE soc_number = ?");
            castVoteVoter = conn.prepareStatement("UPDATE voters SET vote_status = 1 WHERE soc_number = ?");
            castVoteVotee = conn.prepareStatement("UPDATE parties SET votes = ? WHERE name = ?");
            registerPartyQuery = conn.prepareStatement("INSERT INTO waitlist VALUES (?,?,?)");
            addPartyQuery = conn.prepareStatement("INSERT INTO parties VALUES (?,?,?,0)");
            deleteRegisteredParty = conn.prepareStatement("DELETE FROM waitlist WHERE name = ?");
            resetWaitlist = conn.prepareStatement("DELETE FROM waitlist");
            resetParties = conn.prepareStatement("DELETE FROM parties");
            resetVoters = conn.prepareStatement("DELETE FROM voters");
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

    public ArrayList<PoliticalParty> parties(){
        ArrayList<PoliticalParty> result = new ArrayList<>();
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

    public ArrayList<PoliticalParty> waitlist(){
        ArrayList<PoliticalParty> result = new ArrayList<>();
        ResultSet rs = null;
        try {
            rs = getWaitlistQuery.executeQuery();
            while(rs.next()){
                PoliticalParty politicalParty = getPartyFromWaitlist(rs);
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
    private PoliticalParty getPartyFromWaitlist(ResultSet rs) throws SQLException{
        return new PoliticalParty(rs.getString(2), rs.getString(3),0);
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

    public void registerNewParty(PoliticalParty party) {
        int pos = 0;
        ResultSet rs = null;
        try {
            rs = getWaitlistQuery.executeQuery();
            while(rs.next()) pos = rs.getInt(1);
            pos = pos + 1;
            registerPartyQuery.setInt(1,pos);
            registerPartyQuery.setString(2, party.getName());
            registerPartyQuery.setString(3, party.getPartyLeader());
            registerPartyQuery.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteParty(PoliticalParty party) {
        try {
            deleteRegisteredParty.setString(1,party.getName());
            deleteRegisteredParty.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addElectableParty(PoliticalParty party) {
        int pos = 0;
        ResultSet rs = null;
        try {
            rs = getPartiesQuery.executeQuery();
            while(rs.next()) pos = rs.getInt(1);
            pos = pos + 1;
            addPartyQuery.setInt(1,pos);
            addPartyQuery.setString(2, party.getName());
            addPartyQuery.setString(3, party.getPartyLeader());
            addPartyQuery.executeUpdate();
            deleteParty(party);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void writeToFile(File chosen) {
        if(chosen==null) return;
        ArrayList<PoliticalParty> party = parties();
        party = party.stream().sorted(Comparator.comparing(PoliticalParty::getVotes)).collect(Collectors.toCollection(ArrayList::new));
        try {
            FileWriter record = new FileWriter(chosen);
            String unos = "";
            for(int i =0; i<party.size(); i++){
                unos = unos+party.get(i).toString();
                if(i<party.size()-1) unos = unos +"\n";
            }
            record.write(unos);
            record.close();
        } catch (IOException e) {
            System.out.println("Writing to file failed");
            e.printStackTrace();
        }
    }

    public void resetElection() {
        try {
            resetVoters.executeUpdate();
            resetParties.executeUpdate();
            resetWaitlist.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void writeFromFile(File chosen) {
        if(chosen==null) return;
        resetElection();
        int i =0;
        try {
            Scanner myReader = new Scanner(chosen);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] arrOfStr = data.split("/", 2);

                PoliticalParty party = new PoliticalParty(arrOfStr[0], arrOfStr[1]);
                addPartyQuery.setInt(1,i);
                addPartyQuery.setString(2, party.getName());
                addPartyQuery.setString(3, party.getPartyLeader());
                addPartyQuery.executeUpdate();
                i = i+1;
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
