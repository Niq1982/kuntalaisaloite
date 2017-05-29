-- Migrate old "verified participants" to verified_participant -table
insert into verified_participant
(initiative_id,
 verified_user_id,
 participate_time,
 show_name,
 verified,
 membership_type,
 municipality_id)
     select
            p.municipality_initiative_id,
            vu.verified_user,
            p.participate_time,
            p.show_name,
            coalesce(vu.verified, false) verified,
            p.membership_type,
            p.municipality_id
      from verified_user_normal_initiatives vu
      inner join participant p on (p.id = vu.participant);

-- Delete old participants
delete from participant where id in
                              (select participant from verified_user_normal_initiatives);

-- Drop the old verified participant mapping table
drop table verified_user_normal_initiatives;