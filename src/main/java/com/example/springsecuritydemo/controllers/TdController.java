package com.example.springsecuritydemo.controllers;

import com.example.springsecuritydemo.model.Todo;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
public class TdController {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	private static final List<Todo> TODO_LIST = List.of(
		new Todo("myusr","Do task A"), new Todo("myusr","Do task B")
	);

	
	@GetMapping("/todos")
	public List<Todo> showTodos() {
		return TODO_LIST;
	}

	@GetMapping("/user/{username}/todos")
	public Todo showTodosForAUser(@PathVariable String username) {
		
		return TODO_LIST.get(0);
	}

	@PostMapping("/user/{username}/todos")
	public void createTodoForAUser(@PathVariable String username, @RequestBody Todo todo) {
		logger.info("Create {} for {}", todo, username);
	}
}
