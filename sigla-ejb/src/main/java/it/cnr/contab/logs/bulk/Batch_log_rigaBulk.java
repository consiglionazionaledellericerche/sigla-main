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

import java.math.BigDecimal;

// Referenced classes of package it.cnr.contab.logs.bulk:
//            Batch_log_rigaBase

public class Batch_log_rigaBulk extends Batch_log_rigaBase
{

    public Batch_log_rigaBulk()
    {
    }

    public Batch_log_rigaBulk(BigDecimal bigdecimal, BigDecimal bigdecimal1)
    {
        super(bigdecimal, bigdecimal1);
    }
    public String getCssClassTrace(){
        if(getTi_messaggio().compareTo("E")==0)
            return "TableColumnRedBold";
        else
            return null;
    }
    public String getCssClassTi_messaggio(){
        if(getTi_messaggio().compareTo("E")==0)
            return "TableColumnRedBold";
        else
            return null;
    }
}