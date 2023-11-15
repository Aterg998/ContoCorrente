package it.betacom.model;


import java.time.LocalDate;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class ContoDeposito extends Conto {

	private static final double TASSO_INTERESSE = 0.10;
	private double limitePrelievo = 1000.0;
	private static final Logger logger = LogManager.getLogger(ContoDeposito.class);

	public ContoDeposito(String titolare) {
		super(titolare);
		this.operazioni = new ArrayList<>(); // Inizializza la lista delle operazioni per il conto deposito
	}

	@Override
	public void preleva(double importo, LocalDate dataCasuale) {
		if (importo <= limitePrelievo) {
			double saldoIniziale = saldo;
			saldo -= importo;
			operazioni.add(new Operazione("Prelievo", importo, dataCasuale, saldo, saldoIniziale));
		}
	}

	public void generaInteressi(LocalDate fineAnno) {
		
		Collections.sort(operazioni, Comparator.comparing(Operazione::getData));

		double interessi = 0.0;
		LocalDate dataIniziale = getDataApertura();
		LocalDate dataChiusura = fineAnno;

		if (operazioni.size() > 0) { // verificato se ci sono operazioni nel conto
			if (operazioni.get(0).getData().isAfter(dataIniziale)) {
				
				long giorni = ChronoUnit.DAYS.between(dataIniziale, operazioni.get(0).getData());
				//System.out.println(giorni);
				interessi += operazioni.get(0).getSaldoIniziale() * (TASSO_INTERESSE / 365.0) * giorni;
			}
			
			for (int i = 0; i < operazioni.size(); i++) {
				Operazione operazioneCorrente = operazioni.get(i); // otteniamo la singola operazione della lista
																	// utilizzando l'indice i
				LocalDate dataOperazioneCorrente = operazioneCorrente.getData(); // prendiamo la data

				LocalDate dataOperazioneSuccessiva = (i < operazioni.size() - 1) ? operazioni.get(i + 1).getData()
						: fineAnno; // Otteniamo la data dell'operazione successiva. Se l'operazione corrente non è
									// l'ultima,
				// otteniamo la data dell'operazione successiva; altrimenti, utilizziamo la data
				// di chiusura dell'anno (fineAnno)

				long giorni = ChronoUnit.DAYS.between(dataOperazioneCorrente, dataOperazioneSuccessiva);
				//System.out.println(giorni);
				interessi += operazioneCorrente.getSaldoParziale() * (TASSO_INTERESSE / 365.0) * giorni; // Calcoliamo
																											// gli
																											// interessi
																											// per
																											// l'operazione
																											// corrente
																											// e li
																											// aggiungiamo
																											// al totale
																											// degli
																											// interessi.
				
				logger.warn("interessi = saldo parziale * ( tasso interesse annuale / 365 ) * giorni del periodo");
				logger.warn(interessi + " = " +  operazioneCorrente.getSaldoParziale() + " * (" + TASSO_INTERESSE + " / 365) * "+ giorni );

			}
		} else {
			// Se non ci sono operazioni, calcola gli interessi sull'intero periodo.
			long giorni = ChronoUnit.DAYS.between(dataIniziale, dataChiusura);
			interessi += getSaldo() * (TASSO_INTERESSE / 365.0) * giorni;
		}

		stampaEstrattoConto(fineAnno, interessi, TASSO_INTERESSE);
	}
 }


