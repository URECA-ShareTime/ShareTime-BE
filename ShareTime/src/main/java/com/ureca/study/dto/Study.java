package com.ureca.study.dto;

import java.sql.Timestamp;

public class Study {
    private int study_id; // 스터디 그룹 고유 ID
    private String study_name; // 스터디 그룹 이름
    private String study_key; // 스터디 그룹 키
    private Timestamp created_at; // 생성일
    private Timestamp updated_at; // 수정일

    // getter와 setter 메서드
    public int getStudy_id() {
        return study_id;
    }

    public void setStudy_id(int study_id) {
        this.study_id = study_id;
    }

    public String getStudy_name() {
        return study_name;
    }

    public void setStudy_name(String study_name) {
        this.study_name = study_name;
    }

    public String getStudy_key() {
        return study_key;
    }

    public void setStudy_key(String study_key) {
        this.study_key = study_key;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }
}