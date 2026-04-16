package no.hvl.dat110.poker;

import java.util.ArrayList;
import java.util.List;

/**
 * Representerer en deltaker i et Texas Hold'em-spill.
 * Klassen håndterer spillerens identitet, saldo (chips), og kortene på hånden,
 * samt handlinger som å satse, høyne eller kaste seg (fold).
 * * @author Gruppe [Ditt gruppenummer]
 * @version 1.2 (LCA-ferdigstilt)
 */
public class Player {

    /** Spillerens navn eller brukernavn. */
    private final String name;

    /** Spillerens gjenværende beholdning av chips. */
    private int chips;

    /** Spillerens private kort (hole cards). */
    private Hand hand;

    /** Spillerens nåværende tilstand i runden. */
    private PlayerStatus status;

    /** Spillerens nåværende bet */
    private int amountBet;

    /**
     * Konstruktør for å opprette en ny spiller.
     * * @param name Navnet på spilleren.
     * @param initialChips Startbeløpet spilleren får ved bordet.
     */
    public Player(String name, int initialChips) {
        this.name = name;
        this.chips = initialChips;
        this.hand = new Hand();
        this.status = PlayerStatus.ACTIVE;
    }

    /**
     * Intern hjelpemetode for å validere og gjennomføre chip-transaksjoner.
     * Sentraliserer logikken for å sjekke dekning på saldo.
     * * @param amount Beløpet som skal trekkes fra saldo.
     * @return {@code true} hvis transaksjonen ble gjennomført, {@code false} ved manglende dekning.
     */
    private boolean deductChips(int amount) {
        if (amount > chips) {
            return false;
        }
        chips -= amount;
        return true;
    }

    /**
     * Utfører en innsats (bet). Trekker beløpet fra spillerens saldo.
     * * @param amount Beløpet spilleren ønsker å satse.
     * @return {@code true} hvis innsatsen ble gjennomført, ellers {@code false}.
     */
    public boolean placeBet(int amount) {
        return deductChips(amount);
    }

    /**
     * Utfører en høyning (raise). Trekker beløpet fra spillerens saldo.
     * * @param amount Beløpet spilleren ønsker å høyne med.
     * @return {@code true} hvis høyningen ble gjennomført, ellers {@code false}.
     */
    public boolean raise(int amount) {
        return deductChips(amount);
    }

    /**
     * Utfører et "call" ved å matche den nåværende innsatsen på bordet.
     * * @param amountToMatch Beløpet som trengs for å syne.
     * @return {@code true} hvis spilleren hadde nok chips til å syne.
     */
    public boolean call(int amountToMatch) {
        return placeBet(amountToMatch);
    }

    /**
     * Markerer at spilleren har kastet seg (foldet) ved å oppdatere statusen.
     */
    public void fold() {
        this.status = PlayerStatus.FOLDED;
    }

    public void winChips(int amount) {
        this.chips += amount;
    }

    public void addChips(int amount) {
        this.chips += amount;
    }

    public void bet(int amount) {
        if (deductChips(amount)) {
            this.amountBet += amount;
        }
    }

    public int getBet() {
        return amountBet;
    }

    public void setBet(int amount) {
        this.amountBet = amount;
    }

    /**
     * Legger til et kort i spillerens hånd (hole card).
     * Navngitt etter prosjektets UML-diagram.
     * * @param card Kortet som skal deles ut.
     */
    public void addCard(Card card) {
        if (hand.getCard(0) == null || hand.getCard(1) == null) {
            hand.addCard(card);
        } else {
            System.err.println("Forsøk på å legge til for mange kort til " + name);
        }
    }

    /**
     * Fjerner alle kort fra spillerens hånd.
     */
    public void clearHand() {
        hand.emptyHand();
    }

    /**
     * Nullstiller spillerens status til ACTIVE og tømmer hånden før en ny runde starter.
     */
    public void resetForNewRound() {
        this.status = PlayerStatus.ACTIVE;
        clearHand();
    }

    /**
     * Sjekker om spilleren har kastet seg i inneværende runde.
     * * @return {@code true} hvis status er FOLDED.
     */
    public boolean isFolded() {
        return status == PlayerStatus.FOLDED;
    }

    // Gettere og Settere

    /** @return Spillerens navn. */
    public String getName() {
        return name;
    }

    /** @return En liste over spillerens nåværende kort. */
    public Hand getHand() {
        return hand;
    }

    /** @return Spillerens nåværende saldo. */
    public int getChips() {
        return chips;
    }

    /** @return Spillerens nåværende status. */
    public PlayerStatus getStatus() {
        return status;
    }

    /**
     * Oppdaterer spillerens status (f.eks. til ALL_IN).
     * @param status Den nye statusen til spilleren.
     */
    public void setStatus(PlayerStatus status) {
        this.status = status;
    }

    /** Oppdatere en spiller sin nåværende mengde chips satset **/
    public void setAmountBet(int amountBet) {
        this.amountBet = amountBet;
    }


    /** Returnere en spiller sin nåværende mengde chips satset **/
    public int getAmountBet() {
        return amountBet;
    }


    /**
     * Genererer en streng-representasjon av spillerens nåværende status.
     * @return Formatert streng med navn, saldo og kort.
     */
    @Override
    public String toString() {
        return String.format("%s [%d chips], Status: %s, Hand: %s", name, chips, status, hand);
    }
}
