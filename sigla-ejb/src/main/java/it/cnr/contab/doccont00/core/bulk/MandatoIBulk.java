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

import java.math.*;
import it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.core.bulk.Modalita_pagamentoBulk;
import it.cnr.contab.anagraf00.core.bulk.BancaBulk;
import java.util.*;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk;
import it.cnr.contab.docamm00.docs.bulk.Documento_genericoBulk;
import it.cnr.contab.preventvar00.bulk.Var_bilancioBulk;
import it.cnr.contab.util.enumeration.StatoVariazioneSostituzione;
import it.cnr.jada.bulk.*;

public class MandatoIBulk extends MandatoBulk {
	protected Collection docPassiviColl = new ArrayList();
	protected V_doc_passivo_obbligazioneBulk find_doc_passivi = new V_doc_passivo_obbligazioneBulk();
	protected List tipoBolloOptions;
	protected AccertamentoBulk accertamentoPerRegolarizzazione;
	protected Collection scadenzeAccertamentoPerRegolarizzazione;
	protected Collection scadenzeAccertamentoSelezionatePerRegolarizzazione;
	protected Collection docGenericiPerRegolarizzazione;
	protected Collection docGenericiSelezionatiPerRegolarizzazione;		
	protected List sospesiDa1210List = new ArrayList();
	protected boolean generaReversaleDaDocAmm;
	protected Var_bilancioBulk var_bilancio;
	private boolean erroreEsitaVariazioneBilancio = false;

	/*impostato a true solo quando è necessario generare la reversale di incasso IVA:
	  fatture passive istituzionali di beni intra ue o san marino */
	protected boolean faiReversale = false;	
public MandatoIBulk() {
	super();
}
public MandatoIBulk(String cd_cds, Integer esercizio, Long pg_mandato) {
	super(cd_cds, esercizio, pg_mandato);
}
/**
 * Aggiunge un nuovo dettaglio (Mandato_rigaBulk) alla lista di dettagli definiti per il mandato
 * inizializzandone alcuni campi
 * @param mr dettaglio da aggiungere alla lista
 * @return int
 */

public int addToMandato_rigaColl( Mandato_rigaBulk riga, V_doc_passivo_obbligazioneBulk docPassivo ) 
{
	mandato_rigaColl.add(riga);
	docPassiviColl.remove( docPassivo);
//	mandato_rigaTable. ???
	return mandato_rigaColl.size()-1;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'accertamentoPerRegolarizzazione'
 *
 * @return Il valore della proprietà 'accertamentoPerRegolarizzazione'
 */
public AccertamentoBulk getAccertamentoPerRegolarizzazione() {
	return accertamentoPerRegolarizzazione;
}
/**
 * Insert the method's description here.
 * Creation date: (12/11/2002 10.19.20)
 * @return java.util.Collection
 */
public java.util.Collection getDocGenericiPerRegolarizzazione() {
	return docGenericiPerRegolarizzazione;
}
/**
 * Insert the method's description here.
 * Creation date: (12/11/2002 10.19.20)
 * @return java.util.Collection
 */
public java.util.Collection getDocGenericiSelezionatiPerRegolarizzazione() {
	return docGenericiSelezionatiPerRegolarizzazione;
}
/**
 * @return java.util.Collection
 */
public java.util.Collection getDocPassiviColl() {
	return docPassiviColl;
}
/**
 * @return it.cnr.contab.doccont00.core.bulk.V_doc_passivo_obbligazioneBulk
 */
public V_doc_passivo_obbligazioneBulk getFind_doc_passivi() {
	return find_doc_passivi;
}
public BigDecimal getImReversaleDiIncassoIVA()
{
	BigDecimal importo = new java.math.BigDecimal(0);

	Mandato_rigaIBulk riga;
	for ( Iterator i = getMandato_rigaColl().iterator() ;i.hasNext() ;) 
	{
		riga = (Mandato_rigaIBulk) i.next();
		
		if ( it.cnr.contab.docamm00.docs.bulk.Numerazione_doc_ammBulk.TIPO_FATTURA_PASSIVA.equals( riga.getCd_tipo_documento_amm())
			  && riga.getIm_ritenute_riga().compareTo( new BigDecimal(0)) > 0 )
			  importo = importo.add( riga.getIm_ritenute_riga());
	}
	return importo;

}
/**
 * Insert the method's description here.
 * Creation date: (10/09/2002 15.26.28)
 * @return java.util.List
 */
public java.util.List getSospesiDa1210List() {
	return sospesiDa1210List;
}
/**
 * @return java.util.List
 */
public java.util.List getTipoBolloOptions() {
	return tipoBolloOptions;
}
public boolean hasFattura_passiva()
{
	
	Mandato_rigaIBulk riga;
	for ( Iterator i = getMandato_rigaColl().iterator() ;i.hasNext() ;) 
	{
		riga = (Mandato_rigaIBulk) i.next();
		
		if ( it.cnr.contab.docamm00.docs.bulk.Numerazione_doc_ammBulk.TIPO_FATTURA_PASSIVA.equals( riga.getCd_tipo_documento_amm()))
			return true;
	}
	return false;

}
/**
 * verifica se il documento passivo passato come parametro e' incluso nel mandato
 * @param V_doc_passivo_obbligazioneBulk documento passivo
 * @return true se il documento passivo e' incluso nel mandato, false altrimenti
 */
public boolean isDocPassivoIncluso( V_doc_passivo_obbligazioneBulk docPassivo ) 
{
	Mandato_rigaIBulk riga;
	for ( Iterator i = getMandato_rigaColl().iterator(); i.hasNext(); )
	{
		riga = (Mandato_rigaIBulk) i.next();
		if ( riga.getEsercizio_obbligazione().compareTo( docPassivo.getEsercizio_obbligazione()) == 0 &&
			  riga.getPg_obbligazione().compareTo( docPassivo.getPg_obbligazione()) == 0 &&
			  riga.getPg_obbligazione_scadenzario().compareTo( docPassivo.getPg_obbligazione_scadenzario()) == 0 &&
			  riga.getCd_cds_doc_amm().equals( docPassivo.getCd_cds())  &&
			  riga.getCd_uo_doc_amm().equals( docPassivo.getCd_unita_organizzativa())  &&
			  riga.getEsercizio_doc_amm().compareTo( docPassivo.getEsercizio()) == 0 &&
			  riga.getCd_tipo_documento_amm().equals( docPassivo.getCd_tipo_documento_amm())  &&			  
			  riga.getPg_doc_amm().compareTo( docPassivo.getPg_documento_amm()) == 0 )
			return true;	
	}	
	return false;
}
/**
 * Insert the method's description here.
 * Creation date: (31/03/2003 14.42.24)
 * @return boolean
 */
public boolean isFaiReversale() {
	return faiReversale;
}
/**
 * Insert the method's description here.
 * Creation date: (12/11/2002 10.26.09)
 * @return boolean
 */
public boolean isGeneraReversaleDaDocAmm() {
	return generaReversaleDaDocAmm;
}
/**
 * Verifica se il mandato è di tipo accreditamento.
 * @return 			FALSE 	Il mandato non è di tipo accreditamento
 */
public boolean isMandatoAccreditamentoBulk() 
{
	return false;
}
/**
 * Read-only property del searchtool Find_accertamento
 *
 * @return true se il searchtool va diabilitato, false altrimenti
 */
public boolean isROFind_accertamento() {
	/*
	if ( !getMandato_rigaColl().isEmpty() && 
		!((Mandato_rigaBulk) getMandato_rigaColl().get(0)).getFl_pgiro().booleanValue())
		return false;
	return true;
	*/
	return getMandato_rigaColl().isEmpty();

}
/**
 * Read-only property del searchtool Find_doc_generico
 *
 * @return true se il searchtool va diabilitato, false altrimenti
 */
public boolean isROFind_doc_generico() {
	/*
	if ( !getMandato_rigaColl().isEmpty() && 
		!((Mandato_rigaBulk) getMandato_rigaColl().get(0)).getFl_pgiro().booleanValue())
		return false;
	return true;
	*/
	return getMandato_rigaColl().isEmpty();

}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'rOFind_terzoAnag'
 *
 * @return Il valore della proprietà 'rOFind_terzoAnag'
 */
public boolean isROFind_terzoAnag() {
	return getFind_doc_passivi().getTerzoAnag().getCrudStatus() == this.NORMAL;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'rOPg_accertamento'
 *
 * @return Il valore della proprietà 'rOPg_accertamento'
 */
public boolean isROPg_accertamento() 
{
	return getAccertamentoPerRegolarizzazione() != null &&
	       getAccertamentoPerRegolarizzazione().getCrudStatus() != UNDEFINED;	
}
/**
 * Metodo per l'eliminazione di un elemento <code>Mandato_rigaBulk</code> dalla <code>Collection</code>
 * delle righe del mandato.
 * @param index L'indice per scorrere la collezione di righe del mandato.
 * @return Mandato_rigaBulk La riga da rimuovere.
 */
public Mandato_rigaBulk removeFromMandato_rigaColl(int index) 
{
	Mandato_rigaIBulk riga = (Mandato_rigaIBulk) super.removeFromMandato_rigaColl( index );
	if (Optional.ofNullable(riga.getMandatoI())
			.flatMap(mandatoIBulk -> Optional.ofNullable(mandatoIBulk.getStatoVarSos()))
			.map(s -> !s.equals(StatoVariazioneSostituzione.DA_VARIARE.value()))
			.orElse(Boolean.TRUE)) {
		if ( riga.getCd_sospeso() != null )
			getSospesiDa1210List().remove( riga.getCd_sospeso());
		if(riga.getMandato_siopeColl()!=null && !riga.getMandato_siopeColl().isEmpty())
			riga.setMandato_siopeColl(null);
		if(riga.getMandatoCupColl()!=null && !riga.getMandatoCupColl().isEmpty())
			riga.setMandatoCupColl(null);
	}
	return riga;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'accertamentoPerRegolarizzazione'
 *
 * @param newAccertamentoPerRegolarizzazione	Il valore da assegnare a 'accertamentoPerRegolarizzazione'
 */
public void setAccertamentoPerRegolarizzazione(AccertamentoBulk newAccertamentoPerRegolarizzazione) {
	accertamentoPerRegolarizzazione = newAccertamentoPerRegolarizzazione;
}
/**
 * Insert the method's description here.
 * Creation date: (12/11/2002 10.19.20)
 * @param newGenericiPerRegolarizzazione java.util.Collection
 */
public void setDocGenericiPerRegolarizzazione(java.util.Collection newGenericiPerRegolarizzazione) {
	docGenericiPerRegolarizzazione = newGenericiPerRegolarizzazione;
}
/**
 * Insert the method's description here.
 * Creation date: (12/11/2002 10.19.20)
 * @param newGenericiSelezionatiPerRegolarizzazione java.util.Collection
 */
public void setDocGenericiSelezionatiPerRegolarizzazione(java.util.Collection newGenericiSelezionatiPerRegolarizzazione) {
	docGenericiSelezionatiPerRegolarizzazione = newGenericiSelezionatiPerRegolarizzazione;
}
/**
 * @param newDocPassiviColl java.util.Collection
 */
public void setDocPassiviColl(java.util.Collection newDocPassiviColl) {
	docPassiviColl = newDocPassiviColl;
}
/**
 * Insert the method's description here.
 * Creation date: (31/03/2003 14.42.24)
 * @param newFaiReversale boolean
 */
public void setFaiReversale(boolean newFaiReversale) {
	faiReversale = newFaiReversale;
}
/**
 * @param newFind_doc_passivi it.cnr.contab.doccont00.core.bulk.V_doc_passivo_obbligazioneBulk
 */
public void setFind_doc_passivi(V_doc_passivo_obbligazioneBulk newFind_doc_passivi) {
	find_doc_passivi = newFind_doc_passivi;
}
/**
 * Insert the method's description here.
 * Creation date: (12/11/2002 10.26.09)
 * @param newReversaleDaDocAmm boolean
 */
public void setGeneraReversaleDaDocAmm(boolean newReversaleDaDocAmm) {
	generaReversaleDaDocAmm = newReversaleDaDocAmm;
}
/**
 * Insert the method's description here.
 * Creation date: (10/09/2002 15.26.28)
 * @param newDa1210List java.util.List
 */
public void setSospesiDa1210List(java.util.List newDa1210List) {
	sospesiDa1210List = newDa1210List;
}
/**
 * @param newTipoBolloOptions java.util.List
 */
public void setTipoBolloOptions(java.util.List newTipoBolloOptions) {
	tipoBolloOptions = newTipoBolloOptions;
}
/**
 * Metodo con cui si verifica la validità di alcuni campi, mediante un 
 * controllo sintattico o contestuale.
 */
public void validate() throws ValidationException {
	super.validate();
/*
	if ( !getMandato_rigaColl().isEmpty() && 
		 !((Mandato_rigaBulk) getMandato_rigaColl().get(0)).getFl_pgiro().booleanValue() &&
		 TIPO_REGOLARIZZAZIONE.equals( getTi_mandato() ) &&
		 (getAccertamentoPerRegolarizzazione() == null || getAccertamentoPerRegolarizzazione().getPg_accertamento() == null))
		throw new ValidationException( "Mandato di regolarizzazione: e' necessario specificare un accertamento per consentire la generazione in automatico della reversale di regolarizzazione" );
	*/
	/*
	if (  TIPO_REGOLARIZZAZIONE.equals( getTi_mandato() ) && getCrudStatus() == TO_BE_CREATED  &&
		  (getAccertamentoPerRegolarizzazione() == null || (getAccertamentoPerRegolarizzazione() != null && getAccertamentoPerRegolarizzazione().getPg_accertamento() == null)) &&
		  (getDocGenericoPerRegolarizzazione() == null || (getDocGenericoPerRegolarizzazione() != null && getDocGenericoPerRegolarizzazione().getPg_documento_generico() == null))	 )
		throw new ValidationException( "Mandato di regolarizzazione: e' necessario specificare un accertamento o un documento generico per consentire la generazione in automatico della reversale di regolarizzazione" );

	if (  TIPO_REGOLARIZZAZIONE.equals( getTi_mandato() ) && getCrudStatus() == TO_BE_CREATED  &&
		   getAccertamentoPerRegolarizzazione() != null && getAccertamentoPerRegolarizzazione().getPg_accertamento() != null &&
		   getDocGenericoPerRegolarizzazione() != null && getDocGenericoPerRegolarizzazione().getPg_documento_generico() != null	 )
		throw new ValidationException( "Mandato di regolarizzazione: e' necessario specificare in alternativa o un accertamento o un documento generico" );
	*/
	if (  TIPO_REGOLARIZZAZIONE.equals( getTi_mandato() ) && getCrudStatus() == TO_BE_CREATED  &&
		  (getAccertamentoPerRegolarizzazione() == null || (getAccertamentoPerRegolarizzazione() != null && getAccertamentoPerRegolarizzazione().getPg_accertamento() == null)) )
		throw new ValidationException( "Mandato di regolarizzazione: e' necessario specificare un accertamento per consentire la generazione in automatico della reversale di regolarizzazione" );

		

}
public Var_bilancioBulk getVar_bilancio() {
	return var_bilancio;
}

public void setVar_bilancio(Var_bilancioBulk var_bilancio) {
	this.var_bilancio = var_bilancio;
}
public boolean isErroreEsitaVariazioneBilancio() {
	return erroreEsitaVariazioneBilancio;
}

public void setErroreEsitaVariazioneBilancio(
		boolean erroreEsitaVariazioneBilancio) {
	this.erroreEsitaVariazioneBilancio = erroreEsitaVariazioneBilancio;
}
public Collection getScadenzeAccertamentoPerRegolarizzazione() {
	return scadenzeAccertamentoPerRegolarizzazione;
}
public void setScadenzeAccertamentoPerRegolarizzazione(Collection scadenzeAccertamentoPerRegolarizzazione) {
	this.scadenzeAccertamentoPerRegolarizzazione = scadenzeAccertamentoPerRegolarizzazione;
}
public Collection getScadenzeAccertamentoSelezionatePerRegolarizzazione() {
	return scadenzeAccertamentoSelezionatePerRegolarizzazione;
}
public void setScadenzeAccertamentoSelezionatePerRegolarizzazione(Collection scadenzeAccertamentoSelezionatePerRegolarizzazione) {
	this.scadenzeAccertamentoSelezionatePerRegolarizzazione = scadenzeAccertamentoSelezionatePerRegolarizzazione;
}
}
