package it.betacom.model;

import java.io.FileOutputStream;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;


import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;




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

	public void simulaOperazioniCasuali() {
		int numeroOperazioni = 3;
	
		LocalDate dataCasuale = generaDataCasuale2021(getDataApertura());

		for (int i = 0; i < numeroOperazioni; i++) {
			double importo = Math.random() * 500;
			dataCasuale = generaDataCasuale2021(dataCasuale);

			boolean isVersamento = Math.random() > 0.5;

			if (isVersamento) {
				versa(importo, dataCasuale);
			} else {
				preleva(importo, dataCasuale);
			}
		}

	}

	public LocalDate generaDataCasuale2021(LocalDate dataApertura) {
	    Random generator = new Random();

	    LocalDate tmp = dataApertura;
	    // Calcola il numero di giorni rimanenti tra la data di apertura e la data massima
	    long giorniRimanenti = ChronoUnit.DAYS.between(dataApertura, LocalDate.of(2021, 12, 31));

	    // Verifica se ci sono giorni rimanenti
	    if (giorniRimanenti > 0) {
	        // Calcola un valore massimo per il generatore casuale basato sulla differenza tra le date
	        int maxRandomValue = (int) giorniRimanenti;
	        
	        // Aggiungi una condizione per gestire il caso in cui maxRandomValue sia zero o negativo
	        if (maxRandomValue > 0) {
	            // Genera un numero casuale di giorni da aggiungere alla data di apertura
	            int giorniCasuali = generator.nextInt(maxRandomValue) + 1;
	            return tmp.plusDays(giorniCasuali);
	        } else {
	            // Gestisci il caso in cui non ci sono giorni rimanenti
	            return tmp;
	        }
	    } else {
	        // Gestisci il caso in cui non ci sono giorni rimanenti
	        return tmp;
	    }
	}


	public void versa(double importo, LocalDate dataCasuale) {
		double saldoIniziale = saldo;
		saldo += importo;
		Operazione nuovaOperazione = new Operazione("Versamento", importo, dataCasuale, saldo, saldoIniziale);
		if (!operazioni.contains(nuovaOperazione)) {
			operazioni.add(nuovaOperazione);
		}
	}

	public void preleva(double importo, LocalDate dataCasuale) {
		double saldoIniziale = saldo;
		saldo -= importo;
		Operazione nuovaOperazione = new Operazione("Prelievo", importo, dataCasuale, saldo, saldoIniziale);
		if (!operazioni.contains(nuovaOperazione)) {
			operazioni.add(nuovaOperazione);
		}
	}
	
	protected void stampaEstrattoConto(LocalDate fineAnno, double interessiLordi, double TASSO_INTERESSE ) {
		DecimalFormat formato = new DecimalFormat("#.##");
		SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy");

		int annoApertura = getDataApertura().getYear();

		System.out.println("******************************************************");

		System.out.println("Estratto Conto di " + getTitolare() + " ||  Data: " + LocalDate.now());
		System.out.println(" ");
		System.out.println("Movimenti dell'anno " + annoApertura);
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
		
		double tassazione = 0.26;
		double interessiNetti = interessiLordi - (interessiLordi * tassazione);

		System.out.println("Tasso interesse annuo: " + TASSO_INTERESSE);

		System.out.println("Saldo dopo le operazioni: " + formato.format(saldo));
		System.out.println("Interessi maturati al 31/12/" + annoApertura + " lordi: " + formato.format(interessiLordi));
		System.out.println("Interessi maturati al 31/12/" + annoApertura + " netti: " + formato.format(interessiNetti));
		System.out.println("Saldo finale: " + formato.format(saldo += interessiNetti));

		System.out.println("******************************************************");
		
		
		
	}
	
	
	
	public void writePDF(LocalDate dataInizio, LocalDate dataFine, String finePath, double interessi) {
			  
		System.out.println("La versione PDF del tuo Estratto Conto è stata creata");
		
			  Document document = new Document();
			  
			  Font titolo = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD), 
				   rosso = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.RED),
				  normale = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
			  
			  try { PdfWriter.getInstance(document, new FileOutputStream("C:/Users/Betacom/Desktop/" +
			  finePath)); document.open();
			  
			  document.add(addTitolo(titolo, normale, dataInizio, dataFine));
			  document.add(addTable(rosso, normale, dataInizio, dataFine));
			  document.add(addFine(rosso, normale, dataFine, interessi));
			  
			  document.close();
			  
			  } catch (Exception e) { e.printStackTrace(); }
			  
			  } 
			  
			private Paragraph addTitolo(Font fTitolo, Font normale, LocalDate dataInizio,
			LocalDate dataFine) {

			Paragraph p, preface = new Paragraph();

			preface.setAlignment(Element.ALIGN_CENTER);

			//titolo
			addEmptyLine(preface, 1); 
			p = new Paragraph("ESTRATTO " + (this.getClass().getSimpleName().replace("Conto", "Conto ")).toUpperCase() + "  " + dataInizio.getYear(), fTitolo);
			p.setAlignment(Element.ALIGN_CENTER); preface.add(p);

			//Nome Utente 
			addEmptyLine(preface, 1); 
			preface.add(new Paragraph(String.format("%100s", " ").replace(" ", "-"), normale));
			preface.add(new Paragraph("Correntista: \t " + getTitolare() +
			",  in data:\t" + dataFine, normale)); preface.add(new
			Paragraph(String.format("%100s", " ").replace(" ", "-"), normale));
			addEmptyLine(preface, 1);

			return preface;
			
			}


		private Paragraph addTable(Font rosso, Font normale, LocalDate dataInizio,
			LocalDate dataFine) throws DocumentException {

			Paragraph content = new Paragraph(); 
			PdfPTable table = new PdfPTable(4);

			DecimalFormat formato = new DecimalFormat("#.##"); 
			SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy");

			formato.setRoundingMode(RoundingMode.UP);

			table.addCell(headerCell("OPERAZIONE", rosso));
			table.addCell(headerCell("DATA", rosso)); table.addCell(headerCell("IMPORTO",
			rosso)); table.addCell(headerCell("ESITO", rosso));

			int i = 0;
			for (Operazione operazione : operazioni) {
				if (operazione.getImporto() != 0) {
					Date dataOperazione = Date.from(operazione.getData().atStartOfDay(ZoneId.systemDefault()).toInstant());
	
			table.addCell(tableCell("" + formatoData.format(dataOperazione), normale, i)); 
			table.addCell(tableCell("" + operazione.getTipo(), normale, i));
			table.addCell(tableCell("" + formato.format(operazione.getImporto()),normale, i)); 
			table.addCell(tableCell("" + formato.format(operazione.getSaldoParziale()), normale, i));

			i++;

			  } } 
			
			content.add(table); 
			return content;
		}
			
			
			private PdfPCell headerCell(String campo, Font font) {

				Phrase p = new Phrase(campo.toUpperCase());
				p.setFont(font);

				PdfPCell cell = new PdfPCell(p);
				cell.setBackgroundColor(BaseColor.YELLOW);

				return cell;
			}

			private PdfPCell tableCell(String campo, Font font, int i) {

				Phrase p = new Phrase(campo.toUpperCase());
				p.setFont(font);

				PdfPCell cell = new PdfPCell(p);

				if (i % 2 != 0)
					cell.setBackgroundColor(BaseColor.LIGHT_GRAY);

				return cell;
			}

			private Paragraph addFine(Font rosso, Font normale, LocalDate dataFine, double interessi) {

				Paragraph preface = new Paragraph();
				DecimalFormat formato = new DecimalFormat("#.##"); 
				

				formato.setMaximumFractionDigits(2);
				formato.setRoundingMode(RoundingMode.UP);

				//interessi = getImporto();
				double tassazione = 0.26;
				double interessiNetti = interessi - (interessi * tassazione);
				
				preface.setAlignment(Element.ALIGN_CENTER);
				

				preface.add(new Paragraph("\nInteressi maturati al 31/12 lordi: \t " + formato.format(interessi), normale));
				preface.add(new Paragraph("\nInteressi maturati al 31/12 netti: \t " + formato.format(interessiNetti), normale));
				preface.add(new Paragraph(String.format("%100s", " ").replace(" ", "-"), normale));
				
				preface.add(new Paragraph(String.format("%100s", " ").replace(" ", "-"), normale));
				preface.add(new Paragraph("Saldo finale: \t " + formato.format(saldo), normale));

				return preface;

			}

			private void addEmptyLine(Paragraph paragraph, int number) {
				for (int i = 0; i < number; i++) {
					paragraph.add(new Paragraph(" "));
				}
			}

	

	public double getSaldo() {
		return saldo;
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
