/*
 * This file is generated by jOOQ.
 */
package io.lumine.achievements.storage.sql.jooq.tables;


import io.lumine.achievements.storage.sql.jooq.DefaultSchema;
import io.lumine.achievements.storage.sql.jooq.Keys;
import io.lumine.achievements.storage.sql.jooq.tables.records.ProfileProgressRecord;

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
public class MythicachievementsProfileProgress extends TableImpl<ProfileProgressRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of
     * <code>mythicachievements_profile_progress</code>
     */
    public static final MythicachievementsProfileProgress MYTHICACHIEVEMENTS_PROFILE_PROGRESS = new MythicachievementsProfileProgress();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<ProfileProgressRecord> getRecordType() {
        return ProfileProgressRecord.class;
    }

    /**
     * The column <code>mythicachievements_profile_progress.profile_uuid</code>.
     */
    public final TableField<ProfileProgressRecord, String> PROFILE_UUID = createField(DSL.name("profile_uuid"), SQLDataType.CHAR(36).nullable(false), this, "");

    /**
     * The column <code>mythicachievements_profile_progress.achievement</code>.
     */
    public final TableField<ProfileProgressRecord, String> ACHIEVEMENT = createField(DSL.name("achievement"), SQLDataType.VARCHAR(64).nullable(false), this, "");

    /**
     * The column <code>mythicachievements_profile_progress.criteria</code>.
     */
    public final TableField<ProfileProgressRecord, String> CRITERIA = createField(DSL.name("criteria"), SQLDataType.VARCHAR(64).nullable(false), this, "");

    /**
     * The column <code>mythicachievements_profile_progress.progress</code>.
     */
    public final TableField<ProfileProgressRecord, Integer> PROGRESS = createField(DSL.name("progress"), SQLDataType.INTEGER.defaultValue(DSL.inline("1", SQLDataType.INTEGER)), this, "");

    private MythicachievementsProfileProgress(Name alias, Table<ProfileProgressRecord> aliased) {
        this(alias, aliased, null);
    }

    private MythicachievementsProfileProgress(Name alias, Table<ProfileProgressRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>mythicachievements_profile_progress</code> table
     * reference
     */
    public MythicachievementsProfileProgress(String alias) {
        this(DSL.name(alias), MYTHICACHIEVEMENTS_PROFILE_PROGRESS);
    }

    /**
     * Create an aliased <code>mythicachievements_profile_progress</code> table
     * reference
     */
    public MythicachievementsProfileProgress(Name alias) {
        this(alias, MYTHICACHIEVEMENTS_PROFILE_PROGRESS);
    }

    /**
     * Create a <code>mythicachievements_profile_progress</code> table reference
     */
    public MythicachievementsProfileProgress() {
        this(DSL.name("mythicachievements_profile_progress"), null);
    }

    public <O extends Record> MythicachievementsProfileProgress(Table<O> child, ForeignKey<O, ProfileProgressRecord> key) {
        super(child, key, MYTHICACHIEVEMENTS_PROFILE_PROGRESS);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : DefaultSchema.DEFAULT_SCHEMA;
    }

    @Override
    public UniqueKey<ProfileProgressRecord> getPrimaryKey() {
        return Keys.KEY_MYTHICACHIEVEMENTS_PROFILE_PROGRESS_PRIMARY;
    }

    @Override
    public List<ForeignKey<ProfileProgressRecord, ?>> getReferences() {
        return Arrays.asList(Keys.MYTHICACHIEVEMENTS_PROFILE_PROGRESS_UUID_FK);
    }

    private transient MythicachievementsProfile _mythicachievementsProfile;

    /**
     * Get the implicit join path to the
     * <code>mythiccraft_test_achievements.mythicachievements_profile</code>
     * table.
     */
    public MythicachievementsProfile mythicachievementsProfile() {
        if (_mythicachievementsProfile == null)
            _mythicachievementsProfile = new MythicachievementsProfile(this, Keys.MYTHICACHIEVEMENTS_PROFILE_PROGRESS_UUID_FK);

        return _mythicachievementsProfile;
    }

    @Override
    public MythicachievementsProfileProgress as(String alias) {
        return new MythicachievementsProfileProgress(DSL.name(alias), this);
    }

    @Override
    public MythicachievementsProfileProgress as(Name alias) {
        return new MythicachievementsProfileProgress(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public MythicachievementsProfileProgress rename(String name) {
        return new MythicachievementsProfileProgress(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public MythicachievementsProfileProgress rename(Name name) {
        return new MythicachievementsProfileProgress(name, null);
    }

    // -------------------------------------------------------------------------
    // Row4 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row4<String, String, String, Integer> fieldsRow() {
        return (Row4) super.fieldsRow();
    }
}
