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
    	
        // 로그 추가: 데이터베이스에서 조회 시도
        System.out.println("Attempting to find study by name: " + study_name);
        
        Study study = sqlSession.selectOne(NAMESPACE + "getStudyByName", study_name);
        
        // 로그 추가: 조회 결과 확인
        if (study != null) {
            System.out.println("Found study: " + study.getStudy_name());
        } else {
            System.out.println("Study not found: " + study_name);
        }

        return study;
    }

    public void insertUserStudy(int user_id, int study_id) {
        var params = new HashMap<String, Object>();
        params.put("user_id", user_id);
        params.put("study_id", study_id);
        
        // 로그 추가: 삽입할 데이터 확인
        System.out.println("Inserting into UserStudy: user_id=" + user_id + ", study_id=" + study_id);

        sqlSession.insert(NAMESPACE + "insertUserStudy", params);

        // 삽입 완료 후 확인
        System.out.println("UserStudy entry added for user_id=" + user_id + ", study_id=" + study_id);
    }
    
    // 사용자와 스터디 간의 관계 확인
    public boolean isUserInStudy(int user_id, int study_id) {
        var params = new HashMap<String, Object>();
        params.put("user_id", user_id);
        params.put("study_id", study_id);
        return sqlSession.selectOne(NAMESPACE + "isUserInStudy", params);
    }
}
