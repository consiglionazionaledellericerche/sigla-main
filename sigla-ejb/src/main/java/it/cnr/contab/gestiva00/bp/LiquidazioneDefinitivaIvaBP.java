package it.cnr.contab.gestiva00.bp;
import it.cnr.contab.gestiva00.ejb.*;
import it.cnr.contab.gestiva00.core.bulk.*;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.*;
import it.cnr.jada.util.ejb.EJBCommonServices;

public class LiquidazioneDefinitivaIvaBP extends LiquidazioneIvaBP {

	private int status = SEARCH;
	private final SimpleDetailCRUDController dettaglio_prospetti = new SimpleDetailCRUDController("prospetti_stampati", Liquidazione_ivaBulk.class,"prospetti_stampati",this);

public LiquidazioneDefinitivaIvaBP() {
	this("");
}

public LiquidazioneDefinitivaIvaBP(String function) {
	super(function+"Tr");
}

/**
 * Crea un OggettoBulk vuoto della classe compatibile con la CRUDComponentSession del ricevente
 */
public Liquidazione_definitiva_ivaVBulk aggiornaProspetti(ActionContext context,Liquidazione_definitiva_ivaVBulk bulk) throws BusinessProcessException {
	try {
		bulk.setProspetti_stampati(createComponentSession().selectProspetti_stampatiByClause(context.getUserContext(),bulk,new Liquidazione_ivaBulk(),null));
		getDettaglio_prospetti().reset(context);
		return bulk;
	} catch(Exception e) {
		throw handleException(e);
	}
}

/**
 * Invocato per creare un modello vuoto da usare su una nuova richiesta di ricerca.
 */
public Liquidazione_definitiva_ivaVBulk createEmptyModelForSearch(ActionContext context) throws BusinessProcessException {

	try {
		return createNewBulk(context);
	} catch(Exception e) {
		throw handleException(e);
	}
}

/**
 * Crea un OggettoBulk vuoto della classe compatibile con la CRUDComponentSession del ricevente
 */
public Liquidazione_definitiva_ivaVBulk createNewBulk(ActionContext context) throws BusinessProcessException {
	try {
		Liquidazione_definitiva_ivaVBulk bulk = new Liquidazione_definitiva_ivaVBulk();
		bulk.setUser(context.getUserInfo().getUserid());
		bulk = (Liquidazione_definitiva_ivaVBulk)bulk.initializeForSearch(this,context);
		setLiquidato(false);
		//bulk.setTipi_sezionali(createComponentSession().selectTipi_sezionaliByClause(context.getUserContext(),bulk,new it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk(),null));
		bulk=aggiornaProspetti(context,bulk);
		
		return bulk;
	} catch(Exception e) {
		throw handleException(e);
	}
}

protected it.cnr.jada.util.jsp.Button[] createToolbar() {

	it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[3];
	int i = 0;

	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.startSearch");
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.ristampa");
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.reset");
	
	return toolbar;
}

/**
 * Restituisce il valore della proprietà 'dettaglio_prospetti'
 *
 * @return Il valore della proprietà 'dettaglio_prospetti'
 */
public final it.cnr.jada.util.action.SimpleDetailCRUDController getDettaglio_prospetti() {
	return dettaglio_prospetti;
}

protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {

	super.init(config,context);
	setStatus(SEARCH);
	resetTabs();
	resetForSearch(context);
}

public boolean isPrintButtonEnabled() {

	return	getDettaglio_prospetti() != null &&
			getDettaglio_prospetti().getDetails() != null &&
			!getDettaglio_prospetti().getDetails().isEmpty();
}

public boolean isPrintButtonHidden() {

	return getPrintbp() == null;
}

/**
 * Inzializza il ricevente nello stato di SEARCH.
 */
public void resetForSearch(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
	try {
		setModel(context,createEmptyModelForSearch(context));
		setStatus(SEARCH);
		resetChildren(context);
	} catch(Throwable e) {
		throw new it.cnr.jada.action.BusinessProcessException(e);
	}
}

/**
* Imposta come attivi i tab di default.
*
*/

public void resetTabs() {
    setTab("tab", "tabEsigDetr");
}
}