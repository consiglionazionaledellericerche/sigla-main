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

package it.cnr.contab.logs.bulk;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;

import java.math.BigDecimal;

public class Batch_controlKey extends OggettoBulk
    implements KeyedPersistent
{

    public Batch_controlKey()
    {
    }

    public Batch_controlKey(BigDecimal bigdecimal)
    {
        pg_batch = bigdecimal;
    }

    public boolean equalsByPrimaryKey(Object obj)
    {
        if(this == obj)
            return true;
        if(!(obj instanceof Batch_controlKey))
            return false;
        Batch_controlKey batch_controlkey = (Batch_controlKey)obj;
        return compareKey(getPg_batch(), batch_controlkey.getPg_batch());
    }

    public BigDecimal getPg_batch()
    {
        return pg_batch;
    }

    public int primaryKeyHashCode()
    {
        return calculateKeyHashCode(getPg_batch());
    }

    public void setPg_batch(BigDecimal bigdecimal)
    {
        pg_batch = bigdecimal;
    }

    private BigDecimal pg_batch;
}