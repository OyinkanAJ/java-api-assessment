package com.cbfacademy.apiassessment;

import com.cbfacademy.apiassessment.dto.Data;
import com.cbfacademy.apiassessment.dto.Response;
import com.cbfacademy.apiassessment.dto.UserRequest;
import com.cbfacademy.apiassessment.entity.User;
import com.cbfacademy.apiassessment.exceptions.customException.UserNotFoundException;
import com.cbfacademy.apiassessment.repository.UserRepository;
import com.cbfacademy.apiassessment.service.UserService;
import com.cbfacademy.apiassessment.service.serviceImpl.UserServiceImpl;
import com.cbfacademy.apiassessment.utils.ResponseUtils;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Description;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.net.URL;

import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

//@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = App.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AppTests {

//	@LocalServerPort
//	private int port;

	@MockBean
	private UserRepository userRepository;
//	@Autowired
//	private TestRestTemplate restTemplate;
	@InjectMocks
	private ResponseUtils responseUtils;

//	@InjectMocks
//	private UserServiceImpl userService;

//	public AppTests(UserRepository userRepository, ResponseUtils responseUtils) {
//		this.userRepository = userRepository;
//		this.responseUtils = responseUtils;
//	}

	@Autowired
	public AppTests(UserRepository userRepository, ResponseUtils responseUtils, UserServiceImpl userService) {
		this.userRepository = userRepository;
		this.responseUtils = responseUtils;
		this.userService = userService;
	}

	@InjectMocks
	private UserServiceImpl userService;


//	private URL base;
//
//
//	AppTests() {
//	}
//
//	@BeforeEach
//	public void setUp() throws Exception {
//		this.base = new URL("http://localhost:" + port + "/greeting");
//	}
//
//	@Test
//	@Description("/greeting endpoint returns expected response for default name")
//	public void greeting_ExpectedResponseWithDefaultName() {
//		ResponseEntity<String> response = restTemplate.getForEntity(base.toString(), String.class);
//
//		assertEquals(200, response.getStatusCode().value());
//		assertEquals("Hello World", response.getBody());
//	}
//
//	@Test
//	@Description("/greeting endpoint returns expected response for specified name parameter")
//	public void greeting_ExpectedResponseWithNameParam() {
//		ResponseEntity<String> response = restTemplate.getForEntity(base.toString() + "?name=John", String.class);
//
//		assertEquals(200, response.getStatusCode().value());
//		assertEquals("Hello John", response.getBody());
//	}


	@Test
	public void testRegisterUser_successfulRegistration() {
		String email = "test@example.com";
		String firstName = "John";
		String lastName = "Doe";
		String otherName = "Middle";
		BigDecimal accountBalance = BigDecimal.valueOf(10000);

		when(userRepository.existsByEmail(email)).thenReturn(false);

		int lengthOfAccNumber = 10;
		String generatedAccountNumber = ResponseUtils.generateAccountNumber(lengthOfAccNumber);

		// Create user request
		UserRequest userRequest = UserRequest.builder()
				.email(email)
				.firstName(firstName)
				.lastName(lastName)
				.otherName(otherName)
				.accountBalance(accountBalance)
				.build();

		// Perform the test
		Response response = userService.registerUser(userRequest);

//		 Verify user creation and saving
		Mockito.verify(userRepository).save(any());

		// Verify response structure and data
		assertEquals(responseUtils.SUCCESS, response.getResponseCode());
		assertEquals(responseUtils.USER_REGISTERED_SUCCESS, response.getResponseMessage());
		Data data = (Data) response.getData();
		assertEquals(userRequest.getAccountBalance(), data.getAccountBalance());
		assertNotEquals(generatedAccountNumber, data.getAccountNumber());
		assertEquals(firstName + " " + lastName + " " + otherName, data.getAccountName());
	}

	@Test
	public void testRegisterUser_emailAlreadyExists() {
		String email = "test@example.com";
		when(userRepository.existsByEmail(email)).thenReturn(true);

		// Create user request
		UserRequest userRequest = UserRequest.builder()
				.email(email)
				.build();

		// Perform the test
		try {
			userService.registerUser(userRequest);
		} catch (UserNotFoundException e) {
			assertEquals("User not found", e.getMessage());
		}
		verify(userRepository, never()).save(any(User.class));
	}
}
