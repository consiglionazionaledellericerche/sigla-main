package it.cnr.contab.logs.action;
import it.cnr.contab.logs.bp.ConsLogsApplicativiBP;
import it.cnr.contab.logs.bulk.Batch_log_rigaBulk;
import it.cnr.contab.logs.bulk.Batch_log_tstaBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.action.ConsultazioniAction;
import it.cnr.jada.util.action.ConsultazioniBP;

public class ConsLogsApplicativiAction extends ConsultazioniAction{

	public Forward doConsultaDettagli(ActionContext context) {
		
		try {	
			ConsLogsApplicativiBP bp = (ConsLogsApplicativiBP)context.getBusinessProcess();
			bp.setSelection(context);
			long selectElements = bp.getSelection().size();

			if (selectElements == 0)
				selectElements = Integer.valueOf(bp.getSelection().getFocus()).compareTo(-1);
			
			if (selectElements == 0) {
				bp.setMessage("Non è stata selezionata nessuna riga.");
				return context.findDefaultForward();
			}
			
			ConsultazioniBP ricercaDettagliBP = null;
			
			bp.getName().equals("ConsLogsApplicativiBP");
			ricercaDettagliBP = (ConsultazioniBP)context.createBusinessProcess("ConsLogsApplicativiRigaBP");
			
			Batch_log_tstaBulk tsta = (Batch_log_tstaBulk)bp.getModel();
			CompoundFindClause clause = new CompoundFindClause();
			clause.addClause("AND","pg_esecuzione",SQLBuilder.EQUALS,tsta.getPg_esecuzione());
			
			ricercaDettagliBP.addToBaseclause(clause);
			ricercaDettagliBP.openIterator(context);
			
			context.addHookForward("close",this,"doDefault");
			return context.addBusinessProcess(ricercaDettagliBP);

			}catch(Throwable ex){
				return handleException(context, ex);
			}
			
		}
}