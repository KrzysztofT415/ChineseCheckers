package appServer.connectionDB;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class GameJDBCTemplate {
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplateObject;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplateObject = new JdbcTemplate(dataSource);
    }

    public void save(Integer gameId, Integer playerId, Integer x1, Integer y1, Integer x2, Integer y2) {
        String SQL = "insert into moves (gameId, playerId, x1, y1, x2, y2) values (?, ?, ?, ?, ?, ?)";
        jdbcTemplateObject.update(SQL, gameId, playerId, x1, y1, x2, y2);
    }

    public void addNewGame(String hostIp) {
        String SQL = "insert into games (hostIp) values (?)";
        jdbcTemplateObject.update(SQL, hostIp);
    }

    public GameIdEntry getNewGameId(String hostIp) {
        String SQL = "select * from games where hostIp = ? order by gameId desc limit 1";
        return jdbcTemplateObject.queryForObject(SQL, new Object[]{hostIp}, new GamesMapper());
    }

    public List<GameEntry> getGameInformation(Integer gameId) {
        String SQL = "select * from moves where gameId = ? order by moveId asc";
        return jdbcTemplateObject.query(SQL, new Object[]{gameId}, new MovesMapper());
    }
}
