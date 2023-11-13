package it.betacom.model;

import java.time.LocalDate;

public class Operazione implements Comparable<Operazione>{

	private String tipo;
    private double importo, saldoParziale;
    private LocalDate data;

    public Operazione(String tipo, double importo, LocalDate dataCasuale, double saldoParziale) {
        this.tipo = tipo;
        this.importo = importo;
        this.data = dataCasuale;
        this.saldoParziale = saldoParziale;
    }

    @Override
    public int compareTo(Operazione other) {
        return this.getData().compareTo(other.getData());
    }
    
	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public double getImporto() {
		return importo;
	}

	public void setImporto(double importo) {
		this.importo = importo;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public double getSaldoParziale() {
		return saldoParziale;
	}

	public void setSaldoParziale(double saldoParziale) {
		this.saldoParziale = saldoParziale;
	}

	
}

