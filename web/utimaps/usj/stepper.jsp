<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<style nonce="EuTVqS192VKl">
    .theme-wizard .nav-link.deemed .nav-item-circle {
    background-color: #b0dce0!important;
}

.theme-wizard .nav-link.active .nav-item-circle {
    border-color :  #00ae65 !important;
    background-color:  #00ae65 !important;
    color: #fff;
}

/** fix the css on milestone step line color**/
.theme-wizard .nav-item:first-child .active .nav-item-circle-parent:after {
    content: "";
    width: 100%;
    position: absolute;
    left: 50%;
    top: 50%;
    -webkit-transform: translateY(-50%);
    -ms-transform: translateY(-50%);
    transform: translateY(-50%);
    height: 2px;
    background-color: var(--falcon-success)!important;
}

.theme-wizard .nav-item:not(:first-child) .active .nav-item-circle-parent:after {
    content: "";
    width: 100%;
    position: absolute;
    left: -1px;
    top: 50%;
    -webkit-transform: translateY(-50%);
    -ms-transform: translateY(-50%);
    transform: translateY(-50%);
    height: 2px;
    background-color: var(--falcon-success)!important;
}


.theme-wizard .nav-item:first-child .deemed .nav-item-circle-parent:after {
    content: "";
/*    width: 100%;*/
    position: absolute;
/*    left: 50%;
    top: 50%;*/
    -webkit-transform: translateY(-50%);
    -ms-transform: translateY(-50%);
    transform: translateY(-50%);
    height: 2px;
/*    background-color: var(--falcon-success)!important;*/
}


.theme-wizard .nav-item:not(:first-child) .deemed .nav-item-circle-parent:after {
    content: "";
    width: 50%;
    position: absolute;
    left: -1px;
    top: 50%;
    -webkit-transform: translateY(-50%);
    -ms-transform: translateY(-50%);
    transform: translateY(-50%);
    height: 2px;
    background-color: var(--falcon-success)!important;
}

/*** end **/
.theme-wizard .license-nav .nav-link .timeline-text-color{
    color: #000;
    font-size: x-small !important;
}

.theme-wizard .license-nav .nav-link .timeline-text-status{
    color: #748194;
    font-size: xx-small !important;
}
</style>

<%@taglib uri="/struts-tags" prefix="s"%>
<ul class="nav justify-content-between nav-wizard license-nav">
    <s:iterator value="lsEntryStep" status="itemStatus" var="item">
        <s:set var="mode" value="%{item.mode}" />
            <%--<li class="nav-item"><a class="nav-link active fw-semi-bold" href="<s:property value="%{#item.url}"/>" ><span class="nav-item-circle-parent"><span class="nav-item-circle"><s:property value="%{#itemStatus.index + 1}"/></span></span><span class="d-none d-md-block mt-1 fs--1"><s:property value="%{#item.name}"/></span></a></li>--%>
            <li class="nav-item">
                <a class="nav-link ${mode == 'active' ? 'active' : mode == 'deemed' ? 'deemed' : '' } fw-semi-bold" href="#">
                    <span class="nav-item-circle-parent">
                        <span class="nav-item-circle"><s:property value="%{#itemStatus.index + 1}"/></span>
                    </span>
                    <span class="d-none d-md-block mt-1 fs--1 timeline-text-color"><s:property value="%{#item.name}"/></span>
                    <span class="d-none d-md-block mt-1 fs--1 timeline-text-status">${mode == 'deemed' ? 'Processing ...': ''}</span>
                </a>
            </li>
     
    </s:iterator>
</ul>

