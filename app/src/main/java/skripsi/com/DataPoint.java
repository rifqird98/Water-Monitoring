package skripsi.com;

public class DataPoint {
   int Level_rlt, Keruh_rlt;

   public DataPoint(){

   }

    public DataPoint(int level_rlt, int keruh_rlt) {
        Level_rlt = level_rlt;
        Keruh_rlt = keruh_rlt;
    }

    public int getLevel_rlt() {
        return Level_rlt;
    }

    public void setLevel_rlt(int level_rlt) {
        Level_rlt = level_rlt;
    }

    public int getKeruh_rlt() {
        return Keruh_rlt;
    }

    public void setKeruh_rlt(int keruh_rlt) {
        Keruh_rlt = keruh_rlt;
    }
}
