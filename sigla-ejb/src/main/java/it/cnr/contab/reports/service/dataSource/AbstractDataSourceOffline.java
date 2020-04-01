package it.cnr.contab.reports.service.dataSource;

import com.google.gson.GsonBuilder;
import it.cnr.contab.reports.bulk.Print_spoolerBulk;
import it.cnr.contab.reports.bulk.Print_spooler_paramBulk;
import it.cnr.jada.comp.ComponentException;

import java.util.Optional;

public abstract class AbstractDataSourceOffline implements PrintDataSourceOffline{


    @Override
    public Print_spoolerBulk getPrintSpooler(Print_spoolerBulk printSpooler) throws ComponentException {
        if (Optional.of(printSpooler).isPresent()){
            String ds =getDataSourceOffline(printSpooler);
            //String jsonDs ="ciao";
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
