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

package it.cnr.contab.doccont00.bp;

import it.cnr.contab.doccont00.ejb.MandatoComponentSession;
import java.util.*;
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.util.action.*;
import it.cnr.jada.util.jsp.Button;

import java.math.*;

/**
 * Business Process che gestisce le attività di CRUD per l'entita' Mandato di Accreditamento
 */

public class MandatoAccreditamentoWizardBP extends it.cnr.jada.util.action.SimpleCRUDBP {
	private final SimpleDetailCRUDController impegni = new SimpleDetailCRUDController("Impegni",V_impegnoBulk.class,"impegniColl",this);
	private final SimpleDetailCRUDController mandati = new SimpleDetailCRUDController("Mandati",MandatoAccreditamentoBulk.class,"mandatiColl",this);
	private String codice_cds;
	private BigDecimal im_mandato;
public MandatoAccreditamentoWizardBP() 
{
	super();
}
public MandatoAccreditamentoWizardBP(String function) 
{
	super(function);
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param function	La funzione con cui è stato creato il BusinessProcess
 * @param codice_cds	
 * @param importo	
 */
public MandatoAccreditamentoWizardBP(String function, String codice_cds, BigDecimal importo ) 
{
	super(function);
	this.codice_cds = codice_cds;
	this.im_mandato = importo;
}
/**
 * Metodo utilizzato per gestire l'aggiunta di nuove righe al mandato.
  	 * @param context <code>ActionContext</code> in uso.
	 *
	 * @return <code>Forward</code>
 */

public void aggiungiRighe(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException 
{
	try 
	{
		MandatoAccreditamentoBulk mandato = (MandatoAccreditamentoBulk) getModel();
		if ( getImpegni().getSelectedModels(context).size() != 0 )
		{
			mandato = (MandatoAccreditamentoBulk) ((MandatoComponentSession) createComponentSession()).aggiungiDocPassivi( context.getUserContext(), mandato, getImpegni().getSelectedModels(context));
			setModel( context, mandato );
			getImpegni().getSelection().clear();
			resyncChildren( context );
		}	
	} catch(Exception e) {
		throw handleException(e);
	}

	
}
/**
 * Metodo utilizzato per gestire la modifica delle coordinate bancarie (BancaBulk) a seguito della 
 * modifica delle modalita Di Pagamento
 * @param context <code>ActionContext</code> in uso.
 *
 */

public void cambiaModalitaPagamento(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException 
{
	try 
	{
		MandatoAccreditamentoBulk mandato = (MandatoAccreditamentoBulk) getModel();
		List result = ((MandatoComponentSession) createComponentSession()).findBancaOptions( context.getUserContext(), mandato);
		mandato.setBancaOptions( result );
		setModel( context, mandato );
	} catch(Exception e) {
		throw handleException(e);
	}

	
}
public void create(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException 
{
	try {
		super.create( context );
		MandatoAccreditamentoWizardBulk wizard = (MandatoAccreditamentoWizardBulk) getModel();
		boolean isSiopeAttivo = Utility.createParametriCnrComponentSession().getParametriCnr(context.getUserContext(), wizard.getEsercizio()).getFl_siope().booleanValue();
		if (wizard.getMandatiColl().size() == 1 )
			setMessage( "Il Mandato di Accreditamento e la corrispondente Reversale " + (isSiopeAttivo?"definitiva":"provvisoria") + " a favore del Cds " + wizard.getCodice_cds() + " sono stati generati.");
		if (wizard.getMandatiColl().size() == 2 )
			setMessage( "I Mandati di Accreditamento (di competenza e residuo) e le corrispondenti Reversali " + (isSiopeAttivo?"definitive":"provvisorie") + " a favore del Cds " + wizard.getCodice_cds() + " sono stati generati.");		
	} catch(Exception e) {
		throw handleException(e);
	}
}
/**
 * Metodo utilizzato per creare una toolbar applicativa personalizzata.
 * @return null In questo caso la toolbar è vuota
 */

protected it.cnr.jada.util.jsp.Button[] createToolbar() 
{
	return null;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'codice_cds'
 *
 * @return Il valore della proprietà 'codice_cds'
 */
public java.lang.String getCodice_cds() {
	return codice_cds;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'im_mandato'
 *
 * @return Il valore della proprietà 'im_mandato'
 */
public java.math.BigDecimal getIm_mandato() {
	return im_mandato;
}
/**
 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
 */
public final it.cnr.jada.util.action.SimpleDetailCRUDController getImpegni() {
	return impegni;
}
/**
 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
 */
public final it.cnr.jada.util.action.SimpleDetailCRUDController getMandati() {
	return mandati;
}
/**
 *	Abilito il bottone di emissione del mandato.
 *	@return			TRUE 	Abilitato se non sono ancora stati emessi mandati
 *					FALSE 	Non è abilitato se sono già stati emessi i mandati
 */
public boolean isEmettiMandatoButtonEnabled() {
	return ((MandatoAccreditamentoWizardBulk)getModel()).getMandatiColl().size() ==  0 ;

}
/**
 *	Abilito il flag di calcolo automatico.
 *	@return			TRUE 	Il flag di calcolo automatico è abilitato
 *					FALSE 	Il flag di calcolo automatico non è abilitato 
 */
public boolean isFlCalcoloAutomaticoCheckboxEnabled() {
	return ((MandatoAccreditamentoWizardBulk)getModel()).getMandatiColl().size() ==  0 ;
}
/**
 *	Gestisce l'abilitazione o meno del bottone di visualizzazione di un mandato di accreditamento
 *	@return			TRUE 	Abilitato se un mandato e' stato selezionato nella lista dei mandati
 *					FALSE 	Non è abilitato se nessun mandato e' stato selezionato nella lista dei mandati
 */
public boolean isVisualizzaMandatoButtonEnabled() {
//	return ((MandatoAccreditamentoWizardBulk)getModel()).getMandatiColl().size() >  0 ;
	return getMandati().getModel() != null  ;
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
 * Imposta il valore della proprietà 'im_mandato'
 *
 * @param newIm_mandato	Il valore da assegnare a 'im_mandato'
 */
public void setIm_mandato(java.math.BigDecimal newIm_mandato) {
	im_mandato = newIm_mandato;
}
}
