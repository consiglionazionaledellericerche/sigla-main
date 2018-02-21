package it.cnr.contab.incarichi00.comp;

import it.cnr.contab.incarichi00.tabrif.bulk.Ass_incarico_attivitaBulk;
import it.cnr.contab.incarichi00.tabrif.bulk.Ass_incarico_attivitaHome;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.CRUDComponent;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.Query;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class AssIncaricoAttivitaComponent extends CRUDComponent {
	protected Query select(UserContext userContext,CompoundFindClause clauses,OggettoBulk bulk) throws ComponentException, it.cnr.jada.persistency.PersistencyException 
	{
		SQLBuilder sql = (SQLBuilder) super.select( userContext, clauses, bulk );
		sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, CNRUserContext.getEsercizio(userContext));
		return sql;
	}

	public void eliminaConBulk(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
		try {
			if (oggettobulk instanceof Ass_incarico_attivitaBulk) {
				Ass_incarico_attivitaBulk incAttBulk = (Ass_incarico_attivitaBulk)oggettobulk;
				Ass_incarico_attivitaHome incAttHome = (Ass_incarico_attivitaHome)getHome( usercontext, Ass_incarico_attivitaBulk.class);

				if (!incAttHome.findProceduraIncarichi(incAttBulk).isEmpty())
					throw new ComponentException("Eliminazione non possibile! Risulta essere già stata effettuata almeno una procedura per il conferimento di incarichi per il tipo incarico/attività che si desidera eliminare.");
			}
			super.eliminaConBulk(usercontext, oggettobulk);
		}
		catch( Exception e )
		{
			throw handleException( e );
		}		
	}
}