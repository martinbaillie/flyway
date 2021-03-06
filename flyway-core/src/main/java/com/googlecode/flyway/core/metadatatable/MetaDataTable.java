/**
 * Copyright (C) 2010-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.googlecode.flyway.core.metadatatable;

import com.googlecode.flyway.core.api.MigrationVersion;
import com.googlecode.flyway.core.dbsupport.Schema;

import java.util.List;

/**
 * The metadata table used to track all applied migrations.
 */
public interface MetaDataTable {
    /**
     * Acquires an exclusive read-write lock on the metadata table. This lock will be released automatically on commit.
     */
    void lock();

    /**
     * Adds this migration as executed to the metadata table.
     *
     * @param appliedMigration The migration that was executed.
     */
    void addAppliedMigration(AppliedMigration appliedMigration);

    /**
     * Checks whether the metadata table contains at least one applied migration.
     *
     * @return {@code true} if it does, {@code false} if it doesn't.
     */
    boolean hasAppliedMigrations();

    /**
     * @return The list of all migrations applied on the schema (oldest first). An empty list if no migration has been
     *         applied so far.
     */
    List<AppliedMigration> allAppliedMigrations();

    /**
     * @return The current version of the schema. {@code MigrationVersion.EMPTY} for an empty schema.
     */
    MigrationVersion getCurrentSchemaVersion();

    /**
     * Creates and initializes the Flyway metadata table.
     *
     * @param initVersion             The version to tag an existing schema with when executing init.
     * @param initDescription         The description to tag an existing schema with when executing init.
     */
    void init(MigrationVersion initVersion, String initDescription);

    /**
     * Checks whether the metadata table contains a marker row for schema init.
     *
     * @return {@code true} if it does, {@code false} if it doesn't.
     */
    boolean hasInitMarker();

    /**
     * <p>
     * Repairs the metadata table after a failed migration.
     * This is only necessary for databases without DDL-transaction support.
     * </p>
     * <p>
     * On databases with DDL transaction support, a migration failure automatically triggers a rollback of all changes,
     * including the ones in the metadata table.
     * </p>
     */
    void repair();

    /**
     * Indicates in the metadata table that Flyway created these schemas.
     *
     * @param schemas The schemas that were created by Flyway.
     */
    void addSchemasMarker(Schema[] schemas);

    /**
     * Checks whether the metadata table contains a marker row for schema creation.
     *
     * @return {@code true} if it does, {@code false} if it doesn't.
     */
    boolean hasSchemasMarker();
}
