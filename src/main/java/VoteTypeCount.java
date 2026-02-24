public class VoteTypeCount {
    private VoteType voteType;
    private int count;

    public VoteTypeCount(VoteType voteType, int count) {
        this.voteType = voteType;
        this.count = count;
    }

    public VoteTypeCount() {

    }

    public VoteType getVoteType() {
        return voteType;
    }

    public void setVoteType(VoteType voteType) {
        this.voteType = voteType;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "VoteTypeCount{" +
                "voteType=" + voteType +
                ", count=" + count +
                '}';
    }
}
