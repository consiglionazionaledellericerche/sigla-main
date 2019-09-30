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

import it.cnr.contab.doccont00.core.bulk.ProspettoSpeseCdrBulk;
import it.cnr.contab.doccont00.core.bulk.V_obblig_pdg_saldo_laBulk;
import it.cnr.contab.doccont00.ejb.ObbligazioneComponentSession;
import it.cnr.jada.util.action.SimpleDetailCRUDController;

import java.util.LinkedList;
import java.util.List;
/**
 * Business process che gestisce attività relative al prospetto delle spese del Cdr.
 */
public class ProspettoSpeseCdrBP extends it.cnr.jada.util.action.SimpleCRUDBP {
	private final SimpleDetailCRUDController speseCdr = new SimpleDetailCRUDController("speseCdr", V_obblig_pdg_saldo_laBulk.class,"speseCdrColl",this);
	private String codiceCdr;
	private String descrizioneCdr;
	public ProspettoSpeseCdrBP() {
		super();
	}
	public ProspettoSpeseCdrBP(String function) {
		super(function);
	}
	/**
	 * Metodo utilizzato per creare una toolbar applicativa personalizzata.
	 * @return null In questo caso la toolbar è vuota
	 */
	protected it.cnr.jada.util.jsp.Button[] createToolbar() {
		return super.createToolbar();
	}
	@Override
	public boolean isSaveButtonHidden() {
		return true;
	}
	@Override
	public boolean isSearchButtonHidden() {
		return true;
	}
	@Override
	public boolean isFreeSearchButtonHidden() {
		return true;
	}
	@Override
	public boolean isNewButtonHidden() {
		return true;
	}
	@Override
	public boolean isDeleteButtonHidden() {
		return true;
	}
	@Override
	public boolean isStartSearchButtonHidden() {
		return true;
	}
	/**
	 * <!-- @TODO: da completare -->
	 * Restituisce il valore della proprietà 'codiceCdr'
	 *
	 * @return Il valore della proprietà 'codiceCdr'
	 */
	public java.lang.String getCodiceCdr() {
		return codiceCdr;
	}
	/**
	 * <!-- @TODO: da completare -->
	 * Restituisce il valore della proprietà 'descrizioneCdr'
	 *
	 * @return Il valore della proprietà 'descrizioneCdr'
	 */
	public java.lang.String getDescrizioneCdr() {
		return descrizioneCdr;
	}
	/**
	 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
	 */
	public final it.cnr.jada.util.action.SimpleDetailCRUDController getSpeseCdr() {
		return speseCdr;
	}
	/**
	 * Serve per aggiornare le spese del Cdr
	 * @param context Il contesto dell'azione
	 */
	public void  refreshSpeseCdr( it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException
	{
		try
		{
			ProspettoSpeseCdrBulk prospetto = (ProspettoSpeseCdrBulk)getModel();
			List input = new LinkedList();
			input.add( prospetto.getCdr() );
			List result = ((ObbligazioneComponentSession)createComponentSession()).generaProspettoSpeseObbligazione(context.getUserContext(), input );
			prospetto.setSpeseCdrColl( result );
			prospetto.refreshTotali();
		}
		catch (Exception e )
		{
			throw handleException(e)	;
		}	

	}
	/**
	 * <!-- @TODO: da completare -->
	 * Imposta il valore della proprietà 'codiceCdr'
	 *
	 * @param newCodiceCdr	Il valore da assegnare a 'codiceCdr'
	 */
	public void setCodiceCdr(java.lang.String newCodiceCdr) {
		codiceCdr = newCodiceCdr;
	}
	/**
	 * <!-- @TODO: da completare -->
	 * Imposta il valore della proprietà 'descrizioneCdr'
	 *
	 * @param newDescrizioneCdr	Il valore da assegnare a 'descrizioneCdr'
	 */
	public void setDescrizioneCdr(java.lang.String newDescrizioneCdr) {
		descrizioneCdr = newDescrizioneCdr;
	}
}
