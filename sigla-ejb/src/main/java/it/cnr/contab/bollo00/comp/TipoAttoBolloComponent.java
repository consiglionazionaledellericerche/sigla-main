package it.cnr.contab.bollo00.comp;

import java.util.Optional;

import it.cnr.contab.bollo00.tabrif.bulk.Tipo_atto_bolloBulk;
import it.cnr.contab.bollo00.tabrif.bulk.Tipo_atto_bolloHome;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.CRUDComponent;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class TipoAttoBolloComponent extends CRUDComponent {
	private static final long serialVersionUID = 1L;

	@Override
	protected void validaCreaModificaConBulk(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
		super.validaCreaModificaConBulk(usercontext, oggettobulk);
		if (oggettobulk instanceof Tipo_atto_bolloBulk) {
			try {
				Tipo_atto_bolloBulk bulk = (Tipo_atto_bolloBulk)oggettobulk;
				//verifico che per per le date di validità del bollo da salvare non esistano altri bolli validi 
				Tipo_atto_bolloHome home = (Tipo_atto_bolloHome)getHome(usercontext, Tipo_atto_bolloBulk.class);
				SQLBuilder sql = home.createSQLBuilder();
				sql.addClause(FindClause.AND,"codice",SQLBuilder.EQUALS, bulk.getCodice());
				Optional.ofNullable(bulk.getId()).ifPresent(el->sql.addClause(FindClause.AND,"id",SQLBuilder.NOT_EQUALS, bulk.getId()));
				sql.openParenthesis(FindClause.AND);
				sql.addClause(FindClause.OR,"dtFinValidita",SQLBuilder.ISNULL, null);
				sql.addClause(FindClause.OR,"dtFinValidita",SQLBuilder.GREATER_EQUALS, bulk.getDtIniValidita());
				sql.closeParenthesis();
	
				Optional.ofNullable(bulk.getDtFinValidita()).ifPresent(dtFine->{
					sql.addClause(FindClause.AND,"dtIniValidita",SQLBuilder.LESS_EQUALS, dtFine );
				});
				
				if (sql.executeExistsQuery(getConnection(usercontext)))
					throw new ApplicationException("Record non aggiornabile in quanto esiste per lo stesso codice atto un record con date di validità che si sovrappongono.");
			} catch (java.sql.SQLException e) {
				throw handleException(e);
			}
		}
	}
}
