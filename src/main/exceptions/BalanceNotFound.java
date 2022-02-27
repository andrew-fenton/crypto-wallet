package exceptions;

public class BalanceNotFound extends Exception {

    public BalanceNotFound() {

    }

    public BalanceNotFound(String msg) {
        super(msg);
    }

}
