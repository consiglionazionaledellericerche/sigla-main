package it.cnr.contab.logs.bulk;

import it.cnr.jada.persistency.Keyed;

import java.math.BigDecimal;
import java.sql.Timestamp;

// Referenced classes of package it.cnr.contab.logs.bulk:
//            Batch_procedura_parametroKey

public class Batch_procedura_parametroBase extends Batch_procedura_parametroKey
    implements Keyed
{

    public Batch_procedura_parametroBase()
    {
    }

    public Batch_procedura_parametroBase(String s, String s1, Integer integer)
    {
        super(s, s1, integer);
    }

    public Timestamp getValore_date()
    {
        return valore_date;
    }

    public BigDecimal getValore_number()
    {
        return valore_number;
    }

    public String getValore_varchar()
    {
        return valore_varchar;
    }

    public void setValore_date(Timestamp timestamp)
    {
        valore_date = timestamp;
    }

    public void setValore_number(BigDecimal bigdecimal)
    {
        valore_number = bigdecimal;
    }

    public void setValore_varchar(String s)
    {
        valore_varchar = s;
    }

    private Timestamp valore_date;
    private BigDecimal valore_number;
    private String valore_varchar;
}