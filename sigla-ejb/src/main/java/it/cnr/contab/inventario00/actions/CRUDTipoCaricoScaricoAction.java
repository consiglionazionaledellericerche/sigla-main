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

/**
 * Insert the type's description here.
 * Creation date: (18/12/2001 16.22.10)
 * @author: Roberto Fantino
 */
import it.cnr.contab.inventario00.tabrif.bulk.*;
import it.cnr.contab.inventario00.bp.*;
import it.cnr.jada.action.*;

public class CRUDTipoCaricoScaricoAction extends it.cnr.jada.util.action.CRUDAction {
/**
 * CRUDTipoCaricoScaricoAction constructor comment.
 */
public CRUDTipoCaricoScaricoAction() {
	super();
}
/**
  *  Viene richiamato nel momento in cui si seleziona il Flag AUMENTO DI VALORE nel 
  *	Tipo Carico Scarico.
  *
  * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
  *
  * @return forward <code>Forward</code>
**/ 
public Forward doOnFlAumentoValoreChange(ActionContext context) {	

	/** 21.06.2004: Borriello
	  *	 In seguito all'implementazione del Buono di Carico per aumento di valore, 
	  *	 proveniente da Fattura Passiva, è stato necessario inibire il codice che segue, poichè
	  *	 deve essere possibile creare un Tipo di Movimento (Carico), che sia allo stesso
	  *	 tempo Visibile da Fattura, (FL_FATTURABILE = 'Y'), e per aumento di valore,
	  *	 (FL_AUMENTO_VALORE = 'Y').
	  *
	  *	26.07.2004: Borriello
	  *  Implementata esclusione del Fl_trasferimento.
	 */
	 
	try {
		CRUDTipoCaricoScaricoBP bp = (CRUDTipoCaricoScaricoBP)getBusinessProcess(context);
		Tipo_carico_scaricoBulk movimento = (Tipo_carico_scaricoBulk)bp.getModel();
		Boolean fatturabile = movimento.getFl_fatturabile();
		Boolean aumento_valore = movimento.getFl_aumento_valore();
		Boolean fl_trasferimento = movimento.getFl_buono_per_trasferimento();
		Boolean movimento_coge = movimento.getFl_elabora_buono_coge();
		fillModel( context );		
		try	{
			if (Boolean.TRUE.equals(movimento.getFl_aumento_valore())) {
				movimento.setFl_buono_per_trasferimento(Boolean.FALSE);
			}			
			bp.setModel(context,movimento);
			return context.findDefaultForward();
		} catch(BusinessProcessException e) {
			movimento.setFl_aumento_valore(aumento_valore);
			movimento.setFl_fatturabile(fatturabile);
			movimento.setFl_buono_per_trasferimento(fl_trasferimento);
			movimento.setFl_elabora_buono_coge(movimento_coge);
			bp.setModel(context,movimento);
			throw e;
		}
	} catch (Throwable t) {
		return handleException(context, t);
	}


}
/**
  *  Viene richiamato nel momento in cui si seleziona il Flag CARICO SOGGETTO AD ELABORAZIONE COGE nel 
  *	Tipo Carico Scarico.
  *
  * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
  *
  * @return forward <code>Forward</code>
**/ 
public Forward doOnFlElaboraCOGEChange(ActionContext context) {	

	try {
		CRUDTipoCaricoScaricoBP bp = (CRUDTipoCaricoScaricoBP)getBusinessProcess(context);
		Tipo_carico_scaricoBulk movimento = (Tipo_carico_scaricoBulk)bp.getModel();
		Boolean fatturabile = movimento.getFl_fatturabile();
		Boolean elaborazione_coge = movimento.getFl_elabora_buono_coge();
		Boolean fl_trasferimento = movimento.getFl_buono_per_trasferimento();
		fillModel( context );		
		try	{
			if (Boolean.TRUE.equals(movimento.getFl_elabora_buono_coge())) {
				movimento.setFl_fatturabile(Boolean.FALSE);
				movimento.setFl_buono_per_trasferimento(Boolean.FALSE);
			}			
			bp.setModel(context,movimento);
			return context.findDefaultForward();
		} catch(BusinessProcessException e) {
			movimento.setFl_elabora_buono_coge(elaborazione_coge);
			movimento.setFl_fatturabile(fatturabile);
			movimento.setFl_buono_per_trasferimento(fl_trasferimento);
			bp.setModel(context,movimento);
			throw e;
		}
	} catch (Throwable t) {
		return handleException(context, t);
	}	
}
/**
  *  Viene richiamato nel momento in cui si seleziona il Flag FATTURABILE nel 
  *	Tipo Carico Scarico.
  *
  * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
  *
  * @return forward <code>Forward</code>
**/ 
public Forward doOnFlFatturabileChange(ActionContext context) {
	
	try {
		CRUDTipoCaricoScaricoBP bp = (CRUDTipoCaricoScaricoBP)getBusinessProcess(context);
		Tipo_carico_scaricoBulk movimento = (Tipo_carico_scaricoBulk)bp.getModel();
		Boolean fatturabile = movimento.getFl_fatturabile();
		//Boolean aumento_valore = movimento.getFl_aumento_valore();	// *
		Boolean elaborazione_coge = movimento.getFl_elabora_buono_coge();	
		Boolean fl_trasferimento = movimento.getFl_buono_per_trasferimento();
		fillModel( context );		
		try	{
			if (Boolean.TRUE.equals(movimento.getFl_fatturabile())) {
				//movimento.setFl_aumento_valore(Boolean.FALSE);		// *
				movimento.setFl_elabora_buono_coge(Boolean.FALSE);
				movimento.setFl_buono_per_trasferimento(Boolean.FALSE);
			}			
			bp.setModel(context,movimento);
			return context.findDefaultForward();
		} catch(BusinessProcessException e) {
			//movimento.setFl_aumento_valore(aumento_valore);			// * Vedi <code>doOnFlAumentoValoreChange()</code>
			movimento.setFl_fatturabile(fatturabile);
			movimento.setFl_elabora_buono_coge(elaborazione_coge);
			movimento.setFl_buono_per_trasferimento(fl_trasferimento);
			bp.setModel(context,movimento);
			throw e;
		}
	} catch (Throwable t) {
		return handleException(context, t);
	}
}
/**
  *  Viene richiamato nel momento in cui si seleziona il Flag PER TRAFSERIMENTO nel 
  *	Tipo Carico Scarico.
  *
  * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
  *
  * @return forward <code>Forward</code>
**/ 
public Forward doOnFlTrasferimentoChange(ActionContext context) {
	
	try {
		CRUDTipoCaricoScaricoBP bp = (CRUDTipoCaricoScaricoBP)getBusinessProcess(context);
		Tipo_carico_scaricoBulk movimento = (Tipo_carico_scaricoBulk)bp.getModel();
		Boolean fatturabile = movimento.getFl_fatturabile();
		Boolean aumento_valore = movimento.getFl_aumento_valore();
		Boolean elaborazione_coge = movimento.getFl_elabora_buono_coge();	
		fillModel( context );		
		try	{
			if (Boolean.TRUE.equals(movimento.getFl_buono_per_trasferimento())) {
				movimento.setFl_fatturabile(Boolean.FALSE);
				movimento.setFl_aumento_valore(Boolean.FALSE);
				movimento.setFl_elabora_buono_coge(Boolean.FALSE);
				movimento.setFl_chiude_fondo(Boolean.FALSE);
				movimento.setFl_storno_fondo(Boolean.FALSE);
			}			
			bp.setModel(context,movimento);
			return context.findDefaultForward();
		} catch(BusinessProcessException e) {
			movimento.setFl_aumento_valore(aumento_valore);
			movimento.setFl_fatturabile(fatturabile);
			movimento.setFl_elabora_buono_coge(elaborazione_coge);
			bp.setModel(context,movimento);
			throw e;
		}
	} catch (Throwable t) {
		return handleException(context, t);
	}
}
/**
  *  Viene richiamato nel momento in cui si seleziona il Flag CARICO/SCARICO nel 
  *	Tipo Carico Scarico.
  *
  * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
  *
  * @return forward <code>Forward</code>
**/ 
public Forward doOnFlCaricoScarico(ActionContext context) {	

	try {
		CRUDTipoCaricoScaricoBP bp = (CRUDTipoCaricoScaricoBP)getBusinessProcess(context);
		Tipo_carico_scaricoBulk movimento = (Tipo_carico_scaricoBulk)bp.getModel();
		Boolean fatturabile = movimento.getFl_fatturabile();
		Boolean aumento_valore = movimento.getFl_aumento_valore();
		fillModel( context );
		if (movimento.getTi_documento() == null){
			return context.findDefaultForward();
		}
		try	{
			if (movimento.getTi_documento().equals(movimento.TIPO_SCARICO)) {
				movimento.setFl_aumento_valore(Boolean.FALSE);
			}
			else{
				movimento.setFl_storno_fondo(Boolean.FALSE);
				movimento.setFl_chiude_fondo(Boolean.FALSE);
			}
			bp.setModel(context,movimento);
			return context.findDefaultForward();
		} catch(BusinessProcessException e) {
			movimento.setFl_aumento_valore(aumento_valore);
			movimento.setFl_fatturabile(fatturabile);
			bp.setModel(context,movimento);
			throw e;
		}
	} catch (Throwable t) {
		return handleException(context, t);
	}	
}
}
