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
package it.cnr.contab.docamm00.consultazioni.bulk;
import it.cnr.jada.persistency.Keyed;
public class CnrIfacFatturaPassivaRigaBase extends CnrIfacFatturaPassivaRigaKey implements Keyed {
//    TI_ISTITUZ_COMMERC CHAR(1) NOT NULL
	private java.lang.String tiIstituzCommerc;
 
//    ESERCIZIO_OBBLIGAZIONE DECIMAL(4,0)
	private java.lang.Integer esercizioObbligazione;
 
//    PG_OBBLIGAZIONE DECIMAL(10,0)
	private java.lang.Long pgObbligazione;
 
//    PG_OBBLIGAZIONE_SCADENZARIO DECIMAL(10,0)
	private java.lang.Long pgObbligazioneScadenzario;
 
//    NR_FATTURA_FORNITORE VARCHAR(20) NOT NULL
	private java.lang.String nrFatturaFornitore;
 
//    DT_FATTURA_FORNITORE TIMESTAMP(7) NOT NULL
	private java.sql.Date dtFatturaFornitore;
 
//    IM_TOTALE_IMPONIBILE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal imTotaleImponibile;
 
//    IM_TOTALE_IVA DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal imTotaleIva;
 
//    IM_TOTALE_FATTURA DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal imTotaleFattura;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: CNR_IFAC_FATTURA_PASSIVA_RIGA
	 **/
	public CnrIfacFatturaPassivaRigaBase(java.lang.String cdCds,java.lang.String cdUnitaOrganizzativa,java.lang.Integer esercizio,java.lang.Long pgFatturaPassiva,java.lang.Long progressivoRiga) {
		super(cdCds, cdUnitaOrganizzativa, esercizio, pgFatturaPassiva, progressivoRiga);
	}
	public CnrIfacFatturaPassivaRigaBase() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [tiIstituzCommerc]
	 **/
	public java.lang.String getTiIstituzCommerc() {
		return tiIstituzCommerc;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [tiIstituzCommerc]
	 **/
	public void setTiIstituzCommerc(java.lang.String tiIstituzCommerc)  {
		this.tiIstituzCommerc=tiIstituzCommerc;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizioObbligazione]
	 **/
	public java.lang.Integer getEsercizioObbligazione() {
		return esercizioObbligazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizioObbligazione]
	 **/
	public void setEsercizioObbligazione(java.lang.Integer esercizioObbligazione)  {
		this.esercizioObbligazione=esercizioObbligazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgObbligazione]
	 **/
	public java.lang.Long getPgObbligazione() {
		return pgObbligazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgObbligazione]
	 **/
	public void setPgObbligazione(java.lang.Long pgObbligazione)  {
		this.pgObbligazione=pgObbligazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgObbligazioneScadenzario]
	 **/
	public java.lang.Long getPgObbligazioneScadenzario() {
		return pgObbligazioneScadenzario;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgObbligazioneScadenzario]
	 **/
	public void setPgObbligazioneScadenzario(java.lang.Long pgObbligazioneScadenzario)  {
		this.pgObbligazioneScadenzario=pgObbligazioneScadenzario;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [nrFatturaFornitore]
	 **/
	public java.lang.String getNrFatturaFornitore() {
		return nrFatturaFornitore;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [nrFatturaFornitore]
	 **/
	public void setNrFatturaFornitore(java.lang.String nrFatturaFornitore)  {
		this.nrFatturaFornitore=nrFatturaFornitore;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtFatturaFornitore]
	 **/
	public java.sql.Date getDtFatturaFornitore() {
		return dtFatturaFornitore;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtFatturaFornitore]
	 **/
	public void setDtFatturaFornitore(java.sql.Date dtFatturaFornitore)  {
		this.dtFatturaFornitore=dtFatturaFornitore;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [imTotaleImponibile]
	 **/
	public java.math.BigDecimal getImTotaleImponibile() {
		return imTotaleImponibile;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [imTotaleImponibile]
	 **/
	public void setImTotaleImponibile(java.math.BigDecimal imTotaleImponibile)  {
		this.imTotaleImponibile=imTotaleImponibile;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [imTotaleIva]
	 **/
	public java.math.BigDecimal getImTotaleIva() {
		return imTotaleIva;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [imTotaleIva]
	 **/
	public void setImTotaleIva(java.math.BigDecimal imTotaleIva)  {
		this.imTotaleIva=imTotaleIva;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [imTotaleFattura]
	 **/
	public java.math.BigDecimal getImTotaleFattura() {
		return imTotaleFattura;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [imTotaleFattura]
	 **/
	public void setImTotaleFattura(java.math.BigDecimal imTotaleFattura)  {
		this.imTotaleFattura=imTotaleFattura;
	}
}