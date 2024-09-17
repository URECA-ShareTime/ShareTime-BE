package com.ureca.study.model.service;

import com.ureca.study.dto.Study;

public interface StudyService {
    boolean studyExists(String study_name);

    boolean validateStudyKey(String study_name, String study_key);

    void createStudy(Study study);

    void addUserToStudy(int user_id, String study_name); // 추가된 메서드
}