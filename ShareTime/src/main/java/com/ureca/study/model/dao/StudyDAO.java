package com.ureca.study.model.dao;

import com.ureca.study.dto.Study;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public class StudyDAO {

    @Autowired
    private SqlSession sqlSession;

    private static final String NAMESPACE = "com.ureca.study.model.dao.StudyDAO.";

    public boolean studyExists(String study_name) {
        return sqlSession.selectOne(NAMESPACE + "studyExists", study_name);
    }

    public boolean validateStudyKey(String study_name, String study_key) {
        var params = new HashMap<String, Object>();
        params.put("study_name", study_name);
        params.put("study_key", study_key);
        return sqlSession.selectOne(NAMESPACE + "validateStudyKey", params);
    }

    public void createStudy(Study study) {
        sqlSession.insert(NAMESPACE + "createStudy", study);
    }

    public Study getStudyByName(String study_name) {
        return sqlSession.selectOne(NAMESPACE + "getStudyByName", study_name);
    }

    public void insertUserStudy(int user_id, int study_id) {
        var params = new HashMap<String, Object>();
        params.put("user_id", user_id);
        params.put("study_id", study_id);
        sqlSession.insert(NAMESPACE + "insertUserStudy", params);
    }
}