package it.cnr.contab.reports.service.dataSource;

import it.cnr.contab.reports.bulk.Print_spoolerBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;

import java.io.Serializable;

public interface PrintDataSourceOffline extends Serializable {

    static final String REPORT_DATA_SOURCE="REPORT_DATA_SOURCE";

    String getDataSourceOffline(Print_spoolerBulk print_spoolerBulk) throws ComponentException;

    Print_spoolerBulk getPrintSpooler(UserContext userContext,Print_spoolerBulk print_spoolerBulk) throws ComponentException;

}