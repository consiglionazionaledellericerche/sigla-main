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

package it.cnr.contab.inventario01.actions;

/**
  *  Si occupa di gestire le richieste provenienti dalle pagine dedicate al Carico di un bene 
  *	in Inventario in modo diretto o da Fattura Passiva.
  * 
**/
import java.math.BigDecimal;
import java.math.BigInteger;
import java.rmi.RemoteException;
import java.sql.SQLException;

import javax.ejb.EJBException;

import it.cnr.contab.inventario01.bp.CRUDCaricoInventarioBP;
import it.cnr.contab.inventario01.bp.CRUDScaricoInventarioBP;
import it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk;
import it.cnr.contab.inventario00.docs.bulk.Utilizzatore_CdrVBulk;
import it.cnr.contab.inventario00.tabrif.bulk.*;
import it.cnr.contab.inventario01.bulk.*;
import it.cnr.contab.config00.esercizio.bulk.EsercizioBulk;
import it.cnr.contab.config00.esercizio.bulk.EsercizioHome;
import it.cnr.contab.docamm00.docs.bulk.*;
import it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_voceBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_voceHome;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.PrimaryKeyHashtable;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.util.action.*;


public class CRUDCaricoBuonoAction extends it.cnr.jada.util.action.CRUDAction {
	String groups;	
/**
 * CRUDInventarioAction constructor comment.
 */
public CRUDCaricoBuonoAction() {
	super();
}

/**
 * Gestisce un comando di cancellazione.
 */
public Forward doElimina(ActionContext context) throws java.rmi.RemoteException {

	try {
		fillModel(context);

		CRUDBP bp = getBusinessProcess(context);
		if (!bp.isEditing()) {
			bp.setMessage("Non è possibile cancellare in questo momento");
		} else {
			bp.delete(context);
			bp.reset(context);
			bp.setMessage("Cancellazione effettuata");
		}
		return doConfermaNuovo(context, OptionBP.YES_BUTTON);
	} catch(Throwable e) {
		return handleException(context,e);
	}
}

	@Override
	public Forward doSalva(ActionContext actioncontext) throws RemoteException {
		return super.doSalva(actioncontext);
	}

	public Forward doSelezionaTipoMovimento(ActionContext context) {

	try {		
		CRUDCaricoInventarioBP bp = (CRUDCaricoInventarioBP)getBusinessProcess(context);	
		Buono_carico_scaricoBulk buonoC = (Buono_carico_scaricoBulk)bp.getModel();
		Tipo_carico_scaricoBulk tipoMovimento = buonoC.getTipoMovimento();
		fillModel(context);
		if (tipoMovimento != null && buonoC.getTipoMovimento() != null){
			if (bp.isInserting()){
				if (tipoMovimento.getFl_aumento_valore().booleanValue() != buonoC.getTipoMovimento().getFl_aumento_valore().booleanValue() 
				&& buonoC.getBuono_carico_scarico_dettColl()!=null && buonoC.getBuono_carico_scarico_dettColl().size()>0
				){
					OptionBP optionBP = openConfirm(context,"Attenzione: tutti i dettagli inseriti verranno persi. Vuoi continuare?",OptionBP.CONFIRM_YES_NO,"doConfirmTipoMovimento");
					optionBP.addAttribute("buonoCarico", buonoC);
					optionBP.addAttribute("tipoMovimento", tipoMovimento);
					return optionBP;			
				}
			}
			else if (bp.isEditing()){
				if(bp.isAssociataTestata(context.getUserContext(), buonoC))
				{
						buonoC.setTipoMovimento(tipoMovimento);
						throw new ApplicationException("Cambio tipologia buono non possibile.");
				}
		  }
		}	
		return context.findDefaultForward();
	 
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
public Forward doConfirmTipoMovimento(ActionContext context, OptionBP optionBP) throws it.cnr.jada.comp.ApplicationException{
	try {
		CRUDCaricoInventarioBP bp = (CRUDCaricoInventarioBP)getBusinessProcess(context);	
		Buono_carico_scaricoBulk buonoC = (Buono_carico_scaricoBulk)optionBP.getAttribute("buonoCarico");
		if (optionBP.getOption() == OptionBP.YES_BUTTON){
			
			buonoC.setBuono_carico_scarico_dettColl(new it.cnr.jada.bulk.SimpleBulkList());
			buonoC.setAccessoriContestualiHash(new PrimaryKeyHashtable());
			bp.getDettaglio().setModelIndex(context,-1);
		}
		else {
			buonoC.setTipoMovimento((Tipo_carico_scaricoBulk)optionBP.getAttribute("tipoMovimento"));
		}
		return context.findDefaultForward();
		
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
/**
  * Calcola il valore totale del bene.
  *
  *	L'utente ha inserito la quantità o il prezzo unitario del bene: questo invoca il metodo 
  *	che calcolerà il totale dei beni, invocando a sua volta il metodo Buono_carico_dettBulk.calcolaTotaleBene.
  *
  * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
  *
  * @return forward <code>Forward</code>
**/ 
public Forward doCalcolaValoreTotaleBene(ActionContext context) {	
	CRUDCaricoInventarioBP bp = (CRUDCaricoInventarioBP)getBusinessProcess(context);
	Buono_carico_scarico_dettBulk riga = null;
	if (bp.isBy_fattura()){
		riga = (Buono_carico_scarico_dettBulk)bp.getRigheInventarioDaFattura().getModel();
	} else if (bp.isBy_documento()){
		riga = (Buono_carico_scarico_dettBulk)bp.getRigheInventarioDaDocumento().getModel();
	} else{
		riga = (Buono_carico_scarico_dettBulk)bp.getDettaglio().getModel();
	}		
	
	Long qta = riga.getQuantita();
			
	java.math.BigDecimal val_unit = riga.getValore_unitario();
	try{
		fillModel(context);
		// Se la riga è per un bene accessorio, controlla che la Quantità indicata NON sia superiore a 999
		if (riga.isBeneAccessorio() && riga.getQuantita() != null && riga.getQuantita().compareTo(new Long(999))>0){
			throw new it.cnr.jada.comp.ApplicationException("Attenzione: non è possibile indicare una Quantità maggiore di 999 per un Bene Accessorio.");
		}
	
		riga.CalcolaTotaleBene();
		riga.getBene().setImponibile_ammortamento(riga.getValore_unitario().setScale(2,java.math.BigDecimal.ROUND_HALF_UP));		
	}
	catch (Throwable e){
		riga.setQuantita(qta);
		riga.setValore_unitario(val_unit);
		return handleException(context,e);
	}
	return context.findDefaultForward();
}
/**
  *   Gestisce l'aggiunta di un nuovo bene nel Buono di Carico.
  *	Controlla, prima di tutto che l'utente abbia specificato in testata un tipo di movimento 
  *	per il buono di Carico.
  * 	Nel caso che il Buono di Carico sia per una variazione di valore di un bene già presente 
  *	nel DB, allora mostra all'utente una finestra di ricerca guidata per la selezione del 
  *	Bene da variare.
  *
  * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
  *
  * @return forward <code>Forward</code>
**/
/**
  *   Gestisce l'aggiunta di un nuovo bene nel Buono di Carico.
  *	Controlla, prima di tutto che l'utente abbia specificato in testata un tipo di movimento 
  *	per il buono di Carico.
  * 	Nel caso che il Buono di Carico sia per una variazione di valore di un bene già presente 
  *	nel DB, allora mostra all'utente una finestra di ricerca guidata per la selezione del 
  *	Bene da variare.
  *
  * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
  *
  * @return forward <code>Forward</code>
**/
public Forward doAddToCRUDMain_Dettaglio(ActionContext context) {

	try {
		CRUDCaricoInventarioBP bp_carico = (CRUDCaricoInventarioBP)getBusinessProcess(context);
		Buono_carico_scaricoBulk buonoC = (Buono_carico_scaricoBulk)bp_carico.getModel();
		// Controlla che sia stato specificato un Tipo Movimento
		if (!bp_carico.isBy_fattura() && buonoC.getTipoMovimento() == null){
			return handleException(context, new it.cnr.jada.bulk.ValidationException("Attenzione: specificare un tipo di movimento nella testata"));
		}
		
		// Controlla se il Tipo Movimento è di tipo AUMENTO DI VALORE, ma NON proveniente da fattura
		else if (buonoC.isPerAumentoValore() && !buonoC.isByFatturaPerAumentoValore() && !buonoC.isByDocumentoPerAumentoValore()){
			if (buonoC.getData_registrazione()==null){
				return handleException(context, new it.cnr.jada.bulk.ValidationException("Attenzione: specificare la data di carico nella testata"));
			}	
			RicercaLiberaBP bp = (RicercaLiberaBP)context.createBusinessProcess("RicercaLibera");
			bp.setCanPerformSearchWithoutClauses(false);
			bp.setSearchProvider(bp_carico.getBeneSearchProvider(context));
			bp.setPrototype( new Inventario_beniBulk());
			bp.setMultiSelection(true);
			context.addHookForward("seleziona",this,"doBringBackAddToBeniDaCaricare");
			return context.addBusinessProcess(bp);
		}
		// Carico normale: richiama il CRUDController del Dettaglio normale, con l'appropriato metodo add
		else {
			getController(context,"main.Dettaglio").add(context);
			return context.findDefaultForward();
		}
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
public Forward doBringBackAddToBeniDaCaricare(ActionContext context) {

	try {
		HookForward fwd = (HookForward)context.getCaller();
		java.util.List selectedModels = (java.util.List)fwd.getParameter("selectedElements");
		
		if (selectedModels != null && !selectedModels.isEmpty()) {
			CRUDCaricoInventarioBP bp = (CRUDCaricoInventarioBP)getBusinessProcess(context);
			bp.aggiungiDettagliCarico(context.getUserContext(),selectedModels);
		}
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
  * @param dettaglio il <code>Buono_carico_dettBulk</code> dettaglio attuale
  *
  * @return forward <code>Forward</code>
**/ 
public Forward doBlankSearchFind_categoria_bene(ActionContext context, 
	Buono_carico_scarico_dettBulk dettaglio) 
	throws java.rmi.RemoteException {
		
	try {
		CRUDCaricoInventarioBP bp = (CRUDCaricoInventarioBP)getBusinessProcess(context);

		dettaglio.getBene().setCategoria_Bene(new it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk());
		dettaglio.getBene().setTi_ammortamenti(null);
		
		dettaglio.getBene().setTipo_ammortamento(null);				
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
  * @param dettaglio il <code>Buono_carico_dettBulk</code> dettaglio 
  * @param cat_gruppo la <code>Categoria_gruppo_inventBulk</code> categoria gruppo selezionata
  *
  * @return forward <code>Forward</code>
**/  
public Forward doBringBackSearchFind_categoria_bene(ActionContext context, Buono_carico_scarico_dettBulk dettaglio, Categoria_gruppo_inventBulk cat_gruppo) {

	try {
		fillModel(context);
		if (cat_gruppo != null){
			CRUDCaricoInventarioBP bp = (CRUDCaricoInventarioBP)getBusinessProcess(context);
			Buono_carico_scaricoBulk buonoCS = (Buono_carico_scaricoBulk)bp.getModel();						
			String cd_pubblicazioni = ((it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession", it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession.class)).getVal01(context.getUserContext(), new Integer(0), "*", "CD_CATEGORIA_GRUPPO_SPECIALE", "PUBBLICAZIONI");
			if(cd_pubblicazioni != null)
				dettaglio.getBene().setPubblicazione(cat_gruppo.getCd_categoria_padre().equalsIgnoreCase(cd_pubblicazioni));
			dettaglio.getBene().setCategoria_Bene(cat_gruppo);
			java.util.Collection ti_ammortamenti = ((it.cnr.contab.inventario00.ejb.Inventario_beniComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRINVENTARIO00_EJB_Inventario_beniComponentSession",it.cnr.contab.inventario00.ejb.Inventario_beniComponentSession.class)).findTipiAmmortamento(context.getUserContext(), cat_gruppo);
			dettaglio.getBene().setTi_ammortamenti(ti_ammortamenti);
			dettaglio.getBene().setFl_ammortamento(dettaglio.getBene().getCategoria_Bene().getFl_ammortamento());
			if (ti_ammortamenti.size()==1) 
				dettaglio.getBene().setTipo_ammortamento((it.cnr.contab.inventario00.tabrif.bulk.Tipo_ammortamentoBulk)ti_ammortamenti.iterator().next());
			if (!dettaglio.getBene().isPubblicazione())
				dettaglio.getBene().setCollocazione(null);
			
			if (dettaglio.getBene().getCategoria_Bene()!=null &&  dettaglio.getBene().getCategoria_Bene().getCd_categoria_gruppo()!=null &&
					!dettaglio.getBene().getCategoria_Bene().getFl_gestione_targa() && dettaglio.getBene().getTarga()!=null)
				dettaglio.getBene().setTarga(null);
			if (dettaglio.getBene().getCategoria_Bene()!=null &&  dettaglio.getBene().getCategoria_Bene().getCd_categoria_gruppo()!=null &&
					!dettaglio.getBene().getCategoria_Bene().getFl_gestione_seriale() && dettaglio.getBene().getSeriale()!=null)
				dettaglio.getBene().setSeriale(null); 
			
		}
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
/**
  *  Richiamato nel caso che l'utente selezioni il Check-Box "Bene Accessorio", (il quale
  * indica che il bene in quesione è accessorio di un altro bene): in tal caso, va
  *	a controllare che al bene stesso non fossero già stati associati degli Utilizzatori.
  *	In tal caso lancia un WARNING, che avvisa l'Utente che è impossibile fare questa
  *	operazione se prima non vengono cancellati i CdR Utilizzatori associati.
  *
  * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
  *
  * @return forward <code>Forward</code>
**/ 
public Forward doSelezionaBeneAccessorio(ActionContext context) {
	
	try {
		CRUDCaricoInventarioBP bp = (CRUDCaricoInventarioBP)getBusinessProcess(context);
		Buono_carico_scarico_dettBulk riga = (Buono_carico_scarico_dettBulk)bp.getDettaglio().getModel();
		fillModel(context);
		 if (riga.getBene().getCd_categoria_gruppo()==null){
			 riga.setFl_bene_accessorio(new Boolean(false));
			 throw new it.cnr.jada.comp.ApplicationException("Bisogna valorizzare prima la categoria");
		 }
		if (riga.isAssociatoConAccessorioContestuale()){
			riga.setFl_bene_accessorio(new Boolean(false));
			// Il bene è un Bene Padre di beni contestuali
			throw new it.cnr.jada.comp.ApplicationException("Attenzione. Questo bene non può essere accessorio poichè ha dei beni associati ad esso");
		}
		if (riga.hasUtilizzatori()){
			riga.setFl_bene_accessorio(new Boolean(false));
			// Il bene è associato a dei CdR Utilizzatori
			throw new it.cnr.jada.comp.ApplicationException("Attenzione. Non è possibile continuare nell'operazione poichè questo bene ha degli Utilizzatori");
		}

		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
/**
  *  L'utente ha indicato il bene appena creato come bene accessorio di un bene creato nello 
  *	stesso Buono di Carico.
  *	Il metodo provvede a cercare, tra i dettagli precedentemente creati, quelli che corrispondono 
  *	alle caratteristiche per essere utilizzati come beni padre. Tali caratteristiche sono:
  *		- il bene specificato nella riga NON deve essere un bene accessorio;
  *		- la quantità espressa nella riga di dettaglio deve essere uguale ad 1.
  * 
  *	Se ci sono righe che corrispondono a tali requsiti, esse vengono visualizzate in modo 
  *	che l'utente possa scegliere il bene padre.
  *
  * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta  
  *
  * @return forward <code>Forward</code>
**/ 
public Forward doFindAccessoriContestuali(ActionContext context) {
	
	try {
		CRUDCaricoInventarioBP bp = (CRUDCaricoInventarioBP)getBusinessProcess(context);
		Buono_carico_scarico_dettBulk riga_da_associare = (Buono_carico_scarico_dettBulk)bp.getDettaglio().getModel();
		fillModel(context);
		 if (riga_da_associare.getBene().getCd_categoria_gruppo()==null)
			 throw new it.cnr.jada.comp.ApplicationException("Bisogna valorizzare prima la categoria");
		java.util.Vector selectedModels = new java.util.Vector();
		for (java.util.Enumeration e = bp.getDettaglio().getElements(); e.hasMoreElements();) {
			Buono_carico_scarico_dettBulk riga = (Buono_carico_scarico_dettBulk)e.nextElement();
			//r.p. aggiunto controllo stessa categoria per scegliere il bene accessorio contestuale
			if(!riga.getFl_accessorio_contestuale()&& 
					(riga_da_associare.getBene().getCd_categoria_gruppo().substring(0,riga_da_associare.getBene().getCd_categoria_gruppo().lastIndexOf(".")).compareTo(
			        (riga.getBene().getCd_categoria_gruppo().substring(0,riga.getBene().getCd_categoria_gruppo().lastIndexOf("."))))==0))
			          if ((!riga.isBeneAccessorio()) && (riga.getQuantita()!=null) && (riga.getQuantita().compareTo(new Long(1))==0))				
				        selectedModels.add(riga);
		}

		if (selectedModels.size()==0){
			riga_da_associare.setFl_accessorio_contestuale(new Boolean(false));			
			throw new it.cnr.jada.comp.ApplicationException("Non ci sono elementi validi da associare come Bene Principale");
		}
		it.cnr.jada.util.action.SelezionatoreListaBP slbp = (it.cnr.jada.util.action.SelezionatoreListaBP) select(
			context,
			new it.cnr.jada.util.ListRemoteIterator(selectedModels),
			it.cnr.jada.bulk.BulkInfo.getBulkInfo(Buono_carico_scarico_dettBulk.class),
			"righeSet",
			"doBringBackAccessoriContestuali");
		slbp.setMultiSelection(false);
		return slbp;
	} catch(Throwable e) {
		return handleException(context,e);
	}	
	
}
/**
  *  Si sta creando un Buono di Carico da una Fattura Passiva.
  *	Ci si trova nel pannello di creazione dei gruppi per le righe di Fattura.
  * L'utente ha indicato il gruppo appena creato come gruppo che conterrà beni accessori di 
  *	un bene creato nello stesso Buono di Carico.
  *	Il metodo provvede a cercare, tra i gruppi precedentemente creati, quelli che corrispondono 
  *	alle caratteristiche per essere utilizzati come beni padre. Tali caratteristiche sono:
  *		- il bene specificato nella riga NON deve essere un bene accessorio;
  *		- la quantità espressa nella riga di dettaglio deve essere uguale ad 1.
  * 
  *	Se ci sono righe che corrispondono a tali requsiti, esse vengono visualizzate in modo 
  *	che l'utente possa scegliere il bene padre.
  *
  * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta  
  *
  * @return forward <code>Forward</code>
**/ 
public Forward doFindAccessoriContestualiByFattura(ActionContext context) {
	
	try {
		CRUDCaricoInventarioBP bp = (CRUDCaricoInventarioBP)getBusinessProcess(context);
		Buono_carico_scarico_dettBulk riga_da_associare = (Buono_carico_scarico_dettBulk)bp.getRigheInventarioDaFattura().getModel();

		if (riga_da_associare.isAssociatoConAccessorioContestuale()){
			riga_da_associare.setFl_bene_accessorio(new Boolean(false));
			throw new it.cnr.jada.comp.ApplicationException("Attenzione: questo bene non può essere accessorio poichè ha dei beni associati ad esso");
		}
		Buono_carico_scaricoBulk buonoCS = (Buono_carico_scaricoBulk)bp.getModel();
		Fattura_passiva_rigaIBulk dett_fattura_selezionato = (Fattura_passiva_rigaIBulk)bp.getDettagliFattura().getModel();

		it.cnr.jada.bulk.BulkList dettagli;
		fillModel(context);
		Buono_carico_scarico_dettBulk riga = null;
		java.util.Vector selectedModels = new java.util.Vector();
		for (java.util.Enumeration e = buonoCS.getDettagliRigheHash().keys(); e.hasMoreElements();) {
			Fattura_passiva_rigaIBulk dett_fattura = (Fattura_passiva_rigaIBulk)e.nextElement();
			if (dett_fattura != dett_fattura_selezionato){
				dettagli = (it.cnr.jada.bulk.BulkList)buonoCS.getDettagliRigheHash().get(dett_fattura);
				for (java.util.Iterator i = dettagli.iterator(); i.hasNext();){
					riga = (Buono_carico_scarico_dettBulk)i.next();
					if(!riga.getFl_accessorio_contestuale()&& 
							(riga_da_associare.getBene().getCd_categoria_gruppo().substring(0,riga_da_associare.getBene().getCd_categoria_gruppo().lastIndexOf(".")).compareTo(
					        (riga.getBene().getCd_categoria_gruppo().substring(0,riga.getBene().getCd_categoria_gruppo().lastIndexOf("."))))==0))
					
					 if ((!riga.isBeneAccessorio()) && (riga.getQuantita()!=null) && (riga.getQuantita().compareTo(new Long(1))==0))				
						selectedModels.add(riga);
				}
			}
		}

		if (selectedModels.size()==0){
			riga_da_associare.setFl_accessorio_contestuale(Boolean.FALSE);
			riga_da_associare.setFl_bene_accessorio(Boolean.FALSE);
			throw new it.cnr.jada.comp.ApplicationException("Non ci sono elementi validi da associare come Bene Principale");
		}
			
		it.cnr.jada.util.action.SelezionatoreListaBP slbp = (it.cnr.jada.util.action.SelezionatoreListaBP) select(
			context,
			new it.cnr.jada.util.ListRemoteIterator(selectedModels),
			it.cnr.jada.bulk.BulkInfo.getBulkInfo(Buono_carico_scarico_dettBulk.class),
			"righeSet",
			"doBringBackAccessoriContestualiByFattura");
		slbp.setMultiSelection(false);
		return slbp;
	} catch(Throwable e) {
		return handleException(context,e);
	}	
}
/**
  *  Assegna bene principale ad un bene accessorio "contestuale".
  *
  *	 E' stata generata la richiesta di cercare il bene di rifermento per un bene accessorio.
  *	La situazione è quella di un Buono di Carico creato da una Fattura Passiva.
  * Sia il bene padre che quello accessorio fanno parte del Buono di Carico attuale. 
  * Il FrameWork, dopo che l'utente ha selezionato il bene padre, richiama questo metodo che 
  *	ha il compito di assegnare al bene accessorio l'Ubicazione e l'Assegnatario del bene padre.
  *
  * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
  *
  * @return forward <code>Forward</code>
**/ 
public Forward doFreeSearchFind_bene_principale(ActionContext context) {

	try {
		BulkBP bp = (BulkBP)context.getBusinessProcess();
		Buono_carico_scaricoBulk buonoC = (Buono_carico_scaricoBulk)bp.getModel();		
		bp.fillModel(context);
		FormField field = getFormField(context,"main.Dettaglio.find_bene_principale");
		Inventario_beniBulk bene_principale = new Inventario_beniBulk();
		bene_principale.setCondizioni(buonoC.getCondizioni());
		return freeSearch(context, field, bene_principale);
	} catch(Exception e) {
		return handleException(context,e);
	}
}
/**
  *  Assegna bene principale ad un bene accessorio "contestuale".
  *
  *	 E' stata generata la richiesta di cercare il bene di rifermento per un bene accessorio.
  *	La situazione è quella di un Buono di Carico creato da una Fattura Passiva.
  * Sia il bene padre che quello accessorio fanno parte del Buono di Carico attuale. 
  * Il FrameWork, dopo che l'utente ha selezionato il bene padre, richiama questo metodo che 
  *	ha il compito di assegnare al bene accessorio l'Ubicazione e l'Assegnatario del bene padre.
  *
  * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
  *
  * @return forward <code>Forward</code>
**/ 
public Forward doBringBackAccessoriContestualiByFattura(ActionContext context) {

	try {
		HookForward caller = (HookForward)context.getCaller();
		CRUDCaricoInventarioBP bp = (CRUDCaricoInventarioBP)getBusinessProcess(context);
		Buono_carico_scaricoBulk buonoCS = (Buono_carico_scaricoBulk)bp.getModel();		
		Buono_carico_scarico_dettBulk selezionato= (Buono_carico_scarico_dettBulk)caller.getParameter("focusedElement");
		Buono_carico_scarico_dettBulk riga = new Buono_carico_scarico_dettBulk();
			
		riga = (Buono_carico_scarico_dettBulk)bp.getRigheInventarioDaFattura().getModel();	
		java.util.List selectedModels = (java.util.List)caller.getParameter("selectedElements");
		    
		if (selectedModels != null && !selectedModels.isEmpty() ){
			riga.getBene().setBene_principale(new Inventario_beniBulk());
			riga.getBene().getBene_principale().setDs_bene(selezionato.getBene().getDs_bene());	
			riga.getBene().setUbicazione(selezionato.getBene().getUbicazione());
			riga.getBene().setAssegnatario(selezionato.getBene().getAssegnatario());
			riga.setFl_bene_accessorio(Boolean.TRUE);
			riga.setFl_accessorio_contestuale(Boolean.TRUE);
			bp.setProgressivo_beni(buonoCS.addToAccessoriContestualiHash(selezionato, riga, bp.getProgressivo_beni()));
			
		}
		else {
			riga.setFl_accessorio_contestuale(new Boolean(false));
		}
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
/**
  *  Durante la creazione di un Buono di Carico, l'utente ha deselezionato la proprietà
  *	<code>Associa a Bene non registrato</code>: il metodo provvede a resettare alcune proprietà
  *	del bene, quali l'Assegnatario e l'Ubicazione, che erano state ereditate dal bene padre.
  *
  * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta  
  *
  * @return forward <code>Forward</code>
**/ 
public Forward doDeselezionaAccessoriContestuali(ActionContext context) {
	
	try {
		CRUDCaricoInventarioBP bp = (CRUDCaricoInventarioBP)getBusinessProcess(context);	
		fillModel(context);
		Buono_carico_scarico_dettBulk riga = (Buono_carico_scarico_dettBulk) bp.getDettaglio().getModel();
		Buono_carico_scaricoBulk buono_cs = (Buono_carico_scaricoBulk)((CRUDCaricoInventarioBP)getBusinessProcess(context)).getModel();

		riga.getBene().setBene_principale(null);
		riga.getBene().setAssegnatario(null);
		riga.getBene().setUbicazione(null);		
		riga.setFl_accessorio_contestuale(new Boolean(false));
		buono_cs.removeFromAccessoriContestualiHash(riga);
		
		return context.findDefaultForward();
		
	} catch(Throwable e) {
		return handleException(context,e);
	}	
	
}
/**
  *  Durante la creazione di un Buono di Carico da Fattura Passiva, l'utente ha deselezionato 
  *	la proprietà <code>Associa a Bene non registrato</code>: il metodo provvede a resettare 
  *	alcune proprietà del bene, quali l'Assegnatario e l'Ubicazione, che erano state ereditate 
  *	dal bene padre.
  *
  * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta  
  *
  * @return forward <code>Forward</code>
**/
public Forward doDeselezionaAccessoriContestualiByFattura(ActionContext context) {
	
	try {
		CRUDCaricoInventarioBP bp = (CRUDCaricoInventarioBP)getBusinessProcess(context);	
		fillModel(context);
		Buono_carico_scarico_dettBulk riga = (Buono_carico_scarico_dettBulk) bp.getRigheInventarioDaFattura().getModel();
		Buono_carico_scaricoBulk buono_cs = (Buono_carico_scaricoBulk)bp.getModel();

		riga.getBene().setBene_principale(null);
		riga.getBene().setAssegnatario(null);
		riga.getBene().setUbicazione(null);		
		riga.setFl_accessorio_contestuale(new Boolean(false));
		riga.setFl_bene_accessorio(Boolean.FALSE);
		buono_cs.removeFromAccessoriContestualiHash(riga);
		
		return context.findDefaultForward();
		
	} catch(Throwable e) {
		return handleException(context,e);
	}	
	
}
/**
  *  Durante la creazione di un Buono di Carico, l'utente ha deselezionato 
  *	la proprietà <code>Bene Accessorio</code>: il metodo provvede a resettare 
  *	alcune proprietà del bene, quali l'Assegnatario e l'Ubicazione, che erano state ereditate 
  *	dal bene padre.
  *
  * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta  
  *
  * @return forward <code>Forward</code>
**/ 
public Forward doDeselezionaBeneAccessorio(ActionContext context) {
	
	try {
		CRUDCaricoInventarioBP bp = (CRUDCaricoInventarioBP)getBusinessProcess(context);	
		fillModel(context);
		Buono_carico_scarico_dettBulk riga = (Buono_carico_scarico_dettBulk) bp.getDettaglio().getModel();
		Buono_carico_scaricoBulk buono_cs = (Buono_carico_scaricoBulk)bp.getModel();
		if (riga.isAccessorioContestuale()){
			doDeselezionaAccessoriContestuali(context);
			buono_cs.removeFromAccessoriContestualiHash(riga);
		}
		riga.getBene().setBene_principale(null);
		riga.getBene().setAssegnatario(null);
		riga.getBene().setUbicazione(null);		
		riga.setFl_accessorio_contestuale(new Boolean(false));
		return context.findDefaultForward();
		
	} catch(Throwable e) {
		return handleException(context,e);
	}	
	
}
/**
  *  Assegna bene principale ad un bene accessorio "contestuale".
  *
  *	 E' stata generata la richiesta di cercare il bene di rifermento per un bene accessorio.
  * Sia il bene padre che quello accessorio fanno parte del Buono di Carico attuale.
  * Il FrameWork, dopo che l'utente ha selezionato il bene padre, richiama questo metodo che 
  *	ha il compito di assegnare al bene accessorio l'Ubicazione e l'Assegnatario del bene padre.
  *
  * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
  *
  * @return forward <code>Forward</code>
**/
public Forward doBringBackAccessoriContestuali(ActionContext context) {

	try {
		HookForward caller = (HookForward)context.getCaller();
		CRUDCaricoInventarioBP bp = (CRUDCaricoInventarioBP)getBusinessProcess(context);
		Buono_carico_scaricoBulk buonoCS = (Buono_carico_scaricoBulk)bp.getModel();		
		Buono_carico_scarico_dettBulk selezionato= (Buono_carico_scarico_dettBulk)caller.getParameter("focusedElement");
		Buono_carico_scarico_dettBulk riga = new Buono_carico_scarico_dettBulk();
			
		riga = (Buono_carico_scarico_dettBulk)bp.getDettaglio().getModel();	
		java.util.List selectedModels = (java.util.List)caller.getParameter("selectedElements");
		
		if (selectedModels != null && !selectedModels.isEmpty()) {			
			riga.getBene().setBene_principale(new Inventario_beniBulk());
			riga.getBene().getBene_principale().setDs_bene(selezionato.getBene().getDs_bene());
			riga.getBene().setUbicazione(selezionato.getBene().getUbicazione());
			riga.getBene().setAssegnatario(selezionato.getBene().getAssegnatario());
			bp.setProgressivo_beni(buonoCS.addToAccessoriContestualiHash(selezionato, riga, bp.getProgressivo_beni()));
		}
		else 
		{
			riga.setFl_accessorio_contestuale(new Boolean(false));
		}
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
/**
  *  Assegna bene principale ad un bene accessorio.
  *
  *	 E' stata generata la richiesta di cercare il bene di rifermento per un bene accessorio.
  * Il FrameWork, dopo che l'utente ha selezionato il bene padre, richiama questo metodo che 
  *	ha il compito di assegnare al bene accessorio l'Ubicazione e l'Assegnatario del bene padre.
  *
  * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
  * @param dettaglio_accessorio il <code>Buono_carico_dettBulk</code> dettaglio che contiene il bene accessorio
  * @param selezionato il <code>Inventario_beniBulk</code> bene padre
  *
  * @return forward <code>Forward</code>
**/ 
public Forward doBringBackSearchFind_bene_principale(
	ActionContext context,
	Buono_carico_scarico_dettBulk dettaglio_accessorio,
	Inventario_beniBulk selezionato) {

	try {
		HookForward caller = (HookForward)context.getCaller();
		CRUDCaricoInventarioBP bp = (CRUDCaricoInventarioBP)getBusinessProcess(context);
		//it.cnr.contab.inventario01.ejb.BuonoCaricoScaricoComponentSession caricoComponentSession = (it.cnr.contab.inventario01.ejb.BuonoCaricoScaricoComponentSession)bp.createComponentSession();
				
		if (selezionato != null){		
			dettaglio_accessorio.getBene().setBene_principale(selezionato);
			dettaglio_accessorio.getBene().setAssegnatario(selezionato.getAssegnatario());
			dettaglio_accessorio.getBene().setUbicazione(selezionato.getUbicazione());
			
			dettaglio_accessorio.getBene().setTi_ammortamento(selezionato.getTi_ammortamento());
			dettaglio_accessorio.getBene().setFl_ammortamento(selezionato.getFl_ammortamento());
		}		
		
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
/**
  * Richiamato quando l'utente seleziona il flag "Soggetto ad Ammortamento", nel
  *	tab relativo all'ammortamento, (solo per Carico Diretto).
  *
  * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta  
  *
  * @return forward <code>Forward</code>
**/
public Forward doNonAmmortizzato(ActionContext context) {	
	CRUDCaricoInventarioBP bp = (CRUDCaricoInventarioBP)getBusinessProcess(context);
	Buono_carico_scarico_dettBulk riga = (Buono_carico_scarico_dettBulk)bp.getDettaglio().getModel();
	try{
		fillModel(context);
		if (riga.getBene()!=null && riga.getBene().getFl_ammortamento()!=null && !riga.getBene().getFl_ammortamento().booleanValue()){
			riga.getBene().setTipo_ammortamento(null);
		}
	}
	catch (Throwable e){		
		return handleException(context,e);
	}
	return context.findDefaultForward();
}
/**
 *   Gestisce l'eliminazione di un bene dal Buono di Carico.
 *	Controlla che il bene non sia associato con beni all'interno dello stesso 
 *	Buono di Carico, (metodo Buono_carico_dettBulk.isAssociatoConAccessorioContestuale()).
 *
 * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
 *
 * @return forward <code>Forward</code>
**/
public Forward doRemoveFromCRUDMain_Dettaglio(ActionContext context)
	throws java.rmi.RemoteException, BusinessProcessException {

	CRUDCaricoInventarioBP bp = (CRUDCaricoInventarioBP)getBusinessProcess(context);

	Buono_carico_scarico_dettBulk riga = (Buono_carico_scarico_dettBulk)bp.getDettaglio().getModel();
	if (riga!=null){
		// Il bene è un bene padre di beni segnalati come accessori, presenti nel Buono di Carico
		if (riga.isAssociatoConAccessorioContestuale()){
			return handleException(context, new it.cnr.jada.comp.ApplicationException("Il bene selezionato non può essere cancellato, poichè associato ad altri beni"));
		}
		try{		
			bp.getDettaglio().remove(context);
		} catch (it.cnr.jada.bulk.ValidationException vge){
		}
		if (riga.isAccessorioContestuale()){
			Buono_carico_scaricoBulk buono_carico = (Buono_carico_scaricoBulk)riga.getBuono_cs();
			buono_carico.removeFromAccessoriContestualiHash(riga);
		}
			
	}
	return context.findDefaultForward();
}
public Forward doRemoveFromCRUDMain_Dettaglio_VUtilizzatori(ActionContext context)
	throws java.rmi.RemoteException, BusinessProcessException {

	CRUDCaricoInventarioBP bp = (CRUDCaricoInventarioBP)getBusinessProcess(context);

	Buono_carico_scarico_dettBulk dettaglio = (Buono_carico_scarico_dettBulk)bp.getDettaglio().getModel();
	Utilizzatore_CdrVBulk cdr_Utilizzatore = (Utilizzatore_CdrVBulk)bp.getVUtilizzatori().getModel();
	
	if (cdr_Utilizzatore!=null){		
		try{		
			bp.getVUtilizzatori().remove(context);			
		} catch (it.cnr.jada.bulk.ValidationException vge){
		}	
	}
	return context.findDefaultForward();
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
		CRUDCaricoInventarioBP bp = (CRUDCaricoInventarioBP)getBusinessProcess(context);
		Buono_carico_scaricoBulk buonoC = (Buono_carico_scaricoBulk)bp.getModel();
		Buono_carico_scarico_dettBulk dett = (Buono_carico_scarico_dettBulk)bp.getDettaglio().getModel();
		it.cnr.contab.config00.sto.bulk.CdrBulk cdrcoll;
		if (dett.getV_utilizzatoriColl().size()>0){

			java.util.Iterator i = dett.getV_utilizzatoriColl().iterator(); 
			while (i.hasNext()){
				Utilizzatore_CdrVBulk v_cdr = (Utilizzatore_CdrVBulk)i.next();
			  if(cdrSelezionato!= null)
				if (v_cdr.getCdr() != null && v_cdr.getCdr().equalsByPrimaryKey(cdrSelezionato)&&  i.hasNext()){
					((Utilizzatore_CdrVBulk)dett.getV_utilizzatoriColl().get(dett.getV_utilizzatoriColl().size()-1)).setCdr(new it.cnr.contab.config00.sto.bulk.CdrBulk());
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
 * Gestisce il comando azzeramento del searchtool per la ricerca del CdR Utilizzatore.
 *
 *  Quando si azzera il searchTool per la ricerca del CdR Utilizzatore,
 * azzera la Collection delle Linee di Attivita collegate
 *
 * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
 * @param cdr_utilizzatore il <code>Utilizzatore_CdrVBulk</code> CdR di riferimento.
 *
 * @return forward <code>Forward</code>
**/ 
public Forward doBlankSearchFind_cdr(ActionContext context, 
	Utilizzatore_CdrVBulk cdr_utilizzatore) 
	throws java.rmi.RemoteException {
		
	try {
		cdr_utilizzatore.getBuono_cs_utilizzatoriColl().clear();
		cdr_utilizzatore.setBene(null);
		cdr_utilizzatore.setCdr(new it.cnr.contab.config00.sto.bulk.CdrBulk());
		cdr_utilizzatore.setDettaglio(null);
		cdr_utilizzatore.setPercentuale_utilizzo_cdr(null);
		return context.findDefaultForward();
		
	} catch(Exception e) {
		return handleException(context,e);
	}
}	
/**
 *  Gestisce l'aggiunta di un nuovo CDR Utilizzatore. Prima di permettere l'aggiunta
 *	di un Utilizzatore, va a fare il controllo di validità su quello attuale,
 *	e sulle Linee di Attività ad esso associate.
 *
 * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
 *
 * @return forward <code>Forward</code>
**/
public Forward doAddToCRUDMain_Dettaglio_VUtilizzatori(ActionContext context) {

	try {
		fillModel(context);
		CRUDCaricoInventarioBP bp_carico = (CRUDCaricoInventarioBP)getBusinessProcess(context);
		Buono_carico_scarico_dettBulk dett = (Buono_carico_scarico_dettBulk)bp_carico.getDettaglio().getModel();
		Utilizzatore_CdrVBulk v_utitliz = (Utilizzatore_CdrVBulk)bp_carico.getVUtilizzatori().getModel();
		
		// Controlla la validità del CdR inserito
		if (dett.getV_utilizzatoriColl().size()>0 && v_utitliz!=null){
			// CdR NON specificato
			if (v_utitliz.getCdr()==null || v_utitliz.getCdr().getCd_centro_responsabilita()==null)
				return handleException(context, new it.cnr.jada.bulk.ValidationException("Attenzione: specificare un CDR Utilizzatore"));

			// Percentuale di utilizzo per il CdR NON specificata
			if (v_utitliz.getPercentuale_utilizzo_cdr().compareTo(new java.math.BigDecimal(0))==0)
				return handleException(context, new it.cnr.jada.bulk.ValidationException("Attenzione: specificare una percentuale di utilizzo per il CDR Utilizzatore"));

			// Valida le Percentuali di utilizzo delle Linee di Attività indicate per il CdR Utilizzatore
			if (v_utitliz.getBuono_cs_utilizzatoriColl().size()>0)
				bp_carico.validate_Percentuali_LA(context, v_utitliz);
			else
				// Nessuna Linea di Attività specificata per il CdR
				return handleException(context, new it.cnr.jada.bulk.ValidationException("Attenzione: specificare almeno un GAE con la relativa Percentuale di Utilizzo"));
		}
		
		getController(context,"main.Dettaglio.VUtilizzatori").add(context);
		return context.findDefaultForward();
			
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
/**
 *  Richiamato nel caso in cui l'utente selezioni un Tipo di Ammortamento dalla select
 *	presente nel tab "Ammortamento", durante il Carico Diretto. Il metodo setta la proprità
 *	ti_ammortamento del bene.
 *
 * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
 *
 * @return forward <code>Forward</code>
**/ 
public Forward doSelezionaTiAmmortamento(ActionContext context) {
	
	try {
		CRUDCaricoInventarioBP bp = (CRUDCaricoInventarioBP)getBusinessProcess(context);
		Buono_carico_scarico_dettBulk riga = (Buono_carico_scarico_dettBulk)bp.getDettaglio().getModel();
		fillModel(context);
		if (riga.getBene().getTipo_ammortamento() != null){
			String tipo = riga.getBene().getTipo_ammortamento().getTi_ammortamento();
			riga.getBene().setTi_ammortamento(tipo);
		}
		else riga.getBene().setTi_ammortamento(null);

		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
/**
 * Crea i raggruppamenti dei beni
 *
 *  E' stata generata la richiesta di creare un Buono di Carico da una Fattura Passiva.
 *	In questa fase ci si trova nel pannello che serve a creare, per ogni riga di Fattura ed 
 *	in base alla quantità della riga stessa, dei gruppi che saranno poi le righe di dettaglio 
 *	del Buono di Carico. L'utente ha richiesto di creare i gruppi relativi ad una riga di Fattura.
 *	Il sistema, esegue vari controlli sul numero di blocchi indicato dall'utente; dopodichè
 *	si passa alla creazione dei blocchi che saranno poi le righe di dettaglio del Buono di Carico: 
 * si creano tante righe di dettaglio, (Buono_carico_detBulk), con i rispettivi beni, (Inventario_beniBulk),
 *	le cui proprietà, (Categoria_gruppo, valore_iniziale, tipo ISTITUZIONALE/COMMERCIALE, etc.) saranno 
 *	ereditate dalla riga di Fattura.
 *
 * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta  
 *
 * @return forward <code>Forward</code>
**/ 
public Forward doCreaGruppi(ActionContext context) {	

	Buono_carico_scarico_dettBulk rigoInventario = new Buono_carico_scarico_dettBulk();
	Buono_carico_scarico_dettBulk nuovoRigoInventario;
	Inventario_beniBulk bene = new Inventario_beniBulk();
	Inventario_beniBulk nuovoBene;
	int gruppi = 0;
	Categoria_gruppo_inventBulk cat_gruppo;
	String cd_pubblicazioni = null;
	boolean isPubblicazione = false;
			
	CRUDCaricoInventarioBP bp = (CRUDCaricoInventarioBP)getBusinessProcess(context);	
	Buono_carico_scaricoBulk buono_cs = (Buono_carico_scaricoBulk)((CRUDCaricoInventarioBP)getBusinessProcess(context)).getModel();	
	Fattura_passiva_rigaBulk riga_fattura = (Fattura_passiva_rigaBulk)bp.getDettagliFattura().getModel();
	PrimaryKeyHashtable h = buono_cs.getDettagliRigheHash();
	BulkList listaInventari = new BulkList();
	java.math.BigDecimal valore_unitario = new java.math.BigDecimal(0);
	
	try{
		// Non è stata indicata la riga di Fattura di riferimento
		if (riga_fattura==null){
			throw new it.cnr.jada.comp.ApplicationException ("Attenzione: selezionare una Bene/Servizio");
		}
		// Non è stato indicato il numero di gruppi
		if (getGroups() == null){
			throw new it.cnr.jada.comp.ApplicationException ("Attenzione: numero di blocchi non valido. Il numero dei blocchi non può essere nullo");
		}
		else{
			try{
				gruppi = Integer.parseInt(getGroups());
				bp.setIsNumGruppiErrato(false);
			} catch (NumberFormatException e){
				bp.setIsNumGruppiErrato(true);
				return handleException(context, new it.cnr.jada.bulk.FillException("Errore di formattazione sul campo N. Gruppi",e));
			}
		}
		// Il numero di gruppi è minore di 0.
		if (gruppi<0){
			throw new it.cnr.jada.comp.ApplicationException ("Attenzione: numero di blocchi non valido. Il numero dei blocchi non può essere negativo");
		}
		// Il numero di gruppi è uguale a 0.
		if (gruppi==0){
			throw new it.cnr.jada.comp.ApplicationException ("Attenzione: numero di blocchi non valido. Il numero dei blocchi non può essere zero");
		}
		// Il numero di gruppi è superiore alla quantità indicata nella riga di Fattura
		if (gruppi>riga_fattura.getQuantita().intValue()){
			throw new it.cnr.jada.comp.ApplicationException ("Attenzione: numero di blocchi non valido. Il numero dei blocchi non può essere superiore alla quantità del Bene");
		}
		// Valida i gruppi
		validaCreaGruppi(buono_cs, riga_fattura);

		// Cerca il Codice relativo alla Categoria PUBBLICAZIONI
		cd_pubblicazioni = ((it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession", it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession.class)).getVal01(context.getUserContext(), new Integer(0), "*", "CD_CATEGORIA_GRUPPO_SPECIALE", "PUBBLICAZIONI");
		if(cd_pubblicazioni != null)
			isPubblicazione = riga_fattura.getBene_servizio().getCategoria_gruppo().getCd_categoria_padre().equalsIgnoreCase(cd_pubblicazioni);
	}
	catch (Throwable t){
		return handleException(context, t);
	}
	//BulkList list = (BulkList)buono_cs.getDettagliRigheHash().get(riga_fattura);	
	
	for (int i=0;i<gruppi;i++){
		
		nuovoRigoInventario = new Buono_carico_scarico_dettBulk();
		nuovoBene = new Inventario_beniBulk();
		nuovoRigoInventario.setToBeCreated();
		nuovoBene.setToBeCreated();
		
		if (gruppi==1){
			nuovoRigoInventario.setQuantita(riga_fattura.getQuantita().longValue());
			bp.setIsQuantitaEnabled(false);
		}
		else{
			bp.setIsQuantitaEnabled(true);
		}		
		// Assegna il Prezzo unitario: il prezzo è diverso a seconda che il dettaglio della Fattura sia ISTITUZIONALE o COMMERCIALE
		if (riga_fattura.getTi_istituz_commerc().equals(riga_fattura.ISTITUZIONALE)){
			valore_unitario = riga_fattura.getIm_imponibile().add(riga_fattura.getIm_iva());
			valore_unitario = valore_unitario.divide(riga_fattura.getQuantita(), 2 ,java.math.BigDecimal.ROUND_HALF_UP);
			nuovoRigoInventario.setValore_unitario(valore_unitario);
		} else {
			valore_unitario = riga_fattura.getIm_imponibile().divide(riga_fattura.getQuantita(), 2 ,java.math.BigDecimal.ROUND_HALF_UP);
			nuovoRigoInventario.setValore_unitario(valore_unitario);
		}
		
		nuovoBene.setDs_bene(riga_fattura.getDs_riga_fattura());
		nuovoBene.setValore_iniziale(nuovoRigoInventario.getValore_unitario());
		nuovoBene.setTi_commerciale_istituzionale(riga_fattura.getTi_istituz_commerc());
		nuovoBene.setUbicazione(new Ubicazione_beneBulk());
		nuovoBene.setAssegnatario(new it.cnr.contab.anagraf00.core.bulk.TerzoBulk());
		nuovoBene.setCd_cds(buono_cs.getCds_scrivania());
		nuovoBene.setCd_unita_organizzativa(buono_cs.getUo_scrivania());
		nuovoBene.setCategoria_Bene(riga_fattura.getBene_servizio().getCategoria_gruppo());
		nuovoBene.setInventario(buono_cs.getInventario());
		nuovoBene.setPg_inventario(buono_cs.getPg_inventario());		
		nuovoBene.setFl_totalmente_scaricato(Boolean.FALSE);
		nuovoBene.setImponibile_ammortamento(nuovoRigoInventario.getValore_unitario());
		if(cd_pubblicazioni != null)			
			nuovoBene.setPubblicazione(isPubblicazione);

		nuovoRigoInventario.setBuono_cs(buono_cs);
		nuovoRigoInventario.setBene(bene);
		nuovoRigoInventario.CalcolaTotaleBene();

		nuovoRigoInventario.setBene(nuovoBene);
		
		nuovoRigoInventario.setProgressivo(bp.incrementaProgressivo_beni().intValue());
		listaInventari.add(nuovoRigoInventario);
	}
	h.remove(riga_fattura);
	h.put(riga_fattura,listaInventari);
	buono_cs.getDettagliRigheHash().put(riga_fattura,listaInventari);
	
	return context.findDefaultForward();

}
/**
 * Crea le righe di dettaglio associate ad un Fattura Passiva
 *
 *  E' stata generata la richiesta di creare un Buono di Carico da una Fattura Passiva.
 *	In questa fase ci si trova nel pannello che serve a creare, per ogni riga di Fattura ed 
 *	in base alla quantità della riga stessa, dei gruppi che saranno poi le righe di dettaglio 
 *	del Buono di Carico. L'utente ha richiesto di creare proprio questi dettagli dopo aver 
 *	compiuto tutte le operazioni di raggruppamento.
 *	Il sistema, prima di tutto, controlla la validità dei gruppi creati, (metodo validaCreaDettagli);
 *	in seguito crea i dettagli del Buono di Carico e li setta come collezione del Buono.
 *
 * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta  
 *
 * @return forward <code>Forward</code>
**/ 
public Forward doCreaDettagli(ActionContext context) {	

	CRUDCaricoInventarioBP bp = (CRUDCaricoInventarioBP)getBusinessProcess(context);	
	Buono_carico_scaricoBulk buono_cs = (Buono_carico_scaricoBulk)bp.getModel();
	Buono_carico_scarico_dettBulk rigoInventario = new Buono_carico_scarico_dettBulk();
	it.cnr.jada.bulk.SimpleBulkList totaleRigheInventario = new it.cnr.jada.bulk.SimpleBulkList();
	if (buono_cs==null)
		return handleException(context, new Exception());
		
	it.cnr.jada.bulk.PrimaryKeyHashtable fatturaTable = buono_cs.getDettagliRigheHash();
	
	// Controlla che siano stati specificati dei gruppi per ogni riga di Fattura
	try{
		validaCreaDettagli(context,fatturaTable);
	} catch (it.cnr.jada.bulk.ValidationException ve){
		return handleException(context, ve);
	}
	
	for (java.util.Enumeration e = fatturaTable.elements(); e.hasMoreElements();){
		BulkList righeInventario = (BulkList)e.nextElement();

		for (java.util.Iterator i = righeInventario.iterator(); i.hasNext();){
			rigoInventario = (Buono_carico_scarico_dettBulk)i.next();
			totaleRigheInventario.add(rigoInventario);
		}
	}

	buono_cs.setBuono_carico_scarico_dettColl(totaleRigheInventario);
	bp.setFirst(false);
	return context.findDefaultForward();
}
public void setGroups(java.lang.String newGroups) {
	groups = newGroups;
}
public java.lang.String getGroups() {
	return groups;
}
/**
  *  E' stata generata la richiesta di creare i dettagli relativi ad un Buono di Carico generato
  *	da una Fattura Passiva.
  *	Il metodo si occupa del controllo sui gruppi creati per ogni riga di Fattura.
  *	Prima di tutto controlla che siano stati indicati dei gruppi per ogni dettaglio della Fattura;
  *	per ogni gruppo, poi, controlla che sia stata indicata una quantità;
  *	infine, controlla che il totale delle quantità indicate per tutti i gruppi relativi ad 
  *	una riga di Fattura corrisponda con la quantità indicata nella riga di Fattura.
  *
  * @param righeDaFatturaHash la <code>PrimaryKeyHashtable</code> tabella contenente le associazioni 
  *		tra le righe di Fattura ed i gruppi ad esse associati
**/  
private void validaCreaDettagli(ActionContext context, PrimaryKeyHashtable righeDaFatturaHash) throws it.cnr.jada.bulk.ValidationException{
	
	java.math.BigDecimal totale;
	
	for (java.util.Enumeration e = righeDaFatturaHash.keys(); e.hasMoreElements();){
		totale = new java.math.BigDecimal(0);
		Fattura_passiva_rigaBulk riga_fattura = (Fattura_passiva_rigaBulk)e.nextElement();
		
		it.cnr.jada.bulk.BulkList righeInventario = (it.cnr.jada.bulk.BulkList)righeDaFatturaHash.get(riga_fattura);

		// Non sono stati specificati dei gruppi per una riga di Fattura
		if (righeInventario.size()==0){
			throw new it.cnr.jada.bulk.ValidationException ("Attenzione: " +
				"è necessario specificare dei gruppi per ogni riga di Fattura.\n" + 
				"Non sono stati specificati dei gruppi per la riga di fattura '"+
				riga_fattura.getDs_riga_fattura() + 
				"'");
		}
		
		for (java.util.Iterator i = righeInventario.iterator(); i.hasNext();){
			Buono_carico_scarico_dettBulk riga_inventario = (Buono_carico_scarico_dettBulk)i.next();
			// Non è stata indicata la quantità nella riga di dettaglio del Buono di Carico
			if (riga_inventario.getQuantita()==null || (riga_inventario.getQuantita().compareTo(new Long(0)))==0){
				throw new it.cnr.jada.bulk.ValidationException ("Attenzione: "+
					"uno o più dettagli specificati per la riga di Fattura '" + 
					riga_fattura.getDs_riga_fattura() + 
					"' non hanno Quantità");
			}
			totale = totale.add(new BigDecimal(riga_inventario.getQuantita().longValue()));
			
			// Imposta il fl_ammortamento ricavandolo dalla Categoria Gruppo di appartenenza
			riga_inventario.getBene().setFl_ammortamento(riga_fattura.getBene_servizio().getCategoria_gruppo().getFl_ammortamento());
			
			riga_inventario.setTi_documento("C");
		try{
			java.util.Collection ti_ammortamenti = ((it.cnr.contab.inventario00.ejb.Inventario_beniComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRINVENTARIO00_EJB_Inventario_beniComponentSession",it.cnr.contab.inventario00.ejb.Inventario_beniComponentSession.class)).findTipiAmmortamento(context.getUserContext(), riga_inventario.getBene().getCategoria_Bene());
			riga_inventario.getBene().setTi_ammortamenti(ti_ammortamenti);
			//r.p. non dovrebbe servire ... sta sopra
			riga_inventario.getBene().setFl_ammortamento(riga_inventario.getBene().getCategoria_Bene().getFl_ammortamento());
			if (ti_ammortamenti.size()==1) 
				riga_inventario.getBene().setTipo_ammortamento((it.cnr.contab.inventario00.tabrif.bulk.Tipo_ammortamentoBulk)ti_ammortamenti.iterator().next());
		}	
		catch(Throwable exception) {
				handleException(context,exception);
		}	
		}
		
		// Il totale dei gruppi associati ad una riga di Fattura non corrisponde con la quantità espressa nella riga di Fattura stessa.
		if (!((totale.compareTo(riga_fattura.getQuantita()))==0)){
			throw new it.cnr.jada.bulk.ValidationException ("Attenzione: " +
				"il totale delle quantità indicate per la riga di Fattura '" + 
				riga_fattura.getDs_riga_fattura() + 
				"' non corrisponde al totale della riga stessa");			
		}
	 //}
	}
}
/**
  *  E' stata generata la richiesta di creare i gruppi associati ad una riga di Fattura Passiva.
  *	Il metodo si occupa di controllare la validità della righe di dettaglio create.
  *
  * @param buonoCS il <code>Buono_caricoBulk</code> Buono di Carico
  * @param riga_fattura la <code>Fattura_passiva_rigaBulk</code> riga di Fattura
**/ 
private void validaCreaGruppi(
	Buono_carico_scaricoBulk buonoCS,
	Fattura_passiva_rigaBulk riga_fattura) 
	throws it.cnr.jada.bulk.ValidationException{	

	// Restuituisce la HashTable che associa le righe d inventario alle righe di Fattura
	PrimaryKeyHashtable righe_fattura = buonoCS.getDettagliRigheHash();
	
	// Restituisce la collezione di righe di inventario associate alla 
	//	riga di fattura selezionata
	BulkList righe_inventario = (BulkList)righe_fattura.get(riga_fattura);

	// Restituisce la HashTable che associa le righe di Inventario "Padre", con le
	//	righe di Inventario "Figlie"
	PrimaryKeyHashtable accessori_contestuali = buonoCS.getAccessoriContestualiHash();

	/* Controlla se, tra le righe create precedentemente, ce n'è qualcuna a cui
	*	è associato uno o più Beni.
	*/
	for (java.util.Iterator i = righe_inventario.iterator(); i.hasNext();){
		Buono_carico_scarico_dettBulk dettaglio_inventario = (Buono_carico_scarico_dettBulk)i.next();
		if (accessori_contestuali != null && accessori_contestuali.containsKey(dettaglio_inventario.getChiaveHash())){
			throw new it.cnr.jada.bulk.ValidationException("Attenzione: non è possibile ricreare i gruppi poichè una delle righe è associata a dei beni Accessori");
		}

		if (dettaglio_inventario.isAccessorioContestuale()){
			throw new it.cnr.jada.bulk.ValidationException("Attenzione: non è possibile ricreare i gruppi poichè una delle righe è indicata come Bene Accessorio");
		}
	}	
}
public Forward doOnData_registrazioneChange(ActionContext context)  {
	try{
		CRUDCaricoInventarioBP bp = (CRUDCaricoInventarioBP)context.getBusinessProcess();
		Buono_carico_scaricoBulk model=(Buono_carico_scaricoBulk)bp.getModel();
		if (!bp.isBy_fattura() && !bp.isBy_documento())
			if ((model.getTipoMovimento()!=null)&& model.getTipoMovimento().getFl_aumento_valore())
				bp.getDettaglio().removeAll(context);
		fillModel(context);
		bp.setModel(context,model);
		return context.findDefaultForward();
	}catch (Throwable t) {
	    return handleException(context, t);

	}
}
	/**
	 *  Durante la creazione di un Buono di Carico da Fattura Passiva, l'utente ha deselezionato 
	 *	la proprietÓ <code>Associa a Bene non registrato</code>: il metodo provvede a resettare 
	 *	alcune proprietÓ del bene, quali l'Assegnatario e l'Ubicazione, che erano state ereditate 
	 *	dal bene padre.
	 *
	 * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta  
	 *
	 * @return forward <code>Forward</code>
	**/
	public Forward doDeselezionaAccessoriContestualiByDocumento(ActionContext context) {
		
		try {
			CRUDCaricoInventarioBP bp = (CRUDCaricoInventarioBP)getBusinessProcess(context);	
			fillModel(context);
			Buono_carico_scarico_dettBulk riga = (Buono_carico_scarico_dettBulk) bp.getRigheInventarioDaDocumento().getModel();
			Buono_carico_scaricoBulk buono_cs = (Buono_carico_scaricoBulk)bp.getModel();

			riga.getBene().setBene_principale(null);
			riga.getBene().setAssegnatario(null);
			riga.getBene().setUbicazione(null);		
			riga.setFl_accessorio_contestuale(new Boolean(false));
			riga.setFl_bene_accessorio(Boolean.FALSE);
			buono_cs.removeFromAccessoriContestualiHash(riga);
			
			return context.findDefaultForward();
			
		} catch(Throwable e) {
			return handleException(context,e);
		}	
		
	}
	public Forward doCreaGruppiDoc(ActionContext context) throws EJBException, SQLException, PersistencyException, ComponentException, RemoteException {	
		Buono_carico_scarico_dettBulk nuovoRigoInventario;
		Inventario_beniBulk bene = new Inventario_beniBulk();
		Inventario_beniBulk nuovoBene;
		int gruppi = 0;
		String cd_pubblicazioni = null;
		boolean isPubblicazione = false;
				
		CRUDCaricoInventarioBP bp = (CRUDCaricoInventarioBP)getBusinessProcess(context);	
		Buono_carico_scaricoBulk buono_cs = (Buono_carico_scaricoBulk)((CRUDCaricoInventarioBP)getBusinessProcess(context)).getModel();	
		Documento_generico_rigaBulk riga = (Documento_generico_rigaBulk)bp.getDettagliDocumento().getModel();
		PrimaryKeyHashtable h = buono_cs.getDettagliRigheDocHash();
		BulkList listaInventari = new BulkList();
		java.math.BigDecimal valore_unitario = new java.math.BigDecimal(0);
		
		try{
			// Non è stata indicata la riga di riferimento
			if (riga==null){
				throw new it.cnr.jada.comp.ApplicationException ("Attenzione: selezionare una riga");
			}
			// Non è stato indicato il numero di gruppi
			if (getGroups() == null){
				throw new it.cnr.jada.comp.ApplicationException ("Attenzione: numero di blocchi non valido. Il numero dei blocchi non può essere nullo");
			}
			else{
				try{
					gruppi = Integer.parseInt(getGroups());
					bp.setIsNumGruppiErrato(false);
				} catch (NumberFormatException e){
					bp.setIsNumGruppiErrato(true);
					return handleException(context, new it.cnr.jada.bulk.FillException("Errore di formattazione sul campo N. Gruppi",e));
				}
			}
			// Il numero di gruppi è minore di 0.
			if (gruppi<0){
				throw new it.cnr.jada.comp.ApplicationException ("Attenzione: numero di blocchi non valido. Il numero dei blocchi non può essere negativo");
			}
			// Il numero di gruppi è uguale a 0.
			if (gruppi==0){
				throw new it.cnr.jada.comp.ApplicationException ("Attenzione: numero di blocchi non valido. Il numero dei blocchi non può essere zero");
			}
			// Valida i gruppi
			validaCreaGruppiDoc(buono_cs, riga);
			
		}
		catch (Throwable t){
			return handleException(context, t);
		}
		
		for (int i=0;i<gruppi;i++){
			
			nuovoRigoInventario = new Buono_carico_scarico_dettBulk();
			nuovoBene = new Inventario_beniBulk();
			nuovoRigoInventario.setToBeCreated();
			nuovoBene.setToBeCreated();
			
			if (gruppi==1){
				nuovoRigoInventario.setQuantita(new Long(1));
				bp.setIsQuantitaEnabled(false);
			}
			else{
				bp.setIsQuantitaEnabled(true);
			}		
			// 
			nuovoRigoInventario.setValore_unitario(riga.getIm_riga().divide(new BigDecimal(gruppi),2,BigDecimal.ROUND_HALF_UP));
			
			nuovoBene.setDs_bene(riga.getDs_riga());
			nuovoBene.setValore_iniziale(nuovoRigoInventario.getValore_unitario().divide(new BigDecimal(gruppi),2,BigDecimal.ROUND_HALF_UP));
			nuovoBene.setTi_commerciale_istituzionale(riga.getDocumento_generico().getTi_istituz_commerc());
			nuovoBene.setUbicazione(new Ubicazione_beneBulk());
			nuovoBene.setAssegnatario(new it.cnr.contab.anagraf00.core.bulk.TerzoBulk());
			nuovoBene.setCd_cds(buono_cs.getCds_scrivania());
			nuovoBene.setCd_unita_organizzativa(buono_cs.getUo_scrivania());
			nuovoBene.setCategoria_Bene(new Categoria_gruppo_inventBulk());
			if((riga.getDocumento_generico().getTi_entrate_spese()==Documento_genericoBulk.SPESE) &&
	 				riga.getObbligazione_scadenziario()!=null &&
	 				riga.getObbligazione_scadenziario().getObbligazione()!=null &&
	 				riga.getObbligazione_scadenziario().getObbligazione().getElemento_voce()!=null){
				nuovoBene.getCategoria_Bene().setNodoPadre(new Categoria_gruppo_inventBulk());
				
				if(((it.cnr.contab.inventario01.ejb.BuonoCaricoScaricoComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRINVENTARIO01_EJB_BuonoCaricoScaricoComponentSession",it.cnr.contab.inventario01.ejb.BuonoCaricoScaricoComponentSession.class)).findCategoria_gruppo_voceforvoce(context.getUserContext(), riga.getObbligazione_scadenziario().getObbligazione().getElemento_voce())!=null)
					nuovoRigoInventario.setCat_voce(((it.cnr.contab.inventario01.ejb.BuonoCaricoScaricoComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRINVENTARIO01_EJB_BuonoCaricoScaricoComponentSession",it.cnr.contab.inventario01.ejb.BuonoCaricoScaricoComponentSession.class)).findCategoria_gruppo_voceforvoce(context.getUserContext(), riga.getObbligazione_scadenziario().getObbligazione().getElemento_voce()));
				else
				  throw new it.cnr.jada.comp.ApplicationException("Non esiste nessuna Categoria inventariale associata al capitolo.");
				
			}
			nuovoBene.setInventario(buono_cs.getInventario());
			nuovoBene.setPg_inventario(buono_cs.getPg_inventario());		
			nuovoBene.setFl_totalmente_scaricato(Boolean.FALSE);
			nuovoBene.setImponibile_ammortamento(nuovoRigoInventario.getValore_unitario());
			if(cd_pubblicazioni != null)			
				nuovoBene.setPubblicazione(isPubblicazione);

			nuovoRigoInventario.setBuono_cs(buono_cs);
			nuovoRigoInventario.setBene(bene);
			nuovoRigoInventario.CalcolaTotaleBene();

			nuovoRigoInventario.setBene(nuovoBene);
			
			nuovoRigoInventario.setProgressivo(bp.incrementaProgressivo_beni().intValue());
			listaInventari.add(nuovoRigoInventario);
		}
		h.remove(riga);
		h.put(riga,listaInventari);
		buono_cs.getDettagliRigheDocHash().put(riga,listaInventari);
		return context.findDefaultForward();
	}
	/**
	 *  E' stata generata la richiesta di creare i gruppi associati ad una riga di Fattura Documento.
	 *	Il metodo si occupa di controllare la validitÓ della righe di dettaglio create.
	 *
	 * @param buonoCS il <code>Buono_caricoBulk</code> Buono di Carico
	 * @param riga la <code>Documento_generico_rigaBulk</code> riga di Documento
	**/ 
	private void validaCreaGruppiDoc(
		Buono_carico_scaricoBulk buonoCS,
		Documento_generico_rigaBulk riga) 
		throws it.cnr.jada.bulk.ValidationException{	

		// Restuituisce la HashTable che associa le righe d inventario alle righe di Documento
		PrimaryKeyHashtable righe = buonoCS.getDettagliRigheDocHash();
		
		// Restituisce la collezione di righe di inventario associate alla 
		//	riga di fattura selezionata
		BulkList righe_inventario = (BulkList)righe.get(riga);

		// Restituisce la HashTable che associa le righe di Inventario "Padre", con le
		//	righe di Inventario "Figlie"
		PrimaryKeyHashtable accessori_contestuali = buonoCS.getAccessoriContestualiHash();

		/* Controlla se, tra le righe create precedentemente, qualcuna a cui
		*	è associato uno o più Beni.
		*/
		for (java.util.Iterator i = righe_inventario.iterator(); i.hasNext();){
			Buono_carico_scarico_dettBulk dettaglio_inventario = (Buono_carico_scarico_dettBulk)i.next();
			if (accessori_contestuali != null && accessori_contestuali.containsKey(dettaglio_inventario.getChiaveHash())){
				throw new it.cnr.jada.bulk.ValidationException("Attenzione: non è possibile ricreare i gruppi poichè una delle righe è associata a dei beni Accessori");
			}

			if (dettaglio_inventario.isAccessorioContestuale()){
				throw new it.cnr.jada.bulk.ValidationException("Attenzione: non è possibile ricreare i gruppi poichè una delle righe è indicata come Bene Accessorio");
			}
		}	
	}
	public Forward doFindAccessoriContestualiByDocumento(ActionContext context) {
		
		try {
			CRUDCaricoInventarioBP bp = (CRUDCaricoInventarioBP)getBusinessProcess(context);
			Buono_carico_scarico_dettBulk riga_da_associare = (Buono_carico_scarico_dettBulk)bp.getRigheInventarioDaDocumento().getModel();

			if (riga_da_associare.isAssociatoConAccessorioContestuale()){
				riga_da_associare.setFl_bene_accessorio(new Boolean(false));
				throw new it.cnr.jada.comp.ApplicationException("Attenzione: questo bene non può essere accessorio poichè ha dei beni associati ad esso");
			}
			Buono_carico_scaricoBulk buonoCS = (Buono_carico_scaricoBulk)bp.getModel();
			Documento_generico_rigaBulk dett_selezionato = (Documento_generico_rigaBulk)bp.getDettagliDocumento().getModel();

			it.cnr.jada.bulk.BulkList dettagli;
			fillModel(context);
			Buono_carico_scarico_dettBulk riga = null;
			java.util.Vector selectedModels = new java.util.Vector();
			for (java.util.Enumeration e = buonoCS.getDettagliRigheDocHash().keys(); e.hasMoreElements();) {
				Documento_generico_rigaBulk dett = (Documento_generico_rigaBulk)e.nextElement();
				if (dett != dett_selezionato){
					dettagli = (it.cnr.jada.bulk.BulkList)buonoCS.getDettagliRigheDocHash().get(dett);
					for (java.util.Iterator i = dettagli.iterator(); i.hasNext();){
						riga = (Buono_carico_scarico_dettBulk)i.next();
						if(!riga.getFl_accessorio_contestuale()&&
						  (riga_da_associare.getCat_voce()!=null &&
						   riga.getCat_voce()!=null &&
						   riga_da_associare.getCat_voce().getCd_categoria_gruppo().compareTo(riga.getCat_voce().getCd_categoria_gruppo())==0))
						 if ((!riga.isBeneAccessorio()) && (riga.getQuantita()!=null) && (riga.getQuantita().compareTo(new Long(1))==0))				
							selectedModels.add(riga);
					}
				}
			}

			if (selectedModels.size()==0){
				riga_da_associare.setFl_accessorio_contestuale(Boolean.FALSE);
				riga_da_associare.setFl_bene_accessorio(Boolean.FALSE);
				throw new it.cnr.jada.comp.ApplicationException("Non ci sono elementi validi da associare come Bene Principale");
			}
				
			it.cnr.jada.util.action.SelezionatoreListaBP slbp = (it.cnr.jada.util.action.SelezionatoreListaBP) select(
				context,
				new it.cnr.jada.util.ListRemoteIterator(selectedModels),
				it.cnr.jada.bulk.BulkInfo.getBulkInfo(Buono_carico_scarico_dettBulk.class),
				"righeSet",
				"doBringBackAccessoriContestualiByDocumento");
			slbp.setMultiSelection(false);
			return slbp;
		} catch(Throwable e) {
			return handleException(context,e);
		}	
	}
	public Forward doBringBackAccessoriContestualiByDocumento(ActionContext context) {

		try {
			HookForward caller = (HookForward)context.getCaller();
			CRUDCaricoInventarioBP bp = (CRUDCaricoInventarioBP)getBusinessProcess(context);
			Buono_carico_scaricoBulk buonoCS = (Buono_carico_scaricoBulk)bp.getModel();		
			Buono_carico_scarico_dettBulk selezionato= (Buono_carico_scarico_dettBulk)caller.getParameter("focusedElement");
			Buono_carico_scarico_dettBulk riga = new Buono_carico_scarico_dettBulk();
				
			riga = (Buono_carico_scarico_dettBulk)bp.getRigheInventarioDaDocumento().getModel();	
			java.util.List selectedModels = (java.util.List)caller.getParameter("selectedElements");
			    
			if (selectedModels != null && !selectedModels.isEmpty() ){
				riga.getBene().setBene_principale(new Inventario_beniBulk());
				riga.getBene().getBene_principale().setDs_bene(selezionato.getBene().getDs_bene());	
				riga.getBene().setUbicazione(selezionato.getBene().getUbicazione());
				riga.getBene().setAssegnatario(selezionato.getBene().getAssegnatario());
				riga.setFl_bene_accessorio(Boolean.TRUE);
				riga.setFl_accessorio_contestuale(Boolean.TRUE);
				bp.setProgressivo_beni(buonoCS.addToAccessoriContestualiHash(selezionato, riga, bp.getProgressivo_beni()));
				
			}
			else {
				riga.setFl_accessorio_contestuale(new Boolean(false));
			}
			return context.findDefaultForward();
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
	public Forward doCreaDettagliDoc(ActionContext context) {	

		CRUDCaricoInventarioBP bp = (CRUDCaricoInventarioBP)getBusinessProcess(context);	
		Buono_carico_scaricoBulk buono_cs = (Buono_carico_scaricoBulk)bp.getModel();
		Buono_carico_scarico_dettBulk rigoInventario = new Buono_carico_scarico_dettBulk();
		it.cnr.jada.bulk.SimpleBulkList totaleRigheInventario = new it.cnr.jada.bulk.SimpleBulkList();
		if (buono_cs==null)
			return handleException(context, new Exception());
			
		it.cnr.jada.bulk.PrimaryKeyHashtable docTable = buono_cs.getDettagliRigheDocHash();
		
		// Controlla che siano stati specificati dei gruppi per ogni riga di Fattura
		try{
			validaCreaDettagliDoc(context,docTable);
		} catch (it.cnr.jada.bulk.ValidationException ve){
			return handleException(context, ve);
		}
		
		for (java.util.Enumeration e = docTable.elements(); e.hasMoreElements();){
			BulkList righeInventario = (BulkList)e.nextElement();

			for (java.util.Iterator i = righeInventario.iterator(); i.hasNext();){
				rigoInventario = (Buono_carico_scarico_dettBulk)i.next();
				totaleRigheInventario.add(rigoInventario);
			}
		}

		buono_cs.setBuono_carico_scarico_dettColl(totaleRigheInventario);
		bp.setFirst(false);
		return context.findDefaultForward();
	}
	private void validaCreaDettagliDoc(ActionContext context, PrimaryKeyHashtable righeHash) throws it.cnr.jada.bulk.ValidationException{
		
		java.math.BigDecimal totale;
		
		for (java.util.Enumeration e = righeHash.keys(); e.hasMoreElements();){
			totale = new java.math.BigDecimal(0);
			Documento_generico_rigaBulk riga = (Documento_generico_rigaBulk)e.nextElement();
			
			it.cnr.jada.bulk.BulkList righeInventario = (it.cnr.jada.bulk.BulkList)righeHash.get(riga);

			// Non sono stati specificati dei gruppi per una riga di Fattura
			if (righeInventario.size()==0){
				throw new it.cnr.jada.bulk.ValidationException ("Attenzione: " +
					"è necessario specificare dei gruppi per ogni riga di Documento.\n" + 
					"Non sono stati specificati dei gruppi per la riga di documento '"+
					riga.getDs_riga() + 
					"'");
			}
			for (java.util.Iterator i = righeInventario.iterator(); i.hasNext();){
				Buono_carico_scarico_dettBulk riga_inventario = (Buono_carico_scarico_dettBulk)i.next();
				// Non Þ stata indicata la quantitÓ nella riga di dettaglio del Buono di Carico
				if (riga_inventario.getQuantita()==null || (riga_inventario.getQuantita().compareTo(new Long(0)))==0){
					throw new it.cnr.jada.bulk.ValidationException ("Attenzione: "+
						"uno o più dettagli specificati per la riga di Documento '" + 
						riga.getDs_riga() + 
						"' non hanno Quantità");
				}
				totale = totale.add(new BigDecimal(riga_inventario.getQuantita().longValue()));
							
				riga_inventario.setTi_documento("C");
		 }
		}
	}

}
