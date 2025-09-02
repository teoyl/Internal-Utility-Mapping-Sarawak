package com.lxg.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
/**
 *
 * @author thensw
 */
@Entity
@Table(name="RVRCODE")
public class Rvrcode implements java.io.Serializable{
    private static final long serialVersionUID = 1L;
//    private String rowid = "";
    private String trancode = "";
    private String head = "";
    private String sub_head = "";
    private String description = "";

    @Column(name="description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name="head")
    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

//    @Id
//    @Column(name="rowid")
//    public String getRowid() {
//        return rowid;
//    }
//
//    public void setRowid(String rowid) {
//        this.rowid = rowid;
//    }

    @Column(name="sub_head")
    public String getSub_head() {
        return sub_head;
    }

    public void setSub_head(String sub_head) {
        this.sub_head = sub_head;
    }

    @Id
    @Column(name="trancode")
    public String getTrancode() {
        return trancode;
    }

    public void setTrancode(String trancode) {
        this.trancode = trancode;
    }

    @Override
    public String toString(){
        return "trancode= " + trancode + ", head="+ head + ", sub_head="+ sub_head + ", description="+ description;
    }

}