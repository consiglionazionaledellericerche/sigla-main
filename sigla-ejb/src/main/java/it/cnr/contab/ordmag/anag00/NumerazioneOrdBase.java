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
 * Date 26/04/2017
 */
package it.cnr.contab.ordmag.anag00;
import it.cnr.jada.persistency.Keyed;
public class NumerazioneOrdBase extends NumerazioneOrdKey implements Keyed {
//    DS_NUMERATORE VARCHAR(100) NOT NULL
	private java.lang.String dsNumeratore;
 
//    CD_TIPO_OPERAZIONE VARCHAR(3) NOT NULL
	private java.lang.String cdTipoOperazione;
 
//    CORRENTE DECIMAL(6,0)
	private java.lang.Integer corrente;
 
//    DATA_PROGRESSIVO TIMESTAMP(7)
	private java.sql.Timestamp dataProgressivo;
 
//    CD_TIPO_SEZIONALE VARCHAR(10)
	private java.lang.String cdTipoSezionale;
 
	private java.math.BigDecimal percProrata;

	private java.lang.String ti_istituz_commerc;

//    DT_CANCELLAZIONE TIMESTAMP(7)
	private java.sql.Timestamp dtCancellazione;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: NUMERAZIONE_ORD
	 **/
	public NumerazioneOrdBase() {
		super();
	}
	public NumerazioneOrdBase(java.lang.String cdUnitaOperativa, java.lang.Integer esercizio, java.lang.String cdNumeratore) {
		super(cdUnitaOperativa, esercizio, cdNumeratore);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dsNumeratore]
	 **/
	public java.lang.String getDsNumeratore() {
		return dsNumeratore;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dsNumeratore]
	 **/
	public void setDsNumeratore(java.lang.String dsNumeratore)  {
		this.dsNumeratore=dsNumeratore;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTipoOperazione]
	 **/
	public java.lang.String getCdTipoOperazione() {
		return cdTipoOperazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTipoOperazione]
	 **/
	public void setCdTipoOperazione(java.lang.String cdTipoOperazione)  {
		this.cdTipoOperazione=cdTipoOperazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [corrente]
	 **/
	public java.lang.Integer getCorrente() {
		return corrente;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [corrente]
	 **/
	public void setCorrente(java.lang.Integer corrente)  {
		this.corrente=corrente;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dataProgressivo]
	 **/
	public java.sql.Timestamp getDataProgressivo() {
		return dataProgressivo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dataProgressivo]
	 **/
	public void setDataProgressivo(java.sql.Timestamp dataProgressivo)  {
		this.dataProgressivo=dataProgressivo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTipoSezionale]
	 **/
	public java.lang.String getCdTipoSezionale() {
		return cdTipoSezionale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTipoSezionale]
	 **/
	public void setCdTipoSezionale(java.lang.String cdTipoSezionale)  {
		this.cdTipoSezionale=cdTipoSezionale;
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
	public java.math.BigDecimal getPercProrata() {
		return percProrata;
	}
	public void setPercProrata(java.math.BigDecimal percProrata) {
		this.percProrata = percProrata;
	}
	public java.lang.String getTi_istituz_commerc() {
		return ti_istituz_commerc;
	}
	public void setTi_istituz_commerc(java.lang.String ti_istituz_commerc) {
		this.ti_istituz_commerc = ti_istituz_commerc;
	}
}