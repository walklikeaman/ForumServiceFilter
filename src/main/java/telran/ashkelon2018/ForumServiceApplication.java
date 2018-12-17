package telran.ashkelon2018;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import telran.ashkelon2018.forum.domain.Role;
import telran.ashkelon2018.forum.service.RoleService;

@SpringBootApplication
public class ForumServiceApplication implements CommandLineRunner {

	@Autowired
	RoleService roleService;
	
	
	
	public static void main(String[] args) {
		SpringApplication.run(ForumServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
//		HashSet<String> mainRoles = new HashSet<>();
//		mainRoles.add("Admin");
//		mainRoles.add("Moderator");
//		roleService.createRole(new Role("removeUser", mainRoles));
//		roleService.createRole(new Role("addRole", mainRoles));
//		roleService.createRole(new Role("removeRole", mainRoles));
		
		
	}
}
