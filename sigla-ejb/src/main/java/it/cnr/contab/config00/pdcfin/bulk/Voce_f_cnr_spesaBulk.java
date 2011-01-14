package it.cnr.contab.config00.pdcfin.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Voce_f_cnr_spesaBulk extends Voce_fBulk {

public Voce_f_cnr_spesaBulk() {
	super();
	setTi_appartenenza(Elemento_voceHome.APPARTENENZA_CNR);
	setTi_gestione(Elemento_voceHome.GESTIONE_SPESE);
}
/**
 * Costruttore del capitolo di tipo CNR
 *
 * @param esercizio esercizio	
 * @param cd_voce codice capitolo
 */
public Voce_f_cnr_spesaBulk(java.lang.Integer esercizio, java.lang.String cd_voce) {
 super(cd_voce, esercizio, Elemento_voceHome.APPARTENENZA_CNR,Elemento_voceHome.GESTIONE_SPESE);
}
public Voce_f_cnr_spesaBulk(java.lang.String cd_voce,java.lang.Integer esercizio,java.lang.String ti_appartenenza,java.lang.String ti_gestione) {
	super(cd_voce,esercizio,ti_appartenenza,ti_gestione);
}
}
