import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DataRetriever {

    public long countAllVotes() {
        long count = 0;
        String query = """
                select count(id) AS total_votes from vote;
                """;
        try (
                Connection conn = new DBConnection().getConnection();
                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();
        ) {

            if(rs.next()) {
                count = rs.getLong("total_votes");
            }

        } catch (Exception e) {
            throw new  RuntimeException(e);
        } finally {
            return count;
        }
    }


}