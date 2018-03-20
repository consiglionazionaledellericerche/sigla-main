<%@ page pageEncoding="UTF-8"
	import = "it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.ordmag.richieste.bp.CRUDRichiestaUopBP,
		it.cnr.contab.ordmag.richieste.bulk.RichiestaUopRigaBulk,
		it.cnr.contab.ordmag.anag00.*"
%>

<% CRUDRichiestaUopBP bp = (CRUDRichiestaUopBP)BusinessProcess.getBusinessProcess(request);
    RichiestaUopRigaBulk riga = (RichiestaUopRigaBulk)bp.getRighe().getModel();
	bp.getRighe().writeHTMLTable(pageContext,"righeSet",true,false,true,"100%","200px"); %>
<tr><td colspan=10>
	      <%
	      	String[][] pages = null;
	      	if(riga != null && riga.getNumero() != null){
	      		pages = new String[][] {
					{ "tabRichiestaDettaglio","Dettaglio Riga","/ordmag/richieste/tab_richiesta_uop_dettaglio.jsp" },
					{ "tabRichiestaDettaglioAllegati","Allegati","/ordmag/richieste/tab_richiesta_uop_dettaglio_allegati.jsp" } };
	      	} else {
	      		pages = new String[][] {
					{ "tabRichiestaDettaglio","Dettaglio Riga","/ordmag/richieste/tab_richiesta_uop_dettaglio.jsp" } };
	      	}
	      	JSPUtils.tabbed(pageContext, "tabRichiestaUopDettaglio",
	      			pages,
	      			bp.getTab("tabRichiestaUopDettaglio"), "left", "90%", null, true);
	      %>
</td></tr>
