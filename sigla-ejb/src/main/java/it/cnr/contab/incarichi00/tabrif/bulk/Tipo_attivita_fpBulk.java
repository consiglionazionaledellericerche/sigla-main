/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 18/07/2007
 */
package it.cnr.contab.incarichi00.tabrif.bulk;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;

public class Tipo_attivita_fpBulk extends Tipo_attivita_fpBase {
	public Tipo_attivita_fpBulk() {
		super();
	}
	public Tipo_attivita_fpBulk(java.lang.String cd_tipo_attivita) {
		super(cd_tipo_attivita);
	}
	public OggettoBulk initializeForInsert(CRUDBP bp, ActionContext context) {
		super.initializeForInsert(bp, context);
		setFl_cancellato(Boolean.FALSE);
		return this;
	}

	private Tipo_attivita_fpBulk tipo_attivita_fp_padre;
	
	public Tipo_attivita_fpBulk getTipo_attivita_fp_padre() {
		return tipo_attivita_fp_padre;
	}
	public void setTipo_attivita_fp_padre(Tipo_attivita_fpBulk tipo_attivita_fp_padre) {
		this.tipo_attivita_fp_padre = tipo_attivita_fp_padre;
	}
	@Override
	public String getCd_tipo_attivita_padre() {
		if (this.getTipo_attivita_fp_padre() == null)
			return null;
		return this.getTipo_attivita_fp_padre().getCd_tipo_attivita();
	}
	public void setCd_tipo_attivita_padre(java.lang.String cd_tipo_attivita_padre) {
		if (this.getTipo_attivita_fp_padre() != null)
			this.getTipo_attivita_fp_padre().setCd_tipo_attivita_padre(cd_tipo_attivita_padre);
	}
}