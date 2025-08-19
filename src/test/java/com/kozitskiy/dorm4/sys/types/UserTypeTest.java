package com.kozitskiy.dorm4.sys.types;

import com.kozitskiy.dorm4.sys.entities.enums.UserType;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import static org.junit.jupiter.api.Assertions.*;

public class UserTypeTest {
    @Test
    void getAuthority_ReturnsCorrectAuthorityName() {
        // Test all enum values
        assertEquals("ADMIN", UserType.ADMIN.getAuthority());
        assertEquals("STUDENT", UserType.STUDENT.getAuthority());
        assertEquals("PLUMBER", UserType.PLUMBER.getAuthority());
        assertEquals("ELECTRICIAN", UserType.ELECTRICIAN.getAuthority());
        assertEquals("MANAGER", UserType.MANAGER.getAuthority());
    }

    @Test
    void userType_ImplementsGrantedAuthority() {
        // Verify that UserType implements GrantedAuthority
        assertTrue(GrantedAuthority.class.isAssignableFrom(UserType.class));
    }

    @Test
    void userType_ValuesAreCorrect() {
        // Verify all expected values are present
        UserType[] values = UserType.values();
        assertEquals(5, values.length);

        assertArrayEquals(new UserType[]{
                UserType.ADMIN,
                UserType.STUDENT,
                UserType.PLUMBER,
                UserType.ELECTRICIAN,
                UserType.MANAGER
        }, values);
    }

    @Test
    void userType_NameMatchesAuthority() {
        // Verify that name() matches getAuthority() for all values
        for (UserType userType : UserType.values()) {
            assertEquals(userType.name(), userType.getAuthority());
        }
    }
}
