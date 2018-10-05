<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.progettiric00.core.bulk.*,
		it.cnr.contab.prevent01.action.*,
		it.cnr.contab.prevent01.bp.*,
		it.cnr.contab.prevent01.bulk.*"
%>
<%
	CRUDPdg_Modulo_EntrateBP bp = (CRUDPdg_Modulo_EntrateBP)BusinessProcess.getBusinessProcess(request);
	Progetto_sipBulk progetto = ((Pdg_moduloBulk)bp.getModel()).getProgetto();
%>
<head>
<% if (bp.getParametriCnr()!=null && bp.getParametriCnr().getFl_nuovo_pdg()) { %>
	<title>Approvvigionamento delle risorse per Progetto</title>
<% } else { %>
	<title>Approvvigionamento delle risorse per Modulo di Commessa</title>
<% } %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
</head>
<body class="Form">
<%
	bp.openFormWindow(pageContext);
%>
<div class="Group card">
 <table>
    <tr>
		<% bp.getController().writeFormField(out,"esercizio_ro"); %>
	</tr>
	<tr>
	    <td><% bp.getController().writeFormLabel(out,"cdr_ro");%></td>
	    <td class="w-100" colspan="7">
	        <div class="input-group input-group-searchtool w-100">
	            <% bp.getController().writeFormInput(out,"cdr_ro");%>
	            <% bp.getController().writeFormInput(out,"ds_centro_responsabilita");%>
	        </div>
	    </td>
	</tr>
	<tr>
	<% if (bp.getParametriCnr().getFl_nuovo_pdg()) { %>
		<td NOWRAP><% bp.getController().writeFormLabel( out, "cd_progetto_liv1"); %></td>	
		<td><% bp.getController().writeFormInput( out, "cd_progetto_liv1"); %></td>
		<td NOWRAP><% bp.getController().writeFormLabel( out, "cd_progetto_liv2"); %></td>	
		<td><% bp.getController().writeFormInput( out, "cd_progetto_liv2"); %></td>
		<td NOWRAP><% bp.getController().writeFormLabel( out, "cd_dipartimento_liv2"); %></td>	
		<td><% bp.getController().writeFormInput( out, "cd_dipartimento_liv2"); %></td>
	<% } else { %>
		<% bp.getController().writeFormField(out,"modulo"); %>
	 	<% bp.getController().writeFormField(out,"commessa"); %>
	 	<% bp.getController().writeFormField(out,"progetto"); %>
	 	<% bp.getController().writeFormField(out,"dipartimento"); %>
	<% } %>
	</tr>
	<% if (bp.getParametriCnr().getFl_nuovo_pdg()) { %>
		<tr>
			<% bp.getController().writeFormField(out,"importo_totale_liv2"); %>
		</tr>
	<% } else { %>	
		<tr>
			<% bp.getController().writeFormField(out,"importo_totale"); %>	
		</tr>	
	<% } %>	
	</table>
</div>	
<% bp.getCrudDettagliEntrate().writeHTMLTable(
		pageContext,
		bp.getParametriCnr().getFl_nuovo_pdg()?"without_area":"default",	
		true,
		false,
		true,
		"100%",
		"200px");
%>
<div class="Group card">
<table class="w-100">
	<tr>
		<% if (bp.getParametriCnr().getFl_pdg_codlast()) { %> 
			<% bp.getCrudDettagliEntrate().writeFormField(out,"find_classificazione_voci_codlast"); %>
		<% } else { %>
			<% bp.getCrudDettagliEntrate().writeFormField(out,"find_classificazione_voci"); %>
		<% } %>
		<% bp.getCrudDettagliEntrate().writeFormField(out,"natura"); %>
	</tr>	
	<tr>
		<td>
			<%bp.getCrudDettagliEntrate().writeFormLabel(out,"find_contraente"); %>
		</td>
		<td>	
			<%bp.getCrudDettagliEntrate().writeFormInput(out,null,"find_contraente",bp.isUtente_Ente(),null,null); %>
		</td>
	</tr>		
	<tr>
		<td>
			<%bp.getCrudDettagliEntrate().writeFormLabel(out,"ds_dettaglio"); %>
		</td>
		<td colspan="3">
			<%bp.getCrudDettagliEntrate().writeFormInput(out,null,"ds_dettaglio",bp.isUtente_Ente(),null,null); %>
		</td>
	</tr>		
	<tr>
		<td>
			<%bp.getCrudDettagliEntrate().writeFormLabel(out,"im_entrata_tot"); %>
		</td>
		<td>
			<span class="d-flex justify-content-between">
				<%bp.getCrudDettagliEntrate().writeFormInput(out,null,"im_entrata_tot",bp.isUtente_Ente(),null,null); %>
				<span class="form-inline">
					<span class="pr-2"><%bp.getCrudDettagliEntrate().writeFormLabel(out,"esercizio_inizio"); %></span>
					<%bp.getCrudDettagliEntrate().writeFormInput(out,null,"esercizio_inizio",bp.isUtente_Ente(),null,null); %>
				</span>
				<span class="form-inline">
					<span class="pr-2"><%bp.getCrudDettagliEntrate().writeFormLabel(out,"esercizio_fine"); %></span>
					<%bp.getCrudDettagliEntrate().writeFormInput(out,null,"esercizio_fine",bp.isUtente_Ente(),null,null); %>
				</span>
			</span>
		</td>
	</tr>	
	<tr>
		<td>
		</td>
    	<td>
		  <span class="FormLabel font-weight-bold text-uppercase text-primary" style="color:black">Proposta</span>
		</td>	
    </tr>
	<tr>
		<td>	
			<%bp.getCrudDettagliEntrate().writeFormLabel(out,"im_entrata"); %>
		</td>
		<td class="d-flex justify-content-between">
			<%bp.getCrudDettagliEntrate().writeFormInput(out,null,"im_entrata",bp.isUtente_Ente(),null,null); %>
			<span class="form-inline">
				<span class="pr-2"><%bp.getCrudDettagliEntrate().writeFormLabel(out,"im_entrata_app"); %></span>
				<span><%bp.getCrudDettagliEntrate().writeFormInput(out,null,"im_entrata_app",!bp.isUtente_Ente(),null,null); %></span>
			</span>
		</td>
	</tr>	
	<tr>
		<td>	
			<%bp.getCrudDettagliEntrate().writeFormLabel(out,"im_entrata_a2"); %>
		</td>
		<td>				
			<%bp.getCrudDettagliEntrate().writeFormInput(out,null,"im_entrata_a2",bp.isUtente_Ente(),null,null); %>
		</td>
	</tr>	
	
	<tr>
		<td>	
			<%bp.getCrudDettagliEntrate().writeFormLabel(out,"im_entrata_a3"); %>				
		</td>
		<td>	
			<%bp.getCrudDettagliEntrate().writeFormInput(out,null,"im_entrata_a3",bp.isUtente_Ente(),null,null); %>
		</td>
	</tr>	
		
	
	<tr>
		<td>	
			<%bp.getCrudDettagliEntrate().writeFormLabel(out,"im_spese_vive"); %>
		</td>
		<td colspan="3">
			<table class="w-100">
				<td>	
					<%bp.getCrudDettagliEntrate().writeFormInput(out,null,"im_spese_vive",bp.isUtente_Ente(),null,null); %>
				</td>	
				<td>
					<%bp.getCrudDettagliEntrate().writeFormLabel(out,"ds_spese_vive"); %>				
				</td>	
				<td>
					<%bp.getCrudDettagliEntrate().writeFormInput(out,null,"ds_spese_vive",bp.isUtente_Ente(),null,null); %>
				</td>
			</table>
		</td>	
	</tr>
<% if (!bp.getParametriCnr().getFl_nuovo_pdg()){%>
	<tr>
		<td>			
			<%bp.getCrudDettagliEntrate().writeFormLabel(out,"area"); %>				
		</td>	
		<td>	
			<%bp.getCrudDettagliEntrate().writeFormInput(out,null,"area",bp.isUtente_Ente(),null,null); %>
		</td>	
	</tr>
<% } %>
</table>
</div>
<%	bp.closeFormWindow(pageContext); %>
</body>










