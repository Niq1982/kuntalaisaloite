-- Add new "name" field for verified_participant (we want the participants name to be what it was when he/she participated)
ALTER TABLE verified_participant
  ADD COLUMN name VARCHAR(100);

-- Migrate old "verified participants" to verified_participant -table
insert into verified_participant
(initiative_id,
 verified_user_id,
 participate_time,
 show_name,
 verified,
 membership_type,
 municipality_id,
 name)
     select
            p.municipality_initiative_id,
            vu.verified_user,
            p.participate_time,
            p.show_name,
            coalesce(vu.verified, false) verified,
            p.membership_type,
            p.municipality_id,
            p.name
      from verified_user_normal_initiatives vu
      inner join participant p on (p.id = vu.participant);

-- Delete old participants
delete from participant where id in
                              (select participant from verified_user_normal_initiatives);

-- Drop the old verified participant mapping table
drop table verified_user_normal_initiatives;

-- Copy missing names for old verified_participants from verified_user -table
update verified_participant
set name = subselect.name
from (select vu.name as name,
             vp.verified_user_id as verified_user_id,
             vp.initiative_id initiative_id
      from verified_participant vp
        inner join verified_user vu on (vu.id = vp.verified_user_id)
      where vp.name is null) as subselect
where verified_participant.verified_user_id = subselect.verified_user_id
      and verified_participant.initiative_id = subselect.initiative_id;

alter table verified_participant alter column name set not null;