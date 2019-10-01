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
 * Created on Jan 4, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.inventario00.bp;

import java.util.BitSet;

import it.cnr.contab.docamm00.docs.bulk.Documento_generico_rigaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_rigaIBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaIBulk;
import it.cnr.contab.docamm00.docs.bulk.Nota_di_credito_rigaBulk;
import it.cnr.contab.docamm00.docs.bulk.Nota_di_debito_rigaBulk;
import it.cnr.contab.inventario00.docs.bulk.Ass_inv_bene_fatturaBulk;
import it.cnr.contab.inventario01.bulk.Buono_carico_scarico_dettBulk;
import it.cnr.contab.inventario01.ejb.BuonoCaricoScaricoComponentSession;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.BulkInfo;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.ejb.CRUDComponentSession;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.Config;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.*;
import it.cnr.jada.util.jsp.Button;

/**
 * @author mspasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ListaBuoniBP
	extends it.cnr.jada.util.action.SelezionatoreListaBP
	implements SearchProvider, SelectionListener {

	private String componentSessioneName;
	private Class bulkClass;
	private BulkInfo bulkInfo;
    private CompoundFindClause findclause;
    
	public Button[] createToolbar()
	{
		Button abutton[] = new Button[5];
		int i = 0;
		abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.print");
		abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.excel");
		abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.multiSelection");
		abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.selectAll");
		abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.freeSearch");		
		return abutton;
	}
	/* (non-Javadoc)
	 * @see it.cnr.jada.util.action.SearchProvider#search(it.cnr.jada.action.ActionContext, it.cnr.jada.persistency.sql.CompoundFindClause, it.cnr.jada.bulk.OggettoBulk)
	 */
	public RemoteIterator search(
		ActionContext actioncontext,
		CompoundFindClause compoundfindclause,
		OggettoBulk oggettobulk)
		throws BusinessProcessException {
			/*
			 * Mi conservo la findClause per poi utilizzarla
			 * nel selectAll
			 */
			setFindclause(compoundfindclause);
			return findFreeSearch(actioncontext,
			                      compoundfindclause,
			                      oggettobulk);
    }
	public it.cnr.jada.util.RemoteIterator findFreeSearch(
		ActionContext context,
		it.cnr.jada.persistency.sql.CompoundFindClause clauses,
		OggettoBulk model) 
		throws it.cnr.jada.action.BusinessProcessException {
	
		try {
			AssBeneFatturaBP assBeneFattureBP = (AssBeneFatturaBP)getParent();
			BuonoCaricoScaricoComponentSession session = (BuonoCaricoScaricoComponentSession) assBeneFattureBP.createComponentSession();
		if(!assBeneFattureBP.isDaDocumento()){
			if (assBeneFattureBP.getDettagliFattura().getSelectedModels(context).get(0) instanceof Fattura_passiva_rigaIBulk){
				Fattura_passiva_rigaIBulk modello_riga = (Fattura_passiva_rigaIBulk)assBeneFattureBP.getDettagliFattura().getSelectedModels(context).get(0);
				return session.cercaBeniAssociabili( context.getUserContext(), (Ass_inv_bene_fatturaBulk)assBeneFattureBP.getModel(),modello_riga, clauses);
			}
			else  if (assBeneFattureBP.getDettagliFattura().getSelectedModels(context).get(0) instanceof Nota_di_credito_rigaBulk){
				Nota_di_credito_rigaBulk modello_riga = (Nota_di_credito_rigaBulk)assBeneFattureBP.getDettagliFattura().getSelectedModels(context).get(0);
				return session.cercaBeniAssociabili( context.getUserContext(), (Ass_inv_bene_fatturaBulk)assBeneFattureBP.getModel(),modello_riga, clauses);
			}
			else if (assBeneFattureBP.getDettagliFattura().getSelectedModels(context).get(0) instanceof Nota_di_debito_rigaBulk){
				Nota_di_debito_rigaBulk modello_riga = (Nota_di_debito_rigaBulk)assBeneFattureBP.getDettagliFattura().getSelectedModels(context).get(0);
				return session.cercaBeniAssociabili( context.getUserContext(), (Ass_inv_bene_fatturaBulk)assBeneFattureBP.getModel(),modello_riga, clauses);
			}else {
				Fattura_attiva_rigaIBulk modello_riga = (Fattura_attiva_rigaIBulk)assBeneFattureBP.getDettagliFattura().getSelectedModels(context).get(0);
				return session.cercaBeniAssociabili( context.getUserContext(), (Ass_inv_bene_fatturaBulk)assBeneFattureBP.getModel(),modello_riga, clauses);
			}
		}
		else
			if (assBeneFattureBP.getDettagliDocumento().getSelectedModels(context).get(0) instanceof Documento_generico_rigaBulk){
				Documento_generico_rigaBulk modello_riga = (Documento_generico_rigaBulk)assBeneFattureBP.getDettagliDocumento().getSelectedModels(context).get(0);
				return session.cercaBeniAssociabili( context.getUserContext(), (Ass_inv_bene_fatturaBulk)assBeneFattureBP.getModel(),modello_riga, clauses);	
		}
			
		} catch(Exception e) {
			throw handleException(e);
		}
		return null;
	}
	/**
	 * Crea la CRUDComponentSession da usare per effettuare le operazioni di CRUD
	 */
	public CRUDComponentSession createComponentSession() throws javax.ejb.EJBException,java.rmi.RemoteException {
		return (CRUDComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB(componentSessioneName,CRUDComponentSession.class);
	}
	/**
	 * @param config it.cnr.jada.action.Config
	 * @param context Il contesto dell'azione
	 */
	protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		try {
			super.init(config,context);
			setPageSize( 20 );	
			setBulkClassName(config.getInitParameter("bulkClassName"));	
			setComponentSessioneName(config.getInitParameter("componentSessionName"));		
		}catch(Throwable e) {
			throw new BusinessProcessException(e);
		}
	}		
	/**
	 * @return
	 */
	public String getComponentSessioneName() {
		return componentSessioneName;
	}

	/**
	 * @param string
	 */
	public void setComponentSessioneName(String string) {
		componentSessioneName = string;
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
			return createComponentSession().inizializzaBulkPerRicercaLibera(context.getUserContext(),new Buono_carico_scarico_dettBulk());
		} catch(Exception e) {
			throw handleException(e);
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
	 * @param newBulkClass java.lang.Class
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
	/* (non-Javadoc)
	 * @see it.cnr.jada.util.action.SelectionListener#deselectAll(it.cnr.jada.action.ActionContext)
	 */
	public void deselectAll(ActionContext actioncontext) {}
	/* (non-Javadoc)
	 * @see it.cnr.jada.util.action.SelectionListener#getSelection(it.cnr.jada.action.ActionContext, it.cnr.jada.bulk.OggettoBulk[], java.util.BitSet)
	 */
	public BitSet getSelection(ActionContext actioncontext, OggettoBulk[] aoggettobulk, BitSet bitset) throws BusinessProcessException {
		return bitset;
	}
	/* (non-Javadoc)
	 * @see it.cnr.jada.util.action.SelectionListener#initializeSelection(it.cnr.jada.action.ActionContext)
	 */
	public void initializeSelection(ActionContext actioncontext) throws BusinessProcessException {
		try {
			AssBeneFatturaBP assBeneFattureBP = (AssBeneFatturaBP)actioncontext.getBusinessProcess();
			assBeneFattureBP.initializeSelection(actioncontext);
		}catch(javax.ejb.EJBException ejbe) {
			throw handleException(ejbe);
		}
	}
	/* (non-Javadoc)
	 * @see it.cnr.jada.util.action.SelectionListener#setSelection(it.cnr.jada.action.ActionContext, it.cnr.jada.bulk.OggettoBulk[], java.util.BitSet, java.util.BitSet)
	 */
	public BitSet setSelection(ActionContext actioncontext, OggettoBulk[] aoggettobulk, BitSet bitset, BitSet bitset1) throws BusinessProcessException {
		try {
			AssBeneFatturaBP assBeneFattureBP = (AssBeneFatturaBP)actioncontext.getBusinessProcess().getParent();
			return assBeneFattureBP.setSelection(actioncontext,aoggettobulk,bitset,bitset1);
		}catch(javax.ejb.EJBException ejbe) {
			throw handleException(ejbe);
		}
	}
	/**
	 * selectAll method comment.
	 */
	public void selectAll(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
	
		try {
			AssBeneFatturaBP assBeneFattureBP = (AssBeneFatturaBP)context.getBusinessProcess().getParent();
			assBeneFattureBP.selectAll(context,getFindclause());
		}catch(javax.ejb.EJBException e) {
			throw handleException(e);
		}
	}					
	/**
	 * @return
	 */
	public CompoundFindClause getFindclause() {
		return findclause;
	}

	/**
	 * @param clause
	 */
	public void setFindclause(CompoundFindClause clause) {
		findclause = clause;
	}

}
