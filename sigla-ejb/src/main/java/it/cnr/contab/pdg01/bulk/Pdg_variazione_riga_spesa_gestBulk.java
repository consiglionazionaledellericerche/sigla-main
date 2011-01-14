/*
* Created by Generator 1.0
* Date 21/04/2006
*/
package it.cnr.contab.pdg01.bulk;
import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.pdcfin.bulk.NaturaBulk;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;

public class Pdg_variazione_riga_spesa_gestBulk extends Pdg_variazione_riga_gestBulk {
	public Pdg_variazione_riga_spesa_gestBulk() {
		super();
	}
	public Pdg_variazione_riga_spesa_gestBulk(java.lang.Integer esercizio, java.lang.Long pg_variazione_pdg, java.lang.Integer pg_riga) {
		super(esercizio, pg_variazione_pdg, pg_riga);
	}
	public Pdg_variazione_riga_spesa_gestBulk(java.lang.Integer esercizio, java.lang.Long pg_variazione_pdg, java.lang.Integer pg_riga, java.lang.String cd_cdr_assegnatario, java.lang.String cd_linea_attivita, java.lang.String cd_cds_area, java.lang.String ti_appartenenza, java.lang.String ti_gestione, java.lang.String cd_elemento_voce) {
		super(esercizio, pg_variazione_pdg, pg_riga, cd_cdr_assegnatario, cd_linea_attivita, cd_cds_area, ti_appartenenza, ti_gestione, cd_elemento_voce);
	}
}