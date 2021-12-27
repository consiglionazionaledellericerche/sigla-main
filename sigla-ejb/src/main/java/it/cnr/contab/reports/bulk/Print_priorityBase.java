/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 28/07/2021
 */
package it.cnr.contab.reports.bulk;
import it.cnr.jada.persistency.Keyed;
public class Print_priorityBase extends Print_priorityKey implements Keyed {
//    PRIORITY DECIMAL(5,0) NOT NULL
	private Integer priority;
 
//    DS_REPORT VARCHAR(300)
	private String dsReport;
 
//    REPORT_NAME_ENTE VARCHAR(1500)
	private String reportNameEnte;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: PRINT_PRIORITY
	 **/
	public Print_priorityBase() {
		super();
	}
	public Print_priorityBase(String reportName) {
		super(reportName);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Intero da 0 a 9 - 0 Stampe Huge, 9 Stampe Ultra light]
	 **/
	public Integer getPriority() {
		return priority;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Intero da 0 a 9 - 0 Stampe Huge, 9 Stampe Ultra light]
	 **/
	public void setPriority(Integer priority)  {
		this.priority=priority;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Descrizione del report]
	 **/
	public String getDsReport() {
		return dsReport;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Descrizione del report]
	 **/
	public void setDsReport(String dsReport)  {
		this.dsReport=dsReport;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Nome del report per l'ENTE comprensivo di percorso relativo alla cartella reports, nel fomrato UNIX es. /preventivo/pdg/pdgdett_voce_funzione_natura.rpt]
	 **/
	public String getReportNameEnte() {
		return reportNameEnte;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Nome del report per l'ENTE comprensivo di percorso relativo alla cartella reports, nel fomrato UNIX es. /preventivo/pdg/pdgdett_voce_funzione_natura.rpt]
	 **/
	public void setReportNameEnte(String reportNameEnte)  {
		this.reportNameEnte=reportNameEnte;
	}
}