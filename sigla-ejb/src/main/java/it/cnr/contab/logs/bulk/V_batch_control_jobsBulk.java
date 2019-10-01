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

import it.cnr.jada.persistency.Persistent;

import java.math.BigDecimal;
import java.sql.Timestamp;

// Referenced classes of package it.cnr.contab.logs.bulk:
//            Batch_controlBulk

public class V_batch_control_jobsBulk extends Batch_controlBulk
    implements Persistent
{

    public V_batch_control_jobsBulk()
    {
    }

    public V_batch_control_jobsBulk(BigDecimal bigdecimal)
    {
        super(bigdecimal);
    }

    public Boolean getBroken()
    {
        return broken;
    }

    public Long getFailures()
    {
        return failures;
    }

    public Timestamp getLast_date()
    {
        return last_date;
    }

    public Timestamp getNext_date()
    {
        return next_date;
    }

    public void setBroken(Boolean boolean1)
    {
        broken = boolean1;
    }

    public void setFailures(Long long1)
    {
        failures = long1;
    }

    public void setLast_date(Timestamp timestamp)
    {
        last_date = timestamp;
    }

    public void setNext_date(Timestamp timestamp)
    {
        next_date = timestamp;
    }

    private Boolean broken;
    private Long failures;
    private Timestamp last_date;
    private Timestamp next_date;
}