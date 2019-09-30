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
 * Date 02/02/2012
 */
package it.cnr.contab.bilaterali00.bulk;
import it.cnr.jada.persistency.Keyed;
public class Blt_autorizzatiBase extends Blt_autorizzatiKey implements Keyed {
//    CD_CDR_TERZO VARCHAR(30)
	private java.lang.String cdCdrTerzo;
 
//    EMAIL_TERZO VARCHAR(100)
	private java.lang.String emailTerzo;
 
//    TELEF_TERZO VARCHAR(30)
	private java.lang.String telefTerzo;
 
//    FAX_TERZO VARCHAR(30)
	private java.lang.String faxTerzo;
 
//    TI_ITALIANO_ESTERO CHAR(1) NOT NULL
	private java.lang.String tiItalianoEstero;

//    FL_ASSIMILATO_DIP CHAR(1) NOT NULL
	private java.lang.Boolean flAssimilatoDip;

//    FL_ASSOCIATO CHAR(1) NOT NULL
	private java.lang.Boolean flAssociato;

//    ENTE_DI_APPARTENENZA CHAR(100) NOT NULL
	private java.lang.String enteDiAppartenenza;
	
//    INDIRIZZO_ENTE_DI_APPARTENENZA CHAR(100) NOT NULL
	private java.lang.String indirizzoEnteDiAppartenenza;

//    CAP_ENTE_DI_APPARTENENZA CHAR(20) NOT NULL
	private java.lang.String capEnteDiAppartenenza;

// 	  PG_COMUNE_ENTE_DI_APPARTENENZA DECIMAL(10,0)
	private java.lang.Long pgComuneEnteDiAppartenenza;

	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: BLT_AUTORIZZATI
	 **/
	public Blt_autorizzatiBase() {
		super();
	}
	public Blt_autorizzatiBase(java.lang.String cdAccordo, java.lang.String cdProgetto, java.lang.Integer cdTerzo) {
		super(cdAccordo, cdProgetto, cdTerzo);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdrTerzo]
	 **/
	public java.lang.String getCdCdrTerzo() {
		return cdCdrTerzo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdrTerzo]
	 **/
	public void setCdCdrTerzo(java.lang.String cdCdrTerzo)  {
		this.cdCdrTerzo=cdCdrTerzo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [emailTerzo]
	 **/
	public java.lang.String getEmailTerzo() {
		return emailTerzo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [emailTerzo]
	 **/
	public void setEmailTerzo(java.lang.String emailTerzo)  {
		this.emailTerzo=emailTerzo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [telefTerzo]
	 **/
	public java.lang.String getTelefTerzo() {
		return telefTerzo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [telefTerzo]
	 **/
	public void setTelefTerzo(java.lang.String telefTerzo)  {
		this.telefTerzo=telefTerzo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [faxTerzo]
	 **/
	public java.lang.String getFaxTerzo() {
		return faxTerzo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [faxTerzo]
	 **/
	public void setFaxTerzo(java.lang.String faxTerzo)  {
		this.faxTerzo=faxTerzo;
	}
	public java.lang.String getTiItalianoEstero() {
		return tiItalianoEstero;
	}
	public void setTiItalianoEstero(java.lang.String tiItalianoEstero) {
		this.tiItalianoEstero = tiItalianoEstero;
	}
	public java.lang.Boolean getFlAssimilatoDip() {
		return flAssimilatoDip;
	}
	public void setFlAssimilatoDip(java.lang.Boolean flAssimilatoDip) {
		this.flAssimilatoDip = flAssimilatoDip;
	}
	public java.lang.Boolean getFlAssociato() {
		return flAssociato;
	}
	public void setFlAssociato(java.lang.Boolean flAssociato) {
		this.flAssociato = flAssociato;
	}
	public java.lang.String getEnteDiAppartenenza() {
		return enteDiAppartenenza;
	}
	public void setEnteDiAppartenenza(java.lang.String enteDiAppartenenza) {
		this.enteDiAppartenenza = enteDiAppartenenza;
	}
	public java.lang.String getIndirizzoEnteDiAppartenenza() {
		return indirizzoEnteDiAppartenenza;
	}
	public void setIndirizzoEnteDiAppartenenza(java.lang.String indirizzoEnteDiAppartenenza) {
		this.indirizzoEnteDiAppartenenza = indirizzoEnteDiAppartenenza;
	}
	public java.lang.String getCapEnteDiAppartenenza() {
		return capEnteDiAppartenenza;
	}
	public void setCapEnteDiAppartenenza(java.lang.String capEnteDiAppartenenza) {
		this.capEnteDiAppartenenza = capEnteDiAppartenenza;
	}
	public java.lang.Long getPgComuneEnteDiAppartenenza() {
		return pgComuneEnteDiAppartenenza;
	}
	public void setPgComuneEnteDiAppartenenza(java.lang.Long pgComuneEnteDiAppartenenza) {
		this.pgComuneEnteDiAppartenenza = pgComuneEnteDiAppartenenza;
	}
}