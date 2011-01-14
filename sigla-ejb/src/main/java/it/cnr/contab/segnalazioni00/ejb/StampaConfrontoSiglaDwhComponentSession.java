package it.cnr.contab.segnalazioni00.ejb;

import it.cnr.contab.segnalazioni00.bulk.Stampa_attivita_siglaBulk;
import it.cnr.contab.segnalazioni00.bulk.Stampa_confronto_sigla_dwhBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;

import javax.ejb.Remote;

@Remote
public interface StampaConfrontoSiglaDwhComponentSession extends it.cnr.jada.ejb.CRUDComponentSession, it.cnr.jada.ejb.PrintComponentSession {
	OggettoBulk inizializzaDate(UserContext param0, Stampa_confronto_sigla_dwhBulk param1) throws ComponentException;
	OggettoBulk inizializzaEsercizio(UserContext param0, Stampa_attivita_siglaBulk param1) throws ComponentException;
}
