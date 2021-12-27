/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 28/07/2021
 */
package it.cnr.contab.reports.bulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public class Print_priorityBulk extends Print_priorityBase {
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: PRINT_PRIORITY
	 **/
	public Print_priorityBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: PRINT_PRIORITY
	 **/
	public final static java.util.Dictionary <Integer,String> PRIORITY;
	static {
		PRIORITY = new it.cnr.jada.util.OrderedHashtable();
		PRIORITY.put(0, "Stampe Huge");
		PRIORITY.put(1, "Stampe Priority 1");
		PRIORITY.put(2, "Stampe Priority 2");
		PRIORITY.put(3, "Stampe Priority 3");
		PRIORITY.put(4, "Stampe Priority 4");
		PRIORITY.put(5, "Stampe Priority 5");
		PRIORITY.put(6, "Stampe Priority 6");
		PRIORITY.put(7, "Stampe Priority 7");
		PRIORITY.put(8, "Stampe Priority 8");
		PRIORITY.put(9, "Stampe Ultra light");


	}

	public Print_priorityBulk(String reportName) {
		super(reportName);
	}
}