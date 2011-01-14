package it.cnr.contab.logs.comp;

import it.cnr.contab.logs.bulk.Batch_log_tstaBulk;
import it.cnr.contab.logs.bulk.Batch_log_tstaKey;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.*;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.*;

import java.io.Serializable;

// Referenced classes of package it.cnr.contab.logs.comp:
//            ITestataLogMgr

public class TestataLogComponent extends CRUDDetailComponent implements
		ITestataLogMgr, Cloneable, Serializable {

	public TestataLogComponent() {
	}

	public OggettoBulk creaConBulk(UserContext usercontext,
			OggettoBulk oggettobulk) throws ComponentException {
		try {
			throw new ApplicationException("Non \350 possibile creare logs");
		} catch (Throwable throwable) {
			throw handleException(oggettobulk, throwable);
		}
	}

	protected Query select(UserContext usercontext,
			CompoundFindClause compoundfindclause, OggettoBulk oggettobulk)
			throws ComponentException, PersistencyException {
		SQLBuilder sqlbuilder = (SQLBuilder) super.select(usercontext,
				compoundfindclause, oggettobulk);
		sqlbuilder.addSQLClause("AND", "UTCR", SQLBuilder.EQUALS, usercontext
				.getUser());
		return sqlbuilder;
	}

	public SQLBuilder selectDettagliByClause(UserContext usercontext,
			Batch_log_tstaBulk batch_log_tstabulk, Class class1,
			CompoundFindClause compoundfindclause) throws ComponentException {
		if (batch_log_tstabulk.getPg_esecuzione() == null) {
			return null;
		} else {
			SQLBuilder sqlbuilder = getHome(usercontext,
					it.cnr.contab.logs.bulk.Batch_log_rigaBulk.class)
					.createSQLBuilder();
			sqlbuilder.addSQLClause("AND", "PG_ESECUZIONE", SQLBuilder.EQUALS,
					batch_log_tstabulk.getPg_esecuzione());
			return sqlbuilder;
		}
	}
}