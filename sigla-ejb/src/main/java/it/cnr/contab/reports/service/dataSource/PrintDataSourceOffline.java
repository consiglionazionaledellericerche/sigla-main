package it.cnr.contab.reports.service.dataSource;

import it.cnr.contab.reports.bulk.Print_spoolerBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.HomeCache;
import it.cnr.jada.persistency.sql.PersistentHome;

import java.io.Serializable;

public interface PrintDataSourceOffline extends Serializable {


    static final String REPORT_DATA_SOURCE="REPORT_DATA_SOURCE";

    Class getBulkClass();

    Print_spoolerBulk getPrintSpooler(UserContext userContext, Print_spoolerBulk print_spoolerBulk, BulkHome bulkHome) throws ComponentException;

}