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

package it.cnr.contab.doccont00.bp;

import it.cnr.contab.docamm00.docs.bulk.Lettera_pagam_esteroBulk;
import it.cnr.contab.doccont00.intcass.bulk.*;
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.jada.util.action.*;
/**
 * Business Process che gestisce le attività di CRUD per le entita' Sospeso o Riscontro
 */
public class CRUDSospesoBP extends it.cnr.jada.util.action.SimpleCRUDBP 
{
	private final SimpleDetailCRUDController reversaliAccertamenti = new SimpleDetailCRUDController("ReversaliAccertamenti",it.cnr.contab.doccont00.intcass.bulk.V_sospeso_rev_accertBulk.class,"reversaliAccertamentiColl",this);	
	
	private final SimpleDetailCRUDController mandatiImpegni = new SimpleDetailCRUDController("MandatiImpegni",it.cnr.contab.doccont00.intcass.bulk.V_sospeso_man_impBulk.class,"mandatiImpegniColl",this);
	private final SimpleDetailCRUDController lettere = new SimpleDetailCRUDController("Lettere",Lettera_pagam_esteroBulk.class,"lettereColl",this);
public SimpleDetailCRUDController getLettere() {
		return lettere;
	}
public SimpleDetailCRUDController getMandatiImpegni() {
		return mandatiImpegni;
	}
public CRUDSospesoBP() {
	super();
}
public CRUDSospesoBP(String function) {
	super(function);
}
/**
 * Metodo utilizzato per gestire il cambiamento dello stato del sospeso o del riscontro.
  	 * @param context <code>ActionContext</code> in uso.
	 *
	 * @return <code>Forward</code>
	 *
	 * @exception <code>BusinessProcessException</code>
 */

public void cambiaStato(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException 
{
	try 
	{
		if ( isInserting() || isEditing() )
		{
			SospesoBulk sospeso = (SospesoBulk) getModel();
			if( sospeso.getTi_sospeso_riscontro() != null && ( sospeso.getTi_sospeso_riscontro().equals(sospeso.TI_SOSPESO) ) )
				sospeso.setStato_sospeso( sospeso.STATO_SOSP_INIZIALE );
			else if ( sospeso.getTi_sospeso_riscontro() != null && ( sospeso.getTi_sospeso_riscontro().equals(sospeso.TI_RISCONTRO) ) )
				sospeso.setStato_sospeso( sospeso.STATO_SOSP_NON_APPLICATA );
			else
				sospeso.setStato_sospeso( null );
			setModel( context, sospeso );
		}	

	} catch(Exception e) {
		throw handleException(e);
	}
}
/**
 * Metodo utilizzato per gestire il cambiamento del tipo entrata/spesa del riscontro.
  	 * @param context <code>ActionContext</code> in uso.
	 *
	 * @return <code>Forward</code>
	 *
	 * @exception <code>BusinessProcessException</code>
 */

public void cambiaTipoEntrataSpesa(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException 
{
	try 
	{
		SospesoBulk sospeso = (SospesoBulk) getModel();
		if( sospeso.getV_man_rev().getPg_documento_cont() != null && ( Numerazione_doc_contBulk.TIPO_MAN.equals( sospeso.getV_man_rev().getCd_tipo_documento_cont() )) && sospeso.getTi_entrata_spesa() != sospeso.TIPO_SPESA )
			sospeso.setV_man_rev( new V_mandato_reversaleBulk() );
		else if ( sospeso.getV_man_rev().getPg_documento_cont() != null && ( Numerazione_doc_contBulk.TIPO_REV.equals( sospeso.getV_man_rev().getCd_tipo_documento_cont() )) && sospeso.getTi_entrata_spesa() != sospeso.TIPO_ENTRATA )
			sospeso.setV_man_rev( new V_mandato_reversaleBulk() );

		setModel( context, sospeso );

	} catch(Exception e) {
		throw handleException(e);
	}
}
/**
 * restituisce il Controller che gestisce la lista delle reversali e degli
 * accertamenti associati al sospeso
 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
 */
public final it.cnr.jada.util.action.SimpleDetailCRUDController getReversaliAccertamenti() {
	return reversaliAccertamenti;
}
public java.lang.String getSearchResultColumnSet() 
{
	return "SospesiPadre";
}
}
