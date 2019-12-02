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

package it.cnr.contab.fondiric00.comp;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.fondiric00.core.bulk.*;
import it.cnr.contab.fondiric00.tabrif.bulk.*;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.*;


public class FondoRicercaComponent extends it.cnr.jada.comp.CRUDComponent implements IFondoRicercaMgr {
/**
 * FondoRicercaComponent constructor comment.
 */
public FondoRicercaComponent() {
	super();
}
/**
 * Pre:  Controllo Dt_inizio > Dt_fine
 * Post: Segnalazione "Data di fine deve essere maggiore della data di inizio!"
 *
 * Pre:  Controllo se Dt_fine = null e Dt_proroga != null
 * Post: Segnalazione "Non può esistere una data di proroga se non si indica una data di fine!"
 *
 * Pre:  Controllo Dt_fine > Dt_proroga
 * Post: Segnalazione "Data di proroga deve essere maggiore della data di fine!"
 *
 * Pre:  Controllo se la lista dei dettagli è vuota
 * Post: Se vuota viene creato un unico dettaglio che ha:
 *			UO = l'UO coordinatrice del fondo
 *			Responsabile = Responsabile del fondo
 *			Importo = Importo del fondo
 *			
 * Pre:  Controllo somma importo dettagli != da importo del fondo
 * Post: Segnalazione "La somma degli importi degli assegnatari è diversa dall'importo del fondo"
 *
 */
	public OggettoBulk creaConBulk(UserContext uc, OggettoBulk bulk) throws ComponentException {

		intBulk(uc, (Fondo_attivita_vincolataBulk)bulk );

		return super.creaConBulk(uc, bulk);
	}

	public void eliminaConBulk(it.cnr.jada.UserContext aUC, it.cnr.jada.bulk.OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
		super.eliminaConBulk(aUC, bulk);
	}

	public it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerInserimento(it.cnr.jada.UserContext aUC, it.cnr.jada.bulk.OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
		return super.inizializzaBulkPerInserimento(aUC, bulk);
	}

/**
 * Pre:  Preparare l'oggetto alle modifiche;
 * Post: carica la lista di dettagli associati a un fondo
 */
	public OggettoBulk inizializzaBulkPerModifica(UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
		try {
			Fondo_attivita_vincolataBulk testata = (Fondo_attivita_vincolataBulk)super.inizializzaBulkPerModifica(userContext,bulk);
			Fondo_attivita_vincolataHome testataHome = (Fondo_attivita_vincolataHome)getHome(userContext, Fondo_attivita_vincolataBulk.class);
			testata.setDettagli(new it.cnr.jada.bulk.BulkList(testataHome.findDettagli(testata)));

			getHomeCache(userContext).fetchAll(userContext);
			return testata;
		} catch(Exception e) {
			throw handleException(e);
		}
	}

	public it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerRicerca(it.cnr.jada.UserContext aUC, it.cnr.jada.bulk.OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
		return super.inizializzaBulkPerRicerca(aUC, bulk);
	}

	public it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerRicercaLibera(it.cnr.jada.UserContext aUC, it.cnr.jada.bulk.OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
		return super.inizializzaBulkPerRicercaLibera(aUC, bulk);
	}

	private Fondo_attivita_vincolataBulk intBulk(UserContext userContext, Fondo_attivita_vincolataBulk bulk) throws ComponentException {

		//se data di fine esiste deve essere minore di data inizio
		if(bulk.getDt_fine() != null && bulk.getDt_inizio().after( bulk.getDt_fine() ))
			throw new it.cnr.jada.comp.ApplicationException("Data di fine deve essere maggiore della data di inizio!");

		//se data di fine non esiste non deve esistere data di proroga
		if(bulk.getDt_fine() == null && bulk.getDt_proroga() != null)
			throw new it.cnr.jada.comp.ApplicationException("Non può esistere una data di proroga se non si indica una data di fine!");

		//se data di proroga esiste deve essere minore di data fine
		if(bulk.getDt_proroga() != null && bulk.getDt_fine().after( bulk.getDt_proroga() ))
			throw new it.cnr.jada.comp.ApplicationException("Data di proroga deve essere maggiore della data di fine!");

		if (bulk.getImporto_fondo() == null)
			throw new it.cnr.jada.comp.ApplicationException("L'importo è obbligatorio.");

		if (bulk.getUnita_organizzativa() == null)
			throw new it.cnr.jada.comp.ApplicationException("L'unità organizzativa è obbligatoria.");

		//se non vengono specificati dettagli si crea un dettaglio di default
		if( bulk.getDettagli().isEmpty() ) {
			Fondo_assegnatarioBulk dett = new Fondo_assegnatarioBulk(
				bulk.getUnita_organizzativa().getCd_unita_organizzativa(),
				bulk.getUnita_organizzativa()
			);
			dett.setResponsabile( bulk.getResponsabile() );
			dett.setImporto( bulk.getImporto_fondo() );
			dett.setCrudStatus( dett.TO_BE_CREATED );
			dett.setUser( bulk.getUser() );
			bulk.addToDettagli(dett);

		} else { //in caso conrrario

			//calcolo la discrepanza tra importo fondo e importo dettagli
			java.math.BigDecimal sum = new java.math.BigDecimal(0);
			java.math.BigDecimal residuo = new java.math.BigDecimal(0);
			for(int i = 0; bulk.getDettagli().size() > i; i++) {
				if ( ((Fondo_assegnatarioBulk) bulk.getDettagli().get(i) ).getCd_unita_organizzativa().equals( bulk.getCd_unita_organizzativa() )
					&& ((Fondo_assegnatarioBulk) bulk.getDettagli().get(i) ).getImporto() != null )
				{
					residuo = ((Fondo_assegnatarioBulk) bulk.getDettagli().get(i) ).getImporto();
				} else if(((Fondo_assegnatarioBulk) bulk.getDettagli().get(i) ).getImporto() != null)
				{
					sum = sum.add(
						((Fondo_assegnatarioBulk) bulk.getDettagli().get(i) ).getImporto()
					);
				}
			}

			sum = bulk.getImporto_fondo().subtract( sum );

			//se i detagli hanno un importo maggiore del fondo
			if(sum.doubleValue() < 0) {
				throw new it.cnr.jada.comp.ApplicationException("La somma degli importi degli assegnatari è superiore all'importo del fondo");

			//se i detagli hanno un importo minore del fondo
			} else if(residuo.compareTo(sum) != 0
					&& sum.doubleValue() > 0) {

				//cerco il dettaglio di default
				int c=-1;
				do {
					c++;
				} while(
					c < bulk.getDettagli().size()
					&&
					!((Fondo_assegnatarioBulk)
						bulk.getDettagli().get(c)
					).getCd_unita_organizzativa().equals(
						bulk.getCd_unita_organizzativa()
					)
				);

				//se non c'è lo creo con un importo uguale alla squadratura
				if(c == bulk.getDettagli().size()) {
					Fondo_assegnatarioBulk dett = new Fondo_assegnatarioBulk(
						bulk.getUnita_organizzativa().getCd_unita_organizzativa(),
						bulk.getUnita_organizzativa()
					);
					dett.setResponsabile( bulk.getResponsabile() );
					dett.setImporto( sum );
					dett.setCrudStatus( dett.TO_BE_CREATED );
					dett.setUser( bulk.getUser() );
					bulk.addToDettagli(dett);

				//se c'è gli assegno un importo uguale alla squadratura
				} else {
					((Fondo_assegnatarioBulk) bulk.getDettagli().get(c) ).setImporto( sum );
				}
			}

		}

		return bulk;
	}

/**
 * Pre:  Controllo Dt_inizio > Dt_fine
 * Post: Segnalazione "Data di fine deve essere maggiore della data di inizio!"
 *
 * Pre:  Controllo se Dt_fine = null e Dt_proroga != null
 * Post: Segnalazione "Non può esistere una data di proroga se non si indica una data di fine!"
 *
 * Pre:  Controllo Dt_fine > Dt_proroga
 * Post: Segnalazione "Data di proroga deve essere maggiore della data di fine!"
 *
 * Pre:  Controllo se la lista dei dettagli è vuota
 * Post: Se vuota viene creato un unico dettaglio che ha:
 *			UO = l'UO coordinatrice del fondo
 *			Responsabile = Responsabile del fondo
 *			Importo = Importo del fondo
 *			
 * Pre:  Controllo somma importo dettagli != da importo del fondo
 * Post: Segnalazione "La somma degli importi degli assegnatari è diversa dall'importo del fondo"
 *
 */
	public OggettoBulk modificaConBulk(UserContext uc, OggettoBulk bulk) throws ComponentException {

		intBulk(uc, (Fondo_attivita_vincolataBulk)bulk );

		return super.modificaConBulk(uc, bulk);
	}

/**
 * Pre:  Ricerca terzo ente
 * Post: Limitazione ai terzi ancora validi.
 */
 	public SQLBuilder selectEnteByClause (UserContext userContext,
											OggettoBulk bulk,
											TerzoBulk ente,
											CompoundFindClause clause)
 	throws ComponentException, PersistencyException
 	{
		if (clause == null) clause = ente.buildFindClauses(null);

		SQLBuilder sql = getHome(userContext, ente).createSQLBuilder();
		sql.addClause(
			it.cnr.jada.persistency.sql.CompoundFindClause.or(
				new it.cnr.jada.persistency.sql.SimpleFindClause("AND", "dt_fine_rapporto", sql.ISNULL, null),
				new it.cnr.jada.persistency.sql.SimpleFindClause("AND", "dt_fine_rapporto", sql.GREATER, getHome(userContext, ente).getServerTimestamp())
			)
		);

		if (clause != null) sql.addClause(clause);

		return sql;
	}

/**
 * Pre:  Ricerca terzo responsabile
 * Post: Limitazione ai terzi ancora validi.
 */
 	public SQLBuilder selectResponsabileByClause(UserContext userContext,
												OggettoBulk bulk,
												TerzoBulk responsabile,
												CompoundFindClause clause)
 	throws ComponentException, PersistencyException
 	{
		if (clause == null) clause = responsabile.buildFindClauses(null);

		SQLBuilder sql = getHome(userContext, responsabile).createSQLBuilder();
		sql.addClause(
			it.cnr.jada.persistency.sql.CompoundFindClause.or(
				new it.cnr.jada.persistency.sql.SimpleFindClause("AND", "dt_fine_rapporto", sql.ISNULL, null),
				new it.cnr.jada.persistency.sql.SimpleFindClause("AND", "dt_fine_rapporto", sql.GREATER, getHome(userContext, responsabile).getServerTimestamp())
			)
		);

		if (clause != null) sql.addClause(clause);

		return sql;
	}

/**
 * Pre:  Ricerca UO
 * Post: Limitazione alle UO valide.
 */
 	public SQLBuilder selectUnita_organizzativaByClause (UserContext userContext,
														OggettoBulk bulk,
														Unita_organizzativaBulk uo,
														CompoundFindClause clause)
 	throws ComponentException, PersistencyException
 	{
		if (clause == null) clause = uo.buildFindClauses(null);

		SQLBuilder sql = getHome(userContext, uo, "V_UNITA_ORGANIZZATIVA_VALIDA").createSQLBuilder();
		sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio( userContext ) );

		if (clause != null) sql.addClause(clause);

		return sql;
	}

}
