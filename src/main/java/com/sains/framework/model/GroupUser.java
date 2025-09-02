package com.sains.framework.model;

import com.sains.framework.base.ModelBase;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.JoinColumn;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="t_setup_group_user")
public class GroupUser extends ModelBase implements java.io.Serializable{
    private static final long serialVersionUID = 1L;

    private String ug_user_id = "";
    private String ug_id = "";
    private String us_id = ""; //for User's PK is varchar
//	private Integer us_id = null; //for User's PK is auto-increment

    private User groupUser;
    private String created_by;
    private SetupGroup userGroup;
    private java.sql.Timestamp created_date;
    private String updated_by;
    private java.sql.Timestamp updated_date;

    //for workflow round robin user
    private String next_action_officer = "";
    private Integer seq ;
    private String on_duty = "Y";

    @Id
    @Column(name="ug_user_id")
    public String getUg_user_id() {
            return ug_user_id;
    }
    public void setUg_user_id(String ugUserId) {
            ug_user_id = ugUserId;
    }

    @Transient
    public String getID() {
            return getUg_user_id();
    }

    public void setID(String ugUserId) {
            setUg_user_id(ugUserId);
    }	

    @Column(name="ug_id")
    public String getUg_id() {
            return ug_id;
    }
    public void setUg_id(String ugId) {
            ug_id = ugId;
    }

    /** for User's PK is varchar : START **/
    @Column(name="us_id")
    public String getUs_id() {
            return us_id;
    }
    public void setUs_id(String usId) {
            this.us_id = usId;
    }
    /** for User's PK is varchar : END **/

    /** for User's PK is auto-increment :START **/
//	@Column(name="us_id")
//	public Integer getUs_id() {
//		return us_id;
//	}
//	public void setUs_id(Integer usId) {
//		this.us_id = usId;
//	}
    /** for User's PK is auto-increment :END **/

    /*@ManyToOne(targetEntity=Module.class, optional=true, fetch=FetchType.LAZY)
    @JoinColumn(name="attached_module_id", insertable=false, updatable=false, nullable = true)
    public Module getAttachedModule() {
            return attachedModule;
    }
    public void setAttachedModule(Module attachedModule) {
            this.attachedModule = attachedModule;
    }*/

    @Column(name="created_by")
    public String getCreated_by() {
            return created_by;
    }
    public void setCreated_by(String createdBy) {
            created_by = createdBy;
    }

    @Column(name="created_date")
    public java.util.Date getCreated_date() {
            return created_date;
    }
    public void setCreated_date(java.sql.Timestamp createdDate) {
            created_date = createdDate;
    }

    @OneToOne(targetEntity=User.class, optional=true, fetch=FetchType.EAGER)
    @JoinColumn(name="us_id", insertable=false, updatable=false, nullable = true)
    public User getGroupUser() {
            return groupUser;
    }
    public void setGroupUser(User groupUser) {
            this.groupUser = groupUser;
    }

    @OneToOne(targetEntity=SetupGroup.class, optional=true, fetch=FetchType.LAZY)
    @JoinColumn(name="ug_id", insertable=false, updatable=false, nullable = true)
    public SetupGroup getUserGroup() {
            return userGroup;
    }
    public void setUserGroup(SetupGroup userGroup) {
            this.userGroup = userGroup;
    }

    @Column(name="updated_by")
    public String getUpdated_by() {
            return updated_by;
    }
    public void setUpdated_by(String updatedBy) {
            updated_by = updatedBy;
    }

    @Column(name="updated_date")
    public java.util.Date getUpdated_date() {
            return updated_date;
    }
    public void setUpdated_date(java.sql.Timestamp updatedDate) {
            updated_date = updatedDate;
    }
    
    @Column(name="next_action_officer")
    public String getNext_action_officer() {
        return next_action_officer;
    }

    public void setNext_action_officer(String next_action_officer) {
        this.next_action_officer = next_action_officer;
    }

    @Column(name="seq")
    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    @Column(name="on_duty")
    public String getOn_duty() {
        return on_duty;
    }

    public void setOn_duty(String on_duty) {
        this.on_duty = on_duty;
    }

    public String[] updatableColumns = new String[]{"Ug_user_id", "Next_action_officer", "Seq", "On_duty"};

    @Transient
    public String[] getUpdatableColumns() {
        return null;
    }
    
    private final String[] columnLength = new String[]{};
    private Map columnLengthMap = null;
    @Transient
    public Map getColumnLengthMap() {
        if (columnLengthMap == null) {
            columnLengthMap = new HashMap();
            String[] strA = null;

            for (String str : columnLength) {
                strA = str.split(":");
                columnLengthMap.put(strA[0], strA[1]);
            }
        }

        return columnLengthMap;
    }
    
    private User userModel = null;
    @OneToOne(targetEntity = User.class, optional = true, fetch = FetchType.LAZY) // ai min @ 9.6.2011
    @JoinColumn(name = "us_id", insertable = false, updatable = false, nullable = true)
    public User getUserModel() {
        return userModel;
    }

    public void setUserModel(User userModel) {
        this.userModel = userModel;
    }
}