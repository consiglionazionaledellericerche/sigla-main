package it.cnr.contab.doccont00.comp;

import it.cnr.contab.utenze00.bp.*;
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.jada.*;

import java.sql.*;

public class DateServices {
/**
 * DateServices constructor comment.
 */
public DateServices() {
	super();
}
/**
 * Recupera il la data corrente dal server db
 *
 * @return La data corrente (solo data) fornita dal db
 * @throws PersistencyException Se si verifica qualche eccezione SQL
 */
public static Timestamp getDataOdierna() throws javax.ejb.EJBException {

	
	return it.cnr.jada.util.ejb.EJBCommonServices.getServerDate();
	
}
public static java.sql.Timestamp getDt_valida( UserContext userContext) throws javax.ejb.EJBException
{
	Timestamp today = it.cnr.jada.util.ejb.EJBCommonServices.getServerDate();
	java.util.Calendar calendar = java.util.GregorianCalendar.getInstance();
	calendar.setTime( today );
	int annoOdierno  = calendar.get( calendar.YEAR) ;
	int esercizioScrivania = ((CNRUserContext)userContext).getEsercizio().intValue();	
	if ( annoOdierno > esercizioScrivania )
		return getLastDayOfYear( esercizioScrivania );
	else
		return today;	
}
public static java.sql.Timestamp getFirstDayOfYear(int year){

	java.util.Calendar calendar = java.util.GregorianCalendar.getInstance();
	calendar.set(java.util.Calendar.DAY_OF_MONTH, 1);
	calendar.set(java.util.Calendar.MONTH, 0);
	calendar.set(java.util.Calendar.YEAR, year);
	calendar.set(java.util.Calendar.HOUR, 0);
	calendar.set(java.util.Calendar.MINUTE, 0);
	calendar.set(java.util.Calendar.SECOND, 0);
	calendar.set(java.util.Calendar.MILLISECOND, 0);
	calendar.set(java.util.Calendar.AM_PM, java.util.Calendar.AM);
	return new java.sql.Timestamp(calendar.getTime().getTime());
}
public static java.sql.Timestamp getLastDayOfYear(int year){

	java.util.Calendar calendar = java.util.GregorianCalendar.getInstance();
	calendar.set(java.util.Calendar.DAY_OF_MONTH, 31);
	calendar.set(java.util.Calendar.MONTH, 11);
	calendar.set(java.util.Calendar.YEAR, year);
	calendar.set(java.util.Calendar.HOUR, 0);
	calendar.set(java.util.Calendar.MINUTE, 0);
	calendar.set(java.util.Calendar.SECOND, 0);
	calendar.set(java.util.Calendar.MILLISECOND, 0);
	calendar.set(java.util.Calendar.AM_PM, java.util.Calendar.AM);
	return new java.sql.Timestamp(calendar.getTime().getTime());
}
public static  java.sql.Timestamp getLastTsOfYear(int year) throws javax.ejb.EJBException
{
	java.util.Calendar cNow = java.util.GregorianCalendar.getInstance();	
	Timestamp now = it.cnr.jada.util.ejb.EJBCommonServices.getServerTimestamp();
	cNow.setTime( now );
	
	java.util.Calendar calendar = java.util.GregorianCalendar.getInstance();
	calendar.set(java.util.Calendar.DAY_OF_MONTH, 31);
	calendar.set(java.util.Calendar.MONTH, 11);
	calendar.set(java.util.Calendar.YEAR, year);
	calendar.set(java.util.Calendar.HOUR, cNow.get(cNow.HOUR));
	calendar.set(java.util.Calendar.MINUTE, cNow.get(cNow.MINUTE));
	calendar.set(java.util.Calendar.SECOND, cNow.get(cNow.SECOND));
	calendar.set(java.util.Calendar.MILLISECOND, cNow.get(cNow.MILLISECOND));
	calendar.set(java.util.Calendar.AM_PM, cNow.get(cNow.AM_PM));
	return new java.sql.Timestamp(calendar.getTime().getTime());   
}
public static  java.sql.Timestamp getMidDayTs(Timestamp aTS) throws javax.ejb.EJBException
{
	java.util.Calendar calendar = java.util.GregorianCalendar.getInstance();	
	calendar.setTime(aTS);
	
	calendar.set(java.util.Calendar.HOUR_OF_DAY, 12);
	calendar.set(java.util.Calendar.MINUTE, 0);
	calendar.set(java.util.Calendar.SECOND, 0);
	calendar.set(java.util.Calendar.MILLISECOND, 0);
	return new java.sql.Timestamp(calendar.getTime().getTime());   
}
/* Ritorna un timestamp incrementato d 1 minuto rispetto a quello passato */  
public static  java.sql.Timestamp getNextMinTs( UserContext userContext, Timestamp aTS) throws javax.ejb.EJBException
{
	java.util.Calendar calendar = java.util.GregorianCalendar.getInstance();
	calendar.setTime( aTS );
	calendar.add(java.util.Calendar.MINUTE,+1);
	return new Timestamp(calendar.getTime().getTime());
}
/* se la data odierna è maggiore dell'esercizio di scrivania ritorna il timestamp così fatto:
     31/12/esercizio di scrivania + ora,minuto, secondo del timestamp corrente
   altrimenti ritorna il timestamp corrente */  
public static  java.sql.Timestamp getTs_valido( UserContext userContext) throws javax.ejb.EJBException
{
	Timestamp today = it.cnr.jada.util.ejb.EJBCommonServices.getServerDate();
	java.util.Calendar calendar = java.util.GregorianCalendar.getInstance();
	calendar.setTime( today );
	int annoOdierno  = calendar.get( calendar.YEAR) ;
	int esercizioScrivania = ((CNRUserContext)userContext).getEsercizio().intValue();	
	if ( annoOdierno > esercizioScrivania )
		return getLastTsOfYear( esercizioScrivania );
	else
		return it.cnr.jada.util.ejb.EJBCommonServices.getServerTimestamp();	
}
/* se la data odierna è maggiore dell'esercizio di scrivania ritorna true altrimenti false */  
public static boolean isAnnoMaggEsScriv( UserContext userContext) throws javax.ejb.EJBException
{
	Timestamp today = it.cnr.jada.util.ejb.EJBCommonServices.getServerDate();

	java.util.Calendar calendar = java.util.GregorianCalendar.getInstance();
	
	calendar.setTime( today );

	int annoOdierno  = calendar.get( calendar.YEAR) ;
	int esercizioScrivania = ((CNRUserContext)userContext).getEsercizio().intValue();	
	if ( annoOdierno > esercizioScrivania )
		return true;
	else
		return false;	
}
}
