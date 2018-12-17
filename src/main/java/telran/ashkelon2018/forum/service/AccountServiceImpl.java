package telran.ashkelon2018.forum.service;

import java.time.LocalDateTime;
import java.util.Set;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import telran.ashkelon2018.forum.configuration.AccountConfiguration;
import telran.ashkelon2018.forum.configuration.AccountUserCredential;
import telran.ashkelon2018.forum.dao.UserAccountRepository;
import telran.ashkelon2018.forum.domain.Role;
import telran.ashkelon2018.forum.domain.UserAccount;
import telran.ashkelon2018.forum.dto.UserProfileDto;
import telran.ashkelon2018.forum.dto.UserRegDto;
import telran.ashkelon2018.forum.exceptions.UserConflictException;
import telran.ashkelon2018.forum.exceptions.UserForbiddenException;

@Service
public class AccountServiceImpl implements AccountService {
	@Autowired
	UserAccountRepository userRepository;

	@Autowired
	RoleService roleService;

	@Autowired
	AccountConfiguration accountConfiguration;

	@Override
	public UserProfileDto addUser(UserRegDto userRegDto, String token) {
		AccountUserCredential credentials = accountConfiguration.tokenDecode(token);
		if (userRepository.existsById(credentials.getLogin())) {
			throw new UserConflictException();
		}
		String hashPassword = BCrypt.hashpw(credentials.getPassword(), BCrypt.gensalt());
		UserAccount userAccount = UserAccount.builder()
				.login(credentials.getLogin()).password(hashPassword)
				.firstName(userRegDto.getFirstName())
				.lastName(userRegDto.getLastName()).role("User")
				.expdate(LocalDateTime.now().plusDays(accountConfiguration.getExpPeriod())).build();
		userRepository.save(userAccount);

		return convertToUserProfileDto(userAccount);
	}

	private UserProfileDto convertToUserProfileDto(UserAccount userAccount) {
		return UserProfileDto.builder().firstName(userAccount.getFirstName()).lastName(userAccount.getLastName())
				.login(userAccount.getLogin()).roles(userAccount.getRoles()).build();
	}

	@Override
	public UserProfileDto editUser(UserRegDto userRegDto, String token) {
		AccountUserCredential credentials = accountConfiguration.tokenDecode(token);
		UserAccount userAccount = userRepository.findById(credentials.getLogin()).get();
		if (userRegDto.getFirstName() != null) {
			userAccount.setFirstName(userRegDto.getFirstName());
		}
		if (userRegDto.getFirstName() != null) {
			userAccount.setLastName(userRegDto.getLastName());
		}
		userRepository.save(userAccount);

		return convertToUserProfileDto(userAccount);
	}

	@Override
	public UserProfileDto removeUser(String login, String token) {
		AccountUserCredential credential = accountConfiguration.tokenDecode(token);
		UserAccount superAccount = userRepository.findById(credential.getLogin()).get();
		Role permission = roleService.getRole("removeUser");

		UserAccount userAccount = userRepository.findById(login).orElse(null);

		if (userAccount == null) {
			throw new UserConflictException();
		}

		if (login.equals(credential.getLogin())) {
			userRepository.delete(userAccount);
			return convertToUserProfileDto(userAccount);
		}
		if (permission != null && checkRoles(superAccount.getRoles(), permission.getRoles())) {
			userRepository.delete(userAccount);
			return convertToUserProfileDto(userAccount);
		}
		throw new UserForbiddenException();
	}

	private boolean checkRoles(Set<String> roles, Set<String> roles2) {
		boolean flag = false;
		for (String string : roles2) {
			if (roles.contains(string)) {
				flag = true;
			}
		}
		return flag;
	}

	@Override
	public Set<String> addRole(String login, String role, String token) {
		AccountUserCredential credential = accountConfiguration.tokenDecode(token);
		UserAccount superAccount = userRepository.findById(credential.getLogin()).get();
		Role permission = roleService.getRole("addRole");

		UserAccount userAccount = userRepository.findById(login).get();

		if (userAccount == null) {
			throw new UserConflictException();
		}
		if (permission != null && checkRoles(superAccount.getRoles(), permission.getRoles())) {
			userAccount.addRole(role);
			userRepository.save(userAccount);
		}

		return userAccount.getRoles();
	}

	@Override
	public Set<String> removeRole(String login, String role, String token) {
		AccountUserCredential credential = accountConfiguration.tokenDecode(token);
		UserAccount superAccount = userRepository.findById(credential.getLogin()).get();
		Role permission = roleService.getRole("removeRole");

		UserAccount userAccount = userRepository.findById(login).get();

		if (userAccount == null) {
			throw new UserConflictException();
		}
		if (permission != null && checkRoles(superAccount.getRoles(), permission.getRoles())) {
			userAccount.removeRole(role);
			userRepository.save(userAccount);
		}
		return userAccount.getRoles();
	}

	@Override
	public void changePassword(String password, String token) {
		AccountUserCredential credentials = accountConfiguration.tokenDecode(token);
		UserAccount userAccount = userRepository.findById(credentials.getLogin()).get();
		String HashPassword = BCrypt.hashpw(password, BCrypt.gensalt());
		userAccount.setPassword(HashPassword);
		userAccount.setExpdate(LocalDateTime.now().plusDays(accountConfiguration.getExpPeriod()));
		userRepository.save(userAccount);
	}

}
