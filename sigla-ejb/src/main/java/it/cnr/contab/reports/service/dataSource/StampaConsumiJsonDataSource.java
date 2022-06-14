package it.cnr.contab.reports.service.dataSource;

import com.google.gson.GsonBuilder;

import it.cnr.contab.ordmag.magazzino.bulk.Stampa_consumiBulk;
import it.cnr.contab.ordmag.magazzino.bulk.Stampa_consumiHome;
import it.cnr.contab.reports.bulk.Print_spoolerBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.comp.ComponentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StampaConsumiJsonDataSource extends AbstractDataSourceOffline{

    private final static Logger _log = LoggerFactory.getLogger(StampaConsumiJsonDataSource.class);

    protected  String getJson( Object o){
        return new GsonBuilder().create().toJson(o );
    }

    @Override
    public Class getBulkClass() {
        return Stampa_consumiBulk.class;
    }

    public String getDataSourceOffline(UserContext userContext, Print_spoolerBulk print_spoolerBulk, BulkHome bulkHome) throws ComponentException {
        Stampa_consumiHome home = ( Stampa_consumiHome) bulkHome;
        String json= home.getJsonDataSource(userContext,print_spoolerBulk);
        _log.info("json:"+json);
        return json;

    }
}
