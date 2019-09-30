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

package it.cnr.contab.logs.comp;

import it.cnr.contab.logs.bulk.Batch_controlBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;
import it.cnr.jada.util.RemoteIterator;

public interface IBatchControlMgr
    extends ICRUDMgr
{

    public abstract Batch_controlBulk attivaBatch(UserContext usercontext, Batch_controlBulk batch_controlbulk)
        throws ComponentException;

    public abstract OggettoBulk creaConBulk(UserContext usercontext, OggettoBulk oggettobulk)
        throws ComponentException;

    public abstract Batch_controlBulk disattivaBatch(UserContext usercontext, Batch_controlBulk batch_controlbulk)
        throws ComponentException;

    public abstract void eliminaConBulk(UserContext usercontext, OggettoBulk oggettobulk)
        throws ComponentException;

    public abstract Batch_controlBulk inizializzaParametri(UserContext usercontext, Batch_controlBulk batch_controlbulk)
        throws ComponentException;

    public abstract RemoteIterator listaBatch_control_jobs(UserContext usercontext)
        throws ComponentException;
}