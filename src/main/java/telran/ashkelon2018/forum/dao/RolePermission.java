package telran.ashkelon2018.forum.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import telran.ashkelon2018.forum.domain.Role;

public interface RolePermission extends MongoRepository<Role, String> {

}
