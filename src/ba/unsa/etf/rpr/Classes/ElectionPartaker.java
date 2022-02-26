package ba.unsa.etf.rpr.Classes;

public class ElectionPartaker {
    private String name;

    public ElectionPartaker(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
