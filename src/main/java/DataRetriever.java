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

    public List<VoteTypeCount> countVotesByType(){
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

    public List<CandidateVoteCount> countValidVotesByCandidate(){
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
                candidateVote.setValidVotesCount(rs.getInt("valid_vote"));
                candidateVoteCounts.add(candidateVote);
            }
        } catch (Exception e){
            throw new  RuntimeException(e);
        } finally {
            return candidateVoteCounts;
        }
    }

    public VoteSummary computeVoteSummary(){
        VoteSummary voteSummary = new VoteSummary();
        String query = """
                select
                    count(case when vote_type = 'VALID' then 1 end) as valid_count,
                    count(case when vote_type = 'BLANK' then 1 end) as blank_count,
                    count(case when vote_type = 'NULL' then 1 end) as null_count
                from vote;
                """;
        try(
                Connection conn = new DBConnection().getConnection();
                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();
                ){
            if(rs.next()) {
                voteSummary.setValidCount(rs.getInt("valid_count"));
                voteSummary.setBlankCount(rs.getInt("blank_count"));
                voteSummary.setNullCount(rs.getInt("null_count"));
            }
        } catch (Exception e){
            throw new  RuntimeException(e);
        } finally {
            return voteSummary;
        }
    }

    public double computeTurnoutRate(){
        double result = 0;
        String query = """
                select ((select count(distinct voter_id) from vote) / (select count(id) from voter)) * 100 as turnout_rate;
        """;
        try(
                Connection conn = new DBConnection().getConnection();
                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();
                ){
                    if(rs.next()) result = rs.getDouble("turnout_rate");
        } catch (Exception e){
            throw new  RuntimeException(e);
        } finally {
            return result;
        }
    }

    public ElectionResult findWinner(){
        ElectionResult result = new ElectionResult();
        String query = """
                select c.name as candidate_name,
                       count(
                        case
                            when v.vote_type = 'VALID' then 1
                            end
                        ) AS valid_vote_count
                from vote v
                join candidate c ON v.candidate_id = c.id
                GROUP BY c.name
                ORDER BY valid_vote_count desc
                LIMIT 1;
                """;

        try(
                Connection conn = new DBConnection().getConnection();
                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();
                ) {
            if(rs.next()) {
                result.setCandidateName(rs.getString("candidate_name"));
                result.setValidVoteCount(rs.getInt("valid_vote_count"));
            }
        } catch (Exception e){
            throw new  RuntimeException(e);
        } finally {
            return result;
        }
    }
}