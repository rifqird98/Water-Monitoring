package skripsi.com.Fragment_level;

public class ContractorLev {
    long xValue;
    float yValue;

    public ContractorLev(){

    }

    public ContractorLev(long xValue, float yValue) {
        this.xValue = xValue;
        this.yValue = yValue;
    }

    public long getxValue() {
        return xValue;
    }

    public void setxValue(long xValue) {
        this.xValue = xValue;
    }

    public float getyValue() {
        return yValue;
    }

    public void setyValue(float yValue) {
        this.yValue = yValue;
    }
}
