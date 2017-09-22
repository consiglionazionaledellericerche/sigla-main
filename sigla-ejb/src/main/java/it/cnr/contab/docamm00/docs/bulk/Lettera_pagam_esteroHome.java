package it.cnr.contab.docamm00.docs.bulk;

import it.cnr.contab.docamm00.ejb.ProgressiviAmmComponentSession;
import it.cnr.contab.doccont00.core.bulk.MandatoBulk;
import it.cnr.contab.doccont00.service.DocumentiContabiliService;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.persistency.PersistentCache;

public class Lettera_pagam_esteroHome extends BulkHome {
	private DocumentiContabiliService documentiContabiliService;
	
	public Lettera_pagam_esteroHome(java.sql.Connection conn) {
		super(Lettera_pagam_esteroBulk.class,conn);
		documentiContabiliService = SpringUtil.getBean("documentiContabiliService",
				DocumentiContabiliService.class);					
	}
	public Lettera_pagam_esteroHome(java.sql.Connection conn,PersistentCache persistentCache) {
		super(Lettera_pagam_esteroBulk.class,conn,persistentCache);
		documentiContabiliService = SpringUtil.getBean("documentiContabiliService",
				DocumentiContabiliService.class);							
	}
	public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext,OggettoBulk bulk) 
		throws PersistencyException {
			
		try	{
			Lettera_pagam_esteroBulk lettera = (Lettera_pagam_esteroBulk) bulk;
			ProgressiviAmmComponentSession progressiviSession = (ProgressiviAmmComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_ProgressiviAmmComponentSession", ProgressiviAmmComponentSession.class);
			lettera.setPg_lettera(progressiviSession.getNextPG(userContext, new Numerazione_doc_ammBulk(lettera)));
		} catch ( Throwable e )	{
			throw new PersistencyException( e );
		}
	}
	@Override
	public Persistent completeBulkRowByRow(UserContext userContext, Persistent persistent) throws PersistencyException {
		persistent =  super.completeBulkRowByRow(userContext, persistent);
		if (persistent instanceof Lettera_pagam_esteroBulk){
			Lettera_pagam_esteroBulk bulk = (Lettera_pagam_esteroBulk)persistent;
			if (!bulk.getStato_trasmissione().equals(MandatoBulk.STATO_TRASMISSIONE_NON_INSERITO)){
				if (((CNRUserContext)userContext).isFromBootstrap()) {
					bulk.setDocumento("<a class='btn btn-link' onclick='"+
							"doVisualizzaSingoloDocumento("+bulk.getEsercizio()+",\""+bulk.getCd_cds()+"\",\""+bulk.getCd_unita_organizzativa()+"\","+bulk.getPg_documento_cont()+",\""+bulk.getCd_tipo_documento_cont()+"\");' "+
							"title='Visualizza Documento Contabile'><i class='fa fa-fw fa-2x fa-file-pdf-o text-danger' aria-hidden='true'></i></a>");
				} else {
					bulk.setDocumento("<button class='Button' style='width:60px;' onclick='cancelBubble(event); if (disableDblClick()) "+
							"doVisualizzaSingoloDocumento("+bulk.getEsercizio()+",\""+bulk.getCd_cds()+"\",\""+bulk.getCd_unita_organizzativa()+"\","+bulk.getPg_documento_cont()+",\""+bulk.getCd_tipo_documento_cont()+"\"); return false' "+
							"onMouseOver='mouseOver(this)' onMouseOut='mouseOut(this)' onMouseDown='mouseDown(this)' onMouseUp='mouseUp(this)' "+
							"title='Visualizza Documento Contabile'><img align='middle' class='Button' src='img/application-pdf.png'></button>");
				}
			}
		}
		return persistent;
	}

}
