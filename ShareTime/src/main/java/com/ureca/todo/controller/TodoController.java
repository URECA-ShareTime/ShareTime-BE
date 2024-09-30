package com.ureca.todo.controller;

import java.util.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ureca.user.util.JwtUtil;

import com.ureca.todo.dto.Todo;
import com.ureca.todo.model.dao.TodoDAO;
import com.ureca.todo.model.service.TodoService;

@RestController
@RequestMapping("/main")
public class TodoController {
	
	@Autowired
    private TodoService todoService;

    @Autowired
    private TodoDAO todoDAO;

    @Autowired
    private JwtUtil jwtUtil;
    
    @PostMapping("/todos") //새로운 todo 생성
    public ResponseEntity<?> createTodo(@RequestBody Todo todo, @RequestHeader("Authorization") String token) throws SQLException { //todo와 user_id를 받아옴
        // JWT 토큰에서 user_id 추출
        String extractedToken = token.replace("Bearer ", "");
        Integer user_id;
        try {
            user_id = jwtUtil.extractUserId(extractedToken);
            if (user_id == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: Invalid token.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: " + e.getMessage());
        }
        
        System.out.println("Post /todos");
        Todo newTodo = new Todo();
        newTodo.setTitle(todo.getTitle());
        newTodo.setDescription(todo.getDescription());
        newTodo.setUser_id(user_id);
        newTodo.setCreated_at(todo.getCreated_at());

        Todo createdTodo = todoService.insert(newTodo);
        if (createdTodo != null) {
            System.out.println("Todo created");
            return new ResponseEntity<>(createdTodo, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/todos")//날짜로 todo 여러개 조회
    public ResponseEntity<?> getTodoByDate(@RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,  @RequestHeader("Authorization") String token) throws SQLException {
        System.out.println("=============>Get /todos/");
        
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
        
        int uid = user_id;
        List<Todo> todoList = todoService.findByDate(date, uid);
        if (todoList != null) {
            return new ResponseEntity<>(todoList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //todo 수정
    @PutMapping("/todos/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable() int id, @RequestBody Todo todo) throws SQLException {
        System.out.println("Put /todos/" + id);
        Todo updatedTodo = todoService.update(id, todo);
        if (updatedTodo != null) {
            return new ResponseEntity<>(updatedTodo, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //todo 삭제
    @DeleteMapping("/todos/{id}")
    public ResponseEntity<Todo> deleteTodo(@PathVariable() int id) throws SQLException {
        System.out.println("Delete /todo/" + id);
        int result = todoService.delete(id);
        if (result > 0) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
