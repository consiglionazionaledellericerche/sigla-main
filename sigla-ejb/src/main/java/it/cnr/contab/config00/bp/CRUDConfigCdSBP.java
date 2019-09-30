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

import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.contab.config00.util.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.util.action.*;
/**
 * Business Process che gestisce le attività di CRUD per l'entita' CDS
 */


public class CRUDConfigCdSBP extends it.cnr.jada.util.action.SimpleCRUDBP {
	private final SimpleDetailCRUDController percentuali = new SimpleDetailCRUDController("percentuali",it.cnr.contab.config00.sto.bulk.Prc_copertura_obbligBulk.class,"percentuali",this);	
public CRUDConfigCdSBP()
{
	super();
	setTab("tab","tabCds");
}
public CRUDConfigCdSBP(String function ) 
{
	super( function );
	setTab("tab","tabCds");
}
/**
 * Insert the method's description here.
 * Creation date: (13/11/2001 11.28.59)
 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
 */
public final it.cnr.jada.util.action.SimpleDetailCRUDController getPercentuali() {
	return percentuali;
}
/**
 * Restituisce il valore della proprietà 'addPercentualeButtonEnaled'
 *
 * @return Il valore della proprietà 'addPercentualeButtonEnaled'
 */
public boolean isAddPercentualeButtonEnaled()
{
	//sono in visualizzazione
	if ( !isEditable() )
		return false;
	//sono in ricerca
	if (!isInserting() && !isEditing() )
		return false;

	CdsBulk cds = (CdsBulk) getModel();
	Prc_copertura_obbligBulk perc;
	for ( java.util.Iterator i = cds.getPercentuali().iterator(); i.hasNext(); )
	{
		perc = (Prc_copertura_obbligBulk) i.next();
		if ( perc.getEsercizio().compareTo( cds.getEsercizioDiScrivania()) == 0 )
			return false;
	}		
	return true;
}
/**
 * Restituisce il valore della proprietà 'percentualeFieldEnaled'
 *
 * @return Il valore della proprietà 'percentualeFieldEnaled'
 */
public boolean isPercentualeFieldEnaled()
{
	//sono in visualizzazione
	if ( !isEditable() )
		return false;
	//sono in ricerca
	if (!isInserting() && !isEditing() )
		return false;

	CdsBulk cds = (CdsBulk) getModel();
	Prc_copertura_obbligBulk perc = (Prc_copertura_obbligBulk) getPercentuali().getModel();
	if ( perc != null && perc.getEsercizio().compareTo( cds.getEsercizioDiScrivania()) == 0 )
			return true;
	return false;
}
/**
 * Inizializza alcuni attributi del modello CdsBulk del business process
 * @param context contesto dell'Action che e' stata richiesta
 * 
 */
public void reset(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {

	try
	{
		super.reset( context );
		CdsBulk cds = (CdsBulk)getModel();
		cds.setLivello( Constants.LIVELLO_CDS );
		cds.setFl_cds( new Boolean( true ) );
		cds.setFl_uo_cds( new Boolean( false) );
	
	//	setModel( cds );
		
	} catch(Throwable e) 
	{
		throw new it.cnr.jada.action.BusinessProcessException(e);
	}

	
}
}
