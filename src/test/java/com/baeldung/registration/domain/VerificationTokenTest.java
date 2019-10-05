package com.baeldung.registration.domain;

import com.baeldung.registration.repositories.VerificationTokenRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class VerificationTokenTest {

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Test
    public void testExpiryDate() {
        VerificationToken verificationToken = VerificationToken.builder().build();
        VerificationToken savedVT = verificationTokenRepository.save(verificationToken);
        System.out.println("Value: " + savedVT.getExpiryDate());
    }
}