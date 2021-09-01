/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 28/07/2021
 */
package it.cnr.contab.reports.bulk;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.FindException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class Print_priorityHome extends BulkHome {
	public Print_priorityHome(Connection conn) {
		super(Print_priorityBulk.class, conn);
	}
	public Print_priorityHome(Connection conn, PersistentCache persistentCache) {
		super(Print_priorityBulk.class, conn, persistentCache);
	}
	public Print_priorityBulk findPrintPriorityByReportName (UserContext userContext,String reportName) throws PersistencyException {
		Print_priorityBulk print_priorityBulk = ( Print_priorityBulk) this.findByPrimaryKey(userContext, new Print_priorityBulk(reportName));
		if (!Optional.ofNullable(print_priorityBulk).isPresent() ){
			SQLBuilder sql= this.createSQLBuilder();
			sql.addClause("AND", "reportNameEnte", SQLBuilder.EQUALS, reportName);
			List<Print_priorityBulk> l = this.fetchAll(sql);
			if ( l!=null && l.isEmpty())
				return null;
			if ( l.size()>1)
				throw new FindException("findPrintPriorityByReportName return more than one row");
			 return  l.get(0);

		}
		return print_priorityBulk;
	}
}