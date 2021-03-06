package org.zerhusen.rest;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.zerhusen.model.security.Authority;
import org.zerhusen.model.security.AuthorityName;
import org.zerhusen.model.security.User;
import org.zerhusen.security.JwtTokenUtil;
import org.zerhusen.security.JwtUser;
import org.zerhusen.security.JwtUserFactory;
import org.zerhusen.security.service.JwtUserDetailsService;
import org.zerhusen.security.service.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRestControllerTest {

	private static final String REGISTER_JSON = "{\"username\":\"example\"," + "\"password\":\"example\","
			+ "\"email\":\"newUser@gmail.com\"}";

	private MockMvc mvc;

	@Autowired
	private WebApplicationContext context;

	@MockBean
	private JwtTokenUtil jwtTokenUtil;

	@MockBean
	private JwtUserDetailsService jwtUserDetailsService;

	@MockBean
	private UserService userServiceMock;

	@Before
	public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
	}

	@Test
	public void shouldGetUnauthorizedWithoutRole() throws Exception {

		mvc.perform(get("/user")).andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser(roles = "USER")
	public void getPersonsSuccessfullyWithUserRole() throws Exception {

		Authority authority = new Authority();
		authority.setId(1L);
		authority.setName(AuthorityName.ROLE_ADMIN);
		List<Authority> authorities = Arrays.asList(authority);

		User user = new User();
		user.setUsername("username");
		user.setAuthorities(authorities);
		user.setEnabled(Boolean.TRUE);
		user.setLastPasswordResetDate(new Date(System.currentTimeMillis() + 1000 * 1000));

		JwtUser jwtUser = JwtUserFactory.create(user);

		when(jwtTokenUtil.getUsernameFromToken(any())).thenReturn(user.getUsername());

		when(jwtUserDetailsService.loadUserByUsername(eq(user.getUsername()))).thenReturn(jwtUser);

		mvc.perform(get("/user").header("Authorization", "Bearer nsodunsodiuv")).andExpect(status().is2xxSuccessful());
	}

	@Test
    public void ShouldReturnSuccessfulStatusWhenRegisteringNewUser()
    throws Exception{
		this.mvc.perform(post("/auth/registration")
				.content(REGISTER_JSON)
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isCreated())
		.andExpect(content().json("{\"status\":\"User created.\"}"));
		verify(userServiceMock).save(any(User.class));
	}

}
