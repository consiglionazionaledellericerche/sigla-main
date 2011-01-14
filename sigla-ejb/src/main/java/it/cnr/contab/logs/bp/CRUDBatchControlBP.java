package it.cnr.contab.logs.bp;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.Config;
import it.cnr.jada.util.action.*;
import it.cnr.jada.util.jsp.Button;

public class CRUDBatchControlBP extends SimpleCRUDBP
{

    public CRUDBatchControlBP()
    {
        parametri = new SimpleDetailCRUDController("parametri", it.cnr.contab.logs.bulk.Batch_procedura_parametroBulk.class, "parametri", this);
        parametri.setReadonly(false);
    }

    public CRUDBatchControlBP(String s)
    {
        super(s);
        parametri = new SimpleDetailCRUDController("parametri", it.cnr.contab.logs.bulk.Batch_procedura_parametroBulk.class, "parametri", this);
        parametri.setReadonly(false);
    }

    protected Button[] createToolbar()
    {
        Button abutton[] = new Button[1];
        int i = 0;
        abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "CRUDToolbar.save");
        return abutton;
    }

    public final SimpleDetailCRUDController getParametri()
    {
        return parametri;
    }

    public OggettoBulk initializeModelForEdit(ActionContext actioncontext, OggettoBulk oggettobulk)
        throws BusinessProcessException
    {
        return oggettobulk;
    }

    private final SimpleDetailCRUDController parametri;
}