package it.cnr.contab.pdg00.bp;

import it.cnr.contab.pdg00.cdip.bulk.*;
import it.cnr.contab.pdg00.ejb.*;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.util.action.*;

public class ContabilizzazioneFlussoStipendialeMensileBP extends it.cnr.jada.util.action.SelezionatoreListaBP {
/**
 * CostiStipendialiMensiliBP constructor comment.
 */
public ContabilizzazioneFlussoStipendialeMensileBP() {
	super();
}
/**
 * CostiStipendialiMensiliBP constructor comment.
 * @param function java.lang.String
 */
public ContabilizzazioneFlussoStipendialeMensileBP(String function) {
	super(function);
	table.setMultiSelection(true);
}
/**
 * Crea il riferimento alla componente CNRPDG00_EJB_CostiDipendenteComponentSession
 *
 * @return Remote interface della componente
 * @throws EJBException	Se si verifica qualche eccezione applicativa per cui non è possibile effettuare l'operazione
 * @throws RemoteException	Se si verifica qualche eccezione di sistema per cui non è possibile effettuare l'operazione
 */
public CostiDipendenteComponentSession createComponentSession() throws javax.ejb.EJBException,java.rmi.RemoteException {
	return (CostiDipendenteComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRPDG00_EJB_CostiDipendenteComponentSession",CostiDipendenteComponentSession.class);
}
public it.cnr.jada.util.jsp.Button[] createToolbar() {
	it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[4];
	int i = 0;
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.contabilizza");
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.apriCompenso");
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.apriDocumentoGenerico");
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.apriMandato");
	return toolbar;
}
public String getFormTitle() {
	return "Contabilizzazione flussi stipendiali mensili";
}
protected void init(Config config,ActionContext context) throws BusinessProcessException {
	super.init(config,context);
	try {
		setBulkInfo(it.cnr.jada.bulk.BulkInfo.getBulkInfo(Stipendi_cofiBulk.class));
		refresh(context);
	} catch(Throwable e) {
		throw handleException(e);
	}
}
public void refresh(ActionContext context) throws BusinessProcessException {
	try {
		setIterator(context,createComponentSession().listaStipendi_cofi(context.getUserContext()));
	} catch(Throwable e) {
		throw handleException(e);
	}
}
}
