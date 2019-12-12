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

package it.cnr.contab.inventario00.comp;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaHome;
import it.cnr.contab.inventario00.tabrif.bulk.Ass_inventario_uoBulk;
import it.cnr.contab.inventario00.tabrif.bulk.Ass_inventario_uoHome;
import it.cnr.contab.inventario00.tabrif.bulk.Id_inventarioBulk;
import it.cnr.contab.inventario00.tabrif.bulk.Id_inventarioHome;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.Query;
import it.cnr.jada.persistency.sql.SQLBuilder;

import java.io.Serializable;

public class IdInventarioComponent
        extends it.cnr.jada.comp.CRUDComponent
        implements ICRUDMgr, IIdInventarioMgr, Cloneable, Serializable {


    public IdInventarioComponent() {

        /*Default constructor*/


    }
//^^@@

    /**
     * Errore nella validazione inventario.
     * PreCondition:
     * ValidaId_inventario  non superato.
     * PostCondition:
     * Non  viene consentita la registrazione dell'inventario.
     * <p>
     * Tutti i controlli superati.
     * PreCondition:
     * Nessun errore rilevato
     * PostCondition:
     * Viene consentito il salvataggio.
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param bulk        <code>OggettoBulk</code> il Bulk da creare
     * @return l'oggetto <code>OggettoBulk</code> creato
     **/
//^^@@
    public OggettoBulk creaConBulk(UserContext aUC, OggettoBulk bulk) throws ComponentException {

        Id_inventarioBulk inv = (Id_inventarioBulk) bulk;
        Id_inventarioHome invHome = (Id_inventarioHome) getHome(aUC, Id_inventarioBulk.class);

        try {
            this.validaId_Inventario(aUC, inv);

            invHome.assegnaProgressivo(inv);
            inv.distribuisciProgressivo();

            return super.creaConBulk(aUC, inv);

        } catch (it.cnr.jada.comp.CRUDValidationException e) {
            throw handleException(inv, e);
        } catch (it.cnr.jada.bulk.OutdatedResourceException e) {
            throw handleException(inv, e);
        } catch (it.cnr.jada.bulk.BusyResourceException e) {
            throw handleException(inv, e);
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(inv, e);
        }

    }

    /**
     * Cerca l'inventario associato ad una data UO
     * PreCondition:
     * E' stata generata la richiesta di cercare l'Inventario (Id_InventarioBulk),
     * associato ad una UO.
     * PostCondition:
     * Effettua la ricerca addottando come criteri di ricerca il Codice del CdS e della UO passati come argomento.
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param cdCds       il <code>String</code> codice del CdS di cui bisogna cercare l'Inventario associato
     * @param cdUo        il <code>String</code> codice della UO di cui bisogna cercare l'Inventario associato
     * @param resp        il <code>boolean</code> flag che indica se bisogna cercare l'Associazione responsabile
     * @return l'Inventario <code>Id_inventarioBulk</code> trovata
     **/
    public Id_inventarioBulk findInventarioFor(UserContext aUC, String cdCds, String cdUo, boolean resp) throws ComponentException {

        try {
            Id_inventarioHome invHome = (Id_inventarioHome) getHome(aUC, Id_inventarioBulk.class);
            return invHome.findInventarioFor(aUC, cdCds, cdUo, resp);
        } catch (it.cnr.jada.persistency.PersistencyException ex) {
            throw handleException(ex);
        } catch (it.cnr.jada.persistency.IntrospectionException ex) {
            throw handleException(ex);
        }
    }
//^^@@

    /**
     * Inizializzazione di una istanza di Id_InventarioBulk
     * PreCondition:
     * E' stata generata la richiesta di inizializzare una istanza di Id_InventarioBulk
     * PostCondition:
     * Vengono cercate e caricate le Unità Organizzative disponibili per essere associate all'Inventario
     * che si sta creando.
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param bulk        <code>OggettoBulk</code> l'Inventario che deve essere inizializzato
     * @return <code>OggettoBulk</code> l'Inventario inizializzato
     */
//^^@@
    public OggettoBulk inizializzaBulkPerInserimento(UserContext userContext, OggettoBulk bulk) throws ComponentException {

        try {

            Id_inventarioBulk inv = (Id_inventarioBulk) super.inizializzaBulkPerInserimento(userContext, bulk);
            inv.setAss_inventario_uo(new it.cnr.jada.bulk.BulkList());

            loadAvailableUo(userContext, inv);

            return inv;
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(bulk, e);
        } catch (it.cnr.jada.persistency.IntrospectionException e) {
            throw handleException(bulk, e);
        }
    }
//^^@@

    /**
     * Inizializzazione di un Id_Inventario per modifica
     * PreCondition:
     * E' stata generata la richiesta di inizializzare l'Inventario per la modifica.
     * PostCondition:
     * Vengono caricate tutte le Unità Oganizzative associate con l'Inventario che si vuole modificare
     * Tra le UO caricate, poi, si va ad individuare quella Responsabile dell'Inventario.
     * Infine si caricano le UO ancora disponibli per essere associate.
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param bulk        <code>OggettoBulk</code> l'Inventario che deve essere inizializzato
     * @return <code>OggettoBulk</code> l'Inventario inizializzato
     */
//^^@@
    public OggettoBulk inizializzaBulkPerModifica(UserContext aUC, OggettoBulk bulk) throws ComponentException {

        try {
            Id_inventarioBulk inv = (Id_inventarioBulk) super.inizializzaBulkPerModifica(aUC, bulk);

            // Carico le Unita Organizzative Associate all'Inventario
            Ass_inventario_uoHome assInvUoHome = (Ass_inventario_uoHome) getHome(aUC, Ass_inventario_uoBulk.class);
            inv.setAss_inventario_uo(new it.cnr.jada.bulk.BulkList(assInvUoHome.findAssInvUoFor(inv)));

            // Cerco il responsabile per la visualizzazione
            for (java.util.Iterator i = inv.getAss_inventario_uo().iterator(); i.hasNext(); ) {
                Ass_inventario_uoBulk assInvUo = (Ass_inventario_uoBulk) i.next();
                if (assInvUo.getFl_responsabile().booleanValue())
                    inv.setAssInvUoResp(assInvUo);
            }

            // Cerco le Uo non ancora associate a nessun inventario
            loadAvailableUo(aUC, inv);

            getHomeCache(aUC).fetchAll(aUC);
            return inv;
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(bulk, e);
        } catch (it.cnr.jada.persistency.IntrospectionException e) {
            throw handleException(bulk, e);
        }
    }

    /**
     * Inventario aperto
     * PreCondition:
     * L'Inventario è in stato 'A', (Aperto)
     * PostCondition:
     * Ritorna True
     * <p>
     * Inventario non e' aperto
     * PreCondition:
     * L'Inventario NON è in stato 'A', (Aperto)
     * PostCondition:
     * Ritorna False
     *
     * @param aUC       lo <code>UserContext</code> che ha generato la richiesta
     * @param inv       <code>Id_inventarioBulk</code> l'Inventario di cui deve essere controllato lo stato
     * @param esercizio <code>Integer</code> l'Esercizio di in cui vafatto il controllo
     * @return lo stato <code>boolean</code> dell'Inventario
     **/
    public boolean isAperto(UserContext aUC, Id_inventarioBulk inv, Integer esercizio) throws ComponentException {

        try {
            Id_inventarioHome invHome = (Id_inventarioHome) getHome(aUC, Id_inventarioBulk.class);
            return invHome.isAperto(inv, esercizio);
        } catch (it.cnr.jada.persistency.PersistencyException ex) {
            throw handleException(ex);
        } catch (it.cnr.jada.persistency.IntrospectionException ex) {
            throw handleException(ex);
        }
    }

    /**
     * Carico le UO disponibili per essere associate ad un Inventario
     * PreCondition:
     * In seguito ad una richiesta di inizializzazione per la creazione di un nuovo Inventario,
     * o per la modifica di uno già esistente, si vanno a cercacre quelle UO che non sono
     * ancora associate ad alcun Inventario.
     * PostCondition:
     * Carico le UO disponibili.
     *
     * @param aUC lo <code>UserContext</code> che ha generato la richiesta
     * @param inv <code>Id_inventarioBulk</code> l'Inventario di riferimento
     */
    private void loadAvailableUo(UserContext userContext, Id_inventarioBulk inv) throws ComponentException, it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException {

// Carico le Unita Organizzative Disponibili per l'associazione

        Unita_organizzativaHome uoHome = (Unita_organizzativaHome) getHome(userContext, Unita_organizzativaBulk.class);
        it.cnr.jada.persistency.sql.SQLBuilder sqlUo = uoHome.createSQLBuilder();

        // 1° Static Clause: Le Uo devono appartenere al CDS di scrivania
        sqlUo.addSQLClause("AND", "CD_UNITA_PADRE", SQLBuilder.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext));
        // 2° Static Clause: Posso selezionare Uo non precedentemente associate
        sqlUo.addSQLClause("AND", "NOT EXISTS (SELECT * FROM ASS_INVENTARIO_UO WHERE UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA = ASS_INVENTARIO_UO.CD_UNITA_ORGANIZZATIVA)");
        sqlUo.addOrderBy("CD_UNITA_ORGANIZZATIVA");

        inv.setAss_inventario_uoDisponibili(new it.cnr.jada.bulk.BulkList());
        for (java.util.Iterator i = uoHome.fetchAll(sqlUo).iterator(); i.hasNext(); ) {
            Unita_organizzativaBulk uo = (Unita_organizzativaBulk) i.next();
            Ass_inventario_uoBulk assInvUo = new Ass_inventario_uoBulk();
            assInvUo.setFl_responsabile(new Boolean(false));
            assInvUo.setInventario(inv);
            assInvUo.setUnita_organizzativa(uo);
            assInvUo.setCd_cds(uo.getCd_unita_padre());
            inv.getAss_inventario_uoDisponibili().add(assInvUo);
        }
    }

    /**
     * Modifica di un Inventario
     * PreCondition:
     * E' stata generata la richiesta di modificare un Inventario.
     * Le modifiche passano la validazione, (metodo validaId_Inventario).
     * PostCondition:
     * Viene consentito il salvataggio.
     * <p>
     * Modifica non valida.
     * PreCondition:
     * ValidaId_Inventario non superato
     * PostCondition:
     * Viene inviato un messaggio : "Attenzione questa modifica non è consentita".
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param bulk        <code>OggettoBulk</code> il Bulk da modificare
     * @return l'oggetto <code>OggettoBulk</code> modificato
     **/
    public OggettoBulk modificaConBulk(UserContext aUC, OggettoBulk bulk) throws ComponentException {
        try {

            Id_inventarioBulk inv = (Id_inventarioBulk) bulk;
            this.validaId_Inventario(aUC, inv);

            return super.modificaConBulk(aUC, inv);

        } catch (it.cnr.jada.comp.CRUDValidationException e) {
            throw handleException(bulk, e);
        } catch (Throwable e) {
            throw handleException(bulk, e);
        }
    }

    /**
     * Ricerca di un Inventario
     * PreCondition:
     * E' stata generata la richiesta di ricercare gli Inventari relativi al CdS di scrivania
     * PostCondition:
     * E' stato creato il SQLBuilder con le clausole implicite (presenti nell'istanza di Id_inventarioBulk),
     * ed è stata aggiunta la clausola che gli Inventari devono essere associati al CdS
     * di scrivania.
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param clauses     <code>CompoundFindClause</code> le clausole della selezione
     * @param bulk        <code>OggettoBulk</code> l'Inventario modello
     * @return sql <code>SQLBuilder</code> Risultato della selezione.
     **/
    public Query select(UserContext userContext, CompoundFindClause clauses, OggettoBulk bulk)
            throws ComponentException, it.cnr.jada.persistency.PersistencyException {
		SQLBuilder sqlAssInventario = getHome(userContext, Ass_inventario_uoBulk.class).createSQLBuilder();
		sqlAssInventario.resetColumns();
		sqlAssInventario.addColumn("PG_INVENTARIO");
		sqlAssInventario.addClause(FindClause.AND, "cd_cds", SQLBuilder.EQUALS, CNRUserContext.getCd_cds(userContext));

        Id_inventarioBulk inventario = (Id_inventarioBulk) bulk;
        SQLBuilder sql = (SQLBuilder) super.select(userContext, clauses, inventario);
		sql.addSQLINClause(FindClause.AND, "PG_INVENTARIO", sqlAssInventario);
        return sql;
    }

    /**
     * validaId_Inventario - UO non associate
     * PreCondition:
     * Si sta tentando di salvare un Inventario a cui non sono state associate UO
     * PostCondition:
     * Un messaggio di errore viene visualizzato all'utente per segnalare l'impossibilità di salvare l'Inventario
     * <p>
     * validaId_Inventario - UO Responsabile non specificata
     * PreCondition:
     * Si sta tentando di salvare un Inventario di cui non è stato specificato il Responsabile tra le UO associate
     * PostCondition:
     * Un messaggio di errore viene visualizzato all'utente per segnalare l'impossibilità di salvare l'Inventario
     *
     * @param aUC  lo <code>UserContext</code> che ha generato la richiesta
     * @param bulk <code>Id_inventarioBulk</code> l'Inventario modello
     **/
    private void validaId_Inventario(UserContext aUC, Id_inventarioBulk bulk) throws ComponentException {

        if (bulk.getClass() == Id_inventarioBulk.class)
            if (bulk.getUo().size() == 0)
                throw new it.cnr.jada.comp.CRUDValidationException("Attenzione: è necessario associare almeno una Unita Organizzativa");
        if (bulk.getUoResp() == null)
            throw new it.cnr.jada.comp.CRUDValidationException("Attenzione: è necessario definire un responsabile tra le Unita Organizzativa selezionate");
    }
}
