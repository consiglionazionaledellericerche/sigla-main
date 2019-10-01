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

import it.cnr.jada.persistency.Keyed;

import java.math.BigDecimal;
import java.sql.Timestamp;

// Referenced classes of package it.cnr.contab.logs.bulk:
//            Batch_procedura_parametroKey

public class Batch_procedura_parametroBase extends Batch_procedura_parametroKey
    implements Keyed
{

    public Batch_procedura_parametroBase()
    {
    }

    public Batch_procedura_parametroBase(String s, String s1, Integer integer)
    {
        super(s, s1, integer);
    }

    public Timestamp getValore_date()
    {
        return valore_date;
    }

    public BigDecimal getValore_number()
    {
        return valore_number;
    }

    public String getValore_varchar()
    {
        return valore_varchar;
    }

    public void setValore_date(Timestamp timestamp)
    {
        valore_date = timestamp;
    }

    public void setValore_number(BigDecimal bigdecimal)
    {
        valore_number = bigdecimal;
    }

    public void setValore_varchar(String s)
    {
        valore_varchar = s;
    }

    private Timestamp valore_date;
    private BigDecimal valore_number;
    private String valore_varchar;
}