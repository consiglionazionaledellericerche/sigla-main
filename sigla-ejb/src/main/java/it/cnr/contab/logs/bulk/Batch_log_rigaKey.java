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

public class Batch_log_rigaKey extends OggettoBulk
    implements KeyedPersistent
{

    public Batch_log_rigaKey()
    {
    }

    public Batch_log_rigaKey(BigDecimal bigdecimal, BigDecimal bigdecimal1)
    {
        pg_esecuzione = bigdecimal;
        pg_riga = bigdecimal1;
    }

    public boolean equalsByPrimaryKey(Object obj)
    {
        if(this == obj)
            return true;
        if(!(obj instanceof Batch_log_rigaKey))
            return false;
        Batch_log_rigaKey batch_log_rigakey = (Batch_log_rigaKey)obj;
        if(!compareKey(getPg_esecuzione(), batch_log_rigakey.getPg_esecuzione()))
            return false;
        return compareKey(getPg_riga(), batch_log_rigakey.getPg_riga());
    }

    public BigDecimal getPg_esecuzione()
    {
        return pg_esecuzione;
    }

    public BigDecimal getPg_riga()
    {
        return pg_riga;
    }

    public int primaryKeyHashCode()
    {
        return calculateKeyHashCode(getPg_esecuzione()) + calculateKeyHashCode(getPg_riga());
    }

    public void setPg_esecuzione(BigDecimal bigdecimal)
    {
        pg_esecuzione = bigdecimal;
    }

    public void setPg_riga(BigDecimal bigdecimal)
    {
        pg_riga = bigdecimal;
    }

    private BigDecimal pg_esecuzione;
    private BigDecimal pg_riga;
}