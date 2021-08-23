package ba.unsa.etf.rpr;

import java.time.LocalTime;
import java.util.Objects;

public abstract class Rezervacija implements Comparable<Rezervacija>{
    private String nazivSale;
    private String predavac;
    private LocalTime pocetak,kraj;

    public Rezervacija(String nazivSale, String predavac, LocalTime pocetak, LocalTime kraj) throws NeispravanFormatRezervacije {
        if(kraj.isBefore(pocetak)) throw new NeispravanFormatRezervacije("Neispravan format početka i kraja rezervacije");
        this.nazivSale = nazivSale;
        this.predavac = predavac;
        this.pocetak = pocetak;
        this.kraj = kraj;
    }

    public String getNazivSale() {
        return nazivSale;
    }

    public void setNazivSale(String nazivSale) {
        this.nazivSale = nazivSale;
    }

    public String getPredavac() {
        return predavac;
    }

    public void setPredavac(String predavac) {
        this.predavac = predavac;
    }

    public LocalTime getPocetak() {
        return pocetak;
    }

    public void setPocetak(LocalTime pocetak) throws NeispravanFormatRezervacije {
        if(getKraj().isBefore(pocetak)) throw new NeispravanFormatRezervacije("Neispravan format početka i kraja rezervacije");
        this.pocetak = pocetak;
    }

    public LocalTime getKraj() {
        return kraj;
    }

    public void setKraj(LocalTime kraj) throws NeispravanFormatRezervacije {
        if(kraj.isBefore(getPocetak())) throw new NeispravanFormatRezervacije("Neispravan format početka i kraja rezervacije");
        this.kraj = kraj;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rezervacija that = (Rezervacija) o;
        return Objects.equals(getNazivSale(), that.getNazivSale()) &&
                Objects.equals(getPredavac(), that.getPredavac()) &&
                Objects.equals(getPocetak(), that.getPocetak()) &&
                Objects.equals(getKraj(), that.getKraj());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNazivSale(), getPredavac(), getPocetak(), getKraj());
    }

    @Override
    public abstract int compareTo(Rezervacija o);
}
