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
 * Date 23/08/2013
 */
package it.cnr.contab.config00.consultazioni.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;

import java.sql.Timestamp;

public class VContrattiTotaliDetBulk extends  OggettoBulk implements Persistent {
//	    TIPO CHAR(3)
		private java.lang.String tipo;
	 
//	    ESERCIZIO_ORIGINALE DECIMAL(4,0)
		private java.lang.Integer esercizioOriginale;
	 
//	    TERZO DECIMAL(8,0)
		private java.lang.Integer terzo;
	 
//	    CD_ELEMENTO_VOCE VARCHAR(20)
		private java.lang.String cdElementoVoce;
	 
//	    ESERCIZIO_CONTRATTO DECIMAL(4,0)
		private java.lang.Integer esercizioContratto;
	 
//	    PG_CONTRATTO DECIMAL(10,0)
		private java.lang.Long pgContratto;
	 
//	    STATO_CONTRATTO CHAR(1)
		private java.lang.String statoContratto;
	 
//	    OGGETTO VARCHAR(500)
		private java.lang.String oggetto;
	 
//	    DATA_INIZIO TIMESTAMP(7)
		private java.sql.Timestamp dataInizio;
	 
//	    DATA_FINE TIMESTAMP(7)
		private java.sql.Timestamp dataFine;
	 
//	    LINEA VARCHAR(10)
		private java.lang.String linea;
	 
//	    CDR VARCHAR(30)
		private java.lang.String cdr;
	 
//	    ESERCIZIO_OBB_ACR DECIMAL(4,0)
		private java.lang.Integer esercizioObbAcr;
	 
//	    PG_OBBLIGAZIONE_ACCERTAMENTO DECIMAL(10,0)
		private java.lang.Long pgObbligazioneAccertamento;
	 
//	    ESERCIZIO_MAN_REV DECIMAL(22,0)
		private java.lang.Long esercizioManRev;
	 
//	    PG_MAN_REV DECIMAL(22,0)
		private java.lang.Long pgManRev;

		private java.sql.Timestamp dt_paginc_manrev;

		//	    ES_DOC_AMM DECIMAL(22,0)
		private java.lang.Long esDocAmm;
	 
//	    PG_DOC_AMM DECIMAL(22,0)
		private java.lang.Long pgDocAmm;
	 
//	    TIPO_DOC VARCHAR(10)
		private java.lang.String tipoDoc;
	 
//	    DESC_VOCE VARCHAR(100)
		private java.lang.String descVoce;
	 
//	    DESC_TERZO VARCHAR(200)
		private java.lang.String descTerzo;
	 
//	    DESC_GAE VARCHAR(300)
		private java.lang.String descGae;
	 
//	    TOTALE_ENTRATE DECIMAL(22,0)
		private java.math.BigDecimal totaleEntrate;
	 
//	    TOTALE_SPESE DECIMAL(22,0)
		private java.math.BigDecimal totaleSpese;
	 
//	    LIQUIDATO_ENTRATE DECIMAL(22,0)
		private java.math.BigDecimal liquidatoEntrate;
	 
//	    LIQUIDATO_SPESE DECIMAL(22,0)
		private java.math.BigDecimal liquidatoSpese;
	 
//	    TOTALE_REVERSALI DECIMAL(22,0)
		private java.math.BigDecimal totaleReversali;
	 
//	    TOTALE_MANDATI DECIMAL(22,0)
		private java.math.BigDecimal totaleMandati;
		
//	    ESERCIZIO_CONTRATTO DECIMAL(4,0)
		private java.lang.Integer esercizioContrattoPadre;
	 
//	    PG_CONTRATTO DECIMAL(10,0)
		private java.lang.Long pgContrattoPadre;
	 
//	    STATO_CONTRATTO CHAR(1)
		private java.lang.String statoContrattoPadre;
		
		private java.lang.String tipoContratto;
		
		private java.lang.String cds;
	 
		private java.math.BigDecimal totaleEntrateContratto;
		
		private java.math.BigDecimal totaleSpeseContratto;
		
		private java.math.BigDecimal totaleMandatiNetto;
		
		private java.math.BigDecimal liquidatoSpeseNetto;

		
		/**
		 * Created by BulkGenerator 2.0 [07/12/2009]
		 * Table name: V_CONTRATTI_TOTALI_DET
		 **/
		public VContrattiTotaliDetBulk() {
			super();
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
		 * Restituisce il valore di: [esercizioOriginale]
		 **/
		public java.lang.Integer getEsercizioOriginale() {
			return esercizioOriginale;
		}
		/**
		 * Created by BulkGenerator 2.0 [07/12/2009]
		 * Setta il valore di: [esercizioOriginale]
		 **/
		public void setEsercizioOriginale(java.lang.Integer esercizioOriginale)  {
			this.esercizioOriginale=esercizioOriginale;
		}
		/**
		 * Created by BulkGenerator 2.0 [07/12/2009]
		 * Restituisce il valore di: [terzo]
		 **/
		public java.lang.Integer getTerzo() {
			return terzo;
		}
		/**
		 * Created by BulkGenerator 2.0 [07/12/2009]
		 * Setta il valore di: [terzo]
		 **/
		public void setTerzo(java.lang.Integer terzo)  {
			this.terzo=terzo;
		}
		/**
		 * Created by BulkGenerator 2.0 [07/12/2009]
		 * Restituisce il valore di: [cdElementoVoce]
		 **/
		public java.lang.String getCdElementoVoce() {
			return cdElementoVoce;
		}
		/**
		 * Created by BulkGenerator 2.0 [07/12/2009]
		 * Setta il valore di: [cdElementoVoce]
		 **/
		public void setCdElementoVoce(java.lang.String cdElementoVoce)  {
			this.cdElementoVoce=cdElementoVoce;
		}
		/**
		 * Created by BulkGenerator 2.0 [07/12/2009]
		 * Restituisce il valore di: [esercizioContratto]
		 **/
		public java.lang.Integer getEsercizioContratto() {
			return esercizioContratto;
		}
		/**
		 * Created by BulkGenerator 2.0 [07/12/2009]
		 * Setta il valore di: [esercizioContratto]
		 **/
		public void setEsercizioContratto(java.lang.Integer esercizioContratto)  {
			this.esercizioContratto=esercizioContratto;
		}
		/**
		 * Created by BulkGenerator 2.0 [07/12/2009]
		 * Restituisce il valore di: [pgContratto]
		 **/
		public java.lang.Long getPgContratto() {
			return pgContratto;
		}
		/**
		 * Created by BulkGenerator 2.0 [07/12/2009]
		 * Setta il valore di: [pgContratto]
		 **/
		public void setPgContratto(java.lang.Long pgContratto)  {
			this.pgContratto=pgContratto;
		}
		/**
		 * Created by BulkGenerator 2.0 [07/12/2009]
		 * Restituisce il valore di: [statoContratto]
		 **/
		public java.lang.String getStatoContratto() {
			return statoContratto;
		}
		/**
		 * Created by BulkGenerator 2.0 [07/12/2009]
		 * Setta il valore di: [statoContratto]
		 **/
		public void setStatoContratto(java.lang.String statoContratto)  {
			this.statoContratto=statoContratto;
		}
		/**
		 * Created by BulkGenerator 2.0 [07/12/2009]
		 * Restituisce il valore di: [oggetto]
		 **/
		public java.lang.String getOggetto() {
			return oggetto;
		}
		/**
		 * Created by BulkGenerator 2.0 [07/12/2009]
		 * Setta il valore di: [oggetto]
		 **/
		public void setOggetto(java.lang.String oggetto)  {
			this.oggetto=oggetto;
		}
		/**
		 * Created by BulkGenerator 2.0 [07/12/2009]
		 * Restituisce il valore di: [dataInizio]
		 **/
		public java.sql.Timestamp getDataInizio() {
			return dataInizio;
		}
		/**
		 * Created by BulkGenerator 2.0 [07/12/2009]
		 * Setta il valore di: [dataInizio]
		 **/
		public void setDataInizio(java.sql.Timestamp dataInizio)  {
			this.dataInizio=dataInizio;
		}
		/**
		 * Created by BulkGenerator 2.0 [07/12/2009]
		 * Restituisce il valore di: [dataFine]
		 **/
		public java.sql.Timestamp getDataFine() {
			return dataFine;
		}
		/**
		 * Created by BulkGenerator 2.0 [07/12/2009]
		 * Setta il valore di: [dataFine]
		 **/
		public void setDataFine(java.sql.Timestamp dataFine)  {
			this.dataFine=dataFine;
		}
		/**
		 * Created by BulkGenerator 2.0 [07/12/2009]
		 * Restituisce il valore di: [linea]
		 **/
		public java.lang.String getLinea() {
			return linea;
		}
		/**
		 * Created by BulkGenerator 2.0 [07/12/2009]
		 * Setta il valore di: [linea]
		 **/
		public void setLinea(java.lang.String linea)  {
			this.linea=linea;
		}
		/**
		 * Created by BulkGenerator 2.0 [07/12/2009]
		 * Restituisce il valore di: [cdr]
		 **/
		public java.lang.String getCdr() {
			return cdr;
		}
		/**
		 * Created by BulkGenerator 2.0 [07/12/2009]
		 * Setta il valore di: [cdr]
		 **/
		public void setCdr(java.lang.String cdr)  {
			this.cdr=cdr;
		}
		/**
		 * Created by BulkGenerator 2.0 [07/12/2009]
		 * Restituisce il valore di: [esercizioObbAcr]
		 **/
		public java.lang.Integer getEsercizioObbAcr() {
			return esercizioObbAcr;
		}
		/**
		 * Created by BulkGenerator 2.0 [07/12/2009]
		 * Setta il valore di: [esercizioObbAcr]
		 **/
		public void setEsercizioObbAcr(java.lang.Integer esercizioObbAcr)  {
			this.esercizioObbAcr=esercizioObbAcr;
		}
		/**
		 * Created by BulkGenerator 2.0 [07/12/2009]
		 * Restituisce il valore di: [pgObbligazioneAccertamento]
		 **/
		public java.lang.Long getPgObbligazioneAccertamento() {
			return pgObbligazioneAccertamento;
		}
		/**
		 * Created by BulkGenerator 2.0 [07/12/2009]
		 * Setta il valore di: [pgObbligazioneAccertamento]
		 **/
		public void setPgObbligazioneAccertamento(java.lang.Long pgObbligazioneAccertamento)  {
			this.pgObbligazioneAccertamento=pgObbligazioneAccertamento;
		}
		/**
		 * Created by BulkGenerator 2.0 [07/12/2009]
		 * Restituisce il valore di: [esercizioManRev]
		 **/
		public java.lang.Long getEsercizioManRev() {
			return esercizioManRev;
		}
		/**
		 * Created by BulkGenerator 2.0 [07/12/2009]
		 * Setta il valore di: [esercizioManRev]
		 **/
		public void setEsercizioManRev(java.lang.Long esercizioManRev)  {
			this.esercizioManRev=esercizioManRev;
		}
		/**
		 * Created by BulkGenerator 2.0 [07/12/2009]
		 * Restituisce il valore di: [pgManRev]
		 **/
		public java.lang.Long getPgManRev() {
			return pgManRev;
		}
		/**
		 * Created by BulkGenerator 2.0 [07/12/2009]
		 * Setta il valore di: [pgManRev]
		 **/
		public void setPgManRev(java.lang.Long pgManRev)  {
			this.pgManRev=pgManRev;
		}

		public Timestamp getDt_paginc_manrev() {
			return dt_paginc_manrev;
		}

		public void setDt_paginc_manrev(Timestamp dt_paginc_manrev) {
			this.dt_paginc_manrev = dt_paginc_manrev;
		}

	/**
		 * Created by BulkGenerator 2.0 [07/12/2009]
		 * Restituisce il valore di: [esDocAmm]
		 **/
		public java.lang.Long getEsDocAmm() {
			return esDocAmm;
		}
		/**
		 * Created by BulkGenerator 2.0 [07/12/2009]
		 * Setta il valore di: [esDocAmm]
		 **/
		public void setEsDocAmm(java.lang.Long esDocAmm)  {
			this.esDocAmm=esDocAmm;
		}
		/**
		 * Created by BulkGenerator 2.0 [07/12/2009]
		 * Restituisce il valore di: [pgDocAmm]
		 **/
		public java.lang.Long getPgDocAmm() {
			return pgDocAmm;
		}
		/**
		 * Created by BulkGenerator 2.0 [07/12/2009]
		 * Setta il valore di: [pgDocAmm]
		 **/
		public void setPgDocAmm(java.lang.Long pgDocAmm)  {
			this.pgDocAmm=pgDocAmm;
		}
		/**
		 * Created by BulkGenerator 2.0 [07/12/2009]
		 * Restituisce il valore di: [tipoDoc]
		 **/
		public java.lang.String getTipoDoc() {
			return tipoDoc;
		}
		/**
		 * Created by BulkGenerator 2.0 [07/12/2009]
		 * Setta il valore di: [tipoDoc]
		 **/
		public void setTipoDoc(java.lang.String tipoDoc)  {
			this.tipoDoc=tipoDoc;
		}
		/**
		 * Created by BulkGenerator 2.0 [07/12/2009]
		 * Restituisce il valore di: [descVoce]
		 **/
		public java.lang.String getDescVoce() {
			return descVoce;
		}
		/**
		 * Created by BulkGenerator 2.0 [07/12/2009]
		 * Setta il valore di: [descVoce]
		 **/
		public void setDescVoce(java.lang.String descVoce)  {
			this.descVoce=descVoce;
		}
		/**
		 * Created by BulkGenerator 2.0 [07/12/2009]
		 * Restituisce il valore di: [descTerzo]
		 **/
		public java.lang.String getDescTerzo() {
			return descTerzo;
		}
		/**
		 * Created by BulkGenerator 2.0 [07/12/2009]
		 * Setta il valore di: [descTerzo]
		 **/
		public void setDescTerzo(java.lang.String descTerzo)  {
			this.descTerzo=descTerzo;
		}
		/**
		 * Created by BulkGenerator 2.0 [07/12/2009]
		 * Restituisce il valore di: [descGae]
		 **/
		public java.lang.String getDescGae() {
			return descGae;
		}
		/**
		 * Created by BulkGenerator 2.0 [07/12/2009]
		 * Setta il valore di: [descGae]
		 **/
		public void setDescGae(java.lang.String descGae)  {
			this.descGae=descGae;
		}

		public java.math.BigDecimal getTotaleEntrate() {
			return totaleEntrate;
		}

		public void setTotaleEntrate(java.math.BigDecimal totaleEntrate) {
			this.totaleEntrate = totaleEntrate;
		}

		public java.math.BigDecimal getTotaleSpese() {
			return totaleSpese;
		}

		public void setTotaleSpese(java.math.BigDecimal totaleSpese) {
			this.totaleSpese = totaleSpese;
		}

		public java.math.BigDecimal getLiquidatoEntrate() {
			return liquidatoEntrate;
		}

		public void setLiquidatoEntrate(java.math.BigDecimal liquidatoEntrate) {
			this.liquidatoEntrate = liquidatoEntrate;
		}

		public java.math.BigDecimal getLiquidatoSpese() {
			return liquidatoSpese;
		}

		public void setLiquidatoSpese(java.math.BigDecimal liquidatoSpese) {
			this.liquidatoSpese = liquidatoSpese;
		}

		public java.math.BigDecimal getTotaleReversali() {
			return totaleReversali;
		}

		public void setTotaleReversali(java.math.BigDecimal totaleReversali) {
			this.totaleReversali = totaleReversali;
		}

		public java.math.BigDecimal getTotaleMandati() {
			return totaleMandati;
		}

		public void setTotaleMandati(java.math.BigDecimal totaleMandati) {
			this.totaleMandati = totaleMandati;
		}

		public java.lang.Integer getEsercizioContrattoPadre() {
			return esercizioContrattoPadre;
		}

		public void setEsercizioContrattoPadre(java.lang.Integer esercizioContrattoPadre) {
			this.esercizioContrattoPadre = esercizioContrattoPadre;
		}

		public java.lang.Long getPgContrattoPadre() {
			return pgContrattoPadre;
		}

		public void setPgContrattoPadre(java.lang.Long pgContrattoPadre) {
			this.pgContrattoPadre = pgContrattoPadre;
		}

		public java.lang.String getStatoContrattoPadre() {
			return statoContrattoPadre;
		}

		public void setStatoContrattoPadre(java.lang.String statoContrattoPadre) {
			this.statoContrattoPadre = statoContrattoPadre;
		}

		public java.lang.String getTipoContratto() {
			return tipoContratto;
		}

		public void setTipoContratto(java.lang.String tipoContratto) {
			this.tipoContratto = tipoContratto;
		}

		public java.lang.String getCds() {
			return cds;
		}

		public void setCds(java.lang.String cds) {
			this.cds = cds;
		}

		public java.math.BigDecimal getTotaleEntrateContratto() {
			return totaleEntrateContratto;
		}

		public void setTotaleEntrateContratto(
				java.math.BigDecimal totaleEntrateContratto) {
			this.totaleEntrateContratto = totaleEntrateContratto;
		}

		public java.math.BigDecimal getTotaleSpeseContratto() {
			return totaleSpeseContratto;
		}

		public void setTotaleSpeseContratto(java.math.BigDecimal totaleSpeseContratto) {
			this.totaleSpeseContratto = totaleSpeseContratto;
		}

		public java.math.BigDecimal getTotaleMandatiNetto() {
			return totaleMandatiNetto;
		}

		public void setTotaleMandatiNetto(java.math.BigDecimal totaleMandatiNetto) {
			this.totaleMandatiNetto = totaleMandatiNetto;
		}

		public java.math.BigDecimal getLiquidatoSpeseNetto() {
			return liquidatoSpeseNetto;
		}

		public void setLiquidatoSpeseNetto(java.math.BigDecimal liquidatoSpeseNetto) {
			this.liquidatoSpeseNetto = liquidatoSpeseNetto;
		}
		
		
	}
