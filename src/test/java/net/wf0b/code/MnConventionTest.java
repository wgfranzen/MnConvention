/*
 * Copyright (c) 2023 by WF0B, Bill Franzen.  All Rights Reserved.
 *
 *  MnConventionTest.java is part of MnConvention.
 *
 *  MnConvention is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *  MnConvention is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with Foobar. If not, see <https://www.gnu.org/licenses/>.
 */

package net.wf0b.code;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit Test of MnConvention
 */
@DisplayName("MnConvention Unit Test")
class MnConventionTest {

    /**
     * The Detail Test - with "detail" and "MnConvention" tags.
     */
    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("detail")
    @Tag("MnConvention")
    @Test
    public @interface DetailTest {
    }

    /**
     * Tests the -h help option
     */
    @DetailTest
    @DisplayName("MnConvention.main(-h)")
    void mainHelp() {
        String[] args = new String[] {"-h"};
        MnConvention.main(args);
        assertTrue(true);
    }

    /**
     * Performs basic conversion test
     */
    @DetailTest
    @DisplayName("MnConvention.main(-i C:/Users/wgfra/IdeaProjects/MnConvention/src/test/resources/bitform_2test4d.csv -o target/bitform_2test4d.xlsx)")
    void mainTest() {
        String[] args = new String[] {"-i", "C:/Users/wgfra/IdeaProjects/MnConvention/src/test/resources/bitform_2test4d.csv", "-o", "target/bitform_2test4d.xlsx"};
        MnConvention.main(args);
        assertTrue(true);
    }

}