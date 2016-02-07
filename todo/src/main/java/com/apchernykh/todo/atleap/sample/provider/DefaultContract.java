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

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class DefaultContract {

    private DefaultContract() {}

    public static final String CONTENT_AUTHORITY = "com.apchernykh.todo.atleap.sample.authority";

    private static final Uri BASE_CONTENT_URI = new Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT).authority(CONTENT_AUTHORITY).build();

    public static final String PATH_USERS = "users";
    public static final String PATH_USER = "users/#";
    public static final String PATH_ITEMS = "items";
    public static final String PATH_ITEM = "item/*";
    private static final String PATH_ITEM_MATCH = "item"; //seems to be a hack, since contentprovider refuses to recognize UUIDs, it distinguishes only numeric IDs

    @SuppressWarnings("unused")
    public static class User implements BaseColumns {
        public static final String TABLE = "user";
        public static final String LOGIN = "login";
        public static final String AVATAR_URL = "avatar_url";
        public static final String NAME = "name";
        public static final String COMPANY = "company";
        public static final String EMAIL = "email";
        }


    public static class Item implements BaseColumns {

        public static Uri getItemUri(String itemUuid) {
            return BASE_CONTENT_URI.buildUpon().appendPath(PATH_ITEM_MATCH).appendPath(itemUuid).build();
        }

        public static final String TABLE = "item";
        public static final String PRIORITY = "priority";
        public static final String TITLE = "title";
        public static final String DESCRIPTION = "description";
        public static final String CREATED_AT = "created_at";
        public static final String ICON_URI = "icon_uri";
    }

    public static final Uri CONTENT_URI_ITEMS =
            BASE_CONTENT_URI.buildUpon().appendPath(PATH_ITEMS).build();

}
