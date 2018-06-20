import java.util.ArrayList;

class Sehir implements Comparable<Sehir> {
    private final String plaka;
    private final String isim;
    private final double lat;
    private final double lon;
    private final double rakim;
    private final int x;
    private final int y;
    public ArrayList<Sehir> komsuluklar = new ArrayList<>();
    public ArrayList<Double> uzakliklar = new ArrayList<>();
    public double enKisaMesafe = Double.POSITIVE_INFINITY;
    public Sehir onceki;

    public void komsuEkle(Sehir komsu, Double uzaklik) {
        komsuluklar.add(komsu);
        uzakliklar.add(uzaklik);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public double getRakim() {
        return rakim;
    }

    public int getPlaka() {
        return Integer.parseInt(plaka);
    }

    public Sehir(String plaka, String lat, String lon, String rakim, String isim, String xy) {
        this.plaka = plaka;
        this.isim = isim;
        this.lat = Double.parseDouble(lat);
        this.lon = Double.parseDouble(lon);
        this.rakim = Double.parseDouble(rakim);
        this.x = Integer.parseInt(xy.split(",")[0]);
        this.y = Integer.parseInt(xy.split(",")[1]);
    }

    public String toString() {
        return isim;
    }

    public int compareTo(Sehir diger) {
        return Double.compare(enKisaMesafe, diger.enKisaMesafe);
    }
}