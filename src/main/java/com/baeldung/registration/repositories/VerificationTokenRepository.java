package com.baeldung.registration.repositories;

import com.baeldung.registration.domain.VerificationToken;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by uc on 10/3/2019
 */
public interface VerificationTokenRepository extends CrudRepository<VerificationToken, Long> {

    VerificationToken findByToken(String token);
}
