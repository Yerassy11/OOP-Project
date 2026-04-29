package exeptions;

public class CreditLimitException extends Exception {
    private final int current;
    private final int max;
    public CreditLimitException(int current, int max) {

        super("Credit limit exceeded: current=" + current + ", max allowed=" +max);
        this.current=current;
        this.max=max;
    }
    public int getCurrent(){
        return current;
    }
    public int getMax(){
        return max;
    }
}
