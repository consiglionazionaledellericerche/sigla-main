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

package it.cnr.contab.inventario00.actions;

import it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk;
import it.cnr.contab.inventario00.bp.CRUDInventarioBeniBP;
import it.cnr.contab.inventario00.consultazioni.bulk.V_cons_registro_inventarioBulk;
import it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk;
import it.cnr.contab.inventario00.docs.bulk.Utilizzatore_CdrVBulk;
import it.cnr.contab.inventario00.docs.bulk.V_ass_inv_bene_fatturaBulk;
import it.cnr.contab.inventario00.ejb.Inventario_beniComponentSession;
import it.cnr.contab.inventario00.tabrif.bulk.Tipo_ammortamentoBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.util.action.CRUDAction;
import it.cnr.jada.util.action.SelezionatoreListaBP;

import java.util.Iterator;

/**
 * Insert the type's description here.
 * Creation date: (16/11/2001 13.09.00)
 * @author: Roberto Fantino
 */
public class CRUDTransitoBeniAction extends CRUDAction {
/**
 * CRUDInventarioAction constructor comment.
 */
public CRUDTransitoBeniAction() {
	super();
}
/**
  * Gestisce l'aggiunta di un nuovo CDR Utilizzatore. Prima di permettere l'aggiunta
  *	di un Utilizzatore, va a fare il controllo di validità su quello attuale,
  *	e sulle linee di attività ad esso associate.
  *
  * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
  *
  * @return forward <code>Forward</code>
**/
public Forward doAddToCRUDMain_VUtilizzatori(ActionContext context) {

	try {
		fillModel(context);
		CRUDInventarioBeniBP bp = (CRUDInventarioBeniBP)getBusinessProcess(context);
		Inventario_beniBulk bene = (Inventario_beniBulk)bp.getModel();	
		
		Utilizzatore_CdrVBulk v_utitliz = (Utilizzatore_CdrVBulk)bp.getVutilizzatori().getModel();
		
		if (bene.getV_utilizzatoriColl().size()>0 && v_utitliz != null){
			if (v_utitliz.getCdr()==null || v_utitliz.getCdr().getCd_centro_responsabilita()==null)
				return handleException(context, new it.cnr.jada.bulk.ValidationException("Attenzione: specificare un CDR Utilizzatore"));

			if (v_utitliz.getPercentuale_utilizzo_cdr().compareTo(new java.math.BigDecimal(0))==0)
				return handleException(context, new it.cnr.jada.bulk.ValidationException("Attenzione: specificare una percentuale di utilizzo per il CDR Utilizzatore"));

			if (v_utitliz.getBuono_cs_utilizzatoriColl().size()>0){
				//bp.validate_Percentuali_LA(context, v_utitliz);
			}
			else{
				return handleException(context, new it.cnr.jada.bulk.ValidationException("Attenzione: specificare almeno un GAE con la relativa Percentuale di Utilizzo"));
			}
		}

		int cont = 1;
		for (Iterator i = bene.getV_utilizzatoriColl().iterator(); i.hasNext();){
			Utilizzatore_CdrVBulk v_utilizzatore = (Utilizzatore_CdrVBulk)i.next();
			
			if (v_utilizzatore.getCdr()==null || v_utilizzatore.getCdr().getCd_centro_responsabilita()==null)
				return handleException(context, new it.cnr.jada.bulk.ValidationException("Attenzione: specificare un CDR per la riga " + cont));

			if (v_utilizzatore.getPercentuale_utilizzo_cdr().compareTo(new java.math.BigDecimal(0))==0)
				return handleException(context, new it.cnr.jada.bulk.ValidationException("Attenzione: specificare una percentuale di utilizzo per il CDR Utilizzatore alla riga " + cont));

			if (v_utilizzatore.getBuono_cs_utilizzatoriColl().size()==0){
				return handleException(context, new it.cnr.jada.bulk.ValidationException("Attenzione: specificare almeno un GAE " + 
					"con la relativa Percentuale di Utilizzo per ogni Utilizzatore.\n " + 
					"Specificare un GAE per la riga " + cont));
			}
			cont++;
		}
		getController(context,"main.VUtilizzatori").add(context);
		return context.findDefaultForward();
			
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
/**
  * Gestisce il comando azzeramento del searchtool per la ricerca della Categoria Gruppo.
  *
  *  Quando si azzera il searchTool per la ricerca della Categoria Gruppo
  * azzera il Tipo Ammortamento eventualmente selezionato nel Tab Ammortamento
  *
  * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
  * @param dettaglio il <code>Inventario_beniBulk</code> bene attuale
  *
  * @return forward <code>Forward</code>
**/  
public Forward doBlankSearchFind_categoria_bene(ActionContext context, Inventario_beniBulk bene) 
	throws java.rmi.RemoteException {

	try {
		fillModel(context);
		Categoria_gruppo_inventBulk cat_gruppo= new Categoria_gruppo_inventBulk();

		if (bene.getDa_fattura()!=null ||bene.isBeneAccessorio())
			if (bene.getDa_fattura().booleanValue() ||bene.isContab().booleanValue()||bene.isMigrato()||bene.isBeneAccessorio()){
				cat_gruppo.setNodoPadre(bene.getCategoria_Bene());
				cat_gruppo.setCd_padre(bene.getCategoria_Bene().getCd_padre());
				cat_gruppo.setFl_gestione_inventario((Boolean.TRUE));
				cat_gruppo.setData_cancellazione(null);
			}
		bene.setCategoria_Bene(cat_gruppo);
		bene.setTi_ammortamenti(null);
		
		bene.setTipo_ammortamento(null);				
		return context.findDefaultForward();
		
	} catch(Exception e) {
		return handleException(context,e);
	}
	
}
/**
 * Gestisce il comando azzeramento di un searchtool
 * Quando si azzera il searchTool per la ricerca del CdR Utilizzatore,
 * azzera la Collection delle Linee di Attivita collegate
 */ 
public Forward doBlankSearchFind_cdr(ActionContext context, 
	Utilizzatore_CdrVBulk cdr_utilizzatore) 
	throws java.rmi.RemoteException {
		
	try {
		CRUDInventarioBeniBP bp = (CRUDInventarioBeniBP)getBusinessProcess(context);
				
		cdr_utilizzatore.getBuono_cs_utilizzatoriColl().clear();
		cdr_utilizzatore.setBene(null);
		cdr_utilizzatore.setCdr(new it.cnr.contab.config00.sto.bulk.CdrBulk());
		cdr_utilizzatore.setDettaglio(null);
		cdr_utilizzatore.setPercentuale_utilizzo_cdr(null);		

		bp.getUtilizzatori().reset(context);
		return context.findDefaultForward();
		
	} catch(Exception e) {
		return handleException(context,e);
	}
}
/**
  * Gestisce il risultato di una ricerca sulla Categoria Gruppo Inventario del Bene:
  *	controlla se la categoria è una Pubblicazione e setta la proprietà del BP di conseguenza.
  *
  * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
  * @param bene il <code>Inventario_beniBulk</code> bene
  * @param cat_gruppo la <code>Categoria_gruppo_inventBulk</code> categoria gruppo selezionata
  *
  * @return forward <code>Forward</code>
**/ 
public Forward doBringBackSearchFind_categoria_bene(ActionContext context, Inventario_beniBulk bene, Categoria_gruppo_inventBulk cat_gruppo) {

	try {
		fillModel(context);
		if (cat_gruppo != null){
			CRUDInventarioBeniBP bp = (CRUDInventarioBeniBP)getBusinessProcess(context);						
			String cd_pubblicazioni = null;
			
			cd_pubblicazioni = ((it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession", it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession.class)).getVal01(context.getUserContext(), new Integer(0), "*", "CD_CATEGORIA_GRUPPO_SPECIALE", "PUBBLICAZIONI");
			if(cd_pubblicazioni != null)
				//bp.setIsPubblicazione(cat_gruppo.getCd_categoria_padre().equalsIgnoreCase(cd_pubblicazioni));
				bene.setPubblicazione(cat_gruppo.getCd_categoria_padre().equalsIgnoreCase(cd_pubblicazioni));
				
			bene.setCategoria_Bene(cat_gruppo);
			
			java.util.Collection ti_ammortamenti = ((Inventario_beniComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRINVENTARIO00_EJB_Inventario_beniComponentSession", Inventario_beniComponentSession.class)).findTipiAmmortamento(context.getUserContext(), cat_gruppo);
			bene.setTi_ammortamenti(ti_ammortamenti);
			if (ti_ammortamenti.size()==1) 
				bene.setTipo_ammortamento((Tipo_ammortamentoBulk)ti_ammortamenti.iterator().next());
		
			bene.setFl_ammortamento(bene.getCategoria_Bene().getFl_ammortamento());
			if (ti_ammortamenti.size()==1) 
				bene.setTipo_ammortamento((Tipo_ammortamentoBulk)ti_ammortamenti.iterator().next());
			//if (!bp.isPubblicazione())
			if (!bene.isPubblicazione())
				bene.setCollocazione(null);
			if (bene.getCategoria_Bene()!=null &&  bene.getCategoria_Bene().getCd_categoria_gruppo()!=null &&
					!bene.getCategoria_Bene().getFl_gestione_targa() && bene.getTarga()!=null)
				bene.setTarga(null);
			if (bene.getCategoria_Bene()!=null &&  bene.getCategoria_Bene().getCd_categoria_gruppo()!=null &&
					!bene.getCategoria_Bene().getFl_gestione_seriale() && bene.getSeriale()!=null)
				bene.setSeriale(null);
			
		  }
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
	
}
/**
  *  E' stata generata la richiesta di cercare un CdR Utilizzatore.
  *	Questo metodo, gestisce il risultato della ricerca:
  *	controlla se il CdR selezionato è già stato indicato come utilizzatore del bene: in caso 
  *	affermativo, lancia un messaggio d'errore all'utente.
  *
  * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
  * @param v_Utilizzatore <code>Utilizzatore_CdrVBulk</code> l'oggetto che contiene le informazioni 
  *		relative al CdR, alle Linee di Attività ed alle percentuali di utilizzo
  * @param cdrSelezionato il <code>CdrBulk</code> CdR selezionato
  *
  * @return forward <code>Forward</code>
**/
public Forward doBringBackSearchFind_cdr(
	ActionContext context,
	Utilizzatore_CdrVBulk v_Utilizzatore,
	it.cnr.contab.config00.sto.bulk.CdrBulk cdrSelezionato) {

	try {
		//HookForward caller = (HookForward)context.getCaller();
		CRUDInventarioBeniBP bp = (CRUDInventarioBeniBP)getBusinessProcess(context);
		Inventario_beniBulk bene = (Inventario_beniBulk)bp.getModel();

		if (bene.getV_utilizzatoriColl() != null && bene.getV_utilizzatoriColl().size()>0){

			Iterator i = bene.getV_utilizzatoriColl().iterator();
			while (i.hasNext()){
				Utilizzatore_CdrVBulk v_cdr = (Utilizzatore_CdrVBulk)i.next();
				if(cdrSelezionato!= null)
				if ( v_cdr.getCdr() != null && v_cdr.getCdr().equalsByPrimaryKey(cdrSelezionato)  && i.hasNext()){
					((Utilizzatore_CdrVBulk)bene.getV_utilizzatoriColl().get(bene.getV_utilizzatoriColl().size()-1)).setCdr(new it.cnr.contab.config00.sto.bulk.CdrBulk());
					return handleException(context, new it.cnr.jada.bulk.ValidationException ("CdR Utilizzatore duplicato. Non è possibile indicare più volte uno stesso CdR come Utilizzatore"));
				}
			}
		}
		v_Utilizzatore.setCdr(cdrSelezionato);
		
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
	
/**
  * Richiamato quando l'utente seleziona il flag "Soggetto ad Ammortamento", nel
  *	tab relativo all'ammortamento.
  *
  * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
  *
  * @return forward <code>Forward</code>
**/ 
public Forward doNonAmmortizzato(ActionContext context) {	

	CRUDInventarioBeniBP bp = (CRUDInventarioBeniBP)getBusinessProcess(context);

	Inventario_beniBulk bene = (Inventario_beniBulk)bp.getModel();
	
	try{
		fillModel(context);
		if (bene !=null && bene.getFl_ammortamento()!=null && !bene.getFl_ammortamento().booleanValue()){
			bene.setTipo_ammortamento(null);
		}
		
	}
	catch (Throwable e){		
		return handleException(context,e);
	}
	
	return context.findDefaultForward();

}
/**
  * Richiamato nel caso in cui l'utente selezioni un Tipo di Ammortamento dalla select
  *	presente nel tab "Ammortamento". Il metodo setta la proprità
  *	ti_ammortamento del bene.
  *
  * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
  *
  * @return forward <code>Forward</code>
**/ 
public Forward doSelezionaTiAmmortamento(ActionContext context) {
	
	try {
		CRUDInventarioBeniBP bp = (CRUDInventarioBeniBP)getBusinessProcess(context);
		Inventario_beniBulk bene = (Inventario_beniBulk)bp.getModel();
		fillModel(context);
		if (bene.getTipo_ammortamento() != null){
			String tipo = bene.getTipo_ammortamento().getTi_ammortamento();
			bene.setTi_ammortamento(tipo);
		}
		else bene.setTi_ammortamento(null);

		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
/**
  * Permette di visualizzare i beni accessori del bene su cui si sta lavorando. 
  *	Apre un'altra finestra, (SelezionatoreBP), che ha come Iterator un Iteratore sui beni 
  *	accessori del bene che è Oggetto Model del BP.
  *
  * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
  *
  * @return forward <code>Forward</code>
**/ 
public Forward doVisualizzaBeniAccessoriFor(ActionContext context) {	

	CRUDInventarioBeniBP bp = (CRUDInventarioBeniBP)getBusinessProcess(context);

	Inventario_beniBulk bene = (Inventario_beniBulk)bp.getModel();
	
	try{	
		it.cnr.jada.util.RemoteIterator ri = ((Inventario_beniComponentSession)bp.createComponentSession()).selectBeniAccessoriFor(context.getUserContext(),bene);
		ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context,ri);
		if (ri.countElements() == 0) {
			it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context,ri);
			throw new it.cnr.jada.comp.ApplicationException("Attenzione: questo bene non ha beni accessori");
		}
		SelezionatoreListaBP nbp = (SelezionatoreListaBP)context.createBusinessProcess("Selezionatore");
		nbp.setIterator(context,ri);
		nbp.disableSelection();
		nbp.setBulkInfo(it.cnr.jada.bulk.BulkInfo.getBulkInfo(Inventario_beniBulk.class));		
		HookForward hook = (HookForward)context.findForward("seleziona");		
		return context.addBusinessProcess(nbp);		
	}
	catch (Throwable e){		
		return handleException(context,e);
	}

}

public Forward doDettagli(ActionContext context) {	

	CRUDInventarioBeniBP bp = (CRUDInventarioBeniBP)getBusinessProcess(context);

	Inventario_beniBulk bene = (Inventario_beniBulk)bp.getModel();
	
	try{	
		it.cnr.jada.util.RemoteIterator ri = ((Inventario_beniComponentSession)bp.createComponentSession()).selectBuonoFor(context.getUserContext(),bene);
		ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context,ri);
		if (ri.countElements() == 0) {
			it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context,ri);
			throw new it.cnr.jada.comp.ApplicationException("Attenzione: Nessun dato disponibile.");
		}
		SelezionatoreListaBP nbp = (SelezionatoreListaBP)context.createBusinessProcess("Selezionatore");
		nbp.setIterator(context,ri);
		nbp.disableSelection();
		nbp.setBulkInfo(it.cnr.jada.bulk.BulkInfo.getBulkInfo(V_cons_registro_inventarioBulk.class));
		nbp.setColumns(it.cnr.jada.bulk.BulkInfo.getBulkInfo(V_cons_registro_inventarioBulk.class).getColumnFieldPropertyDictionary("BASECONS"));
		HookForward hook = (HookForward)context.findForward("seleziona");		
		return context.addBusinessProcess(nbp);		
	}
	catch (Throwable e){		
		return handleException(context,e);
	}
 }
 public Forward doFattura_coll(ActionContext context) {	

	 CRUDInventarioBeniBP bp = (CRUDInventarioBeniBP)getBusinessProcess(context);

	 Inventario_beniBulk bene = (Inventario_beniBulk)bp.getModel();
	
	 try{	
		 it.cnr.jada.util.RemoteIterator ri = ((Inventario_beniComponentSession)bp.createComponentSession()).selectFatturaFor(context.getUserContext(),bene);
		 ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context,ri);
		 if (ri.countElements() == 0) {
			 it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context,ri);
			 throw new it.cnr.jada.comp.ApplicationException("Attenzione: Nessun dato disponibile.");
		 }
		 SelezionatoreListaBP nbp = (SelezionatoreListaBP)context.createBusinessProcess("Selezionatore");
		 nbp.setIterator(context,ri);
		 nbp.disableSelection();
		 nbp.setBulkInfo(it.cnr.jada.bulk.BulkInfo.getBulkInfo(V_ass_inv_bene_fatturaBulk.class));		
		 HookForward hook = (HookForward)context.findForward("seleziona");		
		 return context.addBusinessProcess(nbp);		
	 }
	 catch (Throwable e){		
		 return handleException(context,e);
	 }
  }
}
