package no.hvl.dat107;


import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

public class AvdelingDAO {

    private EntityManagerFactory emf;

    public AvdelingDAO() {
        emf = Persistence.createEntityManagerFactory("oblig3PU");
    }

    public Avdeling finnAvdelingMedId(int avdelings_id) {
        EntityManager em = emf.createEntityManager();
        Avdeling avdeling = null;

        try {
            avdeling = em.find(Avdeling.class, avdelings_id);
        } finally {
            em.close();
        }

        return avdeling;
    }
    
    public Avdeling finnAvdelingMedNavn(String navn) {
        EntityManager em = emf.createEntityManager();

        try {
            TypedQuery<Avdeling> query = em.createQuery("SELECT a FROM Avdeling a WHERE a.navn = :navn", Avdeling.class);
            query.setParameter("navn", navn);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    
    public void setSjef(int avdelingId, int ansattId) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            Avdeling avdeling = em.find(Avdeling.class, avdelingId);
            Ansatt ansatt = em.find(Ansatt.class, ansattId);
            Ansatt eksisterendeSjef = avdeling.getSjef();

            if (eksisterendeSjef != null) {
                eksisterendeSjef.setErSjef(false);
                em.merge(eksisterendeSjef);
            }

            ansatt.setErSjef(true);
            ansatt.setAvdeling(avdeling);
            avdeling.setSjef(ansatt);

            em.merge(ansatt);
            em.merge(avdeling);

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public Ansatt getSjef(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Ansatt> query = em.createQuery(
                    "SELECT a FROM Ansatt a WHERE a.avdeling = :avdeling AND a.erSjef = true", Ansatt.class);
            query.setParameter("avdeling", this);
            List<Ansatt> result = query.getResultList();
            for (Ansatt ansatt : result) {
                if (ansatt.getId() == id) {
                    return ansatt;
                }
            }
            return null;
        } finally {
            em.close();
        }
    }


    public List<Avdeling> finnAlleAvdelinger() {
        EntityManager em = emf.createEntityManager();
        List<Avdeling> avdelinger = null;

        try {
            TypedQuery<Avdeling> query = em.createQuery("SELECT a FROM Avdeling a", Avdeling.class);
            avdelinger = query.getResultList();
        } finally {
            em.close();
        }

        return avdelinger;
    }
    
    public int finnAntallAnsatteIAvdeling(int avdelingId) {
        EntityManager em = emf.createEntityManager();

        try {
            TypedQuery<Long> query = em.createQuery("SELECT COUNT(a) FROM Ansatt a WHERE a.avdeling.avdeling_id = :avdelingId", Long.class);
            query.setParameter("avdelingId", avdelingId);
            return query.getSingleResult().intValue();
        } finally {
            em.close();
        }
    }

    public List<Ansatt> finnAlleAnsatteIAvdeling(int avdelings_id) {
        EntityManager em = emf.createEntityManager();
        List<Ansatt> ansatte = null;

        try {
            TypedQuery<Ansatt> query = em.createQuery("SELECT a FROM Ansatt a WHERE a.avdeling.avdeling_id = :avdelings_id", Ansatt.class);
            query.setParameter("avdelings_id", avdelings_id);
            ansatte = query.getResultList();
        } finally {
            em.close();
        }

        return ansatte;
    }

    public void leggTilAvdeling(Avdeling avdeling, Ansatt sjef) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            Ansatt eksisterendeSjef = sjef.getAvdeling().getSjef();
            if (eksisterendeSjef != null) {
                eksisterendeSjef.setErSjef(false);
                em.merge(eksisterendeSjef);
            }

            sjef.setErSjef(true);
            avdeling.setSjef(sjef);
            avdeling.leggTilAnsatt(sjef);
            em.persist(avdeling);

            sjef.setAvdeling(avdeling);
            em.merge(sjef);

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void close() {
        emf.close();
    }
}
