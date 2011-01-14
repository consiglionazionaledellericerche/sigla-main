package it.cnr.contab.logs.bulk;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;

public class Batch_proceduraKey extends OggettoBulk
    implements KeyedPersistent
{

    public Batch_proceduraKey()
    {
    }

    public Batch_proceduraKey(String s)
    {
        cd_procedura = s;
    }

    public boolean equalsByPrimaryKey(Object obj)
    {
        if(this == obj)
            return true;
        if(!(obj instanceof Batch_proceduraKey))
            return false;
        Batch_proceduraKey batch_procedurakey = (Batch_proceduraKey)obj;
        return compareKey(getCd_procedura(), batch_procedurakey.getCd_procedura());
    }

    public String getCd_procedura()
    {
        return cd_procedura;
    }

    public int primaryKeyHashCode()
    {
        return calculateKeyHashCode(getCd_procedura());
    }

    public void setCd_procedura(String s)
    {
        cd_procedura = s;
    }

    private String cd_procedura;
}