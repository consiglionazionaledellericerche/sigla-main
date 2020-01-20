<%@ page pageEncoding="UTF-8"
	import = "it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.ordmag.anag00.*"
%>

<% 
SimpleCRUDBP bp = (SimpleCRUDBP)BusinessProcess.getBusinessProcess(request);
%>
<div class="Group card p-2 m-1">
	<table class="w-100" cellpadding="2">
		<tr>				
			<td>
				<% bp.writeFormField(out, "findTipoMovimentoMagCarMag");%>
			</td>
		</tr>
		<tr>
            <td>
                <% bp.writeFormField(out, "findTipoMovimentoMagCarTra");%>
            </td>
        </tr>
		<tr>
            <td>
                <% bp.writeFormField(out, "findTipoMovimentoMagCarFma");%>
            </td>
        </tr>
		<tr>
            <td>
                <% bp.writeFormField(out, "findTipoMovimentoMagScaUo");%>
            </td>
        </tr>
		<tr>
            <td>
                <% bp.writeFormField(out, "findTipoMovimentoMagTraSca");%>
            </td>
        </tr>
        <tr>
            <td>
                <% bp.writeFormField(out, "findTipoMovimentoMagTraCar");%>
            </td>
        </tr>

		<tr>
            <td>
                <% bp.writeFormField(out, "findTipoMovimentoMagRvPos");%>
            </td>
        </tr>
		<tr>
            <td>
                <% bp.writeFormField(out, "findTipoMovimentoMagRvNeg");%>
            </td>
        </tr>
		<tr>
            <td>
                <% bp.writeFormField(out, "findTipoMovimentoMagChi");%>
            </td>
        </tr>
	</table>
</div>