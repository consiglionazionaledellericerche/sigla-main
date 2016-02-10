/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 20/01/2007
 */
package it.cnr.contab.prevent01.bulk;

import it.cnr.jada.persistency.Keyed;

public class Ass_pdg_missione_tipo_uoBase extends Ass_pdg_missione_tipo_uoKey implements Keyed {
	private static final long serialVersionUID = 1L;

	public Ass_pdg_missione_tipo_uoBase() {
		super();
	}
	public Ass_pdg_missione_tipo_uoBase(java.lang.String cd_missione, java.lang.String cd_tipo_unita) {
		super(cd_missione, cd_tipo_unita);
	}
}