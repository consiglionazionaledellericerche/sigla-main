/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 19/08/2021
 */
package it.cnr.contab.config00.contratto.bulk;

import it.cnr.contab.config00.consultazioni.bulk.VContrattiTotaliDetBulk;
import it.cnr.contab.docamm00.fatturapa.bulk.DocumentoEleTestataBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioHome;
import it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventHome;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Dettaglio_contrattoHome extends BulkHome {
	public Dettaglio_contrattoHome(Connection conn) {
		super(Dettaglio_contrattoBulk.class, conn);
	}
	public Dettaglio_contrattoHome(Connection conn, PersistentCache persistentCache) {
		super(Dettaglio_contrattoBulk.class, conn, persistentCache);
	}
	Long recuperoProgressivoDettaglio(UserContext userContext) throws PersistencyException,it.cnr.jada.comp.ComponentException {
		return new Long(this.fetchNextSequenceValue(userContext,"CNRSEQ00_DETTAGLIO_CONTRATTO").longValue());
	}
	public void initializePrimaryKeyForInsert(UserContext userContext, OggettoBulk bulk) throws PersistencyException,it.cnr.jada.comp.ComponentException {
		Dettaglio_contrattoBulk dettaglio = (Dettaglio_contrattoBulk)bulk;
		if (dettaglio.getId() == null)
			dettaglio.setId(recuperoProgressivoDettaglio(userContext));
	}


	@Override
	public void delete(Persistent persistent, UserContext userContext) throws PersistencyException {
		Dettaglio_contrattoBulk dettaglio=(Dettaglio_contrattoBulk)persistent;
		if ( ContrattoBulk.STATO_PROVVISORIO.equals(dettaglio.getContratto().getStato()))
			super.delete(persistent,userContext);
		else{
			dettaglio.setStato(Dettaglio_contrattoBulk.STATO_ANNULLATO);
			super.update(persistent, userContext);
		}
	}
}