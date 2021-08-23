package ba.unsa.etf.rpr;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class PeriodicnaRezervacija extends Rezervacija {
    private int dan;

    public PeriodicnaRezervacija(String nazivSale, String predavac, LocalTime pocetak, LocalTime kraj, int dan) throws NeispravanFormatRezervacije {
        super(nazivSale, predavac, pocetak, kraj);
        setDan(dan);
    }

    public int getDan() {
        return dan;
    }

    public void setDan(int kojiDan) {
        if(kojiDan<0 || kojiDan>6) throw new IllegalArgumentException("Neispravan dan u sedmici");
        this.dan = kojiDan;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("kk:mm");
        String formatiraniPocetak = getPocetak().format(formatter);
        String formatiraniKraj = getKraj().format(formatter);

        String[] daniUSedmici = new String[]{"svakog ponedjeljka", "svakog utorka", "svake srijede",
                "svakog četvrtka", "svakog petka", "svake subote", "svake nedjelje"};

        return getNazivSale() + " - " + getPredavac() + " (periodična) - početak: " + formatiraniPocetak +", kraj: " + formatiraniKraj +", "+daniUSedmici[dan];
    }

    @Override
    public int compareTo(Rezervacija o) {
        if(o instanceof VandrednaRezervacija) return -1;

        return Integer.compare(this.getDan(), ((PeriodicnaRezervacija)o).getDan());
    }
}
