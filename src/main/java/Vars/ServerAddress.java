package Vars;

public enum ServerAddress {
    LOCALHOST("localhost:9092");

    private String address;

    private ServerAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }
}
