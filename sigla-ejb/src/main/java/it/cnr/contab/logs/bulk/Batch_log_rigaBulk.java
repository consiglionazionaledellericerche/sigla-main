package it.cnr.contab.logs.bulk;

import java.math.BigDecimal;

// Referenced classes of package it.cnr.contab.logs.bulk:
//            Batch_log_rigaBase

public class Batch_log_rigaBulk extends Batch_log_rigaBase
{

    public Batch_log_rigaBulk()
    {
    }

    public Batch_log_rigaBulk(BigDecimal bigdecimal, BigDecimal bigdecimal1)
    {
        super(bigdecimal, bigdecimal1);
    }
    public String getCssClassTrace(){
    	if(getTi_messaggio().compareTo("E")==0)
    		return "TableColumnRedBold";
    	else
    		return null; 
    }
    public String getCssClassTi_messaggio(){
    	if(getTi_messaggio().compareTo("E")==0)
     		return "TableColumnRedBold";
    	else
    		return null; 
    }
}
