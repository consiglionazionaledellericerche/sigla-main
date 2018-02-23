package it.cnr.contab.compensi00.docs.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Vsx_liquidazione_rateBulk extends Vsx_liquidazione_rateBase {

public Vsx_liquidazione_rateBulk() {
	super();
}
/**
  * Imposta le proprietà del Vsx_liquidazione_rate.
  *	E' stata generata la richiesta di effettuare una liquidazione: l'utente ha selezionato
  *	le rate da liquidare, le quali vengono utilizzati per popolare le tabelle dalle
  *	quali la procedura di liquidazione andrà a prelevare i dati.
  *	Il metodo setta le varie proprietà del Vsx_liquidazione_rate, sulla base della rata selezionata.
  * Il bulk così creato verrà poi inserito in tabella.
  *	
  * @param rata La <code>Liquidazione_rate_minicarrieraBulk</code> rata da liquidare.
**/  
public void completeFrom(Liquidazione_rate_minicarrieraBulk rata) {

	if (rata==null) return;

	setCd_cds(rata.getCd_cds());
	setCd_unita_organizzativa(rata.getCd_unita_organizzativa());
	setEsercizio(rata.getEsercizio());
	setPg_minicarriera(rata.getPg_minicarriera());
	setPg_rata(rata.getPg_rata());

	setTi_appartenenza(rata.getElementoVoce().getTi_appartenenza());
	setTi_gestione(rata.getElementoVoce().getTi_gestione());
	setCd_elemento_voce(rata.getElementoVoce().getCd_elemento_voce());
	
	setCd_voce(rata.getVoceF().getCd_voce());
	
	setCd_centro_responsabilita(rata.getLineaAttivita().getCd_centro_responsabilita());
	setCd_linea_attivita(rata.getLineaAttivita().getCd_linea_attivita());
	
	setProc_name("CNRCTB610.liquidazMassivaMinicarriere");
	setUser(rata.getUser());
}
}
