package com.ureca.study.controller;
import com.ureca.study.dto.Study;
import com.ureca.study.model.service.StudyService;
import com.ureca.user.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("/study")
public class StudyController {
    @Autowired
    private StudyService studyService;
    @Autowired
    private JwtUtil jwtUtil; // JwtUtil 주입

    @PostMapping("/checkExistence")
    public Map<String, String> checkExistence(@RequestBody Map<String, String> request) {
        String study_name = request.get("study_name");
        boolean exists = studyService.studyExists(study_name);
        Map<String, String> response = new HashMap<>();
        response.put("exists", String.valueOf(exists));
        return response;
    }
    @PostMapping("/validateKey")
    public Map<String, String> validateKey(@RequestBody Map<String, String> request) {
        String study_name = request.get("study_name");
        String study_key = request.get("study_key");
        boolean isValid = studyService.validateStudyKey(study_name, study_key);
        Map<String, String> response = new HashMap<>();
        response.put("isValid", String.valueOf(isValid));
        return response;
    }
    @PostMapping("/create")
    public Map<String, String> createStudy(@RequestBody Study study) {
        studyService.createStudy(study);
        Map<String, String> response = new HashMap<>();
        response.put("message", "스터디가 생성되었습니다.");
        return response;
    }

    @PostMapping("/joinOrCreate")
    public Map<String, String> joinOrCreateStudy(
            @RequestBody Map<String, String> request,
            @RequestHeader("Authorization") String token) {
        Map<String, String> response = new HashMap<>();
        String study_name = request.get("study_name");
        String study_key = request.get("study_key");
        System.out.println("Received study_name: " + study_name + ", study_key: " + study_key);

        String extractedToken = token.replace("Bearer ", "");
        Integer user_id;
        try {
            user_id = jwtUtil.extractUserId(extractedToken);
            if (user_id == null) {
                response.put("message", "INVALID_TOKEN");
                return response;
            }
        } catch (Exception e) {
            response.put("message", "TOKEN_ERROR");
            response.put("error", e.getMessage());
            return response;
        }

        try {
            if (studyService.studyExists(study_name)) {
                boolean isValid = studyService.validateStudyKey(study_name, study_key);
                if (isValid) {
                    try {
                        studyService.addUserToStudy(user_id, study_name);
                        response.put("message", "JOIN_SUCCESS");
                    } catch (IllegalArgumentException e) {
                        response.put("message", "ALREADY_JOINED");
                        response.put("error", e.getMessage());
                    }
                } else {
                    response.put("message", "INVALID_KEY");
                }
            } else {
                Study study = new Study();
                study.setStudy_name(study_name);
                study.setStudy_key(study_key);
                studyService.createStudy(study);
                studyService.addUserToStudy(user_id, study_name);
                response.put("message", "CREATED");
            }
        } catch (Exception e) {
            response.put("message", "ERROR");
            response.put("error", e.getMessage());
            e.printStackTrace();
        }
        return response;
    }
}