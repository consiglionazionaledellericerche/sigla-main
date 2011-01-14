package it.cnr.contab.logs.bulk;

import it.cnr.jada.persistency.Persistent;

import java.math.BigDecimal;
import java.sql.Timestamp;

// Referenced classes of package it.cnr.contab.logs.bulk:
//            Batch_controlBulk

public class V_batch_control_jobsBulk extends Batch_controlBulk
    implements Persistent
{

    public V_batch_control_jobsBulk()
    {
    }

    public V_batch_control_jobsBulk(BigDecimal bigdecimal)
    {
        super(bigdecimal);
    }

    public Boolean getBroken()
    {
        return broken;
    }

    public Long getFailures()
    {
        return failures;
    }

    public Timestamp getLast_date()
    {
        return last_date;
    }

    public Timestamp getNext_date()
    {
        return next_date;
    }

    public void setBroken(Boolean boolean1)
    {
        broken = boolean1;
    }

    public void setFailures(Long long1)
    {
        failures = long1;
    }

    public void setLast_date(Timestamp timestamp)
    {
        last_date = timestamp;
    }

    public void setNext_date(Timestamp timestamp)
    {
        next_date = timestamp;
    }

    private Boolean broken;
    private Long failures;
    private Timestamp last_date;
    private Timestamp next_date;
}