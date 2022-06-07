package skripsi.com;

public class PointsValue2 {
String waktu, Data;

public PointsValue2(){

}
    public PointsValue2(String waktu, String data) {
        this.waktu = waktu;
        Data = data;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }
}
