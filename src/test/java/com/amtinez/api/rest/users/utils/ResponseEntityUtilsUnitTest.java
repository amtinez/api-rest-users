package com.amtinez.api.rest.users.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for {@link ResponseEntityUtils}
 *
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@ExtendWith(MockitoExtension.class)
public class ResponseEntityUtilsUnitTest {

    @Test
    public void testGetResponseEntityByAffectedEntitiesOneEntities() {
        assertThat(ResponseEntityUtils.getResponseEntityByAffectedEntities(1).getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testGetResponseEntityByAffectedEntitiesNoEntities() {
        assertThat(ResponseEntityUtils.getResponseEntityByAffectedEntities(0).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

}
