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

import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.doccont00.ordine.bulk.*;
import it.cnr.contab.doccont00.core.bulk.AccertamentoPGiroBulk;
import it.cnr.contab.doccont00.ejb.*;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.util.action.*;

/**
 * <!-- @TODO: da completare -->
 */

public class CRUDOrdineBP extends it.cnr.jada.util.action.SimpleCRUDBP {

	private final SimpleDetailCRUDController dettagliCRUDController = new SimpleDetailCRUDController("dettagliCRUDController",Ordine_dettBulk.class,"dettagli",this) {
		protected void validate(ActionContext context,OggettoBulk bulk) throws ValidationException {
			validaDettaglioOrdine(context, bulk);
		}
	};

	private String clausola;
	
public CRUDOrdineBP() {
	super();
	setTab("tab","tabOrdineTestata");
}
public CRUDOrdineBP(String function) {
	super(function);
	setTab("tab","tabOrdineTestata");
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param context	L'ActionContext della richiesta
 * @param obblig	
 * @return 
 * @throws BusinessProcessException	
 */
private OrdineBulk creaOrdineDa(ActionContext context, it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk obblig) throws BusinessProcessException{

	OrdineBulk ordine = (OrdineBulk)getModel();

	ordine.setObbligazione(obblig);
	ordine.setElementoVoce(obblig.getElemento_voce());
	ordine.setIm_obbligazione(obblig.getIm_obbligazione());

	it.cnr.contab.anagraf00.core.bulk.TerzoBulk terzo = obblig.getCreditore();
	ordine.setTerzo(terzo);
	ordine.setNome(terzo.getAnagrafico().getNome());
	ordine.setCognome(terzo.getAnagrafico().getCognome());
	ordine.setRagione_sociale(terzo.getAnagrafico().getRagione_sociale());
	ordine.setCodice_fiscale(terzo.getAnagrafico().getCodice_fiscale());
	ordine.setPartita_iva(terzo.getAnagrafico().getPartita_iva());
	ordine.setVia_sede(terzo.getVia_sede());
	ordine.setNumero_civico_sede(terzo.getNumero_civico_sede());

	try {

		OrdineComponentSession comp = (OrdineComponentSession)createComponentSession();
		ordine = comp.completaTerzo(context.getUserContext(), ordine);
		setModel(context, ordine);
		
	}catch(Throwable ex){
		throw handleException(ex);
	}

	return ordine;
}
/**
 * Metodo utilizzato per creare una toolbar applicativa personalizzata.
 *	Aggiunge alla normale Toolbar, il pulsante "Stampa".
 *
 * @return newToolbar La nuova toolbar creata
 */

protected it.cnr.jada.util.jsp.Button[] createToolbar() 
{
		
	it.cnr.jada.util.jsp.Button[] toolbar = super.createToolbar();
	it.cnr.jada.util.jsp.Button[] newToolbar = new it.cnr.jada.util.jsp.Button[ toolbar.length + 1 ];
	for ( int i = 0; i< toolbar.length; i++ )
		newToolbar[ i ] = toolbar[ i ];
	newToolbar[ toolbar.length ] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.stampa");

	return newToolbar;
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param context	L'ActionContext della richiesta
 * @param obblig	
 * @return 
 * @throws BusinessProcessException	
 */
public OrdineBulk findOrdineFor(ActionContext context, it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk obblig) throws BusinessProcessException {

	try {
		it.cnr.jada.UserContext userContext = context.getUserContext();
		ObbligazioneComponentSession obbligComp = (ObbligazioneComponentSession)createComponentSession("CNRDOCCONT00_EJB_ObbligazioneComponentSession",ObbligazioneComponentSession.class);

		return obbligComp.findOrdineFor(userContext,obblig);

	}catch (it.cnr.jada.comp.ComponentException ex){
		throw handleException(ex);
	}catch (java.rmi.RemoteException ex){
		throw handleException(ex);
	}
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param context	L'ActionContext della richiesta
 * @param obblig	
 * @return 
 * @throws BusinessProcessException	
 */
public OrdineBulk generaOrdinePer(ActionContext context, it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk obblig) throws BusinessProcessException{

	// Se è già stato generato un ordine per l'obbligazione
	// allora carico l'ordine corrispondente

	OrdineBulk ordine = findOrdineFor(context,obblig);

	if ( (!isEditable()) && (ordine==null))
		return null;
	else if (ordine==null)
		ordine = creaOrdineDa(context, obblig);
	else	
		edit(context,ordine);
		

	return ordine;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'dettagliCRUDController'
 *
 * @return Il valore della proprietà 'dettagliCRUDController'
 */
public final SimpleDetailCRUDController getDettagliCRUDController() {
	return dettagliCRUDController;
}
public boolean isNewButtonHidden() {
	return true;
}
/**
 *	Abilito il bottone di stampa dell'Ordine solo se questo e' in fase di 
 *	modifica/inserimento.
 * 
 */

public boolean isStampaOrdineButtonEnabled() {
	return isEditable() || isInserting();
}
/**
 *	Abilito il bottone di stampa dell'Ordine
 *	se è in stato Editonly
 */

public boolean isStampaOrdineButtonHidden() {
	
	return isEditOnly();
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param context	L'ActionContext della richiesta
 * @param bulk	
 * @throws ValidationException	
 */
public void validaDettaglioOrdine(ActionContext context, OggettoBulk bulk) throws ValidationException{
	
	Ordine_dettBulk ordDett = (Ordine_dettBulk)bulk;

	if (ordDett.getDs_dettaglio()==null){
		throw new ValidationException("Il campo Descrizione è obbligatorio!");
	}
	if (ordDett.getQuantita()==null || ordDett.getQuantita().compareTo(new java.math.BigDecimal(0))<=0){
		throw new ValidationException("Il campo Quantità è obbligatorio!");
	}
	if (ordDett.getIm_unitario()==null){
		throw new ValidationException("Il campo Importo Unitario è obbligatorio!");
	}

}
public OggettoBulk initializeModelForInsert(ActionContext actioncontext, OggettoBulk oggettobulk) throws BusinessProcessException{
	try
	{
		OrdineBulk ordine = (OrdineBulk) super.initializeModelForInsert( actioncontext, oggettobulk );
		Integer esercizio = CNRUserContext.getEsercizio(actioncontext.getUserContext());
		Parametri_cnrBulk par_cnr = (Parametri_cnrBulk) createComponentSession().findByPrimaryKey(actioncontext.getUserContext(), new Parametri_cnrBulk(esercizio));
		ordine.setClausola(par_cnr.getClausolaOrdine());
		return ordine;
	}
	catch(Exception exception)
	{
		throw handleException(exception);
	}
}


}
