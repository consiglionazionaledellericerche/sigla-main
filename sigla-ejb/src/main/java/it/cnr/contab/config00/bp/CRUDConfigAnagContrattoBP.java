/*
 * Created on Apr 12, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.config00.bp;

import it.cnr.contab.config00.contratto.bulk.Ass_contratto_uoBulk;
import it.cnr.contab.config00.contratto.bulk.ContrattoBulk;
import it.cnr.contab.config00.ejb.ContrattoComponentSession;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.util.action.RemoteDetailCRUDController;
import it.cnr.jada.util.action.SimpleCRUDBP;
import it.cnr.jada.util.action.SimpleDetailCRUDController;

/**
 * @author mspasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CRUDConfigAnagContrattoBP extends SimpleCRUDBP {
	
	//private RemoteDetailCRUDController crudAssUO = new RemoteDetailCRUDController( "Associazione UO", Ass_contratto_uoBulk.class, "associazioneUO","CNRCONFIG00_EJB_ContrattoComponentSession", this);
	//private RemoteDetailCRUDController crudAssUODisponibili = new RemoteDetailCRUDController( "Associazione UO", Ass_contratto_uoBulk.class, "associazioneUODisponibili","CNRCONFIG00_EJB_ContrattoComponentSession", this);

	private SimpleDetailCRUDController crudAssUO = new SimpleDetailCRUDController( "Associazione UO", Ass_contratto_uoBulk.class, "associazioneUO", this);
	private SimpleDetailCRUDController crudAssUODisponibili = new SimpleDetailCRUDController( "Associazione UO Disponibili", Unita_organizzativaBulk.class, "associazioneUODisponibili", this);
	
	public CRUDConfigAnagContrattoBP()
	{
		super();
		//initAssUOTable();
	}

	public CRUDConfigAnagContrattoBP(String s)
	{
		super(s);
		//initAssUOTable();
	}
	private void initAssUOTable() {
		crudAssUO.setPaged(true);
		crudAssUO.setMultiSelection(true);
		crudAssUODisponibili.setPaged(true);
		crudAssUODisponibili.setMultiSelection(true);
	}	
	public void basicEdit(it.cnr.jada.action.ActionContext context,it.cnr.jada.bulk.OggettoBulk bulk, boolean doInitializeForEdit) throws it.cnr.jada.action.BusinessProcessException {
	
		super.basicEdit(context, bulk, doInitializeForEdit);
		if (getStatus()!=VIEW){
			ContrattoBulk contratto= (ContrattoBulk)getModel();
			if (contratto.getUnita_organizzativa() != null && contratto.getUnita_organizzativa().getCd_unita_organizzativa() != null &&
			    !contratto.getUnita_organizzativa().getCd_unita_organizzativa().equals(CNRUserContext.getCd_unita_organizzativa(context.getUserContext()))){
					setStatus(VIEW);					
					getCrudAssUO().setEnabled(false);		
					getCrudAssUODisponibili().setEnabled(false);	    	
			    }
			else{
				getCrudAssUO().setEnabled(true);  							
				getCrudAssUODisponibili().setEnabled(true);
			}
			if (contratto!=null && contratto.isCancellatoLogicamente()) {
				setStatus(VIEW);
				getCrudAssUODisponibili().setEnabled(false);
			}			
		}else{
			getCrudAssUODisponibili().setEnabled(false);
		}
	}	
	/**
	 * @return
	 */
	public SimpleDetailCRUDController getCrudAssUO() {
		return crudAssUO;
	}

	/**
	 * @param controller
	 */
	public void setCrudAssUO(SimpleDetailCRUDController controller) {
		crudAssUO = controller;
	}
	protected void resetTabs(it.cnr.jada.action.ActionContext context) {
		setTab("tab","tabTestata");
	}
	public boolean isVisualizzaDocContSpeButtonEnabled(){
		ContrattoBulk contratto = (ContrattoBulk)getModel();
		if (contratto==null)
		  return false;
		else if (contratto.getTot_doc_cont_spe()==null)
		  return false;
		else if (contratto.getTot_doc_cont_spe().compareTo(new java.math.BigDecimal(0))==0)
		  return false;
		return true;  	
	}
	public boolean isVisualizzaDocContEtrButtonEnabled(){
		ContrattoBulk contratto = (ContrattoBulk)getModel();
		if (contratto==null)
		  return false;
		else if (contratto.getTot_doc_cont_etr()==null)
		  return false;
		else if (contratto.getTot_doc_cont_etr().compareTo(new java.math.BigDecimal(0))==0)
		  return false;
		return true;  	
	}
	public boolean isVisualizzaCommessaButtonEnabled(){
		return isVisualizzaDocContSpeButtonEnabled()||isVisualizzaDocContEtrButtonEnabled();
	}	
	/**
	 * Metodo utilizzato per creare una toolbar applicativa personalizzata.
	 * @return toolbar Toolbar in uso
	 */

	protected it.cnr.jada.util.jsp.Button[] createToolbar() {

		it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[10];
		int i = 0;
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.search");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.startSearch");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.freeSearch");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.new");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.save");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.delete");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.bringBack");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.print");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.undoBringBack");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.definitiveSave");

		return toolbar;
	}
	/**
	 * Restituisce il valore della proprietà 'salvaDefinitivoButtonEnabled'
	 *
	 * @return Il valore della proprietà 'salvaDefinitivoButtonEnabled'
	 */
	public boolean isSalvaDefinitivoButtonEnabled() {

		return isEditing() && !isDirty() &&
				getModel() != null && 
				getModel().getCrudStatus() == it.cnr.jada.bulk.OggettoBulk.NORMAL &&
				((ContrattoBulk)getModel()).isProvvisorio();
	}
	/**
	 * Gestione del salvataggio definitivo di un contratto
	 *
	 * @param context	L'ActionContext della richiesta
	 * @throws BusinessProcessException	
	 */
	public void salvaDefinitivo(ActionContext context) throws it.cnr.jada.action.BusinessProcessException{
		try {

			ContrattoComponentSession comp = (ContrattoComponentSession)createComponentSession();
			ContrattoBulk contratto = comp.salvaDefinitivo(context.getUserContext(), (ContrattoBulk)getModel());
			edit(context,contratto);
		}catch(it.cnr.jada.comp.ComponentException ex){
			throw handleException(ex);
		}catch(java.rmi.RemoteException ex){
			throw handleException(ex);
		}
	}
	public void controllaCancellazioneAssociazioneUo(ActionContext context,Ass_contratto_uoBulk ass_contratto_uo) throws it.cnr.jada.action.BusinessProcessException{
		try {

			ContrattoComponentSession comp = (ContrattoComponentSession)createComponentSession();
			comp.controllaCancellazioneAssociazioneUo(context.getUserContext(), ass_contratto_uo);
		}catch(it.cnr.jada.comp.ComponentException ex){
			throw handleException(ex);
		}catch(java.rmi.RemoteException ex){
			throw handleException(ex);
		}
	}
	public void initializzaUnita_Organizzativa(ActionContext context) throws it.cnr.jada.action.BusinessProcessException{
		try {

			ContrattoComponentSession comp = (ContrattoComponentSession)createComponentSession();
			setModel(context, comp.initializzaUnita_Organizzativa(context.getUserContext(), (ContrattoBulk)getModel()));
		}catch(it.cnr.jada.comp.ComponentException ex){
			throw handleException(ex);
		}catch(java.rmi.RemoteException ex){
			throw handleException(ex);
		}
	}
	
	/**
	 * @return
	 */
	public SimpleDetailCRUDController getCrudAssUODisponibili() {
		return crudAssUODisponibili;
	}

	/**
	 * @param controller
	 */
	public void setCrudAssUODisponibili(SimpleDetailCRUDController controller) {
		crudAssUODisponibili = controller;
	}
	public boolean isDeleteButtonEnabled()
	{
		return isEditable() && isEditing();
	}
}
