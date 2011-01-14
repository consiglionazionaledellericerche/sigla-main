/*
* Created by Generator 1.0
* Date 22/09/2005
*/
package it.cnr.contab.logregistry00.logs.bulk;
import it.cnr.jada.persistency.Keyed;
public class L_ass_comp_doc_cont_nmpBase extends L_ass_comp_doc_cont_nmpKey implements Keyed {
//    USER_ VARCHAR(20) NOT NULL
	private java.lang.String user_;
 
//    DT_TRANSACTION_ TIMESTAMP(7) NOT NULL
	private java.sql.Timestamp dt_transaction_;
 
//    ACTION_ CHAR(1) NOT NULL
	private java.lang.String action_;
 
	public L_ass_comp_doc_cont_nmpBase() {
		super();
	}
	public L_ass_comp_doc_cont_nmpBase(java.math.BigDecimal pg_storico_, java.lang.String cd_cds_compenso, java.lang.Integer esercizio_compenso, java.lang.String cd_uo_compenso, java.lang.Long pg_compenso, java.lang.String cd_cds_doc, java.lang.Integer esercizio_doc, java.lang.Long pg_doc, java.lang.String cd_tipo_doc) {
		super(pg_storico_, cd_cds_compenso, esercizio_compenso, cd_uo_compenso, pg_compenso, cd_cds_doc, esercizio_doc, pg_doc, cd_tipo_doc);
	}
	public java.lang.String getUser_ () {
		return user_;
	}
	public void setUser_(java.lang.String user_)  {
		this.user_=user_;
	}
	public java.sql.Timestamp getDt_transaction_ () {
		return dt_transaction_;
	}
	public void setDt_transaction_(java.sql.Timestamp dt_transaction_)  {
		this.dt_transaction_=dt_transaction_;
	}
	public java.lang.String getAction_ () {
		return action_;
	}
	public void setAction_(java.lang.String action_)  {
		this.action_=action_;
	}
}