package it.cnr.contab.progettiric00.core.bulk;

import it.cnr.jada.persistency.Keyed;

public class Ass_progetto_piaeco_voceBase extends Ass_progetto_piaeco_voceKey implements Keyed {
	public Ass_progetto_piaeco_voceBase() {
		super();
	}
	
	public Ass_progetto_piaeco_voceBase(java.lang.Integer pg_progetto, java.lang.String cd_unita_organizzativa, java.lang.String cd_voce_piano, java.lang.Integer esercizio_piano,
			java.lang.Integer esercizio_voce, java.lang.String ti_appartenenza, java.lang.String ti_gestione, java.lang.String cd_elemento_voce) {
		super(pg_progetto, cd_unita_organizzativa, cd_voce_piano, esercizio_piano, esercizio_voce, ti_appartenenza, ti_gestione, cd_elemento_voce);
	}
}
