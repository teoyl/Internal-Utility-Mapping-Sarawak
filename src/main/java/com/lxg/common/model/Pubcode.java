package com.lxg.common.model;

import com.sains.common.util.Validator;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
/**
 *
 * @author thensw
 */
@Entity
@Table(name="pubcode")
//@NamedQueries({
//    @NamedQuery(name = "pubcode.filterCode1",query = "from pubcode where code_type = :type and code_1 = :code1")
//})
public class Pubcode implements java.io.Serializable{
    private static final long serialVersionUID = 1L;
    private String rowid = "";
    private String code_type = "";
    private String code_1 = "";
    private String code_2 = "";
    private String code_3 = "";
    private String code_4 = "";
    private String code_acr = "";
    private String code_desc = "";
    private String bm_desc = "";
    private String keyCode = "code_1";
    private String valueCode = "code_desc";

    @Id
    @Column(name="rowid")
    public String getRowid() {
        return rowid;
    }

    public void setRowid(String rowid) {
        this.rowid = rowid;
    }
    
    @Column(name="code_1")
    public String getCode_1() {
        return code_1;
    }

    public void setCode_1(String code_1) {
        this.code_1 = code_1;
    }

    @Column(name="code_2")
    public String getCode_2() {
        return code_2;
    }

    public void setCode_2(String code_2) {
        this.code_2 = code_2;
    }

    @Column(name="code_3")
    public String getCode_3() {
        return code_3;
    }

    public void setCode_3(String code_3) {
        this.code_3 = code_3;
    }

    @Column(name="code_4")
    public String getCode_4() {
        return code_4;
    }

    public void setCode_4(String code_4) {
        this.code_4 = code_4;
    }

    @Column(name="code_acr")
    public String getCode_acr() {
        return code_acr;
    }

    public void setCode_acr(String code_acr) {
        this.code_acr = code_acr;
    }

    @Column(name="code_desc")
    public String getCode_desc() {
        return code_desc;
    }

    public void setCode_desc(String code_desc) {
        this.code_desc = code_desc;
    }

    @Column(name="bm_desc")
    public String getBm_desc() {
        return bm_desc;
    }

    public void setBm_desc(String bm_desc) {
        this.bm_desc = bm_desc;
    }

    @Column(name="code_type")
    public String getCode_type() {
        return code_type;
    }
    public void setCode_type(String code_type) {
        this.code_type = code_type;
    }


    @Transient
    public String getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(String keyCode) {
        this.keyCode = keyCode;
    }

    @Transient
    public String getValueCode() {
        return valueCode;
    }

    public void setValueCode(String valueCode) {
        this.valueCode = valueCode;
    }

    @Transient
    public String getKeyData(){
        if (keyCode.equals("code_1")){
            return code_1;
        } else if (keyCode.equals("code_2")){
            return code_2;
        } else if (keyCode.equals("code_3")){
            return code_3;
        } else if (keyCode.equals("code_4")){
            return code_4;
        } else if (keyCode.equals("code_acr")){
            return code_acr;
        }
        return code_1;
    }

    @Transient
    public String getValueData(){
        if (keyCode.equals("code_desc")){
            return code_desc;
        } else if (keyCode.equals("code_acr")){
            return code_acr;
        }
        return code_desc;
    }
    //added wongkk @ 7 mac 2012 for eSearch District to display Code2 and Description
    @Transient
    public String getCode2NDesc(){
        if(Validator.isEmpty(code_2)){
            return code_desc;
        }else{
            return code_2+" - "+code_desc;
        }
    }
    @Transient
    public String getCode1NDesc(){
        if(Validator.isEmpty(code_1)){
            return code_desc;
        }else{
            return code_1+" - "+code_desc;
        }
    }

    @Override
    public String toString(){
        return "code_type= " + code_type + ", code_1="+ code_1 + ", code_2="+ code_2 + ", code_3="+ code_3 + ", code_4="+ code_4 + ", code_acr="+ code_acr + ", code_desc="+ code_desc;
    }

    
}