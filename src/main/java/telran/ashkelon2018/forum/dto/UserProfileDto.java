package telran.ashkelon2018.forum.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public class UserProfileDto {
	String login;
	String firstName;
	String lastName;
	Set<String> roles;
}
