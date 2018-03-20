<!-- 
 ?ResourceName ""
 ?ResourceTimestamp ""
 ?ResourceEdition ""
-->

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		    it.cnr.contab.anagraf00.bp.*,
            it.cnr.contab.anagraf00.core.bulk.Anagrafico_terzoBulk"
%>

<%
  CRUDAnagraficaBP bp = (CRUDAnagraficaBP)BusinessProcess.getBusinessProcess(request);
  bp.getCrudAssociatiStudio().writeHTMLTable(pageContext,null,true,false,true,"100%","150px");
  Anagrafico_terzoBulk terzoAssociato = (Anagrafico_terzoBulk)bp.getCrudAssociatiStudio().getModel();
  boolean isReadOnly = false;
  if (terzoAssociato!=null && terzoAssociato.isNew())
	  isReadOnly=true;
%>
<table border="0" cellspacing="0" cellpadding="2">
	  <tr>         
         <td><% bp.getCrudAssociatiStudio().writeFormLabel(out,"find_terzo");%></td>
         <td colspan=3><% bp.getCrudAssociatiStudio().writeFormInput(out,null,"cd_terzo",!isReadOnly,null,"");%>
		               <% bp.getCrudAssociatiStudio().writeFormInput(out,"ds_terzo");%>
		               <% bp.getCrudAssociatiStudio().writeFormInput(out,null,"find_terzo",!isReadOnly,null,"");%></td>
      </tr>
      <tr>
         <td><% bp.getCrudAssociatiStudio().writeFormLabel(out,"dt_canc");%></td>
         <td colspan=3><% bp.getCrudAssociatiStudio().writeFormInput(out,"dt_canc");%>
      </tr>            
      </table>