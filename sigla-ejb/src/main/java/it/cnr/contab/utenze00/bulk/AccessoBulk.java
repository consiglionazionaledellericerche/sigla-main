package it.cnr.contab.utenze00.bulk;

/**
 * Definisce "cosa" un utente può fare all'interno di tutte le funzionalità della Gestione Contabile
 *	
 */

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class AccessoBulk extends AccessoBase {
	public static final String TIPO_RISERVATO_CNR 	= "C";
	public static final String TIPO_PUBBLICO 		= "D";
	public static final String TIPO_SUPERUTENTE 	= "S";
	public static final String TIPO_AMMIN_UTENZE 	= "A";	
//	public static final String CD_MOD_AMMIN_UTENZE = "CFGUTENZECOREUADMINM";
//	public static final String CD_VIS_AMMIN_UTENZE = "CFGUTENZECOREUADMINV";
//	public static final String CD_MOD_UT_COMUNE = "CFGUTENZECOREUCOMUNV";
//	public static final String CD_VIS_UT_COMUNE = "CFGUTENZECOREUCOMUNM";
//	public static final String CD_MOD_ACC_RUO 	= "CFGUTENZECOREACCRUOV";
//	public static final String CD_VIS_ACC_RUO 	= "CFGUTENZECOREACCRUOM";	
public AccessoBulk() {
	super();
}
public AccessoBulk(java.lang.String cd_accesso) {
	super(cd_accesso);
	this.setCd_accesso(cd_accesso);
}
}
