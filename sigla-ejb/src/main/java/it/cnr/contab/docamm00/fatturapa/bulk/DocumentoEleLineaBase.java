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
 * Date 25/02/2015
 */
package it.cnr.contab.docamm00.fatturapa.bulk;
import it.cnr.jada.persistency.Keyed;
public class DocumentoEleLineaBase extends DocumentoEleLineaKey implements Keyed {
//    TIPO_CESSIONE VARCHAR(2)
	private java.lang.String tipoCessione;
 
//    ARTICOLO_TIPO VARCHAR(35)
	private java.lang.String articoloTipo;
 
//    ARTICOLO_VALORE VARCHAR(35)
	private java.lang.String articoloValore;

//  CD_BENE_SERVIZIO VARCHAR(10)
	private java.lang.String cdBeneServizio;
 
//    LINEA_DESCRIZIONE VARCHAR(1000)
	private java.lang.String lineaDescrizione;
 
//    LINEA_QUANTITA DECIMAL(17,2)
	private java.math.BigDecimal lineaQuantita;
 
//    LINEA_UNITAMISURA VARCHAR(10)
	private java.lang.String lineaUnitamisura;
 
//    INIZIO_DATACOMPETENZA TIMESTAMP(7)
	private java.sql.Timestamp inizioDatacompetenza;
 
//    FINE_DATACOMPETENZA TIMESTAMP(7)
	private java.sql.Timestamp fineDatacompetenza;
 
//    LINEA_PREZZOUNITARIO DECIMAL(17,2)
	private java.math.BigDecimal lineaPrezzounitario;
 
//    TIPO_SCONTOMAG VARCHAR(2)
	private java.lang.String tipoScontomag;
 
//    PERCENTUALE_SCONTOMAG DECIMAL(17,2)
	private java.math.BigDecimal percentualeScontomag;
 
//    IMPORTO_SCONTOMAG DECIMAL(17,2)
	private java.math.BigDecimal importoScontomag;
 
//    LINEA_PREZZOTOTALE DECIMAL(17,2)
	private java.math.BigDecimal lineaPrezzototale;
 
//    LINEA_ALIQUOTAIVA DECIMAL(17,2)
	private java.math.BigDecimal lineaAliquotaiva;
 
//    LINEA_RITENUTA VARCHAR(2)
	private java.lang.String lineaRitenuta;
 
//    LINEA_NATURA VARCHAR(256)
	private java.lang.String lineaNatura;
 
//    LINEA_RIFERIMENTOAMM VARCHAR(20)
	private java.lang.String lineaRiferimentoamm;
 
//    TIPO_DATO VARCHAR(10)
	private java.lang.String tipoDato;
 
//    RIFERIMENTO_TESTO VARCHAR(60)
	private java.lang.String riferimentoTesto;
 
//    RIFERIMENTO_NUMERO DECIMAL(17,2)
	private java.math.BigDecimal riferimentoNumero;
 
//    RIFERIMENTODATA TIMESTAMP(7)
	private java.sql.Timestamp riferimentodata;
 
//    ANOMALIE VARCHAR(2000)
	private java.lang.String anomalie;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: DOCUMENTO_ELE_LINEA
	 **/
	public DocumentoEleLineaBase() {
		super();
	}
	public DocumentoEleLineaBase(java.lang.String idPaese, java.lang.String idCodice, java.lang.Long progressivoInvio, java.lang.Long progressivo, java.lang.Integer numeroLinea) {
		super(idPaese, idCodice, progressivoInvio, progressivo, numeroLinea);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [tipoCessione]
	 **/
	public java.lang.String getTipoCessione() {
		return tipoCessione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [tipoCessione]
	 **/
	public void setTipoCessione(java.lang.String tipoCessione)  {
		this.tipoCessione=tipoCessione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [articoloTipo]
	 **/
	public java.lang.String getArticoloTipo() {
		return articoloTipo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [articoloTipo]
	 **/
	public void setArticoloTipo(java.lang.String articoloTipo)  {
		this.articoloTipo=articoloTipo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [articoloValore]
	 **/
	public java.lang.String getArticoloValore() {
		return articoloValore;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [articoloValore]
	 **/
	public void setArticoloValore(java.lang.String articoloValore)  {
		this.articoloValore=articoloValore;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [lineaDescrizione]
	 **/
	public java.lang.String getLineaDescrizione() {
		return lineaDescrizione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [lineaDescrizione]
	 **/
	public void setLineaDescrizione(java.lang.String lineaDescrizione)  {
		this.lineaDescrizione=lineaDescrizione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [lineaQuantita]
	 **/
	public java.math.BigDecimal getLineaQuantita() {
		return lineaQuantita;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [lineaQuantita]
	 **/
	public void setLineaQuantita(java.math.BigDecimal lineaQuantita)  {
		this.lineaQuantita=lineaQuantita;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [lineaUnitamisura]
	 **/
	public java.lang.String getLineaUnitamisura() {
		return lineaUnitamisura;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [lineaUnitamisura]
	 **/
	public void setLineaUnitamisura(java.lang.String lineaUnitamisura)  {
		this.lineaUnitamisura=lineaUnitamisura;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [inizioDatacompetenza]
	 **/
	public java.sql.Timestamp getInizioDatacompetenza() {
		return inizioDatacompetenza;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [inizioDatacompetenza]
	 **/
	public void setInizioDatacompetenza(java.sql.Timestamp inizioDatacompetenza)  {
		this.inizioDatacompetenza=inizioDatacompetenza;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [fineDatacompetenza]
	 **/
	public java.sql.Timestamp getFineDatacompetenza() {
		return fineDatacompetenza;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [fineDatacompetenza]
	 **/
	public void setFineDatacompetenza(java.sql.Timestamp fineDatacompetenza)  {
		this.fineDatacompetenza=fineDatacompetenza;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [lineaPrezzounitario]
	 **/
	public java.math.BigDecimal getLineaPrezzounitario() {
		return lineaPrezzounitario;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [lineaPrezzounitario]
	 **/
	public void setLineaPrezzounitario(java.math.BigDecimal lineaPrezzounitario)  {
		this.lineaPrezzounitario=lineaPrezzounitario;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [tipoScontomag]
	 **/
	public java.lang.String getTipoScontomag() {
		return tipoScontomag;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [tipoScontomag]
	 **/
	public void setTipoScontomag(java.lang.String tipoScontomag)  {
		this.tipoScontomag=tipoScontomag;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [percentualeScontomag]
	 **/
	public java.math.BigDecimal getPercentualeScontomag() {
		return percentualeScontomag;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [percentualeScontomag]
	 **/
	public void setPercentualeScontomag(java.math.BigDecimal percentualeScontomag)  {
		this.percentualeScontomag=percentualeScontomag;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [importoScontomag]
	 **/
	public java.math.BigDecimal getImportoScontomag() {
		return importoScontomag;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [importoScontomag]
	 **/
	public void setImportoScontomag(java.math.BigDecimal importoScontomag)  {
		this.importoScontomag=importoScontomag;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [lineaPrezzototale]
	 **/
	public java.math.BigDecimal getLineaPrezzototale() {
		return lineaPrezzototale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [lineaPrezzototale]
	 **/
	public void setLineaPrezzototale(java.math.BigDecimal lineaPrezzototale)  {
		this.lineaPrezzototale=lineaPrezzototale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [lineaAliquotaiva]
	 **/
	public java.math.BigDecimal getLineaAliquotaiva() {
		return lineaAliquotaiva;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [lineaAliquotaiva]
	 **/
	public void setLineaAliquotaiva(java.math.BigDecimal lineaAliquotaiva)  {
		this.lineaAliquotaiva=lineaAliquotaiva;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [lineaRitenuta]
	 **/
	public java.lang.String getLineaRitenuta() {
		return lineaRitenuta;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [lineaRitenuta]
	 **/
	public void setLineaRitenuta(java.lang.String lineaRitenuta)  {
		this.lineaRitenuta=lineaRitenuta;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [lineaNatura]
	 **/
	public java.lang.String getLineaNatura() {
		return lineaNatura;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [lineaNatura]
	 **/
	public void setLineaNatura(java.lang.String lineaNatura)  {
		this.lineaNatura=lineaNatura;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [lineaRiferimentoamm]
	 **/
	public java.lang.String getLineaRiferimentoamm() {
		return lineaRiferimentoamm;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [lineaRiferimentoamm]
	 **/
	public void setLineaRiferimentoamm(java.lang.String lineaRiferimentoamm)  {
		this.lineaRiferimentoamm=lineaRiferimentoamm;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [tipoDato]
	 **/
	public java.lang.String getTipoDato() {
		return tipoDato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [tipoDato]
	 **/
	public void setTipoDato(java.lang.String tipoDato)  {
		this.tipoDato=tipoDato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [riferimentoTesto]
	 **/
	public java.lang.String getRiferimentoTesto() {
		return riferimentoTesto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [riferimentoTesto]
	 **/
	public void setRiferimentoTesto(java.lang.String riferimentoTesto)  {
		this.riferimentoTesto=riferimentoTesto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [riferimentoNumero]
	 **/
	public java.math.BigDecimal getRiferimentoNumero() {
		return riferimentoNumero;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [riferimentoNumero]
	 **/
	public void setRiferimentoNumero(java.math.BigDecimal riferimentoNumero)  {
		this.riferimentoNumero=riferimentoNumero;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [riferimentodata]
	 **/
	public java.sql.Timestamp getRiferimentodata() {
		return riferimentodata;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [riferimentodata]
	 **/
	public void setRiferimentodata(java.sql.Timestamp riferimentodata)  {
		this.riferimentodata=riferimentodata;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [anomalie]
	 **/
	public java.lang.String getAnomalie() {
		return anomalie;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [anomalie]
	 **/
	public void setAnomalie(java.lang.String anomalie)  {
		this.anomalie=anomalie;
	}
	public java.lang.String getCdBeneServizio() {
		return cdBeneServizio;
	}
	public void setCdBeneServizio(java.lang.String cdBeneServizio) {
		this.cdBeneServizio = cdBeneServizio;
	}	
}