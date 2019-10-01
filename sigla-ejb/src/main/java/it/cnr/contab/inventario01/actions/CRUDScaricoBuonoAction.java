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

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import it.cnr.contab.docamm00.docs.bulk.Documento_generico_rigaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_rigaIBulk;
import it.cnr.contab.docamm00.docs.bulk.Nota_di_credito_rigaBulk;
import it.cnr.contab.inventario01.bp.CRUDScaricoInventarioBP;
import it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk;
import it.cnr.contab.inventario00.docs.bulk.Trasferimento_inventarioBulk;
import it.cnr.contab.inventario00.tabrif.bulk.Tipo_carico_scaricoBulk;
import it.cnr.contab.inventario01.bulk.*;
import it.cnr.contab.inventario01.ejb.BuonoCaricoScaricoComponentSession;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.PrimaryKeyHashtable;
import it.cnr.jada.bulk.SimpleBulkList;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.util.action.*;
public class CRUDScaricoBuonoAction extends it.cnr.jada.util.action.CRUDAction {
	private List deletedList;
/**
 * CRUDScaricoInventarioAction constructor comment.
 */
public CRUDScaricoBuonoAction() {
	super();
}
public Forward doSelezionaTipoMovimento(ActionContext context) {	
	try {		
		CRUDScaricoInventarioBP bp = (CRUDScaricoInventarioBP)getBusinessProcess(context);	
		Buono_carico_scaricoBulk buono = (Buono_carico_scaricoBulk)bp.getModel();
		Tipo_carico_scaricoBulk tipoMovimento = buono.getTipoMovimento();
		fillModel(context);
		if (tipoMovimento != null && buono.getTipoMovimento() != null){
			if (bp.isEditing() ||bp.getDettController().countDetails()!=0){
				if(bp.isAssociataTestata(context.getUserContext(), buono)||bp.isContabilizzato(context.getUserContext(), buono)||
							(buono.getTipoMovimento().getFl_vendita().booleanValue()!=tipoMovimento.getFl_vendita().booleanValue())||
						(buono.getTipoMovimento().getFl_storno_fondo().booleanValue()!=tipoMovimento.getFl_storno_fondo().booleanValue()))
				{
						buono.setTipoMovimento(tipoMovimento);
						throw new ApplicationException("Cambio tipologia buono non possibile.");
					}
				}
			}
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
/**
  *  Gestisce l'aggiunta di un nuovo bene nel Buono di Scarico.
  *	E' stata generata la richiesta di aggiungere un bene al Buono di Scarico.
  *	Viene creato un iteratore sui beni che hanno i requisiti per essere scaricati, 
  * Viene proposta all'utente una finestra di ricerca guidata sui beni disponibili.
  *
  * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
  *
  * @return forward <code>Forward</code>
**/
public Forward doAddToCRUDMain_DettController(ActionContext context) {
	try {
		fillModel(context);
		CRUDScaricoInventarioBP bp = (CRUDScaricoInventarioBP)getBusinessProcess(context);
		Buono_carico_scaricoBulk buonoS =(Buono_carico_scaricoBulk)bp.getModel();
		boolean no_accessori = buonoS.isByFattura();
		if (!(buonoS instanceof Trasferimento_inventarioBulk) && buonoS.getTipoMovimento() == null){
			return handleException(context, new it.cnr.jada.bulk.ValidationException("Attenzione: specificare un tipo di movimento nella testata"));
		}else if(buonoS instanceof Trasferimento_inventarioBulk){
		  Trasferimento_inventarioBulk trasf =(Trasferimento_inventarioBulk)buonoS;
			// CONTROLLA CHE SIA STATO SPECIFICATO UN TIPO DI MOVIMENTO DI CARICO
			if (trasf.getTipoMovimentoCarico()==null)
				throw new it.cnr.jada.comp.ApplicationException("Attenzione: specificare un Tipo di Movimento Carico");
			// CONTROLLA CHE SIA STATO SPECIFICATO UN TIPO DI MOVIMENTO DI SCARICO
			if (trasf.getTipoMovimentoScarico()==null)
				throw new it.cnr.jada.comp.ApplicationException("Attenzione: specificare un Tipo di Movimento Scarico");
			// CONTROLLA CHE SIA STATA SPECIFICATA UNA DESCRIZIONE 
			if (trasf.getDs_buono_carico_scarico()==null)
				throw new it.cnr.jada.comp.ApplicationException("Attenzione: indicare una Descrizione per il Buono di Scarico");
		}
		if (buonoS.getData_registrazione()==null)
			return handleException(context, new it.cnr.jada.bulk.ValidationException("Attenzione: specificare la data scarico"));
		bp.getDettController().validate(context);
		SimpleBulkList selezionati=((it.cnr.contab.inventario01.ejb.BuonoCaricoScaricoComponentSession)bp.createComponentSession()).selezionati(context.getUserContext(),(Buono_carico_scaricoBulk)bp.getModel());
		it.cnr.jada.util.RemoteIterator ri = ((it.cnr.contab.inventario01.ejb.BuonoCaricoScaricoComponentSession)bp.createComponentSession()).cercaBeniScaricabili(context.getUserContext(),(Buono_carico_scaricoBulk)bp.getModel(),no_accessori,selezionati,null);
		ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context,ri);
		int count = ri.countElements();

		/*  Non ci sono beni disponibili */
		if (count == 0) {
			bp.setMessage("Nessun Bene recuperato");
			it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, ri);
		} else {
			RicercaLiberaBP rlbp = (RicercaLiberaBP)context.createBusinessProcess("RicercaLibera");
			rlbp.setCanPerformSearchWithoutClauses(false);
			rlbp.setPrototype(new Inventario_beniBulk());
			context.addHookForward("searchResult",this,"doBringBackAddBeni");
			context.addHookForward("filter",this,"doBringBackAddBeni");
			return context.addBusinessProcess(rlbp);
		}		
		return context.findDefaultForward();	
		
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
/**
 *  Gestisce l'aggiunta di un nuovo bene nel Buono di Scarico generato da Fattura Attiva.
 *	L'utente sta creando un Buono di Scarico partendo dalla creazione di una Fattura Attiva.
 *	Prima di tutto, il metodo controlla che sia stata selezionata una riga dalla finestra 
 *	dei dettagli della Fattura; in seguito, costruisce un iteratore sui beni che hanno i requisiti 
 *	per essere associati ad una riga di fattura e scaricati, (metodo BuonoScaricoComponent.cercaBeniAssociabili).
 * Infine viene proposta all'utente una finestra di ricerca guidata sui beni disponibili.
 *
 * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
 *
 * @return forward <code>Forward</code>
**/
public Forward doAddToCRUDMain_DettagliFattura_RigheInventarioDaFattura(ActionContext context) {
	
	try {
		fillModel(context);
		CRUDScaricoInventarioBP bp_scarico = (CRUDScaricoInventarioBP)getBusinessProcess(context);
		if (bp_scarico.getModel()!=null &&((Buono_carico_scaricoBulk)bp_scarico.getModel()).getData_registrazione()==null)
			return handleException(context, new it.cnr.jada.bulk.ValidationException("Attenzione: specificare la data scarico"));
	
		List fatture = bp_scarico.getDettagliFattura().getSelectedModels(context);
		Fattura_attiva_rigaIBulk riga_fattura=null;
		Nota_di_credito_rigaBulk nota=null;
		if (fatture.size()==0|| bp_scarico.getDettagliFattura().getModel()== null)
			throw new it.cnr.jada.bulk.ValidationException("Attenzione: selezionare una riga di Fattura");
		else if (fatture.size()>1)
			throw new it.cnr.jada.bulk.ValidationException("Attenzione: selezionare una riga di Fattura per volta");
		if (bp_scarico.getDettagliFattura().getModel().getClass().equals(Fattura_attiva_rigaIBulk.class))
			 riga_fattura = (Fattura_attiva_rigaIBulk)bp_scarico.getDettagliFattura().getModel();
		else
			 nota = (Nota_di_credito_rigaBulk)bp_scarico.getDettagliFattura().getModel();
		/* Nessuna riga di Fattura selezionata */
		if (riga_fattura == null && nota==null){
			throw new it.cnr.jada.bulk.ValidationException("Attenzione: selezionare una riga di Fattura");
		}

		it.cnr.jada.util.RemoteIterator ri=null;
		if (riga_fattura != null){
			 ri =((BuonoCaricoScaricoComponentSession)bp_scarico.createComponentSession()).cercaBeniAssociabili(context.getUserContext(),(Buono_carico_scaricoBulk)bp_scarico.getModel(),riga_fattura,null);
		}else{
			ri=((BuonoCaricoScaricoComponentSession)bp_scarico.createComponentSession()).cercaBeniAssociabili(context.getUserContext(),(Buono_carico_scaricoBulk)bp_scarico.getModel(),nota,null);
		}
		ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context,ri);
		int count = ri.countElements();
		/* Non ci sono beni disponibili ad essere associati */
		if (count == 0) {
			bp_scarico.setMessage("Nessun Bene associabile");
			it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, ri);
		} else {
			//SelezionatoreListaBP slbp = select(context,ri,it.cnr.jada.bulk.BulkInfo.getBulkInfo(Inventario_beniBulk.class),null,"doSelezionaBeniScaricati",null,bp_scarico);
			//slbp.setMultiSelection(true);

			/* Crea BP di ricerca guidata */
			RicercaLiberaBP rlbp = (RicercaLiberaBP)context.createBusinessProcess("RicercaLibera");
			rlbp.setCanPerformSearchWithoutClauses(false);
			//rlbp.setSearchProvider(bp_scarico.getBeneSearchProvider(context));		
			it.cnr.contab.inventario00.ejb.Inventario_beniComponentSession inventario_component = (it.cnr.contab.inventario00.ejb.Inventario_beniComponentSession)bp_scarico.createComponentSession("CNRINVENTARIO00_EJB_Inventario_beniComponentSession",Inventario_beniBulk.class);
			Inventario_beniBulk model_for_search = (Inventario_beniBulk)inventario_component.inizializzaBulkPerInserimento(context.getUserContext(),new Inventario_beniBulk());
			rlbp.setFreeSearchSet("freeSearchWithoutProgressivoSet");
			rlbp.setPrototype(model_for_search);
			//rlbp.setMultiSelection(true);
			rlbp.setShowSearchResult(false);
			context.addHookForward("searchResult",this,"doBringBackAddBeni");
			context.addHookForward("filter",this,"doBringBackAddBeni");
			return context.addBusinessProcess(rlbp);
		}
		
		return context.findDefaultForward();	
		
	} catch(Throwable e) {
		return handleException(context,e);
	}	
	
}
public Forward doBringBackAddBeni(ActionContext context) {

	try {
		HookForward fwd = (HookForward)context.getCaller();
		CRUDScaricoInventarioBP bp = (CRUDScaricoInventarioBP)getBusinessProcess(context);
		Buono_carico_scaricoBulk buonoS =(Buono_carico_scaricoBulk)bp.getModel();
		boolean no_accessori = buonoS.isByFattura();
		SimpleBulkList selezionati=null;
		it.cnr.jada.persistency.sql.CompoundFindClause clauses = (it.cnr.jada.persistency.sql.CompoundFindClause)fwd.getParameter("filter");
		bp.setClauses(clauses);
		context.addHookForward("filter",this,"doSelezionaBeniScaricati");
		if(buonoS.isByFattura() && bp.getDettagliFattura()!=null && bp.getDettagliFattura().countDetails()!=0 && !(bp.getDettagliFattura().getModel() instanceof Nota_di_credito_rigaBulk))
			 selezionati=((it.cnr.contab.inventario01.ejb.BuonoCaricoScaricoComponentSession)bp.createComponentSession()).selezionati(context.getUserContext(),(Buono_carico_scaricoBulk)bp.getModel());
		if(buonoS.isByDocumento() && bp.getDettagliDocumento()!=null && bp.getDettagliDocumento().countDetails()!=0)
			 selezionati=((it.cnr.contab.inventario01.ejb.BuonoCaricoScaricoComponentSession)bp.createComponentSession()).selezionati(context.getUserContext(),(Buono_carico_scaricoBulk)bp.getModel());
		else
			selezionati=((it.cnr.contab.inventario01.ejb.BuonoCaricoScaricoComponentSession)bp.createComponentSession()).selezionati(context.getUserContext(),(Buono_carico_scaricoBulk)bp.getModel());
		
		it.cnr.jada.util.RemoteIterator iterator = bp.getListaBeniDaScaricare(context.getUserContext(),no_accessori,selezionati, clauses);
		iterator = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context,iterator);
		int count = iterator.countElements();
		
		/* Non ci sono beni disponibili ad essere associati */
		if (count == 0) {
			bp.setMessage("Nessun Bene associabile");
			it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, iterator);
		} else{
			SelezionatoreListaBP slbp = select(context,iterator,it.cnr.jada.bulk.BulkInfo.getBulkInfo(Inventario_beniBulk.class),null,"doSelezionaBeniScaricati",null,bp);
			slbp.setMultiSelection(true);
			return slbp;
		}
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
}

public Forward doSelezionaBeniScaricati(ActionContext context) {
	try {
		
		CRUDScaricoInventarioBP bp = (CRUDScaricoInventarioBP)context.getBusinessProcess();
		if (bp.isBy_fattura())
			bp.getRigheInventarioDaFattura().reset(context);
		else if (bp.isBy_documento())
			bp.getRigheInventarioDaDocumento().reset(context);
		else
			bp.getDettController().reset(context);
		return context.findDefaultForward();
	} catch(Exception e) {
		return handleException(context,e);
	}
}

/**
 * E' stata generata la richiesta di scaricare totalmente un bene.
 *  L'utente ha selezionato il check-box "Scarico Totale", nel form del Buono di Scarico.
 *	Il metodo controlla se il bene selzionato ha dei beni accessori ed, eventualmente, li carica,
 *	(metodo CRUDScaricoInventarioBP.getBeniAccessoriFor). Il passo successivo, consiste nel
 *	controllare se tra i beni che sono stati già specificati nel Buono di Carico, ci siano
 *	dei beni accessori del bene indicato: in caso positivo, viene lanciata una eccezzione con
 *	un messaggio che indica all'utente che non può scaricare totalmente un bene se ne Buono
 *	di Scarico sono stati indicati dei suoi accessori.
 *
 * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
 *
 * @return Forward
**/
public Forward doClickFlagScaricoTotale(ActionContext context) throws it.cnr.jada.comp.ApplicationException{

	
	CRUDScaricoInventarioBP bp = (CRUDScaricoInventarioBP)getBusinessProcess(context);	
	Inventario_beniBulk bene = (Inventario_beniBulk)bp.getDettController().getModel();
	Buono_carico_scaricoBulk test_buono = (Buono_carico_scaricoBulk)bp.getModel();
	java.util.List selected_righe_fatt = null;
	try {		
		fillModel(context);
		if (bene.getFl_totalmente_scaricato().booleanValue()){

			if (bp.isBy_fattura()){
				selected_righe_fatt = bp.getDettagliFattura().getSelectedModels(context);
			}else if(bp.isBy_documento()){
				selected_righe_fatt = bp.getDettagliDocumento().getSelectedModels(context);
			}

			// Carica gli eventuali accessori del bene selezionato.
			java.util.List accessori = bp.getBeniAccessoriFor(context.getUserContext(), bene);
			bene.setAccessori(new it.cnr.jada.bulk.SimpleBulkList(accessori));
			
			if (!bene.getAccessori().isEmpty()){
				/* Controlla che non siano presenti nella lista dei beni scaricati, 
				 *	beni accessori del bene selezionato
				*/			
				bp.checkBeniAccessoriAlreadyExistFor(context, (Buono_carico_scaricoBulk)bp.getModel(), bene);
				boolean da_scaricare=false;
				for(Iterator i=bene.getAccessori().iterator();i.hasNext();){
				 Inventario_beniBulk accessorio=(Inventario_beniBulk)i.next();
				 if (accessorio.getFl_totalmente_scaricato()!=null && !accessorio.getFl_totalmente_scaricato().booleanValue())
					 da_scaricare=true;
				}
				if (da_scaricare){
					OptionBP optionBP = openConfirm(context,"Attenzione: il bene selezionato ha degli accessori che devono essere scaricati totalmente. Vuoi continuare?",OptionBP.CONFIRM_YES_NO,"doConfirmScaricoBene");
					optionBP.addAttribute("EditDettagliScarico", bene);
					optionBP.addAttribute("selected_righe_fatt", selected_righe_fatt);
					return optionBP;
				}else{
					bene.setValore_unitario(bene.getValoreBene());
					bp.scaricoTotale(context.getUserContext(),bene,selected_righe_fatt,false);
				}
			}else{
				bene.setValore_unitario(bene.getValoreBene());
				bp.scaricoTotale(context.getUserContext(),bene,selected_righe_fatt,false);
			}
		}else{
			if (!bene.isBeneAccessorio()){
				bp.rimuoviBeniAccessoriFor(context.getUserContext(),bene,selected_righe_fatt);
			}	
			bene.setValore_unitario(new java.math.BigDecimal(0));
			bp.modificaBeneScaricato(context.getUserContext(),bene, null);
			bp.getDettController().reset(context);
		}
		return context.findDefaultForward();
	} catch(Throwable e) {
		bene.setFl_totalmente_scaricato(Boolean.FALSE);
		return handleException(context,e);
	}
}
public Forward doScaricaTutti(ActionContext context) {
	try{
		CRUDScaricoInventarioBP bp = (CRUDScaricoInventarioBP)getBusinessProcess(context);
		bp.scaricaTuttiDef(context);
		bp.getDettController().reset(context);
		return context.findDefaultForward();
	} catch(Throwable e) {
			return handleException(context,e);
	}
}
public Forward doConfirmScaricoBene(ActionContext context, OptionBP optionBP) throws it.cnr.jada.comp.ApplicationException,BusinessProcessException{

	CRUDScaricoInventarioBP bp = (CRUDScaricoInventarioBP)getBusinessProcess(context);
	Inventario_beniBulk bene = (Inventario_beniBulk)optionBP.getAttribute("EditDettagliScarico");
	List selected_righe_fatt = (List)optionBP.getAttribute("selected_righe_fatt");
	if (optionBP.getOption() == OptionBP.YES_BUTTON) {	
		bene.setValore_unitario(bene.getValoreBene());
		bp.scaricoTotale(context.getUserContext(),bene,selected_righe_fatt,true);
		bp.getDettController().reset(context);
	}
	else {
		bene.setFl_totalmente_scaricato(new Boolean(false));
	}
	return context.findDefaultForward();
}
/**
 * Richiamato quando si conferma la variazione del valore alienazione.
 *
 * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
 *
 * @return Forward
**/
public Forward doAssegnaValoreAlienazione(ActionContext context) throws it.cnr.jada.comp.ApplicationException{

	CRUDScaricoInventarioBP bp = (CRUDScaricoInventarioBP)getBusinessProcess(context);
	Documento_generico_rigaBulk riga=null;
	Fattura_attiva_rigaIBulk riga_fattura=null;
	if (bp.isBy_fattura())
		 riga_fattura = (Fattura_attiva_rigaIBulk)bp.getDettagliFattura().getModel();
	else 
		 riga = (Documento_generico_rigaBulk)bp.getDettagliDocumento().getModel();
	Inventario_beniBulk bene = null;		
	try{
		fillModel(context);
		if(riga_fattura!=null){
				bene = (Inventario_beniBulk)bp.getRigheInventarioDaFattura().getModel();
				bp.modificaBeneScaricato(context.getUserContext(), bene, riga_fattura);
		}
		else if(riga!=null){
				bene = (Inventario_beniBulk)bp.getRigheInventarioDaDocumento().getModel();
				bp.modificaBeneScaricato(context.getUserContext(), bene, riga);
		}
		else{
				bene =(Inventario_beniBulk)bp.getDettController().getModel();
				bp.modificaBeneScaricato(context.getUserContext(), bene, null);
		}
	}
	catch (Throwable e){
		return handleException(context,e);
	}
	
	return context.findDefaultForward();
}
/**
 *  Salva il valore indicato nel campo variazione_piu.
 *
 * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
 *
 * @return forward <code>Forward</code>
**/  
public Forward doSalvaVariazioneMeno(ActionContext context) {
	Inventario_beniBulk bene_scaricato=null;
	try {		
		fillModel(context);
		CRUDScaricoInventarioBP bp = (CRUDScaricoInventarioBP)getBusinessProcess(context);
		// Controllo sul valore da scaricare
		if (bp.isBy_fattura())
			bene_scaricato = (Inventario_beniBulk)bp.getRigheInventarioDaFattura().getModel();
		else 
			bene_scaricato = (Inventario_beniBulk)bp.getRigheInventarioDaDocumento().getModel();
		
		int result = bene_scaricato.getVariazione_meno().compareTo(bene_scaricato.getValoreBene());
		// valore da scaricare > valore bene
		if (result >0)
			throw new ApplicationException("Attenzione: il valore indicato nel campo 'Valore Scaricato' del bene nr. " + bene_scaricato.getNumeroBeneCompleto() + "\n non può essere superiore al valore del bene stesso");
		// valore da scaricare = valore bene - questa operazione porta a 0 il valore del bene: 
		//	il bene deve essere scaricato totalmente
		if (result == 0){
			//throw new ApplicationException("Attenzione: la variazione per il bene nr. " + bene_scaricato.getNumeroBeneCompleto() + "\n porta il valore del bene a 0.\nQuesta operazione è possibile solo scaricando totalmente il bene");
			bene_scaricato.setFl_totalmente_scaricato(new Boolean(true));
			((it.cnr.contab.inventario01.ejb.BuonoCaricoScaricoComponentSession)bp.createComponentSession()).scaricaBeniAccessori(context.getUserContext(),(Buono_carico_scaricoBulk)bp.getModel(),bene_scaricato);
		}else
			bene_scaricato.setFl_totalmente_scaricato(new Boolean(false));
	
		bp.modificaBeneAssociatoConBulk(context,bene_scaricato);		
	
	} catch(Exception e) {
		bene_scaricato.setVariazione_meno(new BigDecimal("0"));
		return handleException(context, e);
	} 	
	return context.findDefaultForward();	
}

public Forward doOnData_registrazioneChange(ActionContext context)  {
		try{
			CRUDScaricoInventarioBP bp = (CRUDScaricoInventarioBP)context.getBusinessProcess();
			if (bp.isBy_fattura())
				bp.getRigheInventarioDaFattura().removeAll(context);
			else if (bp.isBy_documento())
				bp.getRigheInventarioDaDocumento().removeAll(context);
			else 
				bp.getDettController().removeAll(context);
			fillModel(context);
		
			Buono_carico_scaricoBulk model=(Buono_carico_scaricoBulk)bp.getModel();
			bp.setModel(context,model);
			return context.findDefaultForward();
		}catch (Throwable t) {
		    return handleException(context, t);
		}
	}
/**
 *  Richiamtao dal FrameWork quando seleziono una riga dalla finestra dei Dettagli Fattura,
 *	permette di cancellare le selezioni fatte precedentemente dal'utente, e pone il
 *	checkBox relativo alla riga selezionata nello stato di CHECKED;
 *
 * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
 *
 * @return forward <code>Forward</code>
**/ 
public Forward doSelectDettagliFattura(ActionContext context) {
	
	try {
		fillModel(context);
		CRUDScaricoInventarioBP bp = (CRUDScaricoInventarioBP)context.getBusinessProcess();
		bp.getDettagliFattura().setSelection(context);
		bp.getDettagliFattura().getSelection().clearSelection();
		bp.getDettagliFattura().getSelection().setSelected(bp.getDettagliFattura().getSelection(context).getFocus());	
		
		return context.findDefaultForward();
	} catch(Exception e) {
		return handleException(context,e);
	}
}

public Forward doAnnullaRiporta(ActionContext context) throws BusinessProcessException {
	
	try{
		fillModel(context);
		CRUDScaricoInventarioBP bp = (CRUDScaricoInventarioBP)context.getBusinessProcess();
		List righe_fattura=null;
		if (bp.isBy_fattura())
			righe_fattura = bp.getDettagliFattura().getDetails();
		else
			righe_fattura = bp.getDettagliDocumento().getDetails();
		((it.cnr.contab.inventario01.ejb.BuonoCaricoScaricoComponentSession)bp.createComponentSession()).annullaRiportaAssFattura_Bene(context.getUserContext(),bp.getModel(), righe_fattura);
		context.closeBusinessProcess();
		HookForward bringBackForward = (HookForward)context.findForward("bringback");
		if (bringBackForward != null)
			return bringBackForward;
		return context.findDefaultForward();
		} catch(Throwable e) {
			return handleException(context, e);
		}
	}
public Forward doAddToCRUDMain_DettagliDocumento_RigheInventarioDaDocumento(ActionContext context) {
	
	try {
		fillModel(context);
		CRUDScaricoInventarioBP bp_scarico = (CRUDScaricoInventarioBP)getBusinessProcess(context);
		if (bp_scarico.getModel()!=null &&((Buono_carico_scaricoBulk)bp_scarico.getModel()).getData_registrazione()==null)
			return handleException(context, new it.cnr.jada.bulk.ValidationException("Attenzione: specificare la data di scarico"));
	
		List documenti = bp_scarico.getDettagliDocumento().getSelectedModels(context);
		
		if (documenti.size()==0|| bp_scarico.getDettagliDocumento().getModel()== null)
			throw new it.cnr.jada.bulk.ValidationException("Attenzione: selezionare una riga di Documento");
		  Documento_generico_rigaBulk documento =(Documento_generico_rigaBulk) bp_scarico.getDettagliDocumento().getModel();
		/* Nessuna riga selezionata */
		if (documento == null ){
			throw new it.cnr.jada.bulk.ValidationException("Attenzione: selezionare una riga di Documento");
		}

		it.cnr.jada.util.RemoteIterator ri =((BuonoCaricoScaricoComponentSession)bp_scarico.createComponentSession()).cercaBeniAssociabili(context.getUserContext(),(Buono_carico_scaricoBulk)bp_scarico.getModel(),documento,null);
		
		ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context,ri);
		int count = ri.countElements();
		/* Non ci sono beni disponibili ad essere associati */
		if (count == 0) {
			bp_scarico.setMessage("Nessun Bene associabile");
			it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, ri);
		} else {
			//SelezionatoreListaBP slbp = select(context,ri,it.cnr.jada.bulk.BulkInfo.getBulkInfo(Inventario_beniBulk.class),null,"doSelezionaBeniScaricati",null,bp_scarico);
			//slbp.setMultiSelection(true);

			/* Crea BP di ricerca guidata */
			RicercaLiberaBP rlbp = (RicercaLiberaBP)context.createBusinessProcess("RicercaLibera");
			rlbp.setCanPerformSearchWithoutClauses(false);
			//rlbp.setSearchProvider(bp_scarico.getBeneSearchProvider(context));		
			it.cnr.contab.inventario00.ejb.Inventario_beniComponentSession inventario_component = (it.cnr.contab.inventario00.ejb.Inventario_beniComponentSession)bp_scarico.createComponentSession("CNRINVENTARIO00_EJB_Inventario_beniComponentSession",Inventario_beniBulk.class);
			Inventario_beniBulk model_for_search = (Inventario_beniBulk)inventario_component.inizializzaBulkPerInserimento(context.getUserContext(),new Inventario_beniBulk());
			rlbp.setFreeSearchSet("freeSearchWithoutProgressivoSet");
			rlbp.setPrototype(model_for_search);
			//rlbp.setMultiSelection(true);
			rlbp.setShowSearchResult(false);
			context.addHookForward("searchResult",this,"doBringBackAddBeni");
			context.addHookForward("filter",this,"doBringBackAddBeni");
			return context.addBusinessProcess(rlbp);
		}
		
		return context.findDefaultForward();	
		
	} catch(Throwable e) {
		return handleException(context,e);
	}	
	
}
public Forward doSelectDettagliDocumento(ActionContext context) {
	
	try {
		fillModel(context);
		CRUDScaricoInventarioBP bp = (CRUDScaricoInventarioBP)context.getBusinessProcess();
		bp.getDettagliDocumento().setSelection(context);
		bp.getDettagliDocumento().getSelection().clearSelection();
		bp.getDettagliDocumento().getSelection().setSelected(bp.getDettagliDocumento().getSelection(context).getFocus());	
		
		return context.findDefaultForward();
	} catch(Exception e) {
		return handleException(context,e);
	}
}
}
