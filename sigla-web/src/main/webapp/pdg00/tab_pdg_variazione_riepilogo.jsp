<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.pdg00.bp.*,
		it.cnr.contab.pdg00.bulk.*"
%>
<script language="JavaScript" src="scripts/util.js"></script>

<%
	PdGVariazioneBP bp = (PdGVariazioneBP)BusinessProcess.getBusinessProcess(request);
	SimpleDetailCRUDController controller1 = ( (PdGVariazioneBP)bp ).getRiepilogoEntrate();
	SimpleDetailCRUDController controller2 = ( (PdGVariazioneBP)bp ).getRiepilogoSpese();
    controller1.setMultiSelection(false);
    controller2.setMultiSelection(false);
    controller1.writeHTMLTable(pageContext,"entrate",false,false,false,"100%","200px"); 
    controller2.writeHTMLTable(pageContext,"spese",false,false,false,"100%","200px"); 
%>
