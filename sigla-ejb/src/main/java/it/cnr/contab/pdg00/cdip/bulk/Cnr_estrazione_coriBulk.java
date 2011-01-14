/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 17/03/2009
 */
package it.cnr.contab.pdg00.cdip.bulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public class Cnr_estrazione_coriBulk extends Cnr_estrazione_coriBase {
	public Cnr_estrazione_coriBulk() {
		super();
	}
	public Cnr_estrazione_coriBulk(java.lang.String cd_cds, java.lang.Integer esercizio, java.lang.String cd_unita_organizzativa, java.lang.Integer pg_liquidazione, java.lang.Integer matricola, java.lang.String codice_fiscale, java.lang.String ti_pagamento, java.lang.Integer esercizio_compenso, java.lang.String cd_imponibile, java.lang.String ti_ente_percipiente, java.lang.String cd_contributo_ritenuta) {
		super(cd_cds, esercizio, cd_unita_organizzativa, pg_liquidazione, matricola, codice_fiscale, ti_pagamento, esercizio_compenso, cd_imponibile, ti_ente_percipiente, cd_contributo_ritenuta);
	}
}