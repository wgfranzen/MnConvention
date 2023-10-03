/*
 * Copyright (c) 2023 by WF0B, Bill Franzen.  All Rights Reserved.
 *
 *  RegistrationTest.java is part of MnConvention.
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

@DisplayName("Registration")
class RegistrationTest {

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("Registration")
    @Test
    public @interface DetailTest {
    }


    @DetailTest
    @DisplayName("Runs the Registration")
    void run() {
       String[] runtimeParameters = { "--input", "C:/Users/wgfra/Downloads/10-1-23 convention-bitform 2.csv",
       "--output", "target/10-1-23 convention-bitform 2.xlsx"};
       MnConvention.main(runtimeParameters);
    }
}