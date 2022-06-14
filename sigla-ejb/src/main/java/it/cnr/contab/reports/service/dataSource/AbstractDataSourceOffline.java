package it.cnr.contab.reports.service.dataSource;

import it.cnr.contab.reports.bulk.Print_spoolerBulk;
import it.cnr.contab.reports.bulk.Print_spooler_paramBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.comp.ComponentException;

import java.util.Optional;

public abstract class AbstractDataSourceOffline implements PrintDataSourceOffline{

    abstract String getDataSourceOffline(UserContext userContext, Print_spoolerBulk print_spoolerBulk, BulkHome bulkHome) throws ComponentException;
    @Override
    public Print_spoolerBulk getPrintSpooler(UserContext userContext, Print_spoolerBulk printSpooler, BulkHome bulkHome) throws ComponentException {
        if (Optional.of(printSpooler).isPresent()){
            String ds =getDataSourceOffline(userContext,printSpooler,bulkHome);
            Print_spooler_paramBulk pDs = new Print_spooler_paramBulk(REPORT_DATA_SOURCE,printSpooler.getPgStampa());
            pDs.setParamType( String.class.getCanonicalName());
            int posDs=printSpooler.getParams().indexOfByPrimaryKey(pDs);
            if ( posDs>=0){
                Print_spooler_paramBulk paramList = (Print_spooler_paramBulk)printSpooler.getParams().get( posDs);
                paramList.setParamType( String.class.getCanonicalName());
                paramList.setValoreParam( ds);
            }else{
                pDs.setValoreParam( ds );
                printSpooler.addParam( pDs );
            }
        }
        return printSpooler;
    }
}
