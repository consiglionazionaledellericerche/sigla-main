package it.cnr.contab.docamm00.views.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class V_stm_paramin_ft_attivaKey extends OggettoBulk implements KeyedPersistent {

	// ID_REPORT DECIMAL(22,0) NOT NULL
	private java.math.BigDecimal id_report;
public V_stm_paramin_ft_attivaKey() {
	super();
}
public V_stm_paramin_ft_attivaKey(java.math.BigDecimal id_report) {
	super();
	this.id_report = id_report;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof V_stm_paramin_ft_attivaKey)) return false;
	V_stm_paramin_ft_attivaKey k = (V_stm_paramin_ft_attivaKey)o;
	if(!compareKey(getId_report(),k.getId_report())) return false;
	return true;
}
/* 
 * Getter dell'attributo id_report
 */
public java.math.BigDecimal getId_report() {
	return id_report;
}
public int primaryKeyHashCode() {
	return calculateKeyHashCode(getId_report());
}
/* 
 * Setter dell'attributo id_report
 */
public void setId_report(java.math.BigDecimal id_report) {
	this.id_report = id_report;
}
}
