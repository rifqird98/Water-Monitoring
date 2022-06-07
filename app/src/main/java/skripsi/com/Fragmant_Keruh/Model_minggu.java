package skripsi.com.Fragmant_Keruh;

public class Model_minggu {
    int day;
    float Data;

public Model_minggu(){

}

    public Model_minggu(int day, float data) {
        this.day = day;
        Data = data;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public float getData() {
        return Data;
    }

    public void setData(float data) {
        Data = data;
    }
}
