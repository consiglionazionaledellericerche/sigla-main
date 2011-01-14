package it.cnr.contab.logs.bulk;

import it.cnr.jada.persistency.Keyed;

import java.math.BigDecimal;

// Referenced classes of package it.cnr.contab.logs.bulk:
//            Batch_log_rigaKey

public class Batch_log_rigaBase extends Batch_log_rigaKey
    implements Keyed
{

    public Batch_log_rigaBase()
    {
    }

    public Batch_log_rigaBase(BigDecimal bigdecimal, BigDecimal bigdecimal1)
    {
        super(bigdecimal, bigdecimal1);
    }

    public String getMessaggio()
    {
        return messaggio;
    }

    public String getNote()
    {
        return note;
    }

    public String getTi_messaggio()
    {
        return ti_messaggio;
    }

    public String getTrace()
    {
        return trace;
    }

    public void setMessaggio(String s)
    {
        messaggio = s;
    }

    public void setNote(String s)
    {
        note = s;
    }

    public void setTi_messaggio(String s)
    {
        ti_messaggio = s;
    }

    public void setTrace(String s)
    {
        trace = s;
    }

    private String messaggio;
    private String note;
    private String ti_messaggio;
    private String trace;
}