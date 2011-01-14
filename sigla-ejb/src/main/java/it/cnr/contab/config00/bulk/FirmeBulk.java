package it.cnr.contab.config00.bulk;

import java.util.Dictionary;

import it.cnr.contab.utenze00.bulk.CNRUserInfo;
import it.cnr.jada.bulk.OggettoBulk;

/**
 * Creation date: (24/02/2005)
 * @author Tilde
 * @version 1.0
 */

public class FirmeBulk extends FirmeBase {

	public FirmeBulk() {
		super();
	}
	public FirmeBulk(java.lang.Integer esercizio, String tipo) {
		super(esercizio,tipo);
	}
	
	public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp, it.cnr.jada.action.ActionContext context){
		super.initializeForInsert(bp,context);
		setEsercizio(CNRUserInfo.getEsercizio(context));
	  return this;
	}

	public static final String DB_CERTIFICATI  ="CRT";
	
	public final static Dictionary tipoDBKeys;
	static {
	tipoDBKeys = new it.cnr.jada.util.OrderedHashtable();
	tipoDBKeys.put(DB_CERTIFICATI,"Stampe Certificazioni");
	};

}
