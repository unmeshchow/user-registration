package com.baeldung.registration.repositories;

import com.baeldung.registration.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Created by uc on 9/30/2019
 */
public interface UserRepository extends CrudRepository<User, Long> {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}
