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

package it.cnr.contab.docamm00.bp;

import it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_IBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_rigaIBulk;
import it.cnr.contab.docamm00.docs.bulk.Nota_di_credito_attivaBulk;
import it.cnr.contab.docamm00.ejb.FatturaAttivaSingolaComponentSession;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.util.jsp.JSPUtils;

import java.rmi.RemoteException;
import java.util.Arrays;

/**
 * Insert the type's description here.
 * Creation date: (10/25/2001 11:20:32 AM)
 * @author: Roberto Peli
 */
public class CRUDFatturaAttivaIBP extends CRUDFatturaAttivaBP {

	private final FatturaAttivaRigaCRUDController dettaglio = new FatturaAttivaRigaCRUDController(
			"Dettaglio",Fattura_attiva_rigaIBulk.class,"fattura_attiva_dettColl", this) {
		/**
		 * Il metodo è stato sovrascritto per consentire all'utente di modificare la descrizione di una riga
		 * che è stata sdoppiata quando il documento non risulta essere modificabile
		 *  
		 */
		public void writeFormInput(javax.servlet.jsp.JspWriter jspwriter,String s,String s1,boolean flag,String s2,String s3) throws java.io.IOException {
			if (isInputReadonly()&&
					s1.equals("ds_riga_fattura") && 
					getModel()!=null && 
					getModel().isToBeCreated()&&
					!((Fattura_attiva_rigaIBulk)getModel()).getFattura_attivaI().isDocumentoModificabile()&&
					((Fattura_attiva_rigaIBulk)getModel()).getFattura_attivaI().isDetailDoubled()) 
				getBulkInfo().writeFormInput(jspwriter, getModel(), s, s1, flag, s2, s3, getInputPrefix(), getStatus(), getFieldValidationMap(), getParentRoot().isBootstrap());
			else
				super.writeFormInput(jspwriter,s,s1,flag,s2,s3);
		}
	};

	/**
	 * CRUDFatturaPassivaIBP constructor comment.
	 */
	public CRUDFatturaAttivaIBP() {

		super(Fattura_attiva_rigaIBulk.class);
	}
	/**
	 * CRUDFatturaPassivaIBP constructor comment.
	 * @param function java.lang.String
	 * @exception it.cnr.jada.action.BusinessProcessException The exception description.
	 */
	public CRUDFatturaAttivaIBP(String function) throws it.cnr.jada.action.BusinessProcessException {
		super(function, Fattura_attiva_rigaIBulk.class);
	}
	protected void basicEdit(it.cnr.jada.action.ActionContext context,OggettoBulk bulk,boolean doInitializeForEdit) throws it.cnr.jada.action.BusinessProcessException {
		try {
			Fattura_attivaBulk fa = (Fattura_attivaBulk)bulk;
			setAnnoDiCompetenza(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()).intValue() == fa.getEsercizio().intValue());
			super.basicEdit(context, fa, doInitializeForEdit);
		} catch(Throwable e) {
			throw new it.cnr.jada.action.BusinessProcessException(e);
		}
	}
	/**
	 * Invocato per creare un modello vuoto da usare su una nuova richiesta di inserimento.
	 * L'implementazione crea un nuovo modello con <code>createNewBulk()</code>, quindi invoca 
	 * <code>initializeForInsert</code> sul nuovo oggetto e infine <code>inizializzaBulkPerInserimento</code>
	 * sulla CRUDComponentSession del ricevente
	 */
	public OggettoBulk createEmptyModel(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		try {
			Fattura_attiva_IBulk fatturaAttiva = (Fattura_attiva_IBulk)super.createEmptyModel(context);
			try {
				if (!isDeleting())
					((it.cnr.contab.docamm00.ejb.FatturaAttivaSingolaComponentSession)createComponentSession()).verificaEsistenzaEdAperturaInventario(context.getUserContext(), fatturaAttiva);
			} catch (it.cnr.jada.comp.ApplicationException e) {
				setMessage(e.getMessage());
			}
			return fatturaAttiva;
		} catch(Exception e) {
			throw handleException(e);
		}
	}
	protected it.cnr.jada.util.jsp.Button[] createFPInventarioToolbar() {

		it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[3];
		int i = 0;
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.inventaria");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.associaInventario");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.beni_coll");
		return toolbar;
	}
	protected it.cnr.jada.util.jsp.Button[] createFPToolbar() {

		it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[4];
		int i = 0;
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.generaNdC");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.apriNdC");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.generaNdD");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.apriNdD");
		return toolbar;
	}
	protected it.cnr.jada.util.jsp.Button[] createToolbar() {
		it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[15];
		int i = 0;
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.search");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.startSearch");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.freeSearch");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.new");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.save");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.delete");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.bringBack");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.undoBringBack");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.print");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.riportaIndietro");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.riportaAvanti");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.documento");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.downloadXml");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.downloadFatturaFirmata");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.ristampa");
		toolbar = IDocAmmEconomicaBP.addPartitario(toolbar, attivaEconomicaParallela, isEditing(), getModel());
		return toolbar;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (09/07/2001 14:55:11)
	 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
	 */
	public FatturaAttivaRigaCRUDController getDettaglio() {

		return dettaglio;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (30/05/2002 11.01.22)
	 * @return it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk
	 */
	public it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk getObbligazione_scadenziario_corrente() {
		return null;
	}
	public boolean isApriNdCButtonEnabled() {

		Fattura_attiva_IBulk fa = (Fattura_attiva_IBulk)getModel();
		return !isInserting() && 
				!isSearching() && 
				fa != null && 
				fa.getCrudStatus() == it.cnr.jada.bulk.OggettoBulk.NORMAL;
	}
	public boolean isApriNdCButtonHidden() {

		return isSearching() || isDeleting();
	}
	public boolean isApriNdDButtonEnabled() {

		Fattura_attiva_IBulk fa = (Fattura_attiva_IBulk)getModel();
		return !isInserting() && 
				!isSearching() && 
				fa != null && 
				fa.getCrudStatus() == it.cnr.jada.bulk.OggettoBulk.NORMAL;
	}
	public boolean isApriNdDButtonHidden() {

		return isSearching() || isDeleting();
	}
	public boolean isAssociaInventarioButtonEnabled() {

		return	 	(isEditing()||isInserting()) && getModel() != null &&
				!getDettaglio().getDetails().isEmpty() &&
				Fattura_attivaBulk.BENEDUREVOLE.equalsIgnoreCase(((Fattura_attivaBulk)getModel()).getTi_causale_emissione());
	}
	public boolean isAssociaInventarioButtonHidden() {

		return isSearching() || isDeleting();
	}
	public boolean isAutoGenerated() {

		return false;
	}
	public boolean isGeneraNdCButtonEnabled() {

		Fattura_attivaBulk fa =	(Fattura_attivaBulk)getModel();
		return isEditing() &&
				fa != null && 
				fa.getCrudStatus() == it.cnr.jada.bulk.OggettoBulk.NORMAL &&
				!fa.isAnnullato() &&
				!fa.isCongelata();/* &&
			((isAnnoDiCompetenza() && !fa.isRiportata()) ||	 				
				// Gennaro Borriello - (02/11/2004 16.48.21)
				// 	Fix sul controllo dello "Stato Riportato": controlla che il documento sia stato riportato
				//	DA UN ES. PRECEDENTE a quello di scrivania.		
				(!isAnnoDiCompetenza() && fa.COMPLETAMENTE_RIPORTATO.equalsIgnoreCase(fa.getRiportataInScrivania())));
				 */
	}
	public boolean isGeneraNdCButtonHidden() {

		return isSearching() || isDeleting();
	}
	public boolean isGeneraNdDButtonEnabled() {

		Fattura_attivaBulk fa =	(Fattura_attivaBulk)getModel();
		return isEditing() && 
				fa != null && 
				fa.getCrudStatus() == it.cnr.jada.bulk.OggettoBulk.NORMAL &&
				!fa.isAnnullato() &&
				!fa.isCongelata();/* &&
			((isAnnoDiCompetenza() && !fa.isRiportata()) || 				
				// Gennaro Borriello - (02/11/2004 16.48.21)
				// 	Fix sul controllo dello "Stato Riportato": controlla che il documento sia stato riportato
				//	DA UN ES. PRECEDENTE a quello di scrivania.
				(!isAnnoDiCompetenza() && fa.COMPLETAMENTE_RIPORTATO.equalsIgnoreCase(fa.getRiportataInScrivania())));
				 */
	}
	public boolean isGeneraNdDButtonHidden() {

		return isSearching() || isDeleting();
	}
	public boolean isInventariaButtonEnabled() {

		return	 	(isEditing()||isInserting())  &&  getModel() != null &&
				!getDettaglio().getDetails().isEmpty() &&
				Fattura_attivaBulk.BENEDUREVOLE.equalsIgnoreCase(((Fattura_attivaBulk)getModel()).getTi_causale_emissione());
	}
	public boolean isInventariaButtonHidden() {

		return isSearching() || isDeleting();
	}
	public void writeFPInventarioToolbar(javax.servlet.jsp.JspWriter writer) throws java.io.IOException,javax.servlet.ServletException {
		if (!isSearching() && !isDeleting()) {
			if (this.getParentRoot().isBootstrap()) {
				writer.println("<!-- TOOLBAR INVENTARIO -->");
				writer.println("<div id=\"inventarioToolbar\" class=\"btn-toolbar\" role=\"toolbar\" aria-label=\"Toolbar with button groups\">");
				JSPUtils.toolbarBootstrap(writer, Arrays.asList(createFPInventarioToolbar()), this);
				writer.println("</div>");
				writer.println("<!-- FINE TOOLBAR INVENTARIO -->");				
			} else {						
				openToolbar(writer);
				it.cnr.jada.util.jsp.JSPUtils.toolbar(writer,createFPInventarioToolbar(),this, this.getParentRoot().isBootstrap());
				closeToolbar(writer);
			}

		}

	}
	public void writeFPToolbar(javax.servlet.jsp.JspWriter writer) throws java.io.IOException,javax.servlet.ServletException {
		if (!isSearching() && !isDeleting()) {
			if (this.getParentRoot().isBootstrap()) {
				writer.println("<!-- TOOLBAR FP -->");
				writer.println("<div id=\"fpToolbar\" class=\"btn-toolbar\" role=\"toolbar\" aria-label=\"Toolbar with button groups\">");				
				JSPUtils.toolbarBootstrap(writer, Arrays.asList(createFPToolbar()), this);
				writer.println("</div>");
				writer.println("<!-- FINE TOOLBAR FP -->");
			} else {			
				openToolbar(writer);
				it.cnr.jada.util.jsp.JSPUtils.toolbar(writer,createFPToolbar(),this, this.getParentRoot().isBootstrap());
				closeToolbar(writer);
			}
		}
	}
	public void writeToolbar(javax.servlet.jsp.JspWriter writer) throws java.io.IOException,javax.servlet.ServletException {

		super.writeToolbar(writer);
		writeFPToolbar(writer);
		writeFPInventarioToolbar(writer);
	}
	public boolean isBeni_collButtonEnabled() {
		Fattura_attivaBulk fattura=(Fattura_attivaBulk) getModel();
		if (fattura.getHa_beniColl()==null) 
			return false;
		else   		
			return	getModel() != null &&
			!getDettaglio().getDetails().isEmpty() &&
			(fattura.getHa_beniColl().booleanValue());
	}
	public boolean isBeni_collButtonHidden() {

		return isSearching() || isDeleting();
	}
	public Nota_di_credito_attivaBulk generaNotaCreditoAutomatica(it.cnr.jada.action.ActionContext context, Fattura_attiva_IBulk fa, Integer esercizio) throws ComponentException, PersistencyException, RemoteException, BusinessProcessException {
		FatturaAttivaSingolaComponentSession h = ((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class));
		try {
			rollbackUserTransaction();
		} catch (BusinessProcessException e) {
			throw handleException(e);
		}
		Nota_di_credito_attivaBulk nota = h.generaNotaCreditoAutomatica(context.getUserContext(), fa, esercizio);
		return nota;
	}
}