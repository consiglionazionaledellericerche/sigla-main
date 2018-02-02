package it.cnr.contab.bollo00.comp;

import it.cnr.contab.bollo00.bulk.Atto_bolloBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.CRUDComponent;
import it.cnr.jada.comp.ComponentException;

public class AttoBolloComponent extends CRUDComponent {
	private static final long serialVersionUID = 1L;

	@Override
	public OggettoBulk inizializzaBulkPerInserimento(UserContext userContext, OggettoBulk oggettoBulk) throws ComponentException {
		try {
			if (oggettoBulk instanceof Atto_bolloBulk) {
				((Atto_bolloBulk)oggettoBulk).setEsercizio(CNRUserContext.getEsercizio(userContext));
				((Atto_bolloBulk)oggettoBulk).setUnitaOrganizzativa((Unita_organizzativaBulk)getHome(userContext, Unita_organizzativaBulk.class).
						findByPrimaryKey(new Unita_organizzativaBulk(CNRUserContext.getCd_unita_organizzativa(userContext))));
			}
			return super.inizializzaBulkPerInserimento(userContext, oggettoBulk);
		}
		catch( Exception e )
		{
			throw handleException( e );
		}		
	}
}
