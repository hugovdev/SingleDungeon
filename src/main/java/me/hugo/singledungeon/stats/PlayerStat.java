package me.hugo.singledungeon.stats;

public enum PlayerStat {

    MOB_KILLS("mob_kills"),
    SESSIONS("sessions"),
    AVERAGE_KILLS("average_kills"),
    DEATHS("deaths");

    private final String databaseName;

    PlayerStat(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getDatabaseName() {
        return databaseName;
    }
}
