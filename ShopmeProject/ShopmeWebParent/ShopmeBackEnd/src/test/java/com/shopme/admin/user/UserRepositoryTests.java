package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {
	
	@Autowired 
	private UserRepository userRepository;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreateNewUserWithOneRole() {
		User user1 = new User("thaianh@gmail.com", "123", "Le", "Thai Anh");
		user1.addRole(entityManager.find(Role.class, 1));
		User user = userRepository.save(user1);
		assertThat(user.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateNewUserWithTwoRoles() {
		User user2 = new User("thanhtuyen@gmail.com", "123", "Le", "Thanh Tuyen");
		user2.addRole(entityManager.find(Role.class, 3));
		user2.addRole(entityManager.find(Role.class, 4));
		
		User user = userRepository.save(user2);
		assertThat(user.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListAllUser() {
		List<User> listUser = (List<User>) userRepository.findAll();
		for (User user : listUser) {
			System.out.println(user);
		}
		assertThat(listUser.size()).isGreaterThan(0);
	}
	
	@Test
	public void testGetUserById() {
		User user = userRepository.findById(1).get();
		System.out.println(user);
		assertThat(user).isNotNull();
	}
	
	@Test
	public void testUpdateUserDetails() {
		User user = userRepository.findById(1).get();
		user.setEnabled(true);
		user.setEmail("thaianhupdated@gmail.com");
		User updatedUser = userRepository.save(user);
		assertThat(updatedUser).isNotNull();
	}
	
	@Test
	public void testUpdateUserRoles() {
		User user = userRepository.findById(2).get();
		user.setEnabled(true);
		
		user.getRoles().remove(new Role(3));
		user.addRole(new Role(1));
		User updatedUser = userRepository.save(user);
		assertThat(updatedUser).isNotNull();
	}
	
	@Test
	public void testDeleteUser() {
		userRepository.deleteById(2);

		assertThat(entityManager.find(User.class, 2)).isNull();
	}
	
	@Test
	public void testGetUserByEmail() {
		String email = "thanhthao@gmail.com";
		User user = userRepository.getUserByEmail(email);

		assertThat(user).isNotNull();
	}
	
	@Test
	public void testCountById() {
		Integer id = 13;
		Long countById = userRepository.countById(id);

		assertThat(countById).isNotNull().isGreaterThan(0);
	}
	
	@Test
	public void testDisabledUser() {
		Integer id = 13;
		userRepository.updateEnabledStatus(id, false);

	}
	
	@Test
	public void testEnabledUser() {
		Integer id = 13;
		userRepository.updateEnabledStatus(id, true);

	}
	
	@Test
	public void testListFirstPage() {
		int pageNumber = 1;
		int pageSize = 4;
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = userRepository.findAll(pageable);
		
		List<User> list = page.getContent();
		for (User user : list) {
			System.out.println(user.getEmail());
		}
		
		assertThat(list.size()).isEqualTo(pageSize);
		
	}
	
	@Test
	public void testSearchUsers() {
		String keyword = "bruce";
		Pageable pageable = PageRequest.of(0, 4);
		Page<User> page = userRepository.findAll(keyword, pageable);
		
		List<User> list = page.getContent();
		for (User user : list) {
			System.out.println(user.getFirstName());
		}
		
		assertThat(list.size()).isGreaterThan(0);
	}

}





















