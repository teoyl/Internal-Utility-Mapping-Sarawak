<%-- 
    Document   : utility_provider_form
    Created on : Aug 12, 2025, 5:01:35 PM
    Author     : yonglai
--%>

<%@taglib uri="/struts-tags" prefix="s"%>

<s:set var="className">${param.className_}</s:set>

<style nonce='<s:property value="%{nonceVal}" />'>
    .select2-container--default .select2-selection--multiple .select2-selection__choice__display {
        padding-left: 20px;
    }
</style>

<div class="col-md-12">
    <div class="card mb-3">
        <div class="card-body">
            <h6 class="pb-1 border-bottom border-bottom-lg-1 text-uppercase fw-bold fs-1" data-anchor="data-anchor"><s:text name="utimaps.checklist.label.utilityProviders" /></h6>
            <s:hidden id="usjProviders%{className}" value="%{model.applicationModel.usj_providers}" escapeHtml="true" escapeJavaScript="true"/>
            <div class="row g-3 my-1 align-items-center">
                <div class="col-12 col-md-2 col-lg-2">
                    <label for="usj_providers<s:property value="%{className}"/>" class="col-form-label">Utility Providers</label>
                </div>
                <div class="col-12 col-md-6 col-lg-6">
                    <s:select 
                        id="usj_providers%{className}"
                        name="model.applicationModel.usj_providers"
                        value="%{model.applicationModel.usj_providers}"
                        list="utilityProvidersList"
                        listKey="keyData"
                        listValue="valueData"                    
                        cssClass="form-select font-sans-serif"
                        multiple="true"
                        disabled="%{disableUtilityProvider}"
                        />
                    <div class="invalid-feedback">Please select a valid Utility Service Providers.</div>
                </div>
            </div>
        </div>
    </div>
</div>

<script nonce="r4DjhKbfO5ry">
    $(document).ready(function () {
        $('#usj_providers<s:property value="%{className}"/>').select2({
            multiple: true,
            placeholder: "Select Utility Service Providers",
            allowClear: true
        });

        let providers = document.querySelector('#usjProviders<s:property value="%{className}"/>').value;
        if (providers) {
            let providersArr = providers.split(",");
            providersArr = providersArr.map(e => e.trim());
            console.log(providersArr)
            $('#usj_providers<s:property value="%{className}"/>').select2().val(providersArr).trigger('change');
        }
    
    });
</script>