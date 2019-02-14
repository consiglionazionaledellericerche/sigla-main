package it.cnr.contab.sdi;

import javax.xml.datatype.XMLGregorianCalendar;

public interface IRicevutaConsegna {
    public String getIdentificativoSdI();
    public String getMessageId();
    public String getNomeFile();
    public XMLGregorianCalendar getDataOraConsegna();
}
