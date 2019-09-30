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
 * Created on May 5, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.pdg01.bp;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJBException;

import it.cnr.contab.config00.latt.bulk.CostantiTi_gestione;
import it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk;
import it.cnr.contab.pdg01.ejb.CRUDPdgVariazioneGestionaleComponentSession;
import it.cnr.contab.prevent00.bulk.V_assestatoBulk;
import it.cnr.contab.prevent00.bulk.V_assestato_residuoBulk;
import it.cnr.contab.util.Utility;
import it.cnr.contab.varstanz00.bulk.Var_stanz_resBulk;
import it.cnr.contab.varstanz00.ejb.VariazioniStanziamentoResiduoComponentSession;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.MessageToUser;
import it.cnr.jada.bulk.BulkInfo;
import it.cnr.jada.bulk.FillException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.PrimaryKeyHashtable;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.ejb.CRUDComponentSession;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.ObjectReplacer;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.ConsultazioniBP;
import it.cnr.jada.util.action.FormController;
import it.cnr.jada.util.action.SelectionIterator;

/**
* @author mspasiano
*
* To change the template for this generated type comment go to
* Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
*/
public class SelezionatoreAssestatoBP extends ConsultazioniBP{
	public class AssestatoReplacer extends PrimaryKeyHashtable implements ObjectReplacer {
		public Object replaceObject(Object obj) {
			if (get(obj) != null)
			  return get(obj);
			return obj;
		}
	}

	private String componentSessioneName;
	private Class bulkClass;
	private BulkInfo bulkInfo;
	private boolean editable;
	private OggettoBulk bulkCaller;
	private String tipoGestione;
	private BigDecimal importoDaRipartire;

	public final String MODALITA_INSERIMENTO_IMPORTI = "1";
	public final String MODALITA_INSERIMENTO_PERCENTUALI = "2";
	public final String MODALITA_INSERIMENTO_SEMPLICE = "3";
	public final String MODALITA_CONSULTAZIONE = "4";

	protected String modalitaMappa = this.MODALITA_CONSULTAZIONE;
	
	AssestatoReplacer assestatoReplacer = new AssestatoReplacer();
	
	public SelezionatoreAssestatoBP() 
	{
		super();
		table.setMultiSelection(true);
	}
	
	public SelezionatoreAssestatoBP( String function ) 
	{
		table.setMultiSelection(true);
		editable = function != null && function.indexOf('M') >= 0;
	}

	public SelezionatoreAssestatoBP( String function , OggettoBulk bulk, BigDecimal importoDaRipartire, String tipoGestione) 
	{
		super(function);
		table.setStatus(FormController.EDIT);
		table.setEditableOnFocus(true);
		table.setMultiSelection(true);
		table.setSingleSelection(false);
		table.setReadonly(false);
		setBulkCaller(bulk);
		setImportoDaRipartire(importoDaRipartire);
		setTipoGestione(tipoGestione);
	}
	
	/**
	 * Gestisce un comando "Conferma"
	 * @param context <code>ActionContext</code> in uso.
	 *
	 * @exception <code>BusinessProcessException</code>, <code>ValidationException</code>
	 *
	 */
	public void confirm(ActionContext context) throws it.cnr.jada.action.BusinessProcessException,ValidationException {
	}

	/**
	 * Crea la CRUDComponentSession da usare per effettuare le operazioni di CRUD
	 */
	public CRUDComponentSession createComponentSession() throws javax.ejb.EJBException,java.rmi.RemoteException {
		if (getBulkCaller() instanceof Pdg_variazioneBulk) 
			return (CRUDPdgVariazioneGestionaleComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRPDG01_EJB_CRUDPdgVariazioneGestionaleComponentSession",CRUDPdgVariazioneGestionaleComponentSession.class);
		else
			return (VariazioniStanziamentoResiduoComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRVARSTANZ00_EJB_VariazioniStanziamentoResiduoComponentSession",VariazioniStanziamentoResiduoComponentSession.class);
	}

	/** 
	 * @param context <code>ActionContext</code> in uso.
	 *
	 * @return <code>OggettoBulk</code>
	 *
	 * @exception <code>BusinessProcessException</code>
	 */
	public OggettoBulk createEmptyModelForFreeSearch(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		try {
			return createComponentSession().inizializzaBulkPerRicercaLibera(context.getUserContext(),createModel( context));
		} catch(Exception e) {
			throw handleException(e);
		}
	}

	/**
	 * @param context <code>ActionContext</code> in uso.
	 *
	 */
	public OggettoBulk createModel(ActionContext context) 
	{
		V_assestatoBulk saldo = new V_assestatoBulk();
		return saldo;
	}
	/**
	* Metodo utilizzato per creare una toolbar applicativa personalizzata.
	* @return toolbar La nuova toolbar creata
	*
	*/
	public it.cnr.jada.util.jsp.Button[] createToolbar() 
	{
		it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[7];
		int i = 0;
		toolbar[ i++ ] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.print");
		toolbar[ i++ ] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.excel");
		toolbar[ i++ ] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.save");
		toolbar[ i-1 ].setSeparator(true);
		toolbar[ i++ ] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.refresh");		
		toolbar[ i++ ] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.insertImporti");
		toolbar[ i-1 ].setSeparator(true);
		toolbar[ i++ ] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.insertPercentuali");
		toolbar[ i-1 ].setSeparator(true);
		toolbar[ i++ ] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.assegnaPercUguali");

		return toolbar;
	}

	public RemoteIterator findFreeSearch(ActionContext context, CompoundFindClause clauses, OggettoBulk model) throws BusinessProcessException {
		return find(context,clauses,model);
	}
	
	/**
	 * find method comment.
	 */
	public it.cnr.jada.util.RemoteIterator find(ActionContext context,it.cnr.jada.persistency.sql.CompoundFindClause clauses,OggettoBulk model) throws it.cnr.jada.action.BusinessProcessException {
		return find(context,clauses,model,getBulkCaller(),isGestioneSpesa()?"assestatoSpese":"assestatoEntrate");
	}

	/**
	 * find method comment.
	 */
	public it.cnr.jada.util.RemoteIterator find(ActionContext actionContext,it.cnr.jada.persistency.sql.CompoundFindClause clauses,OggettoBulk bulk,OggettoBulk context,String property) throws it.cnr.jada.action.BusinessProcessException {
		try {
			return it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator( actionContext,createComponentSession().cerca(actionContext.getUserContext(),clauses,bulk,context,property));
		} catch(Exception e) {
			throw new it.cnr.jada.action.BusinessProcessException(e);
		}
	}

	/**
	 * @return java.lang.Class
	 */
	public java.lang.Class getBulkClass() {
		return bulkClass;
	}

	/**
	 * @return it.cnr.jada.bulk.BulkInfo
	 */
	public it.cnr.jada.bulk.BulkInfo getBulkInfo() {
		return bulkInfo;
	}

	/**
	 * @return java.lang.String
	 */
	public java.lang.String getComponentSessioneName() {
		return componentSessioneName;
	}

	protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		try {
			super.init(config,context);
			setBulkClassName(config.getInitParameter("bulkClassName"));
			setComponentSessioneName(config.getInitParameter("componentSessionName"));
			setObjectReplacer(getAssestatoReplacer());
			refreshList( context );
		} catch(ClassNotFoundException e) {
			throw new RuntimeException("Non trovata la classe bulk");
		} catch(Throwable e) {
			throw new BusinessProcessException(e);
		}
	}
	public boolean isEditable() {
		return editable;
	}
	/**
	 * Ritorna TRUE se l'obbligazione e' in fase di modifica/inserimento
	 */
	public boolean isEditButtonEnabled() {
		return getSelection().getFocus() != -1;
	}
	/**
	 * @param context <code>ActionContext</code> in uso.
	 *
	 * @exception <code>BusinessProcessException</code>, <code>ValidationException</code>
	 */
	public void refreshList(ActionContext context) throws it.cnr.jada.action.BusinessProcessException,ValidationException 
	{
		try
		{
			OggettoBulk saldo = (OggettoBulk) createModel( context );
			setIterator(context,createComponentSession().cerca(context.getUserContext(),null,saldo,getBulkCaller(),isGestioneSpesa()?"assestatoSpese":"assestatoEntrate"));
			selection.clear();
		} catch(Exception e) 
		{
			throw handleException(e);
		} 
	
	}
	public OggettoBulk[] fillModels(ActionContext actioncontext) throws FillException {
		OggettoBulk aoggettobulk[] = getPageContents();
		for(int i = 0; i < aoggettobulk.length; i++)
		{
			OggettoBulk oggettobulk = aoggettobulk[i];
			if (oggettobulk.fillFromActionContext(actioncontext, "mainTable.[" + (i + getFirstElementIndexOnCurrentPage()), 2, getFieldValidationMap()))
			  setDirty(true);
		}
		return aoggettobulk;
	}

	public void aggiungiDettaglioVariazione(ActionContext actioncontext, V_assestatoBulk saldo)throws it.cnr.jada.action.BusinessProcessException{
		try {
			if (getBulkCaller() instanceof Pdg_variazioneBulk) 
			{
				CRUDPdgVariazioneGestionaleComponentSession session = (CRUDPdgVariazioneGestionaleComponentSession)createComponentSession();
				session.aggiungiDettaglioVariazione(actioncontext.getUserContext(), (Pdg_variazioneBulk)getBulkCaller(), saldo);
			}
		} catch (EJBException e) {
			throw handleException(e);
		} catch (RemoteException e) {
			throw handleException(e);
		}catch (ComponentException e) {
			throw handleException(e);
		}
	}

	public void aggiungiDettaglioVariazione(ActionContext actioncontext, V_assestato_residuoBulk saldo)throws it.cnr.jada.action.BusinessProcessException{
		try {
			if (getBulkCaller() instanceof Var_stanz_resBulk) 
			{
				VariazioniStanziamentoResiduoComponentSession session = (VariazioniStanziamentoResiduoComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRVARSTANZ00_EJB_VariazioniStanziamentoResiduoComponentSession",VariazioniStanziamentoResiduoComponentSession.class);
				session.aggiungiDettaglioVariazione(actioncontext.getUserContext(), (Var_stanz_resBulk)getBulkCaller(), saldo);
			}
		} catch (EJBException e) {
			throw handleException(e);
		} catch (RemoteException e) {
			throw handleException(e);
		}catch (ComponentException e) {
			throw handleException(e);
		}
	}

	/**
	 * Imposta il valore della proprietà 'bulkClass'
	 *
	 * @param newBulkClass	Il valore da assegnare a 'bulkClass'
	 */
	public void setBulkClass(java.lang.Class newBulkClass) {
		bulkClass = newBulkClass;
	}

	/**
	 * Imposta il valore della proprietà 'bulkClassName'
	 *
	 * @param bulkClassName	Il valore da assegnare a 'bulkClassName'
	 * @throws ClassNotFoundException	
	 */
	public void setBulkClassName(java.lang.String bulkClassName) throws ClassNotFoundException {
		bulkClass = getClass().getClassLoader().loadClass(bulkClassName);
		bulkInfo = it.cnr.jada.bulk.BulkInfo.getBulkInfo(bulkClass);
		setColumns(bulkInfo.getColumnFieldPropertyDictionary());
	}

	public void setBulkInfo(it.cnr.jada.bulk.BulkInfo newBulkInfo) {
		bulkInfo = newBulkInfo;
	}
	/**
	 * Imposta il valore della proprietà 'componentSessioneName'
	 *
	 * @param newComponentSessioneName	Il valore da assegnare a 'componentSessioneName'
	 */
	public void setComponentSessioneName(java.lang.String newComponentSessioneName) {
		componentSessioneName = newComponentSessioneName;
	}
	public void setEditable(boolean newEditable) {
		editable = newEditable;
	}

	public String getFormTitle(){
	   String title = BulkInfo.getBulkInfo(V_assestatoBulk.class).getLongDescription();
	   title = title.concat(" - " + (isGestioneSpesa()?"Spese":"Entrate")); 
	   if (this.getParentRoot().isBootstrap())
		   return title;
	   return "<script>document.write(\""+title+"\")</script>";
	}
	public AssestatoReplacer getAssestatoReplacer() {
		if (assestatoReplacer == null)
			setAssestatoReplacer(new AssestatoReplacer());
		return assestatoReplacer;
	}
	
	public void setAssestatoReplacer(AssestatoReplacer assestatoReplacer) {
		this.assestatoReplacer = assestatoReplacer;
	}

	public OggettoBulk getBulkCaller() {
		return bulkCaller;
	}
	
	private void setBulkCaller(OggettoBulk bulkCaller) {
		this.bulkCaller = bulkCaller;
	}

	private String getTipoGestione() {
		return tipoGestione;
	}
	
	private void setTipoGestione(String tipoGestione) {
		this.tipoGestione = tipoGestione;
	}

	public boolean isGestioneSpesa() {
		return getTipoGestione().equals(CostantiTi_gestione.TI_GESTIONE_SPESE);
	}
	
	public List getSelectedElements(ActionContext actioncontext) throws BusinessProcessException
    {
        ArrayList arraylist = new ArrayList(selection.size());
        for(SelectionIterator selectioniterator = selection.iterator(); selectioniterator.hasNext();arraylist.add(getElementAt(actioncontext, selectioniterator.nextIndex())));
        return arraylist;
	}

	public Object getElementAt(ActionContext actioncontext, int i) throws BusinessProcessException
	{
		return getAssestatoReplacer().replaceObject(super.getElementAt(actioncontext, i));
	}

	public void allineaImportiePercentuali(ActionContext actioncontext) throws it.cnr.jada.action.BusinessProcessException {
		if (isModalitaInserimentoImporti())
			allineaPercentualiSuImporti(actioncontext);
		if (isModalitaInserimentoPercentuali())
			allineaImportiSuPercentuali(actioncontext);
	}

	protected void allineaPercentualiSuImporti(ActionContext actioncontext) throws it.cnr.jada.action.BusinessProcessException {
		try	{
			BigDecimal totaleSelVoci = new BigDecimal(0), totalePrcVoci = new BigDecimal(0);
			for (Iterator s = getSelectedElements(actioncontext).iterator();s.hasNext();) 
			{
				V_assestatoBulk voceSel = (V_assestatoBulk) s.next();
				if (Utility.nvl(voceSel.getImp_da_assegnare()).compareTo(new BigDecimal(0))>0)
					totaleSelVoci = totaleSelVoci.add( Utility.nvl(voceSel.getImp_da_assegnare()) );
			}
			//Valorizzo il campo Percentuale che utilizzerò per individuare gli importi da attribuire ad ogni scadenza
			//facendo in modo che il totale percentuale sia sempre uguale a 1 (equivalente al 100%)
			if (totaleSelVoci.compareTo(Utility.ZERO)>0) {
				for (Iterator s = getSelectedElements(actioncontext).iterator();s.hasNext();) 
				{
					V_assestatoBulk voceSel = (V_assestatoBulk) s.next();
					if (!s.hasNext())
					{
						voceSel.setPrc_da_assegnare( new BigDecimal(100).subtract(totalePrcVoci));
					}
					else
					{
						voceSel.setPrc_da_assegnare(Utility.nvl(voceSel.getImp_da_assegnare()).divide(totaleSelVoci, 4, java.math.BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
						totalePrcVoci = totalePrcVoci.add( voceSel.getPrc_da_assegnare() );
					}
				}
			}
	   }catch(Throwable e) {
		   throw new BusinessProcessException(e);
	   }
	}

	protected void allineaImportiSuPercentuali(ActionContext actioncontext ) throws BusinessProcessException {
		BigDecimal totalePrcVoci = new BigDecimal(0);
		boolean allineaPercentuali = Boolean.FALSE;
		for (Iterator s = getSelectedElements(actioncontext).iterator();s.hasNext();) 
		{
			V_assestatoBulk voceSel = (V_assestatoBulk) s.next();
			
			if (Utility.nvl(voceSel.getPrc_da_assegnare()).compareTo(Utility.ZERO)>0)
				totalePrcVoci = totalePrcVoci.add( Utility.nvl(voceSel.getPrc_da_assegnare()) );
			else
				voceSel.setPrc_da_assegnare(new BigDecimal(0));

			voceSel.setImp_da_assegnare(Utility.ZERO);
			
			if (voceSel.getImporto_disponibile_netto().compareTo(importoDaRipartire.multiply(voceSel.getPrc_da_assegnare().divide(new BigDecimal(100))).setScale(2,BigDecimal.ROUND_HALF_UP))<0) {
				voceSel.setImp_da_assegnare(voceSel.getImporto_disponibile_netto());
				allineaPercentuali = Boolean.TRUE;
			}
			else
				voceSel.setImp_da_assegnare(importoDaRipartire.multiply(voceSel.getPrc_da_assegnare().divide(new BigDecimal(100))).setScale(2,BigDecimal.ROUND_HALF_UP));
		}

		//Siccome potrei aver inserito, a causa della mancanza di disponibilità, un importo non coerente con
		//la percentuale, riallineo le percentuali rispetto agli importi
		if (allineaPercentuali) allineaPercentualiSuImporti(actioncontext);

		//Valorizzo il campo Percentuale che utilizzerò per individuare gli importi da attribuire ad ogni scadenza
		if (totalePrcVoci.compareTo(new BigDecimal(100))>0)
			throw new BusinessProcessException("Il totale percentuale non deve essere superiore a 100.");
	}
	
	public String getModalitaMappa() {
		return modalitaMappa;
	}
	
	public void impostaModalitaMappa(ActionContext actioncontext, String modalitaMappa) throws it.cnr.jada.action.BusinessProcessException {
		try
		{
			BulkInfo bulkInfo = BulkInfo.getBulkInfo(V_assestatoBulk.class);
				
			if (modalitaMappa.equals(this.MODALITA_INSERIMENTO_IMPORTI) ||
			    modalitaMappa.equals(this.MODALITA_INSERIMENTO_PERCENTUALI)) {
				String val1 = ((it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession", it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession.class)).getVal01(actioncontext.getUserContext(), new Integer(0), "*", "DOCUMENTI_CONTABILI", "ATTIVA_SELEZIONE_ASSESTATO");
				if (val1!=null && val1.equals("N"))
					modalitaMappa=this.MODALITA_INSERIMENTO_SEMPLICE;
			}

			if (modalitaMappa.equals(this.MODALITA_INSERIMENTO_IMPORTI))
				setColumns(bulkInfo.getColumnFieldPropertyDictionary("modalitaInserimentoImporti"));
			else if (modalitaMappa.equals(this.MODALITA_INSERIMENTO_PERCENTUALI))
				setColumns(bulkInfo.getColumnFieldPropertyDictionary("modalitaInserimentoPercentuali"));
			else if (modalitaMappa.equals(this.MODALITA_INSERIMENTO_SEMPLICE)) {
				setMultiSelection(Boolean.TRUE);
				setColumns(bulkInfo.getColumnFieldPropertyDictionary("modalitaConsultazione"));
			}
			else if (modalitaMappa.equals(this.MODALITA_CONSULTAZIONE)) {
				setMultiSelection(Boolean.FALSE);
				setColumns(bulkInfo.getColumnFieldPropertyDictionary("modalitaConsultazione"));
			}
				
			this.modalitaMappa = modalitaMappa;
	   }catch(Throwable e) {
			throw new MessageToUser(e.getMessage());
	   }	
	}
	
	public boolean isModalitaInserimentoImporti(){
		return getModalitaMappa().equals(this.MODALITA_INSERIMENTO_IMPORTI);
	}

	public boolean isModalitaConsultazione(){
		return getModalitaMappa().equals(this.MODALITA_CONSULTAZIONE);
	}

	public boolean isModalitaInserimentoPercentuali(){
		return getModalitaMappa().equals(this.MODALITA_INSERIMENTO_PERCENTUALI);
	}

	public boolean isModalitaInserimentoSemplice(){
		return getModalitaMappa().equals(this.MODALITA_INSERIMENTO_SEMPLICE);
	}

	public boolean isModalitaInserimentoImportiButtonHidden() {
		return isModalitaConsultazione() || isModalitaInserimentoSemplice() || isModalitaInserimentoImporti();
	}

	public boolean isModalitaInserimentoPercentualiButtonHidden() {
		return isModalitaConsultazione() || isModalitaInserimentoSemplice() || isModalitaInserimentoPercentuali();
	}

	public boolean isAssegnaPercentualiUgualiButtonHidden() {
		return isModalitaConsultazione() || isModalitaInserimentoSemplice();
	}

	public boolean isRefreshButtonHidden() {
		return isModalitaConsultazione() || isModalitaInserimentoSemplice();
	}

	public boolean isConfermaButtonHidden() {
		return isModalitaConsultazione();
	}

	public void allineaSelezioneeObjectReplacer(ActionContext actioncontext) throws it.cnr.jada.action.BusinessProcessException {
		allineaImportiePercentuali(actioncontext);
		for (int i = 0; i<getElementsCount(); i++) { 
			V_assestatoBulk bulk = (V_assestatoBulk)getElementAt(actioncontext, i);
			if (!getSelectedElements(actioncontext).contains(bulk)) {
				bulk.setPrc_da_assegnare(null);
				bulk.setImp_da_assegnare(null);
			}
		}
	}

	public void assegnaPercentualiUguali(ActionContext actioncontext) throws BusinessProcessException {
		for (int i = 0; i<getElementsCount(); i++) {
			V_assestatoBulk bulk = (V_assestatoBulk)getElementAt(actioncontext, i);
			bulk.setPrc_da_assegnare(null);
			bulk.setImp_da_assegnare(null);
		}

		BigDecimal impResiduo = getImportoDaRipartire();

		List list = getSelectedElements(actioncontext);
		
		while (!list.isEmpty()&&impResiduo.compareTo(Utility.ZERO)>0) {
			BigDecimal impDaAssegnarePrevisto = impResiduo.divide(new BigDecimal(list.size()),4,BigDecimal.ROUND_DOWN).setScale(2,BigDecimal.ROUND_DOWN);
			if (impDaAssegnarePrevisto.compareTo(Utility.ZERO)==0 && impResiduo.compareTo(Utility.ZERO)>0)
				impDaAssegnarePrevisto = impResiduo.divide(new BigDecimal(list.size()),4,BigDecimal.ROUND_UP).setScale(2,BigDecimal.ROUND_UP);
				
			for (Iterator s = getSelectedElements(actioncontext).iterator();s.hasNext()&&impResiduo.compareTo(Utility.ZERO)>0&&!list.isEmpty();) 
			{
				V_assestatoBulk voceSel = (V_assestatoBulk) s.next();
	
				if (list.contains(voceSel)) {
					BigDecimal impDaAssegnareEffettivo = impDaAssegnarePrevisto;				

					if (impDaAssegnareEffettivo.compareTo(impResiduo)>0)
						impDaAssegnareEffettivo = impResiduo;
					if (impDaAssegnareEffettivo.compareTo(voceSel.getImporto_disponibile_netto())>0) {
						impDaAssegnareEffettivo = voceSel.getImporto_disponibile_netto();
						list.remove(voceSel);
					}				
					voceSel.setImp_da_assegnare(Utility.nvl(voceSel.getImp_da_assegnare()).add(impDaAssegnareEffettivo));
					getAssestatoReplacer().put(voceSel,voceSel);
					impResiduo = impResiduo.subtract(impDaAssegnareEffettivo);
				}
			}
		}
		allineaPercentualiSuImporti(actioncontext);
	}

	protected BigDecimal getImportoDaRipartire() {
		return importoDaRipartire;
	}
	
	private void setImportoDaRipartire(BigDecimal importoDaRipartire) {
		this.importoDaRipartire = importoDaRipartire;
	}

	/* 
	 * Il campo, anche se non di tabella, viene reso ordinabile in quanto il metodo doSort() dentro il BP
	 * gestisce la richiesta 
	 */
	public boolean isOrderableBy(String s) {
	    if (s.equals("importo_disponibile_netto"))
	    	return super.isOrderableBy("importo_disponibile");
		return super.isOrderableBy(s);
	}
	public int getOrderBy(String s) {
	    if (s.equals("importo_disponibile_netto"))
	    	return super.getOrderBy("importo_disponibile");
		return super.getOrderBy(s);
	}
}
