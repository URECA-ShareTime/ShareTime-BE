package com.ureca.user.controller;

import com.ureca.user.util.FileUploadUtil;
import com.ureca.user.util.JwtUtil;
import com.ureca.user.dto.User;
import com.ureca.user.model.service.UserService;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.nio.file.Files;
import java.nio.file.Paths; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;
    
    @Autowired
    private JwtUtil jwtUtil;

    @Value("${file.upload-dir}")
    private String uploadDir;
    
    // 서버 URL을 직접 설정
    private String serverUrl = "http://localhost:8080";

    @Autowired
    private FileUploadUtil fileUploadUtil;

    @GetMapping("/register")
    public String registerForm() {
        System.out.println("GET /register - 회원가입 폼 요청");
        return "register";
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestParam("name") String name,
            @RequestParam("class_id") int classId,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam(value = "profileimg", required = false) MultipartFile profileImg) {

        // profileImg가 null이 아니고 비어있지 않은 경우에만 출력
        if (profileImg != null && !profileImg.isEmpty()) {
            System.out.println("업로드된 파일: " + profileImg.getOriginalFilename());
            System.out.println("파일 크기: " + profileImg.getSize());
            System.out.println("파일 유형: " + profileImg.getContentType());
        } else {
            System.out.println("프로필 이미지가 업로드되지 않았거나 비어 있습니다.");
        }
        
        System.out.println("POST /register - 회원가입 요청: name=" + name + ", email=" + email);
       

        // 파일 저장 경로 설정
        String imagePath = null;
        if (profileImg != null && !profileImg.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + profileImg.getOriginalFilename();
            System.out.println("파일 저장 시도: 파일명=" + fileName + ", 파일 크기=" + profileImg.getSize());

            try {
                // 절대 경로 여부 확인
                if (!Paths.get(uploadDir).isAbsolute()) {
                    System.out.println("설정된 경로가 절대 경로가 아닙니다: " + uploadDir);
                    return ResponseEntity.status(500).body("업로드 디렉토리가 절대 경로로 설정되지 않았습니다.");
                }
                
                // 명확한 절대 경로 설정 확인
                File uploadDirectory = new File(uploadDir);
                System.out.println("현재 업로드 디렉토리 경로: " + uploadDir);

                // 디렉토리가 존재하지 않으면 생성
                if (!uploadDirectory.exists()) {
                    System.out.println("업로드 디렉토리가 존재하지 않아 생성합니다: " + uploadDir);
                    boolean created = uploadDirectory.mkdirs();
                    if (created) {
                        System.out.println("업로드 디렉토리 생성 성공: " + uploadDir);
                    } else {
                        System.out.println("업로드 디렉토리 생성 실패: " + uploadDir);
                        return ResponseEntity.status(500).body("파일 저장 디렉토리 생성에 실패했습니다.");
                    }
                }

                // 디렉토리 권한 체크
                if (!Files.isWritable(Paths.get(uploadDir))) {
                    System.out.println("업로드 디렉토리에 쓰기 권한이 없습니다: " + uploadDir);
                    return ResponseEntity.status(500).body("업로드 디렉토리에 쓰기 권한이 없습니다.");
                }

                // 절대 경로를 사용하여 파일 저장 시도
                File dest = new File(uploadDirectory, fileName);
                System.out.println("저장할 파일의 절대 경로: " + dest.getAbsolutePath());
                
                profileImg.transferTo(dest);
                imagePath = dest.getAbsolutePath(); // 저장된 파일의 절대 경로를 사용하여 DB에 저장
                System.out.println("파일 저장 성공: 저장 경로=" + imagePath);
            } catch (IOException e) {
                System.out.println("프로필 이미지 저장 실패: " + e.getMessage());
                e.printStackTrace(); // 예외의 전체 스택 트레이스를 출력하여 원인을 확인
                return ResponseEntity.status(500).body("프로필 이미지 저장에 실패했습니다. 에러 메시지: " + e.getMessage());
            }
        } else {
            System.out.println("프로필 이미지가 업로드되지 않았거나 비어 있습니다.");
        }

        // 유저 정보 설정
        User user = new User();
        user.setName(name);
        user.setClass_id(classId);
        user.setEmail(email);
        user.setPassword(password);
        user.setProfile_picture(imagePath);

        try {
            userService.insert(user);
            System.out.println("회원가입 성공: user=" + user);
            return ResponseEntity.ok("회원가입이 성공적으로 완료되었습니다.");
        } catch (DataIntegrityViolationException e) {
            // 이메일 중복에 대한 예외 처리
            String rootMessage = e.getRootCause() != null ? e.getRootCause().getMessage() : e.getMessage();
            System.out.println("회원가입 실패 - 데이터 무결성 위반: " + rootMessage);

            if (rootMessage.contains("email")) {
                return ResponseEntity.badRequest().body("이미 사용중인 메일입니다.");
            } else {
                return ResponseEntity.badRequest().body("입력한 정보에 문제가 있습니다.");
            }
        } catch (SQLException e) {
            System.out.println("DB 에러 발생: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("DB 오류가 발생했습니다. 다시 시도해 주세요.");
        } catch (Exception e) {
            System.out.println("알 수 없는 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("알 수 없는 오류가 발생했습니다.");
        }
    }

    @GetMapping("/login")
    public String loginForm() {
        System.out.println("GET /login - 로그인 폼 요청");
        return "login";
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody User user, HttpSession session) {
        System.out.println("POST /login - 로그인 요청: user=" + user);
        Map<String, String> response = new HashMap<>();
        try {
            User result = userService.login(user);
            if (result != null) {
                System.out.println("로그인 성공: user=" + result);

                // JWT 토큰 생성 전 로그 추가
                System.out.println("Generating JWT tokens for user: " + result);

                // JWT 토큰 생성
                String accessToken = jwtUtil.generateAccessToken(result);
                String refreshToken = jwtUtil.generateRefreshToken(result);

                // 응답에 토큰 포함
                response.put("message", "로그인 성공");
                response.put("accessToken", accessToken);
                response.put("refreshToken", refreshToken);

                return ResponseEntity.ok(response);
            } else {
                System.out.println("로그인 실패 - 아이디 혹은 비밀번호가 틀렸습니다.");
                response.put("message", "아이디 혹은 비밀번호가 틀렸습니다. 다시 입력해주세요.");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (SQLException e) {
            System.out.println("DB 에러 발생: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("message", "DB 에러가 발생했습니다."));
        } catch (Exception e) {
            // 알 수 없는 오류 발생 시의 로그
            System.out.println("알 수 없는 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("message", "알 수 없는 오류가 발생했습니다."));
        }
    }
    @PutMapping("/updateProfileImage")
    public ResponseEntity<String> updateProfileImage(
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
            @RequestHeader("Authorization") String token) {
        String extractedToken = token.replace("Bearer ", "");
        Integer user_id;
        try {
            user_id = jwtUtil.extractUserId(extractedToken);
            if (user_id == null) {
                return ResponseEntity.status(401).body("Unauthorized: Invalid token.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Unauthorized: " + e.getMessage());
        }

        // 프로필 이미지 업로드 처리
        String imagePath = null;
        if (profileImage != null && !profileImage.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + profileImage.getOriginalFilename();
            File uploadDirectory = new File(uploadDir);
            if (!uploadDirectory.exists()) uploadDirectory.mkdirs();

            File dest = new File(uploadDirectory, fileName);
            try {
                profileImage.transferTo(dest);
                // 클라이언트가 접근할 수 있는 URL로 설정 (DB에 저장할 때는 상대 경로로 저장)
                imagePath = "/images/" + fileName;
                System.out.println("Image Uploaded to: " + imagePath); // 이미지 저장 경로 출력
            } catch (IOException e) {
                System.out.println("Failed to upload profile image: " + e.getMessage());
                return ResponseEntity.status(500).body("Failed to upload profile image.");
            }
        } else {
            // 기본 이미지로 변경 요청이 들어온 경우
            imagePath = "/images/default.png";
            System.out.println("Default Image Set: " + imagePath); // 기본 이미지 경로 출력
        }

        // 사용자 프로필 업데이트
        try {
            User user = userService.select(user_id);
            if (user == null) {
                return ResponseEntity.status(404).body("User not found.");
            }
            user.setProfile_picture(imagePath);
            userService.update(user);
            System.out.println("Profile Image Updated in DB: " + imagePath); // DB에 저장된 이미지 경로 출력
            return ResponseEntity.ok("Profile image updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error updating profile image.");
        }
    }
    
    // 비밀번호 수정 API 추가
    @PutMapping("/updatePassword")
    public ResponseEntity<String> updatePassword(
            @RequestBody Map<String, String> request,
            @RequestHeader("Authorization") String token) {
        // JWT 토큰에서 user_id 추출
        String extractedToken = token.replace("Bearer ", "");
        Integer user_id;
        try {
            user_id = jwtUtil.extractUserId(extractedToken);
            if (user_id == null) {
                return ResponseEntity.status(401).body("Unauthorized: Invalid token.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Unauthorized: " + e.getMessage());
        }

        String newPassword = request.get("password");
        try {
            User user = userService.select(user_id); // 수정된 부분: int 타입으로 변경
            if (user == null) {
                return ResponseEntity.status(404).body("User not found.");
            }
            user.setPassword(newPassword);
            userService.update(user);
            return ResponseEntity.ok("Password updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error updating password.");
        }
    }
    
    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo(@RequestHeader("Authorization") String token) {
        try {
            String extractedToken = token.replace("Bearer ", "");
            Integer userId = jwtUtil.extractUserId(extractedToken);

            if (userId == null) {
                return ResponseEntity.status(401).body("Unauthorized: Invalid token.");
            }

            User user = userService.select(userId); // String 타입에서 int 타입으로 변경

            if (user == null) {
                return ResponseEntity.status(404).body("User not found.");
            }

            return ResponseEntity.ok(user);
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Database error occurred.");
        } catch (Exception e) {
            System.err.println("Error retrieving user info: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal server error.");
        }
    }
    
    // 사용자가 속한 클래스의 멤버들을 조회하는 API
    @GetMapping("/class-members")
    public ResponseEntity<?> getClassMembers(@RequestHeader("Authorization") String token) {
        try {
            // JWT 토큰에서 사용자 아이디 추출
            String extractedToken = token.replace("Bearer ", "");
            Integer userId = jwtUtil.extractUserId(extractedToken);

            if (userId == null) {
                return ResponseEntity.status(401).body("Unauthorized: Invalid token.");
            }

            // 현재 사용자의 class_id를 가져오기 위해 사용자 정보를 조회
            User user = userService.select(userId);
            if (user == null) {
                return ResponseEntity.status(404).body("User not found.");
            }

            // 해당 클래스에 속한 사용자 리스트 조회
            List<User> classUsers = userService.selectUsersByClassId(user.getClass_id());

            return ResponseEntity.ok(classUsers);
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Database error occurred.");
        } catch (Exception e) {
            System.err.println("Error retrieving class members: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal server error.");
        }
    }
    
 // 사용자가 참여 중인 스터디 리스트를 반환하는 API 추가
    @GetMapping("/study-list")
    public ResponseEntity<?> getStudyList(@RequestHeader("Authorization") String token) {
        try {
            String extractedToken = token.replace("Bearer ", "");
            Integer userId = jwtUtil.extractUserId(extractedToken);

            if (userId == null) {
                return ResponseEntity.status(401).body("Unauthorized: Invalid token.");
            }

            List<Map<String, Object>> studyList = userService.getStudyListByUserId(userId);

            if (studyList == null || studyList.isEmpty()) {
                return ResponseEntity.status(404).body("No studies found for the user.");
            }

            return ResponseEntity.ok(studyList);
        } catch (Exception e) {
            System.err.println("Error retrieving study list: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal server error.");
        }
    }
    
 // 각 스터디의 멤버들을 조회하는 API
    @GetMapping("/study-members/{studyId}")
    public ResponseEntity<?> getStudyMembers(@PathVariable("studyId") int studyId, @RequestHeader("Authorization") String token) {
        try {
            String extractedToken = token.replace("Bearer ", "");
            Integer userId = jwtUtil.extractUserId(extractedToken);

            if (userId == null) {
                return ResponseEntity.status(401).body("Unauthorized: Invalid token.");
            }

            List<User> studyMembers = userService.selectUsersByStudyId(studyId);

            if (studyMembers == null || studyMembers.isEmpty()) {
                return ResponseEntity.status(404).body("No members found for this study.");
            }

            return ResponseEntity.ok(studyMembers);
        } catch (Exception e) {
            System.err.println("Error retrieving study members: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal server error.");
        }
    }
    
    // 모든 유저를 조회하는 API
    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers(@RequestHeader("Authorization") String token) {
        try {
            String extractedToken = token.replace("Bearer ", "");
            Integer userId = jwtUtil.extractUserId(extractedToken);

            if (userId == null) {
                return ResponseEntity.status(401).body("Unauthorized: Invalid token.");
            }

            List<User> users = userService.selectAllUsers(); // 모든 유저 리스트 조회
            if (users == null || users.isEmpty()) {
                return ResponseEntity.status(404).body("No users found.");
            }

            return ResponseEntity.ok(users);
        } catch (Exception e) {
            System.err.println("Error retrieving users: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal server error.");
        }
    }
}

