package it.cnr.contab.pdg00.bulk;

/**
 * Insert the type's description here.
 * Creation date: (06/11/2003 15.53.38)
 * @author: Gennaro Borriello
 */
public class Stampa_sintetica_entrate_ldaBulk extends Stampa_spese_ldaBulk {
/**
 * Stampa_sintetica_entrate_ldaBulk constructor comment.
 */
public Stampa_sintetica_entrate_ldaBulk() {
	super();
}
/**
 * Stampa_sintetica_entrate_ldaBulk constructor comment.
 * @param cd_centro_responsabilita java.lang.String
 * @param cd_elemento_voce java.lang.String
 * @param cd_linea_attivita java.lang.String
 * @param esercizio java.lang.Integer
 * @param pg_spesa java.lang.Long
 * @param ti_appartenenza java.lang.String
 * @param ti_gestione java.lang.String
 */
public Stampa_sintetica_entrate_ldaBulk(String cd_centro_responsabilita, String cd_elemento_voce, String cd_linea_attivita, Integer esercizio, Long pg_spesa, String ti_appartenenza, String ti_gestione) {
	super(cd_centro_responsabilita, cd_elemento_voce, cd_linea_attivita, esercizio, pg_spesa, ti_appartenenza, ti_gestione);
}
}
