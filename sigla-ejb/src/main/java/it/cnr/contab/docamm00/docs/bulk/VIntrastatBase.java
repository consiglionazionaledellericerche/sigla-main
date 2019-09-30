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
 * Date 22/04/2010
 */
package it.cnr.contab.docamm00.docs.bulk;
import it.cnr.jada.persistency.Keyed;
public class VIntrastatBase extends VIntrastatKey implements Keyed {
//    TIPO CHAR(1)
	private java.lang.String tipo;
 
//    TI_BENE_SERVIZIO CHAR(1)
	private java.lang.String tiBeneServizio;
 
//    DT_REGISTRAZIONE TIMESTAMP(7)
	private java.sql.Timestamp dtRegistrazione;
 
//    NR_FATTURA VARCHAR(40)
	private java.lang.String nrFattura;
 
	private java.lang.String dtFattura;
 
//    PARTITA_IVA VARCHAR(20)
	private java.lang.String partitaIva;
 
//    CD_NATURA_TRANSAZIONE VARCHAR(5)
	private java.lang.String cdNaturaTransazione;
 
//    CD_NOMENCLATURA_COMBINATA VARCHAR(8)
	private java.lang.String cdNomenclaturaCombinata;
 
//    AMMONTARE_EURO DECIMAL(22,0)
	private java.lang.Long ammontareEuro;
 
//    MASSA_NETTA DECIMAL(22,0)
	private java.lang.Long massaNetta;
 
//    UNITA_SUPPLEMENTARI DECIMAL(22,0)
	private java.lang.Long unitaSupplementari;
 
//    VALORE_STATISTICO DECIMAL(22,0)
	private java.lang.Long valoreStatistico;
 
//    CD_CONSEGNA VARCHAR(5)
	private java.lang.String cdConsegna;
 
//    CD_MODALITA_TRASPORTO VARCHAR(5)
	private java.lang.String cdModalitaTrasporto;
 
//    CD_CPA VARCHAR(6)
	private java.lang.String cdCpa;
 
//    CD_MODALITA_INCASSO CHAR(1)
	private java.lang.String cdModalitaIncasso;
 
//    CD_MODALITA_EROGAZIONE CHAR(1)
	private java.lang.String cdModalitaErogazione;
 
//    PROVENIENZA VARCHAR(10)
	private java.lang.String provenienza;
 
//    ORIGINE VARCHAR(10)
	private java.lang.String origine;
 
//    DEST VARCHAR(10)
	private java.lang.String dest;
 
//    CD_PROVINCIA_DESTINAZIONE VARCHAR(10)
	private java.lang.String cdProvinciaDestinazione;
 
//    CD_PROVINCIA_ORIGINE VARCHAR(10)
	private java.lang.String cdProvinciaOrigine;
 
	private java.lang.Integer mese;
	
//  TI_FATTURA CHAR(1)
	private java.lang.String tiFattura;
	
	private java.lang.Long ammontareDivisa;
 
	private java.lang.String nazFiscale;
	
	private java.lang.String nrProtocollo;
	
	private java.lang.Integer nrProgressivo;
	
	private java.lang.String ds_tipo;
	
	private java.lang.String uo_origine;
	
	public java.lang.String getDs_tipo() {
		return ds_tipo;
	}
	public void setDs_tipo(java.lang.String dsTipo) {
		ds_tipo = dsTipo;
	}
	public java.lang.String getUo_origine() {
		return uo_origine;
	}
	public void setUo_origine(java.lang.String cdUoOrigine) {
		uo_origine = cdUoOrigine;
	}
	public VIntrastatBase() {
		super();
	}
	public VIntrastatBase(java.lang.String cd_cds,java.lang.String cd_unita_organizzativa,java.lang.Integer esercizio,java.lang.Long pg_fattura,java.lang.Long pg_riga_intra,java.lang.Long pgStorico) {
		super(cd_cds,cd_unita_organizzativa,esercizio,pg_fattura,pg_riga_intra,pgStorico);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [tipo]
	 **/
	public java.lang.String getTipo() {
		return tipo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [tipo]
	 **/
	public void setTipo(java.lang.String tipo)  {
		this.tipo=tipo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [tiBeneServizio]
	 **/
	public java.lang.String getTiBeneServizio() {
		return tiBeneServizio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [tiBeneServizio]
	 **/
	public void setTiBeneServizio(java.lang.String tiBeneServizio)  {
		this.tiBeneServizio=tiBeneServizio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtRegistrazione]
	 **/
	public java.sql.Timestamp getDtRegistrazione() {
		return dtRegistrazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtRegistrazione]
	 **/
	public void setDtRegistrazione(java.sql.Timestamp dtRegistrazione)  {
		this.dtRegistrazione=dtRegistrazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [nrFattura]
	 **/
	public java.lang.String getNrFattura() {
		return nrFattura;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [nrFattura]
	 **/
	public void setNrFattura(java.lang.String nrFattura)  {
		this.nrFattura=nrFattura;
	}
	
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [partitaIva]
	 **/
	public java.lang.String getPartitaIva() {
		return partitaIva;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [partitaIva]
	 **/
	public void setPartitaIva(java.lang.String partitaIva)  {
		this.partitaIva=partitaIva;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdNaturaTransazione]
	 **/
	public java.lang.String getCdNaturaTransazione() {
		return cdNaturaTransazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdNaturaTransazione]
	 **/
	public void setCdNaturaTransazione(java.lang.String cdNaturaTransazione)  {
		this.cdNaturaTransazione=cdNaturaTransazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdNomenclaturaCombinata]
	 **/
	public java.lang.String getCdNomenclaturaCombinata() {
		return cdNomenclaturaCombinata;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdNomenclaturaCombinata]
	 **/
	public void setCdNomenclaturaCombinata(java.lang.String cdNomenclaturaCombinata)  {
		this.cdNomenclaturaCombinata=cdNomenclaturaCombinata;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [ammontareEuro]
	 **/
	public java.lang.Long getAmmontareEuro() {
		return ammontareEuro;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [ammontareEuro]
	 **/
	public void setAmmontareEuro(java.lang.Long ammontareEuro)  {
		this.ammontareEuro=ammontareEuro;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [massaNetta]
	 **/
	public java.lang.Long getMassaNetta() {
		return massaNetta;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [massaNetta]
	 **/
	public void setMassaNetta(java.lang.Long massaNetta)  {
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
	public java.lang.Long getValoreStatistico() {
		return valoreStatistico;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [valoreStatistico]
	 **/
	public void setValoreStatistico(java.lang.Long valoreStatistico)  {
		this.valoreStatistico=valoreStatistico;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdConsegna]
	 **/
	public java.lang.String getCdConsegna() {
		return cdConsegna;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdConsegna]
	 **/
	public void setCdConsegna(java.lang.String cdConsegna)  {
		this.cdConsegna=cdConsegna;
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
	 * Restituisce il valore di: [cdCpa]
	 **/
	public java.lang.String getCdCpa() {
		return cdCpa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCpa]
	 **/
	public void setCdCpa(java.lang.String cdCpa)  {
		this.cdCpa=cdCpa;
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
	 * Restituisce il valore di: [provenienza]
	 **/
	public java.lang.String getProvenienza() {
		return provenienza;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [provenienza]
	 **/
	public void setProvenienza(java.lang.String provenienza)  {
		this.provenienza=provenienza;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [origine]
	 **/
	public java.lang.String getOrigine() {
		return origine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [origine]
	 **/
	public void setOrigine(java.lang.String origine)  {
		this.origine=origine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dest]
	 **/
	public java.lang.String getDest() {
		return dest;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dest]
	 **/
	public void setDest(java.lang.String dest)  {
		this.dest=dest;
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
	 * Restituisce il valore di: [cdProvinciaOrigine]
	 **/
	public java.lang.String getCdProvinciaOrigine() {
		return cdProvinciaOrigine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdProvinciaOrigine]
	 **/
	public void setCdProvinciaOrigine(java.lang.String cdProvinciaOrigine)  {
		this.cdProvinciaOrigine=cdProvinciaOrigine;
	}
	public java.lang.Integer getMese() {
		return mese;
	}
	public void setMese(java.lang.Integer mese) {
		this.mese = mese;
	}
	public java.lang.String getTiFattura() {
		return tiFattura;
	}
	public void setTiFattura(java.lang.String tiFattura) {
		this.tiFattura = tiFattura;
	}
	public java.lang.Long getAmmontareDivisa() {
		return ammontareDivisa;
	}
	public void setAmmontareDivisa(java.lang.Long ammontareDivisa) {
		this.ammontareDivisa = ammontareDivisa;
	}
	public java.lang.String getNazFiscale() {
		return nazFiscale;
	}
	public void setNazFiscale(java.lang.String nazFiscale) {
		this.nazFiscale = nazFiscale;
	}
	public java.lang.String getDtFattura() {
		return dtFattura;
	}
	public void setDtFattura(java.lang.String dtFattura) {
		this.dtFattura = dtFattura;
	}
	public java.lang.String getNrProtocollo() {
		return nrProtocollo;
	}
	public void setNrProtocollo(java.lang.String nrProtocollo) {
		this.nrProtocollo = nrProtocollo;
	}
	public java.lang.Integer getNrProgressivo() {
		return nrProgressivo;
	}
	public void setNrProgressivo(java.lang.Integer nrProgressivo) {
		this.nrProgressivo = nrProgressivo;
	}
}