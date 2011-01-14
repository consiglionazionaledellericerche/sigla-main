package it.cnr.contab.inventario00.bp;
/**
  *  Questa classe gestisce le operazioni di business relative all'associazione di 
  *	una Fattura Passiva a dei beni esistenti nel DB.
**/

import it.cnr.contab.config00.sto.bulk.EnteBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.inventario00.ejb.ConsRegistroInventarioComponentSession;
import it.cnr.contab.inventario00.tabrif.bulk.Id_inventarioBulk;
import it.cnr.contab.inventario01.ejb.BuonoCaricoScaricoComponentSession;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.Config;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.ConsultazioniBP;
import it.cnr.jada.util.ejb.EJBCommonServices;
import it.cnr.jada.util.jsp.Button;
 
public class ConsImpegniAssociazioniBP extends ConsultazioniBP{	


	
	protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		try {

			CompoundFindClause clauses = new CompoundFindClause();		
			clauses.addClause("AND", "esercizio", SQLBuilder.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()));
			clauses.addClause("or", "esercizio_originale", SQLBuilder.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()));
			setBaseclause(clauses);
			CompoundFindClause clauses_uo = new CompoundFindClause();
			if(!isUoEnte(context)){
				 clauses_uo.addClause("AND", "cd_cds", SQLBuilder.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(context.getUserContext()));
				 clauses_uo.addClause("AND", "cd_unita_organizzativa", SQLBuilder.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getCd_unita_organizzativa(context.getUserContext()));
			}
			addToBaseclause(clauses_uo);
			super.init(config,context);
		   		
		}catch(Throwable e) {
			throw new BusinessProcessException(e);
		}
	}
	public java.util.Vector addButtonsToToolbar(java.util.Vector listButton){
		Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.doc");
		button.setSeparator(true);
		listButton.addElement(button);
		Button button1 = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.ass");
		button1.setSeparator(true);
		listButton.addElement(button1);
		return listButton;
	}
	public boolean isUoEnte(ActionContext context){	
		Unita_organizzativaBulk uo = it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context);
		if (uo.getCd_tipo_unita().equals(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE))
			return true;	
		return false; 
	}	
}
