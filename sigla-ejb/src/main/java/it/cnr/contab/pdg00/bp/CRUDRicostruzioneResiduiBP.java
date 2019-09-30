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
 * Created on Apr 4, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.pdg00.bp;

import java.rmi.RemoteException;

import javax.ejb.RemoveException;

import it.cnr.contab.chiusura00.ejb.RicercaDocContComponentSession;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.pdg00.bulk.Pdg_residuoBulk;
import it.cnr.contab.pdg00.bulk.Pdg_residuo_detBulk;
import it.cnr.contab.pdg00.comp.RicostruzioneResiduiComponent;
import it.cnr.contab.pdg00.ejb.RicostruzioneResiduiComponentSession;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcess;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.bulk.FillException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.CRUDBP;
import it.cnr.jada.util.action.SelezionatoreListaBP;
import it.cnr.jada.util.action.SimpleCRUDBP;
import it.cnr.jada.util.action.SimpleDetailCRUDController;
import it.cnr.jada.util.ejb.EJBCommonServices;
import it.cnr.jada.util.jsp.Button;

/**
 * @author mincarnato
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CRUDRicostruzioneResiduiBP extends it.cnr.jada.util.action.SimpleCRUDBP {
	private RicosResiduiCRUDController crudDettagli = new RicosResiduiCRUDController("Dettagli", Pdg_residuo_detBulk.class, "dettagli", this);
	private CdrBulk cdr;
	private CdrBulk cdrUo;
	private boolean cdrSAC;
	private boolean uoScrivaniaEnte;
	private boolean ribaltato;
	
	public CRUDRicostruzioneResiduiBP() {
		super("Tn");
	}

	public CRUDRicostruzioneResiduiBP(String function) {
		super("Tn"+function);
	}

	public final SimpleDetailCRUDController getCrudDettagli() {
		return crudDettagli;
	}

	public final RicosResiduiCRUDController getResiduiCrudDettagli() {
		return crudDettagli;
	}

	protected void resetTabs(it.cnr.jada.action.ActionContext context) {
		setTab("tab","tabTestata");
	}
	
	protected void initialize(ActionContext context) throws BusinessProcessException {
		try
		{
			setUoScrivaniaEnte(((RicostruzioneResiduiComponentSession)createComponentSession()).isUOScrivaniaEnte(context.getUserContext()));
			setCdrUo(((RicostruzioneResiduiComponentSession)createComponentSession()).findCdrUo(context.getUserContext()));
			cercaCdr(context);
			setCdrSAC(isCdrSAC(context, getCdr()));
		} catch(Exception e) {
			throw handleException(e);
		} 
		super.initialize(context);
		setRibaltato(initRibaltato(context));
		if (isRibaltato() && !isUoScrivaniaEnte()) {
			throw handleException( new ApplicationException("Non è possibile utilizzare questa funzione perchè è stato effettuato il ribaltamento complessivo dei documenti contabili per il CDS "+CNRUserContext.getCd_cds(context.getUserContext())));
		}
		// dal 2006 non si deve usare più questa funzione (è brutto questo controllo così, ma non importa
		if (((CNRUserContext)context.getUserContext()).getEsercizio().compareTo(new Integer(2006))>=0) {
			throw handleException( new ApplicationException("Funzione non utilizzabile per questo esercizio contabile."));
		}
		getCrudDettagli().setMultiSelection(false);
		try
		{
			cerca(context);
		} catch(Exception e) {
			throw handleException(e);
		}
		if ((Pdg_residuoBulk)getModel() != null && ((Pdg_residuoBulk)getModel()).getCd_centro_responsabilita() != null) {
			CdrBulk cdr = new CdrBulk(((Pdg_residuoBulk)getModel()).getCd_centro_responsabilita());
			if (isRibaltato(context,cdr))
				throw handleException( new ApplicationException("Non è possibile utilizzare questa funzione perchè è stato effettuato il ribaltamento complessivo dei documenti contabili per il CDS a cui afferisce il CdR selezionato"));
		}
	}

	public it.cnr.jada.bulk.OggettoBulk initializeModelForEdit(ActionContext context,it.cnr.jada.bulk.OggettoBulk bulk) throws BusinessProcessException 
	{
		Pdg_residuoBulk residuo = (Pdg_residuoBulk)super.initializeModelForEdit(context,bulk);	
		residuo.setStatiKeys(residuo.STATI_COMPLETO);
		return residuo;
	}
	public it.cnr.jada.bulk.OggettoBulk initializeModelForFreeSearch(ActionContext context,it.cnr.jada.bulk.OggettoBulk bulk) throws BusinessProcessException 
	{
		Pdg_residuoBulk residuo = (Pdg_residuoBulk)super.initializeModelForFreeSearch(context,bulk);	
		residuo.setStatiKeys(residuo.STATI_COMPLETO);
		return residuo;
	}
	public it.cnr.jada.bulk.OggettoBulk initializeModelForInsert(ActionContext context,it.cnr.jada.bulk.OggettoBulk bulk) throws BusinessProcessException 
	{
		Pdg_residuoBulk residuo = (Pdg_residuoBulk)super.initializeModelForInsert(context,bulk);	
		residuo.setStatiKeys(residuo.STATI_CREAZIONE);
		residuo.setStato(residuo.STATO_APERTO);
		try {
			if (getCdr()!=null&&residuo.getCd_centro_responsabilita()!=null) {
				residuo.setCentro_responsabilita(getCdr());
				completeSearchTool(context,residuo,this.getBulkInfo().getFieldProperty("centro_responsabilita"));
				if (!isCdrSAC(context, getCdr()))
					residuo = calcolaMassaSpendibile(context, residuo, getCdr());
			}
		} catch (ValidationException e) {
			throw new BusinessProcessException(e);
		}
		return residuo;
	}
	public it.cnr.jada.bulk.OggettoBulk initializeModelForSearch(ActionContext context,it.cnr.jada.bulk.OggettoBulk bulk) throws BusinessProcessException 
	{
		Pdg_residuoBulk residuo = (Pdg_residuoBulk)super.initializeModelForSearch(context,bulk);	
		residuo.setStatiKeys(residuo.STATI_COMPLETO);
		return residuo;
	}

	/**
	 * ricerca il CdR del residuo, impostato in fase di inizializzazione della mappa,
	 * che verrà utilizzato per valorizzare la chiave della testata
	 * 
	 * @return
	 */	
	public void cercaCdr(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		try 
		{
			setCdr(((RicostruzioneResiduiComponentSession)createComponentSession()).findCdr(context.getUserContext()));
		} catch(Exception e) {
			throw handleException(e);
		} 
	}

	/**
	 * getter del CdR del residuo, impostato in fase di inizializzazione della mappa,
	 * che verrà utilizzato per valorizzare la chiave della testata
	 * questo rappresenta il CdR di primo livello associato alla uo selezionata
	 * a meno che la uo non sia della SAC, in tal caso viene impostato con il CdR di secondo livello
	 * 
	 * @return
	 */
	public CdrBulk getCdr() {
		return cdr;
	}

	/**
	 * setter del CdR del residuo, impostato in fase di inizializzazione della mappa,
	 * che verrà utilizzato per valorizzare la chiave della testata
	 * questo rappresenta il CdR di primo livello associato alla uo selezionata
	 * a meno che la uo non sia della SAC, in tal caso viene impostato con il CdR di secondo livello
	 * 
	 * @return
	 */
	public void setCdr(CdrBulk bulk) {
		cdr = bulk;
	}

	public boolean isAbilitatoMassaSpendibile() {
		if (isUoScrivaniaEnte())
			return true;

		return false;
	}

	public boolean isAbilitatoModificaStatoTestata() {
		if (isUoScrivaniaEnte())
			return true;

		if (getCdr()!=null&&getCdr().getCd_centro_responsabilita().equals(getCdrUo().getCd_centro_responsabilita()))
			return true;

		return false;
	}

	public boolean isAbilitatoModificaDettagli() {
		Pdg_residuoBulk res = (Pdg_residuoBulk)getModel();

		if (res == null || res.getStato() == null) 
			return false;
		
		if (!isUoScrivaniaEnte() && !res.getStato().equals(Pdg_residuoBulk.STATO_CHIUSO))
			return true;
		
		return false;
	}
	/**
	 * @return
	 */
	public boolean isUoScrivaniaEnte() {
		return uoScrivaniaEnte;
	}

	/**
	 * @param b
	 */
	public void setUoScrivaniaEnte(boolean b) {
		uoScrivaniaEnte = b;
	}

	/**
	 * Il metodo stabilisce se le regole di modificabilità dei campi in base all'uo, cds e cdr
	 * ed allo s
	 * 
	 */
	public void basicEdit(it.cnr.jada.action.ActionContext context,it.cnr.jada.bulk.OggettoBulk bulk, boolean doInitializeForEdit) throws it.cnr.jada.action.BusinessProcessException 
	{
		super.basicEdit(context, bulk, doInitializeForEdit);
		impostaCampi(context); 
	}

	public void reset(ActionContext context) throws BusinessProcessException 
	{
		super.reset(context);
		impostaCampi(context); 
	}

	/**
	 * Metodo per impostare la navigabilità dei campi
	 * 
	 * @param context
	 * @param bulk
	 */
	public void impostaCampi(it.cnr.jada.action.ActionContext context) throws BusinessProcessException 
	{
		Pdg_residuoBulk residuo = (Pdg_residuoBulk)getModel();
		
		if (getStatus()!=VIEW && residuo != null)
		{
			if(residuo.getStato().equals(residuo.STATO_CHIUSO)&&!isAbilitatoModificaStatoTestata())
			{
				setStatus(VIEW);
				setEditable(false);
			}

			// settiamo l'abilitazione del campo massa spendibile
			if (residuo.getStato().equals(residuo.STATO_CHIUSO))
				residuo.setAbilitaModificaMassaSpendibile(false);
			else
				residuo.setAbilitaModificaMassaSpendibile(isAbilitatoMassaSpendibile());
			// settiamo l'abilitazione del campo stato
			residuo.setAbilitaModificaStatoTestata(isAbilitatoModificaStatoTestata());
			// settiamo lo statikeys
			if (residuo.getStato().equals(residuo.STATO_APERTO))
				residuo.setStatiKeys(residuo.STATI_APERTO);
			else if (residuo.getStato().equals(residuo.STATO_CHIUSO))
				residuo.setStatiKeys(residuo.STATI_MODIFICA);
			else if (residuo.getStato().equals(residuo.STATO_MODIFICA))
				residuo.setStatiKeys(residuo.STATI_MODIFICA);
		
			Pdg_residuo_detBulk dett = (Pdg_residuo_detBulk) getCrudDettagli().getModel();
		}
	}

	public Pdg_residuoBulk calcolaMassaSpendibile(it.cnr.jada.action.ActionContext context, Pdg_residuoBulk residuo, CdrBulk cdr) throws BusinessProcessException {
		try {
			return ((RicostruzioneResiduiComponentSession)createComponentSession()).calcolaDispCassaPerCds(context.getUserContext(), residuo);
		} catch(Exception e) {
			throw handleException(e);
		} 
	}

	public boolean isCdrSAC(it.cnr.jada.action.ActionContext context, CdrBulk cdr) throws BusinessProcessException {
		try {
			return ((RicostruzioneResiduiComponentSession)createComponentSession()).isCdrSAC(context.getUserContext(), cdr);
		} catch(Exception e) {
			throw handleException(e);
		} 
	}

	public boolean isDettaglioReadonly() {
		if (!isAbilitatoModificaDettagli())
			return true;
		
		Pdg_residuo_detBulk dett = (Pdg_residuo_detBulk) getCrudDettagli().getModel(); 
		if (dett!=null&&dett.getStato().equals(dett.STATO_ANNULLATO))
			return true;
		else
			return false;
	}
	
	public boolean isDettaglioShrinkable()
	{
		Pdg_residuo_detBulk dett = (Pdg_residuo_detBulk) getCrudDettagli().getModel(); 
		if (dett!=null&&dett.isToBeCreated())
			return true;
		return false;
	}

	/**
	 * getter del CdR associato alla uo in scrivania
	 * 
	 * @return
	 */
	public CdrBulk getCdrUo() {
		return cdrUo;
	}

	/**
	 * setter del CdR associato alla uo in scrivania
	 * 
	 * @return
	 */
	public void setCdrUo(CdrBulk bulk) {
		cdrUo = bulk;
	}

	public boolean isDeleteButtonHidden()
	{
		return true;
	}

	public boolean isNewButtonEnabled() 
	{
		return (isUoScrivaniaEnte()||isCdrSAC());	
	}

	public void cerca(ActionContext actioncontext)
		throws RemoteException, InstantiationException, RemoveException, BusinessProcessException
	{
		try
		{
			fillModel(actioncontext);
			OggettoBulk oggettobulk = getModel();
			RemoteIterator remoteiterator = find(actioncontext, null, oggettobulk);
			if(remoteiterator == null || remoteiterator.countElements() == 0)
			{
				EJBCommonServices.closeRemoteIterator(actioncontext, remoteiterator);
				return;
			}
			if(remoteiterator.countElements() == 1)
			{
				OggettoBulk oggettobulk1 = (OggettoBulk)remoteiterator.nextElement();
				EJBCommonServices.closeRemoteIterator(actioncontext, remoteiterator);
				if(oggettobulk1 != null) {
					edit(actioncontext, oggettobulk1);
				}
				return;
			}
			else {
				EJBCommonServices.closeRemoteIterator(actioncontext, remoteiterator);
				setStatus(SEARCH);
			}
		}
		catch(Throwable throwable)
		{
			throw handleException(throwable);
		}
	}
	
	public void caricaDettagliFiltrati(ActionContext context) throws BusinessProcessException {
		try {
			setModel(context,((RicostruzioneResiduiComponentSession)createComponentSession()).caricaDettagliFiltrati(context.getUserContext(), getResiduiCrudDettagli().getFilter(), (Pdg_residuoBulk) getModel()));
			impostaCampi(context);
		} catch(Exception e) {
			throw handleException(e);
		} 		
	}

	/**
	 * getter della proprietà cdrSAC, impostata a true se il campo "Cdr" di questo BP è di tipo SAC
	 * 
	 * @return
	 */
	public boolean isCdrSAC() {
		return cdrSAC;
	}

	/**
	 * setter della proprietà cdrSAC, impostata a true se il campo "Cdr" di questo BP è di tipo SAC
	 * 
	 * @param b
	 */
	private void setCdrSAC(boolean b) {
		cdrSAC = b;
	}

	public boolean initRibaltato(it.cnr.jada.action.ActionContext context)  throws it.cnr.jada.action.BusinessProcessException
	{
		try 
		{
			return (((RicercaDocContComponentSession)createComponentSession("CNRCHIUSURA00_EJB_RicercaDocContComponentSession", RicercaDocContComponentSession.class)).isRibaltato(context.getUserContext()));
		} catch(Exception e) 
		{
			throw handleException(e);
		} 
	}
	public boolean isRibaltato(it.cnr.jada.action.ActionContext context, it.cnr.contab.config00.sto.bulk.CdrBulk cdr)  throws it.cnr.jada.action.BusinessProcessException
	{
		try 
		{
			return (((RicercaDocContComponentSession)createComponentSession("CNRCHIUSURA00_EJB_RicercaDocContComponentSession", RicercaDocContComponentSession.class)).isRibaltato(context.getUserContext(),cdr));
		} catch(Exception e) 
		{
			throw handleException(e);
		} 
	}
	public boolean isRibaltato() {
		return ribaltato;
	}
	public void setRibaltato(boolean b) {
		ribaltato = b;
	}
}
