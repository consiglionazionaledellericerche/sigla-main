package it.cnr.contab.logs.bulk;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;

import java.math.BigDecimal;

public class Batch_log_tstaKey extends OggettoBulk
    implements KeyedPersistent
{

    public Batch_log_tstaKey()
    {
    }

    public Batch_log_tstaKey(BigDecimal bigdecimal)
    {
        pg_esecuzione = bigdecimal;
    }

    public boolean equalsByPrimaryKey(Object obj)
    {
        if(this == obj)
            return true;
        if(!(obj instanceof Batch_log_tstaKey))
            return false;
        Batch_log_tstaKey batch_log_tstakey = (Batch_log_tstaKey)obj;
        return compareKey(getPg_esecuzione(), batch_log_tstakey.getPg_esecuzione());
    }

    public BigDecimal getPg_esecuzione()
    {
        return pg_esecuzione;
    }

    public int primaryKeyHashCode()
    {
        return calculateKeyHashCode(getPg_esecuzione());
    }

    public void setPg_esecuzione(BigDecimal bigdecimal)
    {
        pg_esecuzione = bigdecimal;
    }

    private BigDecimal pg_esecuzione;
}