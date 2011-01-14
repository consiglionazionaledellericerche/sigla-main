package it.cnr.contab.logregistry00.core.bulk;

/**
 * Insert the type's description here.
 * Creation date: (30/09/2003 17.13.16)
 * @author: CNRADM
 */
public interface ILogRegistryBulk {

	public static final String PKG_NAME = "it.cnr.contab.logregistry00.logs.bulk";
	public static final String SUFFIX = "Bulk";
	
/**
 * Insert the method's description here.
 * Creation date: (01/10/2003 16.01.34)
 * @return java.lang.String
 */
public java.lang.String getAction_();
/* 
 * Getter dell'attributo dt_transaction_
 */
public java.sql.Timestamp getDt_transaction_();
/* 
 * Getter dell'attributo pg_storico_
 */
public java.math.BigDecimal getPg_storico_();
/* 
 * Getter dell'attributo user_
 */
public java.lang.String getUser_();
/**
 * Insert the method's description here.
 * Creation date: (01/10/2003 16.01.34)
 * @param newAction_ java.lang.String
 */
public void setAction_(java.lang.String newAction_);
/* 
 * Setter dell'attributo dt_transaction_
 */
public void setDt_transaction_(java.sql.Timestamp dt_transaction_);
/* 
 * Setter dell'attributo pg_storico_
 */
public void setPg_storico_(java.math.BigDecimal pg_storico_);
/* 
 * Setter dell'attributo user_
 */
public void setUser_(java.lang.String user_);
}
