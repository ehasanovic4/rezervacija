package ba.unsa.etf.rpr;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Evidencija implements Pretrazivanje{
    private List<Rezervacija> rezervacije = new ArrayList<>();


    public void rezervisi(Rezervacija rezervacija) {
        rezervacije.add(rezervacija);
    }

    public void rezervisi(List<Rezervacija> lista){
        rezervacije.addAll(lista);
    }


    public List<Rezervacija> dajSveRezervacije() {
        return rezervacije;
    }

    public void otkaziRezervaciju(Rezervacija rezervacija) {
        rezervacije.remove(rezervacija);
    }

    public void otkaziRezervacije(List<Rezervacija> rezervacije1) {
        rezervacije.removeAll(rezervacije1);
    }

    public void otkaziRezervacije(Predicate<Rezervacija> kriterij){
        List<Rezervacija> zaOtkazati = rezervacije.stream().filter(kriterij).collect(Collectors.toList());
        rezervacije.removeAll(zaOtkazati);
    }


    public Map<String, List<Rezervacija>> dajEvidenciju() {
        Map<String,List<Rezervacija>> rez=new HashMap<>();
        for(Rezervacija r : rezervacije){
            if(!rez.containsKey(r.getNazivSale())){
                rez.put(r.getNazivSale(),new ArrayList<>());
                for(Rezervacija r1 : rezervacije){
                    if(r1.getNazivSale().equals(r.getNazivSale()))
                        rez.get(r.getNazivSale()).add(r1);
                }
            }
        }
        return rez;
    }

    public List<Rezervacija> dajEvidencijuZaSalu(String nazivSale) {
        List<Rezervacija> rez=new ArrayList<>();
        boolean postoji=false;
        for(Rezervacija r : rezervacije){
            if(r.getNazivSale().equals(nazivSale)) {
                rez.add(r);
                postoji=true;
            }
        }
        if(!postoji) throw new IllegalArgumentException("Za salu "+ nazivSale+ "ne postoji evidentirane rezervacije");
        return rez;
    }

    @Override
    public List<Rezervacija> filtrirajPoKriteriju(Predicate<Rezervacija> kriterij) {
        return rezervacije.stream().filter(kriterij).collect(Collectors.toList());
    }

    @Override
    public List<Rezervacija> dajRezervacijeZaDan(String nazivSale, LocalDate datum) {
        proveriNazivSale(nazivSale);
        return rezervacije.stream().
                filter(rezervacija -> {
                    if(rezervacija.getNazivSale()!=nazivSale) return false;
                    if(rezervacija instanceof VandrednaRezervacija)
                        return ((VandrednaRezervacija) rezervacija).getDatum().equals(datum);
                    else {
                        return ((PeriodicnaRezervacija) rezervacija).getDan() == datum.getDayOfWeek().getValue()-1;
                        }
                }).collect(Collectors.toList());
    }

    private void proveriNazivSale(String nazivSale) {
        boolean postoji=false;
        for(Rezervacija r : rezervacije){
            if(r.getNazivSale().equals(nazivSale)) postoji=true;
        }
        if(!postoji) throw new IllegalArgumentException("Za salu "+ nazivSale+ "ne postoji evidentirane rezervacije");
    }

    @Override
    public List<Rezervacija> dajSortiraneRezervacije(String nazivSale, BiFunction<Rezervacija, Rezervacija, Integer> kriterij) {
        proveriNazivSale(nazivSale);
        return rezervacije.stream().filter(rezervacija -> rezervacija.getNazivSale().equals(nazivSale))
                .sorted(kriterij::apply).collect(Collectors.toList());
    }

    @Override
    public Set<Rezervacija> dajSortiranePoTipu(String nazivSale) {
        proveriNazivSale(nazivSale);
        return new TreeSet<>(rezervacije.stream().filter(rezervacija -> rezervacija.getNazivSale().equals(nazivSale)).collect(Collectors.toList()));
    }

    @Override
    public List<Rezervacija> dajRezervacijeNakon(String nazivSale, LocalDate datum) {
        proveriNazivSale(nazivSale);
        return rezervacije.stream().filter(rezervacija ->
                rezervacija instanceof VandrednaRezervacija &&
                rezervacija.getNazivSale().equals(nazivSale) &&
                        ((VandrednaRezervacija)rezervacija).getDatum().isAfter(datum))
                .collect(Collectors.toList());
    }

    @Override
    public boolean daLiJeSlobodna(String nazivSale, LocalDate datum, LocalTime pocetak, LocalTime kraj) {
        proveriNazivSale(nazivSale);
        if(kraj.isBefore(pocetak)){
            throw new IllegalArgumentException("Neispravni početak i kraj rezervacije");
        }

        return !rezervacije.stream().filter(rezervacija -> {
            if(!nazivSale.equals(rezervacija.getNazivSale())){
                return false;
            }

            if(rezervacija instanceof VandrednaRezervacija){
                return ((VandrednaRezervacija) rezervacija).getDatum().equals(datum) &&
                        !postojiPresjek(rezervacija.getPocetak(), rezervacija.getKraj(), pocetak,kraj);
            }else{
                return ((PeriodicnaRezervacija) rezervacija).getDan() == datum.getDayOfWeek().getValue() - 1 &&
                        !postojiPresjek(rezervacija.getPocetak(), rezervacija.getKraj(), pocetak,kraj);
            }
        }).collect(Collectors.toList()).isEmpty();
    }

    public static boolean postojiPresjek(LocalTime pocetak1, LocalTime kraj1, LocalTime pocetak2, LocalTime kraj2){
        // return ! k2 <= p1 || p2 >= k1;
        return !((kraj2.isBefore(pocetak1) || kraj2.equals(pocetak1)) || (pocetak2.isAfter(kraj1) || pocetak2.equals(kraj1)));
    }

    @Override
    public String toString() {
        int i=0;
        String rez="";
        for(Rezervacija r : rezervacije){
            rez+=r.toString();
            if(i!=rezervacije.size()-1) rez+='\n';
            i++;
        }
        return rez;
    }
}
