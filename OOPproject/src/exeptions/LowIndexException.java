package exeptions;

public class LowIndexException extends RuntimeException {
    private final int hIndex;
    private final int minRequired = 3;
    public LowIndexException(int hIndex){
        super("Cannot assign supervisor: h-index is "+ hIndex + ", minimum required is 3");
        this.hIndex=hIndex;
    }
    public int getIndex() { return hIndex;}
    public int getMinRequired(){ return minRequired;}
}
