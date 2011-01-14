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

    private String ds_log;
    private Boolean fl_errori;
    private String note;
    private BigDecimal pg_batch;
    private BigDecimal pg_job;
}