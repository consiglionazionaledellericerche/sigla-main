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

package it.cnr.contab.config00.pdcfin.bulk;

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

import java.sql.*;

public class Voce_f_cnr_spesaHome extends BulkHome {
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Voce_f_cnr_spesaHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 */
public Voce_f_cnr_spesaHome(java.sql.Connection conn) {
	super(Voce_f_cnr_spesaBulk.class,conn);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Voce_f_cnr_spesaHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 * @param persistentCache	La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
 */
public Voce_f_cnr_spesaHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Voce_f_cnr_spesaBulk.class,conn,persistentCache);
}
/**
 * Restituisce il SQLBuilder per selezionare fra tutte le Voci_f quelle con gestione Spese e appartenenza CNR
 * @return SQLBuilder
 */

public SQLBuilder createSQLBuilder() 
{
	SQLBuilder sql = super.createSQLBuilder();
	sql.addClause("AND", "ti_appartenenza", SQLBuilder.EQUALS, Elemento_voceHome.APPARTENENZA_CNR );
	sql.addClause("AND", "ti_gestione", SQLBuilder.EQUALS, Elemento_voceHome.GESTIONE_SPESE );
	return sql;
}
/**
 * Elimina dalla struttura del PDCF parte spese del CNR il sottoarticolo corrispondente all'area di ricerca
 * collegata all'uo specificata nel caso l'uo specificata sia l'ultima uo collegata.
 * @parameter cap istanza di Voce_f valorizzata per l'estrazione dei capitoli interessati (categoria 1 parte spese cnr)
 * @parameter uo unità organizzativa in processo
 */

public void eliminaSottoArticoliCategoria1(Voce_f_cnr_spesaBulk cap, it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk uo)
    throws
        BusyResourceException,
        PersistencyException,
        OutdatedResourceException,
        ApplicationException {

    try {
        java.util.List aL = find(cap);
        for (int i = 0; i < aL.size(); i++) {
            Voce_f_cnr_spesaBulk aC = (Voce_f_cnr_spesaBulk) aL.get(i);
            lock(aC);
        }

        try {
        	LoggableStatement ps =
                new LoggableStatement(getConnection(),
                    "DELETE FROM "
                        + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()
                        + "VOCE_F "
                        + "WHERE ESERCIZIO = ? AND "
                        + "TI_APPARTENENZA = ? AND "
                        + "TI_GESTIONE = ? AND "
                        + "CD_PARTE = ? AND "
                        + "TI_VOCE = ? AND "
                        + "CD_CDS = ? AND "
                        + "CD_PROPRIO_VOCE = ? ",true,this.getClass());
        	ps.setObject(1, cap.getEsercizio());
            ps.setString(2, Elemento_voceHome.APPARTENENZA_CNR);
            ps.setString(3, Elemento_voceHome.GESTIONE_SPESE);
            ps.setString(4, cap.getCd_parte());
            ps.setString(5, Voce_fHome.TIPO_SOTTOARTICOLO);
            ps.setString(6, cap.getCd_cds());
            ps.setObject(7, uo.getCds_area_ricerca().getCd_unita_organizzativa());

            ps.executeQuery();
        } catch (SQLException e) {
            throw new PersistencyException(e);
        }

        return;
    } catch (FindException e) {
        throw new ApplicationException("Errore interno su aggiornamento PDC Finanziario!");
    } 
}
/**
 * Aggiunge nella struttura del PDCF parte spese del CNR il sottoarticolo corrispondente all'area di ricerca
 * collegata all'uo specificata.
 * @parameter cap istanza di Voce_f valorizzata per l'estrazione dei capitoli interessati (categoria 1 parte spese cnr)
 * @parameter uo unità organizzativa in processo
 */

public void inserisciSottoArticoliCategoria1(UserContext userContext, Voce_f_cnr_spesaBulk cap, it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk uo)
    throws
        BusyResourceException,
        PersistencyException,
        OutdatedResourceException,
        ApplicationException {

    try {
        java.util.List aL = find(cap);
        for (int i = 0; i < aL.size(); i++) {
            Voce_f_cnr_spesaBulk aC = (Voce_f_cnr_spesaBulk) aL.get(i);
            lock(aC);
          Voce_f_cnr_spesaBulk articolo = new Voce_f_cnr_spesaBulk();
          articolo.setCd_voce_padre(aC.getCd_voce());
          articolo.setEsercizio(aC.getEsercizio());
          articolo.setTi_voce(Voce_fHome.TIPO_ARTICOLO);
          java.util.List aLN = find(articolo);
          for (int j = 0; j < aLN.size(); j++) {
	       articolo = (Voce_f_cnr_spesaBulk)aLN.get(j);
           Voce_f_cnr_spesaBulk sottoarticolo = new Voce_f_cnr_spesaBulk();
           sottoarticolo.setCd_voce_padre(articolo.getCd_voce());
           sottoarticolo.setEsercizio(aC.getEsercizio());
           sottoarticolo.setTi_voce(Voce_fHome.TIPO_SOTTOARTICOLO);
           sottoarticolo.setCd_proprio_voce(uo.getCds_area_ricerca().getCd_unita_organizzativa());
           sottoarticolo.setCd_voce(articolo.getCd_voce()+"."+uo.getCds_area_ricerca().getCd_unita_organizzativa());
           sottoarticolo.setCd_cds(uo.getUnita_padre().getCd_unita_organizzativa());
           sottoarticolo.setCd_parte(articolo.getCd_parte());
           sottoarticolo.setDs_voce(uo.getCds_area_ricerca().getDs_unita_organizzativa());
           sottoarticolo.setCd_funzione(articolo.getCd_funzione());
           sottoarticolo.setLivello(new Integer(Voce_fHome.LIVELLO_SOTTOARTICOLO_SPESE_CNR));
           sottoarticolo.setFl_mastrino(new Boolean(true));
           sottoarticolo.setCd_categoria(articolo.getCd_categoria());
           sottoarticolo.setCd_natura(articolo.getCd_natura());
           sottoarticolo.setUser(cap.getUser());
           insert(sottoarticolo, userContext);
          }           
        }
        return;
    } catch (FindException e) {
        throw new ApplicationException("Errore interno su aggiornamento PDC Finanziario!");
    } 
}
}
