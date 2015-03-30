package it.cnr.contab.docamm00.bp;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.jsp.PageContext;

import it.cnr.contab.docamm00.fatturapa.bulk.FileSdIConMetadatiTypeBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.BulkBP;

public class CaricaFatturaElettronicaBP extends BulkBP {
	private static final long serialVersionUID = 1L;

	public CaricaFatturaElettronicaBP() {
		super();
	}

	public CaricaFatturaElettronicaBP(String s) {
		super(s);
	}

	@Override
	public RemoteIterator find(ActionContext actioncontext,
			CompoundFindClause compoundfindclause, OggettoBulk oggettobulk,
			OggettoBulk oggettobulk1, String s) throws BusinessProcessException {
		return null;
	}
	
	@Override
	public void openForm(PageContext pagecontext, String action, String target,
			String encType) throws IOException, ServletException {
		super.openForm(pagecontext,action,target,"multipart/form-data");
	}
	
	@Override
	protected void init(Config config, ActionContext actioncontext)
			throws BusinessProcessException {
		super.init(config, actioncontext);
		setModel(actioncontext, new FileSdIConMetadatiTypeBulk());
	}
}
