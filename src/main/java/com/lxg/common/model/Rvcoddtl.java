package com.lxg.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 *
 * @author steph
 */
@Entity
@Table(name="rvcoddtl")
public class Rvcoddtl implements java.io.Serializable{
    private static final long serialVersionUID = 1L;
    private String trancode = "";
    private String subcode = "";
    private String description = "";

    @Id
    @Column(name="trancode")
    public String getTrancode() {
        return trancode;
    }

    public void setTrancode(String trancode) {
        this.trancode = trancode;
    }

    @Column(name="subcode")
    public String getSubcode() {
        return subcode;
    }

    public void setSubcode(String subcode) {
        this.subcode = subcode;
    }

    @Column(name="description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}