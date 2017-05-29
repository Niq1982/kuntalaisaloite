ALTER TABLE municipality_initiative
  ADD COLUMN participant_count_citizen INTEGER NOT NULL DEFAULT 0;

-- Denormalize from verified participants
update municipality_initiative
  set participant_count_citizen = subselect.c
from
  (select count(*) as c,
     initiative_id as initiative_id
   from verified_participant vp inner join municipality_initiative i on (i.id = vp.initiative_id)
   where i.municipality_id = vp.municipality_id group by initiative_id) as subselect
where
  municipality_initiative.id = subselect.initiative_id;

-- Denormalize from normal participants
update municipality_initiative
set participant_count_citizen = participant_count_citizen + subselect.c
from
  (select count(*) as c,
          municipality_initiative_id as initiative_id
   from participant vp inner join municipality_initiative i on (i.id = vp.municipality_initiative_id)
   where i.municipality_id = vp.municipality_id
     and confirmation_code is null
   group by initiative_id) as subselect
where
  municipality_initiative.id = subselect.initiative_id;