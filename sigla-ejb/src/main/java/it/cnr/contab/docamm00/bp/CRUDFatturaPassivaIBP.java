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

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.jsp.PageContext;

import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.contab.compensi00.ejb.CompensoComponentSession;
import it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk;
import it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk;
import it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_rigaIBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaIBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_IBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk;
import it.cnr.contab.docamm00.ejb.FatturaPassivaComponentSession;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.action.SimpleDetailCRUDController;
import it.cnr.jada.util.jsp.JSPUtils;

/**
 * Insert the type's description here.
 * Creation date: (10/25/2001 11:20:32 AM)
 * @author: Roberto Peli
 */
public class CRUDFatturaPassivaIBP extends CRUDFatturaPassivaBP implements IDocumentoAmministrativoSpesaBP {

	public final static String SAVE_POINT_NAME = "FATTURAP_SP";

	private final FatturaPassivaRigaCRUDController dettaglio = new FatturaPassivaRigaCRUDController(
			"Dettaglio",Fattura_passiva_rigaIBulk.class,"fattura_passiva_dettColl", this){

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
					!((Fattura_passiva_rigaIBulk)getModel()).getFattura_passivaI().isDocumentoModificabile()&&
					((Fattura_passiva_rigaIBulk)getModel()).getFattura_passivaI().isDetailDoubled()) 
				getBulkInfo().writeFormInput(jspwriter, getModel(), s, s1, flag, s2, s3, getInputPrefix(), getStatus(), getFieldValidationMap(), getParentRoot().isBootstrap());
			else
				super.writeFormInput(jspwriter,s,s1,flag,s2,s3);
		}
	};
	/**
	 * CRUDFatturaPassivaIBP constructor comment.
	 */
	public CRUDFatturaPassivaIBP() {

		super(Fattura_passiva_rigaIBulk.class);
	}
	/**
	 * CRUDFatturaPassivaIBP constructor comment.
	 * @param function java.lang.String
	 * @exception it.cnr.jada.action.BusinessProcessException The exception description.
	 */
	public CRUDFatturaPassivaIBP(String function) throws it.cnr.jada.action.BusinessProcessException {
		super(function, Fattura_passiva_rigaIBulk.class);
	}
	/**
	 * Invocato per creare un modello vuoto da usare su una nuova richiesta di inserimento.
	 * L'implementazione crea un nuovo modello con <code>createNewBulk()</code>, quindi invoca 
	 * <code>initializeForInsert</code> sul nuovo oggetto e infine <code>inizializzaBulkPerInserimento</code>
	 * sulla CRUDComponentSession del ricevente
	 */
	public OggettoBulk createEmptyModel(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {

		try {
			Fattura_passiva_IBulk fatturaPassiva = (Fattura_passiva_IBulk)super.createEmptyModel(context);
			try {
				if (!isDeleting())
					((it.cnr.contab.docamm00.ejb.FatturaPassivaComponentSession)createComponentSession()).verificaEsistenzaEdAperturaInventario(context.getUserContext(), fatturaPassiva);
			} catch (it.cnr.jada.comp.ApplicationException e) {
				setMessage(e.getMessage());
			}
			return fatturaPassiva;
		} catch(Exception e) {
			throw handleException(e);
		}
	}
	protected it.cnr.jada.util.jsp.Button[] createFPInventarioToolbar() {

		it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[4];
		int i = 0;
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.inventaria");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.associaInventario");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.inventariaPerAumento");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.beni_coll");
		return toolbar;
	}
	protected it.cnr.jada.util.jsp.Button[] createFPToolbar() {

		it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[6];
		int i = 0;
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.generaNdC");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.apriNdC");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.generaNdD");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.apriNdD");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.generaCompenso");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.apriCompenso");
		return toolbar;
	}
	protected it.cnr.jada.util.jsp.Button[] createToolbar() {
		it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[12];
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
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.download");
		toolbar[i - 1].setSeparator(true);
		return toolbar;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (09/07/2001 14:55:11)
	 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
	 */
	public Accertamento_scadenzarioBulk getAccertamento_scadenziario_corrente() {

		return null;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (09/07/2001 14:55:11)
	 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
	 */
	public FatturaPassivaRigaCRUDController getDettaglio() {

		return dettaglio;
	}
	public boolean isApriNdCButtonEnabled() {

		Fattura_passiva_IBulk fp = (Fattura_passiva_IBulk)getModel();
		return !isInserting() && !isSearching() && fp != null &&
				//fp.getCrudStatus() == it.cnr.jada.bulk.OggettoBulk.NORMAL &&
				// RP 23/03/2010  commentato per permettere la generazione delle nc/nd di fatture con lettera di pagamento
				//!(fp.isEstera() && fp.getLettera_pagamento_estero() != null) &&
				!fp.isByFondoEconomale() &&
				!fp.isGenerataDaCompenso() &&
				!fp.isBollaDoganale() &&
				!fp.isSpedizioniere();
	}
	public boolean isApriNdCButtonHidden() {

		return isSearching() || isDeleting();
	}
	public boolean isApriNdDButtonEnabled() {

		Fattura_passiva_IBulk fp = (Fattura_passiva_IBulk)getModel();
		return 	!isInserting() && !isSearching() && fp != null &&
				//fp.getCrudStatus() == it.cnr.jada.bulk.OggettoBulk.NORMAL &&
				// RP 23/03/2010  commentato per permettere la generazione delle nc/nd di fatture con lettera di pagamento
				//!(fp.isEstera() && fp.getLettera_pagamento_estero() != null) &&
				!fp.isByFondoEconomale() &&
				!fp.isGenerataDaCompenso() &&
				!fp.isBollaDoganale() &&
				!fp.isSpedizioniere();
	}
	public boolean isApriNdDButtonHidden() {

		return isSearching() || isDeleting();
	}
	public boolean isAutoGenerated() {

		return false;
	}
	public boolean isFreeSearchButtonHidden() {

		return super.isFreeSearchButtonHidden() || isSpesaBP();
	}
	public boolean isGeneraNdCButtonEnabled() {

		Fattura_passiva_IBulk fp = (Fattura_passiva_IBulk)getModel();
		return 	isEditing() && fp != null &&
				//fp.getCrudStatus() == it.cnr.jada.bulk.OggettoBulk.NORMAL &&
				// RP 23/03/2010  commentato per permettere la generazione delle nc/nd di fatture con lettera di pagamento
				!(fp.isEstera() && fp.getLettera_pagamento_estero() != null && !fp.isPagata() ) &&
				!fp.isAnnullato() &&
				!fp.isCongelata() &&
				!fp.isByFondoEconomale() &&
				!fp.isGenerataDaCompenso() &&
				!fp.isBollaDoganale() &&
				!fp.isSpedizioniere();// && 
		//((isAnnoDiCompetenza() && !fp.isRiportata())   				
		// RP 16/03/2010 Da commentare per generare NC di anni precedenti
		// Gennaro Borriello - (02/11/2004 16.48.21)
		// 	Fix sul controllo dello "Stato Riportato": controlla che il documento sia stato riportato
		//	DA UN ES. PRECEDENTE a quello di scrivania. 
		//||(!isAnnoDiCompetenza() && (fp.COMPLETAMENTE_RIPORTATO.equalsIgnoreCase(fp.getRiportataInScrivania())|| fp.PARZIALMENTE_RIPORTATO.equalsIgnoreCase(fp.getRiportataInScrivania())))
		// );
	}
	public boolean isGeneraNdCButtonHidden() {

		return isSearching() || isDeleting();
	}
	public boolean isGeneraNdDButtonEnabled() {

		Fattura_passiva_IBulk fp = (Fattura_passiva_IBulk)getModel();
		return isEditing() && fp != null &&
				//fp.getCrudStatus() == it.cnr.jada.bulk.OggettoBulk.NORMAL &&
				// RP 23/03/2010  commentato per permettere la generazione delle nc/nd di fatture con lettera di pagamento
				!(fp.isEstera() && fp.getLettera_pagamento_estero() != null && !fp.isPagata()) &&
				!fp.isAnnullato() &&
				!fp.isCongelata() &&
				!fp.isByFondoEconomale() &&
				!fp.isGenerataDaCompenso() &&
				!fp.isBollaDoganale() &&
				!fp.isSpedizioniere(); // &&
		//((isAnnoDiCompetenza() && !fp.isRiportata())  				
		// Gennaro Borriello - (02/11/2004 16.48.21)
		// 	Fix sul controllo dello "Stato Riportato": controlla che il documento sia stato riportato
		//	DA UN ES. PRECEDENTE a quello di scrivania. 
		//||(!isAnnoDiCompetenza() && fp.COMPLETAMENTE_RIPORTATO.equalsIgnoreCase(fp.getRiportataInScrivania()))
		//);
	}
	public boolean isGeneraNdDButtonHidden() {

		return isSearching() || isDeleting();
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (04/06/2001 11:45:16)
	 * @return boolean
	 */
	public boolean isInputReadonlyDoc1210() {

		Fattura_passiva_IBulk fp = (Fattura_passiva_IBulk)getModel();
		it.cnr.contab.docamm00.docs.bulk.Lettera_pagam_esteroBulk lettera = fp.getLettera_pagamento_estero();
		return super.isInputReadonlyDoc1210() || isDeleting() || isModelVoided() || 
				(fp.isDoc1210Associato() && !lettera.isAnnoDiCompetenza()) ||
				(fp != null && 
				((fp.isPagata() && !isSearching()) ||
						(isAnnoDiCompetenza() && fp.isRiportata()) ||
						// Gennaro Borriello - (02/11/2004 16.48.21)
						// Fix sul controllo dello "Stato Riportato": controlla che il documento sia stato riportato 
						//	DA UN ES. PRECEDENTE a quello di scrivania.
						(!isAnnoDiCompetenza() && !fp.COMPLETAMENTE_RIPORTATO.equalsIgnoreCase(fp.getRiportataInScrivania()))));
	}
	public boolean isManualModify() {

		boolean manuallyEditable = super.isManualModify();
		if (manuallyEditable) {
			Fattura_passiva_IBulk fp = (Fattura_passiva_IBulk)getModel();
			if (fp.isDoc1210Associato()) {
				it.cnr.contab.docamm00.docs.bulk.Lettera_pagam_esteroBulk lettera = fp.getLettera_pagamento_estero();

				if (isAnnoDiCompetenza())
					manuallyEditable = !fp.isRiportata() && lettera.isAnnoDiCompetenza();
				else
					manuallyEditable = fp.COMPLETAMENTE_RIPORTATO.equals(fp.getRiportataInScrivania()) &&
					lettera.isAnnoDiCompetenza();
			}
		}
		return manuallyEditable;
	}
	public boolean isSpesaBP() {

		return getParent() != null && (getParent() instanceof it.cnr.contab.fondecon00.bp.FondoSpesaBP);
	}
	public void writeFormFieldDoc1210(javax.servlet.jsp.JspWriter out,String name) throws java.io.IOException {

		Fattura_passivaBulk fp = (Fattura_passivaBulk)getModel();


		boolean isReadonly = isInputReadonly();

		if (fp.COMPLETAMENTE_RIPORTATO.equals(fp.getRiportataInScrivania())){
			isReadonly = super.isInputReadonlyDoc1210() 
					|| isDeleting() 
					|| isModelVoided() 
					//|| (!isAnnoDiCompetenza() && isEditing()) 
					|| (fp != null && ((fp.isPagata() || fp.isCongelata()) && !isSearching()) );
		}



		//if (fp == null || !fp.COMPLETAMENTE_RIPORTATO.equalsIgnoreCase(fp.getRiportata()))
		//getBulkInfo().writeFormField(out,fp,null,name,getInputPrefix(),1,1,getStatus(), isReadonly, getFieldValidationMap());
		//else {
		//getBulkInfo().writeFormField(out,fp,null,name,getInputPrefix(),1,1,getStatus(),isInputReadonlyDoc1210(),getFieldValidationMap());
		//}

		getBulkInfo().writeFormField(out,fp,null,name,getInputPrefix(),1,1,getStatus(),isReadonly,getFieldValidationMap(), this.getParentRoot().isBootstrap());	
	}
	public void writeFormInputDoc1210(javax.servlet.jsp.JspWriter out,String name) throws java.io.IOException {

		Fattura_passivaBulk fp = (Fattura_passivaBulk)getModel();




		boolean isReadonly = isInputReadonly();

		if (!isAnnoDiCompetenza() && fp.COMPLETAMENTE_RIPORTATO.equals(fp.getRiportataInScrivania())){
			isReadonly = super.isInputReadonlyDoc1210() 
					|| isDeleting() 
					|| isModelVoided() 
					//|| (!isAnnoDiCompetenza() && isEditing()) 
					|| (fp != null && ((fp.isPagata() || fp.isCongelata()) && !isSearching()) );
		}


		//if (fp == null || !fp.COMPLETAMENTE_RIPORTATO.equalsIgnoreCase(fp.getRiportata()))
		//getBulkInfo().writeFormInput(out,fp,null,name,isInputReadonly(),null,null,getInputPrefix(),getStatus(),getFieldValidationMap());
		//else {
		//getBulkInfo().writeFormInput(out,fp,null,name,isInputReadonlyDoc1210(),null,null,getInputPrefix(),getStatus(),getFieldValidationMap());
		//}

		getBulkInfo().writeFormInput(out,fp,null,name,isReadonly,null,null,getInputPrefix(),getStatus(),getFieldValidationMap(), this.getParentRoot().isBootstrap());

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
	
	public boolean isCreaCompensoButtonEnabled() {

		Fattura_passiva_IBulk fp = (Fattura_passiva_IBulk)getModel();
		return 	/*isEditing()*/fp != null &&
				/*fp.getCrudStatus() == it.cnr.jada.bulk.OggettoBulk.NORMAL &&*/
				fp.isGestione_doc_ele() &&		
				fp.isGenerataDaCompenso() &&
				fp.getCompenso() == null && 
				((fp.isElettronica() &&  isInserting())||(!fp.isElettronica()&& !fp.isStampataSuRegistroIVA())) &&
				!fp.isAnnullato() &&
				!fp.isCongelata() &&
				!fp.isBollaDoganale() &&
				!fp.isSpedizioniere() && 
				!fp.isRiportata();

	}
	public boolean isCreaCompensoButtonHidden() {

		return isSearching() || isDeleting();
	}
	public boolean isApriCompensoButtonEnabled() {

		Fattura_passiva_IBulk fp = (Fattura_passiva_IBulk)getModel();
		return  !isInserting() && !isSearching() && fp != null &&
				fp.getCrudStatus() == it.cnr.jada.bulk.OggettoBulk.NORMAL &&
				fp.isGenerataDaCompenso() &&
				fp.getCompenso() != null;
	}
	public boolean isApriCompensoButtonHidden() {

		return isSearching() || isDeleting();
	}
	public void validaFatturaPerCompenso(ActionContext context) throws BusinessProcessException {

		try {

			Fattura_passivaBulk fp = (Fattura_passivaBulk)getModel();
			fp.aggiornaImportiTotali();
			FatturaPassivaComponentSession sess = (FatturaPassivaComponentSession)createComponentSession();
			sess.validaFatturaPerCompenso(context.getUserContext(), fp);

			setModel(context, fp);

		}catch(it.cnr.jada.comp.ComponentException ex){
			throw handleException(ex);
		}catch(java.rmi.RemoteException ex){
			throw handleException(ex);
		}
	}
	public boolean isSelezionaOrdiniButtonEnabled() {

		return (isEditing() || isInserting()) && getModel() != null
				&& !((Fattura_passivaBulk) getModel()).isGenerataDaCompenso()
				&& (isAnnoDiCompetenza());
	}

	public boolean isSelezionaOrdiniButtonHidden() {

		return isSearching() || isDeleting();
	}

}
