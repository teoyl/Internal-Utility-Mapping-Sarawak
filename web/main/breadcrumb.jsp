<%@taglib uri="/struts-tags" prefix="s"%>

<style nonce="EuTVqS192VKl">
    nav {
        --falcon-breadcrumb-divider: '»';
    }
</style>

<s:if test='!(myBreadCrumb_==null ||myBreadCrumb_.equals(""))'>
    <nav aria-label="breadcrumb">
        <ol class="breadcrumb mb-3">
            <s:property value="%{myBreadCrumb_}" escapeHtml="false" />
        </ol>  
    </nav>
</s:if>
