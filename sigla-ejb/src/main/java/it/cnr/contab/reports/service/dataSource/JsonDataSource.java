package it.cnr.contab.reports.service.dataSource;

import it.cnr.contab.reports.bulk.Print_spoolerBulk;
import it.cnr.jada.comp.ComponentException;

public interface JsonDataSource {

     static final String REPORT_DATA_SOURCE="REPORT_DATA_SOURCE";

    String getJsonDataSource(Print_spoolerBulk print_spoolerBulk) throws ComponentException;

    Print_spoolerBulk getPrintSpooler(Print_spoolerBulk print_spoolerBulk) throws ComponentException;

}
