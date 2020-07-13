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

package it.cnr.contab.compensi00.bp;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;

import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.anagraf00.core.bulk.AnagraficoHome;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoHome;
import it.cnr.contab.anagraf00.tabrif.bulk.*;
import it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk;
import it.cnr.contab.anagraf00.util.ExPartitaIVA;
import it.cnr.contab.compensi00.tabrif.bulk.*;
import it.cnr.contab.compensi00.docs.bulk.*;
import it.cnr.contab.compensi00.ejb.*;
import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.reports.bp.*;
import it.cnr.contab.reports.bulk.Print_spooler_paramBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;

/**
 * Insert the type's description here.
 * Creation date: (08/07/2002 16.56.00)
 * @author: Roberto Fantino
 */
public class CRUDConguaglioBP extends it.cnr.jada.util.action.SimpleCRUDBP {
/**
 * CRUDConguaglioBP constructor comment.
 */
public CRUDConguaglioBP() {
	super("Tr");
}
/**
 * CRUDConguaglioBP constructor comment.
 * @param function java.lang.String
 */
public CRUDConguaglioBP(String function) {
	super(function+"Tr");
}
/**
  * Il conguaglio viene messo in Visualizzazione se il compenso associato ha
  * una obbligazione riportata.
  */

	public void basicEdit(it.cnr.jada.action.ActionContext context, it.cnr.jada.bulk.OggettoBulk bulk, boolean doInitializeForEdit) throws it.cnr.jada.action.BusinessProcessException
{
	super.basicEdit(context, bulk, doInitializeForEdit);

	ConguaglioBulk conguaglio = (ConguaglioBulk)getModel();
	if (!isViewing())
	{
		if(conguaglio.getCompenso()!=null && conguaglio.getCompenso().isRiportata())
			setStatus(VIEW);
	}
}

	/**
 * Insert the method's description here.
 * Creation date: (25/02/2002 12.56.44)
 * @return it.cnr.contab.compensi00.docs.bulk.CompensoBulk
 * @param userContext it.cnr.jada.UserContext
 * @param compenso it.cnr.contab.compensi00.docs.bulk.CompensoBulk
 * @param aTerzo it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk
 */
public void completaTerzo(ActionContext context, ConguaglioBulk conguaglio, V_terzo_per_compensoBulk vTerzo) throws BusinessProcessException {

	try {

		ConguaglioComponentSession component = (ConguaglioComponentSession)createComponentSession();
		conguaglio = component.completaTerzo(context.getUserContext(), conguaglio, vTerzo);

		conguaglio.setStatoToAbilitaConguaglio();
		setModel(context, conguaglio);

	}catch(it.cnr.jada.comp.ComponentException ex){
		throw handleException(ex);
	}catch(java.rmi.RemoteException ex){
		throw handleException(ex);
	}

}
/**
 * Insert the method's description here.
 * Creation date: (25/02/2002 12.56.44)
 * @return it.cnr.contab.compensi00.docs.bulk.CompensoBulk
 * @param userContext it.cnr.jada.UserContext
 * @param compenso it.cnr.contab.compensi00.docs.bulk.CompensoBulk
 * @param aTerzo it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk
 */
public void doAbilitaConguaglio(ActionContext context) throws BusinessProcessException {

	try {

		ConguaglioComponentSession session = (ConguaglioComponentSession )createComponentSession();
		ConguaglioBulk conguaglio = session.doAbilitaConguaglio(context.getUserContext(), (ConguaglioBulk)getModel());

		conguaglio.setStatoToCreaCompensoConguaglio();
		setModel(context, conguaglio);

	}catch(it.cnr.jada.comp.ComponentException ex){
		throw handleException(ex);
	}catch(java.rmi.RemoteException ex){
		throw handleException(ex);
	}

}
/**
 * Insert the method's description here.
 * Creation date: (25/02/2002 12.56.44)
 * @return it.cnr.contab.compensi00.docs.bulk.CompensoBulk
 * @param userContext it.cnr.jada.UserContext
 * @param compenso it.cnr.contab.compensi00.docs.bulk.CompensoBulk
 * @param aTerzo it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk
 */
public void doCreaCompensoConguaglio(ActionContext context) throws BusinessProcessException {

	try {

		ConguaglioComponentSession session = (ConguaglioComponentSession )createComponentSession();
		ConguaglioBulk conguaglio = session.doCreaCompensoConguaglio(context.getUserContext(), (ConguaglioBulk)getModel());

		conguaglio.setStatoToNormale();
		setModel(context, conguaglio);

	}catch(it.cnr.jada.comp.ComponentException ex){
		throw handleException(ex);
	}catch(java.rmi.RemoteException ex){
		throw handleException(ex);
	}

}
public void findListaBanche(ActionContext context) throws BusinessProcessException{

	try{
		ConguaglioBulk conguaglio = (ConguaglioBulk)getModel();
		if (conguaglio.getModalitaPagamento() != null) {
			ConguaglioComponentSession component = (ConguaglioComponentSession )createComponentSession();
			java.util.List coll = component.findListaBanche(context.getUserContext(), conguaglio);

			//	Assegno di default la prima banca tra quelle selezionate
			if(coll == null || coll.isEmpty())
				conguaglio.setBanca(null);
			else
				conguaglio.setBanca((it.cnr.contab.anagraf00.core.bulk.BancaBulk)new java.util.Vector(coll).firstElement());
		}else
			conguaglio.setBanca(null);

	}catch(it.cnr.jada.comp.ComponentException ex){
		throw handleException(ex);
	}catch(java.rmi.RemoteException ex){
		throw handleException(ex);
	}
}
/**
 *	Ricerca i tipi rapporto validi associati al terzo selezionato
 *
**/

public void findTipiRapporto(ActionContext context) throws BusinessProcessException{

	try{
		ConguaglioBulk conguaglio = (ConguaglioBulk)getModel();
		if (conguaglio.getTerzo()!= null) {
			ConguaglioComponentSession sess = (ConguaglioComponentSession)createComponentSession();
			java.util.Collection coll = sess.findTipiRapporto(context.getUserContext(), conguaglio);
			conguaglio.setTipiRapporto(coll);

			if(coll == null || coll.isEmpty()){
				conguaglio.setTipoRapporto(null);
				throw new it.cnr.jada.comp.ApplicationException("Non esistono Tipi Rapporto validi associati al terzo selezionato");
			}
		}else
			conguaglio.setTipoRapporto(null);

	}catch(it.cnr.jada.comp.ComponentException ex){
		throw handleException(ex);
	}catch(java.rmi.RemoteException ex){
		throw handleException(ex);
	}
}
public void findTipiTrattamento(ActionContext context) throws BusinessProcessException{

	try{
		ConguaglioBulk conguaglio = (ConguaglioBulk)getModel();
		if (conguaglio.getTipoRapporto()!= null) {
			ConguaglioComponentSession sess = (ConguaglioComponentSession)createComponentSession();
			java.util.Collection coll = sess.findTipiTrattamento(context.getUserContext(), conguaglio);
			conguaglio.setTipiTrattamento(coll);

			if(coll == null || coll.isEmpty()){
				conguaglio.setTipoTrattamento(null);
				throw new it.cnr.jada.comp.ApplicationException("Non esistono Tipi Trattamento associati al Tipo di Rapporto selezionato");
			}
		}else
			conguaglio.setTipoTrattamento(null);
			
	}catch(it.cnr.jada.comp.ComponentException ex){
		throw handleException(ex);
	}catch(java.rmi.RemoteException ex){
		throw handleException(ex);
	}
}
protected void initializePrintBP(ActionContext context, it.cnr.jada.util.action.AbstractPrintBP bp) {

	OfflineReportPrintBP printbp = (OfflineReportPrintBP)bp;
//	printbp.setReportName("/docamm/docamm/vpg_conguaglio.rpt");
	ConguaglioBulk conguaglio = (ConguaglioBulk)getModel();
	
//	printbp.setReportParameter(0, "0"); // tc
//	printbp.setReportParameter(1, conguaglio.getCd_cds());
//	printbp.setReportParameter(2, conguaglio.getCd_unita_organizzativa());
//	printbp.setReportParameter(3, conguaglio.getEsercizio().toString());
//	printbp.setReportParameter(4, conguaglio.getPg_conguaglio().toString());
	
	printbp.setReportName("/docamm/docamm/vpg_conguaglio.jasper");
	Print_spooler_paramBulk param;
	param = new Print_spooler_paramBulk();
	param.setNomeParam("Esercizio");
	param.setValoreParam(conguaglio.getEsercizio().toString());
	param.setParamType("java.lang.Integer");
	printbp.addToPrintSpoolerParam(param);

	param = new Print_spooler_paramBulk();
	param.setNomeParam("CDS");
	param.setValoreParam(conguaglio.getCd_cds());
	param.setParamType("java.lang.String");
	printbp.addToPrintSpoolerParam(param);
	
	param = new Print_spooler_paramBulk();
	param.setNomeParam("UO");
	param.setValoreParam(conguaglio.getCd_unita_organizzativa());
	param.setParamType("java.lang.String");
	printbp.addToPrintSpoolerParam(param);
	
	param = new Print_spooler_paramBulk();
	param.setNomeParam("Pg_conguaglio");
	param.setValoreParam(conguaglio.getPg_conguaglio().toString());
	param.setParamType("java.lang.Integer");
	printbp.addToPrintSpoolerParam(param);
	
}
public boolean isBottoneAbilitaConguaglioEnabled(){

	if (isViewing() || isSearching())
		return false;

	ConguaglioBulk conguaglio = (ConguaglioBulk)getModel();
	if (!conguaglio.isStatoAbilitaConguaglio())
		return false;

	return true;
}
public boolean isBottoneCreaCompensoConguaglioEnabled(){

	if (isViewing() || isSearching())
		return false;

	ConguaglioBulk conguaglio = (ConguaglioBulk)getModel();
	if (!conguaglio.isStatoCreaCompensoConguaglio())
		return false;

	return true;
}
public boolean isBottoneVisualizzaCompensoEnabled(){

	if (isSearching())
		return false;

	ConguaglioBulk conguaglio = (ConguaglioBulk)getModel();
	return (conguaglio!=null && conguaglio.getPg_compenso()!=null);
}
/** 
  * Abilito il bottone di cancellazione conguaglio solo se non e' stato 
  * annullato e se non sono ne in ricerca ne in inserimento
  */

public boolean isDeleteButtonEnabled() 
{
	ConguaglioBulk conguaglio = (ConguaglioBulk)getModel();
	if(conguaglio.getCompenso()!=null && conguaglio.getCompenso().isRiportata())
		return false;

	return 	conguaglio != null &&
			conguaglio.getDt_cancellazione() == null &&
			!isInputReadonly() &&
			!isSearching() &&
			!isInserting();
}

public boolean isPrintButtonHidden() {
	
	return super.isPrintButtonHidden() || isInserting() || isSearching();
}
/**
  * Il Bottone salva non deve essere abilitato in modifica
  */

public boolean isSaveButtonEnabled() 
{
	return super.isSaveButtonEnabled() && !isEditing();
}
/**
 * Insert the method's description here.
 * Creation date: (22/02/2002 18.28.25)
 */
public void resetTabs(ActionContext context) {

	setTab("tab","tabConguaglio");
}
/**
 * Ripristina la selezione del vecchio Tipo Rapporto
 *
**/
public void ripristinaSelezioneTipoRapporto(){
	
	ConguaglioBulk conguaglio = (ConguaglioBulk)getModel();
	Tipo_rapportoBulk tipoRapporto = conguaglio.getTipoRapporto();

	// ripristino la selezione del Tipo Rapporto
	if (tipoRapporto!=null){
		for (java.util.Iterator i = conguaglio.getTipiRapporto().iterator();i.hasNext();){
			Tipo_rapportoBulk tipoRapp = (Tipo_rapportoBulk)i.next();
			if (tipoRapp.getCd_tipo_rapporto().equals(tipoRapporto.getCd_tipo_rapporto()))
				conguaglio.setTipoRapporto(tipoRapp);
		}
	}
}
/**
 * Ripristina la selezione del vecchio Tipo Trattamento
 * 
**/
public void ripristinaSelezioneTipoTrattamento(){

	ConguaglioBulk conguaglio= (ConguaglioBulk)getModel();
	Tipo_trattamentoBulk tipoTrattamento = conguaglio.getTipoTrattamento();

	// ripristino la selezione del Tipo Trattamento
	if (tipoTrattamento!=null){
		for (java.util.Iterator i = conguaglio.getTipiTrattamento().iterator();i.hasNext();){
			Tipo_trattamentoBulk tratt = (Tipo_trattamentoBulk)i.next();
			if (tratt.getCd_trattamento().equals(tipoTrattamento.getCd_trattamento()))
				conguaglio.setTipoTrattamento(tratt);
		}
	}
}
/**
  * Significato codici di errore:
  *	-> 0 nessun errore
  * -> 1 non è selezionato il TERZO
  * -> 2 il TERZO non è valido
  * -> 3 non è stato selezionato il TIPO RAPPORTO 
  * -> 4 il TIPO RAPPORTO non è valido
  * -> 5 non è stato selezionato il TIPO TRATTAMENTO
  * -> 6 il TIPO TRATTAMENTO non è valido
  *
**/
public int validaTerzo(ActionContext context, boolean aBool) throws BusinessProcessException {

	try{

		ConguaglioComponentSession sess = (ConguaglioComponentSession)createComponentSession();
		return sess.validaTerzo(context.getUserContext(), (ConguaglioBulk)getModel(), aBool);

	}catch(it.cnr.jada.comp.ComponentException ex){
		throw handleException(ex);
	}catch(java.rmi.RemoteException ex){
		throw handleException(ex);
	}
}
public boolean isGestiteDeduzioniIrpef(UserContext userContext) throws BusinessProcessException {
	try{
		ConguaglioComponentSession sess = (ConguaglioComponentSession)createComponentSession();
		return sess.isGestiteDeduzioniIrpef(userContext);
	}catch(it.cnr.jada.comp.ComponentException ex){
		throw handleException(ex);
	}catch(java.rmi.RemoteException ex){
		throw handleException(ex);
	}
}
public boolean isGestiteDetrazioniFamily(UserContext userContext) throws BusinessProcessException {
	try{
		ConguaglioComponentSession sess = (ConguaglioComponentSession)createComponentSession();
		return sess.isGestiteDetrazioniFamily(userContext);
	}catch(it.cnr.jada.comp.ComponentException ex){
		throw handleException(ex);
	}catch(java.rmi.RemoteException ex){
		throw handleException(ex);
	}
}
public void doValidaDatiEsterni(ActionContext context) throws ApplicationException, BusinessProcessException
{
	boolean esistonoDatiEsterni;
	ConguaglioBulk conguaglio= (ConguaglioBulk)getModel();
	if(conguaglio.getIm_addcom_esterno()==null)
		conguaglio.setIm_addcom_esterno(new BigDecimal(0));
	if(conguaglio.getIm_addprov_esterno()==null)
		conguaglio.setIm_addprov_esterno(new BigDecimal(0));
	if(conguaglio.getIm_addreg_esterno()==null)
		conguaglio.setIm_addreg_esterno(new BigDecimal(0));
	if(conguaglio.getIm_irpef_esterno()==null)
		conguaglio.setIm_irpef_esterno(new BigDecimal(0));
	if(conguaglio.getImponibile_fiscale_esterno()==null)
		conguaglio.setImponibile_fiscale_esterno(new BigDecimal(0));
	if(conguaglio.getDetrazioni_co_esterno()==null)
		conguaglio.setDetrazioni_co_esterno(new BigDecimal(0));
	if(conguaglio.getDetrazioni_fi_esterno()==null)
		conguaglio.setDetrazioni_fi_esterno(new BigDecimal(0));
	if(conguaglio.getDetrazioni_al_esterno()==null)
		conguaglio.setDetrazioni_al_esterno(new BigDecimal(0));
	if(conguaglio.getDetrazioni_pe_esterno()==null)
		conguaglio.setDetrazioni_pe_esterno(new BigDecimal(0));
	if(conguaglio.getDetrazioni_rid_cuneo_esterno()==null)
		conguaglio.setDetrazioni_rid_cuneo_esterno(new BigDecimal(0));
	if (
	   (conguaglio.getCodice_fiscale_esterno()!= null ||
	    conguaglio.getDt_da_competenza_esterno()!= null ||
	    conguaglio.getDt_a_competenza_esterno() != null ||
	    (conguaglio.getImponibile_fiscale_esterno()!= null &&
	     conguaglio.getImponibile_fiscale_esterno().compareTo(new BigDecimal(0))>0))
	    &&
	    (conguaglio.getCodice_fiscale_esterno()== null ||
	     conguaglio.getDt_da_competenza_esterno()== null ||
	     conguaglio.getDt_a_competenza_esterno() == null ||
	     conguaglio.getImponibile_fiscale_esterno()== null ||
	     conguaglio.getImponibile_fiscale_esterno().compareTo(new BigDecimal(0))==0)
	    ) 
		throw new it.cnr.jada.comp.ApplicationException("Se si devono inserire i Dati Esterni, sono obbligatori il Cod. Fiscale del datore di lavoro, la competenza e l'imponibile fiscale. In caso contrario, ripulire i dati inseriti.");
	
	    if (conguaglio.getCodice_fiscale_esterno()!= null)
	    	esistonoDatiEsterni=new Boolean(true);
	    else
	    	esistonoDatiEsterni=new Boolean(false);
	    
	    if (esistonoDatiEsterni)
	    {
	    	//Controllo correttezza codice fiscale/partita IVA
	    	if (conguaglio.getCodice_fiscale_esterno().length() != 16 &&
	    		conguaglio.getCodice_fiscale_esterno().length() != 11)
	    		throw new it.cnr.jada.comp.ApplicationException("Dati Esterni: Codice fiscale/Partita IVA Datore di lavoro errato.");
	    	if (conguaglio.getCodice_fiscale_esterno().length() == 16 &&
	    	    !it.cnr.contab.anagraf00.util.CodiceFiscaleControllo.checkCC(conguaglio.getCodice_fiscale_esterno().toUpperCase()))
	    		throw new it.cnr.jada.comp.ApplicationException("Dati Esterni: Codice fiscale Datore di lavoro errato.");
	    	if (conguaglio.getCodice_fiscale_esterno().length() == 11)
				try {
					it.cnr.contab.anagraf00.util.PartitaIVAControllo.parsePartitaIVA(conguaglio.getCodice_fiscale_esterno());
				} catch (ExPartitaIVA e) {
		    		throw new it.cnr.jada.comp.ApplicationException("Dati Esterni: Codice fiscale/Partita IVA Datore di lavoro errata.");
				}
			/*	
		    if (conguaglio.getDetrazioni_pe_esterno()!=null &&
		    	conguaglio.getDetrazioni_pe_esterno().compareTo(new BigDecimal(0))>0 &&	
		    	conguaglio.getNumero_giorni_esterno()== null)
		    {
		    	throw new it.cnr.jada.comp.ApplicationException("Dati Esterni: Poichè sono valorizzate le detrazioni personali, il numero dei giorni di spettanza è obbligatorio.");
		    }
		    */
		    if (conguaglio.getNumero_giorni_esterno()!= null &&
		    	conguaglio.getNumero_giorni_esterno().compareTo(new Long(365))>0)
		    {
		    	throw new it.cnr.jada.comp.ApplicationException("Dati Esterni: Il numero dei giorni di spettanza non può essere superiore a 365.");
		    }
			try {
				ConguaglioComponentSession sess = (ConguaglioComponentSession)createComponentSession();
				sess.validaAltriDatiEsterni(context.getUserContext(),conguaglio);
			} catch (ComponentException e) {
				throw handleException(e);
			} catch (RemoteException e) {
				throw handleException(e);
			}
	    }
	    if (conguaglio.getIm_irpef_esterno().compareTo(new BigDecimal(0))>0 &&
	    	conguaglio.getImponibile_fiscale_esterno().compareTo(new BigDecimal(0))== 0)
	    {
	    	throw new it.cnr.jada.comp.ApplicationException("Dati Esterni: Per valorizzare la Ritenuta irpef netta occorre valorizzare anche l'Imponibile fiscale.");	    	
	    }
	    if (conguaglio.getIm_irpef_esterno().compareTo(new BigDecimal(0))==0 &&
	    	 (conguaglio.getIm_addreg_esterno().compareTo(new BigDecimal(0))!=0 ||
	    	  conguaglio.getIm_addcom_esterno().compareTo(new BigDecimal(0))!=0) )
	    {
	    	throw new it.cnr.jada.comp.ApplicationException("Dati Esterni: Non è possibile valorizzare le Addizionali se non è presente la Ritenuta irpef netta.");	    	
	    }	
	    if (conguaglio.getIm_addcom_esterno().compareTo(new BigDecimal(0))!=0 &&
	    	conguaglio.getComune_esterno()==null)
	    {
	    	throw new it.cnr.jada.comp.ApplicationException("Dati Esterni: E' obbligatorio inserire il Comune per la relativa Addizionale.");
	    }
	    if ((conguaglio.getDetrazioni_fi_esterno().add(conguaglio.getDetrazioni_co_esterno()).add(conguaglio.getDetrazioni_rid_cuneo_esterno()).add(conguaglio.getDetrazioni_al_esterno()).add(conguaglio.getIm_irpef_esterno())).compareTo(conguaglio.getImponibile_fiscale_esterno())>0)
		{
		    	throw new it.cnr.jada.comp.ApplicationException("Dati Esterni: La somma delle detrazioni e della Ritenuta irpef non può essere superiore all'Imponibile fiscale.");
		}
}
public String doVerificaIncoerenzaCarichiFam(ActionContext context) throws BusinessProcessException
{
	try {
		ConguaglioBulk conguaglio= (ConguaglioBulk)getModel();
		ConguaglioComponentSession sess = (ConguaglioComponentSession)createComponentSession();
		return sess.verificaIncoerenzaCarichiFam(context.getUserContext(),conguaglio);
	
	} catch (ComponentException e) {
		throw handleException(e);
	} catch (RemoteException e) {
		throw handleException(e);
	}
}

}
