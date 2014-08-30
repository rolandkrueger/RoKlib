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

import org.roklib.util.helper.CheckForNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @since 2.0
 */
public class URIActionMapperTree {

    private URIActionDispatcher dispatcher;

    private URIActionMapperTree() {
        dispatcher = new URIActionDispatcher(false);
    }

    public static URIActionMapperTreeBuilder create() {
        return new URIActionMapperTreeBuilder();
    }

    public static URIPathSegmentActionMapperBuilder pathSegment(String segment) {
        return new URIPathSegmentActionMapperBuilder(segment);
    }

    public static URIActionCommandBuilder action(final AbstractURIActionCommand command) {
        return new URIActionCommandBuilder();
    }

    public static SubtreeActionMapperBuilder subtree() {
        return null;
    }

    public Collection<AbstractURIPathSegmentActionMapper> getRootPathSegmentActionMappers() {
        return dispatcher.getRootActionHandler().getSubMapperMap().values(); // TODO: refactor
    }

    public static class URIActionMapperTreeBuilder {
        private List<URIPathSegmentActionMapperBuilder> builders = new ArrayList<>();

        public URIActionMapperTree build() {
            return addMappersFromBuilderToMapperTreeRoot(new URIActionMapperTree());
        }

        private URIActionMapperTree addMappersFromBuilderToMapperTreeRoot(final URIActionMapperTree uriActionMapperTree) {
            for (URIPathSegmentActionMapperBuilder builder : builders) {
                uriActionMapperTree.dispatcher.addURIPathSegmentMapper(builder.getMapper());
            }
            return uriActionMapperTree;
        }

        public URIActionMapperTreeBuilder map(URIPathSegmentActionMapperBuilder pathSegmentBuilder) {
            builders.add(pathSegmentBuilder);
            return this;
        }
    }

    public static class URIPathSegmentActionMapperBuilder {
        private String segmentName;
        private AbstractURIPathSegmentActionMapper mapper;

        public URIPathSegmentActionMapperBuilder(final String segmentName) {
            CheckForNull.check(segmentName);
            this.segmentName = segmentName;
        }

        public URIPathSegmentActionMapperBuilder on(final URIActionCommandBuilder actionBuilder) {
            mapper = new SimpleURIPathSegmentActionMapper(segmentName);
            return this;
        }

        public URIPathSegmentActionMapperBuilder on(final SubtreeActionMapperBuilder subtreeBuilder) {
            mapper = new DispatchingURIPathSegmentActionMapper(segmentName);
            return this;
        }

        public AbstractURIPathSegmentActionMapper getMapper() {
            return mapper;
        }
    }

    public static class URIActionCommandBuilder {
    }

    public static class SubtreeActionMapperBuilder {
    }
}
