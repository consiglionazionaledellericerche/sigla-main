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

import java.util.*;
import it.cnr.contab.anagraf00.core.bulk.*;
import it.cnr.jada.bulk.*;
public class MandatoAccreditamentoBulk extends MandatoBulk {
	private boolean fl_imputazione_manuale;
	private MandatoAccreditamentoBulk mandatoPerResiduo;
	public static final String DS_MANDATO_ACCREDITAMENTO = "ACCREDITAMENTO DA CNR A CDS ";
	
	protected BancaBulk banca = new BancaBulk();
	protected Modalita_pagamentoBulk modalita_pagamento = new Modalita_pagamentoBulk();
	protected List modalita_pagamentoOptions;
	protected List bancaOptions;

	protected String codice_cds;

	protected java.util.Collection impegniSelezionatiColl = new java.util.LinkedList();
	protected boolean impegniModificati = false;

public MandatoAccreditamentoBulk() {
	super();
}
public MandatoAccreditamentoBulk(String cd_cds, Integer esercizio, Long pg_mandato) {
	super(cd_cds, esercizio, pg_mandato);
}
/**
 * Aggiunge un nuovo dettaglio (Mandato_rigaBulk) alla lista di dettagli definiti per il mandato
 * inizializzandone alcuni campi
 * @param mr dettaglio da aggiungere alla lista
 * @return int
 */

public int addToImpegniSelezionatiColl( List impegni) 
{
	impegniSelezionatiColl.addAll(impegni);
	return impegniSelezionatiColl.size()-1;
}
/**
 * Aggiunge un nuovo dettaglio (Mandato_rigaBulk) alla lista di dettagli definiti per il mandato
 * inizializzandone alcuni campi
 * @param mr dettaglio da aggiungere alla lista
 * @return int
 */

public int addToMandato_rigaColl( Mandato_rigaBulk riga) 
{
	riga.setStato( riga.STATO_INIZIALE );
	riga.setModalita_pagamentoOptions( getModalita_pagamentoOptions());
	riga.setBancaOptions( getBancaOptions());
	riga.setMandato( this );
	riga.setFl_pgiro( new Boolean(false));
	mandato_rigaColl.add(riga);
//	impegniColl.remove( impegno);
	return mandato_rigaColl.size()-1;
}
/**
 * Aggiunge un nuovo dettaglio (Mandato_rigaBulk) alla lista di dettagli definiti per il mandato
 * inizializzandone alcuni campi
 * @param mr dettaglio da aggiungere alla lista
 * @return int
 */

public int addToMandato_rigaColl( Mandato_rigaBulk riga, V_impegnoBulk impegno ) 
{
	mandato_rigaColl.add(riga);
//	impegniColl.remove( impegno);
	return mandato_rigaColl.size()-1;
}
/**
 * @return it.cnr.contab.anagraf00.core.bulk.BancaBulk
 */
public it.cnr.contab.anagraf00.core.bulk.BancaBulk getBanca() {
	return banca;
}
/**
 * @return java.util.List
 */
public java.util.List getBancaOptions() {
	return bancaOptions;
}
/**
 * @return java.lang.String
 */
public java.lang.String getCodice_cds() {
	return codice_cds;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'fl_imputazione_manuale'
 *
 * @return Il valore della proprietà 'fl_imputazione_manuale'
 */
public boolean getFl_imputazione_manuale() {
	return fl_imputazione_manuale;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'im_righe_mandato'
 *
 * @return Il valore della proprietà 'im_righe_mandato'
 */
public java.math.BigDecimal getIm_righe_mandato() {
	java.math.BigDecimal tot = new java.math.BigDecimal( 0 );
	Mandato_rigaBulk riga;
	for ( Iterator i = getMandato_rigaColl().iterator(); i.hasNext(); )
	{
		riga = (Mandato_rigaBulk) i.next();
		if ( riga.getIm_mandato_riga() != null )
			tot = tot.add( riga.getIm_mandato_riga() );
		
	}	
	return tot;
}
/**
 * Insert the method's description here.
 * Creation date: (01/07/2003 10.02.27)
 * @return java.util.Collection
 */
public java.util.Collection getImpegniSelezionatiColl() {
	return impegniSelezionatiColl;
}
/**
 * @return it.cnr.contab.doccont00.core.bulk.MandatoAccreditamentoBulk
 */
public MandatoAccreditamentoBulk getMandatoPerResiduo() {
	return mandatoPerResiduo;
}
/**
 * @return it.cnr.contab.anagraf00.core.bulk.Modalita_pagamentoBulk
 */
public it.cnr.contab.anagraf00.core.bulk.Modalita_pagamentoBulk getModalita_pagamento() {
	return modalita_pagamento;
}
/**
 * @return java.util.List
 */
public java.util.List getModalita_pagamentoOptions() {
	return modalita_pagamentoOptions;
}
/**
 * Inizializza l'Oggetto Bulk per l'inserimento.
 * @param bp Il Business Process in uso
 * @param context Il contesto dell'azione
 * @return OggettoBulk L'oggetto bulk inizializzato
 */
public OggettoBulk initialize(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) 
{
	super.initialize( bp, context );
	setFl_imputazione_manuale( true );
	setTi_mandato( MandatoBulk.TIPO_ACCREDITAMENTO );
	return this;
}	
/**
 * Inizializza l'Oggetto Bulk per la ricerca.
 * @param bp Il Business Process in uso
 * @param context Il contesto dell'azione
 * @return OggettoBulk L'oggetto bulk inizializzato
 */
public OggettoBulk initializeForSearch(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
	super.initializeForSearch( bp, context );
	//reset dell'uo
	unita_organizzativa = new it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk();
	return this;
}
public boolean isMandatoAccreditamentoBulk() 
{
	return true;
}
public V_impegnoBulk removeFromImpegniSelezionatiColl(int index) 
{
	return (V_impegnoBulk)((LinkedList)impegniSelezionatiColl).remove(index );
}
/**
 * @param newBanca it.cnr.contab.anagraf00.core.bulk.BancaBulk
 */
public void setBanca(it.cnr.contab.anagraf00.core.bulk.BancaBulk newBanca) {
	banca = newBanca;
}
/**
 * @param newBancaOptions java.util.List
 */
public void setBancaOptions(java.util.List newBancaOptions) {
	bancaOptions = newBancaOptions;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'codice_cds'
 *
 * @param newCodice_cds	Il valore da assegnare a 'codice_cds'
 */
public void setCodice_cds(java.lang.String newCodice_cds) {
	codice_cds = newCodice_cds;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'fl_imputazione_manuale'
 *
 * @param newFl_imputazione_manuale	Il valore da assegnare a 'fl_imputazione_manuale'
 */
public void setFl_imputazione_manuale(boolean newFl_imputazione_manuale) {
	fl_imputazione_manuale = newFl_imputazione_manuale;
}
/**
 * Insert the method's description here.
 * Creation date: (01/07/2003 10.02.27)
 * @param newImpegniSelezionatiColl java.util.Collection
 */
public void setImpegniSelezionatiColl(java.util.Collection newImpegniSelezionatiColl) {
	impegniSelezionatiColl = newImpegniSelezionatiColl;
}
/**
 * @param newMandatoPerResiduo it.cnr.contab.doccont00.core.bulk.MandatoAccreditamentoBulk
 */
public void setMandatoPerResiduo(MandatoAccreditamentoBulk newMandatoPerResiduo) {
	mandatoPerResiduo = newMandatoPerResiduo;
}
/**
 * @param newModalita_pagamento it.cnr.contab.anagraf00.core.bulk.Modalita_pagamentoBulk
 */
public void setModalita_pagamento(it.cnr.contab.anagraf00.core.bulk.Modalita_pagamentoBulk newModalita_pagamento) {
	modalita_pagamento = newModalita_pagamento;
}
/**
 * @param newModalita_pagamentoOptions java.util.List
 */
public void setModalita_pagamentoOptions(java.util.List newModalita_pagamentoOptions) {
	modalita_pagamentoOptions = newModalita_pagamentoOptions;
}
/**
 * Metodo con cui si verifica la validità di alcuni campi, mediante un 
 * controllo sintattico o contestuale.
 */
public void validate() throws ValidationException {
	super.validate();
	//verifico le righe specificate dall'utente
	for ( Iterator i = getMandato_rigaColl().iterator(); i.hasNext(); )
		((MandatoAccreditamento_rigaBulk) i.next()).validate();					
}
/**
 * Metodo con cui si verifica la validità di alcuni campi, mediante un 
 * controllo sintattico o contestuale.
 */
public void validateNuovoImpegno( V_impegnoBulk impegno) throws ValidationException
{
	// verifico che l'impegno non sia già presente fra le righe del mandato
	for ( Iterator i = getMandato_rigaColl().iterator(); i.hasNext(); )
	{
		MandatoAccreditamento_rigaBulk riga = (MandatoAccreditamento_rigaBulk) i.next();
		if ( riga.getImpegno().equalsByPrimaryKey( impegno ))
		 	 throw new ValidationException( "L'impegno selezionato e' già presente nel mandato");
	}

}	
}
