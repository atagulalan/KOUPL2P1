import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

class Zeplin {

    public static String latLong;
    public static String komsuluklar;

    public static List<Sehir> grandPath;
    private static ArrayList<Sehir> sehirler = new ArrayList<>();
    public static int rakimCarpani = 1000; //metre cinsinden (1000 metre = 1 kilometre)
    public static int maxYolcu = 50; //Zeplinin alabilecegi maximum yolcu sayisi
    public static int minYolcu = 5; //Zeplinin alabilecegi minimum yolcu sayisi
    public static double yolcuUcreti = 20.0; //Binis sirasinda alinan ucret
    public static double yolcuAgirlik = 1.0; //Her binen kisi maksimum hareket kabiliyetini kac derece azaltacak
    public static double maxAci = 80.0; //Yolcu binmedigindeki hareket kabiliyeti
    public static double yerdenYukseklik = 50.0; //Kilometre cinsinden
    public static double maliyet = 1000.0 / 100.0; //Fiyat/Kilometre
    public static int baslangic = 1; //Baslangic sehri
    public static int bitis = 1; //Bitis sehri
    public static int yolcu = 5; //Yolcu sayisi

    public static void putGlobals(int rC, int mnY, int mxY, double yU, double yA, double mA, double yY, double m, int y) {
        rakimCarpani = rC;
        maxYolcu = mxY;
        minYolcu = mnY;
        yolcuUcreti = yU;
        yolcuAgirlik = yA;
        maxAci = mA;
        yerdenYukseklik = yY;
        maliyet = m;
        yolcu = y;
    }

    public static Sehir getSehir(int plaka) {
        return sehirler.get(plaka - 1);
    }

    private static String plakadanIsim(String plaka) {
        return ("Adana,Adıyaman,Afyon,Ağrı,Amasya,Ankara," +
                "Antalya,Artvin,Aydın,Balıkesir,Bilecik,Bingöl,Bitlis,Bolu,Burdur," +
                "Bursa,Çanakkale,Çankırı,Çorum,Denizli,Diyarbakır,Edirne,Elazığ," +
                "Erzincan,Erzurum,Eskişehir,Gaziantep,Giresun,Gümüşhane,Hakkari," +
                "Hatay,Isparta,Mersin,İstanbul,İzmir,Kars,Kastamonu,Kayseri,Kırklareli," +
                "Kırşehir,Kocaeli,Konya,Kütahya,Malatya,Manisa,Kahramanmaraş,Mardin," +
                "Muğla,Muş,Nevşehir,Niğde,Ordu,Rize,Sakarya,Samsun,Siirt,Sinop,Sivas," +
                "Tekirdağ,Tokat,Trabzon,Tunceli,Şanlıurfa,Uşak,Van,Yozgat,Zonguldak," +
                "Aksaray,Bayburt,Karaman,Kırıkkale,Batman,Şırnak,Bartın,Ardahan,Iğdır," +
                "Yalova,Karabük,Kilis,Osmaniye,Düzce").split(",")[Integer.parseInt(plaka) - 1];
    }

    private static String plakadanXY(String plaka) {
        return ("512,371;659,335;253,274;906,206;530,149;367,206;225,400;837,106;116,329;" +
                "118,195;235,175;791,252;869,288;326,145;219,351;182,175;62,165;410,143;" +
                "473,156;174,336;774,317;67,88;705,284;698,212;827,185;286,212;628,390;" +
                "673,153;716,169;981,344;558,437;272,325;422,416;186,105;80,287;907,148;" +
                "424,89;526,286;110,62;447,238;233,118;353,319;202,227;653,291;131,261;" +
                "591,330;799,363;134,373;840,259;473,279;469,337;621,138;792,124;261,136;" +
                "533,108;870,327;484,83;611,224;90,108;579,166;737,132;727,245;715,379;" +
                "189,276;953,290;505,218;328,103;426,299;760,170;403,378;421,201;826,321;" +
                "888,355;365,80;893,103;957,181;192,142;369,111;597,406;551,375;297,126")
                .split(";")[Integer.parseInt(plaka) - 1];
    }

    private static ArrayList<String> dosyaOkuma(String dosya) throws Exception {
        //Graf her olusturuldugunda dosyadan okumamasi icin bellege aliyoruz
        if (dosya.equals("latlong.txt") && latLong != null) {
            return new ArrayList<>(Arrays.asList(latLong.split("\n")));
        }
        if (dosya.equals("komsuluklar.txt") && latLong != null) {
            return new ArrayList<>(Arrays.asList(komsuluklar.split("\n")));
        }
        ArrayList<String> satirlar = new ArrayList<>();
        File file = new File(dosya);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        int satirSayisi = 0;
        String satir;
        while ((satir = br.readLine()) != null) {
            satirlar.add(satirSayisi, satir);
            satirSayisi++;
        }
        br.close();
        return satirlar;
    }

    private static double sehirlerArasiUzaklik(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        return (Math.acos(dist) * 1.609344 * 60 * 1.1515 * 180.0 / Math.PI);
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    public static void init() {
        //eskiyi temizle
        sehirler = new ArrayList<>();

        //latlong.txt oku
        try {
            for (String latLong : dosyaOkuma("latlong.txt")) {
                String[] lineToArray = latLong.split(",");
                sehirler.add(new Sehir(lineToArray[2], lineToArray[0], lineToArray[1], lineToArray[3], plakadanIsim(lineToArray[2]), plakadanXY(lineToArray[2])));
            }
        } catch (Exception e) {
            System.out.println("latlong.txt Okunurken Hata!");
            System.exit(41);
        }

        //komsular.txt oku
        try {
            for (String komsuluklar : dosyaOkuma("komsular.txt")) {
                String[] lineToArray = komsuluklar.split(",");
                Sehir anaSehir = getSehir(Integer.parseInt(lineToArray[0]));
                for (int i = 1; i < lineToArray.length; i++) {
                    Sehir komsuSehir = getSehir(Integer.parseInt(lineToArray[i]));
                    double getKM = sehirlerArasiUzaklik(anaSehir.getLat(), anaSehir.getLon(), komsuSehir.getLat(), komsuSehir.getLon());
                    anaSehir.komsuEkle(komsuSehir, getKM);
                }
            }
        } catch (Exception e) {
            System.out.println("komsular.txt Okunurken Hata!");
            System.exit(42);
        }
    }

    private static double ucmaUzakligiBul(Sehir s1, Sehir s2) {
        double ekstra = s1.getPlaka() == baslangic && s2.getPlaka() == bitis ? 0.0 : //Iki sehir komsu ise direk git
                s1.getPlaka() == baslangic ? -yerdenYukseklik : //Sehir 1 baslangic ise
                        s2.getPlaka() == bitis ? yerdenYukseklik : 0.0; //Sehir 2 bitis ise
        double x = s1.uzakliklar.get(s1.komsuluklar.indexOf(s2));
        double y = Math.abs(s1.getRakim() * rakimCarpani * 0.001 - s2.getRakim() * rakimCarpani * 0.001 + ekstra * rakimCarpani * 0.001);
        return Math.sqrt((x * x) + (y * y));
    }

    private static void dijkstra(Sehir baslangic) {
        baslangic.enKisaMesafe = 0.; //Baslangicin en kisa mesafesini 0 atayalim
        PriorityQueue<Sehir> sehirKuyrugu = new PriorityQueue<>(); //Bir sehir oncelik kuyrugu olusturalim ve
        sehirKuyrugu.add(baslangic); //Baslangici ekleyelim
        while (!sehirKuyrugu.isEmpty()) { //Sehir kalmayana kadar dolasip
            Sehir uygunSehir = sehirKuyrugu.poll(); //En dusuk maliyetli (en son gidilen) sehri cekelim
            for (Sehir komsu : uygunSehir.komsuluklar) { //En uygun sehrin komsularina baglantilarini dolasalim
                double uygunSehirUzakligi = uygunSehir.enKisaMesafe + ucmaUzakligiBul(uygunSehir, komsu); //Bu sehre en kisa uzaklik, bu sehre gitmek icin harcanan maliyet + kenar uzakligidir
                if (uygunSehirUzakligi < komsu.enKisaMesafe) { //Eger bu sehre daha onceden daha kisa sekilde gidilememisse
                    sehirKuyrugu.remove(komsu); //Bu sehre en kisa uzakligi bulduk demektir. Simdikini kaldiralim.
                    komsu.enKisaMesafe = uygunSehirUzakligi; //Bunu sehire yazalim
                    komsu.onceki = uygunSehir; //Ve bu sehre bagli sehirlerden en kisasini atayalim
                    sehirKuyrugu.add(komsu);
                }
            }
        }
    }

    private static List<Sehir> enKisaYol(Sehir baslangic) {
        List<Sehir> yol = new ArrayList<>();
        for (Sehir sehir = baslangic; sehir != null; sehir = sehir.onceki)
            yol.add(sehir);

        Collections.reverse(yol);
        return yol;
    }

    private static boolean gidilebilirMi(double x, Sehir s1, Sehir s2, double ekstra, int yolcu) {
        if (s1.getPlaka() == baslangic) {
            ekstra = -ekstra;
        }
        double y = Math.abs(s1.getRakim() * rakimCarpani * 0.001 - s2.getRakim() * rakimCarpani * 0.001 + ekstra * rakimCarpani * 0.001);
        double e = Math.atan2(y, x) * 180 / Math.PI;
        System.out.println("ekstra kalkis    : " + ekstra);
        System.out.println("uzaklik          : " + x);
        System.out.println("yukseklik        : " + y);
        System.out.println("egim             : " + Math.floor(e * 100) / 100 + " (" + s1 + "-" + s2 + ")");
        System.out.println("maximum egim     : " + (maxAci - yolcu * yolcuAgirlik) + "\n");
        return (maxAci - yolcu * yolcuAgirlik) >= e;
    }

    private static Double flyWithCapasity(Sehir baslangic, Sehir bitis, int yolcu) {
        dijkstra(baslangic);
        List<Sehir> path = enKisaYol(bitis);
        if (bitis.enKisaMesafe != Double.POSITIVE_INFINITY) {
            System.out.println("Durum            : Deneniyor...");
            for (int i = 0; i < path.size() - 1; i++) {
                //birinci veya sondan bir onceki ise ekstra +50 rakim ekle (yukselme-alcalma)
                double ekstra = i == 0 || i == path.size() - 2 ? yerdenYukseklik : 0;
                //Eger iki sehir komsu ise kalkis masrafi yok
                if (path.size() == 2) {
                    ekstra = 0;
                }
                Sehir s1 = path.get(i);
                Sehir s2 = path.get(i + 1);
                double uzaklik = s1.uzakliklar.get(s1.komsuluklar.indexOf(s2));
                if (gidilebilirMi(uzaklik, s1, s2, ekstra, yolcu)) {
                    //System.out.println("Gidilebilir: " + s1 + "-" + s2);
                } else {
                    System.out.println("Gidilemeyen Yol  : " + path);
                    System.out.println("Burasi Cok Dik   : " + s1 + "-" + s2 + "\n\n");
                    s1.uzakliklar.set(s1.komsuluklar.indexOf(s2), Double.POSITIVE_INFINITY);
                    s2.uzakliklar.set(s2.komsuluklar.indexOf(s1), Double.POSITIVE_INFINITY);

                    //Yolu tekrar hesaplamak icin tum mesafeleri resetliyoruz
                    for (int j = 0; j < 81; j++) {
                        sehirler.get(j).enKisaMesafe = Double.POSITIVE_INFINITY;
                    }

                    return flyWithCapasity(baslangic, bitis, yolcu);
                }
            }
            System.out.println("Gidilen Yol      : " + path + "\n\n");
            Ekran.logla("Gidilen Yol: " + path);
            grandPath = path;
            return ((Math.round(bitis.enKisaMesafe * 10000) / 10000.0));

        } else {
            System.out.println("Gidilen Yol      : Gidilemedi.\n\n");
            Ekran.logla("Gidilemedi.\n\n");
            return Double.POSITIVE_INFINITY;
        }
    }

    public static String maksimumKarHesapla(int bs, int bt) {
        baslangic = bs;
        bitis = bt;
        grandPath = null;
        double maxKar = Double.NEGATIVE_INFINITY;
        int optimalYolcu = minYolcu;
        for (int i = minYolcu; i <= maxYolcu; i++) {
            init();
            System.out.println("\n\n\n\n" + i + " Kişi için ---------------------------------------");
            Ekran.logla("\n" + i + " Kişi için;");
            double newKM = flyWithCapasity(getSehir(baslangic), getSehir(bitis), i);
            double newKar = (yolcuUcreti * i) - (newKM * maliyet);
            if (maxKar <= newKar) {
                maxKar = newKar;
                optimalYolcu = i;
            }
        }

        init();
        flyWithCapasity(getSehir(baslangic), getSehir(bitis), optimalYolcu);
        if (grandPath != null) { //Yol var ise
            Ekran.drawSmt(grandPath);
            Ekran.logla("\n\n" + optimalYolcu + " yolcu ile gidilen yol: " + grandPath.toString());
            return (optimalYolcu + " yolcu ile yola çıkıldığında firma maksimum kâr'a ulaşır.\n" +
                    "Maksimum kâr: " + maxKar + "TL\n");
        } else {
            Ekran.drawSmt(null);
            return "Buraya hiçbir şekilde gidilemez. \n";
        }
    }

    public static String yuzdeElliKarHesapla(int bs, int bt, int yl) {
        baslangic = bs;
        bitis = bt;
        yolcu = yl;
        if (yolcu >= minYolcu && yolcu <= maxYolcu) {
            init();
            Ekran.logla(yolcu + " yolcu ile;");
            double km = flyWithCapasity(getSehir(bs), getSehir(bt), yolcu);
            if (km != Double.POSITIVE_INFINITY) {
                System.out.println("Toplamda         : " + km + " KM yol gidildi.\n\n");
                Ekran.logla("Toplamda " + km + " KM yol gidildi.");
                double seferMaliyeti = maliyet * km;
                double seferBasiYolcuUcreti = seferMaliyeti / yolcu;
                Ekran.drawSmt(grandPath);
                return "Kişi başı " + seferBasiYolcuUcreti * 2 + "TL alındığında firma %50 kâr'a ulaşır."; //Yuzde elli kar
            } else {
                return "";
            }

        } else {
            return "Yolcu sayisi verilen aralikta degil. (Girilen:" + yolcu + ", Aralik:[" + minYolcu + "," + maxYolcu + "])";
        }
    }
}