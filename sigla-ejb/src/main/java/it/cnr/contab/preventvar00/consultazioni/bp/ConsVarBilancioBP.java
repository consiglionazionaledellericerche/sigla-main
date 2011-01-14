package it.cnr.contab.preventvar00.consultazioni.bp;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.util.action.ConsultazioniBP;

public class ConsVarBilancioBP extends ConsultazioniBP {

	protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		try {

			if(!isUoEnte(context))	 {					
        		throw new ApplicationException("Funzione abilitata unicamente per il CDS Ente");
        	}	

			super.init(config,context);
					
		} catch(Throwable e) {
			throw new BusinessProcessException(e);
		}
	}		
	public boolean isUoEnte(ActionContext context){	
		Unita_organizzativaBulk uo = it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context);
		if (uo.getCd_tipo_unita().equals(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE))
			return true;	
		return false; 
	}	
}
