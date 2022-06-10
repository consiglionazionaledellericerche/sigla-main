package it.cnr.contab.pagopa.util;

import javax.xml.datatype.XMLGregorianCalendar;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public final class PagoPaUtil {
    public static SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
    public static String convertiData(Timestamp dataInput){
        Date data = new Date(dataInput.getTime());
        return FORMATTER.format(data);
    }
    public static String convertiData(XMLGregorianCalendar dataInput){
        java.util.Date data = dataInput.toGregorianCalendar().getTime();
        return FORMATTER.format(data);
    }
}
