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

package com.apchernykh.todo.atleap.sample.model;

import com.apchernykh.todo.atleap.sample.provider.DefaultContract;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Collection;
import java.util.Date;

@SuppressWarnings("unused")
@JsonIgnoreProperties(ignoreUnknown = true)
@DatabaseTable(tableName = DefaultContract.Item.TABLE)
public class Item {

    @JsonProperty("id")
    @DatabaseField(id = true, columnName = DefaultContract.Item._ID)
    private String id;

    @JsonProperty("title")
    @DatabaseField(columnName = DefaultContract.Item.TITLE)
    public String name;

    @JsonProperty("description")
    @DatabaseField(columnName = DefaultContract.Item.DESCRIPTION)
    public String description;

    @JsonProperty("priority")
    @DatabaseField(columnName = DefaultContract.Item.PRIORITY)
    public int priority;

    @JsonProperty("created_at")
    @DatabaseField(columnName = DefaultContract.Item.CREATED_AT)
    public Date createdAt;

    @JsonProperty("icon_uri")
    @DatabaseField(columnName = DefaultContract.Item.ICON_URI)
    public String icon_uri;

    @DatabaseField(foreign = true)
    private ItemsResult result;

    @SuppressWarnings("unused")
    @JsonIgnoreProperties(ignoreUnknown = true)
    @DatabaseTable(tableName = "item_result")
    public static class ItemsResult {

        @DatabaseField(id = true)
        private int id = 0;

        @JsonProperty("total_count")
        @DatabaseField(columnName = "total_count")
        public int totalCount;

        @ForeignCollectionField(eager = false)
        private Collection<Item> items;

        public Collection<Item> getItems() {
            return items;
        }

        public void setItems(Collection<Item> items) {
            this.items = items;
        }
    }
}

