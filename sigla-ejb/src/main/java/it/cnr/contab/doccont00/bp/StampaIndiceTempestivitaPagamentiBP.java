package it.cnr.contab.doccont00.bp;

import it.cnr.contab.doccont00.core.bulk.StampaIndiceTempestivitaPagamentiBulk;
import it.cnr.contab.reports.bp.ParametricPrintBP;
import it.cnr.jada.util.Config;
import it.cnr.jada.util.jsp.Button;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class StampaIndiceTempestivitaPagamentiBP extends ParametricPrintBP {
    public StampaIndiceTempestivitaPagamentiBP() {
    }

    public StampaIndiceTempestivitaPagamentiBP(String function) {
        super(function);
    }

    @Override
    public Button[] createToolbar() {
        List<Button> toolbar = new ArrayList<Button>();
        toolbar.addAll(Arrays.asList(super.createToolbar()));
        toolbar.add(new Button(Config.getHandler().getProperties(getClass()), "Toolbar.excel"));
        return toolbar.toArray(new Button[toolbar.size()]);
    }

    public boolean isExcelButtonHidden() {
        return !Optional.ofNullable(getModel())
                .filter(StampaIndiceTempestivitaPagamentiBulk.class::isInstance)
                .map(StampaIndiceTempestivitaPagamentiBulk.class::cast)
                .flatMap(sitpb -> Optional.ofNullable(sitpb.getDettagli()))
                .orElse(Boolean.FALSE);
    }
}
