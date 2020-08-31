<!-- 
 ?ResourceName ""
 ?ResourceTimestamp ""
 ?ResourceEdition ""
-->

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		    it.cnr.contab.anagraf00.bp.*,
            it.cnr.contab.anagraf00.core.bulk.*"
%>

<%
  CRUDAnagraficaBP bp = (CRUDAnagraficaBP)BusinessProcess.getBusinessProcess(request);
  AnagraficoBulk anagrafico = (AnagraficoBulk)bp.getModel();
  AssGruppoIvaAnagBulk ass = null;
  if (anagrafico.isGruppoIVA()){
      bp.getCrudAnagraficoGruppiIvaCollegati().writeHTMLTable(pageContext,"anagrafico",true,false,true,"100%","150px");
      ass = (AssGruppoIvaAnagBulk)bp.getCrudAnagraficoGruppiIvaCollegati().getModel();
  } else {
      bp.getCrudAnagraficoGruppiIvaCollegati().writeHTMLTable(pageContext,"gruppiIvaAssociati",true,false,true,"100%","150px");
      ass = (AssGruppoIvaAnagBulk)bp.getCrudAnagraficoGruppiIvaCollegati().getModel();
  }

%>
<table border="0" cellspacing="0" cellpadding="2">
	  <tr>
	    <td>
	    <%  if (anagrafico.isGruppoIVA()){
                bp.getCrudAnagraficoGruppiIvaCollegati().writeFormField(out,"findAnagrafico");
            }else{
                bp.getCrudAnagraficoGruppiIvaCollegati().writeFormField(out,"anagraficoGruppoIva");
            }%>
         </td>
      </tr>
</table>