package telran.ashkelon2018.forum.service;

import java.util.Set;

import telran.ashkelon2018.forum.domain.Role;

public interface RoleService {
	
	Role createRole(Role role);
	Role removeRole(String roleMethod);
	Role addRoleByMethod(String roleMethod, Set<String> newRoles);
	Role getRole(String roleMethod);
	Role removeRoleByMethod(String roleMethod, Set<String> newRoles);
	
}
