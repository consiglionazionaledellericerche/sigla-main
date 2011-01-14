package it.cnr.contab.logs.bulk;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;

import java.math.BigDecimal;

public class Batch_controlKey extends OggettoBulk
    implements KeyedPersistent
{

    public Batch_controlKey()
    {
    }

    public Batch_controlKey(BigDecimal bigdecimal)
    {
        pg_batch = bigdecimal;
    }

    public boolean equalsByPrimaryKey(Object obj)
    {
        if(this == obj)
            return true;
        if(!(obj instanceof Batch_controlKey))
            return false;
        Batch_controlKey batch_controlkey = (Batch_controlKey)obj;
        return compareKey(getPg_batch(), batch_controlkey.getPg_batch());
    }

    public BigDecimal getPg_batch()
    {
        return pg_batch;
    }

    public int primaryKeyHashCode()
    {
        return calculateKeyHashCode(getPg_batch());
    }

    public void setPg_batch(BigDecimal bigdecimal)
    {
        pg_batch = bigdecimal;
    }

    private BigDecimal pg_batch;
}