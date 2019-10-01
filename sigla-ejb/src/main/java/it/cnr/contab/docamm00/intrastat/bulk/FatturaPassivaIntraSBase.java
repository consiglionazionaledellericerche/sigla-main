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
 * Date 14/06/2010
 */
package it.cnr.contab.docamm00.intrastat.bulk;
import it.cnr.jada.persistency.Keyed;
public class FatturaPassivaIntraSBase extends FatturaPassivaIntraSKey implements Keyed {
//    AMMONTARE_EURO DECIMAL(15,2)
	private java.math.BigDecimal ammontareEuro;
 
//    AMMONTARE_DIVISA DECIMAL(15,2)
	private java.math.BigDecimal ammontareDivisa;
 
//    ID_NATURA_TRANSAZIONE DECIMAL(22,0)
	private java.lang.Long idNaturaTransazione;
 
//    ID_NOMENCLATURA_COMBINATA DECIMAL(22,0)
	private java.lang.Long idNomenclaturaCombinata;
 
//    MASSA_NETTA DECIMAL(15,2)
	private java.math.BigDecimal massaNetta;
 
//    UNITA_SUPPLEMENTARI DECIMAL(10,0)
	private java.lang.Long unitaSupplementari;
 
//    VALORE_STATISTICO DECIMAL(15,2)
	private java.math.BigDecimal valoreStatistico;
 
//    ESERCIZIO_COND_CONSEGNA DECIMAL(4,0)
	private java.lang.Integer esercizioCondConsegna;
 
//    CD_INCOTERM VARCHAR(10)
	private java.lang.String cdIncoterm;
 
//    ESERCIZIO_MOD_TRASPORTO DECIMAL(4,0)
	private java.lang.Integer esercizioModTrasporto;
 
//    CD_MODALITA_TRASPORTO VARCHAR(5)
	private java.lang.String cdModalitaTrasporto;
 
//    PG_NAZIONE_PROVENIENZA DECIMAL(10,0)
	private java.lang.Long pgNazioneProvenienza;
 
//    PG_NAZIONE_ORIGINE DECIMAL(10,0)
	private java.lang.Long pgNazioneOrigine;
 
//    CD_PROVINCIA_DESTINAZIONE VARCHAR(10)
	private java.lang.String cdProvinciaDestinazione;
 
//    DS_BENE VARCHAR(300)
	private java.lang.String dsBene;
 
//    ID_CPA DECIMAL(22,0)
	private java.lang.Long idCpa;
 
//    ESERCIZIO_MOD_INCASSO DECIMAL(4,0)
	private java.lang.Integer esercizioModIncasso;
 
//    CD_MODALITA_INCASSO CHAR(1)
	private java.lang.String cdModalitaIncasso;
 
//    ESERCIZIO_MOD_EROGAZIONE DECIMAL(4,0)
	private java.lang.Integer esercizioModErogazione;
 
//    CD_MODALITA_EROGAZIONE CHAR(1)
	private java.lang.String cdModalitaErogazione;
 
//    FL_INVIATO CHAR(1) NOT NULL
	private Boolean flInviato;
 
//    NR_PROTOCOLLO DECIMAL(6,0)
	private java.lang.String nrProtocollo;
 
//    NR_PROGRESSIVO DECIMAL(5,0)
	private java.lang.Integer nrProgressivo;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: FATTURA_PASSIVA_INTRA_S
	 **/
	public FatturaPassivaIntraSBase() {
		super();
	}
	public FatturaPassivaIntraSBase(java.lang.String cdCds, java.lang.String cdUnitaOrganizzativa, java.lang.Integer esercizio, java.lang.Long pgFatturaPassiva, java.lang.Long pgRigaIntra, java.lang.Long pgStorico) {
		super(cdCds, cdUnitaOrganizzativa, esercizio, pgFatturaPassiva, pgRigaIntra, pgStorico);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [ammontareEuro]
	 **/
	public java.math.BigDecimal getAmmontareEuro() {
		return ammontareEuro;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [ammontareEuro]
	 **/
	public void setAmmontareEuro(java.math.BigDecimal ammontareEuro)  {
		this.ammontareEuro=ammontareEuro;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [ammontareDivisa]
	 **/
	public java.math.BigDecimal getAmmontareDivisa() {
		return ammontareDivisa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [ammontareDivisa]
	 **/
	public void setAmmontareDivisa(java.math.BigDecimal ammontareDivisa)  {
		this.ammontareDivisa=ammontareDivisa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [idNaturaTransazione]
	 **/
	public java.lang.Long getIdNaturaTransazione() {
		return idNaturaTransazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [idNaturaTransazione]
	 **/
	public void setIdNaturaTransazione(java.lang.Long idNaturaTransazione)  {
		this.idNaturaTransazione=idNaturaTransazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [idNomenclaturaCombinata]
	 **/
	public java.lang.Long getIdNomenclaturaCombinata() {
		return idNomenclaturaCombinata;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [idNomenclaturaCombinata]
	 **/
	public void setIdNomenclaturaCombinata(java.lang.Long idNomenclaturaCombinata)  {
		this.idNomenclaturaCombinata=idNomenclaturaCombinata;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [massaNetta]
	 **/
	public java.math.BigDecimal getMassaNetta() {
		return massaNetta;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [massaNetta]
	 **/
	public void setMassaNetta(java.math.BigDecimal massaNetta)  {
		this.massaNetta=massaNetta;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [unitaSupplementari]
	 **/
	public java.lang.Long getUnitaSupplementari() {
		return unitaSupplementari;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [unitaSupplementari]
	 **/
	public void setUnitaSupplementari(java.lang.Long unitaSupplementari)  {
		this.unitaSupplementari=unitaSupplementari;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [valoreStatistico]
	 **/
	public java.math.BigDecimal getValoreStatistico() {
		return valoreStatistico;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [valoreStatistico]
	 **/
	public void setValoreStatistico(java.math.BigDecimal valoreStatistico)  {
		this.valoreStatistico=valoreStatistico;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizioCondConsegna]
	 **/
	public java.lang.Integer getEsercizioCondConsegna() {
		return esercizioCondConsegna;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizioCondConsegna]
	 **/
	public void setEsercizioCondConsegna(java.lang.Integer esercizioCondConsegna)  {
		this.esercizioCondConsegna=esercizioCondConsegna;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdIncoterm]
	 **/
	public java.lang.String getCdIncoterm() {
		return cdIncoterm;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdIncoterm]
	 **/
	public void setCdIncoterm(java.lang.String cdIncoterm)  {
		this.cdIncoterm=cdIncoterm;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizioModTrasporto]
	 **/
	public java.lang.Integer getEsercizioModTrasporto() {
		return esercizioModTrasporto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizioModTrasporto]
	 **/
	public void setEsercizioModTrasporto(java.lang.Integer esercizioModTrasporto)  {
		this.esercizioModTrasporto=esercizioModTrasporto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdModalitaTrasporto]
	 **/
	public java.lang.String getCdModalitaTrasporto() {
		return cdModalitaTrasporto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdModalitaTrasporto]
	 **/
	public void setCdModalitaTrasporto(java.lang.String cdModalitaTrasporto)  {
		this.cdModalitaTrasporto=cdModalitaTrasporto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgNazioneProvenienza]
	 **/
	public java.lang.Long getPgNazioneProvenienza() {
		return pgNazioneProvenienza;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgNazioneProvenienza]
	 **/
	public void setPgNazioneProvenienza(java.lang.Long pgNazioneProvenienza)  {
		this.pgNazioneProvenienza=pgNazioneProvenienza;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgNazioneOrigine]
	 **/
	public java.lang.Long getPgNazioneOrigine() {
		return pgNazioneOrigine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgNazioneOrigine]
	 **/
	public void setPgNazioneOrigine(java.lang.Long pgNazioneOrigine)  {
		this.pgNazioneOrigine=pgNazioneOrigine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdProvinciaDestinazione]
	 **/
	public java.lang.String getCdProvinciaDestinazione() {
		return cdProvinciaDestinazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdProvinciaDestinazione]
	 **/
	public void setCdProvinciaDestinazione(java.lang.String cdProvinciaDestinazione)  {
		this.cdProvinciaDestinazione=cdProvinciaDestinazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dsBene]
	 **/
	public java.lang.String getDsBene() {
		return dsBene;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dsBene]
	 **/
	public void setDsBene(java.lang.String dsBene)  {
		this.dsBene=dsBene;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [idCpa]
	 **/
	public java.lang.Long getIdCpa() {
		return idCpa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [idCpa]
	 **/
	public void setIdCpa(java.lang.Long idCpa)  {
		this.idCpa=idCpa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizioModIncasso]
	 **/
	public java.lang.Integer getEsercizioModIncasso() {
		return esercizioModIncasso;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizioModIncasso]
	 **/
	public void setEsercizioModIncasso(java.lang.Integer esercizioModIncasso)  {
		this.esercizioModIncasso=esercizioModIncasso;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdModalitaIncasso]
	 **/
	public java.lang.String getCdModalitaIncasso() {
		return cdModalitaIncasso;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdModalitaIncasso]
	 **/
	public void setCdModalitaIncasso(java.lang.String cdModalitaIncasso)  {
		this.cdModalitaIncasso=cdModalitaIncasso;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizioModErogazione]
	 **/
	public java.lang.Integer getEsercizioModErogazione() {
		return esercizioModErogazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizioModErogazione]
	 **/
	public void setEsercizioModErogazione(java.lang.Integer esercizioModErogazione)  {
		this.esercizioModErogazione=esercizioModErogazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdModalitaErogazione]
	 **/
	public java.lang.String getCdModalitaErogazione() {
		return cdModalitaErogazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdModalitaErogazione]
	 **/
	public void setCdModalitaErogazione(java.lang.String cdModalitaErogazione)  {
		this.cdModalitaErogazione=cdModalitaErogazione;
	}
	
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [nrProtocollo]
	 **/
	public java.lang.String getNrProtocollo() {
		return nrProtocollo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [nrProtocollo]
	 **/
	public void setNrProtocollo(java.lang.String nrProtocollo)  {
		this.nrProtocollo=nrProtocollo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [nrProgressivo]
	 **/
	public java.lang.Integer getNrProgressivo() {
		return nrProgressivo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [nrProgressivo]
	 **/
	public void setNrProgressivo(java.lang.Integer nrProgressivo)  {
		this.nrProgressivo=nrProgressivo;
	}
	public Boolean getFlInviato() {
		return flInviato;
	}
	public void setFlInviato(Boolean flInviato) {
		this.flInviato = flInviato;
	}
}