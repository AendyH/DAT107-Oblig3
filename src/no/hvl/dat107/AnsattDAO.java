package no.hvl.dat107;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

public class AnsattDAO {

    private EntityManagerFactory emf
            = Persistence.createEntityManagerFactory("oblig3PU");

    public Ansatt finnAnsattMedId(int id) {
        EntityManager em = emf.createEntityManager();
        Ansatt ansatt = null;
        try {
            ansatt = em.find(Ansatt.class, id);
            if (ansatt == null) {
                System.err.println("Ingenn ansatt med ID: " + id);
            }
        } finally {
            em.close();
        }
        return ansatt;
    }


    public Ansatt finnAnsattMedBrukernavn(String brukernavn) {
        EntityManager em = emf.createEntityManager();
        Ansatt ansatt = null;
        try {
            TypedQuery<Ansatt> query = em.createQuery("SELECT a FROM Ansatt a WHERE a.brukernavn = :brukernavn", Ansatt.class);
            query.setParameter("brukernavn", brukernavn);
            try {
                ansatt = query.getSingleResult();
            } catch (NoResultException e) {
                System.err.println("Ingen ansatt med brukernavn: " + brukernavn);
            }
        } finally {
            em.close();
        }
        return ansatt;
    }


    public List<Ansatt> finnAlleAnsatte() {
        EntityManager em = emf.createEntityManager();
        List<Ansatt> ansatte = null;
        try {
            TypedQuery<Ansatt> query = em.createQuery("SELECT a FROM Ansatt a", Ansatt.class);
            ansatte = query.getResultList();
        } finally {
            em.close();
        }
        return ansatte;
    }

    public void oppdaterAnsatt(int id, String nyStilling, double nyLonn, int avdelings_id, boolean ersjef) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            Ansatt ansatt = em.find(Ansatt.class, id);
            ansatt.setStilling(nyStilling);
            ansatt.setManedslonn(nyLonn);
            ansatt.setErSjef(ersjef);
            Avdeling avdeling = em.find(Avdeling.class, avdelings_id);
            ansatt.setAvdeling(avdeling);
            
            em.merge(ansatt);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public void leggTilAnsatt(Ansatt ansatt, int avdelings_id) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            
            Avdeling avdeling = em.find(Avdeling.class, avdelings_id);
            ansatt.setAvdeling(avdeling);
            
            em.persist(ansatt);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
    
    public void flyttAnsatt(int ansattId, int avdeling_id, boolean erSjef) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();

            Ansatt dbAnsatt = em.find(Ansatt.class, ansattId);
            Avdeling nyAvdeling = em.find(Avdeling.class, avdeling_id);

            if (dbAnsatt == null) {
                throw new RuntimeException("Ansatt med ID " + ansattId + " ikke funnet i databasen");
            }

            if (nyAvdeling == null) {
                throw new RuntimeException("Avdeling med ID " + avdeling_id + " ikke funnet i databasen");
            }

            dbAnsatt.setAvdeling(nyAvdeling);
            dbAnsatt.setErSjef(erSjef);

            if (erSjef) {
                nyAvdeling.setSjef(dbAnsatt);
            }

            em.merge(dbAnsatt);
            em.merge(nyAvdeling);
            tx.commit();

        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.err.println();
        } finally {
            em.close();
        }
    }


    




    

}
