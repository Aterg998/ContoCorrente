package it.betacom.model;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public abstract class Conto {

	private String titolare;
	private LocalDate dataApertura;
	private LocalDate dataChiusura;
	protected double saldo;
	protected List<Operazione> operazioni;

	static Random random = new Random();

	// costruttore
	public Conto(String titolare) {
		this.setTitolare(titolare);
		this.setDataApertura(LocalDate.of(2021, Month.JANUARY, 1)); // Imposta la data di apertura il 1° gennaio 2021
		this.setDataChiusura(LocalDate.of(2021, Month.DECEMBER, 31));

		this.saldo = 1000.0; // Saldo iniziale
		DecimalFormat formato = new DecimalFormat("#.##");
		saldo = Double.parseDouble(formato.format(saldo));
		this.operazioni = new ArrayList<>();
	}

	// ottengo la lista
	public List<Operazione> getOperazione() {
		return operazioni;
	}

	public abstract void simulaOperazioniCasuali();

	public static LocalDate generaDataCasuale2021(LocalDate dataApertura, List<Operazione> operazioniSimulate) {
		Random generator = new Random();

		// Calcola il numero di giorni rimanenti tra la data di apertura e la data
		// massima
		long giorniRimanenti = ChronoUnit.DAYS.between(dataApertura, LocalDate.of(2021, 12, 31));

		// Calcola un valore massimo per il generatore casuale basato sulla differenza
		// tra le date
		int maxRandomValue = (int) giorniRimanenti;

		// Genera un numero casuale di giorni da aggiungere alla data di apertura
		int giorniCasuali = generator.nextInt(maxRandomValue + 1);

		// Calcola la data casuale
		LocalDate dataCasuale = dataApertura.plusDays(giorniCasuali);

		// Ordina la lista di operazioni simulate
		operazioniSimulate.add(new Operazione("Dummy", 0, dataCasuale, 0));
		Collections.sort(operazioniSimulate);

		// Restituisci l'ultima data ordinata
		return operazioniSimulate.get(operazioniSimulate.size() - 1).getData();
	}

	public void versa(double importo, LocalDate dataCasuale) {
		saldo += importo;
		Operazione nuovaOperazione = new Operazione("Versamento", importo, dataCasuale, saldo);
		if (!operazioni.contains(nuovaOperazione)) {
			operazioni.add(nuovaOperazione);
		}
	}

	public void preleva(double importo, LocalDate dataCasuale) {
		saldo -= importo;
		Operazione nuovaOperazione = new Operazione("Prelievo", importo, dataCasuale, saldo);
		if (!operazioni.contains(nuovaOperazione)) {
			operazioni.add(nuovaOperazione);
		}
	}

	public double getSaldo() {
		return saldo;
	}

	public void stampaPDF() {
	}

	public LocalDate getDataApertura() {
		return dataApertura;
	}

	public void setDataApertura(LocalDate dataApertura) {
		this.dataApertura = dataApertura;
	}

	public String getTitolare() {
		return titolare;
	}

	public void setTitolare(String titolare) {
		this.titolare = titolare;
	}

	public LocalDate getDataChiusura() {
		return dataChiusura;
	}

	public void setDataChiusura(LocalDate localDate) {
		this.dataChiusura = localDate;
	}

}
