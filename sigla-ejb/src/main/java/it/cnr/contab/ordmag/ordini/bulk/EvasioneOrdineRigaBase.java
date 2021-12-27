/*
 * Copyright (C) 2020  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 21/09/2017
 */
package it.cnr.contab.ordmag.ordini.bulk;
import it.cnr.jada.persistency.Keyed;

import java.math.BigDecimal;

public class EvasioneOrdineRigaBase extends EvasioneOrdineRigaKey implements Keyed {
//    CD_CDS_ORDINE VARCHAR(30) NOT NULL
	private java.lang.String cdCdsOrdine;
 
//    CD_UNITA_OPERATIVA VARCHAR(30) NOT NULL
	private java.lang.String cdUnitaOperativa;
 
//    ESERCIZIO_ORDINE DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizioOrdine;
 
//    CD_NUMERATORE_ORDINE VARCHAR(3) NOT NULL
	private java.lang.String cdNumeratoreOrdine;
 
//    NUMERO_ORDINE DECIMAL(6,0) NOT NULL
	private java.lang.Integer numeroOrdine;
 
//    RIGA_ORDINE DECIMAL(6,0) NOT NULL
	private java.lang.Integer rigaOrdine;
 
//    CONSEGNA DECIMAL(6,0) NOT NULL
	private java.lang.Integer consegna;
 
//    QUANTITA_EVASA DECIMAL(12,5) NOT NULL
	private java.math.BigDecimal quantitaEvasa;

	private Long id_movimenti_mag;

	public Long getId_movimenti_mag() {
		return id_movimenti_mag;
	}

	public void setId_movimenti_mag(Long id_movimenti_mag) {
		this.id_movimenti_mag = id_movimenti_mag;
	}


	//    STATO VARCHAR(3) NOT NULL
	private java.lang.String stato;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: EVASIONE_ORDINE_RIGA
	 **/
	public EvasioneOrdineRigaBase() {
		super();
	}
	public EvasioneOrdineRigaBase(java.lang.String cdCds, java.lang.String cdMagazzino, java.lang.Integer esercizio, java.lang.String cdNumeratoreMag, java.lang.Long numero, java.lang.Long riga) {
		super(cdCds, cdMagazzino, esercizio, cdNumeratoreMag, numero, riga);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdsOrdine]
	 **/
	public java.lang.String getCdCdsOrdine() {
		return cdCdsOrdine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsOrdine]
	 **/
	public void setCdCdsOrdine(java.lang.String cdCdsOrdine)  {
		this.cdCdsOrdine=cdCdsOrdine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdUnitaOperativa]
	 **/
	public java.lang.String getCdUnitaOperativa() {
		return cdUnitaOperativa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdUnitaOperativa]
	 **/
	public void setCdUnitaOperativa(java.lang.String cdUnitaOperativa)  {
		this.cdUnitaOperativa=cdUnitaOperativa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizioOrdine]
	 **/
	public java.lang.Integer getEsercizioOrdine() {
		return esercizioOrdine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizioOrdine]
	 **/
	public void setEsercizioOrdine(java.lang.Integer esercizioOrdine)  {
		this.esercizioOrdine=esercizioOrdine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdNumeratoreOrdine]
	 **/
	public java.lang.String getCdNumeratoreOrdine() {
		return cdNumeratoreOrdine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdNumeratoreOrdine]
	 **/
	public void setCdNumeratoreOrdine(java.lang.String cdNumeratoreOrdine)  {
		this.cdNumeratoreOrdine=cdNumeratoreOrdine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [numeroOrdine]
	 **/
	public java.lang.Integer getNumeroOrdine() {
		return numeroOrdine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [numeroOrdine]
	 **/
	public void setNumeroOrdine(java.lang.Integer numeroOrdine)  {
		this.numeroOrdine=numeroOrdine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [rigaOrdine]
	 **/
	public java.lang.Integer getRigaOrdine() {
		return rigaOrdine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [rigaOrdine]
	 **/
	public void setRigaOrdine(java.lang.Integer rigaOrdine)  {
		this.rigaOrdine=rigaOrdine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [consegna]
	 **/
	public java.lang.Integer getConsegna() {
		return consegna;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [consegna]
	 **/
	public void setConsegna(java.lang.Integer consegna)  {
		this.consegna=consegna;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [quantitaEvasa]
	 **/
	public java.math.BigDecimal getQuantitaEvasa() {
		return quantitaEvasa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [quantitaEvasa]
	 **/
	public void setQuantitaEvasa(java.math.BigDecimal quantitaEvasa)  {
		this.quantitaEvasa=quantitaEvasa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [stato]
	 **/
	public java.lang.String getStato() {
		return stato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [stato]
	 **/
	public void setStato(java.lang.String stato)  {
		this.stato=stato;
	}
}