package it.cnr.contab.gestiva00.bp;
import it.cnr.contab.gestiva00.ejb.*;
import it.cnr.contab.gestiva00.core.bulk.*;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.BulkBP;
import it.cnr.jada.util.ejb.EJBCommonServices;


public class StampaProvvisoriaRegistriIvaBP extends StampaRegistriIvaBP {

	private int status = INSERT;

public StampaProvvisoriaRegistriIvaBP() {
	super();
}

public StampaProvvisoriaRegistriIvaBP(String function) {
	super(function+"Tr");
}

/**
 * Invocato per creare un modello vuoto da usare su una nuova richiesta di ricerca.
 */
public Stampa_provvisoria_registri_ivaVBulk createEmptyModelForSearch(ActionContext context) throws BusinessProcessException {

	try {
		return createNewBulk(context);
	} catch(Exception e) {
		throw handleException(e);
	}
}

/**
 * Crea un OggettoBulk vuoto della classe compatibile con la CRUDComponentSession del ricevente
 */
public Stampa_provvisoria_registri_ivaVBulk createNewBulk(ActionContext context) throws BusinessProcessException {
	try {
		Stampa_provvisoria_registri_ivaVBulk bulk = new Stampa_provvisoria_registri_ivaVBulk();
		bulk.setUser(context.getUserInfo().getUserid());
		bulk= (Stampa_provvisoria_registri_ivaVBulk)bulk.initializeForSearch(this,context);
		
		bulk.setTipi_sezionali(
				createComponentSession().selectTipi_sezionaliByClause(
													context.getUserContext(),
													bulk,
													new it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk(),
													null));
		//bulk.setSezionali(createComponentSession().selectSezionaliByClause(context.getUserContext(),bulk,new it.cnr.contab.docamm00.tabrif.bulk.SezionaleBulk(),null));

		return bulk;
	} catch(Exception e) {
		throw handleException(e);
	}
}

protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {

	super.init(config,context);
	setStatus(SEARCH);
	resetForSearch(context);
}

/**
 * Inzializza il ricevente nello stato di SEARCH.
 */
public void resetForSearch(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
	try {
		setModel(context,createEmptyModelForSearch(context));
		setStatus(SEARCH);
		setDirty(false);
		resetChildren(context);
	} catch(Throwable e) {
		throw new it.cnr.jada.action.BusinessProcessException(e);
	}
}
}