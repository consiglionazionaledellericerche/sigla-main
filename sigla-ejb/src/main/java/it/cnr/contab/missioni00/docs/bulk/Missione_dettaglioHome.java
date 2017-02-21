package it.cnr.contab.missioni00.docs.bulk;

import org.springframework.util.StringUtils;

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.persistency.PersistentCache;

public class Missione_dettaglioHome extends BulkHome {
public Missione_dettaglioHome(java.sql.Connection conn) {
	super(Missione_dettaglioBulk.class,conn);
}
public Missione_dettaglioHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Missione_dettaglioBulk.class,conn,persistentCache);
}
@Override
public Persistent completeBulkRowByRow(UserContext userContext,
		Persistent persistent) throws PersistencyException {
	Missione_dettaglioBulk dettaglio = (Missione_dettaglioBulk)persistent;
	if (dettaglio.isMissioneFromGemis() && !StringUtils.isEmpty(dettaglio.getDs_giustificativo())){
		dettaglio.setAllegatiDocumentale("<button class='Button' style='width:60px;' onclick='cancelBubble(event); if (disableDblClick()) "+
				"doVisualizzaAllegatiGiustificativi("+dettaglio.getDs_giustificativo()+"); return false' "+
				"onMouseOver='mouseOver(this)' onMouseOut='mouseOut(this)' onMouseDown='mouseDown(this)' onMouseUp='mouseUp(this)' "+
				"title='Visualizza Giustificativi Collegati'><img align='middle' class='Button' src='img/application-pdf.png'></button>");
	}
	return super.completeBulkRowByRow(userContext, persistent);
	}
}
