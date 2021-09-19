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
 * Date 02/10/2018
 */
package it.cnr.contab.doccont00.intcass.giornaliera;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
import it.cnr.contab.doccont00.intcass.xmlbnl.*;
public class MovimentoContoEvidenzaBulk extends MovimentoContoEvidenzaBase {
	/**
	 * [INFORMAZIONI_CONTO_EVIDENZA null]
	 **/

	public final static String RIFERIMENTO_INTERNO_BANCA_ITALIA = "FZPBP1";
	public final static String RIFERIMENTO_INTERNO_PAGAMENTO_STIPENDI = "PAGSTIP";
	public final static String STATO_RECORD_INIZIALE = "I";
	public final static String STATO_RECORD_PROCESSATO = "P";
	public final static String TIPO_MOVIMENTO_ENTRATA = "ENTRATA";
	public final static String TIPO_DOCUMENTO_MANDATO = "MANDATO";
	public final static String INIZIO_TIPO_DOCUMENTO_SOSPESO = "SOSPESO";
	public final static String TIPO_DOCUMENTO_REVERSALE = "REVERSALE";
	public final static String TIPO_OPERAZIONE_ESEGUITO = "ESEGUITO";
	public final static String TIPO_OPERAZIONE_REGOLARIZZATO = "REGOLARIZZATO";
	public final static String TIPO_OPERAZIONE_STORNATO = "STORNATO";
	public final static String TIPO_ESECUZIONE_BANCA_ITALIA = "ACCREDITO BANCA D'ITALIA";
	public final static String TIPO_ESECUZIONE_TESORERIA_TAB_A = "ACCREDITO TESORERIA PROVINCIALE STATO PER TAB A";
	public final static String TIPO_ESECUZIONE_TESORERIA_TAB_B = "ACCREDITO TESORERIA PROVINCIALE STATO PER TAB B";
	public final static String TIPO_ESECUZIONE_REG_BANCA_ITALIA = "REGOLARIZZAZIONE ACCREDITO BANCA D'ITALIA";
	public final static String TIPO_ESECUZIONE_REG_TESORERIA_TAB_A = "REGOLARIZZAZIONE ACCREDITO TESORERIA PROVINCIALE STATO PER TAB A";
	public final static String TIPO_ESECUZIONE_REG_TESORERIA_TAB_B = "REGOLARIZZAZIONE ACCREDITO TESORERIA PROVINCIALE STATO PER TAB B";

	private InformazioniContoEvidenzaBulk informazioniContoEvidenza =  new InformazioniContoEvidenzaBulk();
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: MOVIMENTI_CONTO_EVIDENZA
	 **/
	public MovimentoContoEvidenzaBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: MOVIMENTI_CONTO_EVIDENZA
	 **/
	public MovimentoContoEvidenzaBulk(java.lang.Integer esercizio, java.lang.String identificativoFlusso, java.lang.String contoEvidenza, java.lang.String stato, java.lang.Long progressivo) {
		super(esercizio, identificativoFlusso, contoEvidenza, stato, progressivo);
		setInformazioniContoEvidenza( new InformazioniContoEvidenzaBulk(esercizio,identificativoFlusso,contoEvidenza) );
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [null]
	 **/
	public InformazioniContoEvidenzaBulk getInformazioniContoEvidenza() {
		return informazioniContoEvidenza;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [null]
	 **/
	public void setInformazioniContoEvidenza(InformazioniContoEvidenzaBulk informazioniContoEvidenza)  {
		this.informazioniContoEvidenza=informazioniContoEvidenza;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizio]
	 **/
	public java.lang.Integer getEsercizio() {
		InformazioniContoEvidenzaBulk informazioniContoEvidenza = this.getInformazioniContoEvidenza();
		if (informazioniContoEvidenza == null)
			return null;
		return getInformazioniContoEvidenza().getEsercizio();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizio]
	 **/
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.getInformazioniContoEvidenza().setEsercizio(esercizio);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [identificativoFlusso]
	 **/
	public java.lang.String getIdentificativoFlusso() {
		InformazioniContoEvidenzaBulk informazioniContoEvidenza = this.getInformazioniContoEvidenza();
		if (informazioniContoEvidenza == null)
			return null;
		return getInformazioniContoEvidenza().getIdentificativoFlusso();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [identificativoFlusso]
	 **/
	public void setIdentificativoFlusso(java.lang.String identificativoFlusso)  {
		this.getInformazioniContoEvidenza().setIdentificativoFlusso(identificativoFlusso);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [contoEvidenza]
	 **/
	public java.lang.String getContoEvidenza() {
		InformazioniContoEvidenzaBulk informazioniContoEvidenza = this.getInformazioniContoEvidenza();
		if (informazioniContoEvidenza == null)
			return null;
		return getInformazioniContoEvidenza().getContoEvidenza();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [contoEvidenza]
	 **/
	public void setContoEvidenza(java.lang.String contoEvidenza)  {
		this.getInformazioniContoEvidenza().setContoEvidenza(contoEvidenza);
	}
	public Boolean isMandatoReversale(){
		return getTipoDocumento() != null && (isMandato() || isReversale());
	}
	public Boolean isMandato(){
		return getTipoDocumento() != null && getTipoDocumento().equals(TIPO_DOCUMENTO_MANDATO);
	}
	public Boolean isReversale(){
		return getTipoDocumento() != null && getTipoDocumento().equals(TIPO_DOCUMENTO_REVERSALE);
	}
	public Boolean isTipoOperazioneEseguitoRegolarizzato(){
		return getTipoOperazione() != null && (isTipoOperazioneEseguito() || isTipoOperazioneRegolarizzato());
	}
	public Boolean isTipoOperazioneEseguito(){
		return getTipoOperazione() != null  && getTipoDocumento().equals(TIPO_OPERAZIONE_ESEGUITO);
	}
	public Boolean isTipoOperazioneRegolarizzato(){
		return getTipoOperazione() != null  && getTipoDocumento().equals(TIPO_OPERAZIONE_REGOLARIZZATO);
	}
	public Boolean isTipoOperazioneStornato(){
		return getTipoOperazione() != null  && getTipoDocumento().equals(TIPO_OPERAZIONE_STORNATO);
	}
	public Boolean isTipoEsecuzioneBancaItalia(){
		return getTipoEsecuzione() != null  && (getTipoEsecuzione().equals(TIPO_ESECUZIONE_BANCA_ITALIA) ||getTipoEsecuzione().equals(TIPO_ESECUZIONE_TESORERIA_TAB_A) ||getTipoEsecuzione().equals(TIPO_ESECUZIONE_TESORERIA_TAB_B)
				||getTipoEsecuzione().equals(TIPO_ESECUZIONE_REG_BANCA_ITALIA) ||getTipoEsecuzione().equals(TIPO_ESECUZIONE_REG_TESORERIA_TAB_A) ||getTipoEsecuzione().equals(TIPO_ESECUZIONE_REG_TESORERIA_TAB_B));
	}
	public Boolean isSospeso(){
		return getTipoDocumento() != null && getTipoDocumento().startsWith(INIZIO_TIPO_DOCUMENTO_SOSPESO);
	}
	public Boolean isMovimentoEntrata(){
		return getTipoMovimento() != null && getTipoMovimento().equals(TIPO_MOVIMENTO_ENTRATA);
	}
	public Boolean isCodiceRiferimentoInternoBancaItalia(){
		return getCodiceRifInterno() != null && getCodiceRifInterno().startsWith(RIFERIMENTO_INTERNO_BANCA_ITALIA);
	}
	public Boolean isCodiceRiferimentoInternoPagamentoStipendi(){
		return getCodiceRifInterno() != null && getCodiceRifInterno().startsWith(RIFERIMENTO_INTERNO_PAGAMENTO_STIPENDI);
	}
}