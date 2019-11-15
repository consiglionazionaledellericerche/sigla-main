package it.cnr.contab.ordmag.magazzino.comp;

import java.io.Serializable;

import it.cnr.contab.ordmag.anag00.TipoMovimentoMagAzBulk;
import it.cnr.contab.ordmag.anag00.TipoMovimentoMagBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.CRUDComponent;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;
import it.cnr.jada.persistency.PersistencyException;

public class TipoMovimentoMagComponent extends CRUDComponent implements ICRUDMgr, Cloneable, Serializable {

	public TipoMovimentoMagComponent() {

		/* Default constructor */

	}

	@Override
	public OggettoBulk inizializzaBulkPerModifica(UserContext usercontext, OggettoBulk oggettobulk)
			throws ComponentException {
		TipoMovimentoMagBulk tipoMovimento = (TipoMovimentoMagBulk) super.inizializzaBulkPerModifica(usercontext, oggettobulk);
		it.cnr.jada.bulk.BulkHome homeTpMovAzioni = getHome(usercontext, TipoMovimentoMagAzBulk.class);
		it.cnr.jada.persistency.sql.SQLBuilder sql = homeTpMovAzioni.createSQLBuilder();
		sql.addClause("AND", "cdCds", sql.EQUALS, tipoMovimento.getCdCds());		
		sql.addClause("AND", "cdTipoMovimento", sql.EQUALS, tipoMovimento.getCdTipoMovimento());
		try {
			tipoMovimento.setRigheTipoMovimentoAzioni(new it.cnr.jada.bulk.BulkList(homeTpMovAzioni.fetchAll(sql)));			
		} catch (PersistencyException e) {
			throw handleException(e);
		}
		return tipoMovimento;
	}
}
