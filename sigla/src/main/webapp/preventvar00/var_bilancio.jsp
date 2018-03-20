<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.action.*,
		it.cnr.jada.util.action.*,
	    it.cnr.contab.preventvar00.bulk.*,		
		it.cnr.contab.preventvar00.bp.*" %>

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Variazioni di Bilancio</title>
</head>
<body class="Form">

<%	CRUDVarBilancioBP bp = (CRUDVarBilancioBP)BusinessProcess.getBusinessProcess(request);
	bp.openFormWindow(pageContext); 
%>

<table class="Panel">
  <tr>
	<td><% bp.getController().writeFormLabel(out,"pg_variazione"); %></td>
	<td>
		<% bp.getController().writeFormInput(out,"pg_variazione"); %>
		<% bp.getController().writeFormLabel(out,"esercizio_res"); %>
		<% bp.getController().writeFormInput(out,null,"esercizio_res",(bp.getCompetenza_residui() != null && bp.getCompetenza_residui().equalsIgnoreCase("C")),null,"onChange=\"submitForm('doOnEsercizioResChange')\""); %>
	</td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"ds_variazione"); %></td>
	<td><% bp.getController().writeFormInput(out,"ds_variazione"); %></td>
  </tr>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"ds_delibera"); %></td>
	<td><% bp.getController().writeFormInput(out,"ds_delibera"); %></td>
  </tr>
  <% if (!bp.isSearching()){ %>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"ti_variazione"); %></td>
	<td><% bp.getController().writeFormInput(out,null,"ti_variazione",false,null,"onChange=\"submitForm('doOnTiVariazioneChange')\"");%></td>
  </tr>
  <% } else { %>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"ti_variazioneForSearch"); %></td>
	<td><% bp.getController().writeFormInput(out,null,"ti_variazioneForSearch",false,null,"onChange=\"submitForm('doOnTiVariazioneChange')\"");%></td>
  </tr>	  
  <% } %>
  <tr>
	<td><% bp.getController().writeFormLabel(out,"find_causale"); %></td>
	<td><% bp.getController().writeFormInput(out,"find_causale"); %></td>
  </tr>
  <% if (bp.isUoEnte(new HttpActionContext(this,request,response))){ %>
   <% if (bp.isSearching() || (bp.getModel() != null && 
                               bp.getModel() instanceof Var_bilancioBulk &&
                               ((Var_bilancioBulk)bp.getModel()).getPg_variazione_pdg()!=null)){ %>   
	<tr>
		<td><% bp.getController().writeFormLabel(out,"find_pdg_variazione"); %></td>
		<td><% bp.getController().writeFormInput(out,"find_pdg_variazione"); %></td>
  	</tr>
   <%}%>
   <% if (bp.isSearching() || (bp.getModel() != null && 
                               bp.getModel() instanceof Var_bilancioBulk &&
                               ((Var_bilancioBulk)bp.getModel()).getPg_var_stanz_res()!=null)){ %>   
	<tr>
		<td><% bp.getController().writeFormLabel(out,"find_variazione_stanz_res"); %></td>
		<td><% bp.getController().writeFormInput(out,"find_variazione_stanz_res"); %></td>
  	</tr>
   <%}%>
   <% if (bp.isSearching() || (bp.getModel() != null && 
                               bp.getModel() instanceof Var_bilancioBulk &&
                               ((Var_bilancioBulk)bp.getModel()).getPg_mandato()!=null)){ %>
	<tr>
		<td><% bp.getController().writeFormLabel(out,"find_cds_mandato"); %></td>
		<td><% bp.getController().writeFormInput(out,"cd_cds_mandato"); %>
            <% bp.getController().writeFormInput(out,"ds_cds_mandato"); %>
			<% bp.getController().writeFormInput(out,"find_cds_mandato"); %> </td>
	</tr>
	<tr>
		<td><% bp.getController().writeFormLabel(out,"find_mandato"); %></td>
		<td><% bp.getController().writeFormInput(out,"find_mandato"); %> </td>
	</tr>
   <%}%>
  <% } %>
</table>

<table class="Group" style="width:100%">

  <tr>
	<td rowspan = "4">

		<% bp.getDettagliCRUDController().writeHTMLTable(
					pageContext,
					null,
					true,
					false,
					true,
					null,
					"150px",
					true);
		%>

	</td>
  </tr>
</table>

<table class="Panel" style="width:100%">
  <tr>
	<td align="right">
	  	<span class="FormLabel">Totale: </span>
		<% bp.getController().writeFormInput(out,"totaleEntrate"); %>
		<% bp.getController().writeFormInput(out,"totaleSpese"); %>
	</td>
  </tr>
</table>
<table class="Group" style="width:100%">
  <tr>
	<td></td>
	<td><% bp.getDettagliCRUDController().writeFormInput(out,"tipoGestione"); %></td>
  </tr>
  <tr>
	<td><% bp.getDettagliCRUDController().writeFormLabel(out,"cd_voce"); %></td>
	<td><% bp.getDettagliCRUDController().writeFormInput(out,"cd_voce"); %>
		<% bp.getDettagliCRUDController().writeFormInput(out,"ds_voce"); %>
		<% bp.getDettagliCRUDController().writeFormInput(out,"find_voce"); %></td>
  </tr>
  <tr>
	<td><% bp.getDettagliCRUDController().writeFormLabel(out,"importoAssestato"); %></td>
	<td><% bp.getDettagliCRUDController().writeFormInput(out,"importoAssestato"); %></td>
  </tr>
  <tr>
	<td><% bp.getDettagliCRUDController().writeFormLabel(out,"im_variazione"); %></td>
	<td><% bp.getDettagliCRUDController().writeFormInput(out,"im_variazione"); %></td>
  </tr>
</table>

<%	bp.closeFormWindow(pageContext); %>

</body>
</html>