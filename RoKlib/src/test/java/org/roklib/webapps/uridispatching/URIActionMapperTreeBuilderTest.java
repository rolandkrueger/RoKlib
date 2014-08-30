/*
 * Copyright (C) 2007 - 2014 Roland Krueger
 *
 * Author: Roland Krueger (www.rolandkrueger.info)
 *
 * This file is part of RoKlib.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.roklib.webapps.uridispatching;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.roklib.webapps.uridispatching.URIActionMapperTree.*;

@RunWith(MockitoJUnitRunner.class)
public class URIActionMapperTreeBuilderTest {

    @Mock
    private AbstractURIActionCommand homeCommandMock;
    @Mock
    private AbstractURIActionCommand adminCommandMock;

    @Test
    public void test_build_gives_empty_mapper_tree() {
        final URIActionMapperTree mapperTree = create().build();

        assertThat(mapperTree, notNullValue());
        assert_number_of_root_path_segment_mappers(mapperTree, 0);
    }

    @Test
    public void test_add_two_action_mapper_to_root() {
        // @formatter:off
        final URIActionMapperTree mapperTree = create().map(
                    pathSegment("home").on(action(homeCommandMock)))
                .map(
                    pathSegment("admin").on(action(adminCommandMock))
                )
           .build();
        // @formatter:on

        assert_number_of_root_path_segment_mappers(mapperTree, 2);
        assert_that_mapper_is_correct(mapperTree.getRootActionMapper("home"), "home", SimpleURIPathSegmentActionMapper.class, homeCommandMock);
        assert_that_mapper_is_correct(mapperTree.getRootActionMapper("admin"), "admin", SimpleURIPathSegmentActionMapper.class, adminCommandMock);
    }

    private void assert_that_mapper_is_correct(final AbstractURIPathSegmentActionMapper actualMapper, String expectedSegmentName, Class<?> expectedClass, AbstractURIActionCommand expectedCommand) {
        assertThat(actualMapper, instanceOf(expectedClass));
        assertThat(actualMapper.getActionName(), equalTo(expectedSegmentName));
        assertThat(actualMapper.getActionCommand(), equalTo(expectedCommand));
    }

    private void assert_number_of_root_path_segment_mappers(final URIActionMapperTree mapperTree, final int number) {
        assertThat(mapperTree.getRootActionMappers(), hasSize(number));
    }
}