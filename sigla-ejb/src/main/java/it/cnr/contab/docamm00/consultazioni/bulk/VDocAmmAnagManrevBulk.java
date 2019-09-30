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
 * Date 18/11/2016
 */
package it.cnr.contab.docamm00.consultazioni.bulk;
import java.util.Hashtable;

import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.docamm00.docs.bulk.Numerazione_doc_ammBulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;


public class VDocAmmAnagManrevBulk extends OggettoBulk implements Persistent {
	static private java.util.Hashtable fatturaPassivaKeys;
	static private java.util.Hashtable fatturaAttivaKeys;	
	private java.util.Hashtable tipoDocumentoKeys;
	{
		tipoDocumentoKeys =  new Hashtable();
		tipoDocumentoKeys.put(Numerazione_doc_ammBulk.TIPO_DOC_GENERICO_S,"Generico Spesa");
		tipoDocumentoKeys.put(Numerazione_doc_ammBulk.TIPO_ANTICIPO,"Anticipo");
		tipoDocumentoKeys.put(Numerazione_doc_ammBulk.TIPO_COMPENSO,"Compenso");
		tipoDocumentoKeys.put(Numerazione_doc_ammBulk.TIPO_GEN_AP_FON,"Documento generico di apertura del fondo economale");
		tipoDocumentoKeys.put(Numerazione_doc_ammBulk.TIPO_MISSIONE,"Missione");
		tipoDocumentoKeys.put(Numerazione_doc_ammBulk.TIPO_DOC_GENERICO_E,"Generico Entrata");
		tipoDocumentoKeys.put(Numerazione_doc_ammBulk.TIPO_RIMBORSO,"Rimborso");
		tipoDocumentoKeys.put(Numerazione_doc_ammBulk.TIPO_GEN_CH_FON,"Documento generico di chiusura del fondo economale");
		
	};
	static 
	{
		fatturaPassivaKeys = new Hashtable();
		fatturaPassivaKeys.put(it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk.TIPO_FATTURA_PASSIVA,	"Fattura Passiva");
		fatturaPassivaKeys.put(it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk.TIPO_NOTA_DI_CREDITO,	"Nota di credito");
		fatturaPassivaKeys.put(it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk.TIPO_NOTA_DI_DEBITO,	"Nota di debito");		
	};

	static 
	{
		fatturaAttivaKeys = new Hashtable();
		fatturaAttivaKeys.put(it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk.TIPO_FATTURA_ATTIVA,	"Fattura Attiva");
		fatturaAttivaKeys.put(it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk.TIPO_NOTA_DI_CREDITO,	"Nota di credito");
		fatturaAttivaKeys.put(it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk.TIPO_NOTA_DI_DEBITO,	"Nota di debito");		
	};
	
	private AnagraficoBulk anagrafico;
//    DT_REGISTRAZIONE TIMESTAMP(7)
	private java.sql.Timestamp dtRegistrazione;
 
//    CD_CDS VARCHAR(30)
	private java.lang.String cdCds;
 
//    CD_UNITA_ORGANIZZATIVA VARCHAR(30)
	private java.lang.String cdUnitaOrganizzativa;
 
//    ESERCIZIO DECIMAL(4,0)
	private java.lang.Integer esercizio;
 
//    CD_TIPO_DOCUMENTO_AMM VARCHAR(10)
	private java.lang.String cdTipoDocumentoAmm;
	
	private java.lang.String dsTipoDocumentoAmm;
 
//    PG_DOCUMENTO_AMM DECIMAL(10,0)
	private java.lang.Long pgDocumentoAmm;
 
//    DT_EMISSIONE TIMESTAMP(7)
	private java.sql.Timestamp dtEmissione;
 
//    CD_CDS_ORIGINE VARCHAR(30)
	private java.lang.String cdCdsOrigine;
 
//    CD_UO_ORIGINE VARCHAR(30)
	private java.lang.String cdUoOrigine;
 
//    TI_FATTURA CHAR(1)
	private java.lang.String tiFattura;
 
//    STATO_COFI CHAR(1)
	private java.lang.String statoCofi;
 
//    STATO_PAGAMENTO_FONDO_ECO CHAR(1)
	private java.lang.String statoPagamentoFondoEco;
 
//    DT_PAGAMENTO_FONDO_ECO TIMESTAMP(7)
	private java.sql.Timestamp dtPagamentoFondoEco;
 
//    DT_FATTURA_FORNITORE TIMESTAMP(7)
	private java.sql.Timestamp dtFatturaFornitore;
 
//    NR_FATTURA_FORNITORE VARCHAR(20)
	private java.lang.String nrFatturaFornitore;
 
//    CD_TERZO DECIMAL(8,0)
	private java.lang.Integer cdTerzo;
 
//    CD_TERZO_CESSIONARIO DECIMAL(22,0)
	private java.lang.Long cdTerzoCessionario;
 
//    COGNOME VARCHAR(50)
	private java.lang.String cognome;
 
//    NOME VARCHAR(50)
	private java.lang.String nome;
 
//    RAGIONE_SOCIALE VARCHAR(100)
	private java.lang.String ragioneSociale;
 
//    CD_MODALITA_PAG VARCHAR(40)
	private java.lang.String cdModalitaPag;
 
//    IM_TOTALE_DOC_AMM DECIMAL(22,0)
	private java.math.BigDecimal imTotaleDocAmm;
 
//    CODICE_FISCALE VARCHAR(20)
	private java.lang.String codiceFiscale;
 
//    PARTITA_IVA VARCHAR(20)
	private java.lang.String partitaIva;
 
//    DS_DOCUMENTO VARCHAR(4000)
	private java.lang.String dsDocumento;
 
//    ESERCIZIO_MANREV DECIMAL(4,0)
	private java.lang.Integer esercizioManrev;
 
//    CDS_MANREV VARCHAR(30)
	private java.lang.String cdsManrev;
 
//    NR_MANREV DECIMAL(10,0)
	private java.lang.Long nrManrev;
	private java.lang.Integer cdAnag;
	private java.lang.String tipologia;

	private java.lang.String ragione_sociale;
  
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
	 * Restituisce il valore di: [cdCds]
	 **/
	public java.lang.String getCdCds() {
		return cdCds;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCds]
	 **/
	public void setCdCds(java.lang.String cdCds)  {
		this.cdCds=cdCds;
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
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizio]
	 **/
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizio]
	 **/
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTipoDocumentoAmm]
	 **/
	public java.lang.String getCdTipoDocumentoAmm() {
		return cdTipoDocumentoAmm;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTipoDocumentoAmm]
	 **/
	public void setCdTipoDocumentoAmm(java.lang.String cdTipoDocumentoAmm)  {
		this.cdTipoDocumentoAmm=cdTipoDocumentoAmm;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgDocumentoAmm]
	 **/
	public java.lang.Long getPgDocumentoAmm() {
		return pgDocumentoAmm;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgDocumentoAmm]
	 **/
	public void setPgDocumentoAmm(java.lang.Long pgDocumentoAmm)  {
		this.pgDocumentoAmm=pgDocumentoAmm;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtEmissione]
	 **/
	public java.sql.Timestamp getDtEmissione() {
		return dtEmissione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtEmissione]
	 **/
	public void setDtEmissione(java.sql.Timestamp dtEmissione)  {
		this.dtEmissione=dtEmissione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdsOrigine]
	 **/
	public java.lang.String getCdCdsOrigine() {
		return cdCdsOrigine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsOrigine]
	 **/
	public void setCdCdsOrigine(java.lang.String cdCdsOrigine)  {
		this.cdCdsOrigine=cdCdsOrigine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdUoOrigine]
	 **/
	public java.lang.String getCdUoOrigine() {
		return cdUoOrigine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdUoOrigine]
	 **/
	public void setCdUoOrigine(java.lang.String cdUoOrigine)  {
		this.cdUoOrigine=cdUoOrigine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [tiFattura]
	 **/
	public java.lang.String getTiFattura() {
		return tiFattura;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [tiFattura]
	 **/
	public void setTiFattura(java.lang.String tiFattura)  {
		this.tiFattura=tiFattura;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [statoCofi]
	 **/
	public java.lang.String getStatoCofi() {
		return statoCofi;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [statoCofi]
	 **/
	public void setStatoCofi(java.lang.String statoCofi)  {
		this.statoCofi=statoCofi;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [statoPagamentoFondoEco]
	 **/
	public java.lang.String getStatoPagamentoFondoEco() {
		return statoPagamentoFondoEco;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [statoPagamentoFondoEco]
	 **/
	public void setStatoPagamentoFondoEco(java.lang.String statoPagamentoFondoEco)  {
		this.statoPagamentoFondoEco=statoPagamentoFondoEco;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtPagamentoFondoEco]
	 **/
	public java.sql.Timestamp getDtPagamentoFondoEco() {
		return dtPagamentoFondoEco;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtPagamentoFondoEco]
	 **/
	public void setDtPagamentoFondoEco(java.sql.Timestamp dtPagamentoFondoEco)  {
		this.dtPagamentoFondoEco=dtPagamentoFondoEco;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtFatturaFornitore]
	 **/
	public java.sql.Timestamp getDtFatturaFornitore() {
		return dtFatturaFornitore;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtFatturaFornitore]
	 **/
	public void setDtFatturaFornitore(java.sql.Timestamp dtFatturaFornitore)  {
		this.dtFatturaFornitore=dtFatturaFornitore;
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
	 * Restituisce il valore di: [cdTerzo]
	 **/
	public java.lang.Integer getCdTerzo() {
		return cdTerzo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTerzo]
	 **/
	public void setCdTerzo(java.lang.Integer cdTerzo)  {
		this.cdTerzo=cdTerzo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTerzoCessionario]
	 **/
	public java.lang.Long getCdTerzoCessionario() {
		return cdTerzoCessionario;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTerzoCessionario]
	 **/
	public void setCdTerzoCessionario(java.lang.Long cdTerzoCessionario)  {
		this.cdTerzoCessionario=cdTerzoCessionario;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cognome]
	 **/
	public java.lang.String getCognome() {
		return cognome;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cognome]
	 **/
	public void setCognome(java.lang.String cognome)  {
		this.cognome=cognome;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [nome]
	 **/
	public java.lang.String getNome() {
		return nome;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [nome]
	 **/
	public void setNome(java.lang.String nome)  {
		this.nome=nome;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [ragioneSociale]
	 **/
	public java.lang.String getRagioneSociale() {
		return ragioneSociale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [ragioneSociale]
	 **/
	public void setRagioneSociale(java.lang.String ragioneSociale)  {
		this.ragioneSociale=ragioneSociale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdModalitaPag]
	 **/
	public java.lang.String getCdModalitaPag() {
		return cdModalitaPag;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdModalitaPag]
	 **/
	public void setCdModalitaPag(java.lang.String cdModalitaPag)  {
		this.cdModalitaPag=cdModalitaPag;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [imTotaleDocAmm]
	 **/
	public java.math.BigDecimal getImTotaleDocAmm() {
		return imTotaleDocAmm;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [imTotaleDocAmm]
	 **/
	public void setImTotaleDocAmm(java.math.BigDecimal imTotaleDocAmm)  {
		this.imTotaleDocAmm=imTotaleDocAmm;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [codiceFiscale]
	 **/
	public java.lang.String getCodiceFiscale() {
		return codiceFiscale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [codiceFiscale]
	 **/
	public void setCodiceFiscale(java.lang.String codiceFiscale)  {
		this.codiceFiscale=codiceFiscale;
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
	 * Restituisce il valore di: [dsDocumento]
	 **/
	public java.lang.String getDsDocumento() {
		return dsDocumento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dsDocumento]
	 **/
	public void setDsDocumento(java.lang.String dsDocumento)  {
		this.dsDocumento=dsDocumento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizioManrev]
	 **/
	public java.lang.Integer getEsercizioManrev() {
		return esercizioManrev;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizioManrev]
	 **/
	public void setEsercizioManrev(java.lang.Integer esercizioManrev)  {
		this.esercizioManrev=esercizioManrev;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdsManrev]
	 **/
	public java.lang.String getCdsManrev() {
		return cdsManrev;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdsManrev]
	 **/
	public void setCdsManrev(java.lang.String cdsManrev)  {
		this.cdsManrev=cdsManrev;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [nrManrev]
	 **/
	public java.lang.Long getNrManrev() {
		return nrManrev;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [nrManrev]
	 **/
	public void setNrManrev(java.lang.Long nrManrev)  {
		this.nrManrev=nrManrev;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: V_DOC_AMM_ANAG_MANREV
	 **/
	public VDocAmmAnagManrevBulk() {
		super();
	}
	public AnagraficoBulk getAnagrafico() {
		return anagrafico;
	}
	public void setAnagrafico(AnagraficoBulk anagrafico) {
		this.anagrafico = anagrafico;
		if (anagrafico!=null)
			setCdAnag(getAnagrafico().getCd_anag());
		if (getAnagrafico().getRagione_sociale()!=null)
			setRagione_sociale(getAnagrafico().getRagione_sociale());
		else if (getAnagrafico().getCognome()!=null)
			setRagione_sociale(getAnagrafico().getCognome()+" - "+getAnagrafico().getNome());
		else
			setRagione_sociale(null);
	}
	public java.lang.Integer getCdAnag() {
		return cdAnag;
	}
	public void setCdAnag(java.lang.Integer cdAnag) {
		this.cdAnag = cdAnag;
	}
	public java.lang.String getRagione_sociale() {
		return ragione_sociale;
	}
	public void setRagione_sociale(java.lang.String ragione_sociale) {
		this.ragione_sociale = ragione_sociale;
	}
	
	public java.lang.String getTipologia() {
		return tipologia;
	}
	public void setTipologia(java.lang.String tipologia) {
		this.tipologia = tipologia;
	}
	public java.lang.String getDsTipoDocumentoAmm() 
	{
		if(getCdTipoDocumentoAmm()!=null){
			if ( Numerazione_doc_ammBulk.TIPO_FATTURA_PASSIVA.equals( getCdTipoDocumentoAmm()))
				return (String) getFatturaPassivaKeys().get( getTiFattura() );
			else if ( Numerazione_doc_ammBulk.TIPO_FATTURA_ATTIVA.equals( getCdTipoDocumentoAmm()))	
				return (String) getFatturaAttivaKeys().get( getTiFattura() );
			return (String) getTipoDocumentoKeys().get( getCdTipoDocumentoAmm() );
		} else return null;
	}
	public static java.util.Hashtable getFatturaPassivaKeys() {
		return fatturaPassivaKeys;
	}
	public static java.util.Hashtable getFatturaAttivaKeys() {
		return fatturaAttivaKeys;
	}
	public  java.util.Hashtable getTipoDocumentoKeys() {
		return tipoDocumentoKeys;
	}
}