package com.ureca.study.model.service;

import com.ureca.study.dto.Study;
import com.ureca.study.model.dao.StudyDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudyServiceImpl implements StudyService {

    @Autowired
    private StudyDAO studyDAO;

    @Override
    public boolean studyExists(String study_name) {
        return studyDAO.studyExists(study_name);
    }

    @Override
    public boolean validateStudyKey(String study_name, String study_key) {
        return studyDAO.validateStudyKey(study_name, study_key);
    }

    // Study 객체를 받도록 수정
    @Override
    public void createStudy(Study study) {
        studyDAO.createStudy(study);
    }
}