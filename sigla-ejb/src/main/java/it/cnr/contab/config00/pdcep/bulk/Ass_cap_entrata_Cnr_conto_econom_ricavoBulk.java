package it.cnr.contab.config00.pdcep.bulk;

import it.cnr.contab.config00.pdcfin.bulk.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.*;

/**
 * Questa classe eredita le caratteristiche della classe <code>Ass_ev_voceepBulk</code>,
 * che contiene le variabili e i metodi comuni a tutte le sue sottoclassi.
 * In particolare, si tratta di un'associazione tra capitolo di entrata del Cnr e conto
 * economico patrimoniale di ricavo.
 */
public class Ass_cap_entrata_Cnr_conto_econom_ricavoBulk extends Ass_ev_voceepBulk {

public Ass_cap_entrata_Cnr_conto_econom_ricavoBulk() {
	super();

	setTi_appartenenza(Elemento_voceHome.APPARTENENZA_CNR);
	setTi_gestione(Elemento_voceHome.GESTIONE_ENTRATE);
	// elemento_voce.setTi_appartenenza( Elemento_voceHome.APPARTENENZA_CNR );
	// elemento_voce.setTi_gestione( Elemento_voceHome.GESTIONE_ENTRATE );
}
public Ass_cap_entrata_Cnr_conto_econom_ricavoBulk(java.lang.String cd_elemento_voce,java.lang.String cd_voce_ep,java.lang.Integer esercizio,java.lang.String ti_appartenenza,java.lang.String ti_gestione) {
	super(cd_elemento_voce,cd_voce_ep,esercizio,ti_appartenenza,ti_gestione);
}
/**
 * Metodo con cui si verifica la validità di alcuni campi, mediante un 
 * controllo sintattico o contestuale.
 */
public void validate() throws ValidationException 
{
	super.validate();
	
	if ( elemento_voce == null || elemento_voce.getCd_elemento_voce() == null ||  elemento_voce.getCd_elemento_voce().equals("") )
		throw new ValidationException( "Il codice del CAPITOLO ENTRATA CNR è obbligatorio." );
	if ( voce_ep == null || voce_ep.getCd_voce_ep() == null || voce_ep.getCd_voce_ep().equals(""))
		throw new ValidationException( "Il codice del CONTO EP RICAVO è obbligatorio." );		
}
}
