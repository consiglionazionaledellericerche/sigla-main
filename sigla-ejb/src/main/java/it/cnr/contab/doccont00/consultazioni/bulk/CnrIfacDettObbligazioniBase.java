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
 * Date 14/03/2016
 */
package it.cnr.contab.doccont00.consultazioni.bulk;
import java.sql.Date;

import it.cnr.jada.persistency.Keyed;
public class CnrIfacDettObbligazioniBase extends CnrIfacDettObbligazioniKey implements Keyed {
//    CD_UNITA_ORGANIZZATIVA VARCHAR(30) NOT NULL
	private java.lang.String cdUnitaOrganizzativa;
 
//    IM_VOCE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal imVoce;
 
//    DESCRIZIONE VARCHAR(1200) NOT NULL
	private java.lang.String descrizione;
 
//    NOTE VARCHAR(300)
	private java.lang.String note;
 
//    RIPORTATO CHAR(1) NOT NULL
	private java.lang.String riportato;
 
//  RIPORTATO CHAR(1) NOT NULL
	private java.lang.Integer esercizioOriOriRiporto;

	private java.sql.Date data_creazione;
	 
	private java.sql.Date data_ultima_variazione;
	 
//    PG_OBBLIGAZIONE_ORI_RIPORTO DECIMAL(10,0)
	private java.lang.Long pgObbligazioneOriRiporto;
 
//    FORNITORE VARCHAR(200) NOT NULL
	private java.lang.String fornitore;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: CNR_IFAC_DETT_OBBLIGAZIONI
	 **/
	public CnrIfacDettObbligazioniBase(java.lang.String cdCds,java.lang.String cdCentroResponsabilita,java.lang.String cdLineaAttivita,java.lang.String cdVoce,java.lang.Integer esercizio,java.lang.Integer esercizioOriginale,java.lang.Long pgObbligazione,java.lang.Long pgObbligazioneScadenzario,java.lang.String tiAppartenenza,java.lang.String tiGestione,java.lang.Long pgDocamm) {
		super(cdCds, cdCentroResponsabilita, cdLineaAttivita, cdVoce, esercizio, esercizioOriginale, pgObbligazione, pgObbligazioneScadenzario, tiAppartenenza, tiGestione, pgDocamm);
	}
	public CnrIfacDettObbligazioniBase() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdUnitaOrganizzativa]
	 **/
	public java.lang.String getCdUnitaOrganizzativa() {
		return cdUnitaOrganizzativa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdUnitaOrganizzativa]
	 **/
	public void setCdUnitaOrganizzativa(java.lang.String cdUnitaOrganizzativa)  {
		this.cdUnitaOrganizzativa=cdUnitaOrganizzativa;
	}
	/**
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [imVoce]
	 **/
	public java.math.BigDecimal getImVoce() {
		return imVoce;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [imVoce]
	 **/
	public void setImVoce(java.math.BigDecimal imVoce)  {
		this.imVoce=imVoce;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [descrizione]
	 **/
	public java.lang.String getDescrizione() {
		return descrizione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [descrizione]
	 **/
	public void setDescrizione(java.lang.String descrizione)  {
		this.descrizione=descrizione;
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
	 * Restituisce il valore di: [riportato]
	 **/
	public java.lang.String getRiportato() {
		return riportato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [riportato]
	 **/
	public void setRiportato(java.lang.String riportato)  {
		this.riportato=riportato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgObbligazioneOriRiporto]
	 **/
	public java.lang.Long getPgObbligazioneOriRiporto() {
		return pgObbligazioneOriRiporto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgObbligazioneOriRiporto]
	 **/
	public void setPgObbligazioneOriRiporto(java.lang.Long pgObbligazioneOriRiporto)  {
		this.pgObbligazioneOriRiporto=pgObbligazioneOriRiporto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [fornitore]
	 **/
	public java.lang.String getFornitore() {
		return fornitore;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [fornitore]
	 **/
	public void setFornitore(java.lang.String fornitore)  {
		this.fornitore=fornitore;
	}
	public java.sql.Date getData_ultima_variazione(){
		return new Date(getDuva().getTime());
	}
	public java.sql.Date getData_creazione(){
		return new Date(getDacr().getTime());
	}
	public java.lang.Integer getEsercizioOriOriRiporto() {
		return esercizioOriOriRiporto;
	}
	public void setEsercizioOriOriRiporto(java.lang.Integer esercizioOriOriRiporto) {
		this.esercizioOriOriRiporto = esercizioOriOriRiporto;
	}
}