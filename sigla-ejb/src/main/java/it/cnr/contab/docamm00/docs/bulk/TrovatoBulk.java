package it.cnr.contab.docamm00.docs.bulk;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value=Include.NON_NULL)
public class TrovatoBulk extends it.cnr.jada.bulk.OggettoBulk {

	private Long pg_trovato;
	private Integer nsrif;
	private String titolo;
	private String inventore;

	/**
	*
	* @return
	* The nsrif
	*/
	public Integer getNsrif() {
	return nsrif;
	}

	/**
	*
	* @param nsrif
	* The nsrif
	*/
	public void setNsrif(Integer nsrif) {
	this.nsrif = nsrif;
	}

	/**
	*
	* @return
	* The titolo
	*/
	public String getTitolo() {
	return titolo;
	}

	/**
	*
	* @param titolo
	* The titolo
	*/
	public void setTitolo(String titolo) {
	this.titolo = titolo;
	}

	/**
	*
	* @return
	* The inventore
	*/
	public String getInventore() {
	return inventore;
	}

	/**
	*
	* @param inventore
	* The inventore
	*/
	public void setInventore(String inventore) {
	this.inventore = inventore;
	}

	public Long getPg_trovato() {
		return pg_trovato;
	}

	public void setPg_trovato(Long pg_trovato) {
		this.pg_trovato = pg_trovato;
	}

}
