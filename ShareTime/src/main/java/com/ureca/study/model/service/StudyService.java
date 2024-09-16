package com.ureca.study.model.service;

import com.ureca.study.dto.Study;

public interface StudyService {
    boolean studyExists(String study_name);

    boolean validateStudyKey(String study_name, String study_key);

    // Study 객체를 받도록 수정
    void createStudy(Study study);
}