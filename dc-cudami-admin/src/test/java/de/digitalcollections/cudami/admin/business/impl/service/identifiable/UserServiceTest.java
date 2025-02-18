package de.digitalcollections.cudami.admin.business.impl.service.identifiable;

import static org.assertj.core.api.Assertions.assertThat;

import de.digitalcollections.cudami.admin.business.api.service.security.UserService;
import de.digitalcollections.cudami.admin.business.impl.service.security.UserServiceImpl;
import de.digitalcollections.cudami.client.CudamiClient;
import de.digitalcollections.cudami.client.security.CudamiUsersClient;
import de.digitalcollections.model.security.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.context.MessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserServiceTest {

  private User user;

  CudamiClient cudamiClient = Mockito.mock(CudamiClient.class);
  CudamiUsersClient userRepository = Mockito.mock(CudamiUsersClient.class);
  MessageSource messageSource = Mockito.mock(MessageSource.class);

  UserService service;

  @BeforeEach
  public void setup() throws Exception {
    user = new User();
    user.setEmail("foo@spar.org");
    user.setPasswordHash(new BCryptPasswordEncoder().encode("foobar"));
    Mockito.when(cudamiClient.forUsers()).thenReturn(userRepository);
    Mockito.when(messageSource.getMessage(Mockito.any(), Mockito.any(), Mockito.any()))
        .thenReturn("foobar");
    Mockito.when(userRepository.findOneByEmail("foo@spar.org")).thenReturn(user);
    service = new UserServiceImpl(null, cudamiClient, messageSource);
  }

  @AfterEach
  public void tearDown() {
    Mockito.reset(userRepository);
  }

  @Test
  public void testLoadUserByUsername() throws Exception {
    UserDetails retrieved = service.loadUserByUsername("foo@spar.org");
    assertThat(retrieved.getUsername()).isEqualTo(user.getEmail());
    Mockito.verify(userRepository, VerificationModeFactory.times(1)).findOneByEmail("foo@spar.org");
  }

  @Test
  public void testGetPasswordHash() throws Exception {
    PasswordEncoder pwEncoder = new BCryptPasswordEncoder();
    assertThat(pwEncoder.matches("foobar", user.getPasswordHash())).isTrue();
  }
}
