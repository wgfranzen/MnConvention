/*
 * Copyright (c) 2024 by WF0B, Bill Franzen.  All Rights Reserved.
 *
 *  TransactionTest.java is part of MnConvention.
 *
 *  MnConvention is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *  MnConvention is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with Foobar. If not, see <https://www.gnu.org/licenses/>.
 */

package net.wf0b.code;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    /**
     * The Detail Test - with "detail" and "MnConvention" tags.
     */
    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("detail")
    @Tag("Transaction")
    @Test
    public @interface DetailTest {
    }

    @DetailTest
    void main() throws IOException {
        Transaction.main(new String[] {});
    }
}