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

package it.cnr.contab.inventario00.tabrif.bulk;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.BusyResourceException;
import it.cnr.jada.bulk.OutdatedResourceException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;

public class Id_inventarioHome extends BulkHome {
    protected Id_inventarioHome(Class clazz, java.sql.Connection connection) {
        super(clazz, connection);
    }

    protected Id_inventarioHome(Class clazz, java.sql.Connection connection, PersistentCache persistentCache) {
        super(clazz, connection, persistentCache);
    }

    public Id_inventarioHome(java.sql.Connection conn) {
        super(Id_inventarioBulk.class, conn);
    }

    public Id_inventarioHome(java.sql.Connection conn, PersistentCache persistentCache) {
        super(Id_inventarioBulk.class, conn, persistentCache);
    }

    public void assegnaProgressivo(Id_inventarioBulk id_inventario) throws PersistencyException, OutdatedResourceException, BusyResourceException {
        Long x = new Long(0);
        try {
            x = (Long) (findAndLockMax(id_inventario, "pg_inventario", x)) + 1;
        } catch (it.cnr.jada.bulk.BusyResourceException e) {
            throw new PersistencyException(e);
        }
        id_inventario.setPg_inventario(x);
    }
//^^@@

    /**
     * Carica il consegnatario relativo all'Inventario "inv"
     * Ritorna null se non è definita un'associazione
     */
//^^@@
    public it.cnr.contab.anagraf00.core.bulk.TerzoBulk findConsegnatarioFor(Id_inventarioBulk inv) throws PersistencyException, IntrospectionException {

        Inventario_consegnatarioHome invCHome = (Inventario_consegnatarioHome) getHomeCache().getHome(Inventario_consegnatarioBulk.class);
        Inventario_consegnatarioBulk invC = invCHome.findInventarioConsegnatarioFor(inv);
        if (invC == null)
            return null;
        return invC.getConsegnatario();
    }
//^^@@

    /**
     * Carica il delegato relativo all'Inventario "inv"
     * Ritorna null se non è definita un'associazione
     */
//^^@@
    public it.cnr.contab.anagraf00.core.bulk.TerzoBulk findDelegatoFor(Id_inventarioBulk inv) throws PersistencyException, IntrospectionException {

        Inventario_consegnatarioHome invCHome = (Inventario_consegnatarioHome) getHomeCache().getHome(Inventario_consegnatarioBulk.class);
        Inventario_consegnatarioBulk invC = invCHome.findInventarioConsegnatarioFor(inv);
        if (invC == null)
            return null;
        return invC.getDelegato();
    }

    public Id_inventarioBulk findInventarioFor(it.cnr.jada.UserContext aUC, boolean resp) throws PersistencyException, IntrospectionException {

        // Trova l'inventario associato alla U.O. di scrivania
        String cdCds = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(aUC);
        String cdUo = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_unita_organizzativa(aUC);

        return findInventarioFor(aUC, cdCds, cdUo, resp);
    }

    public Id_inventarioBulk findInventarioFor(it.cnr.jada.UserContext userContext, String cdCds, String cdUO, boolean resp) throws PersistencyException, IntrospectionException {

        Ass_inventario_uoHome assInvUoHome = (Ass_inventario_uoHome) getHomeCache().getHome(Ass_inventario_uoBulk.class);
        Ass_inventario_uoBulk assInvUo = assInvUoHome.findAssInvUoFor(userContext, cdCds, cdUO, resp);
        if (assInvUo == null)
            return null;
        return assInvUo.getInventario();
    }

    public Id_inventarioBulk findInventarioRespFor(it.cnr.jada.UserContext aUC) throws PersistencyException, IntrospectionException {

        return findInventarioFor(aUC, true);
    }

    public it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk findUoRespFor(it.cnr.jada.UserContext userContext, Id_inventarioBulk inv) throws PersistencyException, IntrospectionException {

        Ass_inventario_uoHome assInvUoHome = (Ass_inventario_uoHome) getHomeCache().getHome(Ass_inventario_uoBulk.class);
        Ass_inventario_uoBulk assInvUo = assInvUoHome.findAssInvUoRespFor(userContext, inv);
        if (assInvUo == null)
            return null;
        return assInvUo.getUnita_organizzativa();
    }

    /**
     * Insert the method's description here.
     * Creation date: (11/12/2001 15.15.15)
     *
     * @return boolean
     * @throws ApplicationException
     */
    public boolean isAperto(Id_inventarioBulk inv, Integer esercizio) throws PersistencyException, IntrospectionException, ApplicationException {

        Inventario_ap_chHome invApChHome = (Inventario_ap_chHome) getHomeCache().getHome(Inventario_ap_chBulk.class);
        return invApChHome.isAperto(inv, esercizio);
    }

    /**
     * Insert the method's description here.
     * Creation date: (11/12/2001 15.15.15)
     *
     * @return boolean
     * @throws ApplicationException
     */
    public boolean isChiuso(Id_inventarioBulk inv, Integer esercizio) throws PersistencyException, IntrospectionException, ApplicationException {

        Inventario_ap_chHome invApChHome = (Inventario_ap_chHome) getHomeCache().getHome(Inventario_ap_chBulk.class);
        return invApChHome.isChiuso(inv, esercizio);
    }
}
