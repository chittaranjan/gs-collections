/*
 * Copyright 2011 Goldman Sachs.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gs.collections.impl.set.sorted.mutable;

import com.gs.collections.impl.factory.SortedSets;
import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class UnmodifiableSortedSetTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEBjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5zZXQuc29ydGVkLm11dGFibGUuVW5tb2Rp\n"
                        + "ZmlhYmxlU29ydGVkU2V0AAAAAAAAAAECAAB4cgBIY29tLmdzLmNvbGxlY3Rpb25zLmltcGwuY29s\n"
                        + "bGVjdGlvbi5tdXRhYmxlLlVubW9kaWZpYWJsZU11dGFibGVDb2xsZWN0aW9uAAAAAAAAAAECAAFM\n"
                        + "AApjb2xsZWN0aW9udAA1TGNvbS9ncy9jb2xsZWN0aW9ucy9hcGkvY29sbGVjdGlvbi9NdXRhYmxl\n"
                        + "Q29sbGVjdGlvbjt4cHNyADhjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5zZXQuc29ydGVkLm11dGFi\n"
                        + "bGUuVHJlZVNvcnRlZFNldAAAAAAAAAABDAAAeHBwdwQAAAAAeA==",
                UnmodifiableSortedSet.of(SortedSets.mutable.of()));
    }
}
