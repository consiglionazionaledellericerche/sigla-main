package it.cnr.contab.ordmag.ordini.action;

import java.rmi.RemoteException;
import java.util.List;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.docamm00.bp.CRUDDocumentoGenericoPassivoBP;
import it.cnr.contab.docamm00.bp.IDocumentoAmministrativoBP;
import it.cnr.contab.docamm00.bp.IGenericSearchDocAmmBP;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk;
import it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk;
import it.cnr.contab.ordmag.magazzino.bulk.BollaScaricoMagBulk;
import it.cnr.contab.ordmag.ordini.bp.CRUDEvasioneOrdineBP;
import it.cnr.contab.ordmag.ordini.bp.CRUDOrdineAcqBP;
import it.cnr.contab.ordmag.ordini.bulk.EvasioneOrdineBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqConsegnaBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqRigaBulk;
import it.cnr.contab.utenze00.bulk.CNRUserInfo;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.bulk.BulkInfo;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.action.SelezionatoreListaBP;

public class CRUDEvasioneOrdineAction extends it.cnr.jada.util.action.CRUDAction {

public CRUDEvasioneOrdineAction() {
        super();
    }
public Forward doCercaConsegneDaEvadere(ActionContext context) 
{
	try 
	{
		CRUDEvasioneOrdineBP bp = (CRUDEvasioneOrdineBP)getBusinessProcess(context);
		fillModel( context );
		EvasioneOrdineBulk bulk = (EvasioneOrdineBulk) bp.getModel();
		bp.cercaConsegne(context);
		return context.findDefaultForward();
	} 
	catch(Throwable e) {return handleException(context,e);}
}

@Override
public Forward doSalva(ActionContext actioncontext) throws RemoteException {
	try
	{
		fillModel(actioncontext);
		List<BollaScaricoMagBulk> listaBolleScarico = gestioneSalvataggio(actioncontext);
		CRUDEvasioneOrdineBP bp = (CRUDEvasioneOrdineBP)actioncontext.getBusinessProcess();
		if (!listaBolleScarico.isEmpty()){
			SelezionatoreListaBP nbp = (SelezionatoreListaBP)actioncontext.createBusinessProcess("BolleScaricoGenerate", new Object[] { "Tn" });
			nbp.setMultiSelection(false);

			BollaScaricoMagBulk bollaScaricoMagBulk = listaBolleScarico.get(0);

			OggettoBulk instance = (OggettoBulk)bollaScaricoMagBulk;
			CompoundFindClause clauses = new CompoundFindClause();
			clauses.addClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, bollaScaricoMagBulk.getEsercizio());
			clauses.addClause("AND", "CD_CDS", SQLBuilder.EQUALS, bollaScaricoMagBulk.getCdCds());
			clauses.addClause("AND", "CD_NUMERATORE_MAG", SQLBuilder.EQUALS, bollaScaricoMagBulk.getCdNumeratoreMag());
			clauses.addClause("AND", "CD_MAGAZZINO", SQLBuilder.EQUALS, bollaScaricoMagBulk.getCdMagazzino());
			boolean primoGiro = true;
			for (BollaScaricoMagBulk bolla : listaBolleScarico){
				clauses.addClause(primoGiro ? "AND" : "OR", "PG_BOLLA_SCA", SQLBuilder.EQUALS, bolla.getPgBollaSca());
				primoGiro = false;
			}

			it.cnr.jada.util.RemoteIterator ri = bp.find(actioncontext, clauses, instance);

			nbp.setIterator(actioncontext,ri);
			BulkInfo bulkInfo = BulkInfo.getBulkInfo(instance.getClass());
			nbp.setBulkInfo(bulkInfo);

			String columnsetName = bp.getColumnSetForBollaScarico();
			if (columnsetName != null)
				nbp.setColumns(bulkInfo.getColumnFieldPropertyDictionary(columnsetName));
			return actioncontext.addBusinessProcess(nbp);
		}
		return actioncontext.findDefaultForward();
	}
	catch(ValidationException validationexception)
	{
		getBusinessProcess(actioncontext).setErrorMessage(validationexception.getMessage());
	}
	catch(Throwable throwable)
	{
		return handleException(actioncontext, throwable);
	}
	return actioncontext.findDefaultForward();
}

private List<BollaScaricoMagBulk> gestioneSalvataggio(ActionContext actioncontext) throws ValidationException, ApplicationException,  BusinessProcessException {
	CRUDEvasioneOrdineBP bp = (CRUDEvasioneOrdineBP)actioncontext.getBusinessProcess();
	it.cnr.jada.util.action.Selection selection = bp.getConsegne().getSelection();
	EvasioneOrdineBulk bulk = (EvasioneOrdineBulk) bp.getModel();
	java.util.List consegne = bp.getConsegne().getDetails();
	bulk.setRigheConsegnaSelezionate(new BulkList<>());
	for (it.cnr.jada.util.action.SelectionIterator i = selection.iterator();i.hasNext();) {
		OrdineAcqConsegnaBulk consegna = (OrdineAcqConsegnaBulk)consegne.get(i.nextIndex());
		bulk.getRigheConsegnaSelezionate().add(consegna);
	}
	if (bulk.getRigheConsegnaSelezionate() == null || bulk.getRigheConsegnaSelezionate().isEmpty()){
		throw new it.cnr.jada.comp.ApplicationException("Selezionare almeno una consegna da evadere!");
	} else {
		List<BollaScaricoMagBulk> listaBolleScarico = bp.evadiConsegne(actioncontext);
		bulk.setRigheConsegnaDaEvadereColl(new BulkList<>());
		return listaBolleScarico;
	}
}

public Forward doOnQuantitaChange(ActionContext context) {

	try{	
		CRUDEvasioneOrdineBP bp = (CRUDEvasioneOrdineBP)getBusinessProcess(context);
		fillModel(context);
		return context.findDefaultForward();

	} catch(Throwable e) {
		return handleException(context, e);
	}
}
public Forward doSelectConsegneDaEvadere(ActionContext context) {

	CRUDEvasioneOrdineBP bp = (CRUDEvasioneOrdineBP)getBusinessProcess(context);
    try {
        bp.getConsegne().setSelection(context);
    } catch (Throwable e) {
		return handleException(context, e);
    }

	OrdineAcqConsegnaBulk consegna = (OrdineAcqConsegnaBulk)bp.getConsegne().getModel();
	consegna.setQuantitaOriginaria(consegna.getQuantita());
	return context.findDefaultForward();
}
public Forward doDeselectConsegneDaEvadere(ActionContext context) {

	CRUDEvasioneOrdineBP bp = (CRUDEvasioneOrdineBP)getBusinessProcess(context);
    try {
        bp.getConsegne().setSelection(context);
    } catch (Throwable e) {
		return handleException(context, e);
    }

	OrdineAcqConsegnaBulk consegna = (OrdineAcqConsegnaBulk)bp.getConsegne().getModel();
	consegna.setQuantita(consegna.getQuantitaOriginaria());
	return context.findDefaultForward();
}
}
