package com.example.todolist.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.todolist.entity.Todo;
import com.example.todolist.form.TodoData;
import com.example.todolist.form.TodoQuery;
import com.example.todolist.repository.TodoRepository;
import com.example.todolist.service.TodoService;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class TodoListController {
	private final TodoRepository todoRepository;
	private final TodoService todoService; // Todolist2 で追加
	private final HttpSession session;

	// ToDo 一覧表示(Todolist で追加)

	@GetMapping("/todo")
	public ModelAndView showTodoList(ModelAndView mv) {
		mv.setViewName("todoList");
		List<Todo> todoList = todoRepository.findAll();
		mv.addObject("todoList", todoList);
		mv.addObject("todoQuery", new TodoQuery()); // ※ Todolist4 で追加
		return mv;
	}

	@PostMapping("/todo/query")
	public ModelAndView queryTodo(@ModelAttribute TodoQuery todoQuery, // ①
			BindingResult result, // ②
			ModelAndView mv) {
		mv.setViewName("todoList");
		List<Todo> todoList = null;
		if (todoService.isValid(todoQuery, result)) { // ③
			// エラーが無ければ検索
			todoList = todoService.doQuery(todoQuery); // ④
		}
		// mv.addObject("todoQuery", todoQuery); // ⑤
		mv.addObject("todoList", todoList); // ⑥
		return mv;
	}

	// ToDo 入力フォーム表示(Todolist2 で追加)
	// 【処理 1 】 ToDo 一覧画面(todoList.html)で[新規追加]リンクがクリックされたとき
	@GetMapping("/todo/create")
	public ModelAndView createTodo(ModelAndView mv) {
		mv.setViewName("todoForm"); // ①
		mv.addObject("todoData", new TodoData()); // ②
		session.setAttribute("mode", "create"); // ③
		return mv;
	}

	// ToDo 追加処理(Todolist2 で追加)
	// 【処理 2 】 ToDo 入力画面(todoForm.html)で[登録]ボタンがクリックされたとき
	// ToDo 追加処理(Todolist2 で追加したものを Todolist3 で改善)
	@PostMapping("/todo/create")
	public String createTodo(@ModelAttribute @Validated TodoData todoData, BindingResult result, Model model) {
		// エラーチェック
		boolean isValid = todoService.isValid(todoData, result);
		if (!result.hasErrors() && isValid) {
			// エラーなし
			Todo todo = todoData.toEntity();
			todoRepository.saveAndFlush(todo);
			return "redirect:/todo";
		} else {
			// エラーあり
			// model.addAttribute("todoData", todoData);
			return "todoForm";
		}
	}

	// ToDo 一覧へ戻る(Todolist2 で追加)
	// 【処理 3 】 ToDo 入力画面で[キャンセル登録]ボタンがクリックされたとき
	@PostMapping("/todo/cancel")
	public String cancel() {
		return "redirect:/todo";
	}

	@GetMapping("/todo/{id}")
	public ModelAndView todoById(@PathVariable(name = "id") int id, ModelAndView mv) {
		mv.setViewName("todoForm");
		Todo todo = todoRepository.findById(id).get(); // ①
		mv.addObject("todoData", todo); // ※ b
		session.setAttribute("mode", "update"); // ②
		return mv;
	}

	@PostMapping("/todo/update")
	public String updateTodo(@ModelAttribute @Validated TodoData todoData, BindingResult result, Model model) {
		// エラーチェック
		boolean isValid = todoService.isValid(todoData, result);
		if (!result.hasErrors() && isValid) {
			// エラーなし
			Todo todo = todoData.toEntity();
			todoRepository.saveAndFlush(todo); // ①
			return "redirect:/todo";
		} else {
			// エラーあり
			// model.addAttribute("todoData", todoData);
			return "todoForm";
		}
	}

	@PostMapping("/todo/delete")
	public String deleteTodo(@ModelAttribute TodoData todoData) {
		todoRepository.deleteById(todoData.getId());
		return "redirect:/todo";
	}

}
