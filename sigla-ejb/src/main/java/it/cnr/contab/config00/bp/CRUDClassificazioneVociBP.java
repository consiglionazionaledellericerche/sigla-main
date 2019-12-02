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
 * Created on Apr 12, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.config00.bp;

import java.rmi.RemoteException;
import java.util.Enumeration;

import it.cnr.contab.config00.ejb.*;
import it.cnr.contab.config00.pdcfin.cla.bulk.*;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteBulkTree;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.FormController;
import it.cnr.jada.util.action.SimpleCRUDBP;
import it.cnr.jada.util.action.SimpleDetailCRUDController;

/**
 * @author rpagano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CRUDClassificazioneVociBP extends SimpleCRUDBP {
	private BulkList bulkTab = new BulkList();
	private int[] focusTab = {-1,-1,-1,-1,-1,-1,-1};
	private CrudAssLivelli[] crudAssLivelli = {null,null,null,null,null,null,null};
	private String[][] tabs = null;
	public Parametri_livelliBulk parametriLivelli;
			
	public CRUDClassificazioneVociBP()
	{
		super();
	}

	public CRUDClassificazioneVociBP(String s)
	{
		super(s);
	}
	
	protected void init(Config config, ActionContext actioncontext) throws BusinessProcessException {
		super.init(config, actioncontext);

		setTab("tab","tabLivello1");
		try {
			setParametriLivelli(((Classificazione_vociComponentSession)createComponentSession()).findParametriLivelli(actioncontext.getUserContext(), CNRUserContext.getEsercizio(actioncontext.getUserContext())));
			allineaDescriptionTab(getTabCorrente().intValue());
			if (this.getName().equals("ClassificazioneVociSpeseBP")) {
				tabs = new String[getParametriLivelli().getLivelli_spesa().intValue()][3];
				
				for (int i = 0; i<getParametriLivelli().getLivelli_spesa().intValue(); i++){
					tabs[i] = new String[]{ "tabLivello"+(i+1),getParametriLivelli().getDs_livello_spe(i+1),"/config00/tab_classificazione_voci.jsp" };
//					setField("cd_livello".concat((new Integer(i+1)).toString()), getParametriLivelli().getDs_livello_spe(i+1), getParametriLivelli().getLung_livello_spe(i+1).intValue(), i+1);
				}
			}
			else
			{
				tabs = new String[getParametriLivelli().getLivelli_entrata().intValue()][3];
				FieldProperty formfieldproperty;				
				for (int i = 0; i<getParametriLivelli().getLivelli_entrata().intValue(); i++){
					tabs[i] = new String[]{ "tabLivello"+(i+1),getParametriLivelli().getDs_livello_etr(i+1),"/config00/tab_classificazione_voci.jsp" };    	    			
//					setField("cd_livello".concat((new Integer(i+1)).toString()), getParametriLivelli().getDs_livello_etr(i+1), getParametriLivelli().getLung_livello_etr(i+1).intValue(), i+1);
				}
			}
		} catch(it.cnr.jada.comp.ComponentException e) {
			throw handleException(e);
		} catch(java.rmi.RemoteException e) {
			throw handleException(e);
		} catch(javax.ejb.EJBException ejbe){
			throw handleException(ejbe);
		} 
	}

	public String getLabelCd_livello1(){
		if (this.getName().equals("ClassificazioneVociSpeseBP"))
			return getParametriLivelli().getDs_livello1s();
		return getParametriLivelli().getDs_livello1e();
	}

	public String getLabelCd_livello2(){
		if (this.getName().equals("ClassificazioneVociSpeseBP"))
			return getParametriLivelli().getDs_livello2s();
		return getParametriLivelli().getDs_livello2e();
	}

	public String getLabelCd_livello3(){
		if (this.getName().equals("ClassificazioneVociSpeseBP"))
			return getParametriLivelli().getDs_livello3s();
		return getParametriLivelli().getDs_livello3e();
	}

	public String getLabelCd_livello4(){
		if (this.getName().equals("ClassificazioneVociSpeseBP"))
			return getParametriLivelli().getDs_livello4s();
		return getParametriLivelli().getDs_livello4e();
	}

	public String getLabelCd_livello5(){
		if (this.getName().equals("ClassificazioneVociSpeseBP"))
			return getParametriLivelli().getDs_livello5s();
		return getParametriLivelli().getDs_livello5e();
	}

	public String getLabelCd_livello6(){
		if (this.getName().equals("ClassificazioneVociSpeseBP"))
			return getParametriLivelli().getDs_livello6s();
		return getParametriLivelli().getDs_livello6e();
	}

	public String getLabelCd_livello7(){
		if (this.getName().equals("ClassificazioneVociSpeseBP"))
			return getParametriLivelli().getDs_livello7s();
		return getParametriLivelli().getDs_livello7e();
	}

	public void setField(String field, String label, int lung, int liv) {
		FieldProperty fieldproperty = null;
		try {
			fieldproperty = (FieldProperty)getBulkInfo().getFieldProperty(field);
			fieldproperty.setLabel(label);
			fieldproperty.setMaxLength(lung);
		} catch (NullPointerException e){}

		try {
			fieldproperty = (FieldProperty)getBulkInfo().getFormFieldProperty(field);
			fieldproperty.setLabel(label);
			fieldproperty.setMaxLength(lung);
		} catch (NullPointerException e){}

		try {
			fieldproperty = getBulkInfo().getColumnFieldProperty(field);
			fieldproperty.setLabel(label);
			fieldproperty.setMaxLength(lung);
		} catch (NullPointerException e){}

		try {
			fieldproperty = getBulkInfo().getFindFieldProperty(field);
			fieldproperty.setLabel(label);
			fieldproperty.setMaxLength(lung);
		} catch (NullPointerException e){}
		
		for(Enumeration enumeration = getBulkInfo().getFreeSearchProperties(); enumeration.hasMoreElements();)
		{
			try {
				fieldproperty = (FieldProperty)enumeration.nextElement();
				if (fieldproperty.getName().equals(field)) {
					fieldproperty.setLabel(label);
					fieldproperty.setMaxLength(lung);
				}
			} catch (NullPointerException e){}
		}
	}

	public boolean isSearchButtonHidden() {
		return super.isSearchButtonHidden() || getTabCorrente().intValue()!=1;
		}

	public boolean isFreeSearchButtonHidden() {
		return super.isFreeSearchButtonHidden() || getTabCorrente().intValue()!=1;
		}
 
 	public boolean isDeleteButtonHidden() {
		return	super.isDeleteButtonHidden() || getTabCorrente().intValue()!=1;
	}

	public boolean isNewButtonHidden() {
		return super.isNewButtonHidden() || getTabCorrente().intValue()!=1;
	}

	public Integer getTabCorrente(){
		return new Integer(getTab("tab").substring("tabLivello".length()));
	}

	public String getNameDsClassificazioniPre(int liv){
		if (((Classificazione_vociBulk)getModel()).getLivelloMax().intValue()==liv)
			return "ds_classificazione_vis";
		return "ds_class_padre_pre"+(((Classificazione_vociBulk)getModel()).getLivelloMax().intValue() - liv);
	}

	public int getLivelliVisualizzati(){
		return ((Classificazione_vociBulk)getModel()).getLivelloMax().intValue();      	
	}

	public String[][] getTabs() {
		return tabs;
	}

	public void setTabs(String[][] strings) {
		tabs = strings;
	}

	public CrudAssLivelli getCrudAssLivelli() {
		Class classController;
		if (crudAssLivelli[getTabCorrente().intValue()-1] == null) {
			if (getModel() instanceof Classificazione_voci_etr_liv7Bulk) 
				classController = Classificazione_voci_etr_liv7Bulk.class;
			else if (getModel() instanceof Classificazione_voci_etr_liv6Bulk) 
				classController = Classificazione_voci_etr_liv7Bulk.class;
			else if (getModel() instanceof Classificazione_voci_etr_liv5Bulk) 
				classController = Classificazione_voci_etr_liv6Bulk.class;
			else if (getModel() instanceof Classificazione_voci_etr_liv4Bulk) 
				classController = Classificazione_voci_etr_liv5Bulk.class;
			else if (getModel() instanceof Classificazione_voci_etr_liv3Bulk) 
				classController = Classificazione_voci_etr_liv4Bulk.class;
			else if (getModel() instanceof Classificazione_voci_etr_liv2Bulk) 
				classController = Classificazione_voci_etr_liv3Bulk.class;
			else if (getModel() instanceof Classificazione_voci_etr_liv1Bulk) 
				classController = Classificazione_voci_etr_liv2Bulk.class;
			else if (getModel() instanceof Classificazione_voci_spe_liv7Bulk) 
				classController = Classificazione_voci_spe_liv7Bulk.class;
			else if (getModel() instanceof Classificazione_voci_spe_liv6Bulk) 
				classController = Classificazione_voci_spe_liv7Bulk.class;
			else if (getModel() instanceof Classificazione_voci_spe_liv5Bulk) 
				classController = Classificazione_voci_spe_liv6Bulk.class;
			else if (getModel() instanceof Classificazione_voci_spe_liv4Bulk) 
				classController = Classificazione_voci_spe_liv5Bulk.class;
			else if (getModel() instanceof Classificazione_voci_spe_liv3Bulk) 
				classController = Classificazione_voci_spe_liv4Bulk.class;
			else if (getModel() instanceof Classificazione_voci_spe_liv2Bulk) 
				classController = Classificazione_voci_spe_liv3Bulk.class;
			else if (getModel() instanceof Classificazione_voci_spe_liv1Bulk) 
				classController = Classificazione_voci_spe_liv2Bulk.class;
			else  
				classController = Classificazione_vociBulk.class;
		
			setCrudAssLivelli(new CrudAssLivelli("ClassVociAssociate"+getTabCorrente().intValue(), classController, "classVociAssociate", this));
		}

		return crudAssLivelli[getTabCorrente().intValue()-1];
	}

	public void setCrudAssLivelli(CrudAssLivelli controller) {
		crudAssLivelli[getTabCorrente().intValue()-1] = controller;
	}

	public BulkList getBulkTab() {
		return bulkTab;
	}

	public void setBulkTab(BulkList list) {
		bulkTab = list;
	}

	public Classificazione_vociBulk getBulkTab(int tab) {
		try{
			return (Classificazione_vociBulk)bulkTab.get(tab-1);
		} catch (ArrayIndexOutOfBoundsException e){
			return null;
		}
	}

	public void setBulkTab(Classificazione_vociBulk bulk, int tab) {
		if ((getBulkTab(tab))==null)
			bulkTab.add(tab-1, bulk);
		else
			bulkTab.set(tab-1, bulk);
	}

	public int[] getFocusTab() {
		return focusTab;
	}

	public void setFocusTab(int[] is) {
		focusTab = is;
	}

	public int getFocusTab(int tab) {
		try{
			return focusTab[tab-1];
		} catch (ArrayIndexOutOfBoundsException e){
			return -1;
		}
	}

	public void setFocusTab(int is, int tab) {
		focusTab[tab-1]=is;
	}

	/*
	 * E' stato selezionato un livello diverso da quello precedentemente memorizzato (il focus del 
	 * controller è diverso da quello memorizzato nel Bulk) vengono annullati tutti i riferimenti 
	 * memorizzati nel BP ai Bulk e focus dei livelli successivi.
	 * Se si naviga dalla Tab1 il processo di annullamento avviene solo se cambia il model del BP
	 **/
	public void allineaFocusBulkTab(int intTabOld){
		if (getBulkTab(intTabOld+1) != null && 
		    ((intTabOld==Classificazione_vociHome.LIVELLO_MIN && !getModel().equalsByPrimaryKey(getBulkTab(intTabOld))) ||
			 (intTabOld!=Classificazione_vociHome.LIVELLO_MIN && intTabOld!=Classificazione_vociHome.LIVELLO_MAX && 
			  (getCrudAssLivelli().getModel() == null ||!getCrudAssLivelli().getModel().equalsByPrimaryKey(getBulkTab(intTabOld+1)))))) {
			for (int i=intTabOld+1; i<=Classificazione_vociHome.LIVELLO_MAX; i++){
				setBulkTab(null, i);
				setFocusTab(-1, i);
				try{
					crudAssLivelli[i-1].clearFilter();
				} catch (NullPointerException e){}
			}
		}
		/*
		 * Settaggio delle informazioni memorizzate sul BP relativamente ai Bulk e focus
		 */
		setBulkTab((Classificazione_vociBulk)getModel(), intTabOld);
		setFocusTab(getCrudAssLivelli().getSelection().getFocus(), intTabOld);
	}

	public void allineaDescriptionTab(int intTabNew){
		if (getModel() instanceof Classificazione_voci_etrBulk) {
			getBulkInfo().setLongDescription(parametriLivelli.getDs_livello_etr(intTabNew));
			getBulkInfo().setShortDescription(parametriLivelli.getDs_livello_etr(intTabNew));
		}
		else if (getModel() instanceof Classificazione_voci_speBulk) {
			getBulkInfo().setLongDescription(parametriLivelli.getDs_livello_spe(intTabNew));
			getBulkInfo().setShortDescription(parametriLivelli.getDs_livello_spe(intTabNew));
		}
	}

	/**
	 * 	E' stata generata la richiesta di cercare la Classificazione che sarà nodo padre della Classificazione
	 *	che si sta creando.
	 *  Il metodo restituisce un Iteratore che permette di navigare tra le Classificazioni passando
	 *	da un livello ai suoi nodi figli e viceversa. Il metodo isLeaf, permette di definire un 
	 *	"livello foglia", il livello, cioè, che non ha nodi sotto di esso.
	 *
	 * @param context la <code>ActionContext</code> che ha generato la richiesta
	 *
	 * @return <code>RemoteBulkTree</code> l'albero richiesto
	**/
	public RemoteBulkTree getClassificazioniTree(ActionContext context) throws it.cnr.jada.comp.ComponentException{
	  return
		new RemoteBulkTree() {
		  public RemoteIterator getChildren(ActionContext context, OggettoBulk bulk) throws java.rmi.RemoteException {
			try{
			  return it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context,((Classificazione_vociComponentSession)createComponentSession()).getChildren(context.getUserContext(),bulk));
			}catch(it.cnr.jada.comp.ComponentException ex){
				throw new java.rmi.RemoteException("Component Exception",ex);
			}catch(it.cnr.jada.action.BusinessProcessException ex){
				throw new java.rmi.RemoteException("BusinessProcess Exception",ex);
			}

		  }
		  public OggettoBulk getParent(ActionContext context, OggettoBulk bulk) throws java.rmi.RemoteException {
			try{
			  return ((Classificazione_vociComponentSession)createComponentSession()).getParent(context.getUserContext(),bulk);
			}catch(it.cnr.jada.comp.ComponentException ex){
				throw new java.rmi.RemoteException("Component Exception",ex);
			}catch(it.cnr.jada.action.BusinessProcessException ex){
				throw new java.rmi.RemoteException("BusinessProcess Exception",ex);
			}
		  }
	  
		  public boolean isLeaf(ActionContext context, OggettoBulk bulk) throws java.rmi.RemoteException {
			try{
			  return ((Classificazione_vociComponentSession)createComponentSession()).isLeaf(context.getUserContext(),bulk);
			}catch(it.cnr.jada.comp.ComponentException ex){
				throw new java.rmi.RemoteException("Component Exception",ex);
			}catch(it.cnr.jada.action.BusinessProcessException ex){
				throw new java.rmi.RemoteException("BusinessProcess Exception",ex);
			}
		  }
		};
	}

	public Parametri_livelliBulk getParametriLivelli(ActionContext actioncontext) throws BusinessProcessException {
		try {
			if (parametriLivelli == null)
				setParametriLivelli(((Classificazione_vociComponentSession)createComponentSession()).findParametriLivelli(actioncontext.getUserContext(), CNRUserContext.getEsercizio(actioncontext.getUserContext())));
		} catch(it.cnr.jada.comp.ComponentException e) {
			throw handleException(e);
		} catch(java.rmi.RemoteException e) {
			throw handleException(e);
		}
		return getParametriLivelli();
	}

	public Parametri_livelliBulk getParametriLivelli() {
		return parametriLivelli;
	}

	public void setParametriLivelli(Parametri_livelliBulk bulk) {
		parametriLivelli = bulk;
	}

	public class CrudAssLivelli extends SimpleDetailCRUDController {
		private CompoundFindClause filterController;

		public CrudAssLivelli(String s, Class class1, String s1, FormController formcontroller) {
			super(s, class1, s1, formcontroller, true);
		}

		public void setFilter(ActionContext actioncontext, CompoundFindClause compoundfindclause) {
			try {
				CRUDClassificazioneVociBP bp = (CRUDClassificazioneVociBP)actioncontext.getBusinessProcess(); 
				bp.setModel(actioncontext, ((Classificazione_vociComponentSession)createComponentSession()).caricaClassVociAssociate(actioncontext.getUserContext(),(Classificazione_vociBulk)bp.getModel(),compoundfindclause));
				bp.allineaFocusBulkTab(bp.getTabCorrente().intValue());
				filterController = compoundfindclause;
			} catch (DetailedRuntimeException e) {
				handleException(e);
			} catch (ComponentException e) {
				handleException(e);
			} catch (RemoteException e) {
				handleException(e);
			} catch (BusinessProcessException e) {
				handleException(e);
			}
		}
		public boolean isFiltered() {
			return filterController != null;
		}
		public CompoundFindClause getFilter()
		{
			return filterController;
		}
		protected void clearFilter(ActionContext actioncontext)
		{
			clearFilter();
		}
		protected void clearFilter()
		{
			filterController = null;
		}
		public void validate(ActionContext actioncontext) throws ValidationException {
			if (getModel()!=null)
			   getModel().validate();
			super.validate(actioncontext);
		}

		public String getLabelCd_livello1(){
			return ((CRUDClassificazioneVociBP)getParentController()).getLabelCd_livello1();
		}

		public String getLabelCd_livello2(){
			return ((CRUDClassificazioneVociBP)getParentController()).getLabelCd_livello2();
		}

		public String getLabelCd_livello3(){
			return ((CRUDClassificazioneVociBP)getParentController()).getLabelCd_livello3();
		}

		public String getLabelCd_livello4(){
			return ((CRUDClassificazioneVociBP)getParentController()).getLabelCd_livello4();
		}

		public String getLabelCd_livello5(){
			return ((CRUDClassificazioneVociBP)getParentController()).getLabelCd_livello5();
		}

		public String getLabelCd_livello6(){
			return ((CRUDClassificazioneVociBP)getParentController()).getLabelCd_livello6();
		}

		public String getLabelCd_livello7(){
			return ((CRUDClassificazioneVociBP)getParentController()).getLabelCd_livello7();
		}
	}

	public boolean isCdrAccentratoreHidden() {
		Classificazione_vociBulk cla;
		if (getTabCorrente().intValue()==1) 
			cla = (Classificazione_vociBulk)getModel();
		else
			cla = (Classificazione_vociBulk)getCrudAssLivelli().getModel();
		return cla == null || cla.getFl_accentrato() == null ||!cla.getFl_accentrato().booleanValue();
	}
}
