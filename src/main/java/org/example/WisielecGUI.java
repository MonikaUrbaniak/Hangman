package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class WisielecGUI extends JFrame {
    private static final String[] SLOWA = {"JAVA", "PROGRAMOWANIE", "KOMPUTER", "ALGORYTM", "GRAFIKA", "SIEC", "BAZA", "DANE", "KOD", "PYTHON", "LINUX", "FUNKCJA", "PETLA", "ZMIENNA", "KLASA", "OBIEKT", "TABLICA", "INTERFEJS", "KOMPILATOR", "DEBUGOWANIE", "SERWER", "PROCESOR", "ROUTER", "INTERNET", "PROTOKOL"};
    private static final int MAX_PROBY = 6;

    private String slowoDoZgadniecia;
    private char[] haslo;
    private Set<Character> niepoprawneLitery;
    private Set<Character> poprawneLitery;
    private int pozostaleProby;

    private JPanel panelHaslo;
    private JTextField textZgadywanie;
    private JButton buttonNoweSlowo;
    private JLabel labelNiepoprawne;
    private JLabel labelProby;
    private JLabel labelKomunikat;
    private JLabel labelOdgadnieteLitery;
    private SzubienicaPanel panelSzubienica;

    public WisielecGUI() {
        setTitle("Wisielec - Gra");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        panelHaslo = new JPanel(new FlowLayout());
        panelSzubienica = new SzubienicaPanel();
        panelSzubienica.setPreferredSize(new Dimension(400, 500));

        textZgadywanie = new JTextField(10);
        buttonNoweSlowo = new JButton("Nowe słowo");
        labelNiepoprawne = new JLabel("Niepoprawne litery: ");
        labelProby = new JLabel();
        labelKomunikat = new JLabel(" ", SwingConstants.CENTER);
        labelOdgadnieteLitery = new JLabel("Odgadnięte litery: ");

        textZgadywanie.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    zgadnij();
                }
            }
        });

        buttonNoweSlowo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nowaGra();
            }
        });

        // Szubienica po lewej stronie
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        add(panelSzubienica, gbc);

        // Hasło na górze
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        add(panelHaslo, gbc);

        // Pole do wpisania litery + przycisk
        JPanel panelGora = new JPanel(new FlowLayout());
        panelGora.add(new JLabel("Podaj literę lub całe słowo:"));
        panelGora.add(textZgadywanie);
        panelGora.add(buttonNoweSlowo);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        add(panelGora, gbc);

        // Informacje o grze (błędne litery, próby, komunikaty)
        JPanel panelInfo = new JPanel(new GridLayout(4, 1));
        panelInfo.add(labelNiepoprawne);
        panelInfo.add(labelProby);
        panelInfo.add(labelOdgadnieteLitery);
        panelInfo.add(labelKomunikat);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        add(panelInfo, gbc);

        nowaGra();
        setVisible(true);
    }

    private void nowaGra() {
        Random random = new Random();
        slowoDoZgadniecia = SLOWA[random.nextInt(SLOWA.length)];
        haslo = new char[slowoDoZgadniecia.length()];
        for (int i = 0; i < haslo.length; i++) {
            haslo[i] = '_';
        }
        niepoprawneLitery = new HashSet<>();
        poprawneLitery = new HashSet<>();
        pozostaleProby = MAX_PROBY;
        labelKomunikat.setText(" ");
        labelOdgadnieteLitery.setText("Odgadnięte litery: ");
        panelSzubienica.repaint();

        aktualizujWidok();
    }

    private void zgadnij() {
        String input = textZgadywanie.getText().toUpperCase().trim();
        textZgadywanie.setText("");

        if (!input.matches("[A-Z]+")) {
            JOptionPane.showMessageDialog(this, "Nie możesz używać cyfr ani znaków interpunkcyjnych!", "Błąd", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (input.length() == 1) {
            zgadnijLitere(input.charAt(0));
        } else {
            zgadnijSlowo(input);
        }
    }

    private void zgadnijLitere(char litera) {
        if (poprawneLitery.contains(litera) || niepoprawneLitery.contains(litera)) {
            labelKomunikat.setText("Litera '" + litera + "' została już wpisana!");
            return;
        }

        if (slowoDoZgadniecia.indexOf(litera) >= 0) {
            for (int i = 0; i < slowoDoZgadniecia.length(); i++) {
                if (slowoDoZgadniecia.charAt(i) == litera) {
                    haslo[i] = litera;
                }
            }
            poprawneLitery.add(litera);
        } else {
            niepoprawneLitery.add(litera);
            pozostaleProby--;
        }

        aktualizujWidok();
    }

    private void zgadnijSlowo(String slowo) {
        if (slowo.equals(slowoDoZgadniecia)) {
            haslo = slowo.toCharArray();
            aktualizujWidok();
            JOptionPane.showMessageDialog(this, "Gratulacje! Odgadłeś hasło: " + slowoDoZgadniecia);
            nowaGra();
        } else {
            JOptionPane.showMessageDialog(this, "Błędne hasło!");
            pozostaleProby--;
            aktualizujWidok();
        }
    }

    private void aktualizujWidok() {
        panelHaslo.removeAll();

        for (char c : haslo) {
            JLabel literaLabel = new JLabel(" " + c + " ");
            literaLabel.setFont(new Font("Arial", Font.BOLD, 24));
            panelHaslo.add(literaLabel);
        }

        panelHaslo.revalidate();
        panelHaslo.repaint();

        labelNiepoprawne.setText("Niepoprawne litery: " + niepoprawneLitery.toString());
        labelProby.setText("Pozostałe próby: " + pozostaleProby);
        labelOdgadnieteLitery.setText("Odgadnięte litery: " + poprawneLitery.toString());

        panelSzubienica.repaint();

        if (pozostaleProby == 0) {
            JOptionPane.showMessageDialog(this, "Przegrałeś, wisisz! Prawidłowe hasło to: " + slowoDoZgadniecia);
            nowaGra();
        }
    }

    class SzubienicaPanel extends JPanel {
        private Image obrazGlowy;

        public SzubienicaPanel() {
            // Wczytanie obrazka głowy
            obrazGlowy = new ImageIcon("C:\\Users\\miaus\\Desktop\\JAVA\\ProjectsJava\\5Wisielce\\WisielecGrafcznieFX\\src\\main\\resources\\Buzia.jpg").getImage();  // Upewnij się, że plik "glowa.png" jest w katalogu projektu
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            setBackground(Color.WHITE);
            g.setColor(Color.BLACK);

            // Rysowanie szubienicy
            g.drawLine(40, 460, 240, 460);
            g.drawLine(140, 460, 140, 100);
            g.drawLine(140, 100, 300, 100);
            g.drawLine(300, 100, 300, 160);

            // Wyświetlenie obrazka głowy zamiast okręgu
            if (pozostaleProby < 6 && obrazGlowy != null) {
                g.drawImage(obrazGlowy, 260, 160, 100, 100, this); // Współrzędne głowy
            }

            // Pozostałe części ciała
            if (pozostaleProby < 5) g.drawLine(300, 260, 300, 360);
            if (pozostaleProby < 4) g.drawLine(300, 270, 250, 320);
            if (pozostaleProby < 3) g.drawLine(300, 270, 350, 320);
            if (pozostaleProby < 2) g.drawLine(300, 360, 250, 460);
            if (pozostaleProby < 1) g.drawLine(300, 360, 350, 460);
        }
    }



    public static void main(String[] args) {
        new WisielecGUI();
    }
}