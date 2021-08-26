/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 19/08/2021
 */
package it.cnr.contab.config00.contratto.bulk;

import it.cnr.contab.docamm00.fatturapa.bulk.DocumentoEleTestataBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioHome;
import it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventHome;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import org.apache.commons.lang.StringUtils;

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
	public SQLBuilder selectBeneServizioByClause(UserContext userContext, Dettaglio_contrattoBulk dettaglio_contrattoBulk,
												 Bene_servizioHome bene_servizioHome, Bene_servizioBulk bene_servizioBulk,
												 CompoundFindClause compoundfindclause) throws PersistencyException{
		SQLBuilder sql = bene_servizioHome.selectByClause(userContext, compoundfindclause);
		List<String> l=dettaglio_contrattoBulk.getContratto().getDettaglio_contratto()
				.stream()
				.filter(e->(e.getCdBeneServizio()!=null && (!(e.getCdBeneServizio().isEmpty()))))
				.map(Dettaglio_contrattoBulk::getCdBeneServizio)
				.map(e->"'".concat(e).concat("'"))
				.collect(Collectors.toList());
		l.remove(bene_servizioBulk.getCd_bene_servizio());
		String  beneServiziAlreadySelected = null;
		if ( l!=null && ( !l.isEmpty()))
			beneServiziAlreadySelected=l.stream().collect(Collectors.joining(","));

		if(StringUtils.isNotBlank(beneServiziAlreadySelected))
			sql.addSQLClause("AND", "CD_BENE_SERVIZIO NOT IN (" + beneServiziAlreadySelected + ")");
		return sql;
	}
	public SQLBuilder selectCategoriaGruppoInventByClause(UserContext userContext, Dettaglio_contrattoBulk dettaglio_contrattoBulk,
														  Categoria_gruppo_inventHome categoria_gruppo_inventHome, Categoria_gruppo_inventBulk categoria_gruppo_inventBulk,
														  CompoundFindClause compoundfindclause) throws PersistencyException{
		SQLBuilder sql = categoria_gruppo_inventHome.selectByClause(userContext, compoundfindclause);
		List<String> l=dettaglio_contrattoBulk.getContratto().getDettaglio_contratto()
				.stream()
				.filter(e->(e.getCdCategoriaGruppo()!=null && (!(e.getCdCategoriaGruppo().isEmpty()))))
				.map(Dettaglio_contrattoBulk::getCdCategoriaGruppo)
				.map(e->"'".concat(e).concat("'"))
				.collect(Collectors.toList());
		l.remove(categoria_gruppo_inventBulk.getCd_categoria_gruppo());
		String  catGrpAlreadySelected = null;
		if ( l!=null && ( !l.isEmpty()))
			catGrpAlreadySelected=l.stream().collect(Collectors.joining(","));

		if(StringUtils.isNotBlank(catGrpAlreadySelected))
			sql.addSQLClause("AND", "CD_CATEGORIA_GRUPPO NOT IN (" + catGrpAlreadySelected + ")");
		return sql;
	}

}