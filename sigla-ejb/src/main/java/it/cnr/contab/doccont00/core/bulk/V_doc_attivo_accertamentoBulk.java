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

package it.cnr.contab.doccont00.core.bulk;


import java.math.BigDecimal;
import java.util.*;

import it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk;
import it.cnr.contab.docamm00.docs.bulk.Numerazione_doc_ammBulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class V_doc_attivo_accertamentoBulk extends V_doc_attivo_accertamentoBase {
	static private java.util.Dictionary classeDiPagamentoKeys;

	/* tipi utilizzati per decodificare tutti i tipi di documenti amministrativi associati ad una reversale */	
	private java.util.Hashtable tipoDocumentoKeys;
	static private java.util.Hashtable fatturaPassivaKeys;
	static private java.util.Hashtable fatturaAttivaKeys;		
	
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

	
	static private java.util.Hashtable fl_pgiro_Keys;

	protected it.cnr.contab.anagraf00.core.bulk.V_all_anagrafico_terzoBulk terzoAnag = new it.cnr.contab.anagraf00.core.bulk.V_all_anagrafico_terzoBulk();
	
public V_doc_attivo_accertamentoBulk() {
	super();
}
public boolean equals(Object obj) 
{
	return (obj != null &&
	      obj instanceof V_doc_attivo_accertamentoBulk &&
			this.getEsercizio_accertamento() != null &&
			this.getEsercizio_ori_accertamento() != null &&
			this.getPg_accertamento() != null &&
			this.getPg_accertamento_scadenzario() != null &&
			(this.getEsercizio_ori_accertamento().compareTo(((V_doc_attivo_accertamentoBulk)obj).getEsercizio_ori_accertamento()) == 0) &&
			(this.getPg_accertamento().compareTo(((V_doc_attivo_accertamentoBulk)obj).getPg_accertamento()) == 0) &&			
			(this.getEsercizio_accertamento().compareTo(((V_doc_attivo_accertamentoBulk)obj).getEsercizio_accertamento()) == 0) &&
			(this.getPg_accertamento_scadenzario().compareTo(((V_doc_attivo_accertamentoBulk)obj).getPg_accertamento_scadenzario())) == 0); 	

}
public String getCd_precedente() {
	it.cnr.contab.anagraf00.core.bulk.V_all_anagrafico_terzoBulk at = this.getTerzoAnag();
	if (at == null)
		return null;
	return at.getCd_precedente();
}
public Integer getCd_terzo() {
	it.cnr.contab.anagraf00.core.bulk.V_all_anagrafico_terzoBulk at = this.getTerzoAnag();
	if (at == null)
		return null;
	return at.getCd_terzo();
}
/**
 * @return java.util.Dictionary
 */
public java.util.Dictionary getClasseDiPagamentoKeys() {
	return it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk.TI_PAGAMENTO_KEYS;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'codice_fiscale'
 *
 * @return Il valore della proprietà 'codice_fiscale'
 */
public java.lang.String getCodice_fiscale() {
	it.cnr.contab.anagraf00.core.bulk.V_all_anagrafico_terzoBulk at = this.getTerzoAnag();
	if (at == null)
		return null;
	return at.getCodice_fiscale();
}
public java.lang.String getCognome() {
	it.cnr.contab.anagraf00.core.bulk.V_all_anagrafico_terzoBulk at = this.getTerzoAnag();
	if (at == null)
		return null;
	return at.getCognome();
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'ds_tipo_documento_amm'
 *
 * @return Il valore della proprietà 'ds_tipo_documento_amm'
 */
public java.lang.String getDs_tipo_documento_amm() 
{
	if ( Numerazione_doc_ammBulk.TIPO_FATTURA_PASSIVA.equals( getCd_tipo_documento_amm()))
		return (String) getFatturaPassivaKeys().get( getTi_fattura() );
	else if ( Numerazione_doc_ammBulk.TIPO_FATTURA_ATTIVA.equals( getCd_tipo_documento_amm()))	
		return (String) getFatturaAttivaKeys().get( getTi_fattura() );
	return (String) getTipoDocumentoKeys().get( getCd_tipo_documento_amm() );
}
/**
 * @return java.util.Hashtable
 */
public static java.util.Hashtable getFatturaAttivaKeys() {
	return fatturaAttivaKeys;
}
/**
 * @return java.util.Hashtable
 */
public static java.util.Hashtable getFatturaPassivaKeys() {
	return fatturaPassivaKeys;
}
/**
 * Metodo con cui si ottiene il valore della variabile <code>fl_pgiro_Keys</code>
 * di tipo <code>Hashtable</code>.
 * In particolare, questo metodo carica in una Hashtable l'elenco dei possibili valori 
 * che può assumere il flag <code>fl_pgiro</code>.
 * @return java.util.Hashtable fl_pgiro_Keys I valori del flag <code>fl_pgiro</code>.
 */
public java.util.Hashtable getFl_pgiro_Keys() {
		Hashtable fl_pgiro_Keys = new Hashtable();
		
		fl_pgiro_Keys.put(new Boolean(true), "Y");
		fl_pgiro_Keys.put(new Boolean(false), "N");
	return fl_pgiro_Keys;
}
public java.lang.String getNome() {
	it.cnr.contab.anagraf00.core.bulk.V_all_anagrafico_terzoBulk at = this.getTerzoAnag();
	if (at == null)
		return null;
	return at.getNome();
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'partita_iva'
 *
 * @return Il valore della proprietà 'partita_iva'
 */
public java.lang.String getPartita_iva() {
	it.cnr.contab.anagraf00.core.bulk.V_all_anagrafico_terzoBulk at = this.getTerzoAnag();
	if (at == null)
		return null;
	return at.getPartita_iva();
}
public java.lang.String getRagione_sociale() {
	it.cnr.contab.anagraf00.core.bulk.V_all_anagrafico_terzoBulk at = this.getTerzoAnag();
	if (at == null)
		return null;
	return at.getRagione_sociale();
}
/**
 * @return it.cnr.contab.anagraf00.core.bulk.V_all_anagrafico_terzoBulk
 */
public it.cnr.contab.anagraf00.core.bulk.V_all_anagrafico_terzoBulk getTerzoAnag() {
	return terzoAnag;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'ti_competenza_residuo'
 *
 * @return Il valore della proprietà 'ti_competenza_residuo'
 */
public String getTi_competenza_residuo() {
	if ( Numerazione_doc_contBulk.TIPO_ACR_RES.equals( getCd_tipo_documento_cont()) )
		return ReversaleBulk.TIPO_RESIDUO;
	else
		return ReversaleBulk.TIPO_COMPETENZA;
}
/**
 * @return java.util.Hashtable
 */
public java.util.Hashtable getTipoDocumentoKeys() {
	return tipoDocumentoKeys;
}
public void setCd_precedente( String cd_precedente) {
	this.getTerzoAnag().setCd_precedente( cd_precedente );
}
public void setCd_terzo( Integer cd_terzo) {
	this.getTerzoAnag().setCd_terzo( cd_terzo );
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'codice_fiscale'
 *
 * @param str	Il valore da assegnare a 'codice_fiscale'
 */
public void setCodice_fiscale( String str) {
	this.getTerzoAnag().setCodice_fiscale( str );
}
public void setCognome( String str) {
	this.getTerzoAnag().setCognome( str );
}
public void setNome( String str) {
	this.getTerzoAnag().setNome( str );
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'partita_iva'
 *
 * @param str	Il valore da assegnare a 'partita_iva'
 */
public void setPartita_iva( String str) {
	this.getTerzoAnag().setPartita_iva( str );
}
public void setRagione_sociale( String str) {
	this.getTerzoAnag().setRagione_sociale( str );
}
/**
 * @param newTerzoAnag it.cnr.contab.anagraf00.core.bulk.V_all_anagrafico_terzoBulk
 */
public void setTerzoAnag(it.cnr.contab.anagraf00.core.bulk.V_all_anagrafico_terzoBulk newTerzoAnag) {
	terzoAnag = newTerzoAnag;
}
/**
 * @param newTipoDocumentoKeys java.util.Hashtable
 */
public void setTipoDocumentoKeys(java.util.Hashtable newTipoDocumentoKeys) {
	tipoDocumentoKeys = newTipoDocumentoKeys;
}
/* Alcuni casi particolari:
1 - partite di giro nel mondo CNR vanno a consumo (ACR o ACR_RES con fl_pgiro = Y) --> im_riga del doc amm
2 - impegni nel mondo CNR vanno a consumo (ACR o ACR_RES con fl_pgiro = N) --> im_riga del doc amm
3 - lettera di pagmento estero --> im_scadenza
4 - nota di credito/nota di debito: 0 e fattura collegata: im_scadenza
*/ 
public BigDecimal getIm_reversale_riga() 
{
	/* a consumo: accert. e accert. residui sia su partite di giro che su capitoli di bilancio */
	if (  Numerazione_doc_contBulk.TIPO_ACR.equals( getCd_tipo_documento_cont()) ||
			Numerazione_doc_contBulk.TIPO_ACR_RES.equals( getCd_tipo_documento_cont()))
		return getIm_totale_doc_amm();  //documenti che vanno a consumo della scadenza dell'accert.
	else if ( Numerazione_doc_ammBulk.TIPO_FATTURA_ATTIVA.equals( getCd_tipo_documento_amm()) &&
		      ( Fattura_attivaBulk.TIPO_NOTA_DI_CREDITO.equals( getTi_fattura()) ||
		        Fattura_attivaBulk.TIPO_NOTA_DI_DEBITO.equals( getTi_fattura())))
		 return new BigDecimal(0);
	else	 
		return getIm_scadenza();	//documenti che vanno a quadratura con la scadenza dell'accert.
}
	public boolean isCompetenza()
	{
		return getCd_tipo_documento_cont().equals( Numerazione_doc_contBulk.TIPO_ACR ) ||
				getCd_tipo_documento_cont().equals( Numerazione_doc_contBulk.TIPO_ACR_PGIRO );
	}
}
