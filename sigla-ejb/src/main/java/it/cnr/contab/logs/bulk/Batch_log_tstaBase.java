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
//            Batch_log_tstaKey

public class Batch_log_tstaBase extends Batch_log_tstaKey
    implements Keyed
{

    public Batch_log_tstaBase()
    {
    }

    public Batch_log_tstaBase(BigDecimal bigdecimal)
    {
        super(bigdecimal);
    }

    public String getDs_log()
    {
        return ds_log;
    }

    public Boolean getFl_errori()
    {
        return fl_errori;
    }

    public String getNote()
    {
        return note;
    }

    public BigDecimal getPg_batch()
    {
        return pg_batch;
    }

    public BigDecimal getPg_job()
    {
        return pg_job;
    }

    public void setDs_log(String s)
    {
        ds_log = s;
    }

    public void setFl_errori(Boolean boolean1)
    {
        fl_errori = boolean1;
    }

    public void setNote(String s)
    {
        note = s;
    }

    public void setPg_batch(BigDecimal bigdecimal)
    {
        pg_batch = bigdecimal;
    }

    public void setPg_job(BigDecimal bigdecimal)
    {
        pg_job = bigdecimal;
    }

    public String getCd_log_tipo() {
        return cd_log_tipo;
    }

    public void setCd_log_tipo(String cd_log_tipo) {
        this.cd_log_tipo = cd_log_tipo;
    }

    private String cd_log_tipo;
    private String ds_log;
    private Boolean fl_errori;
    private String note;
    private BigDecimal pg_batch;
    private BigDecimal pg_job;
}