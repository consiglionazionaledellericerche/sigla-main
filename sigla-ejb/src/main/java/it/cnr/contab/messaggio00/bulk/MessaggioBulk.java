package it.cnr.contab.messaggio00.bulk;

import java.sql.Timestamp;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.action.CRUDBP;

public class MessaggioBulk extends MessaggioBase {

	public static final String ICONA_MESSAGGIO_VISIONATO = "<img align=middle class=Button src=img/book_opened.gif>";
	public static final String ICONA_MESSAGGIO_NON_VISIONATO = "<img align=middle class=Button src=img/book_closed.gif>";
	private Boolean visionato = Boolean.FALSE;
	private Boolean letto = Boolean.FALSE;
	private String iconaOpenClose;

	public MessaggioBulk() {
		super();
	}
	public MessaggioBulk(java.lang.Long pg_messaggio) {
		super(pg_messaggio);
	}
	public String getIconaOpenClose() {
		if (getVisionato().booleanValue()) 
			return ICONA_MESSAGGIO_VISIONATO;
		else
			return ICONA_MESSAGGIO_NON_VISIONATO;
	}
	public Timestamp getData_creazione()
	{
		return getDacr();
	}
	public Boolean getVisionato() {
		return visionato;
	}
	public void setVisionato(Boolean boolean1) {
		visionato = boolean1;
	}
	public Boolean getLetto() {
		return letto;
	}
	public void setLetto(Boolean boolean1) {
		letto = boolean1;
	}
}
