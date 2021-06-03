/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 03/06/2021
 */
package it.cnr.contab.incarichi00.bulk;

public class Scadenzario_dottoratirataBulk extends Scadenzario_dottoratirataBase {
	/**
	 * [SCADENZARIO_DOTTORATI ]
	 **/
	private Scadenzario_dottoratiBulk scadenzarioDottorati =  new Scadenzario_dottoratiBulk();
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: SCADENZARIO_DOTTORATI_RATA
	 **/
	public Scadenzario_dottoratirataBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: SCADENZARIO_DOTTORATI_RATA
	 **/
	public Scadenzario_dottoratirataBulk(Long id) {
		super(id);
	}
	public Scadenzario_dottoratiBulk getScadenzarioDottorati() {
		return scadenzarioDottorati;
	}
	public void setScadenzarioDottorati(Scadenzario_dottoratiBulk scadenzarioDottorati)  {
		this.scadenzarioDottorati=scadenzarioDottorati;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Identificativo scadenzario dottorati.]
	 **/
	public Long getIdScadenzarioDottorati() {
		Scadenzario_dottoratiBulk scadenzarioDottorati = this.getScadenzarioDottorati();
		if (scadenzarioDottorati == null)
			return null;
		return getScadenzarioDottorati().getId();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Identificativo scadenzario dottorati.]
	 **/
	public void setIdScadenzarioDottorati(Long idScadenzarioDottorati)  {
		this.getScadenzarioDottorati().setId(idScadenzarioDottorati);
	}
}