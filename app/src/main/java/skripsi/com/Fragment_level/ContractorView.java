package skripsi.com.Fragment_level;

public class ContractorView {

public ContractorView(){

}
    String waktu, Data;

    public ContractorView(String waktu, String data) {
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
