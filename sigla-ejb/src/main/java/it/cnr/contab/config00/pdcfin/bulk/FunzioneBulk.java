package it.cnr.contab.config00.pdcfin.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class FunzioneBulk extends FunzioneBase {
public FunzioneBulk() {
	super();
}
public FunzioneBulk(java.lang.String cd_funzione) {
	super(cd_funzione);
}
	public boolean equals(Object obj) {
	 if(obj instanceof FunzioneBulk) {
	  FunzioneBulk aFB = (FunzioneBulk)obj;
	  if(( getCd_funzione() == null ) && ( aFB.getCd_funzione() == null)) return true;
	  if(   ((getCd_funzione() != null) && (aFB.getCd_funzione() == null))
		 || ((getCd_funzione() == null) && (aFB.getCd_funzione() != null))
		) return false;
	  
	  return ((FunzioneBulk)obj).getCd_funzione().equals( getCd_funzione() );
	 }
	 return super.equals(obj);
	}
/**
 * Restituisce il valore della proprietà 'cd_ds_funzione'
 *
 * @return Il valore della proprietà 'cd_ds_funzione'
 */
public String getCd_ds_funzione() {
	return getCd_funzione() + " - " + getDs_funzione();
}
}
