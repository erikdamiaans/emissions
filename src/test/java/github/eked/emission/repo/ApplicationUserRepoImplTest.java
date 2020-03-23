package github.eked.emission.repo;

import github.eked.emission.bean.ApplicationUser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ApplicationUserRepoImplTest.TestConfig.class)
@TestPropertySource(properties = {"user.db.location=users.json"})
public class ApplicationUserRepoImplTest {
    @Autowired
    private ApplicationUserRepo applicationUserRepo;

    @Test
    public void getUserOk() {
        ApplicationUser ester = this.applicationUserRepo.findByUserName("ester");
        Assert.assertNotNull(ester);
    }

    @TestConfiguration
    @Import(ApplicationUserRepoImpl.class)
    static class TestConfig{

    }
}
