/*
 * This file is generated by jOOQ.
 */
package io.lumine.achievements.storage.sql.jooq.tables;


import io.lumine.achievements.storage.sql.jooq.DefaultSchema;
import io.lumine.achievements.storage.sql.jooq.Keys;
import io.lumine.achievements.storage.sql.jooq.tables.records.ProfileCompletedRecord;

import java.util.Arrays;
import java.util.List;

import io.lumine.mythic.bukkit.utils.lib.jooq.Field;
import io.lumine.mythic.bukkit.utils.lib.jooq.ForeignKey;
import io.lumine.mythic.bukkit.utils.lib.jooq.Name;
import io.lumine.mythic.bukkit.utils.lib.jooq.Record;
import io.lumine.mythic.bukkit.utils.lib.jooq.Row4;
import io.lumine.mythic.bukkit.utils.lib.jooq.Schema;
import io.lumine.mythic.bukkit.utils.lib.jooq.Table;
import io.lumine.mythic.bukkit.utils.lib.jooq.TableField;
import io.lumine.mythic.bukkit.utils.lib.jooq.TableOptions;
import io.lumine.mythic.bukkit.utils.lib.jooq.UniqueKey;
import io.lumine.mythic.bukkit.utils.lib.jooq.impl.DSL;
import io.lumine.mythic.bukkit.utils.lib.jooq.impl.SQLDataType;
import io.lumine.mythic.bukkit.utils.lib.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class MythicachievementsProfileCompleted extends TableImpl<ProfileCompletedRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of
     * <code>mythicachievements_profile_completed</code>
     */
    public static final MythicachievementsProfileCompleted MYTHICACHIEVEMENTS_PROFILE_COMPLETED = new MythicachievementsProfileCompleted();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<ProfileCompletedRecord> getRecordType() {
        return ProfileCompletedRecord.class;
    }

    /**
     * The column
     * <code>mythicachievements_profile_completed.profile_uuid</code>.
     */
    public final TableField<ProfileCompletedRecord, String> PROFILE_UUID = createField(DSL.name("profile_uuid"), SQLDataType.CHAR(36).nullable(false), this, "");

    /**
     * The column <code>mythicachievements_profile_completed.achievement</code>.
     */
    public final TableField<ProfileCompletedRecord, String> ACHIEVEMENT = createField(DSL.name("achievement"), SQLDataType.VARCHAR(64).nullable(false), this, "");

    /**
     * The column
     * <code>mythicachievements_profile_completed.completed_time</code>.
     */
    public final TableField<ProfileCompletedRecord, Long> COMPLETED_TIME = createField(DSL.name("completed_time"), SQLDataType.BIGINT.defaultValue(DSL.inline("NULL", SQLDataType.BIGINT)), this, "");

    /**
     * The column
     * <code>mythicachievements_profile_completed.collected_loot</code>.
     */
    public final TableField<ProfileCompletedRecord, Byte> COLLECTED_LOOT = createField(DSL.name("collected_loot"), SQLDataType.TINYINT.defaultValue(DSL.inline("0", SQLDataType.TINYINT)), this, "");

    private MythicachievementsProfileCompleted(Name alias, Table<ProfileCompletedRecord> aliased) {
        this(alias, aliased, null);
    }

    private MythicachievementsProfileCompleted(Name alias, Table<ProfileCompletedRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>mythicachievements_profile_completed</code> table
     * reference
     */
    public MythicachievementsProfileCompleted(String alias) {
        this(DSL.name(alias), MYTHICACHIEVEMENTS_PROFILE_COMPLETED);
    }

    /**
     * Create an aliased <code>mythicachievements_profile_completed</code> table
     * reference
     */
    public MythicachievementsProfileCompleted(Name alias) {
        this(alias, MYTHICACHIEVEMENTS_PROFILE_COMPLETED);
    }

    /**
     * Create a <code>mythicachievements_profile_completed</code> table
     * reference
     */
    public MythicachievementsProfileCompleted() {
        this(DSL.name("mythicachievements_profile_completed"), null);
    }

    public <O extends Record> MythicachievementsProfileCompleted(Table<O> child, ForeignKey<O, ProfileCompletedRecord> key) {
        super(child, key, MYTHICACHIEVEMENTS_PROFILE_COMPLETED);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : DefaultSchema.DEFAULT_SCHEMA;
    }

    @Override
    public UniqueKey<ProfileCompletedRecord> getPrimaryKey() {
        return Keys.KEY_MYTHICACHIEVEMENTS_PROFILE_COMPLETED_PRIMARY;
    }

    @Override
    public List<ForeignKey<ProfileCompletedRecord, ?>> getReferences() {
        return Arrays.asList(Keys.MYTHICACHIEVEMENTS_PROFILE_COMPLETED_FK);
    }

    private transient MythicachievementsProfile _mythicachievementsProfile;

    /**
     * Get the implicit join path to the
     * <code>mythiccraft_test_achievements.mythicachievements_profile</code>
     * table.
     */
    public MythicachievementsProfile mythicachievementsProfile() {
        if (_mythicachievementsProfile == null)
            _mythicachievementsProfile = new MythicachievementsProfile(this, Keys.MYTHICACHIEVEMENTS_PROFILE_COMPLETED_FK);

        return _mythicachievementsProfile;
    }

    @Override
    public MythicachievementsProfileCompleted as(String alias) {
        return new MythicachievementsProfileCompleted(DSL.name(alias), this);
    }

    @Override
    public MythicachievementsProfileCompleted as(Name alias) {
        return new MythicachievementsProfileCompleted(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public MythicachievementsProfileCompleted rename(String name) {
        return new MythicachievementsProfileCompleted(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public MythicachievementsProfileCompleted rename(Name name) {
        return new MythicachievementsProfileCompleted(name, null);
    }

    // -------------------------------------------------------------------------
    // Row4 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row4<String, String, Long, Byte> fieldsRow() {
        return (Row4) super.fieldsRow();
    }
}
