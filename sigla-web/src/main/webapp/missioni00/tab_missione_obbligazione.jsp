<%@ page pageEncoding="UTF-8"  import = "it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*, it.cnr.jada.util.action.*, it.cnr.contab.missioni00.bp.*, it.cnr.contab.missioni00.docs.bulk.*, it.cnr.jada.bulk.*, it.cnr.contab.docamm00.bp.*" %>

<%  
    CRUDMissioneBP bp = (CRUDMissioneBP)BusinessProcess.getBusinessProcess(request);
    MissioneBulk missione = (MissioneBulk) bp.getModel();

    if(missione == null)
        missione = new MissioneBulk();
%>

<div class="Group card p-2" style="width:100%">
    <table>
     <% if (bp.isSearching())
        {
          boolean isInSpesaMode = (bp instanceof IDocumentoAmministrativoSpesaBP && ((IDocumentoAmministrativoSpesaBP)bp).isSpesaBP()) ? true : false; %>
            <tr>
            <td><% bp.getController().writeFormLabel(out,"stato_pagamento_fondo_ecoForSearch");%></td>
            <td><% bp.getController().writeFormInput(out,null,"stato_pagamento_fondo_ecoForSearch", isInSpesaMode, null,""); %></td>
            </tr>

      <% } else { %>

            <tr>
            <td><% bp.getController().writeFormLabel(out,"stato_pagamento_fondo_eco");%></td>
            <td><% bp.getController().writeFormInput(out,null,"stato_pagamento_fondo_eco",false,null,"onChange=\"submitForm('doDefault')\"");%></td>
            </tr>

            <tr>
            <td><% bp.getController().writeFormLabel(out,"dt_pagamento_fondo_eco"); %></td>
            <td><% bp.getController().writeFormInput(out,"dt_pagamento_fondo_eco"); %></td>
            </tr>
      <% } %>
    </table>
</div>

<fieldset class="mt-2">
    <legend ACCESSKEY=G TABINDEX=1 style="font-weight:bold; font-family:sans-serif; font-size:12px; color:blue"><span class="h4 text-info ml-2">Compenso</span></legend>
    <div class="Panel card p-2" style="width:100%">
        <table width="100%">
            <tr>
                <td><% bp.getController().writeFormLabel( out, "pg_compenso"); %></td>
                <td><% bp.getController().writeFormInput( out, "pg_compenso"); %></td>
                <td><% bp.getController().writeFormLabel( out, "esercizio_compenso"); %></td>
                <td><% bp.getController().writeFormInput( out, "esercizio_compenso"); %></td>
            </tr>

            <tr>
                <td><% bp.getController().writeFormLabel( out, "cd_uo_compenso"); %></td>
                <td><% bp.getController().writeFormInput( out, "cd_uo_compenso"); %></td>
                <td><% bp.getController().writeFormLabel( out, "cd_cds_compenso"); %></td>
                <td><% bp.getController().writeFormInput( out, "cd_cds_compenso"); %></td>
            </tr>
        </table>
        <table width="100%" class="mt-2">
            <tr>
                <td ALIGN="CENTER">
                    <% JSPUtils.button(out,
                        bp.getParentRoot().isBootstrap()?"fa fa-fw fa-eye":null,
                        bp.getParentRoot().isBootstrap()?"fa fa-fw fa-eye":null,
                        "Visualizza compenso",
                        "javascript:submitForm('doVisualizzaCompenso')",
                        "btn-outline-primary btn-title",
                        true,
                        bp.getParentRoot().isBootstrap()); %>
                </td>
            </tr>
        </table>
    </div>
</fieldset>

<fieldset class="mt-2">
<legend ACCESSKEY=G TABINDEX=1 style="font-weight:bold; font-family:sans-serif; font-size:12px; color:blue"><span class="h4 text-info ml-2">Anticipo</span></legend>
    <div class="Panel card p-2" style="width:100%">
        <table width="100%">
            <tr>
                <td><% bp.getController().writeFormLabel( out, "pg_anticipo"); %></td>
                <td><% bp.getController().writeFormInput(out,null,"find_anticipo", bp.areCampiAnticipoReadonly(),null,""); %></td>
                <td><% bp.getController().writeFormLabel(out, "im_anticipo");%></td>
                <td><% bp.getController().writeFormInput(out, "im_anticipo");%></td>
            </tr>
            <tr>
                <td><% bp.getController().writeFormLabel(out, "esercizio_anticipo");%></td>
                <td><% bp.getController().writeFormInput(out, "esercizio_anticipo");%></td>
                <td><% bp.getController().writeFormLabel(out, "cd_cds_anticipo");%></td>
                <td><% bp.getController().writeFormInput(out, "cd_cds_anticipo");%></td>
                <td><% bp.getController().writeFormLabel(out, "cd_uo_anticipo");%></td>
                <td><% bp.getController().writeFormInput(out, "cd_uo_anticipo");%></td>
            </tr>
        </table>
        <table width="100%" class="mt-2">
            <tr>
            <td ALIGN="CENTER">
                <% JSPUtils.button(out,
                    bp.getParentRoot().isBootstrap()?"fa fa-fw fa-eye": null,
                    bp.getParentRoot().isBootstrap()?"fa fa-fw fa-eye": null,
                    "Visualizza anticipo",
                    "javascript:submitForm('doVisualizzaAnticipo')",
                    "btn-outline-primary btn-title",
                    true,
                    bp.getParentRoot().isBootstrap()); %>
            </td>
            </tr>
        </table>
    </div>
</fieldset>

<fieldset class="mt-2">
    <legend ACCESSKEY=G TABINDEX=1 style="font-weight:bold; font-family:sans-serif; font-size:12px; color:blue"><span class="h4 text-info ml-2">Scadenze Impegno<span></legend>
	<div class="Group card">
	<% bp.getMissioneRigheController().writeHTMLTable(pageContext,"default",true,false,true,"100%","150px",true); %>
	<table width="100%">
	  <tr><td width="70%">
		<div class="card">
			<fieldset class="fieldset mb-2">
			<legend class="GroupLabel card-header text-primary">Scadenza</legend>
			<table class="m-2 p-2">
			  <tr>
				<% bp.getMissioneRigheController().writeFormField(out,"cd_cds_obbligazione"); %>
			  </tr>
			  <tr>
				<% bp.getMissioneRigheController().writeFormField(out,"esercizio_ori_obbligazione"); %>
			  </tr>
			  <tr>
				<% bp.getMissioneRigheController().writeFormField(out,"pg_obbligazione"); %>
			  </tr>
			  <tr>
				<% bp.getMissioneRigheController().writeFormField(out,"pg_obbligazione_scadenzario"); %>
			  </tr>
			  <tr>
				<% bp.getMissioneRigheController().writeFormField(out,"scadenza_dt_scadenza"); %>
			  </tr>
			  <tr>
				<% bp.getMissioneRigheController().writeFormField(out,"scadenza_im_scadenza"); %>
			  </tr>
			  <tr>
				<% bp.getMissioneRigheController().writeFormField(out,"im_totale_riga_missione"); %>
			  </tr>
			  <tr>
				<% bp.getMissioneRigheController().writeFormField(out,"scadenza_ds_scadenza"); %>
			  </tr>
			  <tr>
				<% bp.getMissioneRigheController().writeFormField(out,"cig"); %>
			  </tr>
			</table>
			</fieldset>
		</div>
	  </td>
	  <td class="align-top">
		<div class="card">
			<fieldset class="fieldset mb-2">
			<legend class="GroupLabel card-header text-primary">Riepilogo</legend>
			<table class="m-2 p-2">
			  <tr>         
			    <td><span class="FormLabel">Missione</span></td>
			    <td><% bp.getController().writeFormInput(out,"im_totale_missione");%></td>
				<td>-</td>
			  </tr>                     	
			  <tr>         
			    <td><span class="FormLabel">Anticipi</span></td>
			    <td><% bp.getController().writeFormInput(out,"im_anticipo");%></td>
				<td>-</td>
			  </tr>                     	
			  <tr>         
			    <td><span class="FormLabel">Impegnato</span></td>
			    <td><% bp.getController().writeFormInput(out,"im_totale_impegnato");%></td>
				<td>=</td>
			  </tr>                     	
			  <tr>         
			    <td><span class="FormLabel" style="color:red">da Impegnare</span></td>
			    <td><% bp.getController().writeFormInput(out,"im_totale_da_impegnare");%></td>
			    <td colspan=4>&nbsp;</td>
			  </tr>                     	
			</table>
			</fieldset>
		</div>
	  </td></tr>
	</table>	
	</div>
</fieldset>
