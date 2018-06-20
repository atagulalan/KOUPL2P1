import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Ekran extends JFrame {

    public static int yazilacakSehir = 1; //Tek sayilar birinci, Cift sayilar ikinci sehri aktive eder
    public static JPanel anaPanel = new JPanel();
    public static cizilebilirHarita harita = new cizilebilirHarita();
    public static JTextArea log = new JTextArea();

    //Renkler
    //Kaynak: https://coolors.co/f45b69-f6e8ea-22181c-5a0001-f13030
    public static Color Red = new Color(0xF13030);
    public static Color ActiveRed = new Color(0xF24242);
    public static Color FocusRed = new Color(0xF45B69);
    public static Color White = new Color(0xffffff);
    public static Color NotSoWhite = new Color(0xcccccc);
    public static Color Background = new Color(0x22181C);
    public static Color DarkBackground = new Color(0x191215);


    //Kaynak: https://stackoverflow.com/questions/27706197/how-can-i-center-graphics-drawstring-in-java
    public static void drawCenteredString(Graphics g, String text, Rectangle rect, Font font) {
        FontMetrics metrics = g.getFontMetrics(font);
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        g.setFont(font);
        g.drawString(text, x, y);
    }


    public static class stilliButon extends JButton {
        public static Color COLOR = Red;
        public String text = "";
        public boolean active = false;

        @Override
        public String toString() {
            return this.text;
        }

        public stilliButon(String text) {
            this.text = text;
            setBorderPainted(false);
            setOpaque(false);
            setBackground(Red);
            addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    setBackground(ActiveRed);
                }

                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    setBackground(ActiveRed);
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    setBackground(Red);
                }

                public void mousePressed(java.awt.event.MouseEvent evt) {
                    setBackground(Red);
                }
            });
        }

        @Override
        public void setBackground(Color c) {
            if (!this.active) {
                COLOR = c;
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            RenderingHints qualityHints =
                    new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            qualityHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.setRenderingHints(qualityHints);
            g2.setPaint(COLOR);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
            g2.setPaint(White);
            drawCenteredString(g2, this.text, new Rectangle(getWidth(), getHeight()), getMontserrat("SemiBold"));
            g2.dispose();
        }

    }

    public static class altiCizgiliInput extends JTextField {
        public altiCizgiliInput(String ad, Dimension dim, int x, int y, String initialValue) {
            setCaretColor(White);
            setForeground(White);
            setOpaque(false);
            setFont(getMontserrat("Regular"));
            setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, Red));
            JLabel label = new JLabel(ad);
            setSize(dim);
            setLocation(x, y);
            anaPanel.add(this);
            label.setForeground(NotSoWhite);
            label.setFont(getMontserrat("Regular"));
            label.setSize(dim);
            label.setLocation(x, y - 20);
            setText(initialValue);
            anaPanel.add(label);
        }

        public altiCizgiliInput() {
            setCaretColor(White);
            setForeground(White);
            setOpaque(false);
            setFont(getMontserrat("Regular"));
            setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, Red));
        }
    }

    //Kaynak: https://docs.oracle.com/javase/7/docs/api/javax/swing/plaf/basic/BasicScrollBarUI.html
    public static class sbUI extends BasicScrollBarUI {
        @Override
        protected JButton createDecreaseButton(int orientation) {
            return createZeroButton();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return createZeroButton();
        }

        private JButton createZeroButton() {
            JButton jbutton = new JButton();
            jbutton.setPreferredSize(new Dimension(0, 0));
            jbutton.setMinimumSize(new Dimension(0, 0));
            jbutton.setMaximumSize(new Dimension(0, 0));
            return jbutton;
        }

        @Override
        protected void configureScrollBarColors() {
            scrollBarWidth = 5;
            this.thumbHighlightColor = Red;
            this.thumbLightShadowColor = Red;
            this.thumbDarkShadowColor = Red;
            this.thumbColor = Red;
            this.trackColor = DarkBackground;
            this.trackHighlightColor = Red;
        }
    }

    public static class cizilebilirHarita extends JPanel {

        private BufferedImage image;

        public cizilebilirHarita() {
            try {
                image = ImageIO.read(new File(".\\tr.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            image = process(image);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(image.getWidth(), image.getHeight());
        }


        private BufferedImage process(BufferedImage old) {
            int w = old.getWidth();
            int h = old.getHeight();
            BufferedImage img = new BufferedImage(
                    w, h, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = img.createGraphics();
            g2d.drawImage(old, 0, 0, w, h, this);

            g2d.dispose();
            return img;
        }

        public void drawNew() {
            try {
                image = ImageIO.read(new File(".\\tr.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void drawSmt(int x1, int y1, int x2, int y2) {
            Graphics2D g2d = image.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setPaint(Red);
            g2d.setStroke(new BasicStroke(3));
            g2d.draw(new Line2D.Float(x1, y1, x2, y2));
            this.image = this.process(image);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, null);
        }
    }


    //Kaynak: https://docs.oracle.com/javase/tutorial/2d/text/fonts.html
    public static Font getMontserrat(String boldness) {
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File(".\\Montserrat-" + boldness + ".ttf"));
            return font.deriveFont(Font.PLAIN, 13);
        } catch (Exception e) {
            System.out.println("Font yuklenirken hata!");
            return new Font("Arial", Font.PLAIN, 13);
        }
    }

    public static void rotaEkle(Sehir sehir, altiCizgiliInput s1, altiCizgiliInput s2) {
        if (yazilacakSehir % 2 == 0) {
            s1.setText(sehir.getPlaka() + "");
            s1.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, Red));
            s2.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, FocusRed));
        } else {
            s2.setText(sehir.getPlaka() + "");
            s1.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, FocusRed));
            s2.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, Red));
        }
    }

    public static void drawSmt(List<Sehir> grandPath) {
        harita.drawNew();
        if (grandPath != null) {
            for (int i = 0; i < grandPath.size() - 1; i++) {
                harita.drawSmt(
                        grandPath.get(i).getX(),
                        grandPath.get(i).getY(),
                        grandPath.get(i + 1).getX(),
                        grandPath.get(i + 1).getY()
                );
            }
        }
        anaPanel.repaint();
    }

    public static void logla(String x) {
        log.setText(log.getText() + x + "\n");
    }

    public static void logla(String x, String y) {
        log.setText(log.getText() + x + y);
    }

    public static void logClr() {
        log.setText("");
    }

    public static void main(String[] args) {

        JFrame frame = new JFrame("Gezgin Zeplin");
        frame.setSize(1024, 760);

        //https://stackoverflow.com/questions/2442599/how-to-set-jframe-to-appear-centered-regardless-of-monitor-resolution
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(screenSize.width / 2 - frame.getSize().width / 2, screenSize.height / 2 - frame.getSize().height / 2);
        //---------

        anaPanel.setLayout(null);

        harita.setSize(1024, 500);
        harita.setLocation(0, 0);
        harita.setBackground(Background);

        altiCizgiliInput sehir1 = new altiCizgiliInput("Bşlm", new Dimension(40, 30), 50, 650, "");
        altiCizgiliInput sehir2 = new altiCizgiliInput("Bitş", new Dimension(40, 30), 110, 650, "");
        sehir1.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, FocusRed));
        altiCizgiliInput yolcuSayisi = new altiCizgiliInput("Yolcu Sayısı", new Dimension(100, 30), 50, 500, "5");
        altiCizgiliInput rakimCarpani = new altiCizgiliInput("Rakım Çarpanı", new Dimension(100, 30), 50, 550, "1000");
        altiCizgiliInput yerdenYukseklik = new altiCizgiliInput("Yükseklik", new Dimension(100, 30), 50, 600, "50");
        altiCizgiliInput maxYolcu = new altiCizgiliInput("Max Yolcu", new Dimension(100, 30), 170, 500, "50");
        altiCizgiliInput minYolcu = new altiCizgiliInput("Min Yolcu", new Dimension(100, 30), 170, 550, "5");
        altiCizgiliInput yolcuUcreti = new altiCizgiliInput("Yolcu Ücreti", new Dimension(100, 30), 170, 600, "20");
        altiCizgiliInput yolcuAgirlik = new altiCizgiliInput("Yolcu Ağırlık", new Dimension(100, 30), 290, 500, "1");
        altiCizgiliInput maxAci = new altiCizgiliInput("Max Açı", new Dimension(100, 30), 290, 550, "80");
        altiCizgiliInput maliyet = new altiCizgiliInput("Maliyet", new Dimension(100, 30), 290, 600, "10");

        //Log kısmı
        JScrollPane sp = new JScrollPane(log);
        DefaultCaret caret = (DefaultCaret) log.getCaret();
        log.setLineWrap(true);
        log.setWrapStyleWord(true);
        log.setBackground(Background);
        log.setForeground(White);
        log.setFont(getMontserrat("Regular"));
        log.setCaretColor(White);
        sp.getVerticalScrollBar().setUI(new sbUI());
        sp.getVerticalScrollBar().setBorder(BorderFactory.createEmptyBorder());
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.setLocation(420, 505);
        sp.setSize(550, 175);
        sp.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, Red));
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        JLabel label = new JLabel();
        label.setText("Log");
        label.setForeground(NotSoWhite);
        label.setFont(getMontserrat("Regular"));
        label.setSize(550, 30);
        label.setLocation(420, 480);
        anaPanel.add(label);
        anaPanel.add(sp);


        stilliButon hesapla = new stilliButon("Max. Hesapla");
        hesapla.setSize(100, 30);
        hesapla.setLocation(170, 650);

        stilliButon hesapla2 = new stilliButon("%50 Hesapla");
        hesapla2.setSize(100, 30);
        hesapla2.setLocation(290, 650);

        //Sehir butonlarını koy
        Zeplin.init();
        for (int k = 0; k < 1; k++) {
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    stilliButon sehirButton = new stilliButon(((j * 9) + i + 1) + "");
                    sehirButton.setSize(20, 20);
                    int x = Zeplin.getSehir((j * 9) + i + 1).getX();
                    int y = Zeplin.getSehir((j * 9) + i + 1).getY();
                    sehirButton.setLocation(x - 10, y - 10); //-20(boyut)/2 (ortalamak icin)
                    sehirButton.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mousePressed(java.awt.event.MouseEvent evt) {
                            yazilacakSehir++;
                            rotaEkle(Zeplin.getSehir(Integer.parseInt(evt.getComponent().toString())), sehir1, sehir2);
                        }
                    });
                    anaPanel.add(sehirButton);
                }
            }
        }


        sehir1.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (yazilacakSehir % 2 == 0) {
                    yazilacakSehir++;
                    sehir2.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, Red));
                    sehir1.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, FocusRed));
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
            }
        });
        sehir2.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (yazilacakSehir % 2 == 1) {
                    yazilacakSehir++;
                    sehir1.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, Red));
                    sehir2.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, FocusRed));
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
            }
        });

        hesapla.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                if (!(rakimCarpani.getText().equals("") ||
                        minYolcu.getText().equals("") ||
                        maxYolcu.getText().equals("") ||
                        yolcuUcreti.getText().equals("") ||
                        yolcuAgirlik.getText().equals("") ||
                        maxAci.getText().equals("") ||
                        yerdenYukseklik.getText().equals("") ||
                        sehir1.getText().equals("") ||
                        sehir2.getText().equals("") ||
                        maliyet.getText().equals("") ||
                        yolcuSayisi.getText().equals(""))) {

                    Zeplin.putGlobals(
                            Integer.parseInt(rakimCarpani.getText()),
                            Integer.parseInt(minYolcu.getText()),
                            Integer.parseInt(maxYolcu.getText()),
                            Double.parseDouble(yolcuUcreti.getText()),
                            Double.parseDouble(yolcuAgirlik.getText()),
                            Double.parseDouble(maxAci.getText()),
                            Double.parseDouble(yerdenYukseklik.getText()),
                            Double.parseDouble(maliyet.getText()),
                            Integer.parseInt(yolcuSayisi.getText())
                    );
                    logClr();
                    logla(Zeplin.maksimumKarHesapla(Integer.parseInt(sehir1.getText()), Integer.parseInt(sehir2.getText())));
                } else {
                    logla("\nZorunlu alanlardan en az biri boş gibi görünüyor.");
                }
            }
        });

        hesapla2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                if (!(rakimCarpani.getText().equals("") ||
                        minYolcu.getText().equals("") ||
                        maxYolcu.getText().equals("") ||
                        yolcuUcreti.getText().equals("") ||
                        yolcuAgirlik.getText().equals("") ||
                        maxAci.getText().equals("") ||
                        yerdenYukseklik.getText().equals("") ||
                        sehir1.getText().equals("") ||
                        sehir2.getText().equals("") ||
                        maliyet.getText().equals("") ||
                        yolcuSayisi.getText().equals(""))) {

                    Zeplin.putGlobals(
                            Integer.parseInt(rakimCarpani.getText()),
                            Integer.parseInt(minYolcu.getText()),
                            Integer.parseInt(maxYolcu.getText()),
                            Double.parseDouble(yolcuUcreti.getText()),
                            Double.parseDouble(yolcuAgirlik.getText()),
                            Double.parseDouble(maxAci.getText()),
                            Double.parseDouble(yerdenYukseklik.getText()),
                            Double.parseDouble(maliyet.getText()),
                            Integer.parseInt(yolcuSayisi.getText())
                    );
                    logClr();
                    logla(Zeplin.yuzdeElliKarHesapla(Integer.parseInt(sehir1.getText()), Integer.parseInt(sehir2.getText()), Integer.parseInt(yolcuSayisi.getText())), "");
                } else {
                    logla("\nZorunlu alanlardan en az biri boş gibi görünüyor.");
                }
            }
        });


        anaPanel.add(harita);
        anaPanel.add(hesapla);
        anaPanel.add(hesapla2);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        anaPanel.setBackground(Background);
        frame.setResizable(false);
        frame.setContentPane(anaPanel);
        frame.setVisible(true);


    }
}
