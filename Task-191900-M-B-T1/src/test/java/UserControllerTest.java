import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Mock
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testGetUserById() throws Exception {
        // Mock the user service behavior
        long userId = 1L;
        User user = new User(userId, "Test User", "test@example.com");
        when(userService.getUserById(userId)).thenReturn(Optional.of(user));

        // Define the expected response based on the OpenAPI specification
        UserResponse expectedResponse = new UserResponse(userId, user.getName(), user.getEmail());

        // Perform the API call and validate the response
        mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userId.intValue())))
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.email", is(user.getEmail())));

        // Optionally, you can also validate the response object using Hamcrest matchers
        verify(userService).getUserById(userId);
    }
}
