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

package it.cnr.contab.config00.bp;

import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.pdcep.bulk.ContoBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;

public class CRUDContoBP extends it.cnr.jada.util.action.SimpleCRUDBP {
	private static final long serialVersionUID = 1L;

	private boolean flNuovoPdg = false;
    
	/**
	 * Primo costruttore della classe <code>CRUDConfigPdcCDSSpeseBP</code>.
	 */
	public CRUDContoBP() {
		super();
	}
	/**
	 * Secondo costruttore della classe <code>CRUDConfigPdcCDSSpeseBP</code>.
	 * @param String function
	 */
	public CRUDContoBP(String function) {
		super(function);
	}
	
	@Override
	public void validate(ActionContext actioncontext) throws ValidationException {
		super.validate(actioncontext);
		ContoBulk bulk = (ContoBulk)getModel();
		if (!isFlNuovoPdg()) {
			if ( bulk==null || bulk.getVoce_ep_padre() == null || OggettoBulk.isNullOrEmpty( bulk.getVoce_ep_padre().getCd_voce_ep()))
				throw new ValidationException( "Il CODICE del CAPOCONTO deve essere inserito." );
			
			bulk.getVoce_ep_padre().setCd_voce_ep( bulk.getVoce_ep_padre().getCd_voce_ep().toUpperCase());
			// controllo su campo NATURA CONTO
			if ( bulk.getNatura_voce() == null || bulk.getNatura_voce().equals("") )
				throw new ValidationException( "Il campo NATURA CONTO deve essere selezionato." );

			// controllo su campo RIEPILOGA A
			if (bulk.getRiepiloga_a() == null || bulk.getRiepiloga_a().equals("") )
				throw new ValidationException( "Il campo RIEPILOGA A deve essere selezionato." );		
					
			// controllo su campo TIPO CONTO SPECIALE
			// Richiesta n.35 - tipo conto speciale facoltativo
			// if ( getConto_speciale() == null || getConto_speciale().equals("") )
			//	throw new ValidationException( "Il campo TIPO CONTO SPECIALE deve essere selezionato." );
			
			// controllo su campo CODICE PROPRIO
			if (!(bulk.getCd_proprio_voce_ep() == null || bulk.getCd_proprio_voce_ep().equals("") ))
			{
				long cdLong;
				try
				{
					cdLong = Long.parseLong( bulk.getCd_proprio_voce_ep() );
				}
				catch (java.lang.NumberFormatException e)
				{
					throw new ValidationException( "Il campo CODICE PROPRIO deve essere numerico. " );			
				}
				if ( cdLong < 0 )
					throw new ValidationException( "Il campo CODICE PROPRIO deve essere maggiore di 0. " );					
			}
		}else
		{
			// 
			if ( bulk.getV_classificazione_voci_ep() == null || bulk.getV_classificazione_voci_ep().getCd_classificazione()==null ||bulk.getV_classificazione_voci_ep().getCd_classificazione().equals("") )
				throw new ValidationException("Il campo classificazione è obbligatorio.");
		}
		// controllo su campo DESCRIZIONE 
			if ( bulk.getDs_voce_ep() == null || bulk.getDs_voce_ep().equals("") )
				throw new ValidationException("Il campo NOME CONTO è obbligatorio.");
			// controllo su campo SEZIONE
			if ( bulk.getTi_sezione() == null || bulk.getTi_sezione().equals("") )
				throw new ValidationException( "Il campo SEZIONE deve essere selezionato." );
	}
	
	public boolean isFlNuovoPdg() {
		return flNuovoPdg;
	}
	
	private void setFlNuovoPdg(boolean flNuovoPdg) {
		this.flNuovoPdg = flNuovoPdg;
	}
	
	protected void initialize(ActionContext actioncontext) throws BusinessProcessException {
		super.initialize(actioncontext);
		try {
			Parametri_cnrBulk parCnr = Utility.createParametriCnrComponentSession().getParametriCnr(actioncontext.getUserContext(), CNRUserContext.getEsercizio(actioncontext.getUserContext())); 
			setFlNuovoPdg(parCnr.getFl_nuovo_pdg().booleanValue());
		}
	    catch(Throwable throwable)
	    {
	        throw new BusinessProcessException(throwable);
	    }
	}
}
