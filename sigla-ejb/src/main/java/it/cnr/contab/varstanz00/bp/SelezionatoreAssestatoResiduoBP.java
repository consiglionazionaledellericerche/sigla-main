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
 * Created on Feb 24, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.varstanz00.bp;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.BitSet;

import javax.ejb.EJBException;
import javax.servlet.ServletException;
import javax.servlet.jsp.JspWriter;

import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.prevent00.bulk.V_assestato_residuoBulk;
import it.cnr.contab.varstanz00.bulk.Var_stanz_resBulk;
import it.cnr.contab.varstanz00.ejb.TransactionalVariazioniStanziamentoResiduoComponentSession;
import it.cnr.contab.varstanz00.ejb.VariazioniStanziamentoResiduoComponentSession;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.BulkInfo;
import it.cnr.jada.bulk.FillException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.ejb.CRUDComponentSession;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.Config;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.ConsultazioniBP;
import it.cnr.jada.util.action.FindBP;
import it.cnr.jada.util.action.FormController;
import it.cnr.jada.util.action.SearchProvider;
import it.cnr.jada.util.action.Selection;
import it.cnr.jada.util.action.SelectionListener;
import it.cnr.jada.util.action.SelezionatoreListaBP;
import it.cnr.jada.util.jsp.Button;
import it.cnr.jada.util.jsp.JSPUtils;

/**
 * @author mspasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SelezionatoreAssestatoResiduoBP extends ConsultazioniBP{
	private String componentSessioneName;
	private Class bulkClass;
	private BulkInfo bulkInfo;
	private boolean editable;
	private Var_stanz_resBulk var_stanz_res;
	private AssestatoResiduoReplacer assestatoReplacer = new AssestatoResiduoReplacer();
	public SelezionatoreAssestatoResiduoBP() 
	{
		super();
		table.setMultiSelection(true);
	}
	public SelezionatoreAssestatoResiduoBP( String function ) 
	{
		table.setMultiSelection(true);
		editable = function != null && function.indexOf('M') >= 0;
	}
	public SelezionatoreAssestatoResiduoBP( String function , Var_stanz_resBulk var_stanz_res) 
	{
		super(function);
		table.setStatus(FormController.EDIT);
		table.setEditableOnFocus(true);
		table.setMultiSelection(true);
		table.setSingleSelection(false);
		table.setReadonly(false);
		setVar_stanz_res(var_stanz_res);
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
	 * @throws BusinessProcessException 
	 */
	public CRUDComponentSession createComponentSession() throws javax.ejb.EJBException,java.rmi.RemoteException, BusinessProcessException {
		return (CRUDComponentSession)super.createComponentSession(componentSessioneName,VariazioniStanziamentoResiduoComponentSession.class);
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
		V_assestato_residuoBulk saldo = new V_assestato_residuoBulk();
		return saldo;
	}
	/**
 	* Metodo utilizzato per creare una toolbar applicativa personalizzata.
	* @return toolbar La nuova toolbar creata
	*
	*/
	public it.cnr.jada.util.jsp.Button[] createToolbar() 
	{
		java.util.Vector listButton = new java.util.Vector();
		listButton.addElement(new Button(Config.getHandler().getProperties(getClass()), "Toolbar.print"));
		listButton.addElement(new Button(Config.getHandler().getProperties(getClass()), "Toolbar.excel"));
		Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.save");
		button.setSeparator(true);
		listButton.addElement(button);
		Button abutton[] = new Button[listButton.size()];
		for(int i = 0;i < listButton.size();i++){
			abutton[i] = (Button)listButton.get(i);
		}		
		return abutton;

	}
	public RemoteIterator findFreeSearch(ActionContext context, CompoundFindClause clauses, OggettoBulk model) throws BusinessProcessException {
		return find(context,clauses,model);
	}
	/**
	 * find method comment.
	 */
	public it.cnr.jada.util.RemoteIterator find(ActionContext context,it.cnr.jada.persistency.sql.CompoundFindClause clauses,OggettoBulk model) throws it.cnr.jada.action.BusinessProcessException {
			return find(context,clauses,model,getVar_stanz_res(),"assestatoResiduo");
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
	/**
	 * @return editable TRUE Se l'obbligazione è editabile
	 *					FALSE in caso contrario
	 */
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
			V_assestato_residuoBulk saldo = (V_assestato_residuoBulk) createModel( context );
			setIterator(context,createComponentSession().cerca(context.getUserContext(),null,saldo,getVar_stanz_res(),"assestatoResiduo"));
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
	public void aggiungiDettaglioVariazione(ActionContext actioncontext,V_assestato_residuoBulk saldo)throws it.cnr.jada.action.BusinessProcessException{
		try {
			VariazioniStanziamentoResiduoComponentSession session =  (VariazioniStanziamentoResiduoComponentSession)createComponentSession();
			session.aggiungiDettaglioVariazione(actioncontext.getUserContext(),getVar_stanz_res(),saldo);
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
	/**
	 * @return
	 */
	public Var_stanz_resBulk getVar_stanz_res() {
		return var_stanz_res;
	}

	/**
	 * @param bulk
	 */
	public void setVar_stanz_res(Var_stanz_resBulk bulk) {
		var_stanz_res = bulk;
	}
	public String getFormTitle(){
	   String title = BulkInfo.getBulkInfo(V_assestato_residuoBulk.class).getLongDescription();
	   if (this.getParentRoot().isBootstrap())
			return title;
	   return "<script>document.write(\""+title+"\")</script>";
	}
	public AssestatoResiduoReplacer getAssestatoReplacer() {
		return assestatoReplacer;
	}
	
	public void setAssestatoReplacer(AssestatoResiduoReplacer assestatoReplacer) {
		this.assestatoReplacer = assestatoReplacer;
	}
}
