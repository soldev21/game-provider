package com.megafair.service;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class PlatformService {

    private final RestClientFactory restClientFactory;

}
