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

public class Batch_proceduraKey extends OggettoBulk
    implements KeyedPersistent
{

    public Batch_proceduraKey()
    {
    }

    public Batch_proceduraKey(String s)
    {
        cd_procedura = s;
    }

    public boolean equalsByPrimaryKey(Object obj)
    {
        if(this == obj)
            return true;
        if(!(obj instanceof Batch_proceduraKey))
            return false;
        Batch_proceduraKey batch_procedurakey = (Batch_proceduraKey)obj;
        return compareKey(getCd_procedura(), batch_procedurakey.getCd_procedura());
    }

    public String getCd_procedura()
    {
        return cd_procedura;
    }

    public int primaryKeyHashCode()
    {
        return calculateKeyHashCode(getCd_procedura());
    }

    public void setCd_procedura(String s)
    {
        cd_procedura = s;
    }

    private String cd_procedura;
}