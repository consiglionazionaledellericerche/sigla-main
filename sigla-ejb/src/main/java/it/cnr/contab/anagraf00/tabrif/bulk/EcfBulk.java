/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 13/09/2007
 */
package it.cnr.contab.anagraf00.tabrif.bulk;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public class EcfBulk extends EcfBase {
	public EcfBulk() {
		super();
	}
	public EcfBulk(java.lang.Long prog) {
		super(prog);
	}
	public static Calendar getDateCalendar(java.sql.Timestamp date) {

		if (date == null)
			try {
				date = it.cnr.jada.util.ejb.EJBCommonServices.getServerTimestamp();
			} catch (javax.ejb.EJBException e) {
				throw new it.cnr.jada.DetailedRuntimeException(e);
			}
			
		java.util.Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(new Date(date.getTime()));
		calendar.set(java.util.Calendar.HOUR, 0);
		calendar.set(java.util.Calendar.MINUTE, 0);
		calendar.set(java.util.Calendar.SECOND, 0);
		calendar.set(java.util.Calendar.MILLISECOND, 0);
		calendar.set(java.util.Calendar.AM_PM, java.util.Calendar.AM);

		return calendar;
	}
}