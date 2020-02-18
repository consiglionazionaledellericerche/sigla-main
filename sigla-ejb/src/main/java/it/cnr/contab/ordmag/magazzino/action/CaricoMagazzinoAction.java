/*
 * Copyright (C) 2020  Consiglio Nazionale delle Ricerche
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

import it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk;
import it.cnr.contab.ordmag.anag00.UnitaMisuraBulk;
import it.cnr.contab.ordmag.magazzino.bp.CaricoManualeMagazzinoBP;
import it.cnr.contab.ordmag.magazzino.bulk.CaricoMagazzinoBulk;
import it.cnr.contab.ordmag.magazzino.bulk.CaricoMagazzinoRigaBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqRigaBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.util.action.OptionBP;

public class CaricoMagazzinoAction extends it.cnr.jada.util.action.CRUDAction {
	private static final long serialVersionUID = 1L;

	public CaricoMagazzinoAction() {
        super();
    }
	
	public Forward doBlanckSearchFindUnitaOperativaOrd(ActionContext context, CaricoMagazzinoBulk caricoMagazzino) {
		caricoMagazzino.setMagazzinoAbilitato(null);
		return context.findDefaultForward();
	}

	public Forward doBringBackSearchFindBeneServizio(ActionContext context, CaricoMagazzinoRigaBulk caricoRiga, Bene_servizioBulk beneServizio) {
		try {
			fillModel(context);
			if (beneServizio!=null) {
				CaricoManualeMagazzinoBP bp = (CaricoManualeMagazzinoBP)getBusinessProcess(context);
				bp.inizializeBeneServizio(context, caricoRiga, beneServizio);
				bp.setDirty(true);
			}
			return context.findDefaultForward();
		}catch (Exception ex) {
			return handleException(context, ex);
		}
	}
	
	public Forward doBlankSearchFindBeneServizio(ActionContext context, CaricoMagazzinoRigaBulk riga) throws java.rmi.RemoteException {

	    try {
	        //imposta i valori di default per il tariffario
	        riga.setBeneServizio(new Bene_servizioBulk());
	        riga.setUnitaMisura(null);
	        riga.setCoefConv(null);
	        riga.setVoceIva(null);
	        return context.findDefaultForward();

	    } catch (Exception e) {
	        return handleException(context, e);
	    }
	}
	public Forward doBringBackSearchFindUnitaMisura(ActionContext context, CaricoMagazzinoRigaBulk caricoRiga, UnitaMisuraBulk unitaMisura) {
		try {
			fillModel(context);
			if (unitaMisura!=null) {
				CaricoManualeMagazzinoBP bp = (CaricoManualeMagazzinoBP)getBusinessProcess(context);
				bp.inizializeUnitaMisura(context, caricoRiga, unitaMisura);
				bp.setDirty(true);
			}
			return context.findDefaultForward();
		}catch (Exception ex) {
			return handleException(context, ex);
		}
	}
	
	public Forward doBlanckSearchFindUnitaMisura(ActionContext context, CaricoMagazzinoRigaBulk caricoRiga) {
		caricoRiga.setCoefConv(null);
		return context.findDefaultForward();
	}

	public Forward doCaricaMagazzino(ActionContext context) {
		try {
			fillModel(context);
			CaricoManualeMagazzinoBP bp = (CaricoManualeMagazzinoBP)getBusinessProcess(context);
			bp.completeSearchTools(context, bp);
			bp.getBeniServiziColl().validate(context);
			bp.completeSearchTools(context, bp.getBeniServiziColl());
			return openConfirm(context, "Attenzione! Confermi il carico dei beni selezionati?", OptionBP.CONFIRM_YES_NO, "doConfirmCaricaMagazzino");
		} catch(Exception e) {
			return handleException(context,e);
		}
	}

	public Forward doConfirmCaricaMagazzino(ActionContext context,int option) {
		try {
			if ( option == OptionBP.YES_BUTTON) 
			{
				fillModel(context);
				CaricoManualeMagazzinoBP bp = (CaricoManualeMagazzinoBP)getBusinessProcess(context);
				bp.caricaMagazzino(context);

				bp.setMessage("Operazione effettuata.");
			}
			return context.findDefaultForward();
		} catch(Exception e) {
			return handleException(context,e);
		}
	}

	public Forward doOnDtCompetenzaChange(ActionContext context) {
		CaricoManualeMagazzinoBP bp = (CaricoManualeMagazzinoBP)getBusinessProcess(context);
		CaricoMagazzinoBulk carico = (CaricoMagazzinoBulk)bp.getModel();
	
		java.sql.Timestamp oldDate=null;
		if (carico.getDataCompetenza()!=null)
			oldDate = (java.sql.Timestamp)carico.getDataCompetenza().clone();
	
		try {
			fillModel(context);
			carico.validaDate();
			return context.findDefaultForward();
		}
		catch (Exception ex) {
			// In caso di errore ripropongo la data precedente
			carico.setDataCompetenza(oldDate);
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
