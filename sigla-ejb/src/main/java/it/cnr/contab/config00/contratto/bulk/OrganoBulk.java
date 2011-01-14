/*
* Creted by Generator 1.0
* Date 07/04/2005
*/
package it.cnr.contab.config00.contratto.bulk;
import it.cnr.contab.util.ICancellatoLogicamente;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public class OrganoBulk extends OrganoBase implements ICancellatoLogicamente{
	
	public OrganoBulk() {
		super();
	}
	public OrganoBulk(java.lang.String cd_tipo_organo) {
		super(cd_tipo_organo);
	}
    /**
     * Inizializza per l'inserimento i flag
     */
	public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
		setFl_cancellato(new Boolean(false));
		setFl_non_definito(new Boolean(false));
		return super.initializeForInsert(bp,context);
	}	

	/* (non-Javadoc)
	 * @see it.cnr.contab.util.ICancellatoLogicamente#isCancellatoLogicamente()
	 */
	public boolean isCancellatoLogicamente() {		
		return getFl_cancellato().booleanValue();
	}
	/* (non-Javadoc)
	 * @see it.cnr.contab.util.ICancellatoLogicamente#cancellaLogicamente()
	 */
	public void cancellaLogicamente() {
		setFl_cancellato(new Boolean(true));		
	}
}