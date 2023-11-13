package it.betacom.model;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class ContoInvestimento extends Conto {

	private static final double TASSO_MIN = -1.0;
	private static final double TASSO_MAX = 1.0;
	private Random random = new Random();

	public ContoInvestimento(String titolare) {
		super(titolare);

	}

	   
		public void generaInteressi(LocalDate fineAnno) {
		    DecimalFormat formato = new DecimalFormat("#.##");
		    Collections.sort(operazioni, Comparator.comparing(Operazione::getData));
		    double interesseAnnuo = (random.nextDouble() * (TASSO_MAX - TASSO_MIN) + TASSO_MIN);

		    double interessi = 0.0;
		    LocalDate dataIniziale = getDataApertura();
		    LocalDate dataChiusura = fineAnno;

		    if (operazioni.size() > 0) {
		        for (int i = 0; i < operazioni.size(); i++) {
		            Operazione operazioneCorrente = operazioni.get(i);
		            LocalDate dataOperazioneCorrente = operazioneCorrente.getData();

		            LocalDate dataOperazioneSuccessiva = (i < operazioni.size() - 1) ? operazioni.get(i + 1).getData() : fineAnno;

		            long giorni = ChronoUnit.DAYS.between(dataOperazioneCorrente, dataOperazioneSuccessiva);
		            interessi += operazioneCorrente.getSaldoParziale() * (interesseAnnuo / 365.0) * giorni;
		        }
		    } else {
		        // Se non ci sono operazioni, calcola gli interessi sull'intero periodo.
		        long giorni = ChronoUnit.DAYS.between(dataIniziale, dataChiusura);
		        interessi += getSaldo() * (interesseAnnuo / 365.0) * giorni;
		    }

		    stampaEstrattoConto(fineAnno, interessi);
		}

	
	public void stampaEstrattoConto(LocalDate fineAnno, double interessiLordi) {
		DecimalFormat formato = new DecimalFormat("#.##");
		SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy");

		int annoApertura = getDataApertura().getYear();
		
		
		System.out.println("******************************************************");
		
		double interesseAnnuo = (random.nextDouble() * (TASSO_MAX - TASSO_MIN) + TASSO_MIN);

		System.out.println("Estratto Conto di " + getTitolare() + " ||  Data: " + LocalDate.now());
		System.out.println(" ");
		System.out.println("Movimenti dell'anno " + fineAnno);
		System.out.println(" ");
		System.out.println("           Data      ||    Tipo di operazione   ||    Importo  ||    Saldo parziale");

		for (Operazione operazione : operazioni) {
			if (operazione.getImporto() != 0) {
				Date dataOperazione = Date.from(operazione.getData().atStartOfDay(ZoneId.systemDefault()).toInstant());
				System.out.println("       " + formatoData.format(dataOperazione) + "       ||       "
						+ operazione.getTipo() + "     ||     " + formato.format(operazione.getImporto()) + "    ||    "
						+ formato.format(operazione.getSaldoParziale()));
			}
		}

		System.out.println("-----------------------------------------------");

		// Calcola interessi e stampa saldo finale dopo il loop
		// double saldoIniziale = annoApertura == LocalDate.now().getYear() ? getSaldo()
		// : 1000;
		double tassazione = 0.26;
		double interessiNetti = interessiLordi - (interessiLordi * tassazione);
		
		System.out.println("Tasso interesse giornaliero: " + formato.format(interesseAnnuo));

		System.out.println("Saldo dopo le operazioni: " + formato.format(saldo));
		System.out.println("Interessi maturati al 31/12/" + annoApertura + " lordi: " + formato.format(interessiLordi));
		System.out.println("Interessi maturati al 31/12/" + annoApertura + " netti: " + formato.format(interessiNetti));
		System.out.println("Saldo finale: " + formato.format(saldo += interessiNetti));
		
		System.out.println("******************************************************");
	}


	
	@Override
	public void simulaOperazioniCasuali() {
		int numeroOperazioni = 3;
		List<Operazione> operazioniSimulate = new ArrayList<>();

		for (int i = 0; i < numeroOperazioni; i++) {
			double importo = Math.random() * 500;
			LocalDate dataCasuale = generaDataCasuale2021(getDataApertura(), operazioniSimulate);
			boolean isVersamento = Math.random() > 0.5;

			if (isVersamento) {
				versa(importo, dataCasuale);
			} else {
				preleva(importo, dataCasuale);
			}

			Operazione operazione = new Operazione((isVersamento ? "Versamento" : "Prelievo"), importo, dataCasuale,
					saldo);
			operazioniSimulate.add(operazione);
		}

		// Ordina le operazioni per data
		Collections.sort(operazioniSimulate);
		
	}

}
