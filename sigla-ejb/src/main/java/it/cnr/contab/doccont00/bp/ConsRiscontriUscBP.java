package it.cnr.contab.doccont00.bp;

import java.util.Iterator;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.doccont00.core.bulk.SospesoBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcess;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.Config;
import it.cnr.jada.util.action.ConsultazioniBP;
import it.cnr.jada.util.jsp.Button;

/**
 * @author fgiardina
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

public class ConsRiscontriUscBP extends ConsultazioniBP {
	public java.util.Vector addButtonsToToolbar(java.util.Vector listButton){
		Button button_usc = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.dettagli_usc");
		button_usc.setSeparator(true);
		listButton.addElement(button_usc);
		return listButton;
	}
	
	protected void init(it.cnr.jada.action.Config config,ActionContext context) throws BusinessProcessException {
			   Integer esercizio = CNRUserContext.getEsercizio(context.getUserContext());
			   String cds = CNRUserContext.getCd_cds(context.getUserContext());
			   String ris = SospesoBulk.TI_RISCONTRO;
			   String spe = SospesoBulk.TIPO_SPESA;
			   CompoundFindClause clauses = new CompoundFindClause();
			   
			  if(!isUoEnte(context))	 {					
					
					clauses.addClause("AND", "esercizio", SQLBuilder.EQUALS, esercizio);
					clauses.addClause("AND", "cd_cds",SQLBuilder.EQUALS, cds);
					clauses.addClause("AND", "ti_sospeso_riscontro",SQLBuilder.EQUALS,ris);
					clauses.addClause("AND", "ti_entrata_spesa",SQLBuilder.EQUALS,spe);
			  }
			else 
			clauses.addClause("AND", "esercizio", SQLBuilder.EQUALS, esercizio);
			clauses.addClause("AND", "ti_sospeso_riscontro",SQLBuilder.EQUALS,ris);
			clauses.addClause("AND", "ti_entrata_spesa",SQLBuilder.EQUALS,spe);
			setBaseclause(clauses);	
			super.init(config, context);	
			 }	 	

	public boolean isUoEnte(ActionContext context){	
		Unita_organizzativaBulk uo = it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context);
		if (uo.getCd_tipo_unita().equals(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE))
		  return true;	
		return false; 
	}	
	
	public java.lang.String getSearchResultColumnSet() 
	{
		return "SospesiPadre";
	}
}