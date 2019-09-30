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
 * Date 12/05/2017
 */
package it.cnr.contab.ordmag.richieste.bulk;
import it.cnr.jada.persistency.Keyed;
public class RichiestaUopBase extends RichiestaUopKey implements Keyed {
//    DATA_RICHIESTA TIMESTAMP(7) NOT NULL
	private java.sql.Timestamp dataRichiesta;
 
//    STATO VARCHAR(3) NOT NULL
	private java.lang.String stato;
 
//    DS_RICHIESTA VARCHAR(300)
	private java.lang.String dsRichiesta;
 
//    NOTA VARCHAR(2000)
	private java.lang.String nota;
 
//    CD_UNITA_OPERATIVA_DEST VARCHAR(30) NOT NULL
	private java.lang.String cdUnitaOperativaDest;
 
//  CD_UNITA_OPERATIVA_DEST VARCHAR(30) NOT NULL
	private java.lang.String cdUopRichiesta;

//    DATA_INVIO TIMESTAMP(7)
	private java.sql.Timestamp dataInvio;
 
//    DT_CANCELLAZIONE TIMESTAMP(7)
	private java.sql.Timestamp dtCancellazione;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: RICHIESTA_UOP
	 **/
	public RichiestaUopBase() {
		super();
	}
	public RichiestaUopBase(java.lang.String cdCds, java.lang.String cdUnitaOperativa, java.lang.Integer esercizio, java.lang.String cdNumeratore, java.lang.Integer numero) {
		super(cdCds, cdUnitaOperativa, esercizio, cdNumeratore, numero);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dataRichiesta]
	 **/
	public java.sql.Timestamp getDataRichiesta() {
		return dataRichiesta;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dataRichiesta]
	 **/
	public void setDataRichiesta(java.sql.Timestamp dataRichiesta)  {
		this.dataRichiesta=dataRichiesta;
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
	 * Restituisce il valore di: [dsRichiesta]
	 **/
	public java.lang.String getDsRichiesta() {
		return dsRichiesta;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dsRichiesta]
	 **/
	public void setDsRichiesta(java.lang.String dsRichiesta)  {
		this.dsRichiesta=dsRichiesta;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [nota]
	 **/
	public java.lang.String getNota() {
		return nota;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [nota]
	 **/
	public void setNota(java.lang.String nota)  {
		this.nota=nota;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdUnitaOperativaDest]
	 **/
	public java.lang.String getCdUnitaOperativaDest() {
		return cdUnitaOperativaDest;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdUnitaOperativaDest]
	 **/
	public void setCdUnitaOperativaDest(java.lang.String cdUnitaOperativaDest)  {
		this.cdUnitaOperativaDest=cdUnitaOperativaDest;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dataInvio]
	 **/
	public java.sql.Timestamp getDataInvio() {
		return dataInvio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dataInvio]
	 **/
	public void setDataInvio(java.sql.Timestamp dataInvio)  {
		this.dataInvio=dataInvio;
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
	public java.lang.String getCdUopRichiesta() {
		return cdUopRichiesta;
	}
	public void setCdUopRichiesta(java.lang.String cdUopRichiesta) {
		this.cdUopRichiesta = cdUopRichiesta;
	}
}