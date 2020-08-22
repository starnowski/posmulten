package com.github.starnowski.posmulten.postgresql.core.context;

import java.util.Objects;
import java.util.Set;

public class SameTenantConstraintForForeignKey {

    private final TableKey mainTable;
    private final TableKey foreignKeyTable;
    private final Set<String> foreignKeyColumns;

    public SameTenantConstraintForForeignKey(TableKey mainTable, TableKey foreignKeyTable, Set<String> foreignKeyColumns) {
        this.mainTable = mainTable;
        this.foreignKeyTable = foreignKeyTable;
        this.foreignKeyColumns = foreignKeyColumns;
    }

    public TableKey getMainTable() {
        return mainTable;
    }

    public TableKey getForeignKeyTable() {
        return foreignKeyTable;
    }

    public Set<String> getForeignKeyColumns() {
        return foreignKeyColumns;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SameTenantConstraintForForeignKey that = (SameTenantConstraintForForeignKey) o;
        return Objects.equals(mainTable, that.mainTable) &&
                Objects.equals(foreignKeyTable, that.foreignKeyTable) &&
                Objects.equals(foreignKeyColumns, that.foreignKeyColumns);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mainTable, foreignKeyTable, foreignKeyColumns);
    }
}