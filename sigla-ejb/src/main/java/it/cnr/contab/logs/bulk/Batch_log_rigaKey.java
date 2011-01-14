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