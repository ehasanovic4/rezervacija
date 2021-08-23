package ba.unsa.etf.rpr;

import javax.swing.text.DateFormatter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class VandrednaRezervacija extends Rezervacija {
    private LocalDate datum;

    public VandrednaRezervacija(String nazivSale, String predavac, LocalTime pocetak, LocalTime kraj, LocalDate datum) throws NeispravanFormatRezervacije {
        super(nazivSale, predavac, pocetak, kraj);
        this.datum = datum;
    }

    public LocalDate getDatum() {
        return datum;
    }

    public void setDatum(LocalDate datum) {
        this.datum = datum;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("kk:mm");
        String formatiraniPocetak = getPocetak().format(formatter);
        String formatiraniKraj = getKraj().format(formatter);

        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formatiraniDatum = getDatum().format(formatter1);

        return getNazivSale() + " - " + getPredavac() + " (vandredna) - početak: " + formatiraniPocetak +", kraj: " + formatiraniKraj +", na dan "+formatiraniDatum;
    }

    @Override
    public int compareTo(Rezervacija o) {
        if(o instanceof PeriodicnaRezervacija) return 1;

        return getDatum().compareTo(((VandrednaRezervacija)o).getDatum());
    }
}
