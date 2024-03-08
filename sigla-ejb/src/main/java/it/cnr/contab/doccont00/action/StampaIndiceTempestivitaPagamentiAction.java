package it.cnr.contab.doccont00.action;

import it.cnr.contab.doccont00.consultazioni.bulk.VConsIndicatorePagamentiBulk;
import it.cnr.contab.doccont00.core.bulk.StampaIndiceTempestivitaPagamentiBulk;
import it.cnr.contab.reports.action.ParametricPrintAction;
import it.cnr.contab.reports.bp.ParametricPrintBP;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.bulk.BulkInfo;
import it.cnr.jada.bulk.ColumnFieldProperty;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.ejb.BulkLoaderIterator;
import it.cnr.jada.ejb.CRUDComponentSession;
import it.cnr.jada.excel.bp.OfflineExcelSpoolerBP;
import it.cnr.jada.excel.ejb.BframeExcelComponentSession;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.Query;
import it.cnr.jada.util.OrderedHashtable;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.ejb.EJBCommonServices;

import java.util.Enumeration;
import java.util.Optional;

public class StampaIndiceTempestivitaPagamentiAction extends ParametricPrintAction {
    public StampaIndiceTempestivitaPagamentiAction() {
    }

    public Forward doExcel(ActionContext actioncontext) {
        try {
            final ParametricPrintBP parametricPrintBP = (ParametricPrintBP) actioncontext.getBusinessProcess();
            StampaIndiceTempestivitaPagamentiBulk stampaIndiceTempestivitaPagamentiBulk = (StampaIndiceTempestivitaPagamentiBulk) parametricPrintBP.getModel();
            BframeExcelComponentSession bframeExcelComponentSession = (BframeExcelComponentSession)
                    EJBCommonServices.createEJB("BFRAMEEXCEL_EJB_BframeExcelComponentSession");
            CRUDComponentSession crudComponentSession = EJBCommonServices.createEJB("JADAEJB_CRUDComponentSession", CRUDComponentSession.class);
            final VConsIndicatorePagamentiBulk vConsIndicatorePagamentiBulk = new VConsIndicatorePagamentiBulk();
            vConsIndicatorePagamentiBulk.setEsercizio(stampaIndiceTempestivitaPagamentiBulk.getEsercizio());
            Optional.ofNullable(stampaIndiceTempestivitaPagamentiBulk.getTrimestre())
                    .filter(s -> !s.equalsIgnoreCase("*"))
                    .map(Integer::valueOf)
                    .ifPresent(trimestre -> vConsIndicatorePagamentiBulk.setTrimestre(trimestre));
            final RemoteIterator remoteiterator = crudComponentSession.cerca(actioncontext.getUserContext(), new CompoundFindClause(), vConsIndicatorePagamentiBulk);

            String longDescription = "Tempestivita Pagamenti";
            Query query = ((BulkLoaderIterator) remoteiterator).getQuery();
            BulkInfo bulkInfo = BulkInfo.getBulkInfo(VConsIndicatorePagamentiBulk.class);

            OrderedHashtable columnLabel = new OrderedHashtable();
            OrderedHashtable columnHeaderLabel = new OrderedHashtable();
            OrderedHashtable colonnedaEstrarre = new OrderedHashtable();
            for (Enumeration enumeration = bulkInfo.getColumnFieldPropertyDictionary().keys(); enumeration.hasMoreElements(); ) {
                String columnName = (String) enumeration.nextElement();
                colonnedaEstrarre.put(columnName, bulkInfo.getColumnFieldPropertyDictionary().get(columnName));
            }
            for (Enumeration enumeration = bulkInfo.getColumnFieldPropertyDictionary().elements(); enumeration.hasMoreElements(); ) {
                ColumnFieldProperty columnfieldproperty = (ColumnFieldProperty) enumeration.nextElement();
                columnLabel.put(columnfieldproperty, columnfieldproperty.getLabel());
                Optional.ofNullable(columnfieldproperty.getHeaderLabel())
                                .ifPresent(s -> columnHeaderLabel.put(columnfieldproperty, s));
            }
            OggettoBulk bulk = bframeExcelComponentSession.addQueue(
                    actioncontext.getUserContext(),
                    columnLabel,
                    columnHeaderLabel, longDescription, colonnedaEstrarre, query.toString(), query.getColumnMap(), vConsIndicatorePagamentiBulk);
            OfflineExcelSpoolerBP excelSpoolerBP = (OfflineExcelSpoolerBP) actioncontext.createBusinessProcess("OfflineExcelSpoolerBP");
            excelSpoolerBP.setModel(actioncontext, bulk);
            return actioncontext.addBusinessProcess(excelSpoolerBP);
        } catch (BusinessProcessException businessprocessexception) {
            return handleException(actioncontext, businessprocessexception);
        } catch (Throwable throwable) {
            return handleException(actioncontext, throwable);
        }
    }
}
