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

package it.cnr.contab.doccont00.action;

import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import java.util.*;
import it.cnr.contab.doccont00.core.bulk.MandatoBulk;
import it.cnr.contab.doccont00.core.bulk.Mandato_rigaBulk;
import it.cnr.contab.doccont00.bp.*;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.util.action.*;
/**
 * Azione che gestisce le richieste relative alla Gestione Documenti Contabili
 * (Mandato di Accreditamento)
 */
public class CRUDMandatoAccreditamentoAction extends CRUDAbstractMandatoAction {
public CRUDMandatoAccreditamentoAction() {
	super();
}
public Forward doAddToCRUDMain_Impegni(ActionContext context)
{
	try 
	{
		CRUDMandatoAccreditamentoBP bp = (CRUDMandatoAccreditamentoBP)context.getBusinessProcess();
		it.cnr.jada.util.RemoteIterator ri = bp.cercaImpegni(context);
		ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context, ri);
		if (ri == null || ri.countElements() == 0) {
			it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, ri);
			bp.setMessage("La ricerca non ha fornito alcun risultato.");
			return context.findDefaultForward();
/*		}
		else if (ri.countElements() == 1) {
			OggettoBulk bulk = (OggettoBulk)ri.nextElement();
			if (ri instanceof javax.ejb.EJBObject)
				((javax.ejb.EJBObject)ri).remove();
			bp.setMessage("La ricerca ha fornito un solo risultato.");
			bp.edit(context,bulk);
			return context.findDefaultForward();*/
		} else {
	//		bp.setModel(context,filtro);
			BulkInfo bulkInfo = BulkInfo.getBulkInfo(V_impegnoBulk.class);
			SelezionatoreListaImpegniBP nbp = (SelezionatoreListaImpegniBP)context.createBusinessProcess("SelezionatoreListaImpegniBP");
			nbp.setColumns( bulkInfo.getColumnFieldPropertyDictionary("impegni")); 
			nbp.setIterator(context,ri);
			nbp.setMultiSelection( true );
//			nbp.setBulkInfo(bulkInfo);
			context.addHookForward("seleziona",this,"doRiportaSelezioneImpegni");
			return context.addBusinessProcess(nbp);
		}
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
/**
 * Metodo utilizzato per emettere il mandato relativo al CdS selezionato.
 	 * @param context <code>ActionContext</code> in uso.
	 *
	 * @return <code>Forward</code>
 */

public Forward doApriMandatoWizard(ActionContext context) 
{
	try 
	{
		fillModel( context );
		SituazioneCdSBP bp = (SituazioneCdSBP)getBusinessProcess(context);
		V_disp_cassa_cdsBulk cds = (V_disp_cassa_cdsBulk) bp.getCds().getModel();
		cds.validate();
		MandatoAccreditamentoWizardBP wizard = 
			(MandatoAccreditamentoWizardBP) context.createBusinessProcess( 
				"MandatoAccreditamentoWizardBP", 
				new Object[] {"M", 
							   cds.getCd_cds(), 
							   cds.getIm_da_trasferire() } );		
		return context.addBusinessProcess(wizard);			
	}		
	catch(Throwable e) {
		return handleException(context,e);
	}
}
/**
 * Gestisce il caricamento di nuovi impegni
 	 * @param context <code>ActionContext</code> in uso.
	 * @param nuovaLatt Oggetto di tipo <code>Linea_attivitaBulk</code> (istanza doc contabili)
	 * @param latt Oggetto di tipo <code>Linea_attivitaBulk</code>
	 *
	 * @return <code>Forward</code>
 */
public Forward doBringBackSearchFindVoce(ActionContext context, MandatoAccreditamento_rigaBulk riga, V_impegnoBulk impegno) 
{
	try 
	{
		if (impegno != null )
		{
			riga.getMandatoAccreditamento().validateNuovoImpegno( impegno );
			riga.setImpegno( impegno );
		}	
		return context.findDefaultForward();
	}
	catch(ValidationException e) 
	{
		getBusinessProcess(context).setErrorMessage(e.getMessage());
		return context.findDefaultForward();
	}		
	
	catch(Throwable e) {return handleException(context,e);}
}
/**
 * Gestisce il cambio del flag imputazione automatica o manuale delle voci
 * di bilancio CNR nel mandato di accreditamento.
 	 * @param context <code>ActionContext</code> in uso.
	 *
	 * @return <code>Forward</code>
 */

public Forward doCambiaFl_imputazione_manuale(ActionContext context)
{
	try
	{
		fillModel( context );
		return context.findDefaultForward();	
	}		
	catch(Throwable e) 
	{
		return handleException(context,e);
	}
	
}
/**
 * Gestisce il cambio delle modalita di pagamento
 	 * @param context <code>ActionContext</code> in uso.
	 *
	 * @return <code>Forward</code>
 */

public Forward doCambiaModalitaPagamento(ActionContext context)
{
	try
	{
		fillModel( context );
		CRUDBP bp = (CRUDBP) getBusinessProcess( context );
		if ( bp instanceof MandatoAccreditamentoWizardBP )
			((MandatoAccreditamentoWizardBP)bp).cambiaModalitaPagamento( context );
/*		else	if ( bp instanceof CRUDMandatoAccreditamentoBP )
			((CRUDMandatoAccreditamentoBP)bp).cambiaModalitaPagamento( context );		*/
		return context.findDefaultForward();	
	}		
	catch(Throwable e) 
	{
		return handleException(context,e);
	}
	
}
/**
 * Gestisce il caricamento di tutti i centri di spesa e della loro disponibilità di cassa 
	 * @param context <code>ActionContext</code> in uso.
	 *
	 * @return <code>Forward</code>
 */
public Forward doCercaCds(ActionContext context) 
{
	try 
	{
		SituazioneCdSBP bp = (SituazioneCdSBP)getBusinessProcess(context);
		fillModel( context );
		bp.cercaTuttiCds( context );
		return context.findDefaultForward();
	}
	catch(BusinessProcessException e) {
		return handleException(context,e);
	}		
	catch(Throwable e) {
		return handleException(context,e);
	}
}
/**
 * Gestisce il caricamento delle obbligazioni in scadenza per i centri di spesa selezionati
	 * @param context <code>ActionContext</code> in uso.
	 *
	 * @return <code>Forward</code>
 */
public Forward doCercaObbligazioni(ActionContext context) 
{
	try 
	{
		SituazioneCdSBP bp = (SituazioneCdSBP)getBusinessProcess(context);
		fillModel( context );
		bp.cercaObbligazioniCds( context );
		return context.findDefaultForward();
	}
	catch(BusinessProcessException e) {
		return handleException(context,e);
	}		
	catch(Throwable e) {
		return handleException(context,e);
	}
}
/**
 * Metodo utilizzato per gestire l'aggiunta di nuove righe al mandato
 */

public Forward doConfermaEmettiMandato(ActionContext context,int option) 
{
	try {
		MandatoAccreditamentoWizardBP bp = (MandatoAccreditamentoWizardBP) getBusinessProcess(context);
		MandatoAccreditamentoWizardBulk wizard = (MandatoAccreditamentoWizardBulk)bp.getModel();		
		if (option == OptionBP.YES_BUTTON)
		{
			if ( wizard.getImpegniSelezionatiColl().size() == 0 )
				if ( wizard.getFl_imputazione_manuale())
					setMessage( context,0, "E' necessario specificare almeno un importo da trasferire!");
				else
					setMessage( context,0, "E' necessario specificare almeno una priorità!");				
			else
			{
				wizard.validate();
				bp.create(context);
			}	
		}	
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}

}
/**
 * Metodo utilizzato per gestire l'aggiunta di nuove righe al mandato
 */

public Forward doEmettiMandato(ActionContext context) 
{
	try 
	{
		fillModel( context );
		MandatoAccreditamentoWizardBP bp = (MandatoAccreditamentoWizardBP)getBusinessProcess(context);
		MandatoAccreditamentoWizardBulk wizard = (MandatoAccreditamentoWizardBulk)bp.getModel();
		wizard.selezionaImpegni();
		if ( !wizard.getFl_imputazione_manuale() )
			wizard.assegnaImportiInBaseAPriorita();
		if ( wizard.getIm_totale_impegni_selezionati().compareTo( wizard.getIm_mandato()) != 0 )
			return openConfirm(context,"Il totale mandato e' diverso dalla somma degli importi da trasferire. Vuoi continuare?",OptionBP.CONFIRM_YES_NO,"doConfermaEmettiMandato");
			
		return doConfermaEmettiMandato( context, OptionBP.YES_BUTTON);
	}		
	catch(Throwable e) {
		return handleException(context,e);
	}
}
/**
 * Gestisce la selezione dei sospesi
 *
 */
public Forward doRiportaSelezioneImpegni(ActionContext context)
{
	
	try 
	{
		CRUDMandatoAccreditamentoBP bp = (CRUDMandatoAccreditamentoBP)context.getBusinessProcess();
		bp.aggiungiImpegni( context );
		return context.findDefaultForward();
	} catch(Throwable e) 
	{
		return handleException(context,e);
	}
	
}
/**
 * Metodo utilizzato per visualizzare le entrate relative al CdS selezionato.
 	 * @param context <code>ActionContext</code> in uso.
	 *
	 * @return <code>Forward</code>
 */

public Forward doVisualizzaEntrate(ActionContext context) 
{
	try 
	{
		String cd_cds;
		fillModel( context );
		OggettoBulk bulk = getBusinessProcess( context ).getModel();
		if ( getBusinessProcess( context ) instanceof SituazioneCdSBP )
		{
			SituazioneCdSBP bp = (SituazioneCdSBP)getBusinessProcess(context);
			V_disp_cassa_cdsBulk cds = (V_disp_cassa_cdsBulk) bp.getCds().getModel();
			cd_cds =  cds.getCd_cds() ;
		}
		else 
			cd_cds = ((MandatoAccreditamentoBulk)bulk).getCodice_cds();
			
		ViewBilancioCdSBP view = (ViewBilancioCdSBP) context.createBusinessProcess( "ViewEntrateCdSBP", new Object[]{ "V" , cd_cds} );		
		return context.addBusinessProcess(view);		
	}		
	catch(Throwable e) {
		return handleException(context,e);
	}
}
/**
 * Metodo utilizzato per gestire l'aggiunta di nuove righe al mandato
 */

public Forward doVisualizzaMandato(ActionContext context) 
{
	try 
	{
		MandatoAccreditamentoWizardBP bp = (MandatoAccreditamentoWizardBP)getBusinessProcess(context);
		if ( bp.getMandati().getModel() != null )
		{
			MandatoAccreditamentoWizardBulk wizard = (MandatoAccreditamentoWizardBulk) bp.getModel();
			CRUDMandatoAccreditamentoBP crud = (CRUDMandatoAccreditamentoBP) context.createBusinessProcess( "CRUDMandatoAccreditamentoBP", new Object[] {"V" } );
			crud.edit( context,bp.getMandati().getModel(), true );
			return context.addBusinessProcess(crud);
		}
		return context.findDefaultForward();	
	}		
	catch(Throwable e) {
		return handleException(context,e);
	}
}
/**
 * Metodo utilizzato per visualizzare le spese relative al CdS selezionato.
 	 * @param context <code>ActionContext</code> in uso.
	 *
	 * @return <code>Forward</code>
 */

public Forward doVisualizzaSpese(ActionContext context) 
{
	try 
	{
		String cd_cds;
		fillModel( context );
		OggettoBulk bulk = getBusinessProcess( context ).getModel();
		if ( getBusinessProcess( context ) instanceof SituazioneCdSBP )
		{
			SituazioneCdSBP bp = (SituazioneCdSBP)getBusinessProcess(context);
			V_disp_cassa_cdsBulk cds = (V_disp_cassa_cdsBulk) bp.getCds().getModel();
			cd_cds =  cds.getCd_cds() ;
		}
		else 
			cd_cds = ((MandatoAccreditamentoBulk)bulk).getCodice_cds();
			
		ViewBilancioCdSBP view = (ViewBilancioCdSBP) context.createBusinessProcess( "ViewSpeseCdSBP", new Object[]{ "V" , cd_cds} );		
		return context.addBusinessProcess(view);		
	}		
	catch(Throwable e) {
		return handleException(context,e);
	}
}
}
