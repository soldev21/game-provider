package com.megafair.security.jwt;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import lombok.Data;
import org.eclipse.microprofile.jwt.Claim;

@Data
@RequestScoped
public class PrincipalWrapper {

    @Inject
    @Claim(value = "data")
    String platformId;
}
