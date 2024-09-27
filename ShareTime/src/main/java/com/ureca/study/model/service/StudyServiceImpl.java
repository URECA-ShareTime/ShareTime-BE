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

    @Override
    public void createStudy(Study study) {
        studyDAO.createStudy(study);
    }

    @Override
    public void addUserToStudy(int user_id, String study_name) {
        String trimmedStudyName = study_name.trim().toLowerCase();
        System.out.println("Attempting to find study by name: " + trimmedStudyName);

        Study study = studyDAO.getStudyByName(trimmedStudyName);
        if (study != null) {
            System.out.println("Found study: " + study.getStudy_name());

            // 사용자가 이미 해당 스터디에 참여했는지 확인
            boolean isUserAlreadyInStudy = studyDAO.isUserInStudy(user_id, study.getStudy_id());
            if (isUserAlreadyInStudy) {
                // 이미 참여한 경우 예외를 던지는 대신 적절한 처리
                throw new IllegalArgumentException("이미 참여한 스터디입니다.");
            }

            studyDAO.insertUserStudy(user_id, study.getStudy_id());
            System.out.println("UserStudy entry added for user_id=" + user_id + ", study_id=" + study.getStudy_id());
        } else {
            System.out.println("Study not found: " + trimmedStudyName);
        }
    }
}