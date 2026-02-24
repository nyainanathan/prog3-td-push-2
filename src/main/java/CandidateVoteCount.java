public class CandidateVoteCount {
    private String candidateName;
    private int validVotesCount;

    public CandidateVoteCount(String candidateName, int validVotesCount) {
    this.candidateName = candidateName;
    this.validVotesCount = validVotesCount;
    }

    public CandidateVoteCount() {
    }

    public String getCandidateName() {
        return candidateName;
    }

    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
    }

    public int getValidVotesCount() {
        return validVotesCount;
    }

    public void setValidVotesCount(int validVotesCount) {
        this.validVotesCount = validVotesCount;
    }
}
