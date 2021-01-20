package appServer.connectionDB;

import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MovesMapper implements RowMapper<GameEntry> {
    public GameEntry mapRow(ResultSet rs, int rowNum) throws SQLException {
        GameEntry gameEntry = new GameEntry();
        gameEntry.setMoveId(rs.getInt("moveId"));
        gameEntry.setGameId(rs.getInt("gameId"));
        gameEntry.setPlayerId(rs.getInt("playerId"));
        gameEntry.setX1(rs.getInt("x1"));
        gameEntry.setY1(rs.getInt("y1"));
        gameEntry.setX2(rs.getInt("x2"));
        gameEntry.setY2(rs.getInt("y2"));
        return gameEntry;
    }
}
