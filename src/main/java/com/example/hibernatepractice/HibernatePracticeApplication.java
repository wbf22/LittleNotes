package com.example.hibernatepractice;

import com.example.hibernatepractice.app.api.Cli;
import com.example.hibernatepractice.service.api.Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Scanner;
import java.util.StringTokenizer;

@SpringBootApplication
@EnableScheduling
public class HibernatePracticeApplication {



	public static void main(String[] args) {


		SpringApplication.run(HibernatePracticeApplication.class, args);

		System.out.println("Welcome to Little Notes Db (type 'help' for command list)");

	}

}
