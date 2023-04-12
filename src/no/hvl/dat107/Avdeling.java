package no.hvl.dat107;

import java.io.Serializable;
import java.util.List;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;


@Entity
@Table(schema = "oblig3")
public class Avdeling implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int avdeling_id;
    private String navn;

    @OneToMany(mappedBy = "avdeling", fetch = FetchType.EAGER)
    private List<Ansatt> ansatte;

    @ManyToOne
    @JoinColumn(name = "sjef_id")
    private Ansatt sjef;

    public Avdeling() {
    	
    }

    public Avdeling(String navn, Ansatt sjef) {
        this.navn = navn;
        this.sjef = sjef;
        this.ansatte = new ArrayList<>();
        leggTilAnsatt(sjef);
    }


    public int getAvdeling_id() {
        return avdeling_id;
    }

    public void setAvdeling_id(int avdeling_id) {
        this.avdeling_id = avdeling_id;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public Ansatt getSjef() {
		return sjef;
	}

	public void setSjef(Ansatt sjef) {
		this.sjef = sjef;
	}

	public List<Ansatt> getAnsatte() {
        return ansatte;
    }

    public void setAnsatte(List<Ansatt> ansatte) {
        this.ansatte = ansatte;
    }


    public void leggTilAnsatt(Ansatt ansatt) {
        if (!ansatte.contains(ansatt)) {
            ansatte.add(ansatt);
            ansatt.setAvdeling(this);
        }
    }

    public void fjernAnsatt(Ansatt ansatt) {
        if (ansatte.contains(ansatt)) {
            ansatte.remove(ansatt);
            ansatt.setAvdeling(null);
        }
    }

    @Override
    public String toString() {
        return "AvdelingsID: " + avdeling_id +
                ", Navn: " + navn +
                ", Sjef: " + (sjef != null ? sjef.getFornavn() + " " + sjef.getEtternavn() : "Ingen") +
                ", Antall ansatte: " + ansatte.size();
    }
}


