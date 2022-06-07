package skripsi.com;

public class PointsValue {
float xValue;
float yValue;

public PointsValue(){

}

    public PointsValue(float xValue, float yValue) {
        this.xValue = xValue;
        this.yValue = yValue;
    }

    public float getxValue() {
        return xValue;
    }

    public void setxValue(float xValue) {
        this.xValue = xValue;
    }

    public float getyValue() {
        return yValue;
    }

    public void setyValue(float yValue) {
        this.yValue = yValue;
    }
}
