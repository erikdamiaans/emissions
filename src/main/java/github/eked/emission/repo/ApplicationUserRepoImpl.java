package github.eked.emission.repo;

import github.eked.emission.bean.ApplicationUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class ApplicationUserRepoImpl implements ApplicationUserRepo {

    private Map<String, ApplicationUser> repo = new HashMap<>();

    public ApplicationUserRepoImpl(@Value("${user.db.location}") String userDbFilePath) throws IOException {
        loadUsers(userDbFilePath);
    }
private void loadUsers(String userDb) throws  IOException{
    try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(userDb)) {
        repo = Arrays.stream(new ObjectMapper().readValue(inputStream, ApplicationUser[].class))
                .collect(Collectors.toMap(ApplicationUser::getUserName, Function.identity()));
    }
}


    @Override
    public ApplicationUser findByUserName(String userName) {
        ApplicationUser applicationUser = repo.get(userName);
        log.info(" ApplicationUserRepoImpl findByUserName {} found {} ",userName,applicationUser);
        return applicationUser;
    }
}
