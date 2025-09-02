package com.sains.framework.lookup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.struts2.ServletActionContext;
import com.opensymphony.xwork2.ActionContext;
import com.sains.common.util.Options;
import com.sains.common.util.Validator;
import com.sains.framework.base.BaseAction;
import com.sains.framework.base.Debug;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.text.StringEscapeUtils;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;


public class ItemChangeAction extends BaseAction {
    private String oriValue;
    public String getOriValue() {
        return oriValue;
    }
    public void setOriValue(String oriValue) {
        this.oriValue = oriValue;
    }
    
    
    private String itemValue;
    public String getItemValue() {
        return itemValue;
    }
    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }

    private String itemCate;
    public String getItemCate() {
        return itemCate;
    }
    public void setItemCate(String itemCate) {
        this.itemCate = itemCate;
    }
    
    private List itemChangeList = null;
    public List getItemChangeList() {
        return itemChangeList;
    }
    public void setItemChangeList(List itemChangeList) {
        this.itemChangeList = itemChangeList;
    }
    
    private void populateList(List<Map> listOfMap) {
        for (Map<String, Object> data : listOfMap) {
            itemChangeList.add(new Options(data.get("key_data").toString(), data.get("value_data").toString()));
        }
    }
    
    private String imageSrc = null;
    public String getImageSrc() {
        return imageSrc;
    }
    
    public String itemChange() throws Exception {
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        try {
            if (itemCate.equals("AppIcon")) { //refresh the application's icon
                itemValue = StringEscapeUtils.escapeEcmaScript(itemValue);
                return "appIcon";
            } else if (itemCate.equals("DivDis")) { //Division changed, reload the District List
                itemChangeList = new ArrayList();
                Debug.printFrameworkDebug("itemValue = " + itemValue);
                if (Validator.isEmpty(itemValue)) {
                    itemChangeList.add(new Options("", getText("pleaseSelect_div")));
                } else {
                    itemChangeList.add(new Options("", getText("pleaseSelect")));
                }
                if (!Validator.isEmpty(itemValue)) {
                    NativeQuery query = baseDAO.getSession().createNativeQuery("select district_id as key_data, district_name as value_data from t_sample_district where division_id = :pDivId");
                    query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
                    query.setParameter("pDivId", new Integer(itemValue));
                    populateList(query.list());
                }
                return "normalSelect";
            } else if (itemCate.equals("Image")) { //
                if (itemValue.equals("1")) {
                    imageSrc = "images/addToCart.jpg";
                } else if (itemValue.equals("2")) {
                    imageSrc = "images/empty.jpg";
                } else if (itemValue.equals("3")) {
                    imageSrc = "images/delivery.gif";
                }
                return "showImage";
            } else if (itemCate.equals("OC_OCLA")) { //

            } else {
                response.getWriter().append(getText("errors.unsupported_itemChange"));
                response.setCharacterEncoding("UTF-8");
                response.setContentType("text/html; charset=UTF-8");
                response.flushBuffer();
                return null;
            }
        } catch (Exception e) {
            response.setContentType("text/html; charset=UTF-8");
            response.flushBuffer();
        }
        return "";
    }
}
