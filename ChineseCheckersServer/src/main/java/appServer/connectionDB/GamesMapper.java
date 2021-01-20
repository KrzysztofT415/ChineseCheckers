package appServer.connectionDB;

import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GamesMapper implements RowMapper<GameIdEntry> {
    public GameIdEntry mapRow(ResultSet rs, int rowNum) throws SQLException {
        GameIdEntry gameIdEntry = new GameIdEntry();
        gameIdEntry.setGameId(rs.getInt("gameId"));
        gameIdEntry.setHostIp(rs.getString("hostIp"));
        return gameIdEntry;
    }
}
