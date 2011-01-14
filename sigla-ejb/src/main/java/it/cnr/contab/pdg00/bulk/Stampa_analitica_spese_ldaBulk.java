package it.cnr.contab.pdg00.bulk;

/**
 * Insert the type's description here.
 * Creation date: (23/01/2003 16.02.55)
 * @author: Roberto Fantino
 */
public class Stampa_analitica_spese_ldaBulk extends Stampa_spese_ldaBulk {

/**
 * Stampa_obbligazioniBulk constructor comment.
 */
public Stampa_analitica_spese_ldaBulk() {
	super();
}
/**
 * Stampa_analitica_spese_ldaBulk constructor comment.
 * 
 * @param cd_centro_responsabilita
 * @param cd_elemento_voce
 * @param cd_linea_attivita
 * @param esercizio
 * @param pg_spesa
 * @param ti_appartenenza
 * @param ti_gestione
 */
public Stampa_analitica_spese_ldaBulk(java.lang.String cd_centro_responsabilita,java.lang.String cd_elemento_voce,java.lang.String cd_linea_attivita,java.lang.Integer esercizio,java.lang.Long pg_spesa,java.lang.String ti_appartenenza,java.lang.String ti_gestione) {
	super(cd_centro_responsabilita,cd_elemento_voce,cd_linea_attivita,esercizio,pg_spesa,ti_appartenenza,ti_gestione);
}
}
