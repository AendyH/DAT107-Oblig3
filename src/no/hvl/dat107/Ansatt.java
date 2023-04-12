package no.hvl.dat107;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(schema = "oblig3")
public class Ansatt implements Serializable {
	
    private static final long serialVersionUID = 1L;
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ansatt_id;
    private String brukernavn;
    private String fornavn;
    private String etternavn;
    private LocalDate dato_for_ansettelse;
    private String stilling;
    private double manedslonn;
    private boolean erSjef;
    
    
    public Ansatt() {}
    
    public Ansatt(String brukernavn, String fornavn, String etternavn, 
    		LocalDate dato_for_ansettelse, String stilling,
    		double manedslonn, Avdeling avdeling, boolean erSjef) {
        this.brukernavn = brukernavn;
        this.fornavn = fornavn;
        this.etternavn = etternavn;
        this.dato_for_ansettelse = dato_for_ansettelse;
        this.stilling = stilling;
        this.manedslonn = manedslonn;
        this.avdeling = avdeling;
        this.erSjef = erSjef;
    }
    
    @ManyToOne
    @JoinColumn(name = "avdeling_id")
    private Avdeling avdeling;

    
	public int getId() {
		return ansatt_id;
	}
	public void setId(int id) {
		this.ansatt_id = id;
	}
	public String getBrukernavn() {
		return brukernavn;
	}
	public void setBrukernavn(String brukernavn) {
		this.brukernavn = brukernavn;
	}
	public String getFornavn() {
		return fornavn;
	}
	public void setFornavn(String fornavn) {
		this.fornavn = fornavn;
	}
	public String getEtternavn() {
		return etternavn;
	}
	public void setEtternavn(String etternavn) {
		this.etternavn = etternavn;
	}
	public LocalDate getDato_for_ansettelse() {
		return dato_for_ansettelse;
	}
	public void setDato_for_ansettelse(LocalDate dato_for_ansettelse) {
		this.dato_for_ansettelse = dato_for_ansettelse;
	}
	public String getStilling() {
		return stilling;
	}
	public void setStilling(String stilling) {
		this.stilling = stilling;
	}
	public double getManedslonn() {
		return manedslonn;
	}
	public void setManedslonn(double manedslonn) {
		this.manedslonn = manedslonn;
	}
	

	@Override
	public String toString() {
	    return 
	            " AnsattID: " + ansatt_id
	            + "\n Brukernavn: " + brukernavn 
	            + "\n Fornavn: " + fornavn 
	            + "\n Etternavn: " + etternavn 
	            + "\n Ansettelsedato: " + dato_for_ansettelse
	            + "\n Stilling: " + stilling 
	            + "\n Månedslønn: " + String.format("%,.2f", manedslonn) 
	            + "\n AvdelingsID: " + avdeling.getAvdeling_id()
	    		+ "\n Sjef: " + erSjef;
	}
	
    public Avdeling getAvdeling() {
        return avdeling;
    }

    public void setAvdeling(Avdeling avdeling) {
        this.avdeling = avdeling;
    }

    public boolean getErSjef() {
        return erSjef;
    }

    public void setErSjef(boolean erSjef) {
        this.erSjef = erSjef;
    }



	
	
	
}
