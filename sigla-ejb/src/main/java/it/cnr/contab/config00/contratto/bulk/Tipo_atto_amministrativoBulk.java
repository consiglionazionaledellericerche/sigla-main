/*
* Creted by Generator 1.0
* Date 07/04/2005
*/
package it.cnr.contab.config00.contratto.bulk;
import it.cnr.contab.util.ICancellatoLogicamente;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public class Tipo_atto_amministrativoBulk extends Tipo_atto_amministrativoBase implements ICancellatoLogicamente{
	
	public Tipo_atto_amministrativoBulk() {
		super();
	}
	public Tipo_atto_amministrativoBulk(java.lang.String cd_tipo_atto) {
		super(cd_tipo_atto);
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