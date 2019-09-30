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

package it.cnr.contab.bilaterali00.comp;

import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.anagraf00.core.bulk.AnagraficoHome;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk;
import it.cnr.contab.bilaterali00.bulk.BltIstitutiBulk;
import it.cnr.contab.bilaterali00.bulk.Blt_accordiBulk;
import it.cnr.contab.bilaterali00.bulk.Blt_accordiHome;
import it.cnr.contab.bilaterali00.bulk.Blt_autorizzatiBulk;
import it.cnr.contab.bilaterali00.bulk.Blt_autorizzatiHome;
import it.cnr.contab.bilaterali00.bulk.Blt_autorizzati_dettBulk;
import it.cnr.contab.bilaterali00.bulk.Blt_autorizzati_dettHome;
import it.cnr.contab.bilaterali00.bulk.Blt_progettiBulk;
import it.cnr.contab.bilaterali00.bulk.Blt_progettiHome;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.CRUDComponent;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

import java.util.Iterator;

public class BltAccordiComponent extends CRUDComponent {
	public OggettoBulk inizializzaBulkPerInserimento(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
		try {
			oggettobulk =  super.inizializzaBulkPerInserimento(usercontext, oggettobulk);
			
			if (oggettobulk instanceof Blt_accordiBulk) {
				Blt_accordiBulk accordo = (Blt_accordiBulk)oggettobulk; 

				accordo.setFl_spese_visto(Boolean.FALSE);
				accordo.setFl_rimborso_treno(Boolean.FALSE);
				accordo.setFl_conv_fiscale(Boolean.FALSE);
				accordo.setFl_pagamento_ente(Boolean.FALSE);
				accordo.setFl_viaggi_interni(Boolean.FALSE);
				accordo.setFl_lettera_invito(Boolean.FALSE);
				accordo.setFl_atti_amministrativi(Boolean.FALSE);
				accordo.setFl_salta_convenzione(Boolean.FALSE);
			}
			return oggettobulk;
		}
		catch( Exception e )
		{
			throw handleException( e );
		}		
	}
	
	public OggettoBulk inizializzaBulkPerModifica(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
		try {
			oggettobulk =  super.inizializzaBulkPerModifica(usercontext, oggettobulk);
			if (oggettobulk instanceof Blt_accordiBulk) {
				Blt_accordiBulk accordo = (Blt_accordiBulk)oggettobulk; 
				
				Blt_accordiHome accHome = (Blt_accordiHome) getHome( usercontext, Blt_accordiBulk.class );
				accordo.setBlt_progettiColl( new BulkList( accHome.findBlt_progettiList(usercontext, accordo ) ));
				
				Blt_progettiHome prgHome = (Blt_progettiHome) getHome( usercontext, Blt_progettiBulk.class );
				Blt_autorizzatiHome autHome = (Blt_autorizzatiHome) getHome( usercontext, Blt_autorizzatiBulk.class );
				Blt_autorizzati_dettHome autDettHome = (Blt_autorizzati_dettHome) getHome( usercontext, Blt_autorizzati_dettBulk.class );
				AnagraficoHome anagraficoHome = (AnagraficoHome)getHome(usercontext, AnagraficoBulk.class);

				for (Iterator i = accordo.getBlt_progettiColl().iterator(); i.hasNext();) {
					Blt_progettiBulk progetto = (Blt_progettiBulk) i.next();
					initializeKeysAndOptionsInto(usercontext,progetto);
					progetto.setBltProgrammaVisiteItaColl( new BulkList( prgHome.findBltProgrammaVisiteItaList(usercontext, progetto) ));
					progetto.setBltProgrammaVisiteStrColl( new BulkList( prgHome.findBltProgrammaVisiteStrList(usercontext, progetto) ));
					progetto.setBltAutorizzatiItaColl( new BulkList( prgHome.findBltAutorizzatiItaList(usercontext, progetto) ));
					for (Iterator iterator = progetto.getBltAutorizzatiItaColl().iterator(); iterator.hasNext();) {
						Blt_autorizzatiBulk autorizzato = (Blt_autorizzatiBulk) iterator.next();
						initializeKeysAndOptionsInto(usercontext,autorizzato);
						autorizzato.getTerzo().setDipendente(anagraficoHome.findRapportoDipendenteFor(autorizzato.getTerzo().getAnagrafico()));
						autorizzato.setBltAutorizzatiDettColl(new BulkList( autHome.findBltAutorizzatiDettList(usercontext, autorizzato) ));
//						autorizzato.setBltIstituto((BltIstitutiBulk) getHome( usercontext, BltIstitutiBulk.class ).
//								findByPrimaryKey(new BltIstitutiBulk(autorizzato.getCdCdrTerzo())));

						for (Iterator iterator2 = autorizzato.getBltAutorizzatiDettColl().iterator(); iterator2.hasNext();) {
							Blt_autorizzati_dettBulk autorizzatoDett = (Blt_autorizzati_dettBulk) iterator2.next();
							autorizzatoDett.setBltVisiteColl(new BulkList( autDettHome.findBlt_visiteList(usercontext, autorizzatoDett)));
						}
						autorizzato.setBltIstituto((BltIstitutiBulk) getHome( usercontext, BltIstitutiBulk.class ).
								findByPrimaryKey(new BltIstitutiBulk(autorizzato.getCdCdrTerzo())));

						getHomeCache(usercontext).fetchAll(usercontext);
					}
					progetto.setBltAutorizzatiStrColl( new BulkList( prgHome.findBltAutorizzatiStrList(usercontext, progetto)));
					for (Iterator iterator = progetto.getBltAutorizzatiStrColl().iterator(); iterator.hasNext();) {
						Blt_autorizzatiBulk autorizzato = (Blt_autorizzatiBulk) iterator.next();
						autorizzato.getTerzo().setDipendente(anagraficoHome.findRapportoDipendenteFor(autorizzato.getTerzo().getAnagrafico()));
						autorizzato.setBltAutorizzatiDettColl(new BulkList( autHome.findBltAutorizzatiDettList(usercontext, autorizzato) ));
						for (Iterator iterator2 = autorizzato.getBltAutorizzatiDettColl().iterator(); iterator2.hasNext();) {
							Blt_autorizzati_dettBulk autorizzatoDett = (Blt_autorizzati_dettBulk) iterator2.next();
							autorizzatoDett.setBltVisiteColl(new BulkList( autDettHome.findBlt_visiteList(usercontext, autorizzatoDett)));
						}
						autorizzato.setBltIstituto((BltIstitutiBulk) getHome( usercontext, BltIstitutiBulk.class ).
								findByPrimaryKey(new BltIstitutiBulk(autorizzato.getCdCdrTerzo())));
					}
					progetto.setBltIstituto((BltIstitutiBulk) getHome( usercontext, BltIstitutiBulk.class ).
							findByPrimaryKey(new BltIstitutiBulk(progetto.getCentro_responsabilitaIta().getCd_centro_responsabilita())));
					getHomeCache(usercontext).fetchAll(usercontext);
				}
			}
			return oggettobulk;
		}
		catch( Exception e )
		{
			throw handleException( e );
		}		
	}
	@Override
	public Persistent findByPrimaryKey(UserContext arg0, OggettoBulk arg1)
			throws ComponentException {
		Persistent persistent = super.findByPrimaryKey(arg0, arg1);
		if (persistent instanceof BltIstitutiBulk)
			try {
				getHomeCache(arg0).fetchAll(arg0);
			} catch (PersistencyException e) {
				throw handleException( e );
			}
		return persistent;
	}
	
	public it.cnr.jada.persistency.sql.SQLBuilder selectTerzoByClause(UserContext userContext, Blt_autorizzatiBulk autorizzati, TerzoBulk terzo, CompoundFindClause clauses) throws ComponentException, it.cnr.jada.persistency.PersistencyException 
	{
		SQLBuilder sql = getHome(userContext, terzo.getClass()).createSQLBuilder();
		sql.addTableToHeader("ANAGRAFICO");
		sql.addSQLJoin("TERZO.CD_ANAG", SQLBuilder.EQUALS, "ANAGRAFICO.CD_ANAG");
		if (autorizzati.isItaliano())
			sql.addSQLClause(FindClause.AND,"ANAGRAFICO.TI_ITALIANO_ESTERO",SQLBuilder.EQUALS, NazioneBulk.ITALIA);
		else
			sql.addSQLClause(FindClause.AND,"ANAGRAFICO.TI_ITALIANO_ESTERO",SQLBuilder.NOT_EQUALS, NazioneBulk.ITALIA);
		sql.addClause( clauses );
		return sql;
	}
	public it.cnr.jada.persistency.sql.SQLBuilder selectElemento_voceByClause(UserContext userContext, Blt_accordiBulk accordo, Elemento_voceBulk voce, CompoundFindClause clauses) throws ComponentException, it.cnr.jada.persistency.PersistencyException 
	{
		SQLBuilder sql = getHome(userContext, voce.getClass()).createSQLBuilder();
		sql.addClause(FindClause.AND,"esercizio",SQLBuilder.EQUALS, CNRUserContext.getEsercizio(userContext));
		sql.addClause(FindClause.AND,"ti_appartenenza",SQLBuilder.EQUALS, Elemento_voceHome.APPARTENENZA_CDS);
		sql.addClause(FindClause.AND,"ti_gestione",SQLBuilder.EQUALS, Elemento_voceHome.GESTIONE_SPESE);
		sql.addClause( clauses );
		return sql;
	}
	public it.cnr.jada.persistency.sql.SQLBuilder selectResponsabileItaByClause(UserContext userContext, Blt_accordiBulk accordo, TerzoBulk terzo, CompoundFindClause clauses) throws ComponentException, it.cnr.jada.persistency.PersistencyException 
	{
		SQLBuilder sql = getHome(userContext, terzo.getClass()).createSQLBuilder();
		sql.addTableToHeader("ANAGRAFICO");
		sql.addSQLJoin("TERZO.CD_ANAG", SQLBuilder.EQUALS, "ANAGRAFICO.CD_ANAG");
		sql.addSQLClause(FindClause.AND,"ANAGRAFICO.TI_ITALIANO_ESTERO",SQLBuilder.EQUALS, NazioneBulk.ITALIA);
		sql.addClause( clauses );
		return sql;
	}
	public it.cnr.jada.persistency.sql.SQLBuilder selectResponsabileItaByClause(UserContext userContext, Blt_progettiBulk progetto, TerzoBulk terzo, CompoundFindClause clauses) throws ComponentException, it.cnr.jada.persistency.PersistencyException 
	{
		SQLBuilder sql = getHome(userContext, terzo.getClass()).createSQLBuilder();
		sql.addTableToHeader("ANAGRAFICO");
		sql.addSQLJoin("TERZO.CD_ANAG", SQLBuilder.EQUALS, "ANAGRAFICO.CD_ANAG");
		sql.addSQLClause(FindClause.AND,"ANAGRAFICO.TI_ITALIANO_ESTERO",SQLBuilder.EQUALS, NazioneBulk.ITALIA);
		sql.addClause( clauses );
		return sql;
	}
	public it.cnr.jada.persistency.sql.SQLBuilder selectResponsabileStrByClause(UserContext userContext, Blt_progettiBulk progetto, TerzoBulk terzo, CompoundFindClause clauses) throws ComponentException, it.cnr.jada.persistency.PersistencyException 
	{
		SQLBuilder sql = getHome(userContext, terzo.getClass()).createSQLBuilder();
		sql.addTableToHeader("ANAGRAFICO");
		sql.addSQLJoin("TERZO.CD_ANAG", SQLBuilder.EQUALS, "ANAGRAFICO.CD_ANAG");
		sql.addSQLClause(FindClause.AND,"ANAGRAFICO.TI_ITALIANO_ESTERO",SQLBuilder.NOT_EQUALS, NazioneBulk.ITALIA);
		sql.addClause( clauses );
		return sql;
	}

	public Blt_autorizzatiBulk setComuneEnteDiAppartenenza(UserContext userContext,Blt_autorizzatiBulk autorizzato, it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk comune) throws it.cnr.jada.comp.ComponentException {
		autorizzato.setComuneEnteDiAppartenenza(comune);
		autorizzato.setCaps_comune(null);
		initializeKeysAndOptionsInto(userContext,autorizzato);
		if (comune != null)
			autorizzato.setCapEnteDiAppartenenza(comune.getCd_cap());
		else
			autorizzato.setCapEnteDiAppartenenza(null);
		return autorizzato;
	}
	public Blt_progettiBulk setComuneEnteResponsIta(UserContext userContext,Blt_progettiBulk progetto, it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk comune) throws it.cnr.jada.comp.ComponentException {
		progetto.setComuneEnteResponsIta(comune);
		progetto.setCaps_comune(null);
		initializeKeysAndOptionsInto(userContext,progetto);
		if (comune != null)
			progetto.setCap_ente_respons_ita(comune.getCd_cap());
		else
			progetto.setCap_ente_respons_ita(null);
		return progetto;
	}
}
