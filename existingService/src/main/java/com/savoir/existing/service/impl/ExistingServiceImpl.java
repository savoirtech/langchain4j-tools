package com.savoir.existing.service.impl;

import com.savoir.existing.service.api.ExistingService;

public class ExistingServiceImpl implements ExistingService {

    @Override
    public String convertToNewSchema(String original) {
        return original.toUpperCase();
    }
}
