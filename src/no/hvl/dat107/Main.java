package no.hvl.dat107;

import java.util.List;
import java.awt.Dimension;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.JOptionPane;
import jakarta.persistence.NoResultException;
import javax.swing.*;


public class Main {
    public static void main(String[] args) {
        AnsattDAO ansattDAO = new AnsattDAO();
        AvdelingDAO avdelingDAO = new AvdelingDAO();
        
        while (true) {
            String valg = JOptionPane.showInputDialog(
                    "Velg et alternativ:\n\n"
                    + "1. Søk etter ansatt på ansatt-id\n"
                    + "2. Søk etter ansatt på brukernavn (initialer)\n"
                    + "3. Utlisting av alle ansatte\n"
                    + "4. Oppdatere en ansatt sin stilling og/eller lønn\n"
                    + "5. Legge inn en ny ansatt\n"
                    + "6. Søk etter avdeling på avdelings-id\n"
                    + "7. Utlisting av alle avdelinger\n"
                    + "8. Utlisting av alle ansatte på en spesifikk avdeling\n"
                    + "9. Oppdatere hvilken avdeling en ansatt jobber på\n"
                    + "10. Legge inn en ny avdeling\n\n"
                    + "0. Avslutt programmet\n"
            );

            if (valg == null || valg.equals("0")) {
                break;
            }

            switch (valg) {
            case "1":
                try {
                    int ansattId = Integer.parseInt(JOptionPane.showInputDialog("Skriv inn ansatt-id:"));
                    Ansatt ansattMedId = ansattDAO.finnAnsattMedId(ansattId);
                    if (ansattMedId != null) {
                        JOptionPane.showMessageDialog(null, ansattMedId);
                    } else {
                        JOptionPane.showMessageDialog(null, "Ingen ansatt funnet.");
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Ugyldig tallformat.");
                }
                break;

            case "2":
                try {
                    String initialer = JOptionPane.showInputDialog("Skriv inn brukernavn(initialer):");
                    Ansatt ansattMedInitialer = ansattDAO.finnAnsattMedBrukernavn(initialer);
                    if (ansattMedInitialer != null) {
                        JOptionPane.showMessageDialog(null, ansattMedInitialer);
                    } else {
                        JOptionPane.showMessageDialog(null, "Ingen ansatt funnet.");
                    }
                } catch (NullPointerException e) {
                    JOptionPane.showMessageDialog(null, "Ugyldig ansatt-id.");
                }
                break;

                case "3":
                    List<Ansatt> ansatte = ansattDAO.finnAlleAnsatte();
                    StringBuilder ansattInfo = new StringBuilder();
                    for (Ansatt ansatt : ansatte) {
                        ansattInfo.append(ansatt.toString()).append("\n\n"); 
                    }
                    JTextArea textArea = new JTextArea(ansattInfo.toString());
                    JScrollPane scrollPane = new JScrollPane(textArea);
                    scrollPane.setPreferredSize(new Dimension(600, 400));
                    JOptionPane.showMessageDialog(null, scrollPane);
                    break;
                case "4":
                    int id = Integer.parseInt(JOptionPane.showInputDialog("Skriv inn ansatt-id:"));
                    String nyStilling = JOptionPane.showInputDialog("Skriv inn ny stilling (hvis ingen endring, trykk OK):");
                    int nyLonn = Integer.parseInt(JOptionPane.showInputDialog("Skriv inn ny lønn (hvis ingen endring, skriv 0):"));
                    int avdelings_id = Integer.parseInt(JOptionPane.showInputDialog("Skriv inn ny avdeling (hvis ingen endring, skriv 0):"));
                    Ansatt ansatt = ansattDAO.finnAnsattMedId(id);

                    if (nyStilling.isEmpty()) {
                        nyStilling = ansatt.getStilling(); 
                    }
                    if (nyLonn == 0) {
                        nyLonn = (int) ansatt.getManedslonn(); 
                    }
                    if (avdelings_id == 0) {
                        avdelings_id = ansatt.getAvdeling().getAvdeling_id(); 
                    }
                    

                    ansattDAO.oppdaterAnsatt(id, nyStilling, nyLonn, avdelings_id, ansatt.getErSjef());
                    JOptionPane.showMessageDialog(null, "Ansatt " + ansatt.getFornavn() + " " +
                    ansatt.getEtternavn() +  " med id " + id + " er oppdatert.");
                    break;
                case "5":
                    String brukernavn = JOptionPane.showInputDialog("Skriv inn brukernavn(initialer):");
                    String fornavn = JOptionPane.showInputDialog("Skriv inn fornavn:");
                    String etternavn = JOptionPane.showInputDialog("Skriv inn etternavn:");
                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    String dateString = JOptionPane.showInputDialog("Skriv inn dato for ansettelse (dd-MM-yyyy):");
                    LocalDate dato = LocalDate.parse(dateString, dateFormatter);
                    boolean ersjef = false;

                    String stilling = JOptionPane.showInputDialog("Skriv inn stilling:");

                    double lonn = Integer.parseInt(JOptionPane.showInputDialog("Skriv inn lønn:"));
                    int avdelingsid = Integer.parseInt(JOptionPane.showInputDialog("Skriv inn avdelingsid:"));

                    Avdeling avdeling = avdelingDAO.finnAvdelingMedId(avdelingsid); 
                    if (avdeling == null) {
                        JOptionPane.showMessageDialog(null, "Avdeling ikke funnet.");
                        break;
                    }

                    Ansatt nyAnsatt = new Ansatt(brukernavn, fornavn, etternavn, dato, stilling, lonn, avdeling, ersjef); 
                    ansattDAO.leggTilAnsatt(nyAnsatt, avdeling.getAvdeling_id());
                    JOptionPane.showMessageDialog(null, "Ny ansatt lagt til med id " + nyAnsatt.getId() + ".");
                    break;
                    
                case "6":
                    try {
                        int avdelingsId = Integer.parseInt(JOptionPane.showInputDialog("Skriv inn avdelings-id:"));
                        Avdeling avdelingMedId = avdelingDAO.finnAvdelingMedId(avdelingsId);
                        if (avdelingMedId != null) {
                            String avdelingInfo = "AvdelingID: " + avdelingMedId.getAvdeling_id() + "\n"
                                    + "Navn: " + avdelingMedId.getNavn() + "\n"
                                    + "Antall ansatte: " + avdelingDAO.finnAntallAnsatteIAvdeling(avdelingMedId.getAvdeling_id())+ "\n" 
                                    + "Sjef: " + (avdelingMedId.getSjef() != null ? avdelingMedId.getSjef().getFornavn() 
                                    + " " + avdelingMedId.getSjef().getEtternavn() + ", AnsattID: " + avdelingMedId.getSjef().getId()
                                   
                                    : "Ingen") + "\n";
                                    
                            JOptionPane.showMessageDialog(null, avdelingInfo);
                        } else {
                            JOptionPane.showMessageDialog(null, "Ingen avdeling funnet.");
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Ugyldig tallformat.");
                    }
                    break;
                case "7":
                    List<Avdeling> avdelinger = avdelingDAO.finnAlleAvdelinger();
                    StringBuilder avdelingInfo = new StringBuilder();
                    for (Avdeling avdelings : avdelinger) {
                        avdelingInfo.append("AvdelingsID: ").append(avdelings.getAvdeling_id()).append("\n")
                        			.append("Navn: ").append(avdelings.getNavn()).append("\n")
                        			.append("Antall ansatte: ").append(avdelingDAO.finnAntallAnsatteIAvdeling(avdelings.getAvdeling_id())).append("\n")                             
                                    .append("Sjef: ").append(avdelings.getSjef() != null ? 
                                        avdelings.getSjef().getFornavn() + " " + avdelings.getSjef().getEtternavn() : "Ingen")
                                    .append(", AnsattID(").append(avdelings.getSjef().getId()).append(") ").append("\n"+"\n");
                                    
                    }
                    JTextArea textArea2 = new JTextArea(avdelingInfo.toString());
                    JScrollPane scrollPane2 = new JScrollPane(textArea2);
                    scrollPane2.setPreferredSize(new Dimension(600, 400));
                    JOptionPane.showMessageDialog(null, scrollPane2);
                    break;
                case "8":
                    try {
                        int avdelingsId = Integer.parseInt(JOptionPane.showInputDialog("Skriv inn avdelings-id:"));
                        Avdeling avdeling1 = avdelingDAO.finnAvdelingMedId(avdelingsId);
                        if (avdeling1 != null) {
                            List<Ansatt> ansatte1 = avdelingDAO.finnAlleAnsatteIAvdeling(avdelingsId);
                            StringBuilder ansattInfo1 = new StringBuilder();
                            ansattInfo1.append("AvdelingsID: ").append(avdeling1.getAvdeling_id()).append("\n")
                                    .append("Navn: ").append(avdeling1.getNavn()).append("\n")
                                    .append("Sjef: ").append(avdeling1.getSjef() != null ? avdeling1.getSjef().getFornavn() + " " + avdeling1.getSjef().getEtternavn() : "Ingen").append("\n\n")
                                    .append("Sjef-AnsattID: ").append(avdeling1.getSjef().getId()).append("\n")
                                    .append("Ansatte:").append("\n");
                            for (Ansatt ansatt1 : ansatte1) {
                                ansattInfo1.append(ansatt1.getFornavn()).append(" ").append(ansatt1.getEtternavn()).append(", AnsattID: " + ansatt1.getId())
                                .append("\n");
                            }
                            JTextArea textArea3 = new JTextArea(ansattInfo1.toString());
                            JScrollPane scrollPane3 = new JScrollPane(textArea3);
                            scrollPane3.setPreferredSize(new Dimension(600, 400));
                            JOptionPane.showMessageDialog(null, scrollPane3);
                        } else {
                            JOptionPane.showMessageDialog(null, "Avdeling ikke funnet.");
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Ugyldig tallformat.");
                    }
                    break;
                case "9":
                    try {
                        int ansattId = Integer.parseInt(JOptionPane.showInputDialog("Skriv inn ansatt-id:"));
                        Ansatt ansatt2 = ansattDAO.finnAnsattMedId(ansattId);
                        String gammelavdeling = ansatt2.getAvdeling().getNavn();
                        int gammelavdelingid = ansatt2.getAvdeling().getAvdeling_id();

                        if (ansatt2.getErSjef()) {
                            JOptionPane.showMessageDialog(null, "Sjefen kan ikke bytte avdeling.");
                            break;
                        }

                        int avdelingId = Integer.parseInt(JOptionPane.showInputDialog("Skriv inn ny avdelingsid:"));
                        Avdeling nyAvdeling = avdelingDAO.finnAvdelingMedId(avdelingId);

                        if (nyAvdeling == null) {
                            JOptionPane.showMessageDialog(null, "Avdeling ikke funnet.");
                            break;
                        }
                        
                        if (nyAvdeling.getAvdeling_id() == gammelavdelingid) {
                            JOptionPane.showMessageDialog(null, "Ansatt er allerede i denne avdelingen.");
                            break;
                        }

                        ansatt2.setAvdeling(nyAvdeling);
                        ansattDAO.oppdaterAnsatt(ansatt2.getId(), ansatt2.getStilling(), ansatt2.getManedslonn(), ansatt2.getAvdeling().getAvdeling_id(), false);

                        JOptionPane.showMessageDialog(null, "Ansatt " + ansatt2.getFornavn() + " " 
                        + ansatt2.getEtternavn() + " med id " + ansatt2.getId() + " har byttet fra " 
                        		+ gammelavdeling + " (" + gammelavdelingid + ") til avdeling " + ansatt2.getAvdeling().getNavn()
                        + " (" + ansatt2.getAvdeling().getAvdeling_id() + ").");
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Ugyldig tallformat.");
                    } catch (NoResultException e) {
                        JOptionPane.showMessageDialog(null, "Ingen ansatt funnet.");
                    } catch (NullPointerException e) {
                        JOptionPane.showMessageDialog(null, "Ugyldig ansatt-id.");
                    }
                    break;
                case "10":
                    String navn = JOptionPane.showInputDialog("Skriv inn navn på avdelingen:");
                    
                    if (avdelingDAO.finnAvdelingMedNavn(navn) != null) {
                        JOptionPane.showMessageDialog(null, "Avdelingsnavnet er allerede tatt.");
                        break;
                    }
                    
                    int sjefId = Integer.parseInt(JOptionPane.showInputDialog("Skriv inn id til sjefen for avdelingen:"));

                    Ansatt sjef = ansattDAO.finnAnsattMedId(sjefId);
                    if (sjef == null) {
                        JOptionPane.showMessageDialog(null, "Kunne ikke finne en ansatt med angitt id.");
                        break;
                    }
                    if (sjef.getErSjef()) {
                        JOptionPane.showMessageDialog(null, "Den ansatte med angitt id er allerede sjef i en annen avdeling.");
                        break;
                    }

                    Avdeling nyAvdeling = new Avdeling(navn, sjef);
                    avdelingDAO.leggTilAvdeling(nyAvdeling, sjef);

                    Avdeling gammelAvdeling = sjef.getAvdeling();
                
                    gammelAvdeling.fjernAnsatt(sjef);
                    
                    sjef.setAvdeling(nyAvdeling);
                    nyAvdeling.leggTilAnsatt(sjef);
                    ansattDAO.flyttAnsatt(sjef.getId(), nyAvdeling.getAvdeling_id(), true);

                    JOptionPane.showMessageDialog(null, "Avdeling med navn " + nyAvdeling.getNavn() + " opprettet med sjef " + sjef.getFornavn() + " " + sjef.getEtternavn() + ".");
                    break;

                default:
                    JOptionPane.showMessageDialog(null, "Ugyldig valg.");
                }
            }
        }
}
