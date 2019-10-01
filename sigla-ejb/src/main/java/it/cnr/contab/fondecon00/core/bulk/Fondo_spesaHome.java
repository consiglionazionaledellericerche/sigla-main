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

package it.cnr.contab.fondecon00.core.bulk;

import it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk;
import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.sql.*;

public class Fondo_spesaHome extends BulkHome {
public Fondo_spesaHome(java.sql.Connection conn) {
	super(Fondo_spesaBulk.class,conn);
}
public Fondo_spesaHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Fondo_spesaBulk.class,conn,persistentCache);
}
public SQLBuilder ass_all_speSQL(
	it.cnr.jada.UserContext context, 
	Fondo_economaleBulk testata,
	Obbligazione_scadenzarioBulk obbScad)
	throws  it.cnr.jada.persistency.PersistencyException,
			it.cnr.jada.comp.ComponentException {

	if (testata == null || 
		(testata.getFl_associatata_for_search() != null &&
		testata.getFl_associatata_for_search().booleanValue()))
		return null;
		

	SQLBuilder sql = cercaSpeseAssociabili(context, testata, obbScad);
	String schema = it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema();
	sql.addUpdateClause("fl_obbligazione", Boolean.TRUE);
	sql.addUpdateClause("utuv", context.getUser());
	sql.addUpdateSQLClause("DUVA = SYSDATE");
	sql.addUpdateSQLClause("PG_VER_REC = PG_VER_REC+1");
	sql.setCommand("UPDATE");

	return sql;
}

	public String ass_spe_obbSQL(it.cnr.jada.UserContext context, Fondo_economaleBulk testata, it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk obbscad) throws  it.cnr.jada.persistency.PersistencyException {

		String schema = it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema();
		String user = it.cnr.contab.utenze00.bp.CNRUserContext.getUser(context);

		return	"UPDATE "+schema+"FONDO_SPESA "
				+"SET CD_CDS_OBBLIGAZIONE = '"+obbscad.getCd_cds()+"', "
					+"ESERCIZIO_OBBLIGAZIONE = "+obbscad.getEsercizio()+", "
					+"ESERCIZIO_ORI_OBBLIGAZIONE = "+obbscad.getEsercizio_originale()+", "
					+"PG_OBBLIGAZIONE = "+obbscad.getPg_obbligazione()+", "
					+"PG_OBBLIGAZIONE_SCADENZARIO = "+obbscad.getPg_obbligazione_scadenzario()+", "
					+"DUVA = SYSDATE, "
					+"UTUV = '"+user+"', "
					+"PG_VER_REC = PG_VER_REC+1 "
				+"WHERE CD_CDS = '"+testata.getCd_cds()+"' "
					+"AND ESERCIZIO = "+testata.getEsercizio()+" "
					+"AND CD_UNITA_ORGANIZZATIVA = '"+testata.getCd_unita_organizzativa()+"' "
					+"AND CD_CODICE_FONDO = '"+testata.getCd_codice_fondo()+"' "
					+"AND FL_OBBLIGAZIONE = 'Y' "
					+"AND FL_DOCUMENTATA = 'N' "
					+"AND FL_REINTEGRATA = 'N' "
					+"AND ( "
						+"CD_CDS_OBBLIGAZIONE IS NULL "
						+"OR ESERCIZIO_OBBLIGAZIONE IS NULL "
						+"OR ESERCIZIO_ORI_OBBLIGAZIONE IS NULL "
						+"OR PG_OBBLIGAZIONE IS NULL "
						+"OR PG_OBBLIGAZIONE_SCADENZARIO IS NULL "
					+")";
	}

	public SQLBuilder cercaSpese(it.cnr.jada.UserContext context, Filtro_ricerca_speseVBulk filtro) throws it.cnr.jada.comp.ComponentException {

		SQLBuilder sql = ((Fondo_spesaHome)getHomeCache().getHome(Fondo_spesaBulk.class)).createSQLBuilder();

		//Fondo economale key
		sql.addClause("AND", "cd_cds", sql.EQUALS, filtro.getFondo_economale().getCd_cds());
		sql.addClause("AND", "esercizio", sql.EQUALS, filtro.getFondo_economale().getEsercizio());
		sql.addClause("AND", "cd_unita_organizzativa", sql.EQUALS, filtro.getFondo_economale().getCd_unita_organizzativa());
		sql.addClause("AND", "cd_codice_fondo", sql.EQUALS, filtro.getFondo_economale().getCd_codice_fondo());

		//Altri
		if(filtro.getPg_fondo_spesa() != null)
			sql.addClause("AND", "pg_fondo_spesa", sql.EQUALS, filtro.getPg_fondo_spesa());

		if(filtro.getCodice_fiscale() != null && filtro.getCodice_fiscale() != "")
			sql.addClause("AND", "codice_fiscale", sql.EQUALS, filtro.getCodice_fiscale());

		if(filtro.getPartita_iva() != null && filtro.getPartita_iva() != "")
			sql.addClause("AND", "partita_iva", sql.EQUALS, filtro.getPartita_iva());

		if(filtro.getDenominazione_fornitore() != null && filtro.getDenominazione_fornitore() != "")
			sql.addClause("AND", "denominazione_fornitore", sql.EQUALS, filtro.getDenominazione_fornitore());

		if(filtro.getDt_spesa() != null)
			sql.addClause("AND", "dt_spesa", sql.EQUALS, filtro.getDt_spesa());

		if(filtro.getIm_ammontare_spesa() != null)
			sql.addClause("AND", "im_ammontare_spesa", sql.EQUALS, filtro.getIm_ammontare_spesa());

		sql.addClause("AND", "fl_documentata", sql.EQUALS, filtro.getFl_documentata());

		sql.addClause("AND", "fl_reintegrata", sql.EQUALS, filtro.getFl_reintegrata());

		return sql;
	}

public SQLBuilder cercaSpeseAssociabili(
		it.cnr.jada.UserContext context, 
		Fondo_economaleBulk testata,
		it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk scadenza)
	throws it.cnr.jada.comp.ComponentException {

		SQLBuilder sql = ((Fondo_spesaHome)getHomeCache().getHome(Fondo_spesaBulk.class)).createSQLBuilder();
		
		sql.addClause("AND", "cd_cds", sql.EQUALS, testata.getCd_cds());
		sql.addClause("AND", "esercizio", sql.EQUALS, testata.getEsercizio());
		sql.addClause("AND", "cd_unita_organizzativa", sql.EQUALS, testata.getCd_unita_organizzativa());
		sql.addClause("AND", "cd_codice_fondo", sql.EQUALS, testata.getCd_codice_fondo());

		sql.addClause("AND", "fl_documentata", sql.EQUALS, Boolean.FALSE);
		sql.addClause("AND", "fl_reintegrata", sql.EQUALS, Boolean.FALSE);
		
		if (testata.getFl_associatata_for_search() != null && testata.getFl_associatata_for_search().booleanValue())
			sql.addClause("AND", "fl_obbligazione", sql.EQUALS, Boolean.TRUE);
		
		if (scadenza == null) {
			throw new it.cnr.jada.comp.ApplicationException("Selezionare una scadenza di impegno per associare le spese!");
		} else {
			sql.openParenthesis("AND");
			sql.openParenthesis("AND");
			sql.addClause("AND", "cd_cds_obbligazione", sql.EQUALS, scadenza.getCd_cds());
			sql.addClause("AND", "esercizio_obbligazione", sql.EQUALS, scadenza.getEsercizio());
			sql.addClause("AND", "esercizio_ori_obbligazione", sql.EQUALS, scadenza.getEsercizio_originale());
			sql.addClause("AND", "pg_obbligazione", sql.EQUALS, scadenza.getPg_obbligazione());
			sql.addClause("AND", "pg_obbligazione_scadenzario", sql.EQUALS, scadenza.getPg_obbligazione_scadenzario());
			sql.closeParenthesis();
			sql.addClause("OR", "pg_obbligazione_scadenzario", sql.ISNULL, null);
			sql.closeParenthesis();
			
			TerzoBulk creditore = scadenza.getObbligazione().getCreditore();
			if (!AnagraficoBulk.DIVERSI.equalsIgnoreCase(creditore.getAnagrafico().getTi_entita()))
				sql.addClause("AND", "cd_terzo", sql.EQUALS, creditore.getCd_terzo());
		}

		return sql;
}

public SQLBuilder cercaSpeseDelFondo(
	Fondo_economaleBulk fondo,
	Obbligazione_scadenzarioBulk scadenza) throws it.cnr.jada.comp.ComponentException {

	SQLBuilder sql = createSQLBuilder();

	//Fondo economale key
	sql.addClause("AND", "cd_cds", sql.EQUALS,fondo.getCd_cds());
	sql.addClause("AND", "esercizio", sql.EQUALS, fondo.getEsercizio());
	sql.addClause("AND", "cd_unita_organizzativa", sql.EQUALS, fondo.getCd_unita_organizzativa());
	sql.addClause("AND", "cd_codice_fondo", sql.EQUALS, fondo.getCd_codice_fondo());

	//Compatibilità
	sql.addClause("AND", "fl_reintegrata", sql.EQUALS, fondo.getFl_reintegrata_for_search());
	sql.addClause("AND", "fl_documentata", sql.EQUALS, fondo.getFl_documentata_for_search());

	if (scadenza != null) {
		//V_ASS_OBBSCAD_FONDO_SPESA
		sql.addSQLClause("AND", "V_ASS_OBBSCAD_FONDO_SPESA.IN_CD_CDS_OBBLIGAZIONE", sql.EQUALS, scadenza.getCd_cds());
		sql.addSQLClause("AND", "V_ASS_OBBSCAD_FONDO_SPESA.IN_ESERCIZIO_OBBLIGAZIONE", sql.EQUALS, scadenza.getEsercizio());
		sql.addSQLClause("AND", "V_ASS_OBBSCAD_FONDO_SPESA.IN_ESERCIZIO_ORI_OBBLIGAZIONE", sql.EQUALS, scadenza.getEsercizio_originale());
		sql.addSQLClause("AND", "V_ASS_OBBSCAD_FONDO_SPESA.IN_PG_OBBLIGAZIONE", sql.EQUALS, scadenza.getPg_obbligazione());
		sql.addSQLClause("AND", "V_ASS_OBBSCAD_FONDO_SPESA.IN_PG_OBBLIGAZIONE_SCADENZARIO", sql.EQUALS, scadenza.getPg_obbligazione_scadenzario());
	}
	return sql;
}

	public SQLBuilder cercaSpeseReintegrabili(it.cnr.jada.UserContext context, Filtro_ricerca_speseVBulk filtro) throws it.cnr.jada.comp.ComponentException {

		SQLBuilder sql = ((Fondo_spesaHome)getHomeCache().getHome(Fondo_spesaBulk.class)).createSQLBuilder();

		//Fondo economale key
		sql.addClause("AND", "cd_cds", sql.EQUALS, filtro.getFondo_economale().getCd_cds());
		sql.addClause("AND", "esercizio", sql.EQUALS, filtro.getFondo_economale().getEsercizio());
		sql.addClause("AND", "cd_unita_organizzativa", sql.EQUALS, filtro.getFondo_economale().getCd_unita_organizzativa());
		sql.addClause("AND", "cd_codice_fondo", sql.EQUALS, filtro.getFondo_economale().getCd_codice_fondo());

		//Obbligazione e compatibilità
		sql.addClause("AND", "fl_reintegrata", sql.EQUALS, Boolean.FALSE);
		sql.addClause("AND", "fl_documentata", sql.EQUALS, filtro.getFl_documentata());
		if (!filtro.getFl_documentata().booleanValue()) {
			sql.openParenthesis("AND");
			sql.addClause("AND", "cd_cds_obbligazione", sql.ISNOTNULL, null);
			sql.addClause("AND", "esercizio_obbligazione", sql.ISNOTNULL, null);
			sql.addClause("AND", "esercizio_ori_obbligazione", sql.ISNOTNULL, null);
			sql.addClause("AND", "pg_obbligazione", sql.ISNOTNULL, null);
			sql.addClause("AND", "pg_obbligazione_scadenzario", sql.ISNOTNULL, null);
			sql.closeParenthesis();
		}

		//Altri
		if(filtro.getPg_fondo_spesa() != null)
			sql.addClause("AND", "pg_fondo_spesa", sql.EQUALS, filtro.getPg_fondo_spesa());

		if(filtro.getCodice_fiscale() != null && filtro.getCodice_fiscale() != "")
			sql.addClause("AND", "codice_fiscale", sql.EQUALS, filtro.getCodice_fiscale());

		if(filtro.getPartita_iva() != null && filtro.getPartita_iva() != "")
			sql.addClause("AND", "partita_iva", sql.EQUALS, filtro.getPartita_iva());

		if(filtro.getDenominazione_fornitore() != null && filtro.getDenominazione_fornitore() != "")
			sql.addClause("AND", "denominazione_fornitore", sql.EQUALS, filtro.getDenominazione_fornitore());

		if(filtro.getDt_spesa() != null)
			sql.addClause("AND", "dt_spesa", sql.GREATER_EQUALS, filtro.getDt_spesa());

		if(filtro.getIm_ammontare_spesa() != null)
			sql.addClause("AND", "im_ammontare_spesa", sql.GREATER_EQUALS, filtro.getIm_ammontare_spesa());

		return sql;
	}

	/**
	 * Recupera tutti i cap relativi al città fornitore in uso.
	 *
	 * @param spesa La spesa in uso.
	 * @param capHome .
	 * @param clause .
	 *
	 * @return java.util.Collection Collezione di oggetti {@link it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk }
	 *
	 * @exception IntrospectionException
	 * @exception PersistencyException
	 *
	 * @see  {@link it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk#findCaps }
	 */

	public java.util.Collection findCaps_fornitore(Fondo_spesaBulk spesa,
												it.cnr.contab.anagraf00.tabter.bulk.CapHome capHome,
												it.cnr.contab.anagraf00.tabter.bulk.CapBulk clause)
													throws IntrospectionException, PersistencyException {

		if (spesa.getCitta() == null || spesa.getCitta().getPg_comune() == null)
			return null;
		return ((it.cnr.contab.anagraf00.tabter.bulk.ComuneHome)
					getHomeCache().getHome(
						it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk.class
					)
				).findCaps(spesa.getCitta());
	}

	public SQLBuilder getSpeseMese(Fondo_economaleBulk testata, int year, int month) throws PersistencyException {

		SQLBuilder sql = createSQLBuilder();

		//Fondo economale key
		sql.addClause("AND", "cd_cds", sql.EQUALS, testata.getCd_cds());
		sql.addClause("AND", "esercizio", sql.EQUALS, testata.getEsercizio());
		sql.addClause("AND", "cd_unita_organizzativa", sql.EQUALS, testata.getCd_unita_organizzativa());
		sql.addClause("AND", "cd_codice_fondo", sql.EQUALS, testata.getCd_codice_fondo());
		//Clause
		sql.addSQLClause( "AND", "TO_CHAR(DT_SPESA, 'MM') = '"+ (month<10? "0"+month: ""+month) +"'" );
		sql.addSQLClause( "AND", "TO_CHAR(DT_SPESA, 'YYYY') = '"+year+"'" );

		return sql;
	}

public SQLBuilder getTotaleSpese(
	Fondo_economaleBulk testata,
	Obbligazione_scadenzarioBulk scadenza) {

	SQLBuilder sql = createSQLBuilder();
	sql.setHeader("SELECT SUM(IM_AMMONTARE_SPESA) SOMMA_SPESE ");
	sql.addClause("AND", "cd_cds", sql.EQUALS,testata.getCd_cds());
	sql.addClause("AND", "cd_unita_organizzativa", sql.EQUALS,testata.getCd_unita_organizzativa());
	sql.addClause("AND", "cd_codice_fondo", sql.EQUALS,testata.getCd_codice_fondo());
	sql.addClause("AND", "esercizio", sql.EQUALS,testata.getEsercizio());

	sql.addClause("AND", "fl_documentata", sql.EQUALS, testata.getFl_documentata_for_search());
	sql.addClause("AND", "fl_reintegrata", sql.EQUALS, testata.getFl_reintegrata_for_search());

	if (scadenza != null) {
		//V_ASS_OBBSCAD_FONDO_SPESA
		sql.addSQLClause("AND", "V_ASS_OBBSCAD_FONDO_SPESA.IN_CD_CDS_OBBLIGAZIONE", sql.EQUALS, scadenza.getCd_cds());
		sql.addSQLClause("AND", "V_ASS_OBBSCAD_FONDO_SPESA.IN_ESERCIZIO_OBBLIGAZIONE", sql.EQUALS, scadenza.getEsercizio());
		sql.addSQLClause("AND", "V_ASS_OBBSCAD_FONDO_SPESA.IN_ESERCIZIO_ORI_OBBLIGAZIONE", sql.EQUALS, scadenza.getEsercizio_originale());
		sql.addSQLClause("AND", "V_ASS_OBBSCAD_FONDO_SPESA.IN_PG_OBBLIGAZIONE", sql.EQUALS, scadenza.getPg_obbligazione());
		sql.addSQLClause("AND", "V_ASS_OBBSCAD_FONDO_SPESA.IN_PG_OBBLIGAZIONE_SCADENZARIO", sql.EQUALS, scadenza.getPg_obbligazione_scadenzario());
	}
	return sql;
	//return buildObbligazioneClauseOn(
					//sql, 
					//scadenza, 
					//testata.getFl_documentata_for_search());
}

	public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext,OggettoBulk spe) throws PersistencyException {
		try {
			((Fondo_spesaBulk)spe).setPg_fondo_spesa(
				new Long(
					((Long)findAndLockMax( spe, "pg_fondo_spesa", new Long(0) )).longValue()+1
				)
			);
		} catch(it.cnr.jada.bulk.BusyResourceException e) {
			throw new PersistencyException(e);
		}
	}

public SQLBuilder rem_all_ass_speSQL(
	it.cnr.jada.UserContext context, 
	Fondo_economaleBulk testata,
	Obbligazione_scadenzarioBulk obbScad)
	throws  it.cnr.jada.persistency.PersistencyException,
			it.cnr.jada.comp.ComponentException {

	SQLBuilder sql = cercaSpeseAssociabili(context, testata, obbScad);
	String schema = it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema();
	sql.addUpdateClause("fl_obbligazione", Boolean.FALSE);
	sql.addUpdateNullClause("obb_scad", Obbligazione_scadenzarioBulk.class);
	sql.addUpdateClause("utuv", context.getUser());
	sql.addUpdateSQLClause("DUVA = SYSDATE");
	sql.addUpdateSQLClause("PG_VER_REC = PG_VER_REC+1");
	sql.setCommand("UPDATE");
	
	return sql;
}

}
