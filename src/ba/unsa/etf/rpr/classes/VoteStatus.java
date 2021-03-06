package ba.unsa.etf.rpr.classes;

public enum VoteStatus {
    VOTED("has voted"), NOT_VOTED("has not voted"), NOT_VALID("marked ballot as not valid");

    private final String status;

    VoteStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return status;
    }
}
