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
//            Batch_controlKey

public class Batch_controlBase extends Batch_controlKey
    implements Keyed
{

    public Batch_controlBase()
    {
    }

    public Batch_controlBase(BigDecimal bigdecimal)
    {
        super(bigdecimal);
    }

    public String getDs_batch()
    {
        return ds_batch;
    }

    public Timestamp getDt_partenza()
    {
        return dt_partenza;
    }

    public Boolean getFl_attivato()
    {
        return fl_attivato;
    }

    public Boolean getFl_exec_conc()
    {
        return fl_exec_conc;
    }

    public Long getIntervallo()
    {
        return intervallo;
    }

    public String getMessage()
    {
        return message;
    }

    public BigDecimal getPg_job()
    {
        return pg_job;
    }

    public String getUtente()
    {
        return utente;
    }

    public void setDs_batch(String s)
    {
        ds_batch = s;
    }

    public void setDt_partenza(Timestamp timestamp)
    {
        dt_partenza = timestamp;
    }

    public void setFl_attivato(Boolean boolean1)
    {
        fl_attivato = boolean1;
    }

    public void setFl_exec_conc(Boolean boolean1)
    {
        fl_exec_conc = boolean1;
    }

    public void setIntervallo(Long long1)
    {
        intervallo = long1;
    }

    public void setMessage(String s)
    {
        message = s;
    }

    public void setPg_job(BigDecimal bigdecimal)
    {
        pg_job = bigdecimal;
    }

    public void setUtente(String s)
    {
        utente = s;
    }

    private String ds_batch;
    private Timestamp dt_partenza;
    private Boolean fl_attivato;
    private Boolean fl_exec_conc;
    private Long intervallo;
    private String message;
    private BigDecimal pg_job;
    private String utente;
}