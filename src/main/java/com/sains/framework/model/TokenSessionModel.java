package com.sains.framework.model;

import com.sains.framework.base.ModelBase;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
    @Table(name = "t_token_session")
    @NamedQueries({
    @NamedQuery(name = "TokenSessionModel.findBy_token", query = "SELECT a FROM TokenSessionModel a WHERE a.token = :token")})
    public class TokenSessionModel extends ModelBase implements java.io.Serializable {

        private String token_session_id;
        private String us_id;
        private String token;
        private Timestamp token_last_active_datetime;
        private String created_by;
        private Timestamp created_date;
        private String updated_by;
        private Timestamp updated_date;

        private final String[] updatableColumns = new String[] {"Token_session_id","Us_id","Token","Token_last_active_datetime"};


        private final String[] columnLength = new String[] {"us_id:20","token:100","token_description:200"};

        private Map columnLengthMap = null;


        public TokenSessionModel() {
            userDefined_autoValidation(Boolean.TRUE);
            userDefined_insertNoDuplicate("token;Token");
        }

        public TokenSessionModel(String token_session_id) {
            this.token_session_id = token_session_id;
        }

        @Id
        @Column(name = "token_session_id")
        public String getToken_session_id() {
            return token_session_id;
        }

        public void setToken_session_id(String token_session_id) {
            this.token_session_id = token_session_id;
        }

        @Transient 
        public String getID() { 
            if (token_session_id == null) {
                return null;
            }
            return token_session_id; 
        } 

        public void setID(String pk_id) {
            this.token_session_id = pk_id;
        }

        @Column(name = "us_id")
        public String getUs_id() {
            return us_id;
        }

        public void setUs_id(String us_id) {
            this.us_id = us_id;
        }

        @Transient
        public String[] getUpdatableColumns() {
            return updatableColumns;
        }

        @Column(name = "token")
        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        @Column(name = "token_last_active_datetime")
        public Timestamp getToken_last_active_datetime() {
            return token_last_active_datetime;
        }

        public void setToken_last_active_datetime(Timestamp token_last_active_datetime) {
            this.token_last_active_datetime = token_last_active_datetime;
        }

        @Column(name = "created_by")
        public String getCreated_by() {
            return created_by;
        }

        public void setCreated_by(String created_by) {
            this.created_by = created_by;
        }

        @Column(name = "created_date")
        public Timestamp getCreated_date() {
            return created_date;
        }

        public void setCreated_date(Timestamp created_date) {
            this.created_date = created_date;
        }

        @Column(name = "updated_by")
        public String getUpdated_by() {
            return updated_by;
        }

        public void setUpdated_by(String updated_by) {
            this.updated_by = updated_by;
        }

        @Column(name = "updated_date")
        public Timestamp getUpdated_date() {
            return updated_date;
        }

        public void setUpdated_date(Timestamp updated_date) {
            this.updated_date = updated_date;
        }

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
    }