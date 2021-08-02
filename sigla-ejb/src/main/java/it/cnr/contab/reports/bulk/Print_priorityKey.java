/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 28/07/2021
 */
package it.cnr.contab.reports.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Print_priorityKey extends OggettoBulk implements KeyedPersistent {
	private String reportName;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: PRINT_PRIORITY
	 **/
	public Print_priorityKey() {
		super();
	}
	public Print_priorityKey(String reportName) {
		super();
		this.reportName=reportName;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Print_priorityKey)) return false;
		Print_priorityKey k = (Print_priorityKey) o;
		if (!compareKey(getReportName(), k.getReportName())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getReportName());
		return i;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Nome del report comprensivo di percorso relativo alla cartella reports, nel fomrato UNIX es. /preventivo/pdg/pdgdett_voce_funzione_natura.rpt]
	 **/
	public void setReportName(String reportName)  {
		this.reportName=reportName;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Nome del report comprensivo di percorso relativo alla cartella reports, nel fomrato UNIX es. /preventivo/pdg/pdgdett_voce_funzione_natura.rpt]
	 **/
	public String getReportName() {
		return reportName;
	}
}