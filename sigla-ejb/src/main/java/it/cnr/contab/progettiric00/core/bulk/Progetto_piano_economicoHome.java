package it.cnr.contab.progettiric00.core.bulk;

import java.util.List;

import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.bulk.Parametri_cnrHome;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.config00.pdcfin.cla.bulk.Classificazione_vociBulk;
import it.cnr.contab.config00.pdcfin.cla.bulk.V_classificazione_vociBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class Progetto_piano_economicoHome extends BulkHome {

	public Progetto_piano_economicoHome(java.sql.Connection conn) {
		super(Progetto_piano_economicoBulk.class,conn);
	}
	
	public Progetto_piano_economicoHome(java.sql.Connection conn,PersistentCache persistentCache) {
		super(Progetto_piano_economicoBulk.class,conn,persistentCache);
	}

	public java.util.Collection findProgettoPianoEconomicoList( Integer pgProgetto ) throws PersistencyException 
	{
		SQLBuilder sql = this.createSQLBuilder();
		sql.addClause(FindClause.AND, "pg_progetto",SQLBuilder.EQUALS,pgProgetto);
		return this.fetchAll(sql);
	}

	@Override
	public Persistent completeBulkRowByRow(UserContext userContext, Persistent persistent) throws PersistencyException {
		Progetto_piano_economicoBulk prgPiaeco = (Progetto_piano_economicoBulk)persistent;
		V_saldi_piano_econom_progettoBulk saldoSpesa = ((V_saldi_piano_econom_progettoHome)getHomeCache().getHome(V_saldi_piano_econom_progettoBulk.class )).
				cercaSaldoPianoEconomico(prgPiaeco, "S");

		V_saldi_piano_econom_progettoBulk saldoEntrata = ((V_saldi_piano_econom_progettoHome)getHomeCache().getHome(V_saldi_piano_econom_progettoBulk.class )).
				cercaSaldoPianoEconomico(prgPiaeco, "E");

		prgPiaeco.setSaldoSpesa(saldoSpesa);
		prgPiaeco.setSaldoEntrata(saldoEntrata);

		prgPiaeco.setVociBilancioAssociate(new BulkList(((Ass_progetto_piaeco_voceHome)getHomeCache().getHome(Ass_progetto_piaeco_voceBulk.class ))
				.findAssProgettoPiaecoVoceList(prgPiaeco.getPg_progetto(), prgPiaeco.getCd_unita_organizzativa(), prgPiaeco.getCd_voce_piano(), 
						prgPiaeco.getEsercizio_piano())));

		return super.completeBulkRowByRow(userContext, persistent);
	}

	public SQLBuilder selectProgettoPianoEconomicoList( java.lang.Integer esercizio, java.lang.Integer pgProgetto, Integer idClassificazione ) throws PersistencyException {
		SQLBuilder sql = this.createSQLBuilder();
        sql.addSQLClause(FindClause.AND, "PROGETTO_PIANO_ECONOMICO.PG_PROGETTO", SQLBuilder.EQUALS, pgProgetto);
		
        Ass_progetto_piaeco_voceHome assHome = (Ass_progetto_piaeco_voceHome)getHomeCache().getHome(Ass_progetto_piaeco_voceBulk.class);
    	SQLBuilder sqlExists = assHome.selectAssProgettoPiaecoVoceList(esercizio, pgProgetto, idClassificazione);
        sqlExists.addSQLJoin("ASS_PROGETTO_PIAECO_VOCE.PG_PROGETTO", "PROGETTO_PIANO_ECONOMICO.PG_PROGETTO");
        sqlExists.addSQLJoin("ASS_PROGETTO_PIAECO_VOCE.CD_UNITA_ORGANIZZATIVA", "PROGETTO_PIANO_ECONOMICO.CD_UNITA_ORGANIZZATIVA");
        sqlExists.addSQLJoin("ASS_PROGETTO_PIAECO_VOCE.CD_VOCE_PIANO", "PROGETTO_PIANO_ECONOMICO.CD_VOCE_PIANO");
        sqlExists.addSQLJoin("ASS_PROGETTO_PIAECO_VOCE.ESERCIZIO_PIANO", "PROGETTO_PIANO_ECONOMICO.ESERCIZIO_PIANO");

        sql.addSQLExistsClause(FindClause.AND, sqlExists);
		return sql;
	}

	public java.util.Collection<Progetto_piano_economicoBulk> findProgettoPianoEconomicoList( java.lang.Integer esercizio, java.lang.Integer pgProgetto, Integer idClassificazione ) throws PersistencyException {
		SQLBuilder sql = this.selectProgettoPianoEconomicoList(esercizio, pgProgetto, idClassificazione);
		return this.fetchAll(sql);
	}
}
