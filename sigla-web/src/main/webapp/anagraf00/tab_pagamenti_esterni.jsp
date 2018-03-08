<!-- 
 ?ResourceName ""
 ?ResourceTimestamp ""
 ?ResourceEdition ""
-->

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.anagraf00.bp.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.anagraf00.core.bulk.*"
%>

<%
  CRUDAnagraficaBP bp = (CRUDAnagraficaBP)BusinessProcess.getBusinessProcess(request);
  AnagraficoBulk anagrafico = (AnagraficoBulk)bp.getModel();  
%>

      <% bp.getCrudPagamenti_esterni().writeHTMLTable(pageContext,null,(!anagrafico.isDipendente()),false,(!anagrafico.isDipendente()),"100%","150px"); %>
      <table border="0" cellspacing="0" cellpadding="2" align=center>
      <tr>
        <td><% bp.getCrudPagamenti_esterni().writeFormLabel(out,"tipo_rapporto");%></td>
        <td><% bp.getCrudPagamenti_esterni().writeFormInput(out,"tipo_rapporto");%></td>
      </tr>      
      <tr>
        <td><% bp.getCrudPagamenti_esterni().writeFormLabel(out,"dt_pagamento");%></td>
        <td><% bp.getCrudPagamenti_esterni().writeFormInput(out,"dt_pagamento");%></td>
      </tr>
      <tr>
        <td><% bp.getCrudPagamenti_esterni().writeFormLabel(out,"im_pagamento");%></td>
        <td><% bp.getCrudPagamenti_esterni().writeFormInput(out,"im_pagamento");%></td>
      </tr>
      <tr>
        <td><% bp.getCrudPagamenti_esterni().writeFormLabel(out,"im_spese");%></td>
        <td><% bp.getCrudPagamenti_esterni().writeFormInput(out,"im_spese");%></td>
      </tr>
      <tr>
        <td><% bp.getCrudPagamenti_esterni().writeFormLabel(out,"im_totale");%></td>
        <td><% bp.getCrudPagamenti_esterni().writeFormInput(out,"im_totale");%></td>
      </tr>      
      <tr>
        <td><% bp.getCrudPagamenti_esterni().writeFormLabel(out,"ds_pagamento");%></td>
        <td><% bp.getCrudPagamenti_esterni().writeFormInput(out,"ds_pagamento");%></td>
      </tr>
      </table>