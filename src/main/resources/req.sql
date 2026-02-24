--1) countAllVotes
select count(id) AS total_votes from vote;

--2)countVotesByType
select vote_type, COUNT(id)
FROM vote
GROUP BY vote_type
ORDER BY vote_type;

--3)countValidVotesByCandidate

select * from vote;

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

--4) computeVoteSummary
select
    count(case when vote_type = 'VALID' then 1 end) as valid_count,
    count(case when vote_type = 'BLANK' then 1 end) as blank_count,
    count(case when vote_type = 'NULL' then 1 end) as null_count
from vote;

--5)computeTurnoutRate
select ((select count(distinct voter_id) from vote) / (select count(id) from voter)) * 100 as turnout_rate;

--6)findWinner
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