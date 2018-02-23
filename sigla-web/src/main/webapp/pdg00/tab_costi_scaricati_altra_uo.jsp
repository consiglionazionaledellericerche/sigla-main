<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.pdg00.cdip.bulk.*,
		it.cnr.contab.pdg00.bp.*"
%>

<%	CostiDipendenteBP bp = (CostiDipendenteBP)BusinessProcess.getBusinessProcess(request);
	bp.getCostiScaricatiAltraUO().writeHTMLTable(
				pageContext,
				bp.getCosti_dipendente().isMensile()?"mese":null,
				true,
				false,
				true,
				"100%",
				"200px",
				false); %>