<%@ page 
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*, it.cnr.jada.util.action.*, it.cnr.contab.doccont00.bp.*,it.cnr.contab.doccont00.action.*, it.cnr.contab.doccont00.core.bulk.*,
			it.cnr.contab.doccont00.core.bulk.*"
%>
<%  
	CRUDAccertamentoBP bp = (CRUDAccertamentoBP)BusinessProcess.getBusinessProcess(request);
	SimpleDetailCRUDController controller = bp.getPdgVincoli();
   	controller.writeHTMLTable(pageContext,"pdgAccertamentoVincolo",true,false,true,"100%","200px"); %>

	<div class="Group" style="width:100%;padding:0px">
	<table class="Panel">
  	  <TR>
  	  	<TD><% controller.writeFormLabel(out,"esercizio_res");%></TD>
  	  	<TD colspan="2"><% controller.writeFormInput(out,"default","esercizio_res",false,null,null); %></TD>
	  	<TD style="float:right; text-align: -webkit-center">
			<div class="GroupLabel">Disponibilità Spesa</div>  
			<div class="Group">
				<% controller.writeFormInput(out,"findAssestato");%>
			</div>
		</TD>
	  </TR>
		<tr>
			<td><% controller.writeFormLabel(out,"cd_centro_responsabilita");%></td>
			<td colspan=3>
				<% controller.writeFormInput(out,"cd_centro_responsabilita");%>
				<% controller.writeFormInput(out,"ds_centro_responsabilita");%>
			</td>
		</tr>
		<tr>
			<td><% controller.writeFormLabel(out,"cd_linea_attivita");%></td>
			<td colspan=3>
				<% controller.writeFormInput(out,"cd_linea_attivita");%>
				<% controller.writeFormInput(out,"ds_linea_attivita");%>
			</td>
		</tr>
		<tr>
			<td><% controller.writeFormLabel(out,"cd_elemento_voce");%></td>
			<td colspan=3>
				<% controller.writeFormInput(out,"cd_elemento_voce");%>
				<% controller.writeFormInput(out,"ds_elemento_voce");%>
			</td>
		</tr>

  	  <TR><TD>
	  	<% controller.writeFormLabel(out,"im_vincolo");%>
	  	</TD><TD colspan="3">
	    <% controller.writeFormInput(out,"im_vincolo"); %>
	  </TD></TR>
	</table>
	</div>