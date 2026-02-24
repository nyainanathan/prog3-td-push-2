import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

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

    List<VoteTypeCount> countVotesByType(){
        List<VoteTypeCount> voteTypeCounts = new ArrayList<>();
        String query = """
                select vote_type, COUNT(id)
                        FROM vote
                        GROUP BY vote_type
                        ORDER BY vote_type;
                
        """;
        try(
                Connection conn = new DBConnection().getConnection();
                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();
                ){
            while(rs.next()) {
                VoteTypeCount vote = new VoteTypeCount();
                vote.setVoteType(VoteType.valueOf(rs.getString("vote_type")));
                vote.setCount(rs.getInt("count"));
                voteTypeCounts.add(vote);
            }
        } catch (Exception e){
            throw new  RuntimeException(e);
        } finally {
            return voteTypeCounts;
        }
    }

    List<CandidateVoteCount> countValidVotesByCandidate(){
        List<CandidateVoteCount> candidateVoteCounts = new ArrayList<>();
        String query = """
                        select c.name as candidate_name, count(
                                       case
                                            when v.vote_type = 'VALID' then 1
                                            end
                                       ) AS valid_vote
                        from vote v
                        right join candidate c
                        ON v.candidate_id = c.id
                        GROUP BY c.name
                        ORDER BY valid_vote desc
                        ;
        """;

        try(
                Connection conn = new DBConnection().getConnection();
                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();
                ){
            while(rs.next()) {
                CandidateVoteCount candidateVote = new CandidateVoteCount();
                candidateVote.setCandidateName(rs.getString("candidate_name"));
                candidateVote.setValidVotesCount(rs.getInt("valid_votes"));
                candidateVoteCounts.add(candidateVote);
            }
        } catch (Exception e){
            throw new  RuntimeException(e);
        } finally {
            return candidateVoteCounts;
        }
    }
}