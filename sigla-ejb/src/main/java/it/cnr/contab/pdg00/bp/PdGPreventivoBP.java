package it.cnr.contab.pdg00.bp;
/**
 * Business Process per la gestione della testata del PDG
 */

public class PdGPreventivoBP extends it.cnr.jada.util.action.BulkBP  implements it.cnr.jada.util.action.FindBP {
	private boolean roStato;
    private boolean roConsPDGEntrata;
	private boolean roConsPDGSpesa;
public PdGPreventivoBP() {
	super();
}

public PdGPreventivoBP(String function) {
	super(function);

	if("V".equals(function)) roStato = true;
	else roStato = false;
}

public void caricaPdg(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
	try {
		setModel(context,createPdGPreventivoComponentSession().caricaPdg(context.getUserContext(),null));
	} catch(Throwable e) {
		throw handleException(e);
	}
}

public void caricaPdg(it.cnr.jada.action.ActionContext context,it.cnr.contab.config00.sto.bulk.CdrBulk cdr) throws it.cnr.jada.action.BusinessProcessException {
	try {
		setModel(context,createPdGPreventivoComponentSession().caricaPdg(context.getUserContext(),cdr));
		roConsPDGEntrata = !createPdGPreventivoComponentSession().controllaDiscrepanzeAggregatoForCons(context.getUserContext(),getModel(),"ETR");
		roConsPDGSpesa = !createPdGPreventivoComponentSession().controllaDiscrepanzeAggregatoForCons(context.getUserContext(),getModel(),"SPE");
	} catch(Throwable e) {
		throw handleException(e);
	}
}

	/**
	 * Crea la PdGComponentSession da usare per effettuare operazioni
	 */
	public it.cnr.contab.pdg00.ejb.PdGPreventivoComponentSession createPdGPreventivoComponentSession() throws javax.ejb.EJBException,java.rmi.RemoteException {
		return (it.cnr.contab.pdg00.ejb.PdGPreventivoComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRPDG00_EJB_PdGPreventivoComponentSession", it.cnr.contab.pdg00.ejb.PdGPreventivoComponentSession.class);
	}

/*
 * Fake method: la ricerca su piano di gestione non è gestita direttamente
 */

public it.cnr.jada.util.RemoteIterator find(
    it.cnr.jada.action.ActionContext context,
    it.cnr.jada.persistency.sql.CompoundFindClause clause,
    it.cnr.jada.bulk.OggettoBulk model
)  throws it.cnr.jada.action.BusinessProcessException {
	try {
		return null;
	} catch(Exception e) {
		throw new it.cnr.jada.action.BusinessProcessException(e);
	}
}

public it.cnr.jada.util.RemoteIterator find(it.cnr.jada.action.ActionContext actionContext,it.cnr.jada.persistency.sql.CompoundFindClause clauses,it.cnr.jada.bulk.OggettoBulk bulk,it.cnr.jada.bulk.OggettoBulk context,String property) throws it.cnr.jada.action.BusinessProcessException {
	try {
		return it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(actionContext,createPdGPreventivoComponentSession().cerca(actionContext.getUserContext(),clauses,bulk,context,property));
	} catch(Exception e) {
		throw new it.cnr.jada.action.BusinessProcessException(e);
	}
}

	protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		super.init(config,context);
		caricaPdg(context,null);
	}

/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'rOStato'
 *
 * @return Il valore della proprietà 'rOStato'
 */
public boolean isROStato() {
		return roStato;
	}
	/**
	 * Restituisce il true se non ci sono dati da visualizzare
	 * in V_DPDG_AGGREGATO_ETR_DET_D
	 */
	public boolean isROConsPDGEntrata() {
			return roConsPDGEntrata;
		}
	/**
	 * Restituisce il true se non ci sono dati da visualizzare
	 * in V_DPDG_AGGREGATO_SPE_DET_D
	 */
	public boolean isROConsPDGSpesa() {
			return roConsPDGSpesa;
		}	
			
}