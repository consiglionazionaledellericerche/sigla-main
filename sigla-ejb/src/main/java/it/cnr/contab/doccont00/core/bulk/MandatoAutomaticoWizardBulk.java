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
 * Created on Mar 27, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.doccont00.core.bulk;


import java.math.BigDecimal;
import java.util.*;

import it.cnr.contab.anagraf00.core.bulk.*;
import it.cnr.contab.docamm00.docs.bulk.Documento_genericoBulk;
import it.cnr.contab.docamm00.docs.bulk.Documento_generico_passivoBulk;
import it.cnr.contab.doccont00.bp.MandatoAutomaticoWizardBP;
import it.cnr.contab.util.enumeration.TipoIVA;
import it.cnr.jada.bulk.*;

public class MandatoAutomaticoWizardBulk extends MandatoIBulk {
	private java.util.Collection impegniColl = java.util.Collections.EMPTY_LIST;
	private java.util.Collection mandatiColl = new java.util.LinkedList();
	
	private boolean fl_imputazione_manuale;
	private MandatoIBulk mandatoPerResiduo;
	
	protected BancaBulk banca = new BancaBulk();
	protected Modalita_pagamentoBulk modalita_pagamento = new Modalita_pagamentoBulk();
	protected List modalita_pagamentoOptions;
	protected List bancaOptions;

	private Documento_genericoBulk modelloDocumento;

	private String ti_impegni;
	private String ti_automatismo;
	
	public final static String AUTOMATISMO_DA_IMPEGNI	= "I";
	public final static String AUTOMATISMO_DA_DOCPASSIVI	= "D";
	public final static Dictionary tipoAutomatismoKeys;

	static 
	{
		tipoAutomatismoKeys = new it.cnr.jada.util.OrderedHashtable();
		tipoAutomatismoKeys.put(AUTOMATISMO_DA_IMPEGNI,	   "Impegni");
		tipoAutomatismoKeys.put(AUTOMATISMO_DA_DOCPASSIVI, "Documenti Passivi");
	};	

	protected java.util.Collection impegniSelezionatiColl = new java.util.LinkedList();
	protected java.util.Collection docPassiviSelezionatiColl = new java.util.LinkedList();
	protected boolean impegniModificati = false;

	public final static String IMPEGNI_TIPO_COMPETENZA	= "C";
	public final static String IMPEGNI_TIPO_RESIDUO		= "R";
	public final static String IMPEGNI_TIPO_ALL			= "A";
	public final static Dictionary tipoImpegniKeys;

	static 
	{
		tipoImpegniKeys = new it.cnr.jada.util.OrderedHashtable();
		tipoImpegniKeys.put(IMPEGNI_TIPO_COMPETENZA,	"Competenza");
		tipoImpegniKeys.put(IMPEGNI_TIPO_RESIDUO,		"Residuo");
		tipoImpegniKeys.put(IMPEGNI_TIPO_ALL,			"Tutti");
	};	

	public void assegnaImportiInBaseAPriorita() throws ValidationException 
	{
		ordinaImpegniPerPriorita();
		validatePriorita();
		resetImportoImpegniSelezionatiColl();
		V_obbligazioneBulk impegno;
		BigDecimal totTrasferito = new BigDecimal(0);
		for (Iterator i = getImpegniSelezionatiColl().iterator(); i.hasNext(); )
		{
			impegno = (V_obbligazioneBulk) i.next();
			if ( impegno.getIm_disponibile().compareTo( getIm_mandato().subtract( totTrasferito)) <= 0 )
				impegno.setIm_da_trasferire( impegno.getIm_disponibile());
			else
			{
				impegno.setIm_da_trasferire( getIm_mandato().subtract(totTrasferito));
				break;
			}	
			totTrasferito = totTrasferito.add( impegno.getIm_da_trasferire());
		}
		//rimuovo dalla lista degli impegni selezionati quelli che hanno im_da_trasferire = 0 
		for (Iterator i = getImpegniSelezionatiColl().iterator(); i.hasNext(); )
		{
			impegno = (V_obbligazioneBulk) i.next();
			if ( impegno.getIm_da_trasferire() == null || impegno.getIm_da_trasferire().compareTo( new BigDecimal(0)) == 0 )
				i.remove();
		}		
	}
	
	/**
	 * <!-- @TODO: da completare -->
	 * Restituisce il valore della proprietà 'im_totale_impegni_selezionati'
	 *
	 * @return Il valore della proprietà 'im_totale_impegni_selezionati'
	 */
	public BigDecimal getIm_totale_impegni_selezionati() {
		java.math.BigDecimal totImpegni = new BigDecimal(0);
		V_obbligazioneBulk impegno;
		for ( java.util.Iterator i = impegniSelezionatiColl.iterator(); i.hasNext(); )
		{
			impegno = (V_obbligazioneBulk) i.next();
			if ( impegno.getIm_da_trasferire() != null )
				totImpegni = totImpegni.add( impegno.getIm_da_trasferire() );
		}		
		return totImpegni;	
	
	}
	
	/**
	 * @return java.util.Collection
	 */
	public java.util.Collection getImpegniColl() {
		return impegniColl;
	}
	
	/**
	 * @return java.util.Collection
	 */
	public java.util.Collection getMandatiColl() {
		return mandatiColl;
	}
	
	/**
	 * Inizializza l'Oggetto Bulk per l'inserimento.
	 * @param bp Il Business Process in uso
	 * @param context Il contesto dell'azione
	 * @return OggettoBulk L'oggetto bulk inizializzato
	 */
	public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
		super.initializeForInsert( bp, context );	
		if ( bp instanceof MandatoAutomaticoWizardBP ) { 
			if (((MandatoAutomaticoWizardBP)bp).getTi_impegni() != null)
				setTi_impegni(((MandatoAutomaticoWizardBP)bp).getTi_impegni());
			else 
				setTi_impegni( IMPEGNI_TIPO_ALL );
			if (((MandatoAutomaticoWizardBP)bp).getTi_automatismo() != null)
				setTi_automatismo(((MandatoAutomaticoWizardBP)bp).getTi_automatismo());
			else 
				setTi_automatismo( AUTOMATISMO_DA_IMPEGNI );
		}
		setTi_mandato( TIPO_PAGAMENTO );
		Documento_generico_passivoBulk docPassivo = new Documento_generico_passivoBulk();
		docPassivo.setTi_istituz_commerc(TipoIVA.COMMERCIALE.value());
		setModelloDocumento(docPassivo);
		return this;
	}
	
	/**
	 * <!-- @TODO: da completare -->
	 * 
	 *
	 * @throws ValidationException	
	 */
	public void ordinaImpegniPerPriorita() throws ValidationException 
	{
		V_obbligazioneBulk impegno;
		if ( getImpegniSelezionatiColl().size() > 0 )
		{
			impegno = (V_obbligazioneBulk) getImpegniSelezionatiColl().iterator().next();
			setImpegniSelezionatiColl( impegno.ordinaPerPriorita((List)getImpegniSelezionatiColl()));
		}	
	}
	
	/**
	 * <!-- @TODO: da completare -->
	 * 
	 *
	 */
	public void resetImportoImpegniSelezionatiColl() {
		for (Iterator i = getImpegniSelezionatiColl().iterator(); i.hasNext(); )
			((V_obbligazioneBulk)i.next()).setIm_da_trasferire( new BigDecimal(0));
	}
	
	/**
	 * <!-- @TODO: da completare -->
	 * 
	 *
	 */
	public void selezionaImpegni()
	{
		impegniSelezionatiColl = new LinkedList();
		V_obbligazioneBulk impegno;
		for (Iterator i = impegniColl.iterator(); i.hasNext(); )
		{
			impegno = (V_obbligazioneBulk) i.next();
			if ( impegno.getIm_da_trasferire() != null && impegno.getIm_da_trasferire().compareTo(new BigDecimal(0)) != 0 )
				impegniSelezionatiColl.add( impegno );
		}		
	}
	
	/**
	 * @param newImpegniColl java.util.Collection
	 */
	public void setImpegniColl(java.util.Collection newImpegniColl) {
		impegniColl = newImpegniColl;
	}

	/**
	 * @param newMandatiColl java.util.Collection
	 */
	public void setMandatiColl(java.util.Collection newMandatiColl) {
		mandatiColl = newMandatiColl;
	}

	/**
	 * Metodo con cui si verifica la validità di alcuni campi, mediante un 
	 * controllo sintattico o contestuale.
	 */
	public void validate() throws ValidationException {
		super.validate();
		for ( java.util.Iterator i = impegniSelezionatiColl.iterator(); i.hasNext(); )
			((V_obbligazioneBulk)i.next()).validate();
		for ( Iterator i = getMandato_rigaColl().iterator(); i.hasNext(); )
			((Mandato_rigaIBulk) i.next()).validate();					
	}

	/**
	 * Metodo con cui si verifica la validità di alcuni campi, mediante un 
	 * controllo sintattico o contestuale.
	 */
	/* gli impegni sono già stati ordinati*/
	public void validatePriorita() throws ValidationException 
	{
		V_obbligazioneBulk impegno;
		int nrImpegni = getImpegniSelezionatiColl().size();
		int prioritaPrecedente = 0;
		for (java.util.Iterator i = getImpegniSelezionatiColl().iterator(); i.hasNext(); )
		{
			impegno = (V_obbligazioneBulk) i.next();
			if ( impegno.getPriorita().intValue() == prioritaPrecedente )
				throw new ValidationException("La priorita' " + prioritaPrecedente + " e' stata specificata piu' volte" );
			prioritaPrecedente = impegno.getPriorita().intValue();	
		}		
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
		return mandato_rigaColl.size()-1;
	}

	/**
	 * Aggiunge un nuovo dettaglio (Mandato_rigaBulk) alla lista di dettagli definiti per il mandato
	 * inizializzandone alcuni campi
	 * @param mr dettaglio da aggiungere alla lista
	 * @return int
	 */
	public int addToMandato_rigaColl( Mandato_rigaBulk riga, V_obbligazioneBulk impegno ) 
	{
		mandato_rigaColl.add(riga);
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
	 * <!-- @TODO: da completare -->
	 * Restituisce il valore della proprietà 'fl_imputazione_manuale'
	 *
	 * @return Il valore della proprietà 'fl_imputazione_manuale'
	 */
	public boolean isFl_imputazione_manuale() {
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
	public MandatoIBulk getMandatoPerResiduo() {
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
		setTi_mandato( MandatoBulk.TIPO_PAGAMENTO );
		return this;
	}	
	
	public V_obbligazioneBulk removeFromImpegniSelezionatiColl(int index) 
	{
		return (V_obbligazioneBulk)((LinkedList)impegniSelezionatiColl).remove(index );
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
	 * @param newMandatoPerResiduo it.cnr.contab.doccont00.core.bulk.MandatoAutomaticoBulk
	 */
	public void setMandatoPerResiduo(MandatoIBulk newMandatoPerResiduo) {
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

	public Dictionary getTi_istituz_commercKeys() {
		return Documento_genericoBulk.TIPO;
	}

	public boolean isROCreditore() {
		return true;
	}

	public String getTi_impegni() {
		return ti_impegni;
	}

	public void setTi_impegni(String ti_impegni) {
		this.ti_impegni = ti_impegni;
	}

	public String getTi_automatismo() {
		return ti_automatismo;
	}
	
	public void setTi_automatismo(String ti_automatismo) {
		this.ti_automatismo = ti_automatismo;
	}
	
	public boolean isAutomatismoDaImpegni() {
		return getTi_automatismo().equals(AUTOMATISMO_DA_IMPEGNI);
	}
	
	public boolean isAutomatismoDaDocumentiPassivi() {
		return getTi_automatismo().equals(AUTOMATISMO_DA_DOCPASSIVI);
	}

	public boolean isMandatiCreati() {
		return getMandatiColl().size()>0;
	}

	public java.util.Collection getDocPassiviSelezionatiColl() {
		return docPassiviSelezionatiColl;
	}

	public void setDocPassiviSelezionatiColl(
			java.util.Collection docPassiviSelezionatiColl) {
		this.docPassiviSelezionatiColl = docPassiviSelezionatiColl;
	}

	public Documento_genericoBulk getModelloDocumento() {
		return modelloDocumento;
	}
	

	public void setModelloDocumento(Documento_genericoBulk modelloDocumento) {
		this.modelloDocumento = modelloDocumento;
	}
}
