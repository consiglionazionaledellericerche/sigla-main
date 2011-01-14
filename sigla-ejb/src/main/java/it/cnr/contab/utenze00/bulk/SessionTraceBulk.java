/*
 * Created by BulkGenerator 1.5 [30/07/2008]
 * Date 31/07/2008
 */
package it.cnr.contab.utenze00.bulk;
import it.cnr.jada.bulk.annotation.*;
@BulkInfoAnnotation(shortDescription="Tabella di controllo sessioni", 
			longDescription="Tabella di controllo sessioni",
			form={@FormAnnotation(value = {
					@FieldPropertyAnnotation(name="idSessione",type=TypeProperty.FormFieldProperty), 
					@FieldPropertyAnnotation(name="serverUrl",type=TypeProperty.FormFieldProperty),
					@FieldPropertyAnnotation(name="dacr",type=TypeProperty.FormFieldProperty),
					@FieldPropertyAnnotation(name="utcr",type=TypeProperty.FormFieldProperty),
					@FieldPropertyAnnotation(name="duva",type=TypeProperty.FormFieldProperty),
					@FieldPropertyAnnotation(name="utuv",type=TypeProperty.FormFieldProperty)}),
				@FormAnnotation(name="searchtool",value={@FieldPropertyAnnotation(name="idSessione",type=TypeProperty.FormFieldProperty)})},
			columnSet=@ColumnSetAnnotation(value={
					@FieldPropertyAnnotation(name="cdUtente", type=TypeProperty.ColumnFieldProperty),
					@FieldPropertyAnnotation(name="cdCds", type=TypeProperty.ColumnFieldProperty),
					@FieldPropertyAnnotation(name="idSessione", type=TypeProperty.ColumnFieldProperty),
					@FieldPropertyAnnotation(name="dacr", type=TypeProperty.ColumnFieldProperty),
					@FieldPropertyAnnotation(name="duva", type=TypeProperty.ColumnFieldProperty)}),
			freeSearchSet=@FreeSearchSetAnnotation(value={
					@FieldPropertyAnnotation(name="idSessione", type=TypeProperty.FindFieldProperty),
					@FieldPropertyAnnotation(name="serverUrl", type=TypeProperty.FindFieldProperty),
					@FieldPropertyAnnotation(name="cdUtente", type=TypeProperty.FindFieldProperty),
					@FieldPropertyAnnotation(name="dacr", type=TypeProperty.FindFieldProperty),
					@FieldPropertyAnnotation(name="duva", type=TypeProperty.FindFieldProperty)})		
		)
public class SessionTraceBulk extends SessionTraceBase {
	/**
	 * [UTENTE Contiene gli utenti dell'applicazione.]
	 **/
	private UtenteBulk utente =  new UtenteBulk();
	/**
	 * Created by BulkGenerator 1.5 [30/07/2008]
	 * Table name: SESSION_TRACE
	 **/	
	public SessionTraceBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 1.5 [30/07/2008]
	 * Table name: SESSION_TRACE
	 **/
	public SessionTraceBulk(java.lang.String id_sessione) {
		super(id_sessione);
	}
	/**
	 * Created by BulkGenerator 1.5 [30/07/2008]
	 * Restituisce il valore di: [Contiene gli utenti dell'applicazione.]
	 **/
	public UtenteBulk getUtente() {
		return utente;
	}
	/**
	 * Created by BulkGenerator 1.5 [30/07/2008]
	 * Setta il valore di: [Contiene gli utenti dell'applicazione.]
	 **/
	public void setUtente(UtenteBulk utente)  {
		this.utente=utente;
	}
	/**
	 * Created by BulkGenerator 1.5 [30/07/2008]
	 * Restituisce il valore di: [cd_utente]
	 **/
	public java.lang.String getCd_utente() {
		UtenteBulk utente = this.getUtente();
		if (utente == null)
			return null;
		return getUtente().getCd_utente();
	}
	/**
	 * Created by BulkGenerator 1.5 [30/07/2008]
	 * Setta il valore di: [cd_utente]
	 **/
	public void setCd_utente(java.lang.String cd_utente)  {
		this.getUtente().setCd_utente(cd_utente);
	}
}