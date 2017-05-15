update verified_participant
set municipality_id = subselect.municipality_id
from (select i.municipality_id as municipality_id,
        vp.verified_user_id as verified_user_id,
        vp.initiative_id initiative_id
      from verified_participant vp
        inner join municipality_initiative i on (i.id = vp.initiative_id)
      where vp.municipality_id is null) as subselect
where verified_participant.verified_user_id = subselect.verified_user_id
      and verified_participant.initiative_id = subselect.initiative_id;