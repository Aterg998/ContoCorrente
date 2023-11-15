package it.betacom.main;

import java.time.LocalDate;
import it.betacom.model.ContoCorrente;
import it.betacom.model.ContoDeposito;
import it.betacom.model.ContoInvestimento;

public class ContoMain {

	public static void main(String[] args) {

		ContoCorrente cc = new ContoCorrente("Greta Tunesi CC");
		ContoDeposito cd = new ContoDeposito("Greta Tunesi CD");
		ContoInvestimento ci = new ContoInvestimento("Greta Tunesi CI");

		// Assegna le operazioni simulate all'attributo operazioni
		cc.simulaOperazioniCasuali();
		cd.simulaOperazioniCasuali();
		ci.simulaOperazioniCasuali();

		// Chiudi l'anno e calcola gli interessi per il 2021
		LocalDate fineAnno = LocalDate.of(2021, 12, 31);
		cc.generaInteressi(fineAnno);
		cd.generaInteressi(fineAnno);
		ci.generaInteressi(fineAnno);

		
	}

}
