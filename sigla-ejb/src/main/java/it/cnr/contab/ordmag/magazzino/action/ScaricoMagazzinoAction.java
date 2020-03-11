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

package it.cnr.contab.ordmag.magazzino.action;

import java.util.List;

import it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk;
import it.cnr.contab.ordmag.anag00.UnitaMisuraBulk;
import it.cnr.contab.ordmag.magazzino.bp.ScaricoManualeMagazzinoBP;
import it.cnr.contab.ordmag.magazzino.bulk.BollaScaricoMagBulk;
import it.cnr.contab.ordmag.magazzino.bulk.ScaricoMagazzinoBulk;
import it.cnr.contab.ordmag.magazzino.bulk.ScaricoMagazzinoRigaBulk;
import it.cnr.contab.ordmag.magazzino.ejb.MovimentiMagComponentSession;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.bulk.BulkInfo;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.OptionBP;
import it.cnr.jada.util.action.SelezionatoreListaBP;

public class ScaricoMagazzinoAction extends it.cnr.jada.util.action.CRUDAction {
	private static final long serialVersionUID = 1L;

	public ScaricoMagazzinoAction() {
        super();
    }
	
	public Forward doBlanckSearchFindUnitaOperativaOrd(ActionContext context, ScaricoMagazzinoBulk scaricoMagazzino) {
		scaricoMagazzino.setMagazzinoAbilitato(null);
		return context.findDefaultForward();
	}

	public Forward doBringBackSearchFindBeneServizio(ActionContext context, ScaricoMagazzinoRigaBulk scaricoRiga, Bene_servizioBulk beneServizio) {
		try {
			fillModel(context);
			if (beneServizio!=null) {
				ScaricoManualeMagazzinoBP bp = (ScaricoManualeMagazzinoBP)getBusinessProcess(context);
				bp.inizializeBeneServizio(context, scaricoRiga, beneServizio);
				bp.setDirty(true);
			}
			return context.findDefaultForward();
		}catch (Exception ex) {
			return handleException(context, ex);
		}
	}
	
	public Forward doBringBackSearchFindUnitaMisura(ActionContext context, ScaricoMagazzinoRigaBulk scaricoRiga, UnitaMisuraBulk unitaMisura) {
		try {
			fillModel(context);
			if (unitaMisura!=null) {
				ScaricoManualeMagazzinoBP bp = (ScaricoManualeMagazzinoBP)getBusinessProcess(context);
				bp.inizializeUnitaMisura(context, scaricoRiga, unitaMisura);
				bp.setDirty(true);
			}
			return context.findDefaultForward();
		}catch (Exception ex) {
			return handleException(context, ex);
		}
	}
	
	public Forward doBlanckSearchFindUnitaMisura(ActionContext context, ScaricoMagazzinoRigaBulk scaricoRiga) {
		scaricoRiga.setCoefConv(null);
		return context.findDefaultForward();
	}

	public Forward doScaricaMagazzino(ActionContext context) {
		try {
			fillModel(context);
			ScaricoManualeMagazzinoBP bp = (ScaricoManualeMagazzinoBP)getBusinessProcess(context);
			bp.completeSearchTools(context, bp);
			bp.getBeniServiziColl().validate(context);
			bp.completeSearchTools(context, bp.getBeniServiziColl());
			return openConfirm(context, "Attenzione! Confermi lo scarico?", OptionBP.CONFIRM_YES_NO, "doConfirmScaricaMagazzino");
		} catch(Exception e) {
			return handleException(context,e);
		}
	}

	public Forward doConfirmScaricaMagazzino(ActionContext context,int option) {
		try {
			if ( option == OptionBP.YES_BUTTON) 
			{
				fillModel(context);
				ScaricoManualeMagazzinoBP bp = (ScaricoManualeMagazzinoBP)getBusinessProcess(context);
				List<BollaScaricoMagBulk> listaBolleScarico = bp.scaricaMagazzino(context).getBolleScaricoColl();

				if (listaBolleScarico!=null && !listaBolleScarico.isEmpty()){
					SelezionatoreListaBP nbp = (SelezionatoreListaBP)context.createBusinessProcess("BolleScaricoGenerate");
					nbp.setMultiSelection(false);

					RemoteIterator iterator = ((MovimentiMagComponentSession)bp.createComponentSession()).preparaQueryBolleScaricoDaVisualizzare(context.getUserContext(), listaBolleScarico);
					
					nbp.setIterator(context,iterator);
					BulkInfo bulkInfo = BulkInfo.getBulkInfo(BollaScaricoMagBulk.class);
					nbp.setBulkInfo(bulkInfo);
					nbp.setColumns(bulkInfo.getColumnFieldPropertyDictionary("bollaScaricoforPrint"));
					return context.addBusinessProcess(nbp);
				} else
					//se arrivato qui vuol dire che ci sono anomalie
					bp.setMessage("Operazione effettuata. Verificare le anomalie riportate sui beni non scaricati.");
			}
			return context.findDefaultForward();
		} catch(Exception e) {
			return handleException(context,e);
		}
	}

	public Forward doOnDtCompetenzaChange(ActionContext context) {
		ScaricoManualeMagazzinoBP bp = (ScaricoManualeMagazzinoBP)getBusinessProcess(context);
		ScaricoMagazzinoBulk scarico = (ScaricoMagazzinoBulk)bp.getModel();
	
		java.sql.Timestamp oldDate=null;
		if (scarico.getDataCompetenza()!=null)
			oldDate = (java.sql.Timestamp)scarico.getDataCompetenza().clone();
	
		try {
			fillModel(context);
			scarico.validaDate();
			return context.findDefaultForward();
		}
		catch (Exception ex) {
			// In caso di errore ripropongo la data precedente
			scarico.setDataCompetenza(oldDate);
			try
			{
				return handleException(context, ex);			
			}
			catch (Exception e) 
			{
				return handleException(context, e);
			}
		}
	}
}
