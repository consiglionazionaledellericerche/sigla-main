package it.cnr.contab.reports.util;

import it.cnr.contab.reports.bulk.Print_priorityBulk;
import it.cnr.contab.reports.bulk.Print_spoolerBulk;

import java.util.Optional;

public class UtilReports {

    public static String getReportNameToRun(Print_priorityBulk print_priority, Print_spoolerBulk print_spooler){

        if (Optional.ofNullable(print_priority).
                flatMap(e->Optional.ofNullable(e.getReportNameEnte())).isPresent())
            return print_priority.getReportNameEnte();

        return print_spooler.getReport();

    }
}
