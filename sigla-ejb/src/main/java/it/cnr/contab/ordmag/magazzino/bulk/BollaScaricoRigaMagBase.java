/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
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
 * Date 03/10/2017
 */
package it.cnr.contab.ordmag.magazzino.bulk;
import it.cnr.jada.persistency.Keyed;
public class BollaScaricoRigaMagBase extends BollaScaricoRigaMagKey implements Keyed {
 
//    CD_CDS_RICH VARCHAR(30)
	private java.lang.String cdCdsRich;
 
//    CD_UNITA_OPERATIVA_RICH VARCHAR(30)
	private java.lang.String cdUnitaOperativaRich;
 
//    ESERCIZIO_RICH DECIMAL(4,0)
	private java.lang.Integer esercizioRich;
 
//    CD_NUMERATORE_RICH VARCHAR(3)
	private java.lang.String cdNumeratoreRich;
 
//    NUMERO_RICH DECIMAL(6,0)
	private java.lang.Integer numeroRich;
 
//    RIGA_RICH DECIMAL(6,0)
	private java.lang.Integer rigaRich;
 
//    NOTE VARCHAR(500)
	private java.lang.String note;
 
//    PG_MOVIMENTO DECIMAL(12,0) NOT NULL
	private java.lang.Long pgMovimento;
 
//    STATO VARCHAR(3) NOT NULL
	private java.lang.String stato;
 
//    DT_CANCELLAZIONE TIMESTAMP(7)
	private java.sql.Timestamp dtCancellazione;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: BOLLA_SCARICO_RIGA_MAG
	 **/
	public BollaScaricoRigaMagBase() {
		super();
	}
	public BollaScaricoRigaMagBase(java.lang.String cdCds, java.lang.String cdMagazzino, java.lang.Integer esercizio, java.lang.String cdNumeratoreMag, java.lang.Integer pgBollaSca, java.lang.Integer rigaBollaSca) {
		super(cdCds, cdMagazzino, esercizio, cdNumeratoreMag, pgBollaSca, rigaBollaSca);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdsRich]
	 **/
	public java.lang.String getCdCdsRich() {
		return cdCdsRich;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsRich]
	 **/
	public void setCdCdsRich(java.lang.String cdCdsRich)  {
		this.cdCdsRich=cdCdsRich;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdUnitaOperativaRich]
	 **/
	public java.lang.String getCdUnitaOperativaRich() {
		return cdUnitaOperativaRich;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdUnitaOperativaRich]
	 **/
	public void setCdUnitaOperativaRich(java.lang.String cdUnitaOperativaRich)  {
		this.cdUnitaOperativaRich=cdUnitaOperativaRich;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizioRich]
	 **/
	public java.lang.Integer getEsercizioRich() {
		return esercizioRich;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizioRich]
	 **/
	public void setEsercizioRich(java.lang.Integer esercizioRich)  {
		this.esercizioRich=esercizioRich;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdNumeratoreRich]
	 **/
	public java.lang.String getCdNumeratoreRich() {
		return cdNumeratoreRich;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdNumeratoreRich]
	 **/
	public void setCdNumeratoreRich(java.lang.String cdNumeratoreRich)  {
		this.cdNumeratoreRich=cdNumeratoreRich;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [numeroRich]
	 **/
	public java.lang.Integer getNumeroRich() {
		return numeroRich;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [numeroRich]
	 **/
	public void setNumeroRich(java.lang.Integer numeroRich)  {
		this.numeroRich=numeroRich;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [rigaRich]
	 **/
	public java.lang.Integer getRigaRich() {
		return rigaRich;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [rigaRich]
	 **/
	public void setRigaRich(java.lang.Integer rigaRich)  {
		this.rigaRich=rigaRich;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [note]
	 **/
	public java.lang.String getNote() {
		return note;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [note]
	 **/
	public void setNote(java.lang.String note)  {
		this.note=note;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgMovimento]
	 **/
	public java.lang.Long getPgMovimento() {
		return pgMovimento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgMovimento]
	 **/
	public void setPgMovimento(java.lang.Long pgMovimento)  {
		this.pgMovimento=pgMovimento;
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
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtCancellazione]
	 **/
	public java.sql.Timestamp getDtCancellazione() {
		return dtCancellazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtCancellazione]
	 **/
	public void setDtCancellazione(java.sql.Timestamp dtCancellazione)  {
		this.dtCancellazione=dtCancellazione;
	}
}