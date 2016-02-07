/*
 * Copyright (C) 2013 Blandware (http://www.blandware.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.apchernykh.todo.atleap.sample.provider;

import com.apchernykh.todo.atleap.sample.model.Item;
import com.blandware.android.atleap.provider.ormlite.OrmLiteUriMatcher;
import com.apchernykh.todo.atleap.sample.model.User;

public class DefaultUriMatcher extends OrmLiteUriMatcher {
    public DefaultUriMatcher(String authority) {
        super(authority);
    }

    @Override
    public void instantiate() {
        addClass(DefaultContract.PATH_USERS, User.class);
        addClass(DefaultContract.PATH_USER, User.class);

        addClass(Item.ItemsResult.class, DefaultContract.PATH_ITEMS);
        addClass(DefaultContract.PATH_ITEM, Item.class);
        addClass(DefaultContract.PATH_ITEMS, Item.class);
    }
}
