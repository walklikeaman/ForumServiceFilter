package telran.ashkelon2018.forum.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import telran.ashkelon2018.forum.dao.RolePermission;
import telran.ashkelon2018.forum.domain.Role;

@Service
public class RoleServiceImpl implements RoleService {
	@Autowired
	RolePermission rolePermission;
	
	@Override
	public Role createRole(Role role) {
		return rolePermission.save(role);
	}

	@Override
	public Role addRoleByMethod(String roleMethod, Set<String> newRoles) {
		Role roleToAdd = getRole(roleMethod);
		if (roleToAdd!=null) {
			newRoles.forEach(r-> roleToAdd.addRole(r));
			rolePermission.save(roleToAdd);
		}
		return roleToAdd;
	}

	@Override
	public Role getRole(String roleMethod) {	
		return rolePermission.findById(roleMethod).orElse(null);
	}

	@Override
	public Role removeRole(String roleMethod) {
		Role roleToRemove = getRole(roleMethod);
		if (roleToRemove!=null) {
			rolePermission.delete(roleToRemove);
		}
		return roleToRemove;
	}

	@Override
	public Role removeRoleByMethod(String roleMethod, Set<String> newRoles) {
		Role roleToRemove = getRole(roleMethod);
		if (roleToRemove!=null) {
			newRoles.forEach(r-> roleToRemove.removeRole(r));
			rolePermission.delete(roleToRemove);
		}
		return roleToRemove;
	}

}
