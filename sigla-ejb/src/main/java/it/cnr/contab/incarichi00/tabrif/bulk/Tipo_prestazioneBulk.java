/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 18/07/2007
 */
package it.cnr.contab.incarichi00.tabrif.bulk;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;

public class Tipo_prestazioneBulk extends Tipo_prestazioneBase {
	final public static String PREVISTA_DA_NORME_DI_LEGGE = "LEGGE";
	final public static String DI_NATURA_DISCREZIONALE = "DISCR";

	public Tipo_prestazioneBulk() {
		super();
	}
	public Tipo_prestazioneBulk(java.lang.String cd_tipo_prestazione) {
		super(cd_tipo_prestazione);
	}
	public OggettoBulk initializeForInsert(CRUDBP bp, ActionContext context) {
		super.initializeForInsert(bp, context);
		setFl_cancellato(Boolean.FALSE);
		return this;
	}
    public boolean isPrevistaDaNormeDiLegge() {
    	return this.getTipo_classificazione()!=null && 
			   this.getTipo_classificazione().equals(PREVISTA_DA_NORME_DI_LEGGE);
    }
    public boolean isDiNaturaDiscrezionale() {
    	return this.getTipo_classificazione()!=null && 
 			   this.getTipo_classificazione().equals(DI_NATURA_DISCREZIONALE);
    }
	
	
}