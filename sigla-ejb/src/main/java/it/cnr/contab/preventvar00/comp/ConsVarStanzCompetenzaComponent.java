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

package it.cnr.contab.preventvar00.comp;

import java.rmi.RemoteException;

import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;

import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.config00.pdcfin.cla.bulk.Classificazione_vociBulk;
import it.cnr.contab.config00.pdcfin.cla.bulk.Parametri_livelliBulk;
import it.cnr.contab.config00.pdcfin.cla.bulk.Parametri_livelliHome;
import it.cnr.contab.config00.pdcfin.cla.bulk.V_classificazione_vociBulk;
import it.cnr.contab.doccont00.singconto.bulk.V_voce_f_sing_contoBulk;
import it.cnr.contab.pdg01.bulk.Pdg_variazione_riga_gestHome;
import it.cnr.contab.preventvar00.consultazioni.bulk.V_cons_var_pdggBulk;
import it.cnr.contab.preventvar00.consultazioni.bulk.V_cons_var_pdggHome;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.CRUDComponent;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.*;



/**
 * Insert the type's description here.
 * Creation date: (13/09/2006)
 * @author: Flavia Giardina
 */
public class ConsVarStanzCompetenzaComponent extends CRUDComponent{
/**
 * VarBilancioComponent constructor comment.
 */
	
//private String tabAlias;

public ConsVarStanzCompetenzaComponent() {
	super();
}

public it.cnr.jada.util.RemoteIterator findVariazioni(UserContext userContext, V_cons_var_pdggBulk variazioni) throws PersistencyException, IntrospectionException, ComponentException, RemoteException
{
	V_cons_var_pdggHome variazioniHome = (V_cons_var_pdggHome)getHome(userContext, V_cons_var_pdggBulk.class);
	Elemento_voceHome elemento_voceHome = (Elemento_voceHome)getHome(userContext, Elemento_voceBulk.class);
	SQLBuilder sql = variazioniHome.createSQLBuilder();
	sql.setDistinctClause(true);
	SQLBuilder sql2 = elemento_voceHome.createSQLBuilder();
	sql2.resetColumns();
	sql2.addColumn("1");
	sql2.addTableToHeader("PDG_VARIAZIONE_RIGA_GEST");
	sql2.addTableToHeader("V_CLASSIFICAZIONE_VOCI");
	sql2.addSQLJoin("PDG_VARIAZIONE_RIGA_GEST.ESERCIZIO",sql2.EQUALS,"V_CONS_VAR_PDGG.ESERCIZIO");
	sql2.addSQLJoin("PDG_VARIAZIONE_RIGA_GEST.PG_VARIAZIONE_PDG",sql2.EQUALS,"V_CONS_VAR_PDGG.PG_VARIAZIONE_PDG");
	sql2.addSQLJoin("PDG_VARIAZIONE_RIGA_GEST.ESERCIZIO",sql2.EQUALS,"ELEMENTO_VOCE.ESERCIZIO");
	sql2.addSQLJoin("PDG_VARIAZIONE_RIGA_GEST.TI_APPARTENENZA",sql2.EQUALS,"ELEMENTO_VOCE.TI_APPARTENENZA");
	sql2.addSQLJoin("PDG_VARIAZIONE_RIGA_GEST.TI_GESTIONE",sql2.EQUALS,"ELEMENTO_VOCE.TI_GESTIONE");
	sql2.addSQLJoin("PDG_VARIAZIONE_RIGA_GEST.CD_ELEMENTO_VOCE",sql2.EQUALS,"ELEMENTO_VOCE.CD_ELEMENTO_VOCE");
	sql2.addSQLJoin("ELEMENTO_VOCE.ID_CLASSIFICAZIONE",sql2.EQUALS,"V_CLASSIFICAZIONE_VOCI.ID_CLASSIFICAZIONE");
	sql2.addSQLClause("AND","PDG_VARIAZIONE_RIGA_GEST.TI_GESTIONE",sql2.EQUALS,variazioni.getTi_gestione());
	sql2.addSQLClause("AND","V_CLASSIFICAZIONE_VOCI.CD_LIVELLO1",sql2.EQUALS,variazioni.getV_classificazione_voci().getCd_livello1());
	sql2.addSQLClause("AND","ROWNUM",sql2.LESS,"2");
	sql.addSQLClause("AND","V_CONS_VAR_PDGG.ESERCIZIO",sql.EQUALS,CNRUserContext.getEsercizio(userContext));
	sql.addSQLBetweenClause("AND","V_CONS_VAR_PDGG.DT_APPROVAZIONE",variazioni.getDt_approvazione_da(),variazioni.getDt_approvazione_a());
	sql.addSQLBetweenClause("AND","V_CONS_VAR_PDGG.ABS_TOT_VARIAZIONE",variazioni.getAbs_tot_variazione_da(),variazioni.getAbs_tot_variazione_a());
	sql.addSQLExistsClause("AND",sql2);
	sql.openParenthesis("AND");
	if (variazioni.getRagr_NO_TIPO()==true)
	sql.addSQLClause("OR","V_CONS_VAR_PDGG.TIPOLOGIA",sql.EQUALS,"NO_TIPO");
	if (variazioni.getRagr_PREL_FON()==true)
	sql.addSQLClause("OR","V_CONS_VAR_PDGG.TIPOLOGIA",sql.EQUALS,"PREL_FON");
	if (variazioni.getRagr_STO_E_CDS()==true)
	sql.addSQLClause("OR","V_CONS_VAR_PDGG.TIPOLOGIA",sql.EQUALS,"STO_E_CDS");
	if (variazioni.getRagr_STO_E_TOT()==true)
	sql.addSQLClause("OR","V_CONS_VAR_PDGG.TIPOLOGIA",sql.EQUALS,"STO_E_TOT");
	if (variazioni.getRagr_STO_S_CDS()==true)
	sql.addSQLClause("OR","V_CONS_VAR_PDGG.TIPOLOGIA",sql.EQUALS,"STO_S_CDS");
	if (variazioni.getRagr_STO_S_TOT()==true)
	sql.addSQLClause("OR","V_CONS_VAR_PDGG.TIPOLOGIA",sql.EQUALS,"STO_S_TOT");
	if (variazioni.getRagr_VAR_MENO_CDS()==true)
	sql.addSQLClause("OR","V_CONS_VAR_PDGG.TIPOLOGIA",sql.EQUALS,"VAR_MENO_CDS");
	if (variazioni.getRagr_VAR_MENO_TOT()==true)
	sql.addSQLClause("OR","V_CONS_VAR_PDGG.TIPOLOGIA",sql.EQUALS,"VAR_MENO_TOT");
	if (variazioni.getRagr_VAR_PIU_CDS()==true)
	sql.addSQLClause("OR","V_CONS_VAR_PDGG.TIPOLOGIA",sql.EQUALS,"VAR_PIU_CDS");
	if (variazioni.getRagr_VAR_PIU_TOT()==true)
	sql.addSQLClause("OR","V_CONS_VAR_PDGG.TIPOLOGIA",sql.EQUALS,"VAR_PIU_TOT");
	
	if (variazioni.getRagr_REST_FOND()==true)
		sql.addSQLClause("OR","V_CONS_VAR_PDGG.TIPOLOGIA",sql.EQUALS,"REST_FOND");
	if (variazioni.getRagr_STO_E_AREA()==true)
		sql.addSQLClause("OR","V_CONS_VAR_PDGG.TIPOLOGIA",sql.EQUALS,"STO_E_AREA");
	if (variazioni.getRagr_STO_S_AREA()==true)
		sql.addSQLClause("OR","V_CONS_VAR_PDGG.TIPOLOGIA",sql.EQUALS,"STO_S_AREA");
	if (variazioni.getRagr_VAR_MENO_FON()==true)
		sql.addSQLClause("OR","V_CONS_VAR_PDGG.TIPOLOGIA",sql.EQUALS,"VAR_MENO_FON");
	if (variazioni.getRagr_VAR_PIU_FON()==true)
		sql.addSQLClause("OR","V_CONS_VAR_PDGG.TIPOLOGIA",sql.EQUALS,"VAR_PIU_FON");
	sql.closeParenthesis();
	return  iterator(userContext,sql,V_cons_var_pdggBulk.class,null);
}

public Parametri_livelliBulk findParametriLivelli(UserContext userContext, Integer esercizio) throws it.cnr.jada.comp.ComponentException {
	try
	{
		Parametri_livelliHome parametri_livelliHome = (Parametri_livelliHome) getHome(userContext, Parametri_livelliBulk.class );
		Parametri_livelliBulk parametri_livelliBulk = (Parametri_livelliBulk)parametri_livelliHome.findByPrimaryKey(new Parametri_livelliBulk(esercizio));
		if (parametri_livelliBulk==null)
			throw new ApplicationException("Parametri Livelli non definiti per l'esercizio " + esercizio + ".");
		return parametri_livelliBulk;
	}
	catch (Exception e )
	{
		throw handleException( e );
	}	
}

public String getDsLivelloClassificazione(UserContext userContext, Integer esercizio, String tipo, Integer livello) throws it.cnr.jada.comp.ComponentException {
	Parametri_livelliBulk parametri = findParametriLivelli(userContext, esercizio);
	if (tipo.equals(V_cons_var_pdggBulk.TI_GESTIONE_ENTRATE))
		return parametri.getDs_livello_etr(livello.intValue());
	else
		return parametri.getDs_livello_spe(livello.intValue());
}

/**
 * 	Ritorna la Descrizione del Livello a cui appartiene la classificazione <cla> indicata.
 */
public String getDsLivelloClassificazione(UserContext userContext, Classificazione_vociBulk cla) throws it.cnr.jada.comp.ComponentException {
	Parametri_livelliBulk parametri = findParametriLivelli(userContext, cla.getEsercizio());
	if (cla.getTi_gestione().equals(V_cons_var_pdggBulk.TI_GESTIONE_ENTRATE))
		return parametri.getDs_livello_etr(cla.getLivelloMax().intValue());
	else
		return parametri.getDs_livello_spe(cla.getLivelloMax().intValue());
}

public SQLBuilder selectV_classificazione_vociByClause(UserContext userContext,V_cons_var_pdggBulk variazioni,V_classificazione_vociBulk classificazioneVoci,CompoundFindClause clause) throws ComponentException, PersistencyException {
	SQLBuilder sql = getHome(userContext, classificazioneVoci).createSQLBuilder();
	
	sql.addClause(clause);
	sql.addClause("AND", "esercizio", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
	sql.addClause("AND", "ti_gestione", sql.EQUALS, variazioni.getTi_gestione());
	sql.addClause("AND", "cd_livello1", sql.ISNOTNULL , null);
	sql.addClause("AND", "cd_livello2", sql.ISNULL , null);
	if (clause != null) 
		sql.addClause(clause);
	return sql;
}



}
