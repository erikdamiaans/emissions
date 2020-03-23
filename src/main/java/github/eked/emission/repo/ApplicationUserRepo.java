package github.eked.emission.repo;

import github.eked.emission.bean.ApplicationUser;

public interface ApplicationUserRepo {
    ApplicationUser findByUserName(String userName);
}
