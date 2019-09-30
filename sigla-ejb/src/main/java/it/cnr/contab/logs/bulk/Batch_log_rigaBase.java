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

// Referenced classes of package it.cnr.contab.logs.bulk:
//            Batch_log_rigaKey

public class Batch_log_rigaBase extends Batch_log_rigaKey
    implements Keyed
{

    public Batch_log_rigaBase()
    {
    }

    public Batch_log_rigaBase(BigDecimal bigdecimal, BigDecimal bigdecimal1)
    {
        super(bigdecimal, bigdecimal1);
    }

    public String getMessaggio()
    {
        return messaggio;
    }

    public String getNote()
    {
        return note;
    }

    public String getTi_messaggio()
    {
        return ti_messaggio;
    }

    public String getTrace()
    {
        return trace;
    }

    public void setMessaggio(String s)
    {
        messaggio = s;
    }

    public void setNote(String s)
    {
        note = s;
    }

    public void setTi_messaggio(String s)
    {
        ti_messaggio = s;
    }

    public void setTrace(String s)
    {
        trace = s;
    }

    private String messaggio;
    private String note;
    private String ti_messaggio;
    private String trace;
}