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

public class Batch_log_tstaKey extends OggettoBulk
    implements KeyedPersistent
{

    public Batch_log_tstaKey()
    {
    }

    public Batch_log_tstaKey(BigDecimal bigdecimal)
    {
        pg_esecuzione = bigdecimal;
    }

    public boolean equalsByPrimaryKey(Object obj)
    {
        if(this == obj)
            return true;
        if(!(obj instanceof Batch_log_tstaKey))
            return false;
        Batch_log_tstaKey batch_log_tstakey = (Batch_log_tstaKey)obj;
        return compareKey(getPg_esecuzione(), batch_log_tstakey.getPg_esecuzione());
    }

    public BigDecimal getPg_esecuzione()
    {
        return pg_esecuzione;
    }

    public int primaryKeyHashCode()
    {
        return calculateKeyHashCode(getPg_esecuzione());
    }

    public void setPg_esecuzione(BigDecimal bigdecimal)
    {
        pg_esecuzione = bigdecimal;
    }

    private BigDecimal pg_esecuzione;
}