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
 * Date 12/09/2016
 */
package it.cnr.contab.docamm00.consultazioni.bulk;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.persistency.PersistentCache;

public class VDocAttiviDaRiscontrareBulk extends OggettoBulk implements Persistent {
	
		private java.lang.Integer cd_terzo;

		private java.lang.String cognome;
		
		private java.lang.String ragione_sociale;

		private java.lang.String nome;

		private java.sql.Timestamp dt_emissione;

		private java.math.BigDecimal im_totale_doc_amm;

		private java.math.BigDecimal da_riscontrare;
		
		private java.lang.Long progressivo_documento;
		
		private java.lang.Long protocollo_iva;

		private java.lang.String tipo;

		private java.lang.String cd_unita_organizzativa;

		private java.lang.String cd_modalita_pag;

		private java.lang.String cd_uo_origine;

		private java.lang.Integer esercizio;
		public VDocAttiviDaRiscontrareBulk() {
			super();
		}
		public java.lang.Integer getCd_terzo() {
			return cd_terzo;
		}
		public void setCd_terzo(java.lang.Integer cd_terzo) {
			this.cd_terzo = cd_terzo;
		}
		public java.lang.String getCognome() {
			return cognome;
		}
		public void setCognome(java.lang.String cognome) {
			this.cognome = cognome;
		}
		public java.lang.String getRagione_sociale() {
			return ragione_sociale;
		}
		public void setRagione_sociale(java.lang.String ragione_sociale) {
			this.ragione_sociale = ragione_sociale;
		}
		public java.lang.String getNome() {
			return nome;
		}
		public void setNome(java.lang.String nome) {
			this.nome = nome;
		}
		public java.sql.Timestamp getDt_emissione() {
			return dt_emissione;
		}
		public void setDt_emissione(java.sql.Timestamp dt_emissione) {
			this.dt_emissione = dt_emissione;
		}
		public java.math.BigDecimal getIm_totale_doc_amm() {
			return im_totale_doc_amm;
		}
		public void setIm_totale_doc_amm(java.math.BigDecimal im_totale_doc_amm) {
			this.im_totale_doc_amm = im_totale_doc_amm;
		}
		public java.math.BigDecimal getDa_riscontrare() {
			return da_riscontrare;
		}
		public void setDa_riscontrare(java.math.BigDecimal da_riscontrare) {
			this.da_riscontrare = da_riscontrare;
		}
		public java.lang.Long getProgressivo_documento() {
			return progressivo_documento;
		}
		public void setProgressivo_documento(java.lang.Long progressivo_documento) {
			this.progressivo_documento = progressivo_documento;
		}
		public java.lang.Long getProtocollo_iva() {
			return protocollo_iva;
		}
		public void setProtocollo_iva(java.lang.Long protocollo_iva) {
			this.protocollo_iva = protocollo_iva;
		}
		public java.lang.String getTipo() {
			return tipo;
		}
		public void setTipo(java.lang.String tipo) {
			this.tipo = tipo;
		}
		public java.lang.String getCd_unita_organizzativa() {
			return cd_unita_organizzativa;
		}
		public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
			this.cd_unita_organizzativa = cd_unita_organizzativa;
		}
		public java.lang.String getCd_modalita_pag() {
			return cd_modalita_pag;
		}
		public void setCd_modalita_pag(java.lang.String cd_modalita_pag) {
			this.cd_modalita_pag = cd_modalita_pag;
		}
		public java.lang.String getCd_uo_origine() {
			return cd_uo_origine;
		}
		public void setCd_uo_origine(java.lang.String cd_uo_origine) {
			this.cd_uo_origine = cd_uo_origine;
		}
		public java.lang.Integer getEsercizio() {
			return esercizio;
		}
		public void setEsercizio(java.lang.Integer esercizio) {
			this.esercizio = esercizio;
		}	
}
