package com.example.todolist.entity;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity // ①
@Table(name = "todo") // ②
@Data
public class Todo {
	@Id // ③
	@GeneratedValue(strategy = GenerationType.IDENTITY) // ④
	@Column(name = "id") // ⑤
	private Integer id;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "importance")
	private Integer importance;
	
	@Column(name = "urgency")
	private Integer urgency;
	
	@Column(name = "deadline")
	private Date deadline;
	
	@Column(name = "done")
	private String done;
}
