/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.cnr.contab.incarichi00.comp;

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoHome;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.CdsHome;
import it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaHome;
import it.cnr.contab.config00.sto.bulk.V_struttura_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.V_struttura_organizzativaHome;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorioBulk;
import it.cnr.contab.incarichi00.bulk.VIncarichiAssRicBorseStBulk;
import it.cnr.contab.incarichi00.bulk.VIncarichiAssRicBorseStHome;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.CRUDComponent;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.DateUtils;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;



/**
 * Insert the type's description here.
 * Creation date: (13/09/2006)
 * @author: Flavia Giardina
 */
public class ConsIncarAssRicBorseStComponent extends CRUDComponent{
/**
 * VarBilancioComponent constructor comment.
 */
	
//private String tabAlias;

public ConsIncarAssRicBorseStComponent() {
	super();
}

public it.cnr.jada.persistency.sql.SQLBuilder selectFiltroSoggettoByClause(
		UserContext aUC,
		VIncarichiAssRicBorseStBulk incarichi, 
		TerzoBulk cliente,
		CompoundFindClause clauses) 
				throws ComponentException {
	try
	{
		TerzoHome home = (TerzoHome)getHome(aUC,TerzoBulk.class);
		return home.selectTerzoPerCompensi(incarichi.getFiltroSoggetto() != null ? incarichi.getFiltroSoggetto().getCd_terzo() : null, clauses);
	}
	catch(it.cnr.jada.persistency.PersistencyException ex){throw handleException(ex);}
}

public SQLBuilder selectUoForPrintByClause (CNRUserContext userContext, 
		VIncarichiAssRicBorseStBulk incarichi, it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk uo, CompoundFindClause clause) throws ComponentException, PersistencyException
{	
  	Unita_organizzativaBulk uoScrivania = ((Unita_organizzativaBulk)getHome(userContext, Unita_organizzativaBulk.class).findByPrimaryKey(new Unita_organizzativaBulk(CNRUserContext.getCd_unita_organizzativa(userContext))));
	boolean isUoEnte = uoScrivania.getCd_tipo_unita().compareTo(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE)==0;
	boolean isUoSac  = uoScrivania.getCd_tipo_unita().compareTo(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_SAC)==0;

	SQLBuilder sqlStruttura = getHome(userContext, V_struttura_organizzativaBulk.class).createSQLBuilder();
	sqlStruttura.addClause("AND", "esercizio", SQLBuilder.EQUALS, ((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getEsercizio());
	if (!isUoEnte){
		sqlStruttura.addClause( "AND", "cd_cds", SQLBuilder.EQUALS, userContext.getCd_cds());
	}
	if (isUoSac){
		sqlStruttura.addClause(FindClause.AND, "cdUnitaOrganizzativa", SQLBuilder.EQUALS, CNRUserContext.getCd_unita_organizzativa(userContext));
	}
	
	sqlStruttura.addClause( "AND", "cd_tipo_livello", SQLBuilder.EQUALS, V_struttura_organizzativaHome.LIVELLO_UO);
	sqlStruttura.addSQLJoin( "V_STRUTTURA_ORGANIZZATIVA.CD_ROOT", "UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA");

	SQLBuilder sql = getHome(userContext, uo.getClass()).createSQLBuilder();
	sql.addSQLExistsClause("AND", sqlStruttura);
	sql.addClause( clause );
	return sql;
}

public VIncarichiAssRicBorseStBulk impostaDatiIniziali(
		it.cnr.jada.UserContext userContext,
		VIncarichiAssRicBorseStBulk incarichi)
		throws it.cnr.jada.comp.ComponentException {
	try {
		CdsHome cds_home = (CdsHome) getHome(userContext, CdsBulk.class);
		CdsBulk cds_scrivania = (CdsBulk) cds_home
				.findByPrimaryKey(new CdsBulk(CNRUserContext
						.getCd_cds(userContext)));

		if (cds_scrivania.getCd_tipo_unita().equals(
				Tipo_unita_organizzativaHome.TIPO_UO_ENTE)) {
			incarichi.setUoForPrint(new Unita_organizzativaBulk());
			incarichi.setUOForPrintEnabled(true);
		} else {
			Unita_organizzativaHome uoHome = (Unita_organizzativaHome) getHome(
					userContext, Unita_organizzativaBulk.class);
			Unita_organizzativaBulk uo = (Unita_organizzativaBulk) uoHome
					.findByPrimaryKey(new Unita_organizzativaBulk(
							CNRUserContext
									.getCd_unita_organizzativa(userContext)));

			if (!uo.isUoCds()) {
				incarichi.setUoForPrint(uo);
				incarichi.setUOForPrintEnabled(false);
			} else {
				incarichi
						.setUoForPrint(new Unita_organizzativaBulk());
				incarichi.setUOForPrintEnabled(true);
			}
		}

	} catch (it.cnr.jada.persistency.PersistencyException pe) {
		throw new ComponentException(pe);
	}
	return incarichi;
}

public it.cnr.jada.util.RemoteIterator findIncarichi(UserContext userContext, VIncarichiAssRicBorseStBulk incarichi) throws PersistencyException, IntrospectionException, ComponentException, RemoteException
{
	VIncarichiAssRicBorseStHome incarichiHome = (VIncarichiAssRicBorseStHome)getHome(userContext, VIncarichiAssRicBorseStBulk.class);
	SQLBuilder sql = incarichiHome.createSQLBuilder();
	
  	Unita_organizzativaBulk uoScrivania = ((Unita_organizzativaBulk)getHome(userContext, Unita_organizzativaBulk.class).findByPrimaryKey(new Unita_organizzativaBulk(CNRUserContext.getCd_unita_organizzativa(userContext))));
  		
	SQLBuilder sqlCdsExists = getHome(userContext, Incarichi_repertorioBulk.class).createSQLBuilder();
	sqlCdsExists.resetColumns();
	sqlCdsExists.addColumn("1");
	sqlCdsExists.addSQLJoin("INCARICHI_REPERTORIO.ESERCIZIO_PROCEDURA", "V_INCARICHI_ASS_RIC_BORSE_ST.ESERCIZIO_PROCEDURA");
	sqlCdsExists.addSQLJoin("INCARICHI_REPERTORIO.PG_PROCEDURA", "V_INCARICHI_ASS_RIC_BORSE_ST.PG_PROCEDURA");

	sqlCdsExists.addTableToHeader("ASS_INCARICO_UO");
	sqlCdsExists.addSQLJoin("ASS_INCARICO_UO.ESERCIZIO","INCARICHI_REPERTORIO.ESERCIZIO");
	sqlCdsExists.addSQLJoin("ASS_INCARICO_UO.PG_REPERTORIO","INCARICHI_REPERTORIO.PG_REPERTORIO");

	if (incarichi.getUoForPrint() != null && incarichi.getUoForPrint().getCd_unita_organizzativa() != null){
  		Unita_organizzativaBulk uoFiltro = incarichi.getUoForPrint();
  		sqlCdsExists.addSQLClause(FindClause.AND, "ASS_INCARICO_UO.CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS, uoFiltro.getCd_unita_organizzativa());
			sql.openParenthesis(FindClause.AND);
				sql.addClause(FindClause.OR, "cdUnitaOrganizzativa", SQLBuilder.EQUALS, uoFiltro.getCd_unita_organizzativa());
				sql.addSQLExistsClause(FindClause.OR,sqlCdsExists);
			sql.closeParenthesis();
	} else {
		boolean isUoEnte = uoScrivania.getCd_tipo_unita().compareTo(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE)==0;
		boolean isUoSac  = uoScrivania.getCd_tipo_unita().compareTo(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_SAC)==0;
		

		if (isUoSac)
			sqlCdsExists.addSQLClause(FindClause.AND, "ASS_INCARICO_UO.CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS, CNRUserContext.getCd_unita_organizzativa(userContext));
		else {
			sqlCdsExists.addTableToHeader("V_STRUTTURA_ORGANIZZATIVA");
			sqlCdsExists.addSQLClause(FindClause.AND, "V_STRUTTURA_ORGANIZZATIVA.ESERCIZIO", SQLBuilder.EQUALS, CNRUserContext.getEsercizio(userContext));
			sqlCdsExists.addSQLJoin("V_STRUTTURA_ORGANIZZATIVA.CD_ROOT","ASS_INCARICO_UO.CD_UNITA_ORGANIZZATIVA");
			sqlCdsExists.addSQLClause(FindClause.AND, "V_STRUTTURA_ORGANIZZATIVA.CD_CDS", SQLBuilder.EQUALS, CNRUserContext.getCd_cds(userContext));
		}

		if (!isUoEnte) {
			sql.openParenthesis(FindClause.AND);
			sql.openParenthesis(FindClause.OR);
			sql.addClause(FindClause.AND, "cdCds", SQLBuilder.EQUALS, CNRUserContext.getCd_cds(userContext));
			if (isUoSac)
				sql.addClause(FindClause.AND, "cdUnitaOrganizzativa", SQLBuilder.EQUALS, CNRUserContext.getCd_unita_organizzativa(userContext));
			sql.closeParenthesis();
			sql.addSQLExistsClause(FindClause.OR,sqlCdsExists);
			sql.closeParenthesis();
		} else if (isUoSac) {
			sql.openParenthesis(FindClause.AND);
				sql.addClause(FindClause.OR, "cdUnitaOrganizzativa", SQLBuilder.EQUALS, CNRUserContext.getCd_unita_organizzativa(userContext));
				sql.addSQLExistsClause(FindClause.OR,sqlCdsExists);
			sql.closeParenthesis();
		}
		
	}
	
//	boolean searchTerzo = procedura.getV_terzoForSearch()!=null && procedura.getV_terzoForSearch().getTerzo()!=null &&
//			 			  procedura.getV_terzoForSearch().getTerzo().getCd_terzo()!=null;
//	boolean searchIncarico = procedura.getIncaricoRepertorioForSearch()!=null &&
//						  	 procedura.getIncaricoRepertorioForSearch().getEsercizio()!=null &&
//						  	 procedura.getIncaricoRepertorioForSearch().getPg_repertorio()!=null;
//
//	if (searchTerzo || searchIncarico) {
//		SQLBuilder sqlExists = getHome(userContext, Incarichi_repertorioBulk.class).createSQLBuilder();
//		sqlExists.addSQLJoin( "INCARICHI_REPERTORIO.ESERCIZIO_PROCEDURA", "INCARICHI_PROCEDURA.ESERCIZIO");
//		sqlExists.addSQLJoin( "INCARICHI_REPERTORIO.PG_PROCEDURA", "INCARICHI_PROCEDURA.PG_PROCEDURA");
//		if (searchTerzo)
//			sqlExists.addClause(FindClause.AND, "cd_terzo", SQLBuilder.EQUALS, procedura.getV_terzoForSearch().getTerzo().getCd_terzo());
//		if (searchIncarico) {
//			sqlExists.addClause(FindClause.AND, "esercizio", SQLBuilder.EQUALS, procedura.getIncaricoRepertorioForSearch().getEsercizio());
//			sqlExists.addClause(FindClause.AND, "pg_repertorio", SQLBuilder.EQUALS, procedura.getIncaricoRepertorioForSearch().getPg_repertorio());
//		}
//		sql.addSQLExistsClause(FindClause.AND, sqlExists);
//	}

	sql.addOrderBy("ESERCIZIO");
	sql.addOrderBy("PG_REPERTORIO");
	
//	sql.setDistinctClause(true);
	if (!incarichi.isSceltaFontiTutte()){
		sql.addSQLClause("AND","TIPO_NATURA",SQLBuilder.EQUALS,incarichi.getFonti());
	}  

	filtroStati(incarichi, sql);
	filtroTipologia(incarichi, sql);

	if (incarichi.getFiltroSoggetto() != null && incarichi.getFiltroSoggetto().getCd_terzo() != null){
		sql.addSQLClause("AND","CD_TERZO",SQLBuilder.EQUALS,incarichi.getFiltroSoggetto().getCd_terzo());
	} 

	if (incarichi.getEsercizioValidita() != null){

    	GregorianCalendar gc = (GregorianCalendar) GregorianCalendar.getInstance();
    	gc.setTime(new GregorianCalendar(incarichi.getEsercizioValidita(),Calendar.DECEMBER,31).getTime());
		Date ultimoGiornoAnno = new Timestamp(gc.getTimeInMillis());
		
		sql.openParenthesis(FindClause.AND);
		
		sql.openParenthesis(FindClause.OR);
		sql.addClause(FindClause.AND, "dtProroga", SQLBuilder.ISNULL, null);
		sql.addClause(FindClause.AND, "dtFineValidita", SQLBuilder.ISNULL, null);
		sql.addClause(FindClause.AND, "dtInizioValidita", SQLBuilder.LESS_EQUALS, ultimoGiornoAnno);
		sql.closeParenthesis();
		
		sql.openParenthesis(FindClause.OR);
		sql.addClause(FindClause.AND, "dtProroga", SQLBuilder.ISNULL, null);
		sql.addClause(FindClause.AND, "dtFineValidita", SQLBuilder.ISNOTNULL, null);
		sql.addClause(FindClause.AND, "dtFineValidita", SQLBuilder.GREATER_EQUALS, DateUtils.firstDateOfTheYear(incarichi.getEsercizioValidita()));
		sql.addClause(FindClause.AND, "dtInizioValidita", SQLBuilder.LESS_EQUALS, ultimoGiornoAnno);
		sql.closeParenthesis();

		sql.openParenthesis(FindClause.OR);
		sql.addClause(FindClause.AND, "dtProroga", SQLBuilder.ISNOTNULL, null);
		sql.addClause(FindClause.AND, "dtProroga", SQLBuilder.GREATER_EQUALS, DateUtils.firstDateOfTheYear(incarichi.getEsercizioValidita()));
		sql.addClause(FindClause.AND, "dtInizioValidita", SQLBuilder.LESS_EQUALS, ultimoGiornoAnno);
		sql.closeParenthesis();
		sql.closeParenthesis();
	}
	
	if (incarichi.getDt_validita() != null){
		sql.addSQLClause("AND","DT_INIZIO_VALIDITA",SQLBuilder.LESS_EQUALS,incarichi.getDt_validita());
		sql.openParenthesis(FindClause.AND);

		sql.openParenthesis(FindClause.OR);
		sql.addClause(FindClause.AND, "dtProroga", SQLBuilder.ISNULL, null);
		sql.addClause(FindClause.AND, "dtFineValidita", SQLBuilder.ISNULL, null);
		sql.closeParenthesis();
		
		sql.openParenthesis(FindClause.OR);
		sql.addClause(FindClause.AND, "dtProroga", SQLBuilder.ISNULL, null);
		sql.addClause(FindClause.AND, "dtFineValidita", SQLBuilder.ISNOTNULL, null);
		sql.addClause(FindClause.AND, "dtFineValidita", SQLBuilder.GREATER_EQUALS, incarichi.getDt_validita());
		sql.closeParenthesis();

		sql.openParenthesis(FindClause.OR);
		sql.addClause(FindClause.AND, "dtProroga", SQLBuilder.ISNOTNULL, null);
		sql.addClause(FindClause.AND, "dtProroga", SQLBuilder.GREATER_EQUALS, incarichi.getDt_validita());
		sql.closeParenthesis();

		sql.closeParenthesis();
	}
	
//	sql.addSQLBetweenClause("AND","v_incarichi_ass_ric_borse_st.DT_APPROVAZIONE",incarichi.getDt_validita_da(),incarichi.getDt_validita_a());
	
	
//	sql2.addTableToHeader("PDG_VARIAZIONE_RIGA_GEST");
//	sql2.addTableToHeader("V_CLASSIFICAZIONE_VOCI");
//	sql2.addSQLJoin("PDG_VARIAZIONE_RIGA_GEST.ESERCIZIO",sql2.EQUALS,"V_CONS_VAR_PDGG.ESERCIZIO");
//	sql2.addSQLJoin("PDG_VARIAZIONE_RIGA_GEST.PG_VARIAZIONE_PDG",sql2.EQUALS,"V_CONS_VAR_PDGG.PG_VARIAZIONE_PDG");
//	sql2.addSQLJoin("PDG_VARIAZIONE_RIGA_GEST.ESERCIZIO",sql2.EQUALS,"ELEMENTO_VOCE.ESERCIZIO");
//	sql2.addSQLJoin("PDG_VARIAZIONE_RIGA_GEST.TI_APPARTENENZA",sql2.EQUALS,"ELEMENTO_VOCE.TI_APPARTENENZA");
//	sql2.addSQLJoin("PDG_VARIAZIONE_RIGA_GEST.TI_GESTIONE",sql2.EQUALS,"ELEMENTO_VOCE.TI_GESTIONE");
//	sql2.addSQLJoin("PDG_VARIAZIONE_RIGA_GEST.CD_ELEMENTO_VOCE",sql2.EQUALS,"ELEMENTO_VOCE.CD_ELEMENTO_VOCE");
//	sql2.addSQLJoin("ELEMENTO_VOCE.ID_CLASSIFICAZIONE",sql2.EQUALS,"V_CLASSIFICAZIONE_VOCI.ID_CLASSIFICAZIONE");
//	sql2.addSQLClause("AND","PDG_VARIAZIONE_RIGA_GEST.TI_GESTIONE",sql2.EQUALS,variazioni.getTi_gestione());
//	sql2.addSQLClause("AND","V_CLASSIFICAZIONE_VOCI.CD_LIVELLO1",sql2.EQUALS,variazioni.getV_classificazione_voci().getCd_livello1());
//	sql2.addSQLClause("AND","ROWNUM",sql2.LESS,"2");
//	sql.addSQLClause("AND","V_CONS_VAR_PDGG.ESERCIZIO",sql.EQUALS,CNRUserContext.getEsercizio(userContext));
//	sql.addSQLBetweenClause("AND","V_CONS_VAR_PDGG.ABS_TOT_VARIAZIONE",variazioni.getAbs_tot_variazione_da(),variazioni.getAbs_tot_variazione_a());
//	sql.addSQLExistsClause("AND",sql2);
//	sql.openParenthesis("AND");
//	if (variazioni.getRagr_NO_TIPO()==true)
//	sql.addSQLClause("OR","V_CONS_VAR_PDGG.TIPOLOGIA",sql.EQUALS,"NO_TIPO");
//	if (variazioni.getRagr_PREL_FON()==true)
//	sql.addSQLClause("OR","V_CONS_VAR_PDGG.TIPOLOGIA",sql.EQUALS,"PREL_FON");
//	if (variazioni.getRagr_STO_E_CDS()==true)
//	sql.addSQLClause("OR","V_CONS_VAR_PDGG.TIPOLOGIA",sql.EQUALS,"STO_E_CDS");
//	if (variazioni.getRagr_STO_E_TOT()==true)
//	sql.addSQLClause("OR","V_CONS_VAR_PDGG.TIPOLOGIA",sql.EQUALS,"STO_E_TOT");
//	if (variazioni.getRagr_STO_S_CDS()==true)
//	sql.addSQLClause("OR","V_CONS_VAR_PDGG.TIPOLOGIA",sql.EQUALS,"STO_S_CDS");
//	if (variazioni.getRagr_STO_S_TOT()==true)
//	sql.addSQLClause("OR","V_CONS_VAR_PDGG.TIPOLOGIA",sql.EQUALS,"STO_S_TOT");
//	if (variazioni.getRagr_VAR_MENO_CDS()==true)
//	sql.addSQLClause("OR","V_CONS_VAR_PDGG.TIPOLOGIA",sql.EQUALS,"VAR_MENO_CDS");
//	if (variazioni.getRagr_VAR_MENO_TOT()==true)
//	sql.addSQLClause("OR","V_CONS_VAR_PDGG.TIPOLOGIA",sql.EQUALS,"VAR_MENO_TOT");
//	if (variazioni.getRagr_VAR_PIU_CDS()==true)
//	sql.addSQLClause("OR","V_CONS_VAR_PDGG.TIPOLOGIA",sql.EQUALS,"VAR_PIU_CDS");
//	if (variazioni.getRagr_VAR_PIU_TOT()==true)
//	sql.addSQLClause("OR","V_CONS_VAR_PDGG.TIPOLOGIA",sql.EQUALS,"VAR_PIU_TOT");
//	
//	if (variazioni.getRagr_REST_FOND()==true)
//		sql.addSQLClause("OR","V_CONS_VAR_PDGG.TIPOLOGIA",sql.EQUALS,"REST_FOND");
//	if (variazioni.getRagr_STO_E_AREA()==true)
//		sql.addSQLClause("OR","V_CONS_VAR_PDGG.TIPOLOGIA",sql.EQUALS,"STO_E_AREA");
//	if (variazioni.getRagr_STO_S_AREA()==true)
//		sql.addSQLClause("OR","V_CONS_VAR_PDGG.TIPOLOGIA",sql.EQUALS,"STO_S_AREA");
//	if (variazioni.getRagr_VAR_MENO_FON()==true)
//		sql.addSQLClause("OR","V_CONS_VAR_PDGG.TIPOLOGIA",sql.EQUALS,"VAR_MENO_FON");
//	if (variazioni.getRagr_VAR_PIU_FON()==true)
//		sql.addSQLClause("OR","V_CONS_VAR_PDGG.TIPOLOGIA",sql.EQUALS,"VAR_PIU_FON");
//	sql.closeParenthesis();
	return  iterator(userContext,sql,VIncarichiAssRicBorseStBulk.class,null);
}

private void filtroStati(VIncarichiAssRicBorseStBulk incarichi, SQLBuilder sql) {
	sql.openParenthesis(FindClause.AND);
	if (incarichi.getStatoAnnullatoEliminato()){
		sql.openParenthesis(FindClause.OR);
		sql.addClause(FindClause.AND, "stato", SQLBuilder.EQUALS, Incarichi_repertorioBulk.STATO_ANNULLATO);
		sql.closeParenthesis();
		sql.openParenthesis(FindClause.OR);
		sql.addClause(FindClause.AND, "stato", SQLBuilder.EQUALS, Incarichi_repertorioBulk.STATO_ELIMINATO);
		sql.closeParenthesis();
	}  

	if (incarichi.getStatoChiuso()){
		sql.openParenthesis(FindClause.OR);
		sql.addClause(FindClause.AND, "stato", SQLBuilder.EQUALS, Incarichi_repertorioBulk.STATO_CHIUSO);
		sql.closeParenthesis();
	} 

	if (incarichi.getStatoDefinitivo()){
		sql.openParenthesis(FindClause.OR);
		sql.addClause(FindClause.AND, "stato", SQLBuilder.EQUALS, Incarichi_repertorioBulk.STATO_DEFINITIVO);
		sql.closeParenthesis();
	} 

	if (incarichi.getStatoInviato()){
		sql.openParenthesis(FindClause.OR);
		sql.addClause(FindClause.AND, "stato", SQLBuilder.EQUALS, Incarichi_repertorioBulk.STATO_INVIATO);
		sql.closeParenthesis();
	} 
	if (incarichi.getStatoProvvisorio()){
		sql.openParenthesis(FindClause.OR);
		sql.addClause(FindClause.AND, "stato", SQLBuilder.EQUALS, Incarichi_repertorioBulk.STATO_PROVVISORIO);
		sql.closeParenthesis();
	} 
	sql.closeParenthesis();
}
private void filtroTipologia(VIncarichiAssRicBorseStBulk incarichi, SQLBuilder sql) {
	sql.openParenthesis(FindClause.AND);
	if (incarichi.getTipologiaAssegni()){
		sql.openParenthesis(FindClause.OR);
		sql.addClause(FindClause.AND, "cdTipoRapporto", SQLBuilder.EQUALS, VIncarichiAssRicBorseStBulk.ASSEGNI_DI_RICERCA);
		sql.closeParenthesis();
	}  
	if (incarichi.getTipologiaBorsaDiStudio()){
		sql.openParenthesis(FindClause.OR);
		sql.addClause(FindClause.AND, "cdTipoRapporto", SQLBuilder.EQUALS, VIncarichiAssRicBorseStBulk.BORSA_DI_STUDIO);
		sql.closeParenthesis();
	}  
	if (incarichi.getTipologiaCococo()){
		sql.openParenthesis(FindClause.OR);
		sql.addClause(FindClause.AND, "cdTipoRapporto", SQLBuilder.EQUALS, VIncarichiAssRicBorseStBulk.COLLABORAZIONE_COORDINATA_CONTINUATIVA);
		sql.closeParenthesis();
	}  
	if (incarichi.getTipologiaCollOcc()){
		sql.openParenthesis(FindClause.OR);
		sql.addClause(FindClause.AND, "cdTipoRapporto", SQLBuilder.EQUALS, VIncarichiAssRicBorseStBulk.COLLABORAZIONE_OCCASIONALE);
		sql.closeParenthesis();
	}  
	if (incarichi.getTipologiaCollProf()){
		sql.openParenthesis(FindClause.OR);
		sql.addClause(FindClause.AND, "cdTipoRapporto", SQLBuilder.EQUALS, VIncarichiAssRicBorseStBulk.COLLABORAZIONE_PROFESSIONALE);
		sql.closeParenthesis();
	}  

	sql.closeParenthesis();
}
}
